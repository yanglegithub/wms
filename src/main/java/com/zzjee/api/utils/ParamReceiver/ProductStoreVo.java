package com.zzjee.api.utils.ParamReceiver;

import com.zzjee.wm.entity.WmImNoticeHEntity;
import com.zzjee.wm.entity.WmImNoticeIEntity;
import com.zzjee.wmutil.WmsContants;
import com.zzjee.wmutil.wmUtil;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;

import java.util.Date;
import java.util.UUID;

public class ProductStoreVo {
    /**产品入库单号*/
    private String orderNo;
    /**产品唯一编码*/
    private String productionId;
    /**产品名称*/
    private String productionName;
    /**产品批号编码*/
    private String batchNo;
    /**生产日期(格式：yyyy-MM-dd)*/
    private Date manufactureDate;
    /**保质期(单位：天)*/
    private String warranty;
    /**入库日期(格式：yyyy-MM-dd)*/
    private Date inDate;
    /**入库数量*/
    private String inQuantity;
    /**入库总质量（单位：kg）*/
    private String inWeight;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getProductionId() {
        return productionId;
    }

    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }

    public String getProductionName() {
        return productionName;
    }

    public void setProductionName(String productionName) {
        this.productionName = productionName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Date getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public String getInQuantity() {
        return inQuantity;
    }

    public void setInQuantity(String inQuantity) {
        this.inQuantity = inQuantity;
    }

    public String getInWeight() {
        return inWeight;
    }

    public void setInWeight(String inWeight) {
        this.inWeight = inWeight;
    }

    public String generateOrderNo(){
        if(StringUtil.isEmpty(this.orderNo)){
            this.orderNo = wmUtil.getNextNoticeid(WmsContants.IN_PRODUCT);
            return this.orderNo;
        } else {
            return this.orderNo;
        }
    }

    public WmImNoticeHEntity generateImNoticeH(){
        WmImNoticeHEntity hEntity = new WmImNoticeHEntity();
        hEntity.setNoticeId(generateOrderNo());
        hEntity.setImData(this.inDate);
        hEntity.setOrderTypeCode(WmsContants.IN_PRODUCT);
        if(ResourceUtil.getConfigByName("wms.stock_io_confirm").equals("true"))
            hEntity.setImSta(WmsContants.CONFIRMING);
        else
            hEntity.setImSta(WmsContants.CONFIRMED);
        return hEntity;
    }

    public WmImNoticeIEntity generateImNoticeI(){
        WmImNoticeIEntity iEntity = new WmImNoticeIEntity();
        iEntity.setImNoticeId(generateOrderNo());
        iEntity.setGoodsCode(this.productionId);
        iEntity.setGoodsName(this.productionName);
        iEntity.setGoodsCount(this.inQuantity);
        iEntity.setGoodsQmCount("0");
        iEntity.setGoodsWqmCount(this.inQuantity);
        iEntity.setGoodsPrdData(this.manufactureDate);
        iEntity.setGoodsBatch(this.batchNo);
        iEntity.setGoodsStoDate(this.inDate);
        iEntity.setBinPre("N");
        iEntity.setGoodsWeight(this.inWeight);
        iEntity.setGoodsUnit("KG");
        iEntity.setShpGuiGe(String.valueOf(Double.valueOf(this.inWeight)/Double.valueOf(this.inQuantity)));
        return iEntity;
    }
}
