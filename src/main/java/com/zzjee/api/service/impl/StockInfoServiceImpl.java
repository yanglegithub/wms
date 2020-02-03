package com.zzjee.api.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.zzjee.api.service.StockInfoServiceI;
import com.zzjee.api.utils.HttpReqUtil;
import com.zzjee.api.utils.NCDataObject;
import com.zzjee.ba.entity.BaStoreEntity;
import com.zzjee.md.controller.MdPalletController;
import com.zzjee.md.entity.MdGoodsEntity;
import com.zzjee.md.entity.MdPalletEntity;
import com.zzjee.md.entity.PalletStatus;
import com.zzjee.pm.entity.PmBasicMaterialEntity;
import com.zzjee.pm.entity.PmMaterialListEntity;
import com.zzjee.pm.entity.PmProcTaskEntity;
import com.zzjee.wm.entity.*;
import com.zzjee.wm.page.WmImNoticeHPage;
import com.zzjee.wmutil.WmsContants;

import okhttp3.*;

import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.sms.util.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("stockInfoService")
@Transactional
public class StockInfoServiceImpl extends CommonServiceImpl implements StockInfoServiceI {
	private String token = "";
	private String sysID = ResourceUtil.getConfigByName("wms.nc.sysID");//"wms";
	private String url = ResourceUtil.getConfigByName("wms.nc.url");//"http://192.168.22.222/ZBServlet";

	@Override
	public List<WmOmNoticeIEntity> generateOmIEntitys(WmOmTaskEntity task) {
		List<WmOmNoticeIEntity> resultList = new ArrayList<WmOmNoticeIEntity>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		//        String stock_sql = "SELECT * FROM md_goods WHERE SHP_BIAN_MA = ? ORDER BY SHENG_CHAN_RI_QI ASC ";
		String stock_sql = "SELECT mp.zhuang_liao_bian_ma as 'goodsCode', mp.zhuang_liao_ming_cheng as 'goodsName', wmi.goods_prd_data as 'goodsPrdDate', " +
				"mp.pi_ci_hao as 'goodsBatch', DATE_ADD(wmi.goods_prd_data,INTERVAL wmi.BZHI_QI DAY) as 'shelfLife', GROUP_CONCAT(mp.tuo_pan_bian_ma) as 'codeList', " +
				"SUM(mp.shu_liang) as 'quantity', SUM(mp.zhong_liang) as 'weight', GROUP_CONCAT(mp.bin_bian_ma) as 'binList'" +
				"FROM md_pallet mp LEFT JOIN wm_im_notice_i wmi ON wmi.id = mp.entry_key " +
				"WHERE mp.tuo_pan_zhuang_tai = ? AND mp.zhuang_liao_bian_ma = ? GROUP BY mp.zhuang_liao_bian_ma, mp.pi_ci_hao " +
				"ORDER BY shelfLife,mp.bin_bian_ma ASC,mp.bin_depth DESC";
		List<Map<String,Object>> goodList =  findForJdbc(stock_sql, PalletStatus.IN_SHELF, task.getProductId());
		Double needWeight = Double.parseDouble(task.getOutWeight());
		double weight = 0;
		for (Map<String,Object> good : goodList){
			if(weight + (Double) good.get("weight") < needWeight){
				WmOmNoticeIEntity temp = new WmOmNoticeIEntity();
				temp.setGoodsId(task.getProductId());
				temp.setGoodsName(task.getProductName());
				temp.setGoodsUnit((String) good.get("shl_dan_wei"));
				temp.setGoodsWeight(good.get("weight").toString());
				temp.setGoodsQua(good.get("quantity").toString());
				temp.setBaseGoodscount(good.get("quantity").toString());
				temp.setGoodsQuaok("0");
				temp.setWeightUnit("KG");
				temp.setBinOm((String) good.get("binList"));
				temp.setBinId((String)good.get("codeList"));
				temp.setGoodsProData((Date) good.get("goodsPrdDate"));
				temp.setGoodsBatch((String) good.get("goodsBatch"));
				temp.setDelvData(format.format(task.getOutDate()));
				temp.setOmSta(Constants.wm_sta1);
				temp.setPlanSta("I");
				resultList.add(temp);
			} else {
				WmOmNoticeIEntity temp = new WmOmNoticeIEntity();
				temp.setGoodsId(task.getProductId());
				temp.setGoodsName(task.getProductName());
				temp.setGoodsUnit((String) good.get("shl_dan_wei"));
				temp.setGoodsWeight(String.valueOf(needWeight - weight));
				long wehas = ((Double) good.get("quantity")).longValue();
				double scale = (needWeight - weight)/Double.valueOf((Double) good.get("weight")) ;
				long need = (long) (wehas * scale);
				temp.setGoodsQua(String.valueOf(need));
				temp.setGoodsQuaok("0");
				temp.setWeightUnit("KG");
				
				String hql = "from MdPalletEntity mp where mp.tuoPanZhuangTai = ? AND mp.zhuangLiaoBianMa = ? and mp.piCiHao=? order by mp.binDepth desc";
				List<MdPalletEntity> mdps = this.findHql(hql, PalletStatus.IN_SHELF, task.getProductId(), (String) good.get("goodsBatch"));
				String binstr = "";
				String tinstr = "";
				Long has = 0L;
				for(MdPalletEntity mp : mdps) {
					has += Long.parseLong(mp.getShuLiang());
					binstr += binstr.equals("")?mp.getBinBianMa():(","+mp.getBinBianMa());
					tinstr += tinstr.equals("")?mp.getTuoPanBianMa():(","+mp.getTuoPanBianMa());
					if(has >= need)
						break;
				}
				
				temp.setBinOm(binstr);
				temp.setBinId(tinstr);
				temp.setGoodsProData((Date) good.get("goodsPrdDate"));
				temp.setGoodsBatch((String) good.get("goodsBatch"));
				temp.setDelvData(format.format(task.getOutDate()));
				temp.setOmSta(Constants.wm_sta1);
				temp.setPlanSta("I");

				/*temp.setGoodsId(task.getProductId());
                temp.setGoodsName(task.getProductName());
                //temp.setGoodsQua();
                temp.setGoodsUnit((String) good.get("shl_dan_wei"));
                temp.setGoodsWeight(String.valueOf(needWeight - weight));
                temp.setWeightUnit("KG");
                temp.setGoodsProData((Date) good.get("sheng_chan_ri_qi"));
                temp.setGoodsBatch((String) good.get("shp_pi_hao"));
                temp.setDelvData(format.format(task.getOutDate()));
                temp.setOmSta("操作中");
                //temp.setBinOm();*/
				resultList.add(temp);
				break;
			}
			weight += Double.parseDouble((String) good.get("weight"));
		}
		return resultList;
	}

	@Override
	public String generateOrderNo() {

		return UUID.randomUUID().toString().replaceAll("-","");
	}

	@Override
	public String getToken() {
		String token_para = "token=&sysID=wms&funCode=getToken";
		String sr_token= HttpReqUtil.sendPost(url,token_para);
		System.out.println(sr_token);
		if(sr_token != null){
			JSONObject token_jo = JSONObject.parseObject(sr_token);
			JSONArray tokenarr = JSONArray.parseArray(token_jo.getString("result"));
			String token = ((JSONObject)tokenarr.get(0)).getString("session");

			return token;
		}
		return null;

	}

	@Override
	public OkHttpClient getOkHttpClient() {
		if(okHttpClient == null){
			return new OkHttpClient().newBuilder().build();
		} else
			return okHttpClient;
	}

	@Override
	public NCDataObject requestMethode(String funCode, String cType, Map<String, Object> params) throws IOException {
		params.put("ctype",cType);
		String para = "token="+ getToken() +"&sysID=wms&funCode="+funCode;
		String paramJson = JSONObject.toJSONString(params);
		try {
			paramJson = URLEncoder.encode(paramJson, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String result = null;
		try {
			result = HttpReqUtil.sendPost(url,para,paramJson);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		NCDataObject resultObj = JSONObject.parseObject(result,NCDataObject.class);
		return resultObj;
	}

	@Override
	public NCDataObject requestMethode(String funCode, String cType, String jsonParams) {
		JSONObject jsonObj = JSONObject.parseObject(jsonParams);
		jsonObj.put("ctype",cType);
		String result = null;
		try {
			String para = "token="+ getToken() +"&sysID="+sysID+"&funCode="+funCode;

			String paramJson = jsonObj.toJSONString();
			paramJson = URLEncoder.encode(paramJson, "UTF-8");
			result = HttpReqUtil.sendPost(url,para,paramJson);
		} catch (UnsupportedEncodingException  e) {
			throw new BusinessException(e.getMessage());
		}
		NCDataObject resultObj = JSONObject.parseObject(result,NCDataObject.class);
		return resultObj;
	}

	@Override
	public String findImNotice(String imKey, String imDate) throws IOException {
		NCDataObject data = null;
		String returnStr = "";
		if(StringUtil.isEmpty(imKey)){
			imKey = "";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Gson gson = new Gson();
		data = requestMethode("alWMSQry","genIn","{\"vbillcode\":\""+imKey+"\",\"dbilldate\":\""+imDate+"\"}");
		if(data == null){
			return null;
		}else{
			for (Map<String, Object> map : (List<Map>)data.getResult()){
				String dataStr = (String) map.get("data");
				List<Map> templist = (List)JSONObject.parseArray(dataStr);
				for (Map<String, Object> temp : templist){
					WmImNoticeHEntity hEntity = new WmImNoticeHEntity();
					hEntity.setNoticeId((String) temp.get("InStorageNo"));
					returnStr += "".equals(returnStr)?hEntity.getNoticeId():(","+hEntity.getNoticeId());
					try {
						hEntity.setImData(format.parse((String) temp.get("storeDate")));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					hEntity.setQcStaff((String) temp.get("QCstaff"));
					hEntity.setNcPkid((String) temp.get("cgeneralhid"));
					hEntity.setOrderTypeCode(WmsContants.IN_MATERIALS);
					if(ResourceUtil.getConfigByName("wms.stock_io_confirm").equals("true"))
						hEntity.setImSta(WmsContants.CONFIRMING);
					else
						hEntity.setImSta(WmsContants.CONFIRMED);
					for(Map<String, Object> iMap : (List<Map>)temp.get("detail")){
						WmImNoticeIEntity iEntity = new WmImNoticeIEntity();
						iEntity.setImNoticeId(hEntity.getNoticeId());
						iEntity.setGoodsCode((String) iMap.get("materialCode"));
						iEntity.setGoodsName((String) iMap.get("maretialName"));
						iEntity.setBarCode(this.findUniqueByProperty(MdGoodsEntity.class,"shpBianMa",iEntity.getGoodsCode()).getShpTiaoMa());
						iEntity.setGoodsBatch((String) iMap.get("materialBatchNo"));
						iEntity.setBzhiQi((String) iMap.get("warranty"));
						try {
							iEntity.setGoodsPrdData(format.parse((String) iMap.get("manufactureDate")));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						iEntity.setGoodsStoDate(hEntity.getImData());
						iEntity.setBinPre("N");
						iEntity.setGoodsWeight((String) iMap.get("storeWeight"));
						iEntity.setGoodsCount((String) iMap.get("storeQuantity"));
						iEntity.setGoodsQmCount("0");
						iEntity.setGoodsWqmCount(iEntity.getGoodsCount());
						iEntity.setShpGuiGe((String) iMap.get("specification"));
						if(hEntity.getSupCode() == null){
							hEntity.setSupName((String) iMap.get("supplierName"));
							hEntity.setSupCode((String) iMap.get("supplierNo"));
						}
						if(ResourceUtil.getConfigByName("wms.stock_io_confirm").equals("true"))
							iEntity.setNoticeiSta(WmsContants.CONFIRMING);
						else
							iEntity.setNoticeiSta(WmsContants.CONFIRMED);
						this.save(iEntity);
					}
					this.save(hEntity);
				}
			}
		}
		return returnStr;
	}

	@Override
	public List<WmImNoticeHPage> findImNoticeFromNC(String imKey, String imDate) throws IOException{
		NCDataObject data = null;
		List<WmImNoticeHPage> returnData = new ArrayList<>();
		if(StringUtil.isEmpty(imKey)){
			imKey = "";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Gson gson = new Gson();
		data = requestMethode("alWMSQry","genIn","{\"vbillcode\":\""+imKey+"\",\"dbilldate\":\""+imDate+"\"}");
		if(data == null){
			return null;
		}else{
			for (Map<String, Object> map : (List<Map>)data.getResult()){
				String dataStr = (String) map.get("data");
				List<Map> templist = (List)JSONObject.parseArray(dataStr);
				for (Map<String, Object> temp : templist){
					WmImNoticeHPage hEntity = new WmImNoticeHPage();
					hEntity.setNoticeId((String) temp.get("InStorageNo"));
					try {
						hEntity.setImData(format.parse((String) temp.get("storeDate")));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					hEntity.setQcStaff((String) temp.get("QCstaff"));
					hEntity.setNcPkid((String) temp.get("cgeneralhid"));
					hEntity.setOrderTypeCode(WmsContants.IN_MATERIALS);
					if(ResourceUtil.getConfigByName("wms.stock_io_confirm").equals("true"))
						hEntity.setImSta(WmsContants.CONFIRMING);
					else
						hEntity.setImSta(WmsContants.CONFIRMED);
					List<WmImNoticeIEntity> iEntities = new ArrayList<>();
					hEntity.setWmImNoticeIList(iEntities);
					for(Map<String, Object> iMap : (List<Map>)temp.get("detail")){
						WmImNoticeIEntity iEntity = new WmImNoticeIEntity();
						iEntity.setImNoticeId(hEntity.getNoticeId());
						iEntity.setGoodsCode((String) iMap.get("materialCode"));
						iEntity.setGoodsName((String) iMap.get("maretialName"));
						iEntity.setBarCode(this.findUniqueByProperty(MdGoodsEntity.class,"shpBianMa",iEntity.getGoodsCode()).getShpTiaoMa());
						iEntity.setGoodsBatch((String) iMap.get("materialBatchNo"));
						iEntity.setBzhiQi((String) iMap.get("warranty"));
						try {
							iEntity.setGoodsPrdData(format.parse((String) iMap.get("manufactureDate")));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						iEntity.setGoodsStoDate(hEntity.getImData());
						iEntity.setBinPre("N");
						iEntity.setGoodsWeight((String) iMap.get("storeWeight"));
						iEntity.setGoodsCount((String) iMap.get("storeQuantity"));
						iEntity.setGoodsQmCount("0");
						iEntity.setGoodsWqmCount(iEntity.getGoodsCount());
						iEntity.setShpGuiGe((String) iMap.get("specification"));
						if(hEntity.getSupCode() == null){
							hEntity.setSupName((String) iMap.get("supplierName"));
							hEntity.setSupCode((String) iMap.get("supplierNo"));
						}
						if(ResourceUtil.getConfigByName("wms.stock_io_confirm").equals("true"))
							iEntity.setNoticeiSta(WmsContants.CONFIRMING);
						else
							iEntity.setNoticeiSta(WmsContants.CONFIRMED);
						iEntities.add(iEntity);
					}
				}
			}
		}
		return returnData;
	}

	@Override
	public NCDataObject pushInStorage() throws IOException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		//List<WmImNoticeHEntity> hlists = findByProperty(WmImNoticeHEntity.class,"has_pushed_nc","N");
		String hql = "from WmImNoticeHEntity wm where wm.hasPushedNC='N' and wm.imSta=? and wm.orderTypeCode='06'";
		List<WmImNoticeHEntity> hlists = findHql(hql,WmsContants.FINISHED);
		for (WmImNoticeHEntity entity : hlists){
			List<WmImNoticeIEntity> iList = findByProperty(WmImNoticeIEntity.class,"imNoticeId",entity.getNoticeId());
			Map map = new HashMap();
			map.put("orderkey",entity.getNoticeId());
			map.put("type","in");
			map.put("dbilldate",format.format(entity.getImData()));
			map.put("cwarehouseid",findByProperty(BaStoreEntity.class,"storeText","成品").get(0).getNcPkid());
			List<Map> detail = new ArrayList<Map>();
			map.put("detail",detail);
			for(WmImNoticeIEntity iEntity : iList){
				Map tempMap = new HashMap();
				tempMap.put("invbasid",this.findUniqueByProperty(PmBasicMaterialEntity.class,"stuffCode",iEntity.getGoodsCode()).getNcPkid());
				tempMap.put("invname",iEntity.getGoodsName());
				tempMap.put("num",iEntity.getGoodsCount());
				detail.add(tempMap);
			}
			NCDataObject data = requestMethode("alWmsGeneral","",map);
		}
		NCDataObject returnObj = new NCDataObject();
		returnObj.setSuccess("true");
		returnObj.setLength(0);
		return returnObj;
	}

	@Override
	public NCDataObject pushProductTask() throws IOException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		//PmProcTaskOrderEntity task = findByProperty(PmProcTaskOrderEntity.class,"remarks","N").get(0);
		PmProcTaskEntity task = this.findByProperty(PmProcTaskEntity.class,"remarks","N").get(0);
		WmOmNoticeHEntity hEntity = this.findUniqueByProperty(WmOmNoticeHEntity.class,"orderNum",task.getOrderNum());
		String orderKey = task.getOrderNum();
		List<PmMaterialListEntity> list = this.findByProperty(PmMaterialListEntity.class,"orderNum",orderKey);
		Map map = new HashMap();
		map.put("orderkey",hEntity.getOmNoticeId());
		map.put("type","out");
		map.put("dbilldate",format.format(hEntity.getDelvData()));
		map.put("cwarehouseid",findByProperty(BaStoreEntity.class,"storeText","成品").get(0).getNcPkid());
		List<Map> detail = new ArrayList<Map>();
		map.put("detail",detail);
		for (PmMaterialListEntity pm : list){
			//            String sql = "select * from wm_om_notice_i a left join wm_om_notice_h h on h.om_notice_id=a.om_notice_id where h.order_num=? and a.goods_id=?";
			//            sql.replaceFirst("\\?",task.getOrderNum()).replaceFirst("\\?",pm.getCode());
			String hql = "from WmOmNoticeIEntity wm where wm.omNoticeId=? and wm.goodsId=?";
			List<WmOmNoticeIEntity> ilist = this.findHql(hql,hEntity.getOmNoticeId(),pm.getCode());
			int count = 0;
			for (WmOmNoticeIEntity iEntity : ilist){
				count += Integer.valueOf(iEntity.getGoodsQua());
			}
			Map tempMap = new HashMap();
			tempMap.put("invbasid",this.findUniqueByProperty(PmBasicMaterialEntity.class,"stuffCode",pm.getCode()).getNcPkid());
			tempMap.put("invname",pm.getName());
			tempMap.put("num",String.valueOf(count));
			detail.add(tempMap);
		}
		NCDataObject data = requestMethode("alWmsGeneral","",map);
		return data;
	}

	@Override
	public boolean outConfirm(String outKey) throws IOException {
		Map map = new HashMap();
		map.put("erp_basid",outKey);
		NCDataObject obj = requestMethode("alSaleOutConfirm","",map);
		if(!"true".equals(obj.getSuccess()))
			return false;
		return true;
	}

	@Override
	public NCDataObject realImUpload(String imNoticeId) throws IOException {
		List<WmImNoticeIEntity> list = this.findByProperty(WmImNoticeIEntity.class,"imNoticeId",imNoticeId);
		Map param = new HashMap();
		param.put("erp_basid",imNoticeId);
		List<Map> detail = new ArrayList<>();
		for (WmImNoticeIEntity entity : list){
			Map temp = new HashMap();
			temp.put("invbasid",entity.getGoodsCode());
			temp.put("num",entity.getGoodsCount());
			detail.add(temp);
		}
		param.put("detail",detail);
		NCDataObject obj = this.requestMethode("alUpdate45","",param);
		return obj;
	}
}
