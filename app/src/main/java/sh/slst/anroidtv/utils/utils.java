package sh.slst.anroidtv.utils;

import android.content.Context;
import android.util.Log;

import sh.slst.anroidtv.bean.DeviceSignalInfo;

/**
 * Created by Administrator on 2018/9/18.
 */

public class utils {
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }



}
