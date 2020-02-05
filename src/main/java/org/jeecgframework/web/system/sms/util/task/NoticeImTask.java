package org.jeecgframework.web.system.sms.util.task;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.zzjee.api.service.StockInfoServiceI;
import com.zzjee.api.utils.NCDataObject;
import com.zzjee.md.entity.MdGoodsEntity;
import com.zzjee.wm.entity.WmImNoticeHEntity;
import com.zzjee.wm.entity.WmImNoticeIEntity;
import com.zzjee.wmutil.WmsContants;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.sms.service.TSSmsServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @ClassName:NoticeImTask 定时从NC获取原料入库数据
 * @Description: TODO
 * @date 2020-01-17 下午5:06:34
 *
 */
@Service("noticeImTask")
public class NoticeImTask {

	@Autowired
	private TSSmsServiceI tSSmsService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private StockInfoServiceI stockInfoService;

	/* @Scheduled(cron="0 0 01 * * ?") */
	public void run() {
		NCDataObject data = null;
		String returnStr = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String todayStr = format.format(new Date());
		Gson gson = new Gson();
		try {
			data = stockInfoService.requestMethode("alWMSQry","genIn","{\"vbillcode\":\"\",\"dbilldate\":\""+todayStr+"\"}");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(data == null){
			//return null;
		}else{
			for (Map<String, Object> map : (List<Map>)data.getResult()){
				String dataStr = (String) map.get("data");
				List<Map> templist = (List) JSONObject.parseArray(dataStr);
				for (Map<String, Object> temp : templist){
					WmImNoticeHEntity hEntity = new WmImNoticeHEntity();
					hEntity.setNoticeId((String) temp.get("InStorageNo"));

					//若该入库单已经保存，则此次尝试读取各原料的过磅重量
					WmImNoticeHEntity old = systemService.findUniqueByProperty(WmImNoticeHEntity.class, "noticeId", hEntity.getNoticeId());
					if(old != null){
						for(Map<String, Object> iMap : (List<Map>)temp.get("detail")){
							String hql = "from WmImNoticeIEntity i where i.imNoticeId=? and i.goodsCode=?";
							List<WmImNoticeIEntity> imis = systemService.findHql(hql,old.getNoticeId(), (String) iMap.get("materialCode"));
							if(imis.size() > 0){
								imis.get(0).setGoodsQmWeight(StringUtil.isEmpty((String) iMap.get("storeWeight"))?null:(String) iMap.get("storeWeight"));
							}
						}
					}else{//若没有保存，则创建入库单
						returnStr += "".equals(returnStr)?hEntity.getNoticeId():(","+hEntity.getNoticeId());
						try {
							hEntity.setImData(format.parse((String) temp.get("storeDate")));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						hEntity.setQcStaff((String) temp.get("QCstaff"));
						hEntity.setNcPkid((String) temp.get("cgeneralhid"));
						hEntity.setOrderTypeCode(WmsContants.IN_MATERIALS);

						hEntity.setImSta(WmsContants.CONFIRMING);

						for(Map<String, Object> iMap : (List<Map>)temp.get("detail")){
							WmImNoticeIEntity iEntity = new WmImNoticeIEntity();
							iEntity.setImNoticeId(hEntity.getNoticeId());
							iEntity.setGoodsCode((String) iMap.get("materialCode"));
							iEntity.setGoodsName((String) iMap.get("maretialName"));
							iEntity.setBarCode(systemService.findUniqueByProperty(MdGoodsEntity.class,"shpBianMa",iEntity.getGoodsCode()).getShpTiaoMa());
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

							iEntity.setNoticeiSta(WmsContants.CONFIRMING);

							systemService.save(iEntity);
						}
						systemService.save(hEntity);
					}

				}
			}
		}

	}
}
