package com.zzjee.flow.web;

import com.zzjee.flow.util.ExceptionType;
import com.zzjee.flow.util.TaskStatus;
import com.zzjee.md.entity.*;
import com.zzjee.md.service.MdMovePalletServiceI;

import com.zzjee.wm.entity.WmImNoticeIEntity;
import com.zzjee.wm.entity.WmToMoveGoodsEntity;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/flow/exceptionHandle")
public class ExceptionHandleController {
    @Resource
    SystemService systemService;
    @Resource
    MdMovePalletServiceI mdMovePalletService;

    @RequestMapping(params = "countPalletException")
    @ResponseBody
    public AjaxJson countPalletException(){
        AjaxJson ajaxJson = new AjaxJson();
        Map map = new HashMap();
        String sql = "SELECT COUNT(id) from md_exception me where me.status=? and me.type=?";
        Long rescount = systemService.getCountForJdbcParam(sql, new Object[]{TaskStatus.INIT, ExceptionType.EXP_PALLET});
        map.put("count",rescount);
        ajaxJson.setObj(ajaxJson);
        return ajaxJson;
    }

    @RequestMapping(params = "countBarcodeException")
    @ResponseBody
    public AjaxJson countBarcodeException(){
        AjaxJson ajaxJson = new AjaxJson();
        Map map = new HashMap();
        String sql = "SELECT COUNT(id) from md_exception me where me.status=? and me.type=?";
        Long rescount = systemService.getCountForJdbcParam(sql, new Object[]{TaskStatus.INIT, ExceptionType.EXP_BARCODE});
        map.put("count",rescount);
        ajaxJson.setObj(ajaxJson);
        return ajaxJson;
    }

    @RequestMapping(params = "countBinException")
    @ResponseBody
    public AjaxJson countBinException(){
        AjaxJson ajaxJson = new AjaxJson();
        Map map = new HashMap();
        String sql = "SELECT COUNT(id) from md_exception me where me.status=? and me.type=?";
        Long rescount = systemService.getCountForJdbcParam(sql, new Object[]{TaskStatus.INIT, ExceptionType.EXP_BIN});
        map.put("count",rescount);
        ajaxJson.setObj(ajaxJson);
        return ajaxJson;
    }

    @RequestMapping(params = "findExceptions")
    @ResponseBody
    public AjaxJson findExceptions(String type){
        AjaxJson ajaxJson = new AjaxJson();

        String hql = "from MdExceptionEntity me where me.status=? and me.type=?";
        List<MdExceptionEntity> elist = systemService.findHql(hql, TaskStatus.INIT, type);
        ajaxJson.setObj(elist);

        return ajaxJson;
    }

    @RequestMapping(params = "findPalletStoreRecords")
    @ResponseBody
    public AjaxJson findPalletStoreRecords(String expId, Long count){
        AjaxJson ajaxJson = new AjaxJson();

        count = (count == null || count <= 0)? 5 : count;

        MdExceptionEntity exp = systemService.getEntity(MdExceptionEntity.class, expId);
        if(exp == null || TaskStatus.FINISHED.equals(exp.getStatus())){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("不存在此异常或异常已完成");
            return null;
        }
        MdPalletEntity pallet = systemService.getEntity(MdPalletEntity.class, exp.getPalletId());
        if(pallet == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("没有找到该异常所对应的托盘信息");
            return ajaxJson;
        }
        String hql = "select * from Md_Move_Pallet md where md.pallet_Code='" + pallet.getTuoPanBianMa()
        		+ "' and md.status='"+TaskStatus.FINISHED+"' and md.type in ('码垛','拆垛') and md.update_Date < '"+exp.getCreateDate()+"' order by md.update_Date desc";
        //List<MdMovePalletEntity> moves = systemService.findHql(hql, pallet.getTuoPanBianMa(), TaskStatus.FINISHED, exp.getCreateDate(), count);
        List<MdMovePalletEntity> moves = mdMovePalletService.findObjForJdbc(hql, 1, count.intValue(), MdMovePalletEntity.class);
        ajaxJson.setObj(moves);
        return ajaxJson;
    }

    @RequestMapping(params = "findPalletUpDownRecords")
    @ResponseBody
    public AjaxJson findPalletUpDownRecords(String expId, Long count){
        AjaxJson ajaxJson = new AjaxJson();

        count = (count == null || count <= 0)? 10 : count;

        MdExceptionEntity exp = systemService.getEntity(MdExceptionEntity.class, expId);
        if(exp == null || TaskStatus.FINISHED.equals(exp.getStatus())){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("不存在此异常或异常已完成");
            return null;
        }
        MdBinEntity bin = systemService.getEntity(MdBinEntity.class, exp.getBinId());
        if(bin == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("没有找到该异常所对应的货位信息");
            return ajaxJson;
        }

        //String hql = "from MdMovePalletEntity md where md.triggerSource=? and md.palletCode=? and md.status=? and md.type in('上架'，'下架') order by md.updateDate limit ?";
        String hql = "select * from Md_Move_Pallet md where md.status='"+TaskStatus.FINISHED
        		+ "' and ( (md.type='上架' and md.record REGEXP '" + "->"+bin.getKuWeiBianMa()+"$"
        		+ "') or (md.type='下架' and md.record REGEXP '" + "^"+bin.getKuWeiBianMa()+"->"
        		+ "') ) and md.update_Date < '"+exp.getCreateDate()+"' order by md.update_Date desc";

        //List<MdMovePalletEntity> moves = systemService.findHql(hql, TaskStatus.FINISHED,"->"+bin.getKuWeiBianMa()+"$","^"+bin.getKuWeiBianMa()+"->", exp.getCreateDate(), count);
        List<MdMovePalletEntity> moves = mdMovePalletService.findObjForJdbc(hql, 1, count.intValue(), MdMovePalletEntity.class);
        ajaxJson.setObj(moves);

        return ajaxJson;
    }

    @RequestMapping(params = "findAllTypeRecords")
    @ResponseBody
    public AjaxJson findAllTypeRecords(String expId, Long count){
        AjaxJson ajaxJson = new AjaxJson();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        count = (count == null || count <= 0)? 10 : count;

        MdExceptionEntity exp = systemService.getEntity(MdExceptionEntity.class, expId);
        if(exp == null || TaskStatus.FINISHED.equals(exp.getStatus())){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("不存在此异常或异常已完成");
            return null;
        }

        String hql = "select * from Md_Move_Pallet md where md.status='"+TaskStatus.FINISHED+"' and md.type in ('上架','下架','码垛','拆垛') "
        		+ "and md.update_Date<'"+format.format(exp.getUpdateDate())+"' order by md.update_Date desc";
//        List<MdMovePalletEntity> moves = systemService.findHql(hql, TaskStatus.FINISHED, exp.getUpdateDate(), count);
        List<MdMovePalletEntity> moves = mdMovePalletService.findObjForJdbc(hql, 1, count.intValue(), MdMovePalletEntity.class);
        ajaxJson.setObj(moves);

        return ajaxJson;
    }

    @RequestMapping(params = "brokenOver")
    @ResponseBody
    public AjaxJson brokenOver(String expId){
        AjaxJson ajaxJson = new AjaxJson();

        MdExceptionEntity exp = systemService.getEntity(MdExceptionEntity.class, expId);
        if(exp == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("不存在此异常");
        }
        exp.setStatus(TaskStatus.FINISHED);
        systemService.updateEntitie(exp);

        return ajaxJson;
    }

    @RequestMapping(params = "noneCodeOver")
    @ResponseBody
    public AjaxJson noneCodeOver(String expId){
        AjaxJson ajaxJson = new AjaxJson();

        MdExceptionEntity exp = systemService.getEntity(MdExceptionEntity.class, expId);
        if(exp == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("不存在此异常");
        }
        exp.setStatus(TaskStatus.FINISHED);
        systemService.updateEntitie(exp);

        return ajaxJson;
    }

    @RequestMapping(params = "addMoveTask")
    @ResponseBody
    public AjaxJson addMoveTask(String oldBinCode, String palletCode, String newBinCode){
        AjaxJson ajaxJson = new AjaxJson();
        SimpleDateFormat format = new SimpleDateFormat();

        MdBinEntity oldbin = systemService.findUniqueByProperty(MdBinEntity.class,"kuWeiBianMa",oldBinCode);
        MdBinEntity newbin = systemService.findUniqueByProperty(MdBinEntity.class,"kuWeiBianMa",newBinCode);
        MdPalletEntity pallet = systemService.findUniqueByProperty(MdPalletEntity.class,"tuoPanBianMa",palletCode);
        if(oldbin == null || newbin == null || pallet == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("不存在此盘或不存在此仓");
            return ajaxJson;
        }
        WmImNoticeIEntity imi = systemService.getEntity(WmImNoticeIEntity.class, pallet.getEntryKey());
        if(imi == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("不存在此托盘所指的入库单");
            return ajaxJson;
        }
        MdGoodsEntity good = systemService.findUniqueByProperty(MdGoodsEntity.class, "shpBianMa",pallet.getZhuangLiaoBianMa());
        if(good == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("系统中不存在此盘上所装物料");
            return ajaxJson;
        }
        WmToMoveGoodsEntity newMove = new WmToMoveGoodsEntity();
        newMove.setOrderIdI(pallet.getEntryKey());
        newMove.setGoodsId(pallet.getZhuangLiaoBianMa());
        newMove.setGoodsId(pallet.getZhuangLiaoBianMa());
        newMove.setGoodsName(pallet.getZhuangLiaoMingCheng());
        newMove.setGoodsQua(pallet.getShuLiang());
        newMove.setGoodsUnit(pallet.getShuLiangDanWei());
        newMove.setBaseGoodscount(pallet.getShuLiang());
        newMove.setBaseUnit(pallet.getShuLiangDanWei());
        newMove.setGoodsProData(format.format(imi.getGoodsPrdData()));
        newMove.setBinFrom(pallet.getBinBianMa());
        newMove.setBinTo(newBinCode);
        newMove.setTinFrom(pallet.getTuoPanBianMa());
        newMove.setTinId(pallet.getTuoPanBianMa());
        newMove.setMoveSta(TaskStatus.INIT);
        systemService.save(newMove);

        return ajaxJson;
    }
}
