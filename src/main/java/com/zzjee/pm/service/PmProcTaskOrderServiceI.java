package com.zzjee.pm.service;
import com.zzjee.pm.entity.PmProcTaskOrderEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface PmProcTaskOrderServiceI extends CommonService{
	
 	public void delete(PmProcTaskOrderEntity entity) throws Exception;
 	
 	public Serializable save(PmProcTaskOrderEntity entity) throws Exception;
 	
 	public void saveOrUpdate(PmProcTaskOrderEntity entity) throws Exception;
 	
}
