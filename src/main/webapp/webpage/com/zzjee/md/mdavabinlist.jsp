<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<link rel="stylesheet" type="text/css" href="plug-in/jquery-seat-charts/css/jquery.seat-charts.css">
<link rel="stylesheet" type="text/css" href="plug-in/jquery-seat-charts/css/seat.css">
<t:base type="jquery,easyui,tools"></t:base>
<div class="easyui-layout" fit="true">
  <div  style="padding:0px;border:0px">
    <div name="searchColums" style="float: left; padding-left: 0px;padding-top: 5px;">
          <span style="vertical-align:middle;display:-moz-inline-box;display:inline-block;width: 90px;text-align:right;" title="仓库">仓库: </span>
              <select name="store" style="width: 100px; height: 30px;">
              <c:forEach items="${baStoreList}" var="op"><option value="${op.id}">${op.storeName}</option></c:forEach></select>
         </span>
<!--       <span style="vertical-align:middle;display:-moz-inline-box;display:inline-block;width: 90px;text-align:right;" title="储位">储位: </span>
              <input type="text" name="chuwei" style="width: 100px; height: 30px;">
         </span>
               <span style="vertical-align:middle;display:-moz-inline-box;display:inline-block;width: 90px;text-align:right;" title="其他">其他: </span>
              <input type="text" name="des" style="width: 100px; height: 30px;">
         </span> -->
        <span style="vertical-align:middle;display:-moz-inline-box;display:inline-block;width: 90px;text-align:right;"  >
          <button onclick="chaxun()" >查询</button>  </span>
         </div>


  </div>
</div>
<div id="seat-map"></div>
<div id="legend"></div>
<script src="plug-in/jquery-seat-charts/js/jquery.seat-charts.js"></script>
<script src="plug-in/layer/layer.js"></script>
 <script type="text/javascript">
 function addtab(name){
	 var str = name.replace('|','\n');
 }
 $(document).ready(function(){
	 chaxun();
// 	 $("#mvCusCostListtb").find("input[name='outtime_begin']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
// 	 $("#mvCusCostListtb").find("input[name='outtime_end']").attr("class","Wdate").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});

 });

	function chaxun(){
		//获取map数据 
		 var map = [
				'eeeeeeee_____fffffff__eeeeee__eee__ee_________',
				'e____________eeeeeee__eeeeee__eee__ee_________',
				'e____________eeeeeee__eeeeee__eee__ee_________',
				'e____________eeeeeee__eeeeee__eee__ee_________',
				'e____________eeeeeee__eeeeee__eee__ee_________',
				'eeeeeeee_____eeeeeee__eeeeee__eee__ee_________',
				'eeeeeeee_____eeeeeee__eeeeee__eee__ee_________',
				'e____________eeeeeee__eeeeee__eee__ee_________',
				'e_____________________________________________',
				'e_____________________________________________',
				'e_____________________________________________',
				'eeeeeeee_____eeeeeeee__________eeee_eeee______',
				'eeeeeeee_____eeeeeeee__________eeee_eeee______',
				'e____________eeeeeeee__________eeee_eeee______',
				'e____________eeeeeeee__________eeee_eeee______',
				'e____________eeeeeeee__________eeee_eeee______',
				'e____________eeeeeeee__________eeee_eeee______',
				'eeeeeeee_____eeeeeee____________eee_eee_______'
				];
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			data:{'mdStoreId':$('select[name="store"]').val()},
			url : 'mdBinController.do?getMdBinView', // 请求的action路径
			error : function() {// 请求失败处理函数
			},
			success : function(data) {
				var d = $.parseJSON(data);
				if (d.success) {
					//$.dialog.tips('操作成功', 2);
					map = d.obj;
				}

			}
		});
		var firstSeatLabel = 1;
		var $cart = $('#selected-seats');
		sc = $('#seat-map').seatCharts({
			map: map,
			seats: {
				f: {
					classes : 'first-class', //your custom CSS class
					category: 'First Class'
				},
				e: {
					classes : 'economy-class', //your custom CSS class
					category: 'Economy Class'
				}					
			},
			naming : {
				top : false,
				left : false,
				getLabel : function (character, row, column) {
					return firstSeatLabel++;
				},
			},
		legend : {

			node : $('#legend'),

		    items : [
				[ 'f', 'available',   '已用储位' ],
				[ 'e', 'available',   '空闲储位']
		    ]					
		},
		click: function () {
			var id = this.settings.id;
			var xy = "{"+(parseInt(id.split("_")[0])-1) + ":" + (parseInt(id.split("_")[1])-1) + "}";
			if(this.settings.character == "e"){
				$.dialog.tips('该位置暂未放置托盘', 2);
			}
			
			if(this.settings.character == "f"){
				layer.open({
					  type: 2,
					  title: '托盘详情',
					  shadeClose: true,
					  area: ['600px', '90%'],
					  content: 'mdBinController.do?getMdBinByXY&xy='+xy//iframe的url
					});
			}
			return this.status();
		}

	});

	}
	//this will handle "[cancel]" link clicks

	$('#selected-seats').on('click', '.cancel-cart-item', function () {

		//let's just trigger Click event on the appropriate seat, so we don't have to repeat the logic here

		sc.get($(this).parents('li:first').data('seatId')).click();

	});

 </script>
