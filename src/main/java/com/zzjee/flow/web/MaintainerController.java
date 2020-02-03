package com.zzjee.flow.web;

import com.zzjee.ba.entity.BaPlatformEntity;
import com.zzjee.flow.service.StoreAssignmentServiceI;
import com.zzjee.flow.util.ExceptionType;
import com.zzjee.flow.util.TaskStatus;
import com.zzjee.md.entity.MdMovePalletEntity;
import com.zzjee.md.entity.MdPalletEntity;
import com.zzjee.md.entity.PalletStatus;
import com.zzjee.wm.entity.WmOmNoticeHEntity;
import com.zzjee.wm.entity.WmOmNoticeIEntity;
import com.zzjee.wm.entity.WmOmQmIEntity;
import com.zzjee.wm.page.WmOmNoticeHPage;
import com.zzjee.wmutil.WmsContants;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.sms.util.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/flow/maintainer")
public class MaintainerController {

    @Resource
    SystemService systemService;
    @Resource
    StoreAssignmentServiceI storeAssignmentService;

    @RequestMapping(params = "specifyPlatTask")
    @ResponseBody
    public AjaxJson specifyPlatTask(){
        AjaxJson j = new AjaxJson();
        //List<WmOmNoticeHEntity> hs = systemService.findByProperty(WmOmNoticeHEntity.class, "omSta", Constants.wm_sta1);
        String omhhql = "from WmOmNoticeHEntity h where h.omSta=? and h.orderTypeCode=?";
        List<WmOmNoticeHEntity> hs = systemService.findHql(omhhql, Constants.wm_sta1, WmsContants.OUT_PRODUCT);
        List<WmOmNoticeHPage> pages = new ArrayList<>();
        for (WmOmNoticeHEntity h : hs){
            WmOmNoticeHPage htmp = new WmOmNoticeHPage();
            try{
                MyBeanUtils.copyBeanNotNull2Bean(h, htmp);
                List<WmOmNoticeIEntity> is = systemService.findByProperty(WmOmNoticeIEntity.class, "omNoticeId", h.getOmNoticeId());
                htmp.setWmOmNoticeIList(is);
            } catch (Exception e){
                j.setMsg(j.getMsg() + "; id:" + h.getId() + ",数据出错");
            }
            pages.add(htmp);
        }
        j.setObj(pages);
        return j;
    }

    @RequestMapping(params = "findPlatInfo")
    @ResponseBody
    public AjaxJson findPlatInfo(String barCode){
        AjaxJson j = new AjaxJson();
        BaPlatformEntity plat = systemService.findUniqueByProperty(BaPlatformEntity.class, "platformBarcode", barCode);
        if(plat == null){
            j.setSuccess(false);
            j.setMsg("无此车位码");
            return j;
        }
        j.setObj(plat);
        return j;
    }

    @RequestMapping(params = "taskOver")
    @ResponseBody
    public AjaxJson taskOver(String id, String platId){
        AjaxJson j = new AjaxJson();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        TSUser user = ResourceUtil.getSessionUserName();

        WmOmNoticeHEntity h = systemService.getEntity(WmOmNoticeHEntity.class, id);
        BaPlatformEntity plat = systemService.getEntity(BaPlatformEntity.class, platId);
        if(h == null || plat == null){
            j.setSuccess(false);
            j.setMsg("操作失败，参数错误");
        }
        //变更状态
        h.setOmPlatNo(plat.getPlatformCode());
        h.setOmSta(Constants.wm_sta4);
        systemService.updateEntitie(h);
        //添加数据
        Map<WmOmNoticeIEntity, List<MdPalletEntity>> assignmentPallets = storeAssignmentService.assignmentPalletForOmOrder(h.getOmNoticeId());
        for (Map.Entry<WmOmNoticeIEntity, List<MdPalletEntity>> entry : assignmentPallets.entrySet()){
            List<MdPalletEntity> pallets = entry.getValue();
            WmOmNoticeIEntity wmOmNoticeI = entry.getKey();

            for (MdPalletEntity pallet : pallets){
                WmOmQmIEntity wmiTemp = new WmOmQmIEntity();
                wmiTemp.setUpdateDate(new Date());
                wmiTemp.setOmNoticeId(wmOmNoticeI.getOmNoticeId());
                wmiTemp.setIomNoticeItem(wmOmNoticeI.getId());
                wmiTemp.setGoodsId(wmOmNoticeI.getGoodsId());
                wmiTemp.setGoodsName(wmOmNoticeI.getGoodsName());
                wmiTemp.setOmQuat(pallet.getShuLiang());
                wmiTemp.setQmOkQuat("0");
                wmiTemp.setBaseGoodscount(pallet.getShuLiang());
                wmiTemp.setTinZhl(pallet.getZhongLiang());
                wmiTemp.setProData(wmOmNoticeI.getGoodsProData()!=null?format.format(wmOmNoticeI.getGoodsProData()):null);
                wmiTemp.setGoodsBatch(wmOmNoticeI.getGoodsBatch());

                wmiTemp.setBinId(pallet.getBinBianMa());
                wmiTemp.setTinId(pallet.getTuoPanBianMa());
                wmiTemp.setBinSta("N");
                systemService.save(wmiTemp);

                MdMovePalletEntity movePallet = new MdMovePalletEntity();
                movePallet.setPalletCode(pallet.getTuoPanBianMa());
                movePallet.setOperateBy(user==null?null:user.getUserKey());
                movePallet.setType("下架");
                movePallet.setRecord(pallet.getBinBianMa() + "->" + h.getOmPlatNo());
                movePallet.setStatus(TaskStatus.INIT);
                movePallet.setTriggerSource(wmOmNoticeI.getId());
                systemService.save(movePallet);

                pallet.setTuoPanZhuangTai(PalletStatus.IN_DOWN);
                systemService.updateEntitie(pallet);
            }
        }
        return j;
    }
}
