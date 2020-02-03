package com.zzjee.pm.service;
import com.zzjee.pm.entity.PmMaterialListEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface PmMaterialListServiceI extends CommonService{
	
 	public void delete(PmMaterialListEntity entity) throws Exception;
 	
 	public Serializable save(PmMaterialListEntity entity) throws Exception;
 	
 	public void saveOrUpdate(PmMaterialListEntity entity) throws Exception;
 	
}
