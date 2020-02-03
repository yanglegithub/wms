package com.zzjee.md.service;
import com.zzjee.md.entity.MdPalletEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface MdPalletServiceI extends CommonService{
	
 	public void delete(MdPalletEntity entity) throws Exception;
 	
 	public Serializable save(MdPalletEntity entity) throws Exception;
 	
 	public void saveOrUpdate(MdPalletEntity entity) throws Exception;
 	
}
