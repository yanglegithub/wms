package org.jeecgframework.test.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.zzjee.api.utils.NCDataObject;
import com.zzjee.api.utils.ResultObject;
import com.zzjee.wm.entity.WmImNoticeHEntity;
import com.zzjee.wm.entity.WmImNoticeIEntity;
import com.zzjee.wm.entity.WmOmQmIEntity;
import com.zzjee.wmutil.WmsContants;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JunitTest {
    @Autowired
    RuntimeService runtimeService;

    @Test
    public void resultObjectGsonTest(){
        List<Map> listObj = new ArrayList<Map>();
        WmOmQmIEntity temp = new WmOmQmIEntity();
        Map map = new HashMap<String, Object>();
        map.put("key_name","000");
        map.put("key_string","value1");
        map.put("key_int",2);
        map.put("key_double",123.222);
        map.put("key_bolean",true);
        Map map1 = (Map) ((HashMap) map).clone();
        Map map2 = (Map) ((HashMap) map).clone();
        map1.put("key_name","001");
        map2.put("key_name","002");
        listObj.add(map);
        listObj.add(map1);
        listObj.add(map2);
        ResultObject obj = ResultObject.success("查询成功",listObj);

        System.out.println(obj.generateJsonStr());
    }

//    @Test
//    public void gsonTest(){
//        String jsonStr = "{\"success\":\"true\",\"length\":1,\"result\":[{\"session\":\"dpbkaibbfkobniibkhdhkeapmcldfnjicddfimfbpmlplpic\"}]}";
//        Gson gson = new Gson();
//        NCDataObject object = gson.fromJson(jsonStr,NCDataObject.class);
//        System.out.println((String) ((List<Map>)object.getResult()).get(0).get("session"));
//
//        runtimeService.startProcessInstanceByKey("","",null);
//    }

    @Test
    public void gsonTest2(){
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("key_string","string");
        map.put("key_int",2);
        map.put("key_pojo",ResultObject.success());
        Gson gson = new Gson();
        String str = gson.toJson(map);
        System.out.println(str);
    }

    @Test
    public void JSONObjectTest(){
        NCDataObject data = null;
        String dataStr = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Gson gson = new Gson();
//        data = requestMethode("alWMSQry","genIn","{\"vbillcode\":\""+imKey+"\",\"dbilldate\":\""+imDate+"\"}");
        try {
            FileInputStream in = new FileInputStream(new File("C:\\Users\\acer\\Desktop\\findImNotice1.txt"));

            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            dataStr = new String(bytes);
            NCDataObject obj = JSONObject.parseObject(dataStr,NCDataObject.class);

            for (Map<String, Object> mapTemp : (List<Map>) obj.getResult()) {
                String dataStrTemp = (String) mapTemp.get("data");
                List<Map> datatemp = (List)JSONObject.parseArray(dataStrTemp);
                for(Map temp : datatemp){
                    WmImNoticeHEntity hEntity = new WmImNoticeHEntity();
                    hEntity.setNoticeId((String) temp.get("InStorageNo"));
                    try {
                        hEntity.setImData(format.parse((String) temp.get("storeDate")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    hEntity.setQcStaff((String) temp.get("QCstaff"));
                    hEntity.setOrderTypeCode("05");
//                    hEntity.setImSta(WmsContants.WM_IM_H_PLANNING);
                    for (Map<String, Object> iMap : (List<Map>) temp.get("detail")) {
                        WmImNoticeIEntity iEntity = new WmImNoticeIEntity();
                        iEntity.setImNoticeId(hEntity.getNoticeId());
                        iEntity.setGoodsCode((String) iMap.get("materialCode"));
                        iEntity.setGoodsName((String) iMap.get("maretialName"));
                        iEntity.setGoodsBatch((String) iMap.get("materialBatchNo"));
                        iEntity.setBzhiQi((String) iMap.get("warranty"));
                        try {
                            iEntity.setGoodsPrdData(format.parse((String) iMap.get("manufactureDate")));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        iEntity.setGoodsWeight((String) iMap.get("storeWeight"));
                        iEntity.setGoodsCount((String) iMap.get("storeQuantity"));
                        iEntity.setShpGuiGe((String) iMap.get("specification"));
                        if (hEntity.getSupCode() == null) {
                            hEntity.setSupName((String) iMap.get("supplierName"));
                            hEntity.setSupCode((String) iMap.get("supplierNo"));
                        }
//                        iEntity.setNoticeiSta(WmsContants.WM_IM_I_PLANNING);
                    }
                }
            }


            System.out.println(dataStr);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件读取错误");
        }
//        if(data == null){
//            return false;
//        }else{
//            for (Map<String, Object> map : (List<Map>)data.getResult()){
//                String dataStr = (String) map.get("data");
//                Map temp = JSONObject.parseObject(dataStr);
//                WmImNoticeHEntity hEntity = new WmImNoticeHEntity();
//                hEntity.setNoticeId((String) temp.get("InStorageNo"));
//                hEntity.setImData(format.parse((String) temp.get("storeDate")));
//                hEntity.setQcStaff((String) temp.get("QCstaff"));
//                hEntity.setOrderTypeCode("05");
//                hEntity.setImSta(WmsContants.WM_IM_H_PLANNING);
//                for(Map<String, Object> iMap : (List<Map>)temp.get("detail")){
//                    WmImNoticeIEntity iEntity = new WmImNoticeIEntity();
//                    iEntity.setImNoticeId(hEntity.getNoticeId());
//                    iEntity.setGoodsCode((String) iMap.get("materialCode"));
//                    iEntity.setGoodsName((String) iMap.get("maretialName"));
//                    iEntity.setGoodsBatch((String) iMap.get("materialBatchNo"));
//                    iEntity.setBzhiQi((String) iMap.get("warranty"));
//                    iEntity.setGoodsPrdData(format.parse((String) iMap.get("manufactureDate")));
//                    iEntity.setGoodsWeight((String) iMap.get("storeWeight"));
//                    iEntity.setGoodsCount((String) iMap.get("storeQuantity"));
//                    iEntity.setShpGuiGe((String) iMap.get("specification"));
//                    if(hEntity.getSupCode() == null){
//                        hEntity.setSupName((String) iMap.get("supplierName"));
//                        hEntity.setSupCode((String) iMap.get("supplierNo"));
//                    }
//                    iEntity.setNoticeiSta(WmsContants.WM_IM_I_PLANNING);
//                    this.save(iEntity);
//                }
//                this.save(hEntity);
//            }
//        }
    }

    @Test
    public void testculString(){
        String scount = "52";
        String sweight = "530.0";
        String sMweigt = "1200.0";

        String weight = "500.0";
        String scount1 = "10";
        String acount = "16";

        long count = Math.round(Long.valueOf(scount)*(Double.valueOf(sweight)/Double.valueOf(sMweigt)));
        double weight2 = Double.valueOf(weight)*((double)Long.parseLong(scount1)/Long.valueOf(acount));
        System.out.println(count);
        System.out.println(weight2);
    }

    @Test
    public void testbasic(){
        Double d = Double.parseDouble("56");
    }
}
