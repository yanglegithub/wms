package com.zzjee.flow.service;

import com.zzjee.md.entity.MdBinEntity;
import com.zzjee.md.entity.MdPalletEntity;
import com.zzjee.wm.entity.WmImNoticeHEntity;
import com.zzjee.wm.entity.WmImNoticeIEntity;
import com.zzjee.wm.entity.WmOmNoticeHEntity;
import com.zzjee.wm.entity.WmOmNoticeIEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.util.List;
import java.util.Map;

public interface StoreAssignmentServiceI extends CommonService {

    public MdBinEntity assignmentForPallet(MdPalletEntity palletEntity, String type);

    public List<MdPalletEntity> getPalletsForMdBin(String id);

    public boolean checkStore(WmImNoticeHEntity h, List<WmImNoticeIEntity> is, String storeCode);

    public List<WmOmNoticeIEntity> checkOmStock(WmOmNoticeHEntity h, List<WmOmNoticeIEntity> is);

    public Map<WmOmNoticeIEntity, List<MdPalletEntity>> assignmentPalletForOmOrder(String omNoticeId);
}
