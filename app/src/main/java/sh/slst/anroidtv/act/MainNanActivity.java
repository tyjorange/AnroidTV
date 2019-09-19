package sh.slst.anroidtv.act;

import android.content.Intent;

import sh.slst.anroidtv.R;

public class MainNanActivity extends BaseActivity {

    @Override
    public int getContentViewID() {
        if (getHeightPixel() == 900) {
            return R.layout.activity_main_nan_900p;
        } else if (getHeightPixel() == 1080) {
            return R.layout.activity_main_nan_1080p;
        }
        return R.layout.activity_main_nan_1080p;
    }

    @Override
    public String getLeft() {
        return dunViewHelper.getNanUse();
    }

    @Override
    public String getRight() {
        return dunViewHelper.getNvUse();
    }

    @Override
    public void changeActivity() {
        startActivity(new Intent(MainNanActivity.this, MainNvActivity.class));
        finish();
    }

}
