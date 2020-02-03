package com.zzjee.pm.service;
import com.zzjee.pm.entity.PmBasicMaterialEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface PmBasicMaterialServiceI extends CommonService{
	
 	public void delete(PmBasicMaterialEntity entity) throws Exception;
 	
 	public Serializable save(PmBasicMaterialEntity entity) throws Exception;
 	
 	public void saveOrUpdate(PmBasicMaterialEntity entity) throws Exception;
 	
}
