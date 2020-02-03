package com.zzjee.flow.web;

import com.zzjee.api.service.StockInfoServiceI;
import com.zzjee.ba.entity.BaPlatformEntity;
import com.zzjee.wm.entity.WmOmNoticeHEntity;
import com.zzjee.wm.page.WmOmNoticeHPage;
import com.zzjee.wm.service.WmImNoticeHServiceI;
import com.zzjee.wm.service.WmOmNoticeHServiceI;
import com.zzjee.wmutil.WmsContants;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/flow/outStorage")
public class OutStorageController {
    @Resource
    SystemService systemService;
    @Resource
    StockInfoServiceI stockInfoService;
    @Resource
    WmOmNoticeHServiceI wmOmNoticeHService;

    @RequestMapping(params = "findOutCount")
    @ResponseBody
    public AjaxJson findOutCount(){
        AjaxJson ajaxJson = new AjaxJson();
        Map result = new HashMap();

        String hql = "select count(id) from Wm_Om_Notice_H wmo where wmo.order_Type_Code=? and wmo.om_Sta=? and not exists( from Wm_Om_Notice_I wmi where wmi.om_Notice_Id=wmo.om_Notice_Id and wmi.om_Sta != ? )";

        Long count = systemService.getCountForJdbcParam(hql, new Object[]{WmsContants.OUT_PRODUCT, WmsContants.OUT_STOCK, WmsContants.FINISHED} );
        if(count != null) {
            result.put("count", count);
            ajaxJson.setObj(result);
        }else{
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("数据错误");
        }
        ajaxJson.setObj(result);
        return ajaxJson;
    }

    @RequestMapping(params = "findOutList")
    @ResponseBody
    public AjaxJson findOutList(){
        AjaxJson ajaxJson = new AjaxJson();
        List<WmOmNoticeHPage> result = wmOmNoticeHService.findLoadedOrderWithDetail();
        if(result != null)
            ajaxJson.setObj(result);
        else{
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("数据错误");
        }
        ajaxJson.setObj(result);
        return ajaxJson;
    }

    @RequestMapping(params = "outConfirm")
    @ResponseBody
    public AjaxJson outConfirm(String orderKey, String platCode){
        AjaxJson ajaxJson = new AjaxJson();
        BaPlatformEntity plat = systemService.findUniqueByProperty(BaPlatformEntity.class,"platformCode",platCode);
        //WmOmNoticeHEntity h = systemService.getEntity(WmOmNoticeHEntity.class,orderKey);
        WmOmNoticeHEntity h = systemService.findUniqueByProperty(WmOmNoticeHEntity.class, "omNoticeId", orderKey);

        if(h == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("查无此单");
            return ajaxJson;
        } else if (plat == null || "Y".equals(plat.getPlatformDel())){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("该月台数据错误");
            return ajaxJson;
        }
        h.setOmSta(WmsContants.FINISHED);
        h.setOmPlatNo(platCode);
        systemService.updateEntitie(h);
        return ajaxJson;
    }

    @RequestMapping(params = "findPlatForm")
    @ResponseBody
    public AjaxJson findPlatForm(String code){
        AjaxJson ajaxJson = new AjaxJson();
        BaPlatformEntity plat = systemService.findUniqueByProperty(BaPlatformEntity.class,"platformBarcode",code);
        if(plat == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("没有查到该月台");
        } else if("Y".equals(plat.getPlatformDel())){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("该月台已停用");
        } else{
            ajaxJson.setObj(plat);
        }
        return ajaxJson;
    }

}
