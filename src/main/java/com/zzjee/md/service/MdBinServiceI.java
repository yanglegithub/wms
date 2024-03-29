package com.zzjee.md.service;
import com.zzjee.md.entity.MdBinEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;
import java.util.Map;

public interface MdBinServiceI extends CommonService{

 	public void delete(MdBinEntity entity) throws Exception;

 	public Serializable save(MdBinEntity entity) throws Exception;

 	public void saveOrUpdate(MdBinEntity entity) throws Exception;

 	public Map<String, Object> findBinsStatus();

 	public Map<String, Object> findBinsStatus(String code);
}
