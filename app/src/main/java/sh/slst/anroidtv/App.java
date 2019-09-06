package sh.slst.anroidtv;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2018/7/9.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

}
