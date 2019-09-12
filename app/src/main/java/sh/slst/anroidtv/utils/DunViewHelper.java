package sh.slst.anroidtv.utils;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import sh.slst.anroidtv.R;
import sh.slst.anroidtv.act.BaseActivity;
import sh.slst.anroidtv.bean.DeviceSignalInfo;

import static android.content.Context.MODE_PRIVATE;

public class DunViewHelper {
    private static SharedPreferences sPreferences;
    private static Map<String, Integer> nanMap = new HashMap<>();
    private static List<Integer> nanList = new ArrayList<>();
    private static Map<String, Integer> nvMap = new HashMap<>();
    private static List<Integer> nvList = new ArrayList<>();
    private static JSONArray nanUseList;
    private static JSONArray nvUseList;
    private static BaseActivity activity;
    private static Logger logger;

    public static void init(BaseActivity baseActivity) {
        sPreferences = baseActivity.getSharedPreferences("STATE", MODE_PRIVATE);
        WeakReference<BaseActivity> activityWeakReference = new WeakReference<>(baseActivity);
        activity = activityWeakReference.get();
        logger = Logger.getLogger(activity.getClass().getSimpleName());
        //        sPreferences.edit().clear().apply();
        initNanViewIds();
        initNvViewIds();
        loadNanUse();
        loadNvUse();
    }

    /**
     * 第一次0填充
     *
     * @return
     */
    private static JSONArray initNanUseZero() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < nanMap.size(); i++) {
            jsonArray.put(0);
        }
        return jsonArray;
    }

    /**
     * 第一次0填充
     *
     * @return
     */
    private static JSONArray initNvUseZero() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < nvMap.size(); i++) {
            jsonArray.put(0);
        }
        return jsonArray;
    }

    /**
     * 初始化男蹲位id
     */
    private static void initNanViewIds() {
        nanMap.clear();
        nanMap.put("hunan_baoqing_nan_nan01", R.id.hunan_baoqing_nan_nan01);
        nanMap.put("hunan_baoqing_nan_nan02", R.id.hunan_baoqing_nan_nan02);
        nanMap.put("hunan_baoqing_nan_nan03", R.id.hunan_baoqing_nan_nan03);
        nanMap.put("hunan_baoqing_nan_nan04", R.id.hunan_baoqing_nan_nan04);
        nanMap.put("hunan_baoqing_nan_nan05", R.id.hunan_baoqing_nan_nan05);
        nanMap.put("hunan_baoqing_nan_nan06", R.id.hunan_baoqing_nan_nan06);
        nanMap.put("hunan_baoqing_nan_nan07", R.id.hunan_baoqing_nan_nan07);
        nanMap.put("hunan_baoqing_nan_nan08", R.id.hunan_baoqing_nan_nan08);
        nanMap.put("hunan_baoqing_nan_nan09", R.id.hunan_baoqing_nan_nan09);
        nanMap.put("hunan_baoqing_nan_nan10", R.id.hunan_baoqing_nan_nan10);
        nanMap.put("hunan_baoqing_nan_nan11", R.id.hunan_baoqing_nan_nan11);
        nanMap.put("hunan_baoqing_nan_nan12", R.id.hunan_baoqing_nan_nan12);
        nanMap.put("hunan_baoqing_nan_nan13", R.id.hunan_baoqing_nan_nan13);
        nanMap.put("hunan_baoqing_nan_nan14", R.id.hunan_baoqing_nan_nan14);
        nanMap.put("hunan_baoqing_nan_nan15", R.id.hunan_baoqing_nan_nan15);
        nanMap.put("hunan_baoqing_nan_nan16", R.id.hunan_baoqing_nan_nan16);
        nanMap.put("hunan_baoqing_nan_nan17", R.id.hunan_baoqing_nan_nan17);
        nanMap.put("hunan_baoqing_nan_nan18", R.id.hunan_baoqing_nan_nan18);
        nanMap.put("hunan_baoqing_nan_nan19", R.id.hunan_baoqing_nan_nan19);
        nanList.clear();
        nanList.add(R.id.hunan_baoqing_nan_nan01);
        nanList.add(R.id.hunan_baoqing_nan_nan02);
        nanList.add(R.id.hunan_baoqing_nan_nan03);
        nanList.add(R.id.hunan_baoqing_nan_nan04);
        nanList.add(R.id.hunan_baoqing_nan_nan05);
        nanList.add(R.id.hunan_baoqing_nan_nan06);
        nanList.add(R.id.hunan_baoqing_nan_nan07);
        nanList.add(R.id.hunan_baoqing_nan_nan08);
        nanList.add(R.id.hunan_baoqing_nan_nan09);
        nanList.add(R.id.hunan_baoqing_nan_nan10);
        nanList.add(R.id.hunan_baoqing_nan_nan11);
        nanList.add(R.id.hunan_baoqing_nan_nan12);
        nanList.add(R.id.hunan_baoqing_nan_nan13);
        nanList.add(R.id.hunan_baoqing_nan_nan14);
        nanList.add(R.id.hunan_baoqing_nan_nan15);
        nanList.add(R.id.hunan_baoqing_nan_nan16);
        nanList.add(R.id.hunan_baoqing_nan_nan17);
        nanList.add(R.id.hunan_baoqing_nan_nan18);
        nanList.add(R.id.hunan_baoqing_nan_nan19);
    }

    /**
     * 初始化女蹲位id
     */
    private static void initNvViewIds() {
        nvMap.clear();
        nvMap.put("hunan_baoqing_nan_nv01", R.id.hunan_baoqing_nan_nv01);
        nvMap.put("hunan_baoqing_nan_nv02", R.id.hunan_baoqing_nan_nv02);
        nvMap.put("hunan_baoqing_nan_nv03", R.id.hunan_baoqing_nan_nv03);
        nvMap.put("hunan_baoqing_nan_nv04", R.id.hunan_baoqing_nan_nv04);
        nvMap.put("hunan_baoqing_nan_nv05", R.id.hunan_baoqing_nan_nv05);
        nvMap.put("hunan_baoqing_nan_nv06", R.id.hunan_baoqing_nan_nv06);
        nvMap.put("hunan_baoqing_nan_nv07", R.id.hunan_baoqing_nan_nv07);
        nvMap.put("hunan_baoqing_nan_nv08", R.id.hunan_baoqing_nan_nv08);
        nvMap.put("hunan_baoqing_nan_nv09", R.id.hunan_baoqing_nan_nv09);
        nvMap.put("hunan_baoqing_nan_nv10", R.id.hunan_baoqing_nan_nv10);
        nvMap.put("hunan_baoqing_nan_nv11", R.id.hunan_baoqing_nan_nv11);
        nvMap.put("hunan_baoqing_nan_nv12", R.id.hunan_baoqing_nan_nv12);
        nvMap.put("hunan_baoqing_nan_nv13", R.id.hunan_baoqing_nan_nv13);
        nvMap.put("hunan_baoqing_nan_nv14", R.id.hunan_baoqing_nan_nv14);
        nvMap.put("hunan_baoqing_nan_nv15", R.id.hunan_baoqing_nan_nv15);
        nvMap.put("hunan_baoqing_nan_nv16", R.id.hunan_baoqing_nan_nv16);
        nvMap.put("hunan_baoqing_nan_nv17", R.id.hunan_baoqing_nan_nv17);
        nvMap.put("hunan_baoqing_nan_nv18", R.id.hunan_baoqing_nan_nv18);
        nvMap.put("hunan_baoqing_nan_nv19", R.id.hunan_baoqing_nan_nv19);
        nvMap.put("hunan_baoqing_nan_nv20", R.id.hunan_baoqing_nan_nv20);
        nvMap.put("hunan_baoqing_nan_nv21", R.id.hunan_baoqing_nan_nv21);
        nvMap.put("hunan_baoqing_nan_nv22", R.id.hunan_baoqing_nan_nv22);
        nvMap.put("hunan_baoqing_nan_nv23", R.id.hunan_baoqing_nan_nv23);
        nvMap.put("hunan_baoqing_nan_nv24", R.id.hunan_baoqing_nan_nv24);
        nvMap.put("hunan_baoqing_nan_nv25", R.id.hunan_baoqing_nan_nv25);
        nvMap.put("hunan_baoqing_nan_nv26", R.id.hunan_baoqing_nan_nv26);
        nvMap.put("hunan_baoqing_nan_nv27", R.id.hunan_baoqing_nan_nv27);
        nvMap.put("hunan_baoqing_nan_nv28", R.id.hunan_baoqing_nan_nv28);
        nvMap.put("hunan_baoqing_nan_nv29", R.id.hunan_baoqing_nan_nv29);
        nvMap.put("hunan_baoqing_nan_nv30", R.id.hunan_baoqing_nan_nv30);
        nvMap.put("hunan_baoqing_nan_nv31", R.id.hunan_baoqing_nan_nv31);
        nvMap.put("hunan_baoqing_nan_nv32", R.id.hunan_baoqing_nan_nv32);
        nvMap.put("hunan_baoqing_nan_nv33", R.id.hunan_baoqing_nan_nv33);
        nvMap.put("hunan_baoqing_nan_nv34", R.id.hunan_baoqing_nan_nv34);
        nvMap.put("hunan_baoqing_nan_nv35", R.id.hunan_baoqing_nan_nv35);
        nvMap.put("hunan_baoqing_nan_nv36", R.id.hunan_baoqing_nan_nv36);
        nvMap.put("hunan_baoqing_nan_nv37", R.id.hunan_baoqing_nan_nv37);
        nvMap.put("hunan_baoqing_nan_nv38", R.id.hunan_baoqing_nan_nv38);
        nvMap.put("hunan_baoqing_nan_nv39", R.id.hunan_baoqing_nan_nv39);
        nvMap.put("hunan_baoqing_nan_nv40", R.id.hunan_baoqing_nan_nv40);
        nvMap.put("hunan_baoqing_nan_nv41", R.id.hunan_baoqing_nan_nv41);
        nvMap.put("hunan_baoqing_nan_nv42", R.id.hunan_baoqing_nan_nv42);
        nvMap.put("hunan_baoqing_nan_nv43", R.id.hunan_baoqing_nan_nv43);
        nvMap.put("hunan_baoqing_nan_nv44", R.id.hunan_baoqing_nan_nv44);
        nvMap.put("hunan_baoqing_nan_nv45", R.id.hunan_baoqing_nan_nv45);
        nvMap.put("hunan_baoqing_nan_nv46", R.id.hunan_baoqing_nan_nv46);
        nvMap.put("hunan_baoqing_nan_nv47", R.id.hunan_baoqing_nan_nv47);
        nvMap.put("hunan_baoqing_nan_nv48", R.id.hunan_baoqing_nan_nv48);
        nvMap.put("hunan_baoqing_nan_nv49", R.id.hunan_baoqing_nan_nv49);
        nvMap.put("hunan_baoqing_nan_nv50", R.id.hunan_baoqing_nan_nv50);
        nvMap.put("hunan_baoqing_nan_nv51", R.id.hunan_baoqing_nan_nv51);
        nvList.clear();
        nvList.add(R.id.hunan_baoqing_nan_nv01);
        nvList.add(R.id.hunan_baoqing_nan_nv02);
        nvList.add(R.id.hunan_baoqing_nan_nv03);
        nvList.add(R.id.hunan_baoqing_nan_nv04);
        nvList.add(R.id.hunan_baoqing_nan_nv05);
        nvList.add(R.id.hunan_baoqing_nan_nv06);
        nvList.add(R.id.hunan_baoqing_nan_nv07);
        nvList.add(R.id.hunan_baoqing_nan_nv08);
        nvList.add(R.id.hunan_baoqing_nan_nv09);
        nvList.add(R.id.hunan_baoqing_nan_nv10);
        nvList.add(R.id.hunan_baoqing_nan_nv11);
        nvList.add(R.id.hunan_baoqing_nan_nv12);
        nvList.add(R.id.hunan_baoqing_nan_nv13);
        nvList.add(R.id.hunan_baoqing_nan_nv14);
        nvList.add(R.id.hunan_baoqing_nan_nv15);
        nvList.add(R.id.hunan_baoqing_nan_nv16);
        nvList.add(R.id.hunan_baoqing_nan_nv17);
        nvList.add(R.id.hunan_baoqing_nan_nv18);
        nvList.add(R.id.hunan_baoqing_nan_nv19);
        nvList.add(R.id.hunan_baoqing_nan_nv20);
        nvList.add(R.id.hunan_baoqing_nan_nv21);
        nvList.add(R.id.hunan_baoqing_nan_nv22);
        nvList.add(R.id.hunan_baoqing_nan_nv23);
        nvList.add(R.id.hunan_baoqing_nan_nv24);
        nvList.add(R.id.hunan_baoqing_nan_nv25);
        nvList.add(R.id.hunan_baoqing_nan_nv26);
        nvList.add(R.id.hunan_baoqing_nan_nv27);
        nvList.add(R.id.hunan_baoqing_nan_nv28);
        nvList.add(R.id.hunan_baoqing_nan_nv29);
        nvList.add(R.id.hunan_baoqing_nan_nv30);
        nvList.add(R.id.hunan_baoqing_nan_nv31);
        nvList.add(R.id.hunan_baoqing_nan_nv32);
        nvList.add(R.id.hunan_baoqing_nan_nv33);
        nvList.add(R.id.hunan_baoqing_nan_nv34);
        nvList.add(R.id.hunan_baoqing_nan_nv35);
        nvList.add(R.id.hunan_baoqing_nan_nv36);
        nvList.add(R.id.hunan_baoqing_nan_nv37);
        nvList.add(R.id.hunan_baoqing_nan_nv38);
        nvList.add(R.id.hunan_baoqing_nan_nv39);
        nvList.add(R.id.hunan_baoqing_nan_nv40);
        nvList.add(R.id.hunan_baoqing_nan_nv41);
        nvList.add(R.id.hunan_baoqing_nan_nv42);
        nvList.add(R.id.hunan_baoqing_nan_nv43);
        nvList.add(R.id.hunan_baoqing_nan_nv44);
        nvList.add(R.id.hunan_baoqing_nan_nv45);
        nvList.add(R.id.hunan_baoqing_nan_nv46);
        nvList.add(R.id.hunan_baoqing_nan_nv47);
        nvList.add(R.id.hunan_baoqing_nan_nv48);
        nvList.add(R.id.hunan_baoqing_nan_nv49);
        nvList.add(R.id.hunan_baoqing_nan_nv50);
        nvList.add(R.id.hunan_baoqing_nan_nv51);
    }

    /**
     * 从文件加载男蹲使用情况
     */
    private static void loadNanUse() {
        // 加载男使用情况
        String nan_use = sPreferences.getString("nan_use", "");

        if (nan_use.equals("")) {
            nanUseList = initNanUseZero();
            SharedPreferences.Editor editor = sPreferences.edit();
            editor.putString("nan_use", nanUseList.toString()).apply();
            activity.postDebug("| init nan_use :", nanUseList.length() + nanUseList.toString());
            logger.info((nanUseList.length() + " init nan_use " + nanUseList.toString()));
        } else {
            try {
                nanUseList = new JSONArray(nan_use);
                activity.postDebug("| load nan_use :", nanUseList.length() + nanUseList.toString());
                logger.info(nanUseList.length() + " load nan_use " + nanUseList.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从文件加载女蹲使用情况
     */
    private static void loadNvUse() {
        //加载女使用情况
        String nv_use = sPreferences.getString("nv_use", "");
        if (nv_use.equals("")) {
            nvUseList = DunViewHelper.initNvUseZero();
            SharedPreferences.Editor editor = sPreferences.edit();
            editor.putString("nv_use", nvUseList.toString()).apply();
            activity.postDebug("| init nv_use :", nvUseList.length() + nvUseList.toString());
            logger.info(nvUseList.length() + " init nv_use " + nvUseList.toString());
        } else {
            try {
                nvUseList = new JSONArray(nv_use);
                activity.postDebug("| load nv_use :", nvUseList.length() + nvUseList.toString());
                logger.info(nvUseList.length() + " load nv_use " + nvUseList.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 男蹲位all view_id list
     *
     * @return
     */
    public static List<Integer> getAllNan() {
        return nanList;
    }

    /**
     * 女蹲位all view_id list
     *
     * @return
     */
    public static List<Integer> getAllNv() {
        return nvList;
    }

    /**
     * 男蹲位占用状态 list
     *
     * @return
     */
    public static JSONArray getNanStatusList() {
        return nanUseList;
    }

    /**
     * 女蹲位占用状态 list
     *
     * @return
     */
    public static JSONArray getNvStatusList() {
        return nvUseList;
    }

    /**
     * 统计男蹲位占用
     *
     * @return
     */
    private static int getNanUseCount() {
        int count = 0;
        for (int i = 0; i < nanUseList.length(); i++) {
            try {
                Integer o = (Integer) nanUseList.get(i);
                if (o == DeviceSignalInfo.STATE_ON) {
                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    /**
     * 统计女蹲位占用
     *
     * @return
     */
    private static int getNvUseCount() {
        int count = 0;
        for (int i = 0; i < nvUseList.length(); i++) {
            try {
                Integer o = (Integer) nvUseList.get(i);
                if (o == DeviceSignalInfo.STATE_ON) {
                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    /**
     * 改变男蹲位状态并持久化
     *
     * @param viewId
     * @param status
     */
    public static void changeNanStatus(int viewId, int status) {
        try {
            for (int index = 0; index < nanList.size(); index++) {
                if (nanList.get(index) == (viewId)) {
                    nanUseList.put(index, status);
                    SharedPreferences.Editor editor = sPreferences.edit();
                    editor.putString("nan_use", nanUseList.toString()).apply();
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 改变女蹲位状态并持久化
     *
     * @param viewId
     * @param status
     */
    public static void changeNvStatus(int viewId, int status) {
        try {
            for (int index = 0; index < nvList.size(); index++) {
                if (nvList.get(index) == (viewId)) {
                    nvUseList.put(index, status);
                    SharedPreferences.Editor editor = sPreferences.edit();
                    editor.putString("nv_use", nvUseList.toString()).apply();
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Integer getNanDunViewByKey(String key) {
        return nanMap.get(key);
    }

    public static Integer getNanDunViewByIndex(int index) {
        return nanList.get(index);
    }

    public static Integer getNvDunViewByKey(String key) {
        return nvMap.get(key);
    }

    public static Integer getNvDunViewByIndex(int index) {
        return nvList.get(index);
    }

    public static String getNanUse() {
        return getNanUseCount() + "/" + nanList.size();
    }

    public static String getNvUse() {
        return getNvUseCount() + "/" + nvList.size();
    }
}
