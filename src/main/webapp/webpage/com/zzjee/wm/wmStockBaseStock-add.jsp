<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>库存表</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="div" action="wmStockBaseStockController.do?doAdd" >
			<input id="id" name="id" type="hidden" value="${wmStockBaseStockPage.id }">
			<input id="createName" name="createName" type="hidden" value="${wmStockBaseStockPage.createName }">
			<input id="createBy" name="createBy" type="hidden" value="${wmStockBaseStockPage.createBy }">
			<input id="createDate" name="createDate" type="hidden" value="${wmStockBaseStockPage.createDate }">
			<input id="updateName" name="updateName" type="hidden" value="${wmStockBaseStockPage.updateName }">
			<input id="updateBy" name="updateBy" type="hidden" value="${wmStockBaseStockPage.updateBy }">
			<input id="updateDate" name="updateDate" type="hidden" value="${wmStockBaseStockPage.updateDate }">
			<input id="sysOrgCode" name="sysOrgCode" type="hidden" value="${wmStockBaseStockPage.sysOrgCode }">
			<input id="sysCompanyCode" name="sysCompanyCode" type="hidden" value="${wmStockBaseStockPage.sysCompanyCode }">
			<input id="bpmStatus" name="bpmStatus" type="hidden" value="${wmStockBaseStockPage.bpmStatus }">
		<fieldset class="step">
			<div class="form">
		      <label class="Validform_label">库存类型:</label>
		     	 <input id="kuctype" name="kuctype" type="text" style="width: 150px" class="inputxt" 
									ignore="ignore"
									 />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">库位编码:</label>
		     	 <input id="kuWeiBianMa" name="kuWeiBianMa" type="text" style="width: 150px" class="inputxt" 
									ignore="ignore"
									 />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">库位id:</label>
		     	 <input id="binId" name="binId" type="text" style="width: 150px" class="inputxt" 
									ignore="ignore"
									 />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">客户编码:</label>
		     	 <input id="cusCode" name="cusCode" type="text" style="width: 150px" class="inputxt" 
									ignore="ignore"
									 />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">中文:</label>
		     	 <input id="zhongWenQch" name="zhongWenQch" type="text" style="width: 150px" class="inputxt" 
									ignore="ignore"
									 />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">商品编码:</label>
		     	 <input id="goodsId" name="goodsId" type="text" style="width: 150px" class="inputxt" 
									ignore="ignore"
									 />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">商品数量:</label>
		     	 <input id="goodsQua" name="goodsQua" type="text" style="width: 150px" class="inputxt" 
									ignore="ignore"
									 />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">订单id:</label>
		     	 <input id="orderId" name="orderId" type="text" style="width: 150px" class="inputxt" 
									ignore="ignore"
									 />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">商品生产日期:</label>
		     	 <input id="goodsProData" name="goodsProData" type="text" style="width: 150px" class="inputxt" 
									ignore="ignore"
									 />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">单位:</label>
		     	 <input id="goodsUnit" name="goodsUnit" type="text" style="width: 150px" class="inputxt" 
									ignore="ignore"
									 />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">基本单位:</label>
		     	 <input id="baseUnit" name="baseUnit" type="text" style="width: 150px" class="inputxt" 
									ignore="ignore"
									 />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">基本数量:</label>
		     	 <input id="baseGoodscount" name="baseGoodscount" type="text" style="width: 150px" class="inputxt" 
									ignore="ignore"
									 />
		      <span class="Validform_checktip"></span>
		    </div>
			<div class="form">
		      <label class="Validform_label">商品保质期:</label>
		     	 <input id="goodsBzhiqi" name="goodsBzhiqi" type="text" style="width: 150px" class="inputxt" 
									ignore="ignore"
									 />
		      <span class="Validform_checktip"></span>
		    </div>
	    </fieldset>
  </t:formvalid>
 </body>
  <script src = "webpage/com/zzjee/wm/wmStockBaseStock.js"></script>	
	