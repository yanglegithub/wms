package com.zzjee.md.entity;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "md_exception", schema = "")
@SuppressWarnings("serial")
public class MdExceptionEntity {
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
    /**异常类型*/
    @Excel(name="异常类型")
    private java.lang.String type;
    /**异常详情*/
    @Excel(name="异常详情")
    private java.lang.String detail;
    /**异常内容*/
    @Excel(name="异常内容")
    private java.lang.String content;
    /**托盘id*/
    @Excel(name="托盘id")
    private java.lang.String palletId;
    /**托盘条码*/
    @Excel(name="托盘条码")
    private java.lang.String palletCode;
    /**货位id*/
    @Excel(name="货位id")
    private java.lang.String binId;
    /**货位条码*/
    @Excel(name="货位条码")
    private java.lang.String binCode;
    /**状态*/
    @Excel(name="状态")
    private java.lang.String status;

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

    @Column(name ="type",nullable=true,length=64)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name ="detail",nullable=true,length=256)
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Column(name ="content",nullable=true,length=64)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name ="pallet_id",nullable=true,length=64)
    public String getPalletId() {
        return palletId;
    }

    public void setPalletId(String palletId) {
        this.palletId = palletId;
    }

    @Column(name ="pallet_code",nullable=true,length=64)
    public String getPalletCode() {
        return palletCode;
    }

    public void setPalletCode(String palletCode) {
        this.palletCode = palletCode;
    }

    @Column(name ="bin_id",nullable=true,length=64)
    public String getBinId() {
        return binId;
    }

    public void setBinId(String binId) {
        this.binId = binId;
    }

    @Column(name ="bin_code",nullable=true,length=64)
    public String getBinCode() {
        return binCode;
    }

    public void setBinCode(String binCode) {
        this.binCode = binCode;
    }

    @Column(name ="status",nullable=true,length=64)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
