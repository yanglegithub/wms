package com.zzjee.flow.web;

import com.zzjee.api.service.StockInfoServiceI;
import com.zzjee.ba.entity.BaPlatformEntity;
import com.zzjee.flow.service.StoreAssignmentServiceI;
import com.zzjee.flow.util.ExceptionType;
import com.zzjee.flow.util.TaskStatus;
import com.zzjee.md.entity.*;
import com.zzjee.wm.entity.WmImNoticeHEntity;
import com.zzjee.wm.entity.WmImNoticeIEntity;
import com.zzjee.wm.entity.WmInQmIEntity;
import com.zzjee.wm.page.WmImNoticeHPage;
import com.zzjee.wm.service.WmImNoticeHServiceI;
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
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/flow/materialsStock")
public class MaterialsStockController {
    @Resource
    private SystemService systemService;

    @Resource
    private WmImNoticeHServiceI wmImNoticeHService;

    @Resource
    private WmImNoticeHServiceI wmImNoticeIService;
    @Resource
    private StoreAssignmentServiceI storeAssignmentService;
    @Resource
    private StockInfoServiceI stockInfoService;

    @RequestMapping(params = "findStockTaskCount")
    @ResponseBody
    public AjaxJson findStockTaskCount(String type){
        AjaxJson ajaxJson = new AjaxJson();
        String hql = "from WmImNoticeIEntity wm where wm.binPre='N' and wm.noticeiSta=? and exists( from WmImNoticeHEntity h where h.noticeId=wm.imNoticeId and h.orderTypeCode=? )";
        List<WmImNoticeIEntity> mlist = wmImNoticeIService.findHql(hql, WmsContants.IN_STOCK,"01");
        List<WmImNoticeIEntity> plist = wmImNoticeIService.findHql(hql, WmsContants.IN_STOCK,"20");

        Map map = new HashMap();
        map.put("materialsCount",mlist.size());
        map.put("productsCount",plist.size());
        ajaxJson.setObj(map);
        return ajaxJson;
    }

    @RequestMapping(params = "findStockTask")
    @ResponseBody
    public AjaxJson findStockTask(String type){
//        List<WmImNoticeHEntity> list = wmImNoticeHService.findByProperty(WmImNoticeHEntity.class,"imSta", WmsContants.WM_IM_H_OPERATION);
//        List<Map> results = new ArrayList<Map>();
//        for(WmImNoticeHEntity entity : list){
//            List<WmImNoticeIEntity> temp = wmImNoticeIService.findByNoticeIdAndStaus(entity.getNoticeId(),WmsContants.WM_IM_I_DOWN);
        String hql = "from WmImNoticeIEntity wm where wm.binPre='N' and wm.noticeiSta=? and exists( from WmImNoticeHEntity h where h.noticeId=wm.imNoticeId and h.orderTypeCode=? )";
        List<Map> results = new ArrayList<Map>();
        List<WmImNoticeIEntity> temp = systemService.findHql(hql, WmsContants.IN_STOCK, type);
            for (WmImNoticeIEntity iEntity : temp){
                WmImNoticeHEntity hEntity = wmImNoticeHService.findUniqueByProperty(WmImNoticeHEntity.class,"noticeId",iEntity.getImNoticeId());
                Map map = new HashMap<>();
                map.put("id",iEntity.getId());
                map.put("orderKey",iEntity.getImNoticeId());
                map.put("goodsCode",iEntity.getGoodsCode());
                map.put("goodsName",iEntity.getGoodsName());
                map.put("goodsBatch",iEntity.getGoodsBatch());
                map.put("goodsCount",iEntity.getGoodsCount());
                map.put("goodsWeigt",iEntity.getGoodsWeight());
                map.put("siteCode",hEntity.getPlatformCode());
                BaPlatformEntity platTemp = systemService.findUniqueByProperty(BaPlatformEntity.class,"platformCode",hEntity.getPlatformCode());
                map.put("siteName",platTemp==null?"":platTemp.getPlatformName());
                results.add(map);
            }
//        }
        AjaxJson j = new AjaxJson();
        j.setObj(results);
        return j;
    }

    @RequestMapping(params = "findStockTaskById")
    @ResponseBody
    public AjaxJson findStockTaskById(String id){
        AjaxJson ajaxJson = new AjaxJson();
        WmImNoticeIEntity entity = systemService.getEntity(WmImNoticeIEntity.class,id);
        MdGoodsEntity good = systemService.findUniqueByProperty(MdGoodsEntity.class, "shpBianMa", entity.getGoodsCode());
        if(entity == null){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("操作失败，无此对象");
            return ajaxJson;
        }
        Long palletCount = 0L;
        try{
            palletCount = Long.parseLong(good.getMpCengGao()) * Long.parseLong(good.getMpDanCeng());
        } catch(Exception e) {
            ajaxJson.setMsg("货物数据错误");
        }
        entity.setBaseGoodscount(palletCount.toString());
        ajaxJson.setObj(entity);
        return ajaxJson;
    }

    @RequestMapping(params = "findPalletByBarCode")
    @ResponseBody
    public AjaxJson findPalletByBarCode(String barCode){
        AjaxJson ajaxJson = new AjaxJson();
        MdPalletEntity pallet = systemService.findUniqueByProperty(MdPalletEntity.class,"tuoPanTiaoMa",barCode);
        if(pallet == null || !PalletStatus.IDLE.equals(pallet.getTuoPanZhuangTai())){
            ajaxJson.setSuccess(false);
            if(pallet == null)
                ajaxJson.setMsg("操作失败，查无此盘");
            else
                ajaxJson.setMsg("操作失败，托盘占用");
            return ajaxJson;
        }
        ajaxJson.setObj(pallet);
        return ajaxJson;
    }

    @RequestMapping(params = "palletOver")
    @ResponseBody
    public AjaxJson palletOver(MdPalletEntity pallet,String goodsPrdData, String bzhiQi){
        AjaxJson ajaxJson = new AjaxJson();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TSUser user = ResourceUtil.getSessionUser();
        bzhiQi = (bzhiQi == null)?"180":bzhiQi;

        MdPalletEntity palletEntity = systemService.getEntity(MdPalletEntity.class,pallet.getId());
        WmImNoticeIEntity iEntity = systemService.findUniqueByProperty(WmImNoticeIEntity.class,"id",pallet.getEntryKey());
        WmImNoticeHEntity h = systemService.findUniqueByProperty(WmImNoticeHEntity.class, "noticeId", iEntity.getImNoticeId());
        if(h == null) {
        	ajaxJson.setSuccess(false);
        	ajaxJson.setMsg("查无此单");
        	return ajaxJson;
        }
        //分配储位
        MdBinEntity bin = null;
        if(h.getOrderTypeCode().equals(WmsContants.IN_MATERIALS))
        	bin = storeAssignmentService.assignmentForPallet(pallet,"CK001");
        else
        	bin = storeAssignmentService.assignmentForPallet(pallet,"CK002");
        if(bin == null) {
        	ajaxJson.setSuccess(false);
        	ajaxJson.setMsg("储位分配失败");
        	return ajaxJson;
        }

        //查看生产日期和保质期是否和上一个码垛一样，若不一样，则返回错并返回上一个填写的生产日期和保质期
        List<MdPalletEntity> storedPallets = wmImNoticeHService.findPalletsById(pallet.getEntryKey());
        if(storedPallets != null && storedPallets.size() >= 1){
//            if(!format.format(iEntity.getGoodsPrdData()).equals(goodsPrdData) || iEntity.getBzhiQi().equals(bzhiQi)){
        	if(!goodsPrdData.equals(format.format(iEntity.getGoodsPrdData())) || !bzhiQi.equals(iEntity.getBzhiQi())) {
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("生产日期和保质期不匹配");
                ajaxJson.setObj("{\"goodsPrdData\":\""+format.format(iEntity.getGoodsPrdData())+"\",\"bzhiQi\":\""+iEntity.getBzhiQi()+"\"}");
                return ajaxJson;
            }
        }
        //保存托盘数据
        pallet.setId(null);
        pallet.setTuoPanBianMa(null);
        pallet.setTuoPanTiaoMa(null);
        try {
            MyBeanUtils.copyBeanNotNull2Bean(pallet,palletEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        palletEntity.setBinBianMa(bin.getKuWeiBianMa());
        palletEntity.setBinTiaoMa(bin.getKuWeiTiaoMa());
        palletEntity.setTuoPanZhuangTai(PalletStatus.IN_UP);
        palletEntity.setLiLunGuiGe(iEntity.getShpGuiGe());
        palletEntity.setZhongLiang(String.valueOf(Double.valueOf(iEntity.getShpGuiGe()) * Double.valueOf(pallet.getShuLiang())));
        palletEntity.setZhongLiangDanWei("KG");
        systemService.saveOrUpdate(palletEntity);
        //保存收货数据
        WmInQmIEntity inQmIEntity = new WmInQmIEntity();
        WmImNoticeIEntity wmImNoticeI = systemService.getEntity(WmImNoticeIEntity.class,
        		palletEntity.getEntryKey());
        WmImNoticeHEntity hEntity = systemService.findUniqueByProperty(WmImNoticeHEntity.class,"noticeId",wmImNoticeI.getImNoticeId());
        inQmIEntity.setImNoticeId(wmImNoticeI.getImNoticeId());
        inQmIEntity.setImNoticeItem(wmImNoticeI.getId());
        inQmIEntity.setGoodsId(wmImNoticeI.getGoodsCode());
        inQmIEntity.setImCusCode(wmImNoticeI.getImCusCode());
        inQmIEntity.setGoodsName(wmImNoticeI.getGoodsName());
        inQmIEntity.setImQuat(palletEntity.getShuLiang());
        inQmIEntity.setQmOkQuat(palletEntity.getShuLiang());
        inQmIEntity.setProData(goodsPrdData);
        inQmIEntity.setTinId(palletEntity.getTuoPanBianMa());
        inQmIEntity.setGoodsUnit(wmImNoticeI.getGoodsUnit());
        inQmIEntity.setGoodsBatch(wmImNoticeI.getGoodsBatch());
        inQmIEntity.setBinId(palletEntity.getBinBianMa());
        inQmIEntity.setTinZhl(palletEntity.getZhongLiang());
        inQmIEntity.setBinSta("N");
//        inQmIEntity.setCusCode(wmImNoticeI.getImCusCode());
//        inQmIEntity.setCusName(wmImNoticeI.);
        systemService.save(inQmIEntity);
        //生成码垛任务，留记录
        MdMovePalletEntity palletStock = new MdMovePalletEntity();
        palletStock.setTriggerSource(wmImNoticeI.getId());
        palletStock.setPalletCode(palletEntity.getTuoPanBianMa());
        palletStock.setOperateBy(user!=null?user.getUserName():"");
        palletStock.setType("码垛");
        palletStock.setRecord(hEntity.getPlatformCode());
        palletStock.setStatus(TaskStatus.FINISHED);
        systemService.save(palletStock);
        //生成上架任务
        MdMovePalletEntity palletMove = new MdMovePalletEntity();
        palletMove.setTriggerSource(wmImNoticeI.getId());
        palletMove.setPalletCode(palletEntity.getTuoPanBianMa());
//        palletStock.setOperateBy(user.getUserName());
        palletMove.setType("上架");
        palletMove.setRecord(hEntity.getPlatformCode() + "->" + palletEntity.getBinBianMa());
        palletMove.setStatus(TaskStatus.INIT);
        systemService.save(palletMove);




        try {
        	iEntity.setGoodsQmCount(String.valueOf(Long.valueOf(iEntity.getGoodsQmCount()) + Long.valueOf(inQmIEntity.getQmOkQuat())));
            iEntity.setGoodsWqmCount(String.valueOf(Long.valueOf(iEntity.getGoodsWqmCount()) - Long.valueOf(inQmIEntity.getQmOkQuat())));
            iEntity.setGoodsPrdData(format.parse(goodsPrdData));
        } catch (Exception e) {
            e.printStackTrace();
        }
        iEntity.setBzhiQi(bzhiQi);
        systemService.save(iEntity);
        return ajaxJson;
    }

    @RequestMapping(params = "getRealWeight")
    @ResponseBody
    public AjaxJson getRealWeight(String id){
        AjaxJson ajaxJson = new AjaxJson();
        SimpleDateFormat format = new SimpleDateFormat();
        WmImNoticeIEntity iEntity = systemService.getEntity(WmImNoticeIEntity.class,id);
        Map result = new HashMap();
       // try {
            /*List<WmImNoticeHPage> hPages = stockInfoService.findImNoticeFromNC(iEntity.getImNoticeId(),format.format(iEntity.getGoodsStoDate()));
            WmImNoticeHPage hPage = hPages.get(0);
            WmImNoticeIEntity temp = null;
            for (WmImNoticeIEntity iEnt : hPage.getWmImNoticeIList()){
                if(iEnt.getGoodsCode().equals(iEntity.getGoodsCode())){
                    temp = iEnt;
                }
            }
            if(temp != null){*/
                result.put("id",iEntity.getId());
                result.put("goodsCode",iEntity.getGoodsCode());
                result.put("goodsName",iEntity.getGoodsName());
                result.put("goodsWeight",iEntity.getGoodsWeight());
            //}
                /* } catch (IOException e) {
            e.printStackTrace();
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("网络请求错误");
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        ajaxJson.setObj(result);
        return ajaxJson;
    }

    @RequestMapping(params = "stockOver")
    @ResponseBody
    public AjaxJson stockOver(String id, String realWeight, String theoryWeight ){
        AjaxJson j = new AjaxJson();
        boolean isover = true;
        WmImNoticeIEntity iEntity = systemService.getEntity(WmImNoticeIEntity.class,id);
        WmImNoticeHEntity imh = systemService.findUniqueByProperty(WmImNoticeHEntity.class,"noticeId",iEntity.getImNoticeId());
        if(iEntity == null || imh == null){
            j.setSuccess(false);
            j.setMsg("无此入库单明细或此入库抬头");
            return j;
        }
        iEntity.setNoticeiSta(WmsContants.FINISHED);
        iEntity.setBinPre("Y");
        if(WmsContants.IN_PRODUCT.equals(imh.getOrderTypeCode())){
            try{
                Double weight = Long.parseLong(iEntity.getGoodsCount()) * Double.parseDouble(iEntity.getShpGuiGe());
                iEntity.setGoodsQmWeight(weight.toString());
                iEntity.setGoodsWeight(weight.toString());
            }catch (Exception e){
                e.printStackTrace();
                j.setSuccess(false);
                j.setMsg("此入库单数据错误");
                return j;
            }

        }else {
            iEntity.setGoodsQmWeight(realWeight);
            iEntity.setGoodsWeight(theoryWeight);
        }
        systemService.saveOrUpdate(iEntity);

        List<WmImNoticeIEntity> ims = systemService.findByProperty(WmImNoticeIEntity.class,"imNoticeId", iEntity.getImNoticeId());
        for (WmImNoticeIEntity entity : ims){
//            if(!entity.getNoticeiSta().equals(WmsContants.WM_IM_I_END))
//                isover = false;
            if(!WmsContants.FINISHED.equals(entity.getNoticeiSta()))
                isover = false;
        }
        if(isover){
            imh.setImSta(WmsContants.FINISHED);
            systemService.saveOrUpdate(imh);
        }

        return j;
    }

    @RequestMapping(params = "brokenNumber")
    @ResponseBody
    public AjaxJson brokenNumber(String id , String num){
        AjaxJson ajaxJson = new AjaxJson();
        WmImNoticeIEntity im = systemService.getEntity(WmImNoticeIEntity.class,id);
        im.setBrokenCount(num);
        systemService.saveOrUpdate(im);

        return ajaxJson;
    }

    @RequestMapping(params = "exceptionUpload")
    @ResponseBody
    public AjaxJson exceptionUpload(String expDetail, String palletBarCode){
        AjaxJson ajaxJson = new AjaxJson();

        MdPalletEntity mp = systemService.findUniqueByProperty(MdPalletEntity.class, "tuoPanTiaoMa", palletBarCode);

        MdExceptionEntity exp = new MdExceptionEntity();
        exp.setType(ExceptionType.EXP_BARCODE);
        exp.setDetail(expDetail);
        exp.setStatus(TaskStatus.INIT);
        switch(expDetail){
            case ExceptionType.DETAIL_CODENONE:
                exp.setPalletCode(palletBarCode);
                break;
            case ExceptionType.DETAIL_CODEBROKEN:
                break;
            case ExceptionType.DETAIL_CODEBUSY:
                if(mp == null){
                    ajaxJson.setSuccess(false);
                    ajaxJson.setMsg("查无此仓");
                    return ajaxJson;
                }
                exp.setPalletCode(mp.getTuoPanTiaoMa());
                exp.setPalletId(mp.getId());
                break;
        }
        systemService.save(exp);

        return ajaxJson;
    }
}
