package com.zzjee.wmutil;

/**
 * wms常量类：含有各种状态常量;
 */
public class WmsContants {
    /**
     * 入库单状态
     */
    public static final String WM_IM_H_INIT = "初始化";
    public static final String WM_IM_H_PLANNING = "计划中";
    public static final String WM_IM_H_OPERATION = "操作中";
    public static final String WM_IM_H_DELETED = "已删除";
    public static final String WM_IM_H_FINISH = "已完成";

    public static final String WM_IM_I_PLANNING = "计划中";
    public static final String WM_IM_I_DOWN = "卸货中";
    public static final String WM_IM_I_END = "已完成";

    /**
     * 出入库单状态
     */
    public static final String CONFIRMING = "待确认";
    public static final String CONFIRMED = "已确认";
    public static final String IN_STOCK = "入库中";
    public static final String IN_STOCKED = "已入库";
    public static final String OUT_STOCK = "出库中";
    public static final String OUT_STOCKED = "已出库";
    public static final String DISCARD = "已废弃";
    public static final String DELETED = "已删除";
    public static final String FINISHED = "已完成";

    /**
     * 订单类型
     */
    public static final String IN_MATERIALS = "01"; //原料入库
    public static final String OUT_MATERIALS = "02"; //原料出库
    public static final String IN_PRODUCT = "20";  //产成品入库
    public static final String OUT_PRODUCT = "21"; //产成品出库
    public static final String IN_OTHER = "09"; //其它入库
    public static final String ACROSS = "04";  //越库通知
}
