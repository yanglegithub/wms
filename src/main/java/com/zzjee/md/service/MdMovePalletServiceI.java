package com.zzjee.md.service;
import com.zzjee.md.entity.MdMovePalletEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface MdMovePalletServiceI extends CommonService{
	
 	public void delete(MdMovePalletEntity entity) throws Exception;
 	
 	public Serializable save(MdMovePalletEntity entity) throws Exception;
 	
 	public void saveOrUpdate(MdMovePalletEntity entity) throws Exception;
 	
}
