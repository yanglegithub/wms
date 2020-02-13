package com.zzjee.md.service.impl;
import com.zzjee.md.service.MdPalletServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.zzjee.md.entity.MdPalletEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.io.Serializable;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;

@Service("mdPalletService")
@Transactional
public class MdPalletServiceImpl extends CommonServiceImpl implements MdPalletServiceI {


 	public void delete(MdPalletEntity entity) throws Exception{
 		super.delete(entity);
 		//执行删除操作增强业务
		this.doDelBus(entity);
 	}

 	public Serializable save(MdPalletEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		//执行新增操作增强业务
 		this.doAddBus(entity);
 		return t;
 	}

 	public void saveOrUpdate(MdPalletEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 		//执行更新操作增强业务
 		this.doUpdateBus(entity);
 	}

	@Override
	public Map<String, Object> findPalletsStatus() {
 		String sql =
				"SELECT  " +
				"  SUM(IF (p.tuo_pan_zhuang_tai <> '空闲', 1, 0 )) AS 'used',  " +
				"  SUM(1) AS 'sum'  " +
				"FROM md_pallet p  " +
				"WHERE p.ting_yong = 'N'";
		Map result =  this.findOneForJdbc(sql);
		return result;
	}

	/**
	 * 新增操作增强业务
	 * @param t
	 * @return
	 */
	private void doAddBus(MdPalletEntity t) throws Exception{
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
	private void doUpdateBus(MdPalletEntity t) throws Exception{
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
	private void doDelBus(MdPalletEntity t) throws Exception{
	    //-----------------sql增强 start----------------------------
	 	//-----------------sql增强 end------------------------------

	 	//-----------------java增强 start---------------------------
	 	//-----------------java增强 end-----------------------------
 	}

 	private Map<String,Object> populationMap(MdPalletEntity t){
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
		map.put("tuo_pan_bian_ma", t.getTuoPanBianMa());
		map.put("tuo_pan_tiao_ma", t.getTuoPanTiaoMa());
		map.put("tuo_pan_lei_xing", t.getTuoPanLeiXing());
		map.put("tuo_pan_zhuang_tai", t.getTuoPanZhuangTai());
		map.put("zhuang_liao_bian_ma", t.getZhuangLiaoBianMa());
		map.put("zhuang_liao_ming_cheng", t.getZhuangLiaoMingCheng());
		map.put("pi_ci_hao", t.getPiCiHao());
		map.put("shu_liang", t.getShuLiang());
		map.put("zhong_liang", t.getZhongLiang());
		map.put("li_lun_gui_ge", t.getLiLunGuiGe());
		map.put("shi_ji_gui_ge", t.getShiJiGuiGe());
		map.put("shu_liang_dan_wei", t.getShuLiangDanWei());
		map.put("zhong_liang_dan_wei", t.getZhongLiangDanWei());
		map.put("bin_bian_ma", t.getBinBianMa());
		map.put("bin_tiao_ma", t.getBinTiaoMa());
		map.put("ting_yong", t.getTingYong());
		map.put("ming_xi", t.getMingXi());
		map.put("entry_key", t.getEntryKey());
		return map;
	}

 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @param t
	 * @return
	 */
 	public String replaceVal(String sql,MdPalletEntity t){
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
 		sql  = sql.replace("#{tuo_pan_bian_ma}",String.valueOf(t.getTuoPanBianMa()));
 		sql  = sql.replace("#{tuo_pan_tiao_ma}",String.valueOf(t.getTuoPanTiaoMa()));
 		sql  = sql.replace("#{tuo_pan_lei_xing}",String.valueOf(t.getTuoPanLeiXing()));
 		sql  = sql.replace("#{tuo_pan_zhuang_tai}",String.valueOf(t.getTuoPanZhuangTai()));
 		sql  = sql.replace("#{zhuang_liao_bian_ma}",String.valueOf(t.getZhuangLiaoBianMa()));
 		sql  = sql.replace("#{zhuang_liao_ming_cheng}",String.valueOf(t.getZhuangLiaoMingCheng()));
 		sql  = sql.replace("#{pi_ci_hao}",String.valueOf(t.getPiCiHao()));
 		sql  = sql.replace("#{shu_liang}",String.valueOf(t.getShuLiang()));
 		sql  = sql.replace("#{zhong_liang}",String.valueOf(t.getZhongLiang()));
 		sql  = sql.replace("#{li_lun_gui_ge}",String.valueOf(t.getLiLunGuiGe()));
 		sql  = sql.replace("#{shi_ji_gui_ge}",String.valueOf(t.getShiJiGuiGe()));
 		sql  = sql.replace("#{shu_liang_dan_wei}",String.valueOf(t.getShuLiangDanWei()));
 		sql  = sql.replace("#{zhong_liang_dan_wei}",String.valueOf(t.getZhongLiangDanWei()));
 		sql  = sql.replace("#{bin_bian_ma}",String.valueOf(t.getBinBianMa()));
 		sql  = sql.replace("#{bin_tiao_ma}",String.valueOf(t.getBinTiaoMa()));
 		sql  = sql.replace("#{ting_yong}",String.valueOf(t.getTingYong()));
 		sql  = sql.replace("#{ming_xi}",String.valueOf(t.getMingXi()));
 		sql  = sql.replace("#{entry_key}",String.valueOf(t.getEntryKey()));
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
					javaInter.execute("md_pallet",data);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("执行JAVA增强出现异常！");
			}
		}
 	}
}
