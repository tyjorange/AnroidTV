package sh.slst.anroidtv;

import android.content.Intent;

import sh.slst.anroidtv.bean.BaseActivity;
import sh.slst.anroidtv.bean.DunViewHolder;

public class MainNanActivity extends BaseActivity {

    @Override
    public int getContentViewID() {
        return R.layout.activity_main_nan;
    }

    @Override
    public String getLeft() {
        return DunViewHolder.getNanUse();
    }

    @Override
    public String getRight() {
        return DunViewHolder.getNvUse();
    }

    @Override
    public void changeActivity() {
        startActivity(new Intent(MainNanActivity.this, MainNvActivity.class));
        finish();
    }

}
