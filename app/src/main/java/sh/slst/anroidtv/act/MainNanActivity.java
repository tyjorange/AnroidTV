package sh.slst.anroidtv.act;

import android.content.Intent;

import sh.slst.anroidtv.R;
import sh.slst.anroidtv.utils.DunViewHelper;

public class MainNanActivity extends BaseActivity {

    @Override
    public int getContentViewID() {
        return R.layout.activity_main_nan;
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
