package com.zzjee.flow.web;

import com.zzjee.ba.entity.BaPlatformEntity;
import com.zzjee.flow.service.PalletUpServiceI;
import com.zzjee.flow.service.StoreAssignmentServiceI;
import com.zzjee.flow.util.ExceptionType;
import com.zzjee.flow.util.TaskStatus;
import com.zzjee.md.entity.*;
import com.zzjee.wm.entity.*;
import com.zzjee.wm.service.WmInQmIServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.sms.util.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/flow/palletUp")
//@Api(value = "托盘上架接口")
public class PalletUpController {
    @Resource
    SystemService systemService;
    @Resource
    PalletUpServiceI palletUpService;
    @Resource
    StoreAssignmentServiceI storeAssignmentService;
    @Resource
    WmInQmIServiceI wmInQmIService;

    @RequestMapping(params = "findPalletUpTask")
    @ResponseBody
//    @ApiOperation(value = "登录")
    public AjaxJson findPalletUpTask(){
        AjaxJson ajaxJson = new AjaxJson();
        List<MdMovePalletEntity> moves = palletUpService.findUpTask();
        Map<WmImNoticeHEntity,List<MdMovePalletEntity>> summarize = new HashMap<>();
        List<Map<String, Object>> result = new ArrayList<>();
        for (MdMovePalletEntity entity : moves){
            WmImNoticeHEntity wmh = palletUpService.findImhByImi(entity.getTriggerSource());
            if(summarize.containsKey(wmh)){
                summarize.get(wmh).add(entity);
            }else{
                List<MdMovePalletEntity> sbuf = new ArrayList<MdMovePalletEntity>();
                sbuf.add(entity);
                summarize.put(wmh,sbuf);
            }
        }
        for (Map.Entry<WmImNoticeHEntity,List<MdMovePalletEntity>> entry : summarize.entrySet()){
            Map<String, Object> temp = new HashMap<>();
            temp.put("siteCode",entry.getKey().getPlatformCode());
            temp.put("siteName",systemService.findUniqueByProperty(BaPlatformEntity.class,"platformCode",entry.getKey().getPlatformCode()));
            temp.put("palletCount",entry.getValue().size());
            temp.put("id",entry.getKey().getId());
            result.add(temp);
        }
        ajaxJson.setObj(result);
        return ajaxJson;
    }


    @RequestMapping(params = "countUpTask")
    @ResponseBody
    public AjaxJson countUpTask(){
        AjaxJson ajaxJson = new AjaxJson();
        Map returnMap = new HashMap();
        List<MdMovePalletEntity> count = palletUpService.findUpTask();
        returnMap.put("count",count==null?0:count.size());
        ajaxJson.setObj(returnMap);
        return ajaxJson;
    }


    /**
     *
     * @param barCode
     * @param id
     * @return
     */
    @RequestMapping(params = "readPalletCode")
    @ResponseBody
    public AjaxJson readPalletCode(String barCode,String id){
        AjaxJson ajaxJson = new AjaxJson();
        //查看该托盘的信息
        MdPalletEntity pallet = systemService.findUniqueByProperty(MdPalletEntity.class,"tuoPanTiaoMa",barCode);
        if(pallet == null || !pallet.getTuoPanZhuangTai().equals(PalletStatus.IN_UP)){
            ajaxJson.setSuccess(false);
            if(pallet == null)
                ajaxJson.setMsg("操作失败，查无此盘");
            else
                ajaxJson.setMsg("操作失败，托盘状态不正确");
            return ajaxJson;
        }
        WmImNoticeIEntity noticeI = systemService.getEntity(WmImNoticeIEntity.class,pallet.getEntryKey());
        WmImNoticeHEntity noticeH = systemService.getEntity(WmImNoticeHEntity.class,id);
        if(!noticeI.getImNoticeId().equals(noticeH.getNoticeId())){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("操作失败，此托盘不在任务中");
        }
        ajaxJson.setObj(pallet);
        return ajaxJson;
    }

    @RequestMapping(params = "readBinCode")
    @ResponseBody
    public AjaxJson readBinCode(String barCode, String palletId){
        Map map = new HashMap();
        AjaxJson ajaxJson = new AjaxJson();
        //托盘码和货架码是否对得上
        MdBinEntity bin = systemService.findUniqueByProperty(MdBinEntity.class,"kuWeiTiaoMa",barCode);
        MdPalletEntity pallet = systemService.getEntity(MdPalletEntity.class,palletId);
        if(bin == null || pallet == null) {
        	ajaxJson.setSuccess(false);
        	ajaxJson.setMsg("无此托盘或货位");
        	return ajaxJson;
        }
        if(!bin.getKuWeiBianMa().equals(pallet.getBinBianMa())){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("操作失败，此货架不是目标货架");
            return ajaxJson;
        }
        List<MdPalletEntity> pallets = storeAssignmentService.getPalletsForMdBin(bin.getId());
        pallet.setBinDepth(pallets.size() + 1);
        systemService.updateEntitie(pallet);

        String goods = "";
        if(pallets.size() <= 0){
            goods = pallet.getZhuangLiaoMingCheng();
        } else{
            goods = pallets.get(0).getZhuangLiaoMingCheng();
        }
        map.put("id",bin.getId());
        map.put("kuWeiBianMa",bin.getKuWeiBianMa());
        map.put("binStore",bin.getBinStore());
        map.put("goodsName",goods);
        map.put("palletCount",pallets.size());
        ajaxJson.setObj(map);
        return ajaxJson;
    }


    @RequestMapping(params = "upover")
    @ResponseBody
    public AjaxJson upover(String palletId){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        AjaxJson ajaxJson = new AjaxJson();
        //更新托盘状态
        MdPalletEntity pallet = systemService.getEntity(MdPalletEntity.class, palletId);
        pallet.setTuoPanZhuangTai(PalletStatus.IN_SHELF);
        systemService.saveOrUpdate(pallet);
        //更新验收单状态
        String hql = "from WmInQmIEntity wm where wm.imNoticeItem=? and wm.tinId=? and binSta=?";
        List<WmInQmIEntity> qmilist =  wmInQmIService.findHql(hql,pallet.getEntryKey(),pallet.getTuoPanBianMa(),"N");
        if(qmilist == null||qmilist.size() <= 0) {
        	ajaxJson.setSuccess(false);
        	ajaxJson.setMsg("没有数据");
        	return ajaxJson;
        }
        WmInQmIEntity qmi = qmilist.get(0);
        qmi.setBinSta("Y");
        systemService.saveOrUpdate(qmi);
        //更新上架任务状态，留记录
        hql = "from MdMovePalletEntity mp where mp.type='上架' and mp.status=? and mp.palletCode=?";
        MdMovePalletEntity palletMove = (MdMovePalletEntity) systemService.findHql(hql, TaskStatus.INIT,pallet.getTuoPanBianMa()).get(0);
        palletMove.setStatus(TaskStatus.FINISHED);
        systemService.saveOrUpdate(palletMove);
        //增加上架记录
        WmToUpGoodsEntity upGoods = new WmToUpGoodsEntity();
        upGoods.setGoodsId(qmi.getGoodsId());
        upGoods.setGoodsProData(qmi.getProData());
        upGoods.setGoodsBatch(qmi.getGoodsBatch());
        upGoods.setGoodsQua(qmi.getQmOkQuat());
        upGoods.setGoodsUnit(qmi.getGoodsUnit());
        upGoods.setOrderIdI(qmi.getId());
        upGoods.setOrderId(qmi.getImNoticeId());
        upGoods.setBinId(qmi.getTinId());
        upGoods.setKuWeiBianMa(qmi.getBinId());
        upGoods.setCusCode(qmi.getCusCode());
        upGoods.setGoodsName(qmi.getGoodsName());
        upGoods.setActTypeCode("01");
        systemService.save(upGoods);

        //更新库存
        WmStockBaseStockEntity stock = systemService.findUniqueByProperty(WmStockBaseStockEntity.class, "kuWeiBianMa", pallet.getBinBianMa());
        WmImNoticeIEntity imi = systemService.getEntity(WmImNoticeIEntity.class, pallet.getEntryKey());
        MdBinEntity bin = systemService.findUniqueByProperty(MdBinEntity.class,"kuWeiBianMa",pallet.getBinBianMa());

        if(stock == null){
            stock = new WmStockBaseStockEntity();
            stock.setKuWeiBianMa(pallet.getBinBianMa());
            stock.setBinId(bin.getId());
            stock.setBaseGoodscount(pallet.getShuLiang());
            stock.setGoodsId(pallet.getZhuangLiaoBianMa());
            //stock.setGoodsProData(pallet.get);
            stock.setGoodsQua(pallet.getShuLiang());
            stock.setOrderId(pallet.getEntryKey());
            stock.setBaseGoodscount(pallet.getShuLiang());
            stock.setGoodsProData(imi.getGoodsPrdData()==null?"":format.format(imi.getGoodsPrdData()));
            stock.setGoodsBzhiqi(imi.getBzhiQi());
            systemService.save(stock);
        }else{
            if(!StringUtil.isEmpty(stock.getGoodsQua())){
                Long old = Long.valueOf(stock.getGoodsQua());
                Long newq = old + Long.valueOf(pallet.getShuLiang());
                stock.setGoodsQua(String.valueOf(newq));
                stock.setBaseGoodscount(stock.getGoodsQua());
                systemService.updateEntitie(stock);
            }else{
                stock.setGoodsQua(pallet.getShuLiang());
                stock.setBaseGoodscount(pallet.getShuLiang());
                if(Long.valueOf(stock.getGoodsQua()) <= 0){
                    stock.setOrderId(pallet.getEntryKey());
                    stock.setGoodsId(imi.getGoodsCode());
                    stock.setGoodsProData(imi.getGoodsPrdData()==null?"":format.format(imi.getGoodsPrdData()));
                    stock.setGoodsBzhiqi(imi.getBzhiQi());
                    stock.setGoodsUnit(imi.getGoodsUnit());
                }
                systemService.updateEntitie(stock);
            }
        }

        return ajaxJson;
    }

    @RequestMapping(params = "exceptionUpload")
    @ResponseBody
    public AjaxJson exceptionUpload(String binId, String expDetail){
        AjaxJson ajaxJson = new AjaxJson();

        MdBinEntity bin = systemService.getEntity(MdBinEntity.class,binId);
        if(bin == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("查无此仓");
            return ajaxJson;
        }
        /*MdPalletEntity pallet = systemService.getEntity(MdPalletEntity.class, palletId);
        String hql = "from MdMovePalletEntity mp where mp.triggerSource=? mp.palletCode=? and mp.type=?";
        List<MdMovePalletEntity> move = systemService.findHql(hql, pallet.getEntryKey(), pallet.getTuoPanBianMa(), "上架");*/

        MdExceptionEntity exp = new MdExceptionEntity();
        exp.setType(ExceptionType.EXP_BIN);
        exp.setDetail(expDetail);
        exp.setBinId(bin.getId());
        exp.setBinCode(bin.getKuWeiBianMa());
        exp.setStatus(TaskStatus.INIT);
        systemService.save(exp);

        return ajaxJson;
    }
}
