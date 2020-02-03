package com.zzjee.flow.util;

public class ExceptionType {
    //条码故障
    public static final String EXP_BARCODE = "EXP_BARCODE";
    //货位故障
    public static final String EXP_BIN = "EXP_BIN";
    //托盘故障
    public static final String EXP_PALLET = "EXP_PALLET";

    public static final String DETAIL_QUANTITY = "数据不一致";
    public static final String DETAIL_TYPE = "种类不一致";
    public static final String DETAIL_TYPE_QUANTITY = "数据种类不一致";
    public static final String DETAIL_CODEBROKEN = "条码破损";
    public static final String DETAIL_CODENONE = "查无此盘";
    public static final String DETAIL_CODEBUSY = "托盘占用";
}
