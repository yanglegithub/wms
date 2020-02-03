package com.zzjee.md.service;
import com.zzjee.md.entity.MdOtherEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface MdOtherServiceI extends CommonService{
	
 	public void delete(MdOtherEntity entity) throws Exception;
 	
 	public Serializable save(MdOtherEntity entity) throws Exception;
 	
 	public void saveOrUpdate(MdOtherEntity entity) throws Exception;
 	
}
