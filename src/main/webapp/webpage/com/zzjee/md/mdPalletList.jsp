<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="mdPalletList" checkbox="true" pagination="true" fitColumns="false" title="托盘管理" actionUrl="mdPalletController.do?datagrid" idField="id" fit="true" queryMode="group">
    <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="创建人名称"  field="createName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="创建人登录名称"  field="createBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="创建日期"  field="createDate" formatter="yyyy-MM-dd" hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="更新人名称"  field="updateName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="更新人登录名称"  field="updateBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="更新日期"  field="updateDate" formatter="yyyy-MM-dd" hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="所属部门"  field="sysOrgCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="所属公司"  field="sysCompanyCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="流程状态"  field="bpmStatus"  hidden="true"  queryMode="single" dictionary="bpm_status" width="120"></t:dgCol>
    <t:dgCol title="托盘编码"  field="tuoPanBianMa"   query="true" queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="托盘条码"  field="tuoPanTiaoMa"    queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="托盘类型"  field="tuoPanLeiXing"   query="true" queryMode="single" dictionary="ba_pallet_type,type_code,type_name"  width="120"></t:dgCol>
    <t:dgCol title="托盘状态"  field="tuoPanZhuangTai"   query="true" queryMode="single" dictionary="pal_status" width="120"></t:dgCol>
    <t:dgCol title="装料编码"  field="zhuangLiaoBianMa"   query="true" queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="装料名称"  field="zhuangLiaoMingCheng"   query="true" queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="装料批次号"  field="piCiHao"    queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="装料数量"  field="shuLiang"    queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="装料重量"  field="zhongLiang"    queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="装料理论规格"  field="liLunGuiGe"    queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="装料实际规格"  field="shiJiGuiGe"    queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="数量单位"  field="shuLiangDanWei"    queryMode="single" dictionary="ba_unit,unit_code,unit_zh_name"  width="120"></t:dgCol>
    <t:dgCol title="重量单位"  field="zhongLiangDanWei"    queryMode="single" dictionary="w_unit" width="120"></t:dgCol>
    <t:dgCol title="储位编码"  field="binBianMa"    queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="储位条码"  field="binTiaoMa"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="停用"  field="tingYong"    queryMode="single" dictionary="sf_yn" width="120"></t:dgCol>
    <t:dgCol title="明细"  field="mingXi"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="mdPalletController.do?doDel&id={id}" urlclass="ace_button"  urlfont="fa-trash-o"/>
   <t:dgToolBar title="录入" icon="icon-add" url="mdPalletController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="mdPalletController.do?goUpdate" funname="update"></t:dgToolBar>
   <t:dgToolBar title="批量删除"  icon="icon-remove" url="mdPalletController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="mdPalletController.do?goUpdate" funname="detail"></t:dgToolBar>
   <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
   <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/com/zzjee/md/mdPalletList.js"></script>		
 <script type="text/javascript">
 $(document).ready(function(){
 });
 
   
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'mdPalletController.do?upload', "mdPalletList");
}

//导出
function ExportXls() {
	JeecgExcelExport("mdPalletController.do?exportXls","mdPalletList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("mdPalletController.do?exportXlsByT","mdPalletList");
}

 </script>