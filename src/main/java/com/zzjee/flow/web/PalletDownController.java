package com.zzjee.flow.web;

import com.zzjee.ba.entity.BaPlatformEntity;
import com.zzjee.flow.service.StoreAssignmentServiceI;
import com.zzjee.flow.util.ExceptionType;
import com.zzjee.flow.util.TaskStatus;
import com.zzjee.md.entity.*;
import com.zzjee.wm.entity.*;
import com.zzjee.wmutil.WmsContants;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.sms.util.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/flow/palletDown")
public class PalletDownController {
    @Resource
    SystemService systemService;
    @Resource
    StoreAssignmentServiceI storeAssignmentService;

    @RequestMapping(params = "findPalletCounts")
    @ResponseBody
    public AjaxJson findPalletCounts(){
        AjaxJson ajaxJson = new AjaxJson();
        List<WmOmQmIEntity> qmis = systemService.findByProperty(WmOmQmIEntity.class,"binSta","N");
        Map<String,List<WmOmQmIEntity>> sbuf = new HashMap<>();
        Map result = new HashMap();
        for (WmOmQmIEntity qmi : qmis){
            if(sbuf.containsKey(qmi.getBinId())){
                sbuf.get(qmi.getBinId()).add(qmi);
            }else{
                List<WmOmQmIEntity> qmi_list= new ArrayList<>();
                qmi_list.add(qmi);
                sbuf.put(qmi.getBinId(),qmi_list);
            }
        }

        result.put("count",sbuf.size());
        return null;
    }


    @RequestMapping(params = "findPalletTask")
    @ResponseBody
    public AjaxJson findPalletTask(){
        AjaxJson ajaxJson = new AjaxJson();
        List<WmOmQmIEntity> qmis = systemService.findByProperty(WmOmQmIEntity.class,"binSta","N");
        Map<String,List<WmOmQmIEntity>> sbuf = new HashMap<>();
        List<Map> result = new ArrayList<>();
        for (WmOmQmIEntity qmi : qmis){
            if(sbuf.containsKey(qmi.getBinId())){
                sbuf.get(qmi.getBinId()).add(qmi);
            }else{
                List<WmOmQmIEntity> qmi_list= new ArrayList<>();
                qmi_list.add(qmi);
                sbuf.put(qmi.getBinId(),qmi_list);
            }
        }
        for (Map.Entry entry : sbuf.entrySet()){
            Map result_item = new HashMap();
            String key = (String) entry.getKey();
            List<WmOmQmIEntity> value = (List<WmOmQmIEntity>) entry.getValue();

            WmOmNoticeHEntity h = systemService.findUniqueByProperty(WmOmNoticeHEntity.class, "omNoticeId", value.size()==0?"":value.get(0).getOmNoticeId());
            //BaPlatformEntity plat = systemService.findUniqueByProperty(BaPlatformEntity.class, "platformCode", h==null?"":h.getOmPlatNo());

            result_item.put("binCode",entry.getKey());
            result_item.put("goodsBatch",value.size()>0?value.get(0).getGoodsBatch():null);
            result_item.put("goodsName",value.size()>0?value.get(0).getGoodsName():null);
            result_item.put("palletCount",value.size());
            result_item.put("site",h==null?"":h.getOmPlatNo());
            result.add(result_item);
        }
        ajaxJson.setObj(result);
        return ajaxJson;
    }

    @RequestMapping(params = "findBinInfo")
    @ResponseBody
    public AjaxJson findBinInfo(String hwCode, String hwBarCode){
        AjaxJson ajaxJson = new AjaxJson();
        Map result = new HashMap();
        MdBinEntity bin = systemService.findUniqueByProperty(MdBinEntity.class,"kuWeiTiaoMa",hwBarCode);
        if(bin==null || !bin.getKuWeiBianMa().equals(hwCode)){
            if(bin == null){
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("查无此仓");
            }
            else{
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("货架不匹配");
            }
        }
        String hql = "from MdPalletEntity md where md.binBianMa=? and md.tuoPanZhuangTai in ('货架中','下架中')";
        List<MdPalletEntity> pallets = systemService.findHql(hql, bin.getKuWeiBianMa());
        result.put("binCode",bin.getKuWeiBianMa());
        result.put("goodsName",pallets.size() > 0?pallets.get(0).getZhuangLiaoMingCheng():"");
        result.put("palletsCount",pallets.size());
        ajaxJson.setObj(result);
        return ajaxJson;
    }

    @RequestMapping(params = "findPalletInfo")
    @ResponseBody
    public AjaxJson findPalletInfo(String hwCode, String palletCode){
        AjaxJson ajaxJson = new AjaxJson();
//        MdPalletEntity pallet = systemService.findUniqueByProperty(MdPalletEntity.class,"tuoPanBianMa",palletCode);
        String hql = "from MdPalletEntity mp where mp.binBianMa=? and mp.tuoPanZhuangTai=? order by mp.binDepth DESC limit 1";
        List<MdPalletEntity> downingPallets = systemService.findHql(hql,hwCode,PalletStatus.IN_DOWN);

        if(downingPallets == null || downingPallets.size() <= 0){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("该货架没有正在下架的托盘");
            return ajaxJson;
        } else if(!palletCode.equals(downingPallets.get(0).getTuoPanBianMa())){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("托盘不匹配");
            return ajaxJson;
        }
        ajaxJson.setObj(downingPallets.get(0));
        return ajaxJson;
    }

    @RequestMapping(params = "findPlatInfo")
    @ResponseBody
    public AjaxJson findPlatInfo(String pid, String platCode){
        AjaxJson j = new AjaxJson();

        try{
            String qmihql = "from WmOmQmIEntity qmi where qmi.tinId=? and qmi.binSta='N'";
            WmOmQmIEntity qmi = (WmOmQmIEntity) systemService.findHql(qmihql, pid).get(0);
            WmOmNoticeHEntity h = systemService.findUniqueByProperty(WmOmNoticeHEntity.class, "", qmi.getOmNoticeId());
            BaPlatformEntity plat = systemService.findUniqueByProperty(BaPlatformEntity.class, "platformCode",h.getOmPlatNo());
            if(plat == null){
                j.setSuccess(false);
                j.setMsg("此出库单未指定卸货口");
                return j;
            }
            if(plat.getPlatformBarcode()!= null && plat.getPlatformBarcode().equals(platCode)){
                j.setObj(plat);
            }else{
                j.setSuccess(false);
                j.setMsg("卸货口不匹配");
            }
        } catch (Exception e){
            e.printStackTrace();
            j.setSuccess(false);
            j.setMsg("获取任务或出库单失败");
        }

        return j;
    }

    @RequestMapping(params = "dowmOver")
    @ResponseBody
    public AjaxJson dowmOver(String palletCode){
        AjaxJson ajaxJson = new AjaxJson();
        TSUser user = ResourceUtil.getSessionUser();
        MdPalletEntity pallet = systemService.findUniqueByProperty(MdPalletEntity.class,"tuoPanBianMa",palletCode);
        pallet.setTuoPanZhuangTai(PalletStatus.IN_UNLOAD);
        String palletBinCode = pallet.getBinBianMa();
        pallet.setBinBianMa(null);
        pallet.setBinTiaoMa(null);
        systemService.updateEntitie(pallet);

        String qmihql = "from WmOmQmIEntity qmi where qmi.binSta='N' and qmi.tinId=?";
        List<WmOmQmIEntity> qmis = systemService.findHql(qmihql,palletCode);
        try{
            WmOmQmIEntity qmi = qmis.get(0);
            WmOmNoticeIEntity wmi = systemService.getEntity(WmOmNoticeIEntity.class,qmi.getIomNoticeItem());
            qmi.setBinSta("Y");
            systemService.updateEntitie(qmi);

            WmToDownGoodsEntity wmToDownGoods = new WmToDownGoodsEntity();
            wmToDownGoods.setBinIdFrom(qmi.getTinId());//下架托盘
            wmToDownGoods.setKuWeiBianMa(qmi.getBinId());//储位
            wmToDownGoods.setBinIdTo(qmi.getOmNoticeId());//到托盘
            wmToDownGoods.setCusCode(qmi.getCusCode());//货主
            wmToDownGoods.setGoodsId(qmi.getGoodsId());//
            wmToDownGoods.setGoodsProData(qmi.getProData());//生产日期
            wmToDownGoods.setOrderId(qmi.getOmNoticeId());//出货通知单
            wmToDownGoods.setOrderIdI(qmi.getId());//出货通知项目
            wmToDownGoods.setBaseUnit(qmi.getBaseUnit());//基本单位
            wmToDownGoods.setBaseGoodscount(qmi.getBaseGoodscount());//基本单位数量
            wmToDownGoods.setGoodsUnit(qmi.getGoodsUnit());//出货单位
            wmToDownGoods.setGoodsQua(qmi.getQmOkQuat());//出货数量
            wmToDownGoods.setGoodsQuaok(qmi.getQmOkQuat());//出货数量
            wmToDownGoods.setGoodsName(qmi.getGoodsName());//商品名称
            wmToDownGoods.setOmBeizhu(qmi.getOmBeizhu());//备注
            wmToDownGoods.setImCusCode(qmi.getImCusCode());//客户单号
            wmToDownGoods.setOrderType(systemService.findUniqueByProperty(WmOmNoticeHEntity.class,"omNoticeId",wmi.getOmNoticeId()).getOrderTypeCode());
            systemService.save(wmToDownGoods);

            MdMovePalletEntity unloadTask = new MdMovePalletEntity();
            unloadTask.setPalletCode(pallet.getTuoPanBianMa());
            unloadTask.setOperateBy(user==null?"":user.getUserName());
            unloadTask.setType("拆垛");
            unloadTask.setRecord("");
            unloadTask.setStatus(TaskStatus.INIT);
            unloadTask.setTriggerSource(wmi.getId());
            systemService.save(unloadTask);

            String qmihql_temp = "from WmOmQmIEntity qmi where qmi.binSta='N' and qmi.iomNoticeItem=?";
            List<WmOmQmIEntity> qmis_temp = systemService.findHql(qmihql_temp,qmi.getIomNoticeItem());
            if(qmis_temp.size() <= 0){
                wmi.setPlanSta("Y");
                wmi.setOmSta(WmsContants.OUT_STOCK);
                systemService.updateEntitie(wmi);
            }

            String movehql = "from MdMovePalletEntity mmp where mmp.status=? and mmp.triggerSource=? and mmp.palletCode=?";
            List<MdMovePalletEntity> moves = systemService.findHql(movehql,TaskStatus.INIT,wmi.getId(),palletCode);
            if(moves.size() > 0){
                MdMovePalletEntity temp = moves.get(0);
                temp.setStatus(TaskStatus.FINISHED);
                systemService.updateEntitie(temp);
            }

            //更新库存数据
            MdBinEntity bin = systemService.findUniqueByProperty(MdBinEntity.class,"kuWeiBianMa",palletBinCode);
            WmStockBaseStockEntity stock = systemService.findUniqueByProperty(WmStockBaseStockEntity.class, "kuWeiBianMa", palletBinCode);
            if(stock == null){
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("不存在此托盘的库存");
            }else{
                if(!StringUtil.isEmpty(stock.getGoodsQua())){
                    Long old = Long.valueOf(stock.getGoodsQua());
                    Long newq = old - Long.valueOf(qmi.getOmQuat());
                    stock.setGoodsQua(String.valueOf(newq));
                    stock.setBaseGoodscount(stock.getGoodsQua());
                    systemService.updateEntitie(stock);
                }else{
                    ajaxJson.setSuccess(false);
                    ajaxJson.setMsg("不存在此托盘的库存");
                }
            }
        }catch(Exception e){
        	e.printStackTrace();
            System.out.println("-- 没有查找到对应的WmOmIEntity数据");
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("没有查找到对应的WmOmIEntity数据");
            return ajaxJson;
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
