package org.jeecgframework.test.data;

import com.zzjee.md.entity.MdGoodsEntity;
import com.zzjee.pm.entity.PmBasicMaterialEntity;
import com.zzjee.wm.page.WmNoticeImpPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class PapreData2 {

    @Test
    public void testMdGoods() throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(2);
        params.setHeadRows(1);
        params.setNeedSave(true);
        //params.setKeyIndex(4);

        File file = new File("E:/PHY-work/正邦智慧工厂/wms模板/商品信息.xls");
        FileInputStream inputStream = new FileInputStream(file);

        List<MdGoodsEntity> list = ExcelImportUtil.importExcel(inputStream, MdGoodsEntity.class, params);
    }

    @Test
    public void testNoticeH() throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(2);
        params.setHeadRows(1);
        params.setNeedSave(true);
        params.setKeyIndex(6);

        File file = new File("E:/PHY-work/正邦智慧工厂/wms模板/进货通知.xls");
        FileInputStream inputStream = new FileInputStream(file);

        List<WmNoticeImpPage> list = ExcelImportUtil.importExcel(inputStream, WmNoticeImpPage.class, params);
    }
}
