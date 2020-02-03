package com.zzjee.wm.service.impl;
import com.zzjee.flow.util.TaskStatus;
import com.zzjee.md.entity.MdCusEntity;
import com.zzjee.md.entity.MdPalletEntity;
import com.zzjee.md.entity.MvGoodsEntity;
import com.zzjee.md.entity.PalletStatus;
import com.zzjee.tms.entity.TmsYwDingdanEntity;
import com.zzjee.wm.page.WmOmNoticeHPage;
import com.zzjee.wm.service.WmOmNoticeHServiceI;

import com.zzjee.wmutil.WmsContants;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

import com.zzjee.wm.entity.WmOmNoticeHEntity;
import com.zzjee.wm.entity.WmOmNoticeIEntity;

import org.jeecgframework.core.util.*;
import org.jeecgframework.web.system.sms.util.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import org.antlr.codegen.ObjCTarget;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.io.Serializable;
import java.text.SimpleDateFormat;


@Service("wmOmNoticeHService")
@Transactional
public class WmOmNoticeHServiceImpl extends CommonServiceImpl implements WmOmNoticeHServiceI {

 	public <T> void delete(T entity) {
 		super.delete(entity);
 		//执行删除操作配置的sql增强
		this.doDelSql((WmOmNoticeHEntity)entity);
 	}

	public void addMain(WmOmNoticeHEntity wmOmNoticeH,
	        List<WmOmNoticeIEntity> wmOmNoticeIList){
			//保存主信息
			this.save(wmOmNoticeH);
			Double jishu = 0.00;
			Double tiji=0.00;
			Double zhongl = 0.00;
			Double chang = 0.00;
			Double kuan = 0.00;
			Double gao = 0.00;
			String huowu = "";
			/**保存-出货商品明细*/
			for(WmOmNoticeIEntity wmOmNoticeI:wmOmNoticeIList){
				//外键设置
				try{
					MvGoodsEntity mvgoods = this.findUniqueByProperty(MvGoodsEntity.class, "goodsCode", wmOmNoticeI.getGoodsId()) ;
					if(mvgoods!=null){
						huowu=huowu+mvgoods.getGoodsName();
						wmOmNoticeI.setGoodsName(mvgoods.getGoodsName());
						try{
						wmOmNoticeI.setBaseUnit(mvgoods.getBaseunit());
						wmOmNoticeI.setGoodsUnit(mvgoods.getShlDanWei());
						if(!mvgoods.getBaseunit().equals(mvgoods.getShlDanWei())){
							wmOmNoticeI.setBaseGoodscount(String.valueOf(Double.parseDouble(mvgoods.getChlShl())*Double.parseDouble(wmOmNoticeI.getGoodsQua())));
						}else{
							wmOmNoticeI.setBaseGoodscount(wmOmNoticeI.getGoodsQua());
						}
						try{
							tiji= tiji+ Double.parseDouble(wmOmNoticeI.getBaseGoodscount())*Double.parseDouble(mvgoods.getTiJiCm());
							zhongl= zhongl+ Double.parseDouble(wmOmNoticeI.getBaseGoodscount())*Double.parseDouble(mvgoods.getZhlKg());
//							chang= chang+ Double.parseDouble(wmOmNoticeI.getBaseGoodscount())*Double.parseDouble(mvgoods.get());
//							kuan= kuan+ Double.parseDouble(wmOmNoticeI.getBaseGoodscount())*Double.parseDouble(mvgoods.getZhlKg());
//							gao= gao+ Double.parseDouble(wmOmNoticeI.getBaseGoodscount())*Double.parseDouble(mvgoods.getZhlKg());

							jishu = jishu + Double.parseDouble(wmOmNoticeI.getBaseGoodscount());
						}catch (Exception e){
						}
						}catch (Exception e){

						}
					}
				}catch (Exception e){
				}
				wmOmNoticeI.setCusCode(wmOmNoticeH.getCusCode());
				wmOmNoticeI.setPlanSta("N");
				wmOmNoticeI.setGoodsQuaok("0");
				wmOmNoticeI.setOmNoticeId(wmOmNoticeH.getOmNoticeId());
				wmOmNoticeI.setImCusCode(wmOmNoticeH.getImCusCode());
				wmOmNoticeI.setOmBeizhu(wmOmNoticeH.getOmBeizhu());
				this.save(wmOmNoticeI);
			}

		if("yes".equals(ResourceUtil.getConfigByName("wms.totms"))){
			try{
				TmsYwDingdanEntity tmsYwDingdanEntity = new TmsYwDingdanEntity();
				MdCusEntity mdcus = this.findUniqueByProperty(MdCusEntity.class,"keHuBianMa",wmOmNoticeH.getCusCode());
				tmsYwDingdanEntity.setHwshjs(jishu.toString());
				tmsYwDingdanEntity.setTiji(tiji.toString());
				tmsYwDingdanEntity.setZhongl(zhongl.toString());
				tmsYwDingdanEntity.setChang(chang.toString());
				tmsYwDingdanEntity.setKuan(kuan.toString());
				tmsYwDingdanEntity.setGao(gao.toString());
				tmsYwDingdanEntity.setHuowu(huowu);
				tmsYwDingdanEntity.setCreateDate(DateUtils.getDate());
				tmsYwDingdanEntity.setUsername(mdcus.getKeHuBianMa());
				tmsYwDingdanEntity.setFahuoren(mdcus.getZhongWenQch());
				tmsYwDingdanEntity.setFhrdh(mdcus.getDianHua());
				tmsYwDingdanEntity.setFhrdz(mdcus.getDiZhi());
				tmsYwDingdanEntity.setSiji(wmOmNoticeH.getReCarno());//司机
				tmsYwDingdanEntity.setShouhuoren(wmOmNoticeH.getDelvMember());
				tmsYwDingdanEntity.setShrdh(wmOmNoticeH.getDelvAddr());
				tmsYwDingdanEntity.setShrsj(wmOmNoticeH.getDelvMobile());
				tmsYwDingdanEntity.setYwddbz(wmOmNoticeH.getOmBeizhu());
				tmsYwDingdanEntity.setYwkhdh(wmOmNoticeH.getOmNoticeId());
				tmsYwDingdanEntity.setZhuangtai("已下单");
				this.save(tmsYwDingdanEntity);
			}catch (Exception e){

			}

		}

			//执行新增操作配置的sql增强
 			this.doAddSql(wmOmNoticeH);
	}

	public boolean wmsAddMain(WmOmNoticeHEntity wmOmNoticeH,
        List<WmOmNoticeIEntity> wmOmNoticeIList){

		//验证库存信息
		for(WmOmNoticeIEntity i : wmOmNoticeIList) {
			String stockCountSql = "select max(a.qua) from ( select sum(ss.goods_qua) as qua from wm_stock_base_stock ss where ss.goods_id=? group by ss.goods_id,ss.goods_pro_data ) a ";
			Long stockCount = this.getCountForJdbcParam(stockCountSql, new Object[] {i.getGoodsId()});
			if(stockCount < Long.parseLong(i.getGoodsQua())) {
				return false;
			}
		}

		//保存主信息
		this.save(wmOmNoticeH);
		Double jishu = 0.00;
		Double tiji=0.00;
		Double zhongl = 0.00;
		Double chang = 0.00;
		Double kuan = 0.00;
		Double gao = 0.00;
		String huowu = "";
		/**保存-出货商品明细*/
		for(WmOmNoticeIEntity wmOmNoticeI:wmOmNoticeIList){

			/*String sql = "select sum(mp.shu_liang) as count,mp.pi_ci_hao as batch from md_pallet mp where mp.zhuang_liao_bian_ma=? group by mp.zhuang_liao_bian_ma,mp.pi_ci_hao";
			List<Map<String, Object>> list = this.findForJdbc(sql, wmOmNoticeI.getGoodsId());
			String batch = "";
			Long count = 0L;
			for(Map<String, Object> map : list) {
				if((Double)map.get("count") > count) {
					count = ((Double)map.get("count")).longValue();
					batch = (String)map.get("batch");
				}
			}
			String hql = "from MdPalletEntity mp where mp.zhuangLiaoBianMa=? and mp.piCiHao=? order by mp.binBianMa,binDepth desc";
			List<MdPalletEntity> listmp = this.findHql(hql, wmOmNoticeI.getGoodsId(),batch);
			String tincodes = "";
			String bincodes = "";
			Long needQua = Long.parseLong(wmOmNoticeI.getGoodsQua());
			Long wehas = 0L;
			for(MdPalletEntity mp : listmp) {
				tincodes += tincodes.equals("")?mp.getTuoPanBianMa():(","+mp.getTuoPanBianMa());
				bincodes += bincodes.matches(mp.getBinBianMa())?"":(bincodes.equals("")?mp.getBinBianMa():(","+mp.getBinBianMa()));
				wehas += Long.parseLong(mp.getShuLiang());
				if(wehas > needQua)
					break;
			}*/

			//外键设置
			try{
				MvGoodsEntity mvgoods = this.findUniqueByProperty(MvGoodsEntity.class, "goodsCode", wmOmNoticeI.getGoodsId()) ;
				if(mvgoods!=null){
					huowu=huowu+mvgoods.getGoodsName();
					wmOmNoticeI.setGoodsName(mvgoods.getGoodsName());
					try{
					wmOmNoticeI.setBaseUnit(mvgoods.getBaseunit());
					wmOmNoticeI.setGoodsUnit(mvgoods.getShlDanWei());
					if(!mvgoods.getBaseunit().equals(mvgoods.getShlDanWei())){
						wmOmNoticeI.setBaseGoodscount(String.valueOf(Double.parseDouble(mvgoods.getChlShl())*Double.parseDouble(wmOmNoticeI.getGoodsQua())));
					}else{
						wmOmNoticeI.setBaseGoodscount(wmOmNoticeI.getGoodsQua());
					}
					try{
						tiji= tiji+ Double.parseDouble(wmOmNoticeI.getBaseGoodscount())*Double.parseDouble(mvgoods.getTiJiCm());
						zhongl= zhongl+ Double.parseDouble(wmOmNoticeI.getBaseGoodscount())*Double.parseDouble(mvgoods.getZhlKg());
//							chang= chang+ Double.parseDouble(wmOmNoticeI.getBaseGoodscount())*Double.parseDouble(mvgoods.get());
//							kuan= kuan+ Double.parseDouble(wmOmNoticeI.getBaseGoodscount())*Double.parseDouble(mvgoods.getZhlKg());
//							gao= gao+ Double.parseDouble(wmOmNoticeI.getBaseGoodscount())*Double.parseDouble(mvgoods.getZhlKg());

						jishu = jishu + Double.parseDouble(wmOmNoticeI.getBaseGoodscount());
					}catch (Exception e){
					}
					}catch (Exception e){

					}
				}
			}catch (Exception e){
			}
			wmOmNoticeI.setCusCode(wmOmNoticeH.getCusCode());
			wmOmNoticeI.setPlanSta("N");
			wmOmNoticeI.setGoodsQuaok("0");
			wmOmNoticeI.setWeightUnit("KG");
			/*wmOmNoticeI.setBinOm(bincodes);
			wmOmNoticeI.setBinId(tincodes);*/
			//wmOmNoticeI.setGoodsBatch((String) good.get("goodsBatch"));
			//wmOmNoticeI.setDelvData(format.format(task.getOutDate()));
			wmOmNoticeI.setOmSta(Constants.wm_sta1);
			wmOmNoticeI.setOmNoticeId(wmOmNoticeH.getOmNoticeId());
			wmOmNoticeI.setImCusCode(wmOmNoticeH.getImCusCode());
			wmOmNoticeI.setOmBeizhu(wmOmNoticeH.getOmBeizhu());
			this.save(wmOmNoticeI);
		}

		if("yes".equals(ResourceUtil.getConfigByName("wms.totms"))){
			try{
				TmsYwDingdanEntity tmsYwDingdanEntity = new TmsYwDingdanEntity();
				MdCusEntity mdcus = this.findUniqueByProperty(MdCusEntity.class,"keHuBianMa",wmOmNoticeH.getCusCode());
				tmsYwDingdanEntity.setHwshjs(jishu.toString());
				tmsYwDingdanEntity.setTiji(tiji.toString());
				tmsYwDingdanEntity.setZhongl(zhongl.toString());
				tmsYwDingdanEntity.setChang(chang.toString());
				tmsYwDingdanEntity.setKuan(kuan.toString());
				tmsYwDingdanEntity.setGao(gao.toString());
				tmsYwDingdanEntity.setHuowu(huowu);
				tmsYwDingdanEntity.setCreateDate(DateUtils.getDate());
				tmsYwDingdanEntity.setUsername(mdcus.getKeHuBianMa());
				tmsYwDingdanEntity.setFahuoren(mdcus.getZhongWenQch());
				tmsYwDingdanEntity.setFhrdh(mdcus.getDianHua());
				tmsYwDingdanEntity.setFhrdz(mdcus.getDiZhi());
				tmsYwDingdanEntity.setSiji(wmOmNoticeH.getReCarno());//司机
				tmsYwDingdanEntity.setShouhuoren(wmOmNoticeH.getDelvMember());
				tmsYwDingdanEntity.setShrdh(wmOmNoticeH.getDelvAddr());
				tmsYwDingdanEntity.setShrsj(wmOmNoticeH.getDelvMobile());
				tmsYwDingdanEntity.setYwddbz(wmOmNoticeH.getOmBeizhu());
				tmsYwDingdanEntity.setYwkhdh(wmOmNoticeH.getOmNoticeId());
				tmsYwDingdanEntity.setZhuangtai("已下单");
				this.save(tmsYwDingdanEntity);
			}catch (Exception e){

			}

		}

			//执行新增操作配置的sql增强
 			this.doAddSql(wmOmNoticeH);
 		return true;
	}


	public void updateMain(WmOmNoticeHEntity wmOmNoticeH,
	        List<WmOmNoticeIEntity> wmOmNoticeIList,List<TmsYwDingdanEntity> wmOmtmsIList) {
		//保存主表信息
		this.saveOrUpdate(wmOmNoticeH);
		//===================================================================================
		//获取参数
		Object id0 = wmOmNoticeH.getOmNoticeId();
		//===================================================================================
		//1.查询出数据库的明细数据-出货商品明细
	    String hql0 = "from WmOmNoticeIEntity where 1 = 1 AND oM_NOTICE_ID = ? ";
	    List<WmOmNoticeIEntity> wmOmNoticeIOldList = this.findHql(hql0,id0);
		//2.筛选更新明细数据-出货商品明细
		if(wmOmNoticeIList!=null&&wmOmNoticeIList.size()>0){
		for(WmOmNoticeIEntity oldE:wmOmNoticeIOldList){
			boolean isUpdate = false;
				for(WmOmNoticeIEntity sendE:wmOmNoticeIList){
					//需要更新的明细数据-出货商品明细
					if(oldE.getId().equals(sendE.getId())){
		    			try {
							MyBeanUtils.copyBeanNotNull2Bean(sendE,oldE);
							oldE.setBinOm(sendE.getBinOm());
							oldE.setGoodsProData(sendE.getGoodsProData());
							oldE.setBinId(sendE.getBinId());
							MvGoodsEntity mvgoods = this.findUniqueByProperty(MvGoodsEntity.class, "goodsCode", oldE.getGoodsId()) ;
							oldE.setGoodsUnit(mvgoods.getShlDanWei());
							oldE.setBaseUnit(mvgoods.getBaseunit());
							if(!mvgoods.getBaseunit().equals(mvgoods.getShlDanWei())){
								oldE.setBaseGoodscount(String.valueOf(Double.parseDouble(mvgoods.getChlShl())*Double.parseDouble(oldE.getGoodsQua())));
							}else{
								oldE.setBaseGoodscount(oldE.getGoodsQua());
							}
							this.saveOrUpdate(oldE);
						} catch (Exception e) {
							e.printStackTrace();
							throw new BusinessException(e.getMessage());
						}
						isUpdate= true;
		    			break;
		    		}
		    	}
	    		if(!isUpdate){
		    		//如果数据库存在的明细，前台没有传递过来则是删除-出货商品明细
		    		super.delete(oldE);
	    		}

			}
			//3.持久化新增的数据-出货商品明细
			for(WmOmNoticeIEntity wmOmNoticeI:wmOmNoticeIList){
				if(oConvertUtils.isEmpty(wmOmNoticeI.getId())){
					//外键设置
//					MvGoodsEntity mvgoods = new MvGoodsEntity();
					MvGoodsEntity mvgoods = this.findUniqueByProperty(MvGoodsEntity.class,"goodsName",wmOmNoticeI.getGoodsId());

					mvgoods = this.findUniqueByProperty(MvGoodsEntity.class, "goodsCode", wmOmNoticeI.getGoodsId()) ;
					wmOmNoticeI.setGoodsUnit(mvgoods.getShlDanWei());
					wmOmNoticeI.setBaseUnit(mvgoods.getBaseunit());
					if(!mvgoods.getBaseunit().equals(mvgoods.getShlDanWei())){
						wmOmNoticeI.setBaseGoodscount(String.valueOf(Double.parseDouble(mvgoods.getChlShl())*Double.parseDouble(wmOmNoticeI.getGoodsQua())));
					}else{
						wmOmNoticeI.setBaseGoodscount(wmOmNoticeI.getGoodsQua());
					}

					wmOmNoticeI.setCusCode(wmOmNoticeH.getCusCode());
					wmOmNoticeI.setPlanSta("N");
					wmOmNoticeI.setGoodsQuaok("0");
					wmOmNoticeI.setOmNoticeId(wmOmNoticeH.getOmNoticeId());
					this.save(wmOmNoticeI);
				}
			}
		}

		//3.筛选更新明细数据-运输商品明细
//		String hql1 ="from TmsYwDingdanEntity where 1 = 1 AND ywkhdh = ? ";
//		List<TmsYwDingdanEntity> wmOmtmsIOldList = this.findHql(hql1,id0);
//
//		if(wmOmtmsIList!=null&&wmOmtmsIList.size()>0){
//			for(TmsYwDingdanEntity oldE:wmOmtmsIOldList){
//				boolean isUpdate = false;
//				for(TmsYwDingdanEntity sendE:wmOmtmsIList){
//					//需要更新的明细数据-出货商品明细
//					if(oldE.getId().equals(sendE.getId())){
//						try {
//							MyBeanUtils.copyBeanNotNull2Bean(sendE,oldE);
//							this.saveOrUpdate(oldE);
//						} catch (Exception e) {
//							e.printStackTrace();
//							throw new BusinessException(e.getMessage());
//						}
//						isUpdate= true;
//						break;
//					}
//				}
//				if(!isUpdate){
//					//如果数据库存在的明细，前台没有传递过来则是删除-出货商品明细
//					super.delete(oldE);
//				}
////
//			}
//			//3.持久化新增的数据-出货商品明细
//			for(TmsYwDingdanEntity wmOmNoticeI:wmOmtmsIList){
//				if(oConvertUtils.isEmpty(wmOmNoticeI.getId())){
//					//外键设置
//					wmOmNoticeI.setYwkhdh(wmOmNoticeH.getOmNoticeId());
//					this.save(wmOmNoticeI);
//				}
//			}
//		}

		//执行更新操作配置的sql增强
 		this.doUpdateSql(wmOmNoticeH);
	}


	public void delMain(WmOmNoticeHEntity wmOmNoticeH) {
		//删除主表信息
		this.delete(wmOmNoticeH);
		//===================================================================================
		//获取参数
		Object id0 = wmOmNoticeH.getOmNoticeId();
		//===================================================================================
		//删除-出货商品明细
	    String hql0 = "from WmOmNoticeIEntity where 1 = 1 AND oM_NOTICE_ID = ? ";
	    List<WmOmNoticeIEntity> wmOmNoticeIOldList = this.findHql(hql0,id0);
		this.deleteAllEntitie(wmOmNoticeIOldList);
	}


 	/**
	 * 默认按钮-sql增强-新增操作
	 * @return
	 */
 	public boolean doAddSql(WmOmNoticeHEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @return
	 */
 	public boolean doUpdateSql(WmOmNoticeHEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @return
	 */
 	public boolean doDelSql(WmOmNoticeHEntity t){
	 	return true;
 	}

	@Override
	public List<WmOmNoticeHPage> findImHList() {


		return null;
	}

	@Override
	public List<WmOmNoticeHPage> findLoadedOrderWithDetail() {
		List<WmOmNoticeHPage> pages = new ArrayList<>();
 		String hql = "from WmOmNoticeHEntity wmo where wmo.orderTypeCode=? and wmo.omSta=? and not exists( from WmOmNoticeIEntity wmi where wmi.omNoticeId=wmo.omNoticeId and wmi.omSta != ? )";
 		List<WmOmNoticeHEntity> hlist = this.findHql(hql,WmsContants.OUT_PRODUCT, WmsContants.OUT_STOCK, WmsContants.FINISHED);
 		for (WmOmNoticeHEntity h : hlist){
			WmOmNoticeHPage page = new WmOmNoticeHPage();
			try {
				MyBeanUtils.copyBeanNotNull2Bean(h,page);
				List<WmOmNoticeIEntity> is = this.findByProperty(WmOmNoticeIEntity.class, "omNoticeId",h.getOmNoticeId());
				page.setWmOmNoticeIList(is);
				pages.add(page);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pages;
	}

    @Override
    public List<WmOmNoticeIEntity> findUnloadTask(String type) {
 	    String hql = "from WmOmNoticeIEntity wmi where " +
                "exists( from WmOmNoticeHEntity wmh where wmi.omNoticeId=wmh.omNoticeId and wmh.orderTypeCode=? ) and " +
                "exists( from MdMovePalletEntity mp where mp.type='拆垛' and mp.triggerSource=wmi.id and mp.status=? )";
        List<WmOmNoticeIEntity> ilist = this.findHql(hql,type, TaskStatus.INIT);
        return ilist;
    }

    /**
	 * 替换sql中的变量
	 * @param sql
	 * @return
	 */
 	public String replaceVal(String sql,WmOmNoticeHEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{create_name}",String.valueOf(t.getCreateName()));
 		sql  = sql.replace("#{create_by}",String.valueOf(t.getCreateBy()));
 		sql  = sql.replace("#{create_date}",String.valueOf(t.getCreateDate()));
 		sql  = sql.replace("#{update_name}",String.valueOf(t.getUpdateName()));
 		sql  = sql.replace("#{update_by}",String.valueOf(t.getUpdateBy()));
 		sql  = sql.replace("#{update_date}",String.valueOf(t.getUpdateDate()));
 		sql  = sql.replace("#{sys_org_code}",String.valueOf(t.getSysOrgCode()));
 		sql  = sql.replace("#{sys_company_code}",String.valueOf(t.getSysCompanyCode()));
 		sql  = sql.replace("#{cus_code}",String.valueOf(t.getCusCode()));
 		sql  = sql.replace("#{delv_data}",String.valueOf(t.getDelvData()));
 		sql  = sql.replace("#{delv_member}",String.valueOf(t.getDelvMember()));
 		sql  = sql.replace("#{delv_mobile}",String.valueOf(t.getDelvMobile()));
 		sql  = sql.replace("#{delv_addr}",String.valueOf(t.getDelvAddr()));
 		sql  = sql.replace("#{re_member}",String.valueOf(t.getReMember()));
 		sql  = sql.replace("#{re_mobile}",String.valueOf(t.getReMobile()));
 		sql  = sql.replace("#{re_carno}",String.valueOf(t.getReCarno()));
 		sql  = sql.replace("#{om_plat_no}",String.valueOf(t.getOmPlatNo()));
 		sql  = sql.replace("#{om_beizhu}",String.valueOf(t.getOmBeizhu()));
 		sql  = sql.replace("#{om_sta}",String.valueOf(t.getOmSta()));
 		sql  = sql.replace("#{om_notice_id}",String.valueOf(t.getOmNoticeId()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		return sql;
 	}
}
