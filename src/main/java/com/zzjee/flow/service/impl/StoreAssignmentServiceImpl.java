package com.zzjee.flow.service.impl;

import com.zzjee.flow.service.StoreAssignmentServiceI;
import com.zzjee.md.entity.MdBinEntity;
import com.zzjee.md.entity.MdGoodsEntity;
import com.zzjee.md.entity.MdPalletEntity;
import com.zzjee.md.entity.PalletStatus;
import com.zzjee.wm.entity.WmImNoticeHEntity;
import com.zzjee.wm.entity.WmImNoticeIEntity;
import com.zzjee.wm.entity.WmOmNoticeHEntity;
import com.zzjee.wm.entity.WmOmNoticeIEntity;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("storeAssignmentService")
@Transactional
public class StoreAssignmentServiceImpl extends CommonServiceImpl implements StoreAssignmentServiceI {
    @Override
    public MdBinEntity assignmentForPallet(MdPalletEntity palletEntity, String type) {
        String hql = "from MdPalletEntity p where p.tuoPanZhuangTai in (?,?) and p.entryKey=?";
        List<MdPalletEntity> palletlist = this.findHql(hql, PalletStatus.IN_SHELF,PalletStatus.IN_UP,palletEntity.getEntryKey());
        Map<String,List<MdPalletEntity>> map = new HashMap<>();
        for (MdPalletEntity pallet : palletlist){
            if(!map.containsKey(pallet.getBinBianMa())){
                List<MdPalletEntity> palletTemp = new ArrayList<>();
                palletTemp.add(pallet);
                map.put(pallet.getBinBianMa(),palletTemp);
            }else{
                map.get(pallet.getBinBianMa()).add(pallet);
            }
        }
        for(Map.Entry<String,List<MdPalletEntity>> entry : map.entrySet()){
            MdBinEntity bintemp = findUniqueByProperty(MdBinEntity.class,"kuWeiBianMa",entry.getKey());
            if(Long.parseLong(bintemp.getZuiDaTuoPan())>entry.getValue().size())
                return bintemp;
        }

        String hql1 = "from MdBinEntity bin where not exists( from MdPalletEntity p where p.binBianMa=bin.kuWeiBianMa ) and bin.binStore=? and bin.kuWeiLeiXing = ?";
        List<MdBinEntity> bins = this.findHql(hql1, type, "正常储位");
        if(bins == null || bins.size() <= 0)
            return null;
        else
            return bins.get(0);

    }

    @Override
    public List<MdPalletEntity> getPalletsForMdBin(String id) {
        String hql = "from MdPalletEntity p where p.tuoPanZhuangTai=? and p.binBianMa=?";
        MdBinEntity bin = this.getEntity(MdBinEntity.class,id);
        List<MdPalletEntity> pallets = this.findHql(hql,PalletStatus.IN_SHELF,bin.getKuWeiBianMa());
        return pallets;
    }

    @Override
    public boolean checkStore(WmImNoticeHEntity h, List<WmImNoticeIEntity> is, String storeCode) {
        /*WmImNoticeHEntity imh = this.findUniqueByProperty(WmImNoticeHEntity.class, "noticeId", imNoticeId);
        List<WmImNoticeIEntity> imis = this.findByProperty(WmImNoticeIEntity.class, "imNoticeId",imNoticeId);*/
        if(h == null || is == null)
            return false;
        WmImNoticeHEntity imh = h;
        List<WmImNoticeIEntity> imis = is;

        long stores = 0;
        String storeslist = "";
        String binhql = " select distinct b from MdBinEntity b left join WmStockBaseStockEntity s on s.kuWeiBianMa=b.kuWeiBianMa where s.goodsQua <= 0 and b.binStore=? and b.tingYong='N' and b.kuWeiLeiXing='正常储位' order by b.shangJiaCiXu ";
        List<MdBinEntity> bins = this.findHql(binhql,storeCode);
        for (WmImNoticeIEntity i : imis) {
            MdGoodsEntity good = this.findUniqueByProperty(MdGoodsEntity.class, "shpBianMa", i.getGoodsCode());
            long evey = Long.parseLong(good.getMpDanCeng()) * Long.parseLong(good.getMpCengGao());
            long tuopans = (long)Math.ceil(Double.parseDouble(i.getGoodsCount())/evey);
            long all = 0;
            for(MdBinEntity bin : bins){
                if(storeslist.matches("\\b"+bin.getKuWeiBianMa()+"\\b"))
                    continue;
                storeslist += storeslist.equals("")?bin.getKuWeiBianMa():(","+bin.getKuWeiBianMa());
                all += Long.parseLong(bin.getZuiDaTuoPan());
                if(all >= tuopans)
                    break;
            }
            if(all < tuopans)
                return false;
        }

        return true;
    }

    @Override
    public List<WmOmNoticeIEntity> checkOmStock(WmOmNoticeHEntity h, List<WmOmNoticeIEntity> is) {
        if(h == null || is == null)
            return null;
        WmOmNoticeHEntity omh = h;
        List<WmOmNoticeIEntity> omis = is;

        for (WmOmNoticeIEntity i : is){
            //查询库存
            String amount = "from ";

            //分解订单

        }

        return null;
    }

    @Override
    public Map<WmOmNoticeIEntity, List<MdPalletEntity>> assignmentPalletForOmOrder(String omNoticeId) {
        Map<WmOmNoticeIEntity, List<MdPalletEntity>> map = new HashMap<>();
        WmOmNoticeHEntity omh = this.findUniqueByProperty(WmOmNoticeHEntity.class, "omNoticeId", omNoticeId);
        List<WmOmNoticeIEntity> omis = this.findByProperty(WmOmNoticeIEntity.class, "omNoticeId", omNoticeId);

        for(WmOmNoticeIEntity i : omis){
            Long needQua = Long.parseLong(i.getGoodsQua());
            String tins = "";
            String bins = "";
            //临时货位
            //String palletHql = "select distinct mp from MdPalletEntity mp left outer join MdBinEntity mb on mp.binBianMa=mb.kuWeiBianMa where mb.kuWeiLeiXing='临时储位' and mp.zhuangLiaoBianMa=? and mp.tuoPanZhuangTai=? order by mb.quHuoCiXu ";
            String palletHql = "select distinct mp from MdPalletEntity mp,MdBinEntity mb where mp.binBianMa=mb.kuWeiBianMa and mb.kuWeiLeiXing='临时储位' and mp.zhuangLiaoBianMa=? and mp.tuoPanZhuangTai=? order by mb.quHuoCiXu ";
            List<MdPalletEntity> mps = this.findHql(palletHql, i.getGoodsId(), PalletStatus.IN_SHELF);
            Long tempCount = 0L;
            for (MdPalletEntity mp : mps){
                tempCount += Long.parseLong(mp.getShuLiang());
                tins += tins.equals("")?mp.getTuoPanBianMa():(","+mp.getTuoPanBianMa());
                bins += bins.matches("\\b"+mp.getBinBianMa()+"\\b")?"":(bins.equals("")?mp.getBinBianMa():(","+mp.getBinBianMa()));
                if(tempCount >= needQua)
                    break;
            }
            if(tempCount >= needQua)
                continue;

            //正常货位
            String sql = "select mp.* from Md_Pallet mp left join Wm_Im_Notice_I imi on mp.entry_Key=imi.id left join Md_Bin mb on mp.bin_Bian_Ma=mb.ku_Wei_Bian_Ma " +
                    " where mp.zhuang_Liao_Bian_Ma=? and mp.tuo_Pan_Zhuang_Tai=? and mb.ku_Wei_Lei_Xing='正常储位' order by DATE_ADD(imi.goods_Prd_Data,INTERVAL imi.bzhi_Qi DAY) asc,mb.qu_Huo_Ci_Xu asc,mp.bin_Depth desc";
            List<Map<String, Object>> pallets = this.findForJdbc(sql, i.getGoodsId(), PalletStatus.IN_SHELF);
            for(Map<String, Object> maptemp : pallets){
                String pcount = (String) maptemp.get("shu_liang");
                String pcode = (String) maptemp.get("tuo_pan_bian_ma");
                String pbincode = (String) maptemp.get("bin_bian_ma");
                tempCount += Long.parseLong(pcount);
                tins += tins.equals("")?pcode:(","+pcode);
                bins += bins.matches("\\b"+pbincode+"\\b")?"":(bins.equals("")?pbincode:(","+pbincode));
                if(tempCount >= needQua)
                    break;
            }
            String hql = "from MdPalletEntity mp where mp.tuoPanBianMa in ('"+tins.replaceAll(",","','")+"')";
            List<MdPalletEntity> presult = this.findHql(hql);
            map.put(i, presult);
        }

        return map;
    }
}
