package org.jeecgframework.web.system.sms.util.task;

import com.zzjee.api.service.StockInfoServiceI;
import com.zzjee.api.utils.NCDataObject;
import com.zzjee.md.entity.MdGoodsEntity;
import com.zzjee.pm.entity.PmBasicMaterialEntity;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service("goodsInfoTask")
public class GoodsInfoTask {

    @Autowired
    private SystemService systemService;
    @Autowired
    private StockInfoServiceI stockInfoService;

    //每周星期天凌晨1点触发
    /* @Scheduled(cron="0 0 1 ? * L") */
    public void run() {
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
                //PmBasicMaterialEntity temp = systemService.findUniqueByProperty(PmBasicMaterialEntity.class,"ncPkid",id);
                MdGoodsEntity good = systemService.findUniqueByProperty(MdGoodsEntity.class,"ncPkid",id);
                if(good != null){
                    /*temp.setStuffCode((String) map.get("invcode"));
                    temp.setStuffName((String) map.get("invname"));
                    temp.setStuffType((String) map.get("property"));
                    temp.setRemarks((String) map.get("measname"));
                    systemService.saveOrUpdate(temp);*/
                    good.setShpBianMa((String) map.get("invcode"));
                    good.setShpMingCheng((String) map.get("invname"));
                    good.setChpShuXing((String) map.get("property"));
                    good.setShlDanWei((String) map.get("measname"));
                    good.setJshDanWei((String) map.get("measname"));
                }else{
                    /*temp = new PmBasicMaterialEntity();
                    temp.setNcPkid(id);
                    temp.setStuffCode((String) map.get("invcode"));
                    temp.setStuffName((String) map.get("invname"));
                    temp.setStuffType((String) map.get("property"));
                    temp.setRemarks((String) map.get("measname"));
                    systemService.save(temp);*/
                    good.setNcPkid(id);
                    good.setShpMingCheng((String) map.get("invname"));
                    good.setShpJianCheng((String) map.get("invname"));
                    good.setShpBianMa((String) map.get("invcode"));
                    good.setChpShuXing((String) map.get("property"));
                    good.setCfWenCeng("常温");

                    good.setShlDanWei((String) map.get("measname"));
                    good.setJshDanWei((String) map.get("measname"));
                    good.setZhuangTai("I");
                }
            }
        }
    }
}
