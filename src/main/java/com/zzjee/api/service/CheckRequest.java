package com.zzjee.api.service;

import com.zzjee.api.utils.ResultObject;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.stereotype.Service;

@Service("checkRequest")
public class CheckRequest {

    public ResultObject checkProductOutOrder(String orderNo, String productId, String productName, String outDate,
                                             String outQuantity, String outWeight, String QCstaff){
        if(StringUtil.isEmpty(orderNo) || StringUtil.isEmpty(productId) || StringUtil.isEmpty(outWeight))
            return ResultObject.error("请检查参数，有的参数不能为空");
        return ResultObject.success();
    }
}
