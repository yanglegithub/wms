package com.zzjee.flow.web;

import com.zzjee.flow.util.ExceptionType;
import com.zzjee.flow.util.TaskStatus;
import com.zzjee.md.entity.*;
import com.zzjee.wm.entity.WmOmNoticeHEntity;
import com.zzjee.wm.entity.WmOmNoticeIEntity;
import com.zzjee.wm.service.WmOmNoticeHServiceI;
import com.zzjee.wmutil.WmsContants;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/flow/unloadPallet")
public class UnloadPalletController {
    @Resource
    SystemService systemService;
    @Resource
    WmOmNoticeHServiceI wmOmNoticeHService;

    @RequestMapping(params = "findUnLoadTask")
    @ResponseBody
    public AjaxJson findTask(String type){
        AjaxJson ajaxJson = new AjaxJson();
        List<WmOmNoticeIEntity> ilist = wmOmNoticeHService.findUnloadTask(type);
        if(ilist == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("操作失败");
            return ajaxJson;
        }
        ajaxJson.setObj(ilist);
        return ajaxJson;
    }
    @RequestMapping(params = "findPalletCount")
    @ResponseBody
    public AjaxJson findPalletCount(){
        AjaxJson ajaxJson = new AjaxJson();
        String hql = "select count(*) from Md_Move_Pallet mp where mp.type='拆垛' and mp.status=?";
        long count = systemService.getCountForJdbcParam(hql,new Object[]{TaskStatus.INIT});
        Map result = new HashMap();
        result.put("count",count);
        ajaxJson.setObj(result);
        return ajaxJson;
    }

    @RequestMapping(params = "findPallet")
    @ResponseBody
    public AjaxJson findPallet(String palletCode, String id){
        AjaxJson ajaxJson = new AjaxJson();
        MdPalletEntity pallet = systemService.findUniqueByProperty(MdPalletEntity.class,"tuoPanBianMa",palletCode);
        if(pallet == null) {
        	ajaxJson.setSuccess(false);
            ajaxJson.setMsg("查无此盘");
            return ajaxJson;
        }

        String hql = "from MdMovePalletEntity mp where mp.triggerSource=? and mp.palletCode=? ";
        List<MdMovePalletEntity> move = systemService.findHql(hql,id,palletCode);

        if(!PalletStatus.IN_UNLOAD.equals(pallet.getTuoPanZhuangTai())){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("托盘状态不正确");
            return ajaxJson;
        }
        if(id == null || move.size()<=0){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("该托盘不在当前任务中");
            return ajaxJson;
        }
        ajaxJson.setObj(pallet);
        return ajaxJson;
    }

    @RequestMapping(params = "findTempBin")
    @ResponseBody
    public AjaxJson findTempBin(){
        AjaxJson ajaxJson = new AjaxJson();
        String binhql = "from MdBinEntity bin where bin.kuWeiLeiXing=? and not exists( MdPalletEntity mp where mp.tuoPanZhuangTai=? and mp.binBianMa=bin.kuWeiBianMa )";
        List<MdBinEntity> bins = systemService.findHql(binhql, "临时储位", PalletStatus.IN_SHELF);
        ajaxJson.setObj(bins);
        return ajaxJson;
    }

    @RequestMapping(params = "unloadOver")
    @ResponseBody
    public AjaxJson unloadOver(String palletCode, String isEmpty, String remain, String binCode){
        AjaxJson ajaxJson = new AjaxJson();
        Map result = new HashMap<>();
        result.put("taskOver", false);
        ajaxJson.setObj(result);
        MdPalletEntity mp = systemService.findUniqueByProperty(MdPalletEntity.class,"tuoPanBianMa",palletCode);
        if(mp == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("查无此盘");
            return ajaxJson;
        }
        String hql = "from MdMovePalletEntity mp where mp.status=? and mp.palletCode=? and mp.type=?";
        List<MdMovePalletEntity> move = systemService.findHql(hql,TaskStatus.INIT,mp.getTuoPanBianMa(),"拆垛");
        if(move.size() <= 0){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("该托盘没有相匹配的任务");
            return ajaxJson;
        }
        WmOmNoticeIEntity omi = systemService.getEntity(WmOmNoticeIEntity.class, move.get(0).getTriggerSource());

        if("true".equals(isEmpty)){
            move.get(0).setRecord(mp.getShuLiang());
            move.get(0).setStatus(TaskStatus.FINISHED);
            systemService.updateEntitie(move.get(0));

            emptyPallet(mp);
            systemService.updateEntitie(mp);

            String movehql = "from MdMovePalletEntity mp where mp.triggerSource=? and mp.type=? and mp.status=?";
            List<MdMovePalletEntity> movetemp = systemService.findHql(movehql,omi.getId(),"拆垛",TaskStatus.INIT);
            if(movetemp.size() <= 0){
                omi.setOmSta(WmsContants.FINISHED);
                systemService.updateEntitie(omi);
                
                String hql1 = "from WmOmNoticeHEntity wmo where wmo.omNoticeId=? and not exists( from WmOmNoticeIEntity wmi where wmi.omNoticeId=wmo.omNoticeId and wmi.omSta != ? )";
         		List<WmOmNoticeHEntity> hlist = systemService.findHql(hql1,omi.getOmNoticeId(), WmsContants.FINISHED);
         		if(hlist != null && hlist.size() > 0) {
         			hlist.get(0).setOmSta(WmsContants.OUT_STOCK);
         			systemService.updateEntitie(hlist.get(0));
         		}
                
                result.put("taskOver", true);
            }
        } else{
            move.get(0).setRecord(mp.getShuLiang());
            move.get(0).setStatus(TaskStatus.FINISHED);
            systemService.updateEntitie(move.get(0));

            mp.setShuLiang(remain);
            double remainWeight = Long.valueOf(mp.getZhongLiang())*(Double.valueOf(remain)/Double.valueOf(mp.getShuLiang()));
            mp.setZhongLiang(String.valueOf(remainWeight));
            mp.setBinBianMa(binCode);
            mp.setBinTiaoMa(systemService.findUniqueByProperty(MdBinEntity.class,"kuWeiBianMa",binCode).getKuWeiTiaoMa());
            mp.setTuoPanZhuangTai(PalletStatus.IN_SHELF);
            systemService.updateEntitie(mp);

            String movehql = "from MdMovePalletEntity mp where mp.triggerSource=? and mp.type=? and mp.status=?";
            List<MdMovePalletEntity> movetemp = systemService.findHql(movehql,omi.getId(),"拆垛",TaskStatus.INIT);
            if(movetemp.size() <= 0){
                omi.setOmSta(WmsContants.FINISHED);
                systemService.updateEntitie(omi);
                
                String hql1 = "from WmOmNoticeHEntity wmo where wmo.omNoticeId=? and not exists( from WmOmNoticeIEntity wmi where wmi.omNoticeId=wmo.omNoticeId and wmi.omSta != ? )";
         		List<WmOmNoticeHEntity> hlist = systemService.findHql(hql1,omi.getOmNoticeId(), WmsContants.FINISHED);
         		if(hlist != null && hlist.size() > 0) {
         			hlist.get(0).setOmSta(WmsContants.OUT_STOCK);
         			systemService.updateEntitie(hlist.get(0));
         		}
                
                result.put("taskOver", true);
            }
        }
        return ajaxJson;
    }

    public void emptyPallet(MdPalletEntity pallet){
        pallet.setTuoPanZhuangTai(PalletStatus.IDLE);
        pallet.setZhuangLiaoBianMa(null);
        pallet.setZhuangLiaoMingCheng(null);
        pallet.setPiCiHao(null);
        pallet.setShuLiang(null);
        pallet.setZhongLiang(null);
        pallet.setLiLunGuiGe(null);
        pallet.setShiJiGuiGe(null);
        pallet.setShuLiangDanWei(null);
        pallet.setZhongLiangDanWei(null);
        pallet.setBinTiaoMa(null);
        pallet.setBinBianMa(null);
        pallet.setEntryKey(null);
        pallet.setStoreDate(null);
    }


    @RequestMapping(params = "exceptionUpload")
    @ResponseBody
    public AjaxJson exceptionUpload(String palletId, String expDetail, String content){
        AjaxJson ajaxJson = new AjaxJson();

        MdPalletEntity pallet = systemService.getEntity(MdPalletEntity.class, palletId);
        if(pallet == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("查无此盘");
            return ajaxJson;
        }
        /*String movehql = "from MdMovePalletEntity mp where mp.palletCode=? and mp.type=? and mp.status=?";
        List<MdMovePalletEntity> movetemp = systemService.findHql(movehql,pallet.getTuoPanBianMa(),"拆垛",TaskStatus.INIT);
        if(movetemp.size() <= 0){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("该托盘没有拆垛任务");
            return ajaxJson;
        }*/

        MdExceptionEntity exp = new MdExceptionEntity();
        exp.setType(ExceptionType.EXP_PALLET);
        exp.setDetail(expDetail);
        exp.setContent(StringUtil.isEmpty(content)?null:content);
        exp.setPalletId(pallet.getId());
        exp.setPalletCode(pallet.getTuoPanBianMa());
        exp.setStatus(TaskStatus.INIT);
        systemService.save(exp);

        return ajaxJson;
    }
}
