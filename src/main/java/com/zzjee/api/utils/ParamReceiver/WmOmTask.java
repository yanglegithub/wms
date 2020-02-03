package com.zzjee.api.utils.ParamReceiver;

import java.util.List;

public class WmOmTask {
    private String erp_baseid;
    private String qcstaff;
    private String orderKey;
    private String outDate;
    private List<WmOmTaskDetail> list;

    public String getErp_baseid() {
        return erp_baseid;
    }

    public void setErp_baseid(String erp_baseid) {
        this.erp_baseid = erp_baseid;
    }

    public String getQcstaff() {
        return qcstaff;
    }

    public void setQcstaff(String qcstaff) {
        this.qcstaff = qcstaff;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public String getOutDate() {
        return outDate;
    }

    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }

    public List<WmOmTaskDetail> getList() {
        return list;
    }

    public void setList(List<WmOmTaskDetail> list) {
        this.list = list;
    }
}
