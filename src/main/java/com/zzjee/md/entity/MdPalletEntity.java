package com.zzjee.md.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.SequenceGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @Title: Entity
 * @Description: 托盘管理
 * @author onlineGenerator
 * @date 2019-12-19 17:15:07
 * @version V1.0
 *
 */
@Entity
@Table(name = "md_pallet", schema = "")
@SuppressWarnings("serial")
public class MdPalletEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**创建人名称*/
	private java.lang.String createName;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人名称*/
	private java.lang.String updateName;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**所属部门*/
	private java.lang.String sysOrgCode;
	/**所属公司*/
	private java.lang.String sysCompanyCode;
	/**流程状态*/
	private java.lang.String bpmStatus;
	/**托盘编码*/
	@Excel(name="托盘编码")
	private java.lang.String tuoPanBianMa;
	/**托盘条码*/
	@Excel(name="托盘条码")
	private java.lang.String tuoPanTiaoMa;
	/**托盘类型*/
	@Excel(name="托盘类型")
	private java.lang.String tuoPanLeiXing;
	/**托盘状态*/
	@Excel(name="托盘状态")
	private java.lang.String tuoPanZhuangTai;
	/**装料编码*/
	@Excel(name="装料编码")
	private java.lang.String zhuangLiaoBianMa;
	/**装料名称*/
	@Excel(name="装料名称")
	private java.lang.String zhuangLiaoMingCheng;
	/**装料批次号*/
	@Excel(name="装料批次号")
	private java.lang.String piCiHao;
	/**装料数量*/
	@Excel(name="装料数量")
	private java.lang.String shuLiang;
	/**装料重量*/
	@Excel(name="装料重量")
	private java.lang.String zhongLiang;
	/**装料理论规格*/
	@Excel(name="装料理论规格")
	private java.lang.String liLunGuiGe;
	/**装料实际规格*/
	@Excel(name="装料实际规格")
	private java.lang.String shiJiGuiGe;
	/**数量单位*/
	@Excel(name="数量单位")
	private java.lang.String shuLiangDanWei;
	/**重量单位*/
	@Excel(name="重量单位")
	private java.lang.String zhongLiangDanWei;
	/**储位编码*/
	@Excel(name="储位编码")
	private java.lang.String binBianMa;
	/**储位条码*/
	@Excel(name="储位条码")
	private java.lang.String binTiaoMa;
	/**储位深度序号*/
	@Excel(name="储位深度序号")
	private java.lang.Integer binDepth;
	/**停用*/
	@Excel(name="停用")
	private java.lang.String tingYong;
	/**明细*/
	@Excel(name="明细")
	private java.lang.String mingXi;
	/**入库单明细主键*/
	@Excel(name="入库单明细主键")
	private java.lang.String entryKey;
	/**码垛日期*/
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone="GMT-8", pattern="yyyy-MM-dd HH:mm:ss")
	@Excel(name="码垛日期", format="yyyy-MM-dd")
	private java.util.Date storeDate;
	/**最大数量**/
	@Excel(name="最大数量")
	private java.lang.String maxCount;
	/**最大重量**/
	@Excel(name="最大重量")
	private java.lang.String maxWeight;

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键
	 */
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name ="ID",nullable=false,length=36)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  主键
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */
	@Column(name ="CREATE_NAME",nullable=true,length=50)
	public java.lang.String getCreateName(){
		return this.createName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人名称
	 */
	public void setCreateName(java.lang.String createName){
		this.createName = createName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public java.lang.String getCreateBy(){
		return this.createBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_DATE",nullable=true,length=20)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人名称
	 */
	@Column(name ="UPDATE_NAME",nullable=true,length=50)
	public java.lang.String getUpdateName(){
		return this.updateName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人名称
	 */
	public void setUpdateName(java.lang.String updateName){
		this.updateName = updateName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="UPDATE_DATE",nullable=true,length=20)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属部门
	 */
	@Column(name ="SYS_ORG_CODE",nullable=true,length=50)
	public java.lang.String getSysOrgCode(){
		return this.sysOrgCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属部门
	 */
	public void setSysOrgCode(java.lang.String sysOrgCode){
		this.sysOrgCode = sysOrgCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属公司
	 */
	@Column(name ="SYS_COMPANY_CODE",nullable=true,length=50)
	public java.lang.String getSysCompanyCode(){
		return this.sysCompanyCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属公司
	 */
	public void setSysCompanyCode(java.lang.String sysCompanyCode){
		this.sysCompanyCode = sysCompanyCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流程状态
	 */
	@Column(name ="BPM_STATUS",nullable=true,length=32)
	public java.lang.String getBpmStatus(){
		return this.bpmStatus;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流程状态
	 */
	public void setBpmStatus(java.lang.String bpmStatus){
		this.bpmStatus = bpmStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  托盘编码
	 */
	@Column(name ="TUO_PAN_BIAN_MA",nullable=true,length=64)
	public java.lang.String getTuoPanBianMa(){
		return this.tuoPanBianMa;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  托盘编码
	 */
	public void setTuoPanBianMa(java.lang.String tuoPanBianMa){
		this.tuoPanBianMa = tuoPanBianMa;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  托盘条码
	 */
	@Column(name ="TUO_PAN_TIAO_MA",nullable=true,length=64)
	public java.lang.String getTuoPanTiaoMa(){
		return this.tuoPanTiaoMa;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  托盘条码
	 */
	public void setTuoPanTiaoMa(java.lang.String tuoPanTiaoMa){
		this.tuoPanTiaoMa = tuoPanTiaoMa;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  托盘类型
	 */
	@Column(name ="TUO_PAN_LEI_XING",nullable=true,length=64)
	public java.lang.String getTuoPanLeiXing(){
		return this.tuoPanLeiXing;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  托盘类型
	 */
	public void setTuoPanLeiXing(java.lang.String tuoPanLeiXing){
		this.tuoPanLeiXing = tuoPanLeiXing;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  托盘状态
	 */
	@Column(name ="TUO_PAN_ZHUANG_TAI",nullable=true,length=64)
	public java.lang.String getTuoPanZhuangTai(){
		return this.tuoPanZhuangTai;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  托盘状态
	 */
	public void setTuoPanZhuangTai(java.lang.String tuoPanZhuangTai){
		this.tuoPanZhuangTai = tuoPanZhuangTai;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  装料编码
	 */
	@Column(name ="ZHUANG_LIAO_BIAN_MA",nullable=true,length=64)
	public java.lang.String getZhuangLiaoBianMa(){
		return this.zhuangLiaoBianMa;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  装料编码
	 */
	public void setZhuangLiaoBianMa(java.lang.String zhuangLiaoBianMa){
		this.zhuangLiaoBianMa = zhuangLiaoBianMa;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  装料名称
	 */
	@Column(name ="ZHUANG_LIAO_MING_CHENG",nullable=true,length=64)
	public java.lang.String getZhuangLiaoMingCheng(){
		return this.zhuangLiaoMingCheng;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  装料名称
	 */
	public void setZhuangLiaoMingCheng(java.lang.String zhuangLiaoMingCheng){
		this.zhuangLiaoMingCheng = zhuangLiaoMingCheng;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  装料批次号
	 */
	@Column(name ="PI_CI_HAO",nullable=true,length=64)
	public java.lang.String getPiCiHao(){
		return this.piCiHao;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  装料批次号
	 */
	public void setPiCiHao(java.lang.String piCiHao){
		this.piCiHao = piCiHao;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  装料数量
	 */
	@Column(name ="SHU_LIANG",nullable=true,length=64)
	public java.lang.String getShuLiang(){
		return this.shuLiang;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  装料数量
	 */
	public void setShuLiang(java.lang.String shuLiang){
		this.shuLiang = shuLiang;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  装料重量
	 */
	@Column(name ="ZHONG_LIANG",nullable=true,length=64)
	public java.lang.String getZhongLiang(){
		return this.zhongLiang;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  装料重量
	 */
	public void setZhongLiang(java.lang.String zhongLiang){
		this.zhongLiang = zhongLiang;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  装料理论规格
	 */
	@Column(name ="LI_LUN_GUI_GE",nullable=true,length=64)
	public java.lang.String getLiLunGuiGe(){
		return this.liLunGuiGe;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  装料理论规格
	 */
	public void setLiLunGuiGe(java.lang.String liLunGuiGe){
		this.liLunGuiGe = liLunGuiGe;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  装料实际规格
	 */
	@Column(name ="SHI_JI_GUI_GE",nullable=true,length=64)
	public java.lang.String getShiJiGuiGe(){
		return this.shiJiGuiGe;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  装料实际规格
	 */
	public void setShiJiGuiGe(java.lang.String shiJiGuiGe){
		this.shiJiGuiGe = shiJiGuiGe;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  数量单位
	 */
	@Column(name ="SHU_LIANG_DAN_WEI",nullable=true,length=64)
	public java.lang.String getShuLiangDanWei(){
		return this.shuLiangDanWei;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  数量单位
	 */
	public void setShuLiangDanWei(java.lang.String shuLiangDanWei){
		this.shuLiangDanWei = shuLiangDanWei;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  重量单位
	 */
	@Column(name ="ZHONG_LIANG_DAN_WEI",nullable=true,length=64)
	public java.lang.String getZhongLiangDanWei(){
		return this.zhongLiangDanWei;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  重量单位
	 */
	public void setZhongLiangDanWei(java.lang.String zhongLiangDanWei){
		this.zhongLiangDanWei = zhongLiangDanWei;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  储位编码
	 */
	@Column(name ="BIN_BIAN_MA",nullable=true,length=64)
	public java.lang.String getBinBianMa(){
		return this.binBianMa;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  储位编码
	 */
	public void setBinBianMa(java.lang.String binBianMa){
		this.binBianMa = binBianMa;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  储位条码
	 */
	@Column(name ="BIN_TIAO_MA",nullable=true,length=64)
	public java.lang.String getBinTiaoMa(){
		return this.binTiaoMa;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  储位条码
	 */
	public void setBinTiaoMa(java.lang.String binTiaoMa){
		this.binTiaoMa = binTiaoMa;
	}

	@Column(name ="BIN_DEPTH",nullable=true,length=64)
	public Integer getBinDepth() {
		return binDepth;
	}

	public void setBinDepth(Integer binDepth) {
		this.binDepth = binDepth;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  停用
	 */
	@Column(name ="TING_YONG",nullable=true,length=64)
	public java.lang.String getTingYong(){
		return this.tingYong;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  停用
	 */
	public void setTingYong(java.lang.String tingYong){
		this.tingYong = tingYong;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  明细
	 */
	@Column(name ="MING_XI",nullable=true,length=64)
	public java.lang.String getMingXi(){
		return this.mingXi;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  明细
	 */
	public void setMingXi(java.lang.String mingXi){
		this.mingXi = mingXi;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  入库单明细主键
	 */
	@Column(name ="ENTRY_KEY",nullable=true,length=64)
	public java.lang.String getEntryKey(){
		return this.entryKey;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  入库单明细主键
	 */
	public void setEntryKey(java.lang.String entryKey){
		this.entryKey = entryKey;
	}
	
	@Column(name ="STORE_DATE",nullable=true)
	public Date getStoreDate() {
		return storeDate;
	}

	public void setStoreDate(Date storeDate) {
		this.storeDate = storeDate;
	}
	@Column(name ="max_count",nullable=true)
	public java.lang.String getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(java.lang.String maxCount) {
		this.maxCount = maxCount;
	}
	@Column(name ="max_weight",nullable=true)
	public java.lang.String getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(java.lang.String maxWeight) {
		this.maxWeight = maxWeight;
	}
	
}
