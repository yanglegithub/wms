package com.zzjee.flow.service;

import com.zzjee.md.entity.MdMovePalletEntity;
import com.zzjee.wm.entity.WmImNoticeHEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.util.List;

public interface PalletUpServiceI extends CommonService {

    public WmImNoticeHEntity findImhByImi(String id);

    public Long countTask();

    public List<MdMovePalletEntity> findUpTask();
}
