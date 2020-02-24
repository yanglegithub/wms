package com.zzjee.wm.service.impl;
import com.zzjee.wm.service.WmStockBaseStockServiceI;
import com.zzjee.wmutil.WmsContants;
import org.activiti.explorer.ui.validator.LongValidator;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.zzjee.wm.entity.WmStockBaseStockEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.io.Serializable;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;

@Service("wmStockBaseStockService")
@Transactional
public class WmStockBaseStockServiceImpl extends CommonServiceImpl implements WmStockBaseStockServiceI {


 	public void delete(WmStockBaseStockEntity entity) throws Exception{
 		super.delete(entity);
 		//执行删除操作增强业务
		this.doDelBus(entity);
 	}

 	public Serializable save(WmStockBaseStockEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		//执行新增操作增强业务
 		this.doAddBus(entity);
 		return t;
 	}

 	public void saveOrUpdate(WmStockBaseStockEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 		//执行更新操作增强业务
 		this.doUpdateBus(entity);
 	}

	@Override
	public Map<String, Object> findQuaGroupByType() {
 		String sql =
				"SELECT " +
						"sum(wms.goods_qua) AS qua, " +
						"imh.order_type_code AS type " +
						"FROM " +
						"wm_stock_base_stock wms " +
						"LEFT JOIN wm_im_notice_i imi ON wms.order_id = imi.id " +
						"LEFT JOIN wm_im_notice_h imh ON imh.notice_id = imi.im_notice_id " +
						"GROUP BY " +
						"imh.order_type_code ";
 		List<Map<String, Object>> result = this.findForJdbc(sql);
		Map temp = toMapByType(result);
		Map ret = new HashMap();
		ret.put("MATERIALS",temp.get("IN_MATERIALS"));
		ret.put("PRODUCT",temp.get("IN_PRODUCT"));
		return  ret;
	}

	@Override
	public Map<String, Object> findQuaGroupByType(int daysAgo) {
		List<Map<String, Object>> temp = new ArrayList<>();
		String sql_stocknow =
				"SELECT " +
					"sum(wms.goods_qua) AS qua, " +
					"imh.order_type_code AS type " +
				"FROM " +
					"wm_stock_base_stock wms " +
				"LEFT JOIN wm_im_notice_i imi ON wms.order_id = imi.id " +
				"LEFT JOIN wm_im_notice_h imh ON imh.notice_id = imi.im_notice_id " +
				"GROUP BY " +
					"imh.order_type_code ";
		temp = this.findForJdbc(sql_stocknow);
		Map stock_now = toMapByType(temp);
		String sql_m_var =
				"SELECT " +
				 	"sum(imi.goods_count) AS qua, " +
				 	"imh.order_type_code AS type " +
				"FROM " +
				 	"wm_im_notice_i imi " +
				"LEFT JOIN wm_im_notice_h imh ON imh.notice_id = imi.im_notice_id " +
				"WHERE " +
				 	"imh.im_sta = '已完成' " +
				"AND imh.create_date > DATE_SUB(now(), INTERVAL ? DAY) " +
				"GROUP BY " +
				 	"imh.order_type_code";
		temp = this.findForJdbc(sql_m_var, daysAgo);
		Map im_var = toMapByType(temp);
		String sql_p_var =
				"SELECT " +
				 	"SUM(omi.goods_qua) AS 'qua', " +
				 	"omh.order_type_code AS 'type' " +
				"FROM " +
				 	"wm_om_notice_i omi " +
				"LEFT JOIN wm_om_notice_h omh ON omh.om_notice_id = omi.om_notice_id " +
				"WHERE " +
				 	"omh.om_sta = '已完成' " +
				"AND omh.create_date > DATE_SUB(now(), INTERVAL ? DAY) " +
				"GROUP BY " +
				 	"omh.order_type_code";
		temp = this.findForJdbc(sql_p_var, daysAgo);
		Map out_var = toMapByType(temp);
		Long m_count = longValue(stock_now.get("IN_MATERIALS"));
		Long p_count = longValue(stock_now.get("IN_PRODUCT"));
		m_count = m_count - longValue(im_var.get("IN_MATERIALS")) + longValue(out_var.get("OUT_MATERIALS"));
		p_count = p_count - longValue(im_var.get("IN_PRODUCT")) + longValue(out_var.get("OUT_PRODUCT"));
		Map result = new HashMap();
		result.put("MATERIALS",m_count);
		result.put("PRODUCT",p_count);
		return result;
	}

	@Override
	public Map<String, Object> findQuaVar() {
		List<Map<String, Object>> tmp = new ArrayList<>();
		String sql_im_var =
				"SELECT " +
						"sum(imi.goods_count) AS qua, " +
						"imh.order_type_code AS type " +
						"FROM " +
						"wm_im_notice_i imi " +
						"LEFT JOIN wm_im_notice_h imh ON imh.notice_id = imi.im_notice_id " +
						"WHERE " +
						"imh.im_sta = '已完成' " +
						"AND imh.create_date > DATE_SUB(now(), INTERVAL ? DAY) " +
						"GROUP BY " +
						"imh.order_type_code";
		tmp = this.findForJdbc(sql_im_var, 30);
		Map im_var = toMapByType(tmp);
		String sql_out_var =
				"SELECT " +
						"SUM(omi.goods_qua) AS 'qua', " +
						"omh.order_type_code AS 'type' " +
						"FROM " +
						"wm_om_notice_i omi " +
						"LEFT JOIN wm_om_notice_h omh ON omh.om_notice_id = omi.om_notice_id " +
						"WHERE " +
						"omh.om_sta = '已完成' " +
						"AND omh.create_date > DATE_SUB(now(), INTERVAL ? DAY) " +
						"GROUP BY " +
						"omh.order_type_code";
		tmp = this.findForJdbc(sql_out_var, 30);
		Map out_var = toMapByType(tmp);

		Map<String, Object> result = new HashMap<>();
		result.put("IN_MATERIALS", im_var.get("IN_MATERIALS") == null ? 0L : im_var.get("IN_MATERIALS"));
		result.put("OUT_MATERIALS", out_var.get("OUT_MATERIALS") == null ? 0L : out_var.get("OUT_MATERIALS"));
		result.put("IN_PRODUCT", im_var.get("IN_PRODUCT") == null ? 0L : im_var.get("IN_PRODUCT"));
		result.put("OUT_PRODUCT", out_var.get("OUT_PRODUCT") == null ? 0L : out_var.get("OUT_PRODUCT"));
		return result;
	}

	@Override
	public long findQuaByType(String code) {
 		String sql = "SELECT SUM(ws.goods_qua) AS 'sum' FROM wm_stock_base_stock ws WHERE ws.goods_id = ?";
		Map result =  this.findOneForJdbc(sql, code);
		return result.get("sum") == null ? 0L : longValue(result.get("sum"));
	}


	/**
	 * 看板业务要将list转换成map，特定业务需求list的泛型必须是Map类型
	 * @param list
	 * @return
	 */
	private Map<String, Object> toMapByType(List list){
 		Map<String, Object> map = new HashMap<>();
 		map.put("IN_MATERIALS", 0L);
 		map.put("OUT_MATERIALS", 0L);
 		map.put("IN_PRODUCT", 0L);
 		map.put("OUT_PRODUCT", 0L);
 		for (Map m : (List<Map>)list){
 			switch ((String)m.get("type")){
				case WmsContants.IN_MATERIALS :
					map.put("IN_MATERIALS", m.get("qua"));
					break;
				case WmsContants.OUT_MATERIALS:
					map.put("OUT_MATERIALS", m.get("qua"));
					break;
				case WmsContants.IN_PRODUCT:
					map.put("IN_PRODUCT", m.get("qua"));
					break;
				case WmsContants.OUT_PRODUCT:
					map.put("OUT_PRODUCT", m.get("qua"));
					break;
				default :
					break;
			}
		}
 		return map;
	}

	/**
	 * 将interger 和 double 类型的数据转换成long 类型的数据
	 * @param object
	 * @return
	 */
	public Long longValue(Object object){
		if(object.getClass().toString().contains("Integer")){
			return ((Integer)object).longValue();
		}else if(object.getClass().toString().contains("Double")){
			return ((Double)object).longValue();
		}else if(object.getClass().toString().contains("Long")){
			return ((Long)object).longValue();
		}else {
			return null;
		}
	}

	/**
	 * 新增操作增强业务
	 * @param t
	 * @return
	 */
	private void doAddBus(WmStockBaseStockEntity t) throws Exception{
		//-----------------sql增强 start----------------------------
	 	//-----------------sql增强 end------------------------------

	 	//-----------------java增强 start---------------------------
	 	//-----------------java增强 end-----------------------------
 	}
 	/**
	 * 更新操作增强业务
	 * @param t
	 * @return
	 */
	private void doUpdateBus(WmStockBaseStockEntity t) throws Exception{
		//-----------------sql增强 start----------------------------
	 	//-----------------sql增强 end------------------------------

	 	//-----------------java增强 start---------------------------
	 	//-----------------java增强 end-----------------------------
 	}
 	/**
	 * 删除操作增强业务
	 * @param id
	 * @return
	 */
	private void doDelBus(WmStockBaseStockEntity t) throws Exception{
	    //-----------------sql增强 start----------------------------
	 	//-----------------sql增强 end------------------------------

	 	//-----------------java增强 start---------------------------
	 	//-----------------java增强 end-----------------------------
 	}

 	private Map<String,Object> populationMap(WmStockBaseStockEntity t){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", t.getId());
		map.put("create_name", t.getCreateName());
		map.put("create_by", t.getCreateBy());
		map.put("create_date", t.getCreateDate());
		map.put("update_name", t.getUpdateName());
		map.put("update_by", t.getUpdateBy());
		map.put("update_date", t.getUpdateDate());
		map.put("sys_org_code", t.getSysOrgCode());
		map.put("sys_company_code", t.getSysCompanyCode());
		map.put("bpm_status", t.getBpmStatus());
		map.put("kuctype", t.getKuctype());
		map.put("ku_wei_bian_ma", t.getKuWeiBianMa());
		map.put("bin_id", t.getBinId());
		map.put("cus_code", t.getCusCode());
		map.put("zhong_wen_qch", t.getZhongWenQch());
		map.put("goods_id", t.getGoodsId());
		map.put("goods_qua", t.getGoodsQua());
		map.put("order_id", t.getOrderId());
		map.put("goods_pro_data", t.getGoodsProData());
		map.put("goods_unit", t.getGoodsUnit());
		map.put("base_unit", t.getBaseUnit());
		map.put("base_goodscount", t.getBaseGoodscount());
		map.put("goods_bzhiqi", t.getGoodsBzhiqi());
		return map;
	}

 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @param t
	 * @return
	 */
 	public String replaceVal(String sql,WmStockBaseStockEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{create_name}",String.valueOf(t.getCreateName()));
 		sql  = sql.replace("#{create_by}",String.valueOf(t.getCreateBy()));
 		sql  = sql.replace("#{create_date}",String.valueOf(t.getCreateDate()));
 		sql  = sql.replace("#{update_name}",String.valueOf(t.getUpdateName()));
 		sql  = sql.replace("#{update_by}",String.valueOf(t.getUpdateBy()));
 		sql  = sql.replace("#{update_date}",String.valueOf(t.getUpdateDate()));
 		sql  = sql.replace("#{sys_org_code}",String.valueOf(t.getSysOrgCode()));
 		sql  = sql.replace("#{sys_company_code}",String.valueOf(t.getSysCompanyCode()));
 		sql  = sql.replace("#{bpm_status}",String.valueOf(t.getBpmStatus()));
 		sql  = sql.replace("#{kuctype}",String.valueOf(t.getKuctype()));
 		sql  = sql.replace("#{ku_wei_bian_ma}",String.valueOf(t.getKuWeiBianMa()));
 		sql  = sql.replace("#{bin_id}",String.valueOf(t.getBinId()));
 		sql  = sql.replace("#{cus_code}",String.valueOf(t.getCusCode()));
 		sql  = sql.replace("#{zhong_wen_qch}",String.valueOf(t.getZhongWenQch()));
 		sql  = sql.replace("#{goods_id}",String.valueOf(t.getGoodsId()));
 		sql  = sql.replace("#{goods_qua}",String.valueOf(t.getGoodsQua()));
 		sql  = sql.replace("#{order_id}",String.valueOf(t.getOrderId()));
 		sql  = sql.replace("#{goods_pro_data}",String.valueOf(t.getGoodsProData()));
 		sql  = sql.replace("#{goods_unit}",String.valueOf(t.getGoodsUnit()));
 		sql  = sql.replace("#{base_unit}",String.valueOf(t.getBaseUnit()));
 		sql  = sql.replace("#{base_goodscount}",String.valueOf(t.getBaseGoodscount()));
 		sql  = sql.replace("#{goods_bzhiqi}",String.valueOf(t.getGoodsBzhiqi()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		return sql;
 	}

 	/**
	 * 执行JAVA增强
	 */
 	private void executeJavaExtend(String cgJavaType,String cgJavaValue,Map<String,Object> data) throws Exception {
 		if(StringUtil.isNotEmpty(cgJavaValue)){
			Object obj = null;
			try {
				if("class".equals(cgJavaType)){
					//因新增时已经校验了实例化是否可以成功，所以这块就不需要再做一次判断
					obj = MyClassLoader.getClassByScn(cgJavaValue).newInstance();
				}else if("spring".equals(cgJavaType)){
					obj = ApplicationContextUtil.getContext().getBean(cgJavaValue);
				}
				if(obj instanceof CgformEnhanceJavaInter){
					CgformEnhanceJavaInter javaInter = (CgformEnhanceJavaInter) obj;
					javaInter.execute("wm_stock_base_stock",data);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("执行JAVA增强出现异常！");
			}
		}
 	}
}
