package com.zzjee.flow.web;

import com.zzjee.ba.entity.BaPlatformEntity;
import com.zzjee.flow.util.BpmStatus;
import com.zzjee.flow.util.TaskStatus;
import com.zzjee.md.entity.MdBinEntity;
import com.zzjee.md.entity.MdGoodsEntity;
import com.zzjee.md.entity.MdMovePalletEntity;
import com.zzjee.md.entity.MdPalletEntity;
import com.zzjee.wm.entity.WmImNoticeHEntity;
import com.zzjee.wm.entity.WmImNoticeIEntity;
import com.zzjee.wm.entity.WmToMoveGoodsEntity;
import com.zzjee.wm.page.WmImNoticeHPage;
import com.zzjee.wm.service.WmImNoticeHServiceI;
import com.zzjee.wmutil.WmsContants;
import javafx.concurrent.Task;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/flow/palletMove")
public class PalletMoveController {
    @Resource
    SystemService systemService;
    @Resource
    private WmImNoticeHServiceI wmImNoticeIService;


    /*************************************************空盘移库*********************************/
    @RequestMapping(params = "findEmptyTaskCount")
    @ResponseBody
    public AjaxJson findEmptyTaskCount(){
        AjaxJson ajaxJson = new AjaxJson();
        String hql = "select count(id) from Wm_Im_Notice_I wm where wm.bin_Pre='N' and wm.bpm_sta=?";
        Long count = systemService.getCountForJdbcParam(hql, new Object[]{BpmStatus.MOVING});
        Map map = new HashMap();
        map.put("count",count==null?0:count);
        ajaxJson.setObj(map);
        return ajaxJson;
    }

    @RequestMapping(params = "findEmptyPalletTask")
    @ResponseBody
    public AjaxJson findEmptyPalletTask(){
        AjaxJson ajaxJson = new AjaxJson();
        List<Map> result = new ArrayList<>();
        String hql = "from WmImNoticeIEntity wm where wm.binPre='N' and wm.bpmSta=?";
        List<WmImNoticeIEntity> list = wmImNoticeIService.findHql(hql,BpmStatus.MOVING);

        boolean error = false;

        for (WmImNoticeIEntity imi : list){
            Map map = new HashMap();
            MdGoodsEntity good = systemService.findUniqueByProperty(MdGoodsEntity.class,"shpBianMa",imi.getGoodsCode());
            if(good == null){
                error = true;
                break;
            }
            WmImNoticeHEntity imh = systemService.findUniqueByProperty(WmImNoticeHEntity.class, "noticeId",imi.getImNoticeId());
            if(imh == null){
                error = true;
                break;
            }
            BaPlatformEntity plat = systemService.findUniqueByProperty(BaPlatformEntity.class,"platformCode",imh.getPlatformCode());

            long count = Long.valueOf(good.getMpDanCeng()) * Long.valueOf(good.getMpCengGao());
            long resultcount = (long) Math.ceil(Double.parseDouble(imi.getGoodsCount()) / count);
            map.put("id",imi.getId());
            map.put("count",resultcount);
            map.put("site",plat==null?"":plat.getPlatformName());
            result.add(map);
        }

        if(error){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("系统中无指定商品或订单数据错误");
            return ajaxJson;
        }

        ajaxJson.setObj(result);
        return ajaxJson;
    }

    @RequestMapping(params = "emptyMoveOver")
    @ResponseBody
    public AjaxJson emptyMoveOver(String id){
        AjaxJson ajaxJson = new AjaxJson();
        WmImNoticeIEntity imi = systemService.getEntity(WmImNoticeIEntity.class, id);
        if(imi == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("无此订单");
            return ajaxJson;
        }
        imi.setNoticeiSta(WmsContants.IN_STOCK);
        imi.setBpmSta(BpmStatus.PALLETIZING);
        systemService.updateEntitie(imi);
        return ajaxJson;
    }

    /*************************************************托盘移库**********************************************************/
    @RequestMapping(params = "moveTask")
    @ResponseBody
    public AjaxJson moveTask(){
        AjaxJson ajaxJson = new AjaxJson();
        List<WmToMoveGoodsEntity> moves = systemService.findByProperty(WmToMoveGoodsEntity.class, "moveSta", TaskStatus.INIT);
        ajaxJson.setObj(moves==null?new ArrayList<>():moves);
        return ajaxJson;
    }

    @RequestMapping(params = "findOldBin")
    @ResponseBody
    public AjaxJson findOldBin(String hwBarCode, String id){
        AjaxJson ajaxJson = new AjaxJson();
        WmToMoveGoodsEntity move = systemService.getEntity(WmToMoveGoodsEntity.class, id);
        MdBinEntity bin = systemService.findUniqueByProperty(MdBinEntity.class, "kuWeiTiaoMa", hwBarCode);
        if(bin == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("查无此货架");
            return ajaxJson;
        }
        if(move == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("查无此任务");
            return ajaxJson;
        } else if(!bin.getKuWeiBianMa().equals(move.getBinFrom())){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("货架与任务不匹配");
            return ajaxJson;
        }
        ajaxJson.setObj(bin);
        return ajaxJson;
    }


    @RequestMapping(params = "findOldPallet")
    @ResponseBody
    public AjaxJson findOldPallet(String palletBarCode, String id){
        AjaxJson ajaxJson = new AjaxJson();
        WmToMoveGoodsEntity move = systemService.getEntity(WmToMoveGoodsEntity.class, id);
        MdPalletEntity pallet = systemService.findUniqueByProperty(MdPalletEntity.class, "tuoPanTiaoMa", palletBarCode);
        if(pallet == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("查无此托盘");
            return ajaxJson;
        }
        if(move == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("查无此任务");
            return ajaxJson;
        } else if(!pallet.getTuoPanBianMa().equals(move.getTinFrom())){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("托盘与任务不匹配");
            return ajaxJson;
        }
        ajaxJson.setObj(pallet);
        return ajaxJson;
    }

    @RequestMapping(params = "findNewBin")
    @ResponseBody
    public AjaxJson findNewBin(String hwBarCode, String id){
        AjaxJson ajaxJson = new AjaxJson();
        WmToMoveGoodsEntity move = systemService.getEntity(WmToMoveGoodsEntity.class, id);
        MdBinEntity bin = systemService.findUniqueByProperty(MdBinEntity.class, "kuWeiTiaoMa", hwBarCode);
        if(bin == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("查无此货架");
            return ajaxJson;
        }
        if(move == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("查无此任务");
            return ajaxJson;
        } else if(!bin.getKuWeiBianMa().equals(move.getBinTo())){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("货架与任务不匹配");
            return ajaxJson;
        }
        ajaxJson.setObj(bin);
        return ajaxJson;
    }

    @RequestMapping(params = "moveOver")
    @ResponseBody
    public AjaxJson moveOver(String id){
        AjaxJson ajaxJson = new AjaxJson();
        WmToMoveGoodsEntity move = systemService.getEntity(WmToMoveGoodsEntity.class, id);
        move.setMoveSta(TaskStatus.FINISHED);
        String hql = "from MdMovePalletEntity mp where mp.palletCode=? and mp.type=? and mp.status=?";
        List<MdMovePalletEntity> pMoves = systemService.findHql(hql, move.getTinFrom(), "移库", TaskStatus.INIT);
        if(pMoves == null || pMoves.size() <= 0){
            MdMovePalletEntity pMove = new MdMovePalletEntity();
            pMove.setPalletCode(move.getTinFrom());
            pMove.setType("移库");
            pMove.setRecord(move.getBinFrom() + "->" + move.getBinTo());
            pMove.setStatus(TaskStatus.FINISHED);
            systemService.save(pMove);
        }else{
            pMoves.get(0).setStatus(TaskStatus.FINISHED);
            systemService.updateEntitie(pMoves.get(0));
        }
        return ajaxJson;
    }
}
