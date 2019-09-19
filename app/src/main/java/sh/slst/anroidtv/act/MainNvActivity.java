package sh.slst.anroidtv.act;

import android.content.Intent;

import sh.slst.anroidtv.R;

public class MainNvActivity extends BaseActivity {

    @Override
    public int getContentViewID() {
        if (getHeightPixel() == 900) {
            return R.layout.activity_main_nv_900p;
        } else if (getHeightPixel() == 1080) {
            return R.layout.activity_main_nv_1080p;
        }
        return R.layout.activity_main_nv_1080p;
    }

    @Override
    public String getLeft() {
        return dunViewHelper.getNvUse();
    }

    @Override
    public String getRight() {
        return dunViewHelper.getNanUse();
    }

    @Override
    public void changeActivity() {
        startActivity(new Intent(MainNvActivity.this, MainNanActivity.class));
        finish();
    }
}
