package com.zzjee.flow.service.impl;

import com.zzjee.flow.service.PalletUpServiceI;
import com.zzjee.flow.util.TaskStatus;
import com.zzjee.md.entity.MdMovePalletEntity;
import com.zzjee.md.entity.PalletStatus;
import com.zzjee.wm.entity.WmImNoticeHEntity;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.web.system.sms.util.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("palletUpService")
@Transactional
public class PalletUpServiceImpl extends CommonServiceImpl implements PalletUpServiceI {
    @Override
    public WmImNoticeHEntity findImhByImi(String id) {
        String hql = "from WmImNoticeHEntity wm where exists( from WmImNoticeIEntity wmi where wmi.id=? and wmi.imNoticeId=wm.noticeId )";
        List<WmImNoticeHEntity> wmhs = this.findHql(hql,id);
        if(wmhs.size() <= 0)
            return null;
        else{
            return wmhs.get(0);
        }
    }

    @Override
    public Long countTask() {
//        String hql = "from WmImNoticeHEntity wm where exists( from MdMovePalletEntity mp,WmImNoticeIEntity wmi where mp.triggerSource=wmi.id and wmi.imNoticeId=wm.noticeId )";
        String sql = "SELECT count(*) FROM wm_im_notice_h wm WHERE EXISTS( SELECT * FROM md_move_pallet mp, wm_im_notice_i wmi WHERE mp.STATUS = ? AND mp.type='上架' and mp.trigger_source = wmi.id AND wmi.im_notice_id = wm.notice_id )";
        return this.getCountForJdbcParam(sql,new Object[]{TaskStatus.INIT});
    }

    @Override
    public List<MdMovePalletEntity> findUpTask() {
        String hql = "from MdMovePalletEntity mp where mp.type='上架' and mp.status=?";
        List<MdMovePalletEntity> pallets = this.findHql(hql, TaskStatus.INIT);
        return pallets;
    }
}
