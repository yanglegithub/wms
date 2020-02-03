package com.zzjee.flow.web;

import com.zzjee.api.service.StockInfoServiceI;
import com.zzjee.ba.entity.BaPlatformEntity;
import com.zzjee.flow.util.BpmStatus;
import com.zzjee.wm.entity.WmImNoticeHEntity;
import com.zzjee.wm.entity.WmImNoticeIEntity;
import com.zzjee.wm.entity.WmPlatIoEntity;
import com.zzjee.wm.page.WmImNoticeHPage;
import com.zzjee.wm.service.WmImNoticeHServiceI;
import com.zzjee.wmutil.WmsContants;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.sms.util.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/flow/inStorage")
public class InStorageController {
    @Resource
    SystemService systemService;
    @Resource
    StockInfoServiceI stockInfoService;
    @Resource
    WmImNoticeHServiceI wmImNoticeHService;

    @RequestMapping(params = "getFromNC")
    @ResponseBody
    public AjaxJson getFromNC(String imKey, String imDate){
        AjaxJson ajaxJson = new AjaxJson();
        AjaxJson fileJson = new AjaxJson();
        fileJson.setSuccess(false);
        fileJson.setMsg("请求失败");
        List<WmImNoticeHPage> returnlist = null;
        //try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//            if(StringUtil.isEmpty(imDate))
//                returnlist = stockInfoService.findImNoticeFromNC(imKey,format.format(new Date()));
//            else
//                returnlist = stockInfoService.findImNoticeFromNC(imKey,imDate);
            imKey = imKey==null?"":imKey;
            returnlist = wmImNoticeHService.findConformedImHList(imKey);
        /*} catch (ParseException e) {
            e.printStackTrace();
            System.out.println("网络请求失败");
            fileJson.setMsg("网络请求失败");
            return fileJson;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("参数格式不正确");
            fileJson.setMsg("参数格式不正确");
            return fileJson;
        }*/
        if(returnlist == null)
            return fileJson;
        else{
            ajaxJson.setObj(returnlist);
            return ajaxJson;
        }
    }

    @RequestMapping(params = "confirmImKey")
    @ResponseBody
    public AjaxJson confirmImkey(String imKey, String imDate, String platcode){
        AjaxJson ajaxJson = new AjaxJson();
        String noticeId = null;
        //try {
            //noticeId = stockInfoService.findImNotice(imKey,imDate);
            if(StringUtil.isEmpty(imKey)){
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("操作失败");
            }else{
                WmImNoticeHEntity hEntity = systemService.findUniqueByProperty(WmImNoticeHEntity.class,"noticeId",imKey);
                hEntity.setPlatformCode(platcode);
                hEntity.setImSta(WmsContants.IN_STOCK);
                hEntity.setBpmSta(BpmStatus.MOVING);
                systemService.saveOrUpdate(hEntity);
                
                List<WmImNoticeIEntity> ilist = systemService.findByProperty(WmImNoticeIEntity.class, "imNoticeId", hEntity.getNoticeId());
                for(WmImNoticeIEntity i : ilist) {
                	i.setBpmSta(BpmStatus.MOVING);
                	systemService.updateEntitie(i);
                }

                WmPlatIoEntity wmPlatIo = new WmPlatIoEntity();
                wmPlatIo.setCarno(hEntity.getImCarNo());
                wmPlatIo.setDocId(hEntity.getNoticeId());
                wmPlatIo.setPlanIndata(hEntity.getImData());
                wmPlatIo.setPlatId(hEntity.getPlatformCode());
                wmPlatIo.setPlatSta(Constants.wm_sta2);
                wmPlatIo.setPlatBeizhu("司机:" + hEntity.getImCarDri() + "电话:"
                        + hEntity.getImCarMobile());
                wmPlatIo.setInData(new Date());
                wmPlatIo.setPlatOper("卸货");
                systemService.save(wmPlatIo);
            }
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
