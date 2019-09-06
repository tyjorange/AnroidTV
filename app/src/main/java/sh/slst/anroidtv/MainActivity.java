package sh.slst.anroidtv;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ViewFlipper;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.appindexing.Thing;
//import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by SH on 2018/3/1.
 */
@Deprecated
public class MainActivity extends Activity {

    String TAG = "Main";

    private TextView
            tvTitle,//标题
    tvMan,//男厕未使用数
    tvWoman,//女厕未使用数
    tvYear,//年
    tvMonth,//月
    tvDay,//日
    tvDate,//星期
    tvTime,//时分
    tvPeopleCounting,//入侧累计人数
    tvTemperature,//温度
    tvWind,//风力
    tvHumidity,//湿度
    tvTip;//标语

    private ViewFlipper
            vfMan,//男厕蹲位当前使用情况
            vfWoman;//女厕蹲位当前使用情况

    private List<ToiletSite> manSites, womanSites;

    private String[] weekWords;
    private String[] tips;
    private int tipsId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weekWords = getResources().getStringArray(R.array.week);
        tips = getResources().getStringArray(R.array.tip);

        tvYear =(TextView)findViewById(R.id.tv_year);
        tvMonth =(TextView) findViewById(R.id.tv_month);
        tvDay = (TextView)findViewById(R.id.tv_day);
        tvDate = (TextView)findViewById(R.id.tv_date);
        tvTime = (TextView)findViewById(R.id.tv_time);

        tvMan = (TextView)findViewById(R.id.tv_man);
        tvWoman =(TextView) findViewById(R.id.tv_woman);

        vfMan = (ViewFlipper)findViewById(R.id.vf_man);
        vfWoman = (ViewFlipper)findViewById(R.id.vf_woman);

        tvTip = (TextView)findViewById(R.id.tv_tip);

        initTime();
        initEnvironment();
        initData();
        initVfMan();
        initVfWoman();
        initTips();
    }

    private boolean isRestart;

    private void initTips() {
        tipsId = 0;
        isRestart = false;
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_fade);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                isRestart = !isRestart;
                Log.e(TAG, "onAnimationRepeat: " + tipsId);
                if (isRestart) {
                    tipsId = (tipsId + 1) % tips.length;
                    tvTip.setText(tips[tipsId]);
                }
            }
        });
        tvTip.startAnimation(animation);
    }

    private void initEnvironment() {

    }

    private void initData() {
        manSites = new ArrayList<>();
        womanSites = new ArrayList<>();
        manSites.addAll(ToiletSite.getTestData(0));
        womanSites.addAll(ToiletSite.getTestData(1));
    }

    private void initVfMan() {
        int usedCount = 0;
        for (ToiletSite site : manSites) {
            if (site.isUsing()) {
                usedCount++;
            }
        }
        String usedCountString = usedCount + "";
        tvMan.setText(usedCountString);

        int viewSize = (int) Math.ceil(manSites.size() / 10.0);
        Log.e(TAG, "viewSize_man: " + viewSize);
        for (int i = 0; i < viewSize; i++) {
            View view = View.inflate(this, R.layout.view_vf, null);
            GridView gv = (GridView) view.findViewById(R.id.gv);
            gv.setAdapter(new ToiletAdapter(this, manSites.subList(i * 10, i == viewSize - 1 ? manSites.size() - 1 : (i + 1) * 10)));
            vfMan.addView(view);
        }
    }

    private void initVfWoman() {
        int usedCount = 0;
        for (ToiletSite site : womanSites) {
            if (site.isUsing()) {
                usedCount++;
            }
        }
        String usedCountString = usedCount + "";
        tvWoman.setText(usedCountString);

        int viewSize = (int) Math.ceil(womanSites.size() / 10.0);
        Log.e(TAG, "viewSize_woman: " + viewSize);
        for (int i = 0; i < viewSize; i++) {
            View view = View.inflate(this, R.layout.view_vf, null);
            GridView gv = (GridView) view.findViewById(R.id.gv);
            gv.setAdapter(new ToiletAdapter(this, womanSites.subList(i * 10, i == viewSize - 1 ? womanSites.size() - 1 : (i + 1) * 10)));
            vfWoman.addView(view);
        }
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        String year = calendar.get(Calendar.YEAR) + "";
        String month = (calendar.get(Calendar.MONTH) + 1) + "";
        String day = calendar.get(Calendar.DAY_OF_MONTH) + "";
        String date = weekWords[calendar.get(Calendar.DAY_OF_WEEK)];
        String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);

        tvYear.setText(year);
        tvMonth.setText(month);
        tvDay.setText(day);
        tvDate.setText(date);
        tvTime.setText(time);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    public Action getIndexApiAction() {
//        Thing object = new Thing.Builder()
//                .setName("Main Page") // TODO: Define a title for the content shown.
//                // TODO: Make sure this auto-generated URL is correct.
//                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
//                .build();
//        return new Action.Builder(Action.TYPE_VIEW)
//                .setObject(object)
//                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
//                .build();
//    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
