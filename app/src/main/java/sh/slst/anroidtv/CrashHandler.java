package sh.slst.anroidtv;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import sh.slst.anroidtv.act.MainNanActivity;

/**
 * Created by Administrator on 2018/7/9.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    Context mcontext;
    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();
    // 获取系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    // CrashHandler实例
    private static CrashHandler INSTANCE;
    private SharedPreferences sPreferences;

    public void init(Context context) {
        mcontext = context;
        sPreferences = mcontext.getSharedPreferences("STATE", Context.MODE_MULTI_PROCESS);
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 自定义错误处理，收集错误信息，发送错误报告等
     *
     * @param ex
     * @return
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread() {
            public void run() {
                Looper.prepare();
                Toast.makeText(mcontext, "很抱歉,程序出现异常", Toast.LENGTH_LONG)
                        .show();
                Looper.loop();
            }

            ;
        }.start();
        collectDeviceInfo(mcontext);
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 获取设备参数信息
     *
     * @param context
     */
    public void collectDeviceInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                infos.put(field.getName(), field.get(null).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        // 得到错误信息
        String result = writer.toString();
        sb.append(result);
        Log.i("wrongmess", result);
        SharedPreferences.Editor editor = sPreferences.edit();
        //      editor.clear();
        editor.putString("wrongmessage", result);
        editor.commit();
    }


    /**
     * 当uncaught发生时会转入该函数处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            AlarmManager mgr = (AlarmManager) mcontext.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(mcontext, MainNanActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("crash", true);
            PendingIntent restartIntent = PendingIntent.getActivity(mcontext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            System.gc();
        }

    }

    public Class<?> getTopActivity() {
        ActivityManager manager = (ActivityManager) mcontext.getSystemService(Context.ACTIVITY_SERVICE);
        String className = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        Class<?> cls = null;
        try {
            cls = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cls;
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            return INSTANCE = new CrashHandler();
        } else {
            return INSTANCE;
        }
    }

}
