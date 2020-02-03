package com.zzjee.md.entity;

public class PalletStatus {
    /**
     * 托盘空闲
     */
    public static final String IDLE = "空闲";
    /**
     * 托盘损坏
     */
    public static final String BROKEN = "损坏";
    /**
     * 托盘运输中,移库中
     */
    public static final String IN_TRANSIT = "运输中";
    /**
     * 托盘上架中
     */
    public static final String IN_UP = "上架中";
    /**
     * 托盘下架中
     */
    public static final String IN_DOWN = "下架中";

    /**
     * 托盘码垛中
     */
    public static final String IN_STOCK = "码垛中";
    /**
     * 托盘卸货中
     */
    public static final String IN_UNLOAD = "卸货中";

    /**
     * 托盘在货架中，处于等待状态
     */
    public static final String IN_SHELF = "货架中";
}
