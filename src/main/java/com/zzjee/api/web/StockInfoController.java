package com.zzjee.api.web;

import com.google.gson.Gson;
import com.zzjee.api.service.StockInfoServiceI;
import com.zzjee.api.utils.NCDataObject;
import com.zzjee.api.utils.ParamReceiver.ProductStoreVo;
import com.zzjee.api.utils.ParamReceiver.WmOmTask;
import com.zzjee.api.utils.ParamReceiver.WmOmTaskDetail;
import com.zzjee.api.utils.ResultObject;
import com.zzjee.ba.entity.BaStoreEntity;
import com.zzjee.flow.util.TaskStatus;
import com.zzjee.md.entity.MdGoodsEntity;
import com.zzjee.md.entity.MdMovePalletEntity;
import com.zzjee.md.entity.MdPalletEntity;
import com.zzjee.md.entity.PalletStatus;
import com.zzjee.pm.entity.PmBasicMaterialEntity;
import com.zzjee.pm.entity.PmMaterialListEntity;
import com.zzjee.pm.entity.PmProcTaskEntity;
import com.zzjee.pm.entity.PmProcTaskOrderEntity;
import com.zzjee.pm.service.PmMaterialListServiceI;
import com.zzjee.pm.service.PmProcTaskOrderServiceI;
import com.zzjee.pm.service.PmProcTaskServiceI;
import com.zzjee.wm.entity.*;
import com.zzjee.wm.service.WmImNoticeHServiceI;
import com.zzjee.wm.service.WmOmNoticeHServiceI;
import com.zzjee.wmutil.WmsContants;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.sms.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/api/V1")
public class StockInfoController {
    @Autowired
    SystemService systemService;
    @Autowired
    WmOmNoticeHServiceI omNoticeHServiceI;
    @Autowired
    PmProcTaskServiceI pmProcTaskService;
    @Autowired
    PmProcTaskOrderServiceI pmProcTaskOrderService;
    @Autowired
    PmMaterialListServiceI pmMaterialListService;
    @Autowired
    WmImNoticeHServiceI wmImNoticeHService;
    @Autowired
    StockInfoServiceI stockInfoService;



    /**
     * 2.1.4成品出库接口，与NC系统交互
     * */
    @RequestMapping(value = "/pushOutOrder")
    @ResponseBody
    public ResultObject pushOutOrder(@RequestBody WmOmTask wmOmTask) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        TSUser user = ResourceUtil.getSessionUserName();
        List<WmOmTaskEntity> list = new ArrayList<>();
        for (WmOmTaskDetail detail : wmOmTask.getList()){
            WmOmTaskEntity temp = new WmOmTaskEntity();
            temp.setNcPkid(wmOmTask.getErp_baseid());
            temp.setOrderKey(wmOmTask.getOrderKey());
            temp.setProductId(detail.getProductId());
            temp.setProductName(detail.getProdectName());
            try {
                temp.setOutDate(format.parse(wmOmTask.getOutDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            temp.setOutQuantity(detail.getOutQuantity());
            temp.setOutWeight(detail.getOutWeight());
            temp.setQcStaff(wmOmTask.getQcstaff());
            list.add(temp);
            systemService.save(temp);
        }
        /*omTask.setQcStaff(QCstaff);
        systemService.save(omTask);
        if(StringUtil.isEmpty(omTask.getProductId()) || StringUtil.isEmpty(omTask.getOutWeight()))
//            return ResultObject.error("更新失败，请检查参数是否正确");*/
        WmOmNoticeHEntity omEntity = new WmOmNoticeHEntity();

        omEntity.setOmNoticeId(wmOmTask.getOrderKey());
        omEntity.setOrderTypeCode("21");
        omEntity.setOrderType("21");
        omEntity.setQcStaff(wmOmTask.getQcstaff());
        try {
            omEntity.setDelvData(format.parse(wmOmTask.getOutDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(ResourceUtil.getConfigByName("wms.stock_io_confirm").equals("true"))
            omEntity.setOmSta(WmsContants.CONFIRMING);//数据字典：转移
        else
            omEntity.setOmSta(WmsContants.CONFIRMED);
        systemService.save(omEntity);
        for (WmOmTaskEntity omTask : list){
            List<WmOmNoticeIEntity> iEntities = stockInfoService.generateOmIEntitys(omTask);
            for (WmOmNoticeIEntity entity:iEntities){
                entity.setOmNoticeId(omEntity.getOmNoticeId());
                systemService.save(entity);

                String hql = "from MdPalletEntity mp where mp.tuoPanZhuangTai = ? AND mp.tuoPanBianMa IN ('"+entity.getBinId().replaceAll(",","','")+"') ORDER BY mp.binBianMa,mp.binDepth desc DESC;";
                List<MdPalletEntity> pallets = systemService.findHql(hql,PalletStatus.IN_SHELF);
                for (MdPalletEntity pallet : pallets){
                    WmOmQmIEntity wmiTemp = new WmOmQmIEntity();
                    wmiTemp.setUpdateDate(new Date());
                    wmiTemp.setOmNoticeId(entity.getOmNoticeId());
                    wmiTemp.setIomNoticeItem(entity.getId());
                    wmiTemp.setGoodsId(entity.getGoodsId());
                    wmiTemp.setGoodsName(entity.getGoodsName());
                    wmiTemp.setOmQuat(pallet.getShuLiang());
                    wmiTemp.setQmOkQuat("0");
                    wmiTemp.setBaseGoodscount(pallet.getShuLiang());
                    wmiTemp.setTinZhl(pallet.getZhongLiang());
                    wmiTemp.setProData(entity.getGoodsProData()!=null?format.format(entity.getGoodsProData()):null);
                    wmiTemp.setGoodsBatch(entity.getGoodsBatch());
                    /*String sql = "SELECT GROUP_CONCAT(mp.tuo_pan_bian_ma) as 'codeList' FROM md_pallet mp WHERE mp.tuo_Pan_Zhuang_Tai=? AND mp.bin_Bian_Ma IN "+"('"+entity.getBinOm().replaceAll(",","','")+"')"+";";
                    Map map = systemService.findOneForJdbc(sql, PalletStatus.IN_SHELF);*/
                    /*String hql = "from MdPalletEntity mp where mp.tuoPanZhuangTai = ? AND mp.binBianMa IN ('"+entity.getBinOm().replaceAll(",","','")+"') ORDER BY mp.updateDate DESC;";
                    List<MdPalletEntity> pallets = systemService.findHql(hql,PalletStatus.IN_SHELF);*/
                    /*String palletCodes = "";
                    String binCodes = "";
                    long count = 0;
                    for (MdPalletEntity pallet : pallets){
                        count += Double.valueOf(pallet.getShuLiang());
                        palletCodes += StringUtil.isEmpty(palletCodes)?pallet.getTuoPanBianMa():(","+pallet.getTuoPanBianMa());
                        if(!binCodes.matches("\\b"+pallet.getBinBianMa()+"\\b"))
                            binCodes += StringUtil.isEmpty(binCodes)?pallet.getBinBianMa():(","+pallet.getBinBianMa());
                        if(count >= Long.valueOf(entity.getGoodsQua())){
                            break;
                        }
                    }*/
                    /*if(pallets.size()>=0){
                        String hql_temp = "from WmImNoticeHEntity wmh where "
                    }*/
                    wmiTemp.setBinId(pallet.getBinBianMa());
                    wmiTemp.setTinId(pallet.getTuoPanBianMa());
                    wmiTemp.setBinSta("N");
                    systemService.save(wmiTemp);

                    MdMovePalletEntity movePallet = new MdMovePalletEntity();
                    movePallet.setPalletCode(pallet.getTuoPanBianMa());
                    movePallet.setOperateBy(user==null?null:user.getUserKey());
                    movePallet.setType("下架");
                    movePallet.setRecord(pallet.getBinBianMa() + "->" + omEntity.getOmPlatNo());
                    movePallet.setStatus(TaskStatus.INIT);
                    movePallet.setTriggerSource(entity.getId());
                    systemService.save(movePallet);

                    pallet.setTuoPanZhuangTai(PalletStatus.IN_DOWN);
                    systemService.updateEntitie(pallet);
                }
            }
            //omNoticeHServiceI.addMain(omEntity,iEntities);
        }

        return ResultObject.success("推送成功");
    }

    /**2.2.1向WMS系统推送排产任务信息接口*/
    @RequestMapping(value = "/pushTaskMsg")
    @ResponseBody
    public ResultObject pushTaskMsg(@RequestBody PmProcTaskEntity task) throws Exception{
        PmProcTaskEntity old = systemService.findUniqueByProperty(PmProcTaskEntity.class,"taskId",task.getTaskId());
        if(old == null){
            pmProcTaskService.save(task);
        }else{
            old.setTaskStatus(task.getTaskStatus());
            pmProcTaskService.saveOrUpdate(old);
        }
        PmProcTaskOrderEntity orderEntity = new PmProcTaskOrderEntity();
        orderEntity.setOrderId(task.getOrderNum());
        orderEntity.setTaskId(task.getTaskId());
        pmProcTaskOrderService.save(orderEntity);

        /* ------------------- */
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        WmOmNoticeHEntity hEntity = stockInfoService.findUniqueByProperty(WmOmNoticeHEntity.class,"orderNum",task.getOrderNum());
        if(hEntity == null){
            hEntity = new WmOmNoticeHEntity();
            hEntity.setOmNoticeId(task.getOrderNum());
            hEntity.setOrderNum(task.getOrderNum());
            if(ResourceUtil.getConfigByName("wms.stock_io_confirm").equals("true"))
                hEntity.setOmSta(WmsContants.CONFIRMING);
            else
                hEntity.setOmSta(WmsContants.CONFIRMED);
            hEntity.setOrderType("12");
            hEntity.setOrderTypeCode("12");
            hEntity.setTaskCode(","+task.getTaskId()+",");
            hEntity.setDelvData(task.getProductDate());
            systemService.save(hEntity);
        }
        /*-------------------*/

        List<PmMaterialListEntity> list = task.getMaterial();
        for (PmMaterialListEntity entity : list) {
            entity.setOrderNum(task.getOrderNum());
            entity.setTaskId(task.getTaskId());
            pmMaterialListService.save(entity);

            /*----------------------*/
            WmOmNoticeIEntity iTemp = new WmOmNoticeIEntity();
            iTemp.setOmNoticeId(hEntity.getOmNoticeId());
            iTemp.setGoodsId(entity.getCode());
            iTemp.setGoodsName(entity.getName());
//            iTemp.setGoodsQua();
            iTemp.setGoodsUnit("包");
            iTemp.setGoodsWeight(entity.getWeight());
            iTemp.setWeightUnit("KG");
            iTemp.setGoodsProData(task.getProductDate());
//            iTemp.setGoodsBatch();
            if(ResourceUtil.getConfigByName("wms.stock_io_confirm").equals("true"))
                iTemp.setOmSta(WmsContants.CONFIRMING);
            else
                iTemp.setOmSta(WmsContants.CONFIRMED);
            systemService.save(iTemp);
            /*----------------------*/
        }




        return ResultObject.success("推送成功");
    }

    /**2.2.2WinCos成品入库，WMS更新入库信息*/
    @RequestMapping(value = "/updateProduction")
    @ResponseBody
    public ResultObject updateProduction(@RequestBody ProductStoreVo pStore){
        WmImNoticeHEntity hEntity = pStore.generateImNoticeH();
        WmImNoticeHEntity oldHEntity = systemService.findUniqueByProperty(WmImNoticeHEntity.class, "noticeId", hEntity.getNoticeId());
        if (oldHEntity != null) {
            oldHEntity.setImSta(hEntity.getImSta());
//            ArrayList<WmImNoticeIEntity> list = new ArrayList<WmImNoticeIEntity>();
//            list.addAll(systemService.findByProperty(WmImNoticeIEntity.class, "imNoticeId", oldHEntity.getNoticeId()));
//            list.add(pStore.generateImNoticeI());
//            wmImNoticeHService.updateMain(oldHEntity, list);
            WmImNoticeIEntity iEntity = pStore.generateImNoticeI();
            iEntity.setBarCode(systemService.findUniqueByProperty(MdGoodsEntity.class,"shpBianMa",iEntity.getGoodsCode()).getShpTiaoMa());
            systemService.save(iEntity);
        } else {
//            ArrayList<WmImNoticeIEntity> list = new ArrayList<WmImNoticeIEntity>();
//            list.add(pStore.generateImNoticeI());
//            wmImNoticeHService.addMain(hEntity, list);
            systemService.save(hEntity);
            WmImNoticeIEntity iEntity = pStore.generateImNoticeI();
            iEntity.setBarCode(systemService.findUniqueByProperty(MdGoodsEntity.class,"shpBianMa",iEntity.getGoodsCode()).getShpTiaoMa());
            systemService.save(iEntity);
        }
        return ResultObject.success("更新成功");
    }

    /**2.2.3获取原料入库单信息*/
    @RequestMapping(value = "/getStuffInventory")
    @ResponseBody
    public ResultObject getStuffInventory(){
        List<Map> resultList = new ArrayList<Map>();
        List<Map<String, Object>> hList = systemService.findForJdbc("SELECT * FROM wm_im_notice_h WHERE order_type_code = '05' ORDER BY CREATE_DATE DESC LIMIT 50");
        for (Map<String, Object> hEntity : hList) {
            List<WmImNoticeIEntity> tempList = systemService.findByProperty(WmImNoticeIEntity.class,"imNoticeId",hEntity.get("notice_id"));
            Map<String, Object> tempMap = new HashMap<String, Object>();
            tempMap.put("storageNo",hEntity.get("notice_id"));
            tempMap.put("supplierNo",hEntity.get("sup_code"));
            List<Map> iList = new ArrayList<Map>();
            for (WmImNoticeIEntity iEntity : tempList) {
                Map<String, Object> iMap = new HashMap<String, Object>();
                iMap.put("stuffCode", iEntity.getGoodsCode());
                iMap.put("stuffName", iEntity.getGoodsName());
                iMap.put("stuffWeight", iEntity.getGoodsWeight());
                iMap.put("packType", iEntity.getBaseUnit());
                iMap.put("specification", iEntity.getShpGuiGe());
                iMap.put("batchNo", iEntity.getGoodsBatch());
                iMap.put("warranty", iEntity.getBzhiQi());
                iList.add(iMap);
            }
            tempMap.put("list",iList);
            resultList.add(tempMap);
        }

        return ResultObject.success("查询成功",resultList);
    }

    /**2.2.4物料数据接口*/
    @RequestMapping(value = "/getStuffMsg")
    @ResponseBody
    public ResultObject getStuffMsg(){
        List<PmBasicMaterialEntity> list = systemService.loadAll(PmBasicMaterialEntity.class);
        return ResultObject.success("查询成功",list);
    }

    /**
     * 2.1.5查询物料信息
     */
    @RequestMapping(value = "/findMaterialInfo")
    @ResponseBody
    public void findMaterialInfo(){
        NCDataObject data = null;
        try {
            data = stockInfoService.requestMethode("alWMSQry","inv","{}");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(data == null){
            System.out.println("请求失败！");
        } else{
            for (Map<String, Object> map : (List<Map>)data.getResult()){
                String id = (String) map.get("pk_invbasdoc");
                PmBasicMaterialEntity temp = systemService.findUniqueByProperty(PmBasicMaterialEntity.class,"ncPkid",id);
                if(temp != null){
                    temp.setStuffCode((String) map.get("invcode"));
                    temp.setStuffName((String) map.get("invname"));
                    temp.setStuffType((String) map.get("property"));
                    temp.setRemarks((String) map.get("measname"));
                    systemService.saveOrUpdate(temp);
                }else{
                    temp = new PmBasicMaterialEntity();
                    temp.setNcPkid(id);
                    temp.setStuffCode((String) map.get("invcode"));
                    temp.setStuffName((String) map.get("invname"));
                    temp.setStuffType((String) map.get("property"));
                    temp.setRemarks((String) map.get("measname"));
                    systemService.save(temp);
                }
            }
        }
    }

    @RequestMapping(value = "/findStoreInfo")
    @ResponseBody
    public void findStoreInfo(){
        NCDataObject data = null;
        Gson gson = new Gson();
        try {
            data = stockInfoService.requestMethode("alWMSQry","store","{}");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(data == null){
            System.out.println("请求失败！");
        }else{
            for (Map<String, Object> map : (List<Map>)data.getResult()){
                String id = (String) map.get("pk_stordoc");
                BaStoreEntity entity = systemService.findUniqueByProperty(BaStoreEntity.class,"ncPkid",id);
                if(entity != null){
                    entity.setStoreName((String) map.get("storcode"));
                    entity.setStoreName((String) map.get("storname"));
                    systemService.saveOrUpdate(entity);
                }else{
                    entity = new BaStoreEntity();
                    entity.setNcPkid(id);
                    entity.setStoreName((String) map.get("storcode"));
                    entity.setStoreName((String) map.get("storname"));
                    systemService.save(entity);
                }
            }
        }
    }

    /**
     * 2.1.1由WMS查询入库原料信息,根据单号
     */
    @RequestMapping(value = "/findImNotice")
    @ResponseBody
    public void findImNotice(String imKey, String imDate){
        try {
            stockInfoService.findImNotice(imKey,imDate);
        } /*catch (ParseException e) {
            e.printStackTrace();

        } */catch (IOException e) {
            e.printStackTrace();
            System.out.println("网络请求失败");
        }
    }

    /**
     * 2.1.3向NC系统推送成品入库信息接口
     */
    @RequestMapping(value = "/pushInStorage")
    @ResponseBody
    public void pushInStorage(){
        try {
            stockInfoService.pushInStorage();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("网络请求失败");
        }
    }

    /**
     * 2.1.2排产完毕，向NC系统推送排产信息
     */
    @RequestMapping(value = "/pushProductTask")
    @ResponseBody
    public void pushProductTask(){
        try {
            stockInfoService.pushProductTask();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("网络请求失败");
        }
    }


    /**
     * 销售出库确认
     * @param outKey
     */
    @RequestMapping(value = "/outConfirm")
    @ResponseBody
    public void outConfirm(String outKey){
        try {
            stockInfoService.outConfirm(outKey);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("网络请求失败");
        }
    }

    /**
     * 实际采购入库数量上传
     * @param imNoticeId
     */
    @RequestMapping(value = "/realImUpload")
    @ResponseBody
    public void realImUpload(String imNoticeId){
        try {
            stockInfoService.realImUpload(imNoticeId);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("网络请求失败");
        }
    }
}
