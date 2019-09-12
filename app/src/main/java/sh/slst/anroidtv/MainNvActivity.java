package sh.slst.anroidtv;

import android.content.Intent;

import sh.slst.anroidtv.bean.BaseActivity;
import sh.slst.anroidtv.bean.DunViewHolder;

public class MainNvActivity extends BaseActivity {
    @Override
    public int getContentViewID() {
        return R.layout.activity_main_nv;
    }

    @Override
    public String getLeft() {
        return DunViewHolder.getNvUse();
    }

    @Override
    public String getRight() {
        return DunViewHolder.getNanUse();
    }

    @Override
    public void changeActivity() {
        startActivity(new Intent(MainNvActivity.this, MainNanActivity.class));
        finish();
    }
}
