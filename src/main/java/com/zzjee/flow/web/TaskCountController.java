package com.zzjee.flow.web;

import com.zzjee.flow.service.PalletUpServiceI;
import com.zzjee.flow.util.BpmStatus;
import com.zzjee.flow.util.ExceptionType;
import com.zzjee.flow.util.TaskStatus;
import com.zzjee.md.entity.MdMovePalletEntity;
import com.zzjee.wm.entity.*;
import com.zzjee.wm.page.WmOmNoticeHPage;
import com.zzjee.wm.service.WmImNoticeHServiceI;
import com.zzjee.wmutil.WmsContants;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.sms.util.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/flow/taskCount")
public class TaskCountController {
    @Resource
    SystemService systemService;
    @Resource
    PalletUpServiceI palletUpService;
    @Resource
    private WmImNoticeHServiceI wmImNoticeIService;


    @RequestMapping(params = "countMaintain")
    @ResponseBody
    public AjaxJson countMaintain(){
        AjaxJson ajaxJson = new AjaxJson();
        Map<String, Object> result = new HashMap();
        String sql = "SELECT type as 'type',COUNT(id) as 'count' from md_exception me where me.status=? group by me.type";
        List<Map<String, Object>> list = systemService.findForJdbc(sql,TaskStatus.INIT);
        for (Map map : list){
            result.put((String) map.get("type"),map.get("count"));
        }
        result.put(ExceptionType.EXP_BARCODE, result.get(ExceptionType.EXP_BARCODE) == null?0:result.get(ExceptionType.EXP_BARCODE));
        result.put(ExceptionType.EXP_BIN, result.get(ExceptionType.EXP_BIN) == null?0:result.get(ExceptionType.EXP_BIN));
        result.put(ExceptionType.EXP_PALLET, result.get(ExceptionType.EXP_PALLET) == null?0:result.get(ExceptionType.EXP_PALLET));

        //List<WmOmNoticeHEntity> hs = systemService.findByProperty(WmOmNoticeHEntity.class, "omSta", Constants.wm_sta1);
        String omhhql = "from WmOmNoticeHEntity h where h.omSta=? and h.orderTypeCode=?";
        List<WmOmNoticeHEntity> hs = systemService.findHql(omhhql, Constants.wm_sta1, WmsContants.OUT_PRODUCT);
        result.put("platTaskCount",hs==null?0:hs.size());
        ajaxJson.setObj(result);
        return ajaxJson;
    }

    @RequestMapping(params = "countForklift")
    @ResponseBody
    public AjaxJson countDriver(){
        AjaxJson ajaxJson = new AjaxJson();
        HttpSession session = ContextHolderUtils.getSession();
        TSUser user = ResourceUtil.getSessionUserName();
        Map returnMap = new HashMap();

        List<MdMovePalletEntity> count = palletUpService.findUpTask();
        returnMap.put("upCount",count==null?0:count.size());

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
        returnMap.put("downCount",sbuf.size());

        String hql = "select count(id) from Wm_Im_Notice_I wm where wm.bin_Pre='N' and wm.bpm_sta=? ";
        Long emptyCount = systemService.getCountForJdbcParam(hql, new Object[]{BpmStatus.MOVING});
        returnMap.put("emptyCount",emptyCount == null?0:emptyCount);

        List<WmToMoveGoodsEntity> moves = systemService.findByProperty(WmToMoveGoodsEntity.class, "moveSta", TaskStatus.INIT);
        returnMap.put("moveCount",moves == null?0:moves.size());

        ajaxJson.setObj(returnMap);
        return ajaxJson;
    }

    @RequestMapping(params = "countUnStore")
    @ResponseBody
    public AjaxJson countUnStore(){
        AjaxJson ajaxJson = new AjaxJson();
        Map<String, Object> result = new HashMap();
        Map<String, Object> returnDate = new HashMap();
        String hql = "select omh.order_type_code as 'type',count(mp.id) as 'count' from Md_Move_Pallet mp " +
                "left join wm_om_notice_i omi on omi.id=mp.trigger_source " +
                "left join wm_om_notice_h omh on omi.om_notice_id=omh.om_notice_id " +
                "where mp.type='拆垛' and mp.status=?" +
                "group by omh.order_type_code";
//        long count = systemService.getCountForJdbcParam(hql,new Object[]{TaskStatus.INIT});
        List<Map<String, Object>> list = systemService.findForJdbc(hql, TaskStatus.INIT);
        for (Map map : list){
            result.put((String)map.get("type"),map.get("count"));
        }
        returnDate.put("02", result.get("02") == null?0:result.get("02"));
        returnDate.put("21", result.get("21") == null?0:result.get("21"));
        ajaxJson.setObj(returnDate);
        return ajaxJson;
    }

    @RequestMapping(params = "countStore")
    @ResponseBody
    public AjaxJson countStore(){
        AjaxJson ajaxJson = new AjaxJson();
        String hql = "from WmImNoticeIEntity wm where wm.binPre='N' and exists( from WmImNoticeHEntity h where h.noticeId=wm.imNoticeId and h.orderTypeCode=? )";
        List<WmImNoticeIEntity> mlist = wmImNoticeIService.findHql(hql,"01");
        List<WmImNoticeIEntity> plist = wmImNoticeIService.findHql(hql,"20");

        Map map = new HashMap();
        map.put("materialsCount",mlist.size());
        map.put("productsCount",plist.size());
        ajaxJson.setObj(map);
        return ajaxJson;
    }
}
