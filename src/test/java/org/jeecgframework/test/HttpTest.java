package org.jeecgframework.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import org.jeecgframework.test.data.HttpReqUtil;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class HttpTest {

    String params = "{\n ctype:''," +
            "\t'InStorageNo': '101123100000000SI985',\n" +
            "\t'storeDate':'2019-11-11',\n" +
            "\t'QCstaff':'李某',\n" +
            "\t'detail': [{\t\t\n" +
            "\t\t'materialCode':'MT00000000001',\n" +
            "\t\t'maretialName': '玉米'，\n" +
            "\t\t'materialBatchNo':'B201901010001',\n" +
            "\t\t'supplierNo':'SC000001',\n" +
            "\t\t'supplierName':'xxx公司',\n" +
            "\t\t'warranty':'30',\n" +
            "\t\t'manufactureDate':'2019-11-05',\n" +
            "\t\t'storeQuantity':'100',\n" +
            "\t\t'storeWeight':'3000',\n" +
            "\t\t'specification':'30'\n" +
            "\t}]\n" +
            "}";

    @Test
    public void test(){
        String url = "http://117.40.132.196:8197/ZBServlet";//本机地址

        //先获取token
        String token_para = "token=&sysID=wms&funCode=getToken";
        String sr_token= HttpReqUtil.sendPost(url,token_para);
        System.out.println(sr_token);
        JSONObject token_jo = JSONObject.parseObject(sr_token);
        JSONArray tokenarr = JSONArray.parseArray(token_jo.getString("result"));
        String token = ((JSONObject)tokenarr.get(0)).getString("session");

        String para = "token="+ token +"&sysID=wms&funCode=alWMSQry";

        //请求json报文要用UTF-8编码
//	String bodyJson = "{\"ctype\":\"inv\"}" ;
        String bodyJson = "{\"ctype\":\"genIn\",\"dbilldate\":\"2019-12-13\"}" ;
        try {
            bodyJson = URLEncoder.encode(bodyJson, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String result = null;
        try {
            result = HttpReqUtil.sendPost(url,para,bodyJson);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("==============" + result);
//	}
    }

//    @Test
//    public void test1(){
//        Map map =
//    }

}
