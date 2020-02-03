package com.zzjee.api.service;

import com.zzjee.api.utils.NCDataObject;
import com.zzjee.wm.entity.WmOmNoticeIEntity;
import com.zzjee.wm.entity.WmOmTaskEntity;
import com.zzjee.wm.page.WmImNoticeHPage;
import okhttp3.OkHttpClient;
import org.jeecgframework.core.common.service.CommonService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface StockInfoServiceI extends CommonService {
    OkHttpClient okHttpClient=null;

    public List<WmOmNoticeIEntity> generateOmIEntitys(WmOmTaskEntity task);

    public String generateOrderNo();

    public String getToken() throws IOException;

    public OkHttpClient getOkHttpClient();

    public NCDataObject requestMethode(String funCode, String cType, Map<String, Object> params) throws IOException;

    public NCDataObject requestMethode(String funCode, String cType, String jsonParams) throws IOException;

    public String findImNotice(String imKey, String imDate) throws IOException;

    public List<WmImNoticeHPage> findImNoticeFromNC(String imKey, String imDate) throws IOException;

    public NCDataObject pushInStorage() throws IOException;

    public NCDataObject pushProductTask() throws IOException;

    public boolean outConfirm(String outKey) throws IOException;

    public NCDataObject realImUpload(String imKey) throws IOException;
}
