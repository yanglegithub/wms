package com.zzjee.BI;

import com.zzjee.md.entity.MdBinEntity;
import com.zzjee.md.entity.MdPalletEntity;
import com.zzjee.md.entity.PalletStatus;
import com.zzjee.md.service.MdBinServiceI;
import com.zzjee.md.service.MdPalletServiceI;
import com.zzjee.wm.service.WmStockBaseStockServiceI;
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
@RequestMapping("/dashboardController")
public class DashboardController {
    @Resource
    WmStockBaseStockServiceI wmStockBaseStockService;
    @Resource
    MdBinServiceI mdBinService;
    @Resource
    MdPalletServiceI mdPalletService;
    @Resource
    SystemService systemService;


    @RequestMapping(params = "inAndOut")
    @ResponseBody
    public Map countInAndOut(){
        Map map = new HashMap();
        Map temp = wmStockBaseStockService.findQuaGroupByType(30);
        map.putAll(temp);
        temp = wmStockBaseStockService.findQuaVar();
        map.putAll(temp);
        temp = wmStockBaseStockService.findQuaGroupByType();
        map.put("NOW_MATERIALS", temp.get("MATERIALS"));
        map.put("NOW_PRODUCT", temp.get("PRODUCT"));
        return map;
    }

    @RequestMapping(params = "bins")
    @ResponseBody
    public Map countBins(String storeCode){
        Map result = new HashMap();
        String[] legend = {"空闲", "占用", "停用"};
        long idel = 0;
        long unIdel = 0;
        long tingyong = 0;
        List<MdBinEntity> bins =  systemService.findHql("from MdBinEntity bin where bin.binStore=?", storeCode);
        for(MdBinEntity bin : bins){
            if("Y".equals(bin.getTingYong())){
                tingyong++;
            }else{
                List<MdPalletEntity> ps = systemService.findHql("from MdPalletEntity p where p.binBianMa=? and p.tuoPanZhuangTai=? and p.tingYong='N'", bin.getKuWeiBianMa(), PalletStatus.IN_SHELF);
                if(ps.size() > 0)
                    unIdel++;
                else
                    idel++;
            }
        }
        result.put("legend", legend);
        result.put("空闲", idel);
        result.put("占用", unIdel);
        result.put("停用", tingyong);
        result.put("sum", bins.size());
        return result;
    }

    @RequestMapping(params = "pallets")
    @ResponseBody
    public Map countPallets(){
        List<MdPalletEntity> pallets = systemService.getList(MdPalletEntity.class);
        Map result = new HashMap();
        String[] legend = {"空闲", "占用", "损坏", "停用"};
        long idel = 0;
        long unIdel = 0;
        long broken = 0;
        long tingyong = 0;
        for (MdPalletEntity p : pallets){
            if ("Y".equals(p.getTingYong())) {
                tingyong++;
            } else if (PalletStatus.IDLE.equals(p.getTuoPanZhuangTai())) {
                idel++;
            } else if (PalletStatus.BROKEN.equals(p.getTuoPanZhuangTai())) {
                broken++;
            } else {
                unIdel++;
            }
        }
        result.put("legend", legend);
        result.put("空闲", idel);
        result.put("占用", unIdel);
        result.put("损坏", broken);
        result.put("停用", tingyong);
        result.put("sum", pallets.size());
        return result;
    }


    @RequestMapping(params = "imAndOutTasks")
    @ResponseBody
    public Map imAndOutTasks(){
        Map result = new HashMap();
        String sql = "SELECT " +
                "SUM(1) AS sum, " +
                "SUM(tg.goods_qua) AS goodsQua " +
                "FROM wm_to_up_goods tg " +
                "WHERE tg.create_date > DATE_FORMAT(now(),'%Y-%m-%d')";
        String sql_out = "SELECT " +
                "SUM(1) AS sum, " +
                "SUM(tdg.base_goodscount) AS goodsQua " +
                "FROM wm_to_down_goods tdg " +
                "WHERE tdg.create_date > DATE_FORMAT(now(),'%Y-%m-%d')";
        Map temp = systemService.findOneForJdbc(sql);
        result.put("imPalletsCount", temp.get("sum")==null?0L:temp.get("sum"));
        result.put("imGoodsCount", temp.get("goodsQua")==null?0L:temp.get("goodsQua"));
        temp = systemService.findOneForJdbc(sql_out);
        result.put("outPalletsCount", temp.get("sum")==null?0L:temp.get("sum"));
        result.put("outGoodsCount", temp.get("goodsQua")==null?0L:temp.get("goodsQua"));
        return result;
    }

    @RequestMapping(params = "allKuWei")
    @ResponseBody
    public Object allKuWei(String code){
        List<MdBinEntity> bins = systemService.findByProperty(MdBinEntity.class,"binStore",code);
        return bins;
    }
}
