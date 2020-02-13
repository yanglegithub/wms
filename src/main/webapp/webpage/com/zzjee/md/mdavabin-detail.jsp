<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>托盘详情</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="online/template/ledefault/css/vendor.css">
<link rel="stylesheet"
	href="online/template/ledefault/css/bootstrap-theme.css">
<link rel="stylesheet"
	href="online/template/ledefault/css/bootstrap.css">
<link rel="stylesheet" href="online/template/ledefault/css/app.css">

<link rel="stylesheet" href="plug-in/Validform/css/metrole/style.css"
	type="text/css" />
<link rel="stylesheet"
	href="plug-in/Validform/css/metrole/tablefrom.css" type="text/css" />

<script type="text/javascript" src="plug-in/jquery/jquery-1.8.3.js"></script>
<script type="text/javascript" src="plug-in/tools/dataformat.js"></script>
<script type="text/javascript"
	src="plug-in/easyui/jquery.easyui.min.1.3.2.js"></script>
<script type="text/javascript" src="plug-in/easyui/locale/zh-cn.js"></script>
<script type="text/javascript" src="plug-in/tools/syUtil.js"></script>
<script type="text/javascript" src="plug-in/lhgDialog/lhgdialog.min.js"></script>
<script type="text/javascript" src="plug-in/tools/curdtools_zh-cn.js"></script>
<script type="text/javascript" src="plug-in/tools/easyuiextend.js"></script>


<script type="text/javascript">
	//编写自定义JS代码
</script>
</head>

<body>
	<div class="tab-wrapper">
		<!-- tab内容 -->
		<div class="con-wrapper" id="con-wrapper1" style="display: block;">
			<c:forEach items="${mdPallets}" var="op">
				<div class="row form-wrapper" style="margin-top:25px">
					<div class="row show-grid">
						<div class="col-xs-3 text-center">
							<b style="color: red">托盘编码</b>
						</div>
						<div class="col-xs-3">
							<input name="kuWeiBianMa" type="text" value="${op.tuoPanBianMa}" readonly
								class="form-control" ignore="checked" datatype="*" /> <span
								class="Validform_checktip" style="float: left; height: 0px;"></span>
							<label class="Validform_label" style="display: none">托盘编码</label>
						</div>
						<div class="col-xs-3 text-center">
							<b style="color: red">托盘条码</b>
						</div>
						<div class="col-xs-3">
							<input name="kuWeiTiaoMa" type="text" value="${op.tuoPanTiaoMa}" readonly
								class="form-control" ignore="checked" datatype="*" /> <span
								class="Validform_checktip" style="float: left; height: 0px;"></span>
							<label class="Validform_label" style="display: none">托盘条码</label>
						</div>
					</div>
					<div class="row show-grid">
						<div class="col-xs-3 text-center">
							<b style="color: red">装料编码 </b>
						</div>
						<div class="col-xs-3">
							<input name="shangJiaCiXu" type="text" value="${op.zhuangLiaoBianMa}" readonly
								class="form-control" ignore="checked" datatype="*" /> 
							<span class="Validform_checktip"
								style="float: left; height: 0px;"></span> <label
								class="Validform_label" style="display: none">装料编码 </label>
						</div>

						<div class="col-xs-3 text-center">
							<b style="color: red">装料名称</b>
						</div>
						<div class="col-xs-3">
							<input name="shangJiaCiXu" type="text" value="${op.zhuangLiaoMingCheng}" readonly
								class="form-control" ignore="checked" datatype="*" /> 
							<span class="Validform_checktip"
								style="float: left; height: 0px;"></span> <label
								class="Validform_label" style="display: none">装料名称</label>
						</div>
					</div>
					<div class="row show-grid">
						<div class="col-xs-3 text-center">
							<b style="color: red">装料批次号</b>
						</div>
						<div class="col-xs-3">
							<input name="shangJiaCiXu" type="text" value="${op.piCiHao}" readonly
								class="form-control" ignore="checked" datatype="*" /> 
							<span class="Validform_checktip"
								style="float: left; height: 0px;"></span> <label
								class="Validform_label" style="display: none">装料批次号</label>
						</div>
					</div>
					<div class="row show-grid">
						<div class="col-xs-3 text-center">
							<b style="color: red">装料数量 </b>
						</div>
						<div class="col-xs-3">
							<input name="shangJiaCiXu" type="text" value="${op.shuLiang}" readonly
								class="form-control" ignore="checked" datatype="*" /> <span
								class="Validform_checktip" style="float: left; height: 0px;"></span>
							<label class="Validform_label" style="display: none">装料数量 </label>
						</div>
						<div class="col-xs-3 text-center">
							<b style="color: red">装料重量</b>
						</div>
						<div class="col-xs-3">
							<input name="quHuoCiXu" type="text" value="${op.zhongLiang}" readonly
								class="form-control" ignore="checked" datatype="*" /> <span
								class="Validform_checktip" style="float: left; height: 0px;"></span>
							<label class="Validform_label" style="display: none">装料重量</label>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
</body>
</html>