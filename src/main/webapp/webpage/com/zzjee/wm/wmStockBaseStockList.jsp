<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
    <t:datagrid name="wmStockBaseStockList" checkbox="false" pagination="true" fitColumns="false" title="库存表"
                actionUrl="wmStockBaseStockController.do?datagrid" idField="id" fit="true" queryMode="group">
      <t:dgCol title="主键" field="id" hidden="true" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="创建人名称" field="createName" hidden="true" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="创建人登录名称" field="createBy" hidden="true" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="创建日期" field="createDate" formatter="yyyy-MM-dd" hidden="true" queryMode="single"
               width="120"></t:dgCol>
      <t:dgCol title="更新人名称" field="updateName" hidden="true" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="更新人登录名称" field="updateBy" hidden="true" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="更新日期" field="updateDate" formatter="yyyy-MM-dd" hidden="true" queryMode="single"
               width="120"></t:dgCol>
      <t:dgCol title="所属部门" field="sysOrgCode" hidden="true" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="所属公司" field="sysCompanyCode" hidden="true" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="流程状态" field="bpmStatus" queryMode="single" dictionary="bpm_status" width="120"></t:dgCol>
      <t:dgCol title="库存类型" field="kuctype" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="库位编码" field="kuWeiBianMa" query="true" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="库位id" field="binId" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="客户编码" field="cusCode" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="中文" field="zhongWenQch" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="商品编码" field="goodsId" query="true" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="商品数量" field="goodsQua" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="订单id" field="orderId" query="true" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="商品生产日期" field="goodsProData" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="单位" field="goodsUnit" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="基本单位" field="baseUnit" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="基本数量" field="baseGoodscount" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="商品保质期" field="goodsBzhiqi" query="true" queryMode="single" width="120"></t:dgCol>
      <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
      <%--<t:dgDelOpt title="删除" url="wmStockBaseStockController.do?doDel&id={id}" urlclass="ace_button"
                  urlfont="fa-trash-o"/>
      <t:dgToolBar title="录入" icon="icon-add" url="wmStockBaseStockController.do?goAdd" funname="add"></t:dgToolBar>
      <t:dgToolBar title="编辑" icon="icon-edit" url="wmStockBaseStockController.do?goUpdate"
                   funname="update"></t:dgToolBar>
      <t:dgToolBar title="批量删除" icon="icon-remove" url="wmStockBaseStockController.do?doBatchDel"
                   funname="deleteALLSelect"></t:dgToolBar>
      <t:dgToolBar title="查看" icon="icon-search" url="wmStockBaseStockController.do?goUpdate"
                   funname="detail"></t:dgToolBar>
      <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
      <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
      <t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>--%>
      <t:dgFunOpt title="查看" funname="mydetail(id)" urlclass="ace_button" urlfont="fa-eye"></t:dgFunOpt>
    </t:datagrid>
  </div>
</div>
<script src="webpage/com/zzjee/wm/wmStockBaseStockList.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
    });


    //导入
    function ImportXls() {
        openuploadwin('Excel导入', 'wmStockBaseStockController.do?upload', "wmStockBaseStockList");
    }

    //导出
    function ExportXls() {
        JeecgExcelExport("wmStockBaseStockController.do?exportXls", "wmStockBaseStockList");
    }

    //模板下载
    function ExportXlsByT() {
        JeecgExcelExport("wmStockBaseStockController.do?exportXlsByT", "wmStockBaseStockList");
    }

    //查看
    function mydetail(id){
        //debugger;
        //detail("查看","wmStockBaseStockController.do?goUpdate",id);
        var url = 'wmStockBaseStockController.do?goUpdate&load=detail&id='+id;
        createdetailwindow("查看",url,null,null);
    }

</script>
