package sh.slst.anroidtv.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

import sh.slst.anroidtv.bean.DeviceSignalInfo;

/**
 * Created by Administrator on 2018/9/18.
 */

public class FileUtils {

    private static final String TAG = "FileUtils";
    private boolean isChang;

    //创建文件路径
    public static File getFilePath(Context context, String dir) {
        String directoryPath = "";
        File file;
        //判断SD卡是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            directoryPath = context.getExternalFilesDir(dir).getAbsolutePath();
            file = new File(directoryPath);
            // directoryPath =context.getExternalCacheDir().getAbsolutePath() ;
        } else {
            //没内存卡就存机身内存
            //  directoryPath = context.getFilesDir() + File.separator + dir;
            file = new File(context.getFilesDir(), dir);
        }
        if (!file.exists()) {//判断文件目录是否存在
            file.mkdirs();
        }
        return file;
    }

    public void jsonTolist(String result) {
        DeviceSignalInfo devsin;
        JSONObject jsonObject;
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                devsin = new DeviceSignalInfo();
                devsin.setCode(jsonObject.getString("code"));
                devsin.setX(jsonObject.getInt("x"));
                devsin.setY(jsonObject.getInt("y"));
                devsin.setState(jsonObject.getInt("state"));
                devsin.setClasss(jsonObject.getInt("classs"));
//              listDeviceFloorMaps.add(devsin);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
