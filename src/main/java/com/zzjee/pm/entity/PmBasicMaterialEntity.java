package com.zzjee.pm.entity;

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

/**
 * @Title: Entity
 * @Description: 物料信息
 * @author onlineGenerator
 * @date 2019-12-09 08:43:12
 * @version V1.0
 *
 */
@Entity
@Table(name = "pm_basic_material", schema = "")
@SuppressWarnings("serial")
public class PmBasicMaterialEntity implements java.io.Serializable {
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
	/**物料编码*/
	@Excel(name="物料编码")
	private java.lang.String stuffCode;
	/**物料名称*/
	@Excel(name="物料名称")
	private java.lang.String stuffName;
	/**物料类型*/
	@Excel(name="物料类型")
	private java.lang.String stuffType;
	/**物料分类*/
	@Excel(name="物料分类")
	private java.lang.String stuffClass;
	/**密度*/
	@Excel(name="密度")
	private java.lang.String density;
	/**保质期_天*/
	@Excel(name="保质期-天")
	private java.lang.String warranty;
	/**备注*/
	@Excel(name="备注")
	private java.lang.String remarks;

	/**nc物料主键id*/
	@Excel(name="nc物料主键id")
	private java.lang.String ncPkid;

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
	 *@return: java.lang.String  物料编码
	 */
	@Column(name ="STUFF_CODE",nullable=true,length=32)
	public java.lang.String getStuffCode(){
		return this.stuffCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  物料编码
	 */
	public void setStuffCode(java.lang.String stuffCode){
		this.stuffCode = stuffCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  物料名称
	 */
	@Column(name ="STUFF_NAME",nullable=true,length=32)
	public java.lang.String getStuffName(){
		return this.stuffName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  物料名称
	 */
	public void setStuffName(java.lang.String stuffName){
		this.stuffName = stuffName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  物料类型
	 */
	@Column(name ="STUFF_TYPE",nullable=true,length=32)
	public java.lang.String getStuffType(){
		return this.stuffType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  物料类型
	 */
	public void setStuffType(java.lang.String stuffType){
		this.stuffType = stuffType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  物料分类
	 */
	@Column(name ="STUFF_CLASS",nullable=true,length=32)
	public java.lang.String getStuffClass(){
		return this.stuffClass;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  物料分类
	 */
	public void setStuffClass(java.lang.String stuffClass){
		this.stuffClass = stuffClass;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  密度
	 */
	@Column(name ="DENSITY",nullable=true,length=32)
	public java.lang.String getDensity(){
		return this.density;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  密度
	 */
	public void setDensity(java.lang.String density){
		this.density = density;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  保质期_天
	 */
	@Column(name ="WARRANTY",nullable=true,length=32)
	public java.lang.String getWarranty(){
		return this.warranty;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  保质期_天
	 */
	public void setWarranty(java.lang.String warranty){
		this.warranty = warranty;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="REMARKS",nullable=true,length=516)
	public java.lang.String getRemarks(){
		return this.remarks;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRemarks(java.lang.String remarks){
		this.remarks = remarks;
	}

	@Column(name ="nc_pkid",nullable=true,length=64)
	public String getNcPkid() {
		return ncPkid;
	}

	public void setNcPkid(String ncPkid) {
		this.ncPkid = ncPkid;
	}
}
