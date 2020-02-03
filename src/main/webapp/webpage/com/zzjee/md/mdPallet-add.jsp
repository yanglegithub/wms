<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>托盘管理</title>
  <meta name="description" content="">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="online/template/ledefault/css/vendor.css">
  <link rel="stylesheet" href="online/template/ledefault/css/bootstrap-theme.css">
  <link rel="stylesheet" href="online/template/ledefault/css/bootstrap.css">
  <link rel="stylesheet" href="online/template/ledefault/css/app.css">

  <link rel="stylesheet" href="plug-in/Validform/css/metrole/style.css" type="text/css"/>
  <link rel="stylesheet" href="plug-in/Validform/css/metrole/tablefrom.css" type="text/css"/>

    <style type="text/css">
        .combo-panel{overflow-y:scroll; border:1px solid #0aa8e4;}
        .combobox-item-hover{background-color: #0b96e5; color: white;}
        .combobox-item-selected{background-color: #0b96e5; color: white;}
    </style>

  <script type="text/javascript" src="plug-in/jquery/jquery-1.8.3.js"></script>
  <script type="text/javascript" src="plug-in/tools/dataformat.js"></script>
  <script type="text/javascript" src="plug-in/easyui/jquery.easyui.min.1.3.2.js"></script>
  <script type="text/javascript" src="plug-in/easyui/locale/zh-cn.js"></script>
  <script type="text/javascript" src="plug-in/tools/syUtil.js"></script>
  <script type="text/javascript" src="plug-in/My97DatePicker/WdatePicker.js"></script>
  <script type="text/javascript" src="plug-in/lhgDialog/lhgdialog.min.js"></script>
  <script type="text/javascript" src="plug-in/tools/curdtools_zh-cn.js"></script>
  <script type="text/javascript" src="plug-in/tools/easyuiextend.js"></script>
  <script type="text/javascript" src="plug-in/Validform/js/Validform_v5.3.1_min_zh-cn.js"></script>
  <script type="text/javascript" src="plug-in/Validform/js/Validform_Datatype_zh-cn.js"></script>
  <script type="text/javascript" src="plug-in/Validform/js/datatype_zh-cn.js"></script>
  <script type="text/javascript" src="plug-in/Validform/plugin/passwordStrength/passwordStrength-min.js"></script>
  <script type="text/javascript"  charset="utf-8" src="plug-in/ueditor/ueditor.config.js"></script>
  <script type="text/javascript"  charset="utf-8" src="plug-in/ueditor/ueditor.all.min.js"></script>

   <script type="text/javascript">
  //编写自定义JS代码
  </script>
</head>

 <body>

	<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="mdPalletController.do?doAdd" tiptype="1" >
			<input type="hidden" id="btn_sub" class="btn_sub"/>
			<input type="hidden" id="id" name="id"/>
			<div class="tab-wrapper">
			    <!-- tab -->
			    <ul class="nav nav-tabs">
			      <li role="presentation" class="active"><a href="javascript:void(0);">托盘管理</a></li>
			    </ul>
			    <!-- tab内容 -->
			    <div class="con-wrapper" id="con-wrapper1" style="display: block;">
			      <div class="row form-wrapper">
							<div class="row show-grid">
			          <div class="col-xs-3 text-center">
			          	<b style="color: red;">托盘编码：</b>
			          </div>
			          <div class="col-xs-3">
								<input id="tuoPanBianMa" name="tuoPanBianMa" type="text" class="form-control"
									ignore="checked"
								 datatype="*" />
						<span class="Validform_checktip" style="float:left;height:0px;"></span>
						<label class="Validform_label" style="display: none">托盘编码</label>
			          </div>

			          <div class="col-xs-3 text-center">
			          	<b style="color: red;">托盘条码：</b>
			          </div>
			          <div class="col-xs-3">
								<input id="tuoPanTiaoMa" name="tuoPanTiaoMa" type="text" class="form-control"
									ignore="checked"
								 datatype="*" />
						<span class="Validform_checktip" style="float:left;height:0px;"></span>
						<label class="Validform_label" style="display: none">托盘条码</label>
			          </div>
						</div>


							<div class="row show-grid">
			          <div class="col-xs-3 text-center">
			          	<b style="color: red;">托盘类型：</b>
			          </div>
			          <div class="col-xs-3">
								<t:dictSelect field="tuoPanLeiXing" type="list" extendJson="{class:'form-control'}" defaultVal="NORMAL"
								dictTable="ba_pallet_type" dictField="type_code" dictText="type_name" hasLabel="false"  title="托盘类型"></t:dictSelect>
						<span class="Validform_checktip" style="float:left;height:0px;"></span>
						<label class="Validform_label" style="display: none">托盘类型</label>
			          </div>

			          <div class="col-xs-3 text-center">
			          	<b style="color: red;">托盘状态：</b>
			          </div>
			          <div class="col-xs-3">
								<t:dictSelect field="tuoPanZhuangTai" type="list" extendJson="{class:'form-control'}" defaultVal="idle"
								typeGroupCode="pal_status" hasLabel="false"  title="托盘状态"></t:dictSelect>
						<span class="Validform_checktip" style="float:left;height:0px;"></span>
						<label class="Validform_label" style="display: none">托盘状态</label>
			          </div>
						</div>


							<div class="row show-grid">
			          <div class="col-xs-3 text-center">
			          	<b>装料编码：</b>
			          </div>
			          <div class="col-xs-3">
								<input id="zhuangLiaoBianMa" name="zhuangLiaoBianMa" type="text" class="form-control"
									ignore="ignore"
								 />
						<span class="Validform_checktip" style="float:left;height:0px;"></span>
						<label class="Validform_label" style="display: none">装料编码</label>
			          </div>

			          <div class="col-xs-3 text-center">
			          	<b>装料名称：</b>
			          </div>
			          <div class="col-xs-3">
								<input id="zhuangLiaoMingCheng" name="zhuangLiaoMingCheng" type="text" class="form-control"
									ignore="ignore"
								 />
						<span class="Validform_checktip" style="float:left;height:0px;"></span>
						<label class="Validform_label" style="display: none">装料名称</label>
			          </div>
						</div>


							<div class="row show-grid">
			          <div class="col-xs-3 text-center">
			          	<b>装料批次号：</b>
			          </div>
			          <div class="col-xs-3">
								<input id="piCiHao" name="piCiHao" type="text" class="form-control"
									ignore="ignore"
								 />
						<span class="Validform_checktip" style="float:left;height:0px;"></span>
						<label class="Validform_label" style="display: none">装料批次号</label>
			          </div>

			          <div class="col-xs-3 text-center">
			          	<b>装料数量：</b>
			          </div>
			          <div class="col-xs-3">
								<input id="shuLiang" name="shuLiang" type="text" class="form-control"
									ignore="ignore"
								 />
						<span class="Validform_checktip" style="float:left;height:0px;"></span>
						<label class="Validform_label" style="display: none">装料数量</label>
			          </div>
						</div>


							<div class="row show-grid">
			          <div class="col-xs-3 text-center">
			          	<b>装料重量：</b>
			          </div>
			          <div class="col-xs-3">
								<input id="zhongLiang" name="zhongLiang" type="text" class="form-control"
									ignore="ignore"
								 />
						<span class="Validform_checktip" style="float:left;height:0px;"></span>
						<label class="Validform_label" style="display: none">装料重量</label>
			          </div>
						</div>


							<div class="row show-grid">
			          <div class="col-xs-3 text-center">
			          	<b>装料理论规格：</b>
			          </div>
			          <div class="col-xs-3">
								<input id="liLunGuiGe" name="liLunGuiGe" type="text" class="form-control"
									ignore="ignore"
								 />
						<span class="Validform_checktip" style="float:left;height:0px;"></span>
						<label class="Validform_label" style="display: none">装料理论规格</label>
			          </div>

			          <div class="col-xs-3 text-center">
			          	<b>装料实际规格：</b>
			          </div>
			          <div class="col-xs-3">
								<input id="shiJiGuiGe" name="shiJiGuiGe" type="text" class="form-control"
									ignore="ignore"
								 />
						<span class="Validform_checktip" style="float:left;height:0px;"></span>
						<label class="Validform_label" style="display: none">装料实际规格</label>
			          </div>
						</div>


							<div class="row show-grid">
			          <div class="col-xs-3 text-center">
			          	<b>数量单位：</b>
			          </div>
			          <div class="col-xs-3">
								<t:dictSelect field="shuLiangDanWei" type="list" extendJson="{class:'form-control'}" defaultVal="包"
								dictTable="ba_unit" dictField="unit_code" dictText="unit_zh_name" dictCondition=" where unit_type='U02' " hasLabel="false"  title="数量单位"></t:dictSelect>
						<span class="Validform_checktip" style="float:left;height:0px;"></span>
						<label class="Validform_label" style="display: none">数量单位</label>
			          </div>

			          <div class="col-xs-3 text-center">
			          	<b>重量单位：</b>
			          </div>
			          <div class="col-xs-3">
								<t:dictSelect field="zhongLiangDanWei" type="list" extendJson="{class:'form-control'}" defaultVal="KG"
								typeGroupCode="w_unit" hasLabel="false"  title="重量单位"></t:dictSelect>
						<span class="Validform_checktip" style="float:left;height:0px;"></span>
						<label class="Validform_label" style="display: none">重量单位</label>
			          </div>
						</div>


							<div class="row show-grid">
			          <div class="col-xs-3 text-center">
			          	<b>储位编码：</b>
			          </div>
			          <div class="col-xs-3">
								<%--<input id="binBianMa" name="binBianMa" type="text" class="form-control"--%>
									<%--ignore="ignore"--%>
								 <%--/>--%>
                                    <t:dictSelect field="binBianMa" type="list" extendJson="{class:'form-control easyui-combobox'}"
                                                  dictTable="md_bin" dictField="ku_wei_bian_ma" dictText="ku_wei_bian_ma" hasLabel="false"  title="储位编码"></t:dictSelect>
						<span class="Validform_checktip" style="float:left;height:0px;"></span>
						<label class="Validform_label" style="display: none">储位编码</label>
			          </div>

			          <div class="col-xs-3 text-center">
			          	<b>储位条码：</b>
			          </div>
			          <div class="col-xs-3">
								<%--<input id="binTiaoMa" name="binTiaoMa" type="text" class="form-control"--%>
									<%--ignore="ignore"--%>
								 <%--/>--%>
                                    <t:dictSelect field="binTiaoMa" type="list" extendJson="{class:'form-control easyui-combobox'}"
                                                  dictTable="md_bin" dictField="ku_wei_tiao_ma" dictText="ku_wei_tiao_ma" hasLabel="false"  title="储位条码"></t:dictSelect>
						<span class="Validform_checktip" style="float:left;height:0px;"></span>
						<label class="Validform_label" style="display: none">储位条码</label>
			          </div>
						</div>


							<div class="row show-grid">
			          <div class="col-xs-3 text-center">
			          	<b>停用：</b>
			          </div>
			          <div class="col-xs-3">
								<t:dictSelect field="tingYong" type="radio" extendJson="{class:'form-control'}" defaultVal="N"
								typeGroupCode="sf_yn" hasLabel="false"  title="停用"></t:dictSelect>
						<span class="Validform_checktip" style="float:left;height:0px;"></span>
						<label class="Validform_label" style="display: none">停用</label>
			          </div>
						</div>


							<div class="row show-grid">
			          <div class="col-xs-3 text-center">
			          	<b>明细：</b>
			          </div>
			          <div class="col-xs-3">
								<input id="mingXi" name="mingXi" type="text" class="form-control"
									ignore="ignore"
								 />
						<span class="Validform_checktip" style="float:left;height:0px;"></span>
						<label class="Validform_label" style="display: none">明细</label>
			          </div>
						</div>




			          <div class="row" id = "sub_tr" style="display: none;">
				        <div class="col-xs-12 layout-header">
				          <div class="col-xs-6"></div>
				          <div class="col-xs-6"><button type="button" onclick="neibuClick();" class="btn btn-default">提交</button></div>
				        </div>
				      </div>
			     </div>
			   </div>

			   <div class="con-wrapper" id="con-wrapper2" style="display: block;"></div>
			 </div>
  </t:formvalid>

<script type="text/javascript">
   $(function(){
    //查看模式情况下,删除和上传附件功能禁止使用
	if(location.href.indexOf("load=detail")!=-1){
		$(".jeecgDetail").hide();
	}

	if(location.href.indexOf("mode=read")!=-1){
		//查看模式控件禁用
		$("#formobj").find(":input").attr("disabled","disabled");
	}
	if(location.href.indexOf("mode=onbutton")!=-1){
		//其他模式显示提交按钮
		$("#sub_tr").show();
	}


	//下拉搜索框样式和响应
    $('.easyui-combobox+.combo .combo-text').addClass('form-control');
	$('.easyui-combobox+.combo .combo-text').css({"width":"","height":""});

   });

  var neibuClickFlag = false;
  function neibuClick() {
	  neibuClickFlag = true;
	  $('#btn_sub').trigger('click');
  }

</script>
 </body>
<script src = "webpage/com/zzjee/md/mdPallet.js"></script>
</html>
