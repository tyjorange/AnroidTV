package sh.slst.anroidtv.act;

import android.content.Intent;

import sh.slst.anroidtv.R;
import sh.slst.anroidtv.utils.DunViewHelper;

public class MainNvActivity extends BaseActivity {
    @Override
    public int getContentViewID() {
        return R.layout.activity_main_nv;
    }

    @Override
    public String getLeft() {
        return DunViewHelper.getNvUse();
    }

    @Override
    public String getRight() {
        return DunViewHelper.getNanUse();
    }

    @Override
    public void changeActivity() {
        startActivity(new Intent(MainNvActivity.this, MainNanActivity.class));
        finish();
    }
}
