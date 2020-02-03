package com.zzjee.pm.service;
import com.zzjee.pm.entity.PmProcTaskEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface PmProcTaskServiceI extends CommonService{
	
 	public void delete(PmProcTaskEntity entity) throws Exception;
 	
 	public Serializable save(PmProcTaskEntity entity) throws Exception;
 	
 	public void saveOrUpdate(PmProcTaskEntity entity) throws Exception;
 	
}
