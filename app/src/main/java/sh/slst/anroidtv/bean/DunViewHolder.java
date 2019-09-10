package sh.slst.anroidtv.bean;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import sh.slst.anroidtv.R;

public class DunViewHolder {
    private static Map<String, Integer> nan = new HashMap<>();
    private static Map<String, Integer> nv = new HashMap<>();

    public static void init() {
        initNan();
        initNv();
    }

    private static void initNan() {
        nan.put("hunan_baoqing_nan_nan01", R.id.hunan_baoqing_nan_nan01);
        nan.put("hunan_baoqing_nan_nan02", R.id.hunan_baoqing_nan_nan02);
        nan.put("hunan_baoqing_nan_nan03", R.id.hunan_baoqing_nan_nan03);
        nan.put("hunan_baoqing_nan_nan04", R.id.hunan_baoqing_nan_nan04);
        nan.put("hunan_baoqing_nan_nan05", R.id.hunan_baoqing_nan_nan05);
        nan.put("hunan_baoqing_nan_nan06", R.id.hunan_baoqing_nan_nan06);
        nan.put("hunan_baoqing_nan_nan07", R.id.hunan_baoqing_nan_nan07);
        nan.put("hunan_baoqing_nan_nan08", R.id.hunan_baoqing_nan_nan08);
        nan.put("hunan_baoqing_nan_nan09", R.id.hunan_baoqing_nan_nan09);
        nan.put("hunan_baoqing_nan_nan10", R.id.hunan_baoqing_nan_nan10);
        nan.put("hunan_baoqing_nan_nan11", R.id.hunan_baoqing_nan_nan11);
        nan.put("hunan_baoqing_nan_nan12", R.id.hunan_baoqing_nan_nan12);
        nan.put("hunan_baoqing_nan_nan13", R.id.hunan_baoqing_nan_nan13);
        nan.put("hunan_baoqing_nan_nan14", R.id.hunan_baoqing_nan_nan14);
        nan.put("hunan_baoqing_nan_nan15", R.id.hunan_baoqing_nan_nan15);
        nan.put("hunan_baoqing_nan_nan16", R.id.hunan_baoqing_nan_nan16);
        nan.put("hunan_baoqing_nan_nan17", R.id.hunan_baoqing_nan_nan17);
        nan.put("hunan_baoqing_nan_nan18", R.id.hunan_baoqing_nan_nan18);
        nan.put("hunan_baoqing_nan_nan19", R.id.hunan_baoqing_nan_nan19);
    }

    private static void initNv() {
        nv.put("hunan_baoqing_nan_nv01", R.id.hunan_baoqing_nan_nv01);
        nv.put("hunan_baoqing_nan_nv02", R.id.hunan_baoqing_nan_nv02);
        nv.put("hunan_baoqing_nan_nv03", R.id.hunan_baoqing_nan_nv03);
        nv.put("hunan_baoqing_nan_nv04", R.id.hunan_baoqing_nan_nv04);
        nv.put("hunan_baoqing_nan_nv05", R.id.hunan_baoqing_nan_nv05);
        nv.put("hunan_baoqing_nan_nv06", R.id.hunan_baoqing_nan_nv06);
        nv.put("hunan_baoqing_nan_nv07", R.id.hunan_baoqing_nan_nv07);
        nv.put("hunan_baoqing_nan_nv08", R.id.hunan_baoqing_nan_nv08);
        nv.put("hunan_baoqing_nan_nv09", R.id.hunan_baoqing_nan_nv09);
        nv.put("hunan_baoqing_nan_nv10", R.id.hunan_baoqing_nan_nv10);
        nv.put("hunan_baoqing_nan_nv11", R.id.hunan_baoqing_nan_nv11);
        nv.put("hunan_baoqing_nan_nv12", R.id.hunan_baoqing_nan_nv12);
        nv.put("hunan_baoqing_nan_nv13", R.id.hunan_baoqing_nan_nv13);
        nv.put("hunan_baoqing_nan_nv14", R.id.hunan_baoqing_nan_nv14);
        nv.put("hunan_baoqing_nan_nv15", R.id.hunan_baoqing_nan_nv15);
        nv.put("hunan_baoqing_nan_nv16", R.id.hunan_baoqing_nan_nv16);
        nv.put("hunan_baoqing_nan_nv17", R.id.hunan_baoqing_nan_nv17);
        nv.put("hunan_baoqing_nan_nv18", R.id.hunan_baoqing_nan_nv18);
        nv.put("hunan_baoqing_nan_nv19", R.id.hunan_baoqing_nan_nv19);
        nv.put("hunan_baoqing_nan_nv20", R.id.hunan_baoqing_nan_nv20);
        nv.put("hunan_baoqing_nan_nv21", R.id.hunan_baoqing_nan_nv21);
        nv.put("hunan_baoqing_nan_nv22", R.id.hunan_baoqing_nan_nv22);
        nv.put("hunan_baoqing_nan_nv23", R.id.hunan_baoqing_nan_nv23);
        nv.put("hunan_baoqing_nan_nv24", R.id.hunan_baoqing_nan_nv24);
        nv.put("hunan_baoqing_nan_nv25", R.id.hunan_baoqing_nan_nv25);
        nv.put("hunan_baoqing_nan_nv26", R.id.hunan_baoqing_nan_nv26);
        nv.put("hunan_baoqing_nan_nv27", R.id.hunan_baoqing_nan_nv27);
        nv.put("hunan_baoqing_nan_nv28", R.id.hunan_baoqing_nan_nv28);
        nv.put("hunan_baoqing_nan_nv29", R.id.hunan_baoqing_nan_nv29);
        nv.put("hunan_baoqing_nan_nv30", R.id.hunan_baoqing_nan_nv30);
        nv.put("hunan_baoqing_nan_nv31", R.id.hunan_baoqing_nan_nv31);
        nv.put("hunan_baoqing_nan_nv32", R.id.hunan_baoqing_nan_nv32);
        nv.put("hunan_baoqing_nan_nv33", R.id.hunan_baoqing_nan_nv33);
        nv.put("hunan_baoqing_nan_nv34", R.id.hunan_baoqing_nan_nv34);
        nv.put("hunan_baoqing_nan_nv35", R.id.hunan_baoqing_nan_nv35);
        nv.put("hunan_baoqing_nan_nv36", R.id.hunan_baoqing_nan_nv36);
        nv.put("hunan_baoqing_nan_nv37", R.id.hunan_baoqing_nan_nv37);
        nv.put("hunan_baoqing_nan_nv38", R.id.hunan_baoqing_nan_nv38);
        nv.put("hunan_baoqing_nan_nv39", R.id.hunan_baoqing_nan_nv39);
        nv.put("hunan_baoqing_nan_nv40", R.id.hunan_baoqing_nan_nv40);
        nv.put("hunan_baoqing_nan_nv41", R.id.hunan_baoqing_nan_nv41);
        nv.put("hunan_baoqing_nan_nv42", R.id.hunan_baoqing_nan_nv42);
        nv.put("hunan_baoqing_nan_nv43", R.id.hunan_baoqing_nan_nv43);
        nv.put("hunan_baoqing_nan_nv44", R.id.hunan_baoqing_nan_nv44);
        nv.put("hunan_baoqing_nan_nv45", R.id.hunan_baoqing_nan_nv45);
        nv.put("hunan_baoqing_nan_nv46", R.id.hunan_baoqing_nan_nv46);
        nv.put("hunan_baoqing_nan_nv47", R.id.hunan_baoqing_nan_nv47);
        nv.put("hunan_baoqing_nan_nv48", R.id.hunan_baoqing_nan_nv48);
        nv.put("hunan_baoqing_nan_nv49", R.id.hunan_baoqing_nan_nv49);
        nv.put("hunan_baoqing_nan_nv50", R.id.hunan_baoqing_nan_nv50);
        nv.put("hunan_baoqing_nan_nv51", R.id.hunan_baoqing_nan_nv51);
    }

    public static Integer getNanDunView(String key) {
        return DunViewHolder.nan.get(key);
    }

    public static Integer getNvDunView(String key) {
        return DunViewHolder.nv.get(key);
    }

    public static Collection<Integer> getAllNan() {
        return nan.values();
    }

    public static Collection<Integer> getAllNv() {
        return nv.values();
    }
}
