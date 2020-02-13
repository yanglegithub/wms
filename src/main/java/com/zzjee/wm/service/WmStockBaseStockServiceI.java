package com.zzjee.wm.service;
import com.zzjee.wm.entity.WmStockBaseStockEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface WmStockBaseStockServiceI extends CommonService{

 	public void delete(WmStockBaseStockEntity entity) throws Exception;

 	public Serializable save(WmStockBaseStockEntity entity) throws Exception;

 	public void saveOrUpdate(WmStockBaseStockEntity entity) throws Exception;

 	public Map<String, Object> findQuaGroupByType();

	public Map<String, Object> findQuaGroupByType(int daysAgo);

	public Map<String, Object> findQuaVar();

	public long findQuaByType(String code);
 }
