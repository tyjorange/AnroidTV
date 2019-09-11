package sh.slst.anroidtv.bean;

import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    public abstract void postDebug(final String tag, final String msg);
}
