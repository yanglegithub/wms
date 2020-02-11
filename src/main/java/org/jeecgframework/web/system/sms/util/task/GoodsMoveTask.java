package org.jeecgframework.web.system.sms.util.task;

import com.zzjee.md.entity.*;
import com.zzjee.mvyj.entity.MvStockYjEntity;
import com.zzjee.wm.entity.*;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.sms.service.TSSmsServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.xiaoleilu.hutool.date.DateTime.now;

/**
 *
 * @ClassName:SmsSendTask 所有信息的发送定时任务类
 * @Description: TODO
 * @date 2014-11-13 下午5:06:34
 *
 */
@Service("goodsMoveTask")
public class GoodsMoveTask {


	@Autowired
	private SystemService systemService;

	/* @Scheduled(cron="0 0 01 * * ?") */
	public void run() {
		/*long start = System.currentTimeMillis();
		String datestr = DateUtils.date2Str(DateUtils.date_sdf);
		org.jeecgframework.core.util.LogUtil
				.info("===================转移定时任务开始===================");
		String moveStats = ResourceUtil.getConfigByName("moveStats");
		String binStoress = ResourceUtil.getConfigByName("binStoress");

		if(StringUtil.isEmpty(moveStats)){
			moveStats = "计划中";
		}
		if(StringUtil.isNotEmpty(binStoress)){
			String binStoressa[] = binStoress.split(",");
			for(String binstore:binStoressa){
				this.goodsMove(binstore,moveStats);
			}
		}



		org.jeecgframework.core.util.LogUtil
				.info("===================转移定时任务结束===================");
		long end = System.currentTimeMillis();
		long times = end - start;
		org.jeecgframework.core.util.LogUtil.info("转移定时任务总耗时" + times + "毫秒");*/

		String warnDays = ResourceUtil.getConfigByName("warningDays");
		List<WmStockBaseStockEntity> stocks = systemService.loadAll(WmStockBaseStockEntity.class);
		Calendar now = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for (WmStockBaseStockEntity stock : stocks){
			MvStockYjEntity mvstock = systemService.findUniqueByProperty(MvStockYjEntity.class, "kuWeiBianMa", stock.getKuWeiBianMa());
			MdGoodsEntity good = systemService.findUniqueByProperty(MdGoodsEntity.class, "shpBianMa", stock.getGoodsId());
			MdBinEntity bin = systemService.findUniqueByProperty(MdBinEntity.class, "kuWeiBianMa", stock.getKuWeiBianMa());
			Map palletMap = systemService.findOneForJdbc("select GROUP_CONCAT(mp.tuo_pan_bian_ma) as codes from md_pallet mp where mp.bin_Bian_Ma=?", stock.getKuWeiBianMa());
			Calendar warnDate = null;
			Calendar expireDate = null;
			try {
				warnDate = DateUtils.parseCalendar(stock.getGoodsProData(), "yyyy-MM-dd");
				expireDate = DateUtils.parseCalendar(stock.getGoodsProData(), "yyyy-MM-dd");
				warnDate.add(Calendar.DAY_OF_YEAR, Integer.parseInt(stock.getGoodsBzhiqi())-Integer.parseInt(warnDays));
				expireDate.add(Calendar.DAY_OF_YEAR, Integer.parseInt(stock.getGoodsBzhiqi()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(mvstock == null){
				mvstock = new MvStockYjEntity();
				mvstock.setKuctype("库存");
				mvstock.setWarnType(warnDate.after(now)?"正常":(expireDate.after(now)?"预警":"过期"));
				mvstock.setBaseGoodscount(Integer.parseInt(stock.getGoodsQua()));
				mvstock.setBaseUnit(good.getShlDanWei());
				mvstock.setKuWeiBianMa(stock.getKuWeiBianMa());
				mvstock.setBinId((String) palletMap.get("codes"));
				mvstock.setCusCode(null);
				mvstock.setZhongWenQch(null);
				mvstock.setGoodsId(good.getShpBianMa());
				mvstock.setShpMingCheng(good.getShpMingCheng());
				mvstock.setGoodsProData(stock.getGoodsProData());
				mvstock.setBzhiQi(stock.getGoodsBzhiqi());
				mvstock.setDqr(DateUtils.formatDate(expireDate, "yyyy-MM-dd"));
				mvstock.setShangJiaCiXu(bin.getShangJiaCiXu());
				mvstock.setQuHuoCiXu(bin.getQuHuoCiXu());
				mvstock.setResDate(String.valueOf(expireDate.get(Calendar.DATE) - now.get(Calendar.DATE)));
				systemService.save(mvstock);
			}else{
				mvstock.setWarnType(warnDate.after(now)?"正常":(expireDate.after(now)?"预警":"过期"));
				mvstock.setBaseGoodscount(Integer.parseInt(stock.getGoodsQua()));
				mvstock.setBinId((String) palletMap.get("codes"));
				mvstock.setDqr(DateUtils.formatDate(expireDate, "yyyy-MM-dd"));
				mvstock.setResDate(String.valueOf(expireDate.get(Calendar.DATE) - now.get(Calendar.DATE)));
				systemService.saveOrUpdate(mvstock);
			}
		}
	}
	public  void goodsMove(String binstrore,String moveStatus ){
	    //转移到B
		String tsql = "SELECT id FROM wv_stock_stt " +
                "where yushoutianshu <> bzhi_qi  " +
                "and bzhi_qi > 0 " +
                "and  bin_id = 'A' " +
                "and to_days(`goods_pro_data` + interval  (bzhi_qi - yushoutianshu) day) < to_days(now())  " +
                "and to_days(`goods_pro_data` + interval  (bzhi_qi ) day) > to_days(now())";
		this.genGoodsMove(tsql,"B","",moveStatus);

		//转移到C
        tsql = "SELECT id FROM wv_stock_stt " +
                "where yushoutianshu <>  bzhi_qi   " +
                "and bzhi_qi > 0 " +
                "and   bin_id in ('A','B') " +
                "and to_days(`goods_pro_data` + interval  (bzhi_qi ) day) <= to_days(now())";
        this.genGoodsMove(tsql,"C","",moveStatus);


		//转移到DB
        tsql = "SELECT id FROM wv_stock_stt " +
				"where yushoutianshu <> bzhi_qi  " +
				"and bzhi_qi > 0 " +
				"and  bin_id = 'DA' " +
				"and to_days(`goods_pro_data` + interval  (bzhi_qi - yushoutianshu) day) < to_days(now())  " +
				"and to_days(`goods_pro_data` + interval  (bzhi_qi ) day) > to_days(now())";
		this.genGoodsMove(tsql,"DB","",moveStatus);


		//转移到DC
		tsql = "SELECT id FROM wv_stock_stt " +
				"where yushoutianshu <>  bzhi_qi   " +
				"and bzhi_qi > 0 " +
				"and   bin_id in ('DA','DB') " +
				"and to_days(`goods_pro_data` + interval  (bzhi_qi ) day) <= to_days(now())";
		this.genGoodsMove(tsql,"DC","",moveStatus);

	}

	private void  genGoodsMove(String Tsql,String TinId,String binstrore,String moveStatus  ){
              List<Map<String, Object>> resulmovea = systemService
                .findForJdbc(Tsql);
        //生成任务转B
        for (int i = 0; i < resulmovea.size(); i++) {
        	try{

            WvStockEntity t = systemService.get(WvStockEntity.class,resulmovea.get(i).get("id").toString());

                WmToMoveGoodsEntity wmtomove = new WmToMoveGoodsEntity();
				wmtomove.setCreateDate(now());
				wmtomove.setCreateBy("system");
                wmtomove.setOrderTypeCode("TPZY");
                wmtomove.setBinFrom(t.getKuWeiBianMa());
                wmtomove.setBinTo(t.getKuWeiBianMa());
                wmtomove.setCusCode(t.getCusCode());
                wmtomove.setCusName(t.getZhongWenQch());
                wmtomove.setToCusCode(t.getCusCode());
                wmtomove.setToCusName(t.getZhongWenQch());
                wmtomove.setGoodsId(t.getGoodsId());
                wmtomove.setGoodsName(t.getShpMingCheng());
                wmtomove.setGoodsProData(t.getGoodsProData());
                wmtomove.setGoodsQua(t.getGoodsQua().toString());
                wmtomove.setGoodsUnit(t.getGoodsUnit());
                wmtomove.setBaseGoodscount(t.getGoodsQua().toString());
                wmtomove.setBaseUnit(t.getGoodsUnit());
                wmtomove.setMoveSta(moveStatus);
                wmtomove.setTinFrom(t.getBinId());
                wmtomove.setTinId(TinId);
                systemService.save(wmtomove);
            }catch (Exception e){
            }
        }

    }
}
