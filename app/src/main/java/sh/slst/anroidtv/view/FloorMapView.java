package sh.slst.anroidtv.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import sh.slst.anroidtv.R;
import sh.slst.anroidtv.bean.DeviceSignalInfo;
import sh.slst.anroidtv.bean.MQTTConfig;

/**
 * Created by liuchengran on 2018/6/22.
 */

public class FloorMapView extends android.support.v7.widget.AppCompatImageView {

    private String TAG = "FloorMapView";
    private Bitmap mFloorBitmap;
    private Bitmap mFloorCanvasBitmap;
    private DisplayMetrics displayMetrics;
    private List<DeviceSignalInfo> mListDeviceState = new ArrayList<>();
    private Canvas mFloorCanvas;
    private Bitmap bitmapdun, bitmapdun1, bitmapzuo, bitmapzuo1, repair, clean, canjiren, canjiren1;
    //    private Rect srcRED, srcGREEN;
    private String toitleClass;
    private MQTTConfig mqttConfig;
    private RectF dst;

    public FloorMapView(Context context) {
        super(context);
    }

    public FloorMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        readDeviceConfig();
        init(attrs);
    }

    private void readDeviceConfig() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("assets/" + "config/mqtt_config.json");
            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
            Type type = new TypeToken<MQTTConfig>() {
            }.getType();
            mqttConfig = gson.fromJson(jsonReader, type);
            toitleClass = mqttConfig.tclass;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    public void setData(List<DeviceSignalInfo> listDeviceState, Bitmap floorBitmap) {
        mFloorBitmap = floorBitmap.copy(Bitmap.Config.ARGB_8888, true);
        mFloorCanvasBitmap = Bitmap.createBitmap(mFloorBitmap.getWidth(), mFloorBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        mFloorCanvas = new Canvas(mFloorCanvasBitmap);
        mListDeviceState = listDeviceState;
        invalidate();
    }

    public void updateDeviceState() {
        invalidate();
    }

    private void init(AttributeSet attrs) {

        displayMetrics = getResources().getDisplayMetrics();

        if (toitleClass.equals("NV")) {
            bitmapdun = BitmapFactory.decodeResource(this.getResources(),
                    R.mipmap.nv1new).copy(Bitmap.Config.ARGB_8888, true);
            bitmapdun1 = BitmapFactory.decodeResource(this.getResources(),
                    R.mipmap.nv2new).copy(Bitmap.Config.ARGB_8888, true);

        } else {
            bitmapdun = BitmapFactory.decodeResource(this.getResources(),
                    R.mipmap.nan1new).copy(Bitmap.Config.ARGB_8888, true);
            bitmapdun1 = BitmapFactory.decodeResource(this.getResources(),
                    R.mipmap.nan2new).copy(Bitmap.Config.ARGB_8888, true);
        }
//        srcRED = new Rect(0, 0, bitmapdun.getWidth(), bitmapdun.getHeight());
//        srcGREEN = new Rect(0, 0, bitmapdun1.getWidth(), bitmapdun1.getHeight());  //  RectF（float left,float top,float right,float bottom）

        bitmapzuo = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.zuobianqi2).copy(Bitmap.Config.ARGB_8888, true);
        bitmapzuo1 = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.zuobianqi1).copy(Bitmap.Config.ARGB_8888, true);

        canjiren = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.canyiren2).copy(Bitmap.Config.ARGB_8888, true);
        canjiren1 = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.canyiren1).copy(Bitmap.Config.ARGB_8888, true);

        repair = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.weixiu).copy(Bitmap.Config.ARGB_8888, true);

        clean = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.qingsao).copy(Bitmap.Config.ARGB_8888, true);
    }


    private int dip2px(int dp) {
        return displayMetrics.densityDpi / 160 * dp;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        if (mFloorBitmap == null) {
            return;
        }
        Paint paint = new Paint();
        mFloorCanvas.drawColor(getResources().getColor(R.color.yellow));
        paint.setDither(true);
        mFloorCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        mFloorCanvas.drawBitmap(mFloorBitmap, 0, 0, paint);
        int colorOn = Color.RED, colorLeave = Color.GREEN;

        for (DeviceSignalInfo deviceFloorMap : mListDeviceState) {

            if (deviceFloorMap.state == DeviceSignalInfo.STATE_ON) {
                paint.setColor(colorOn);
                Log.i(TAG, "state=STATE_ON");
//                bitmapdun = BitmapFactory.decodeResource(this.getResources(),
//                        R.mipmap.nvce1).copy(Bitmap.Config.ARGB_8888, true);
                Fillicon(deviceFloorMap, bitmapzuo, canjiren, bitmapdun, paint);

            } else if (deviceFloorMap.state == DeviceSignalInfo.STATE_LEAVE) {
                paint.setColor(colorLeave);
                Fillicon(deviceFloorMap, bitmapzuo1, canjiren1, bitmapdun1, paint);

            } else if (deviceFloorMap.state == DeviceSignalInfo.SQUETT_REPAIR) {
                paint.setColor(colorOn);
                int destRectWith = 38, destRectHeight = 48;
                int dstFillWidth = repair.getWidth() / 2 > destRectWith ? destRectWith : repair.getWidth() / 2;
                int dstFillHeight = repair.getHeight() / 2 > destRectHeight ? destRectHeight : repair.getHeight() / 2;
                dst = new RectF(deviceFloorMap.x - dstFillWidth + 8,
                        deviceFloorMap.y - dstFillHeight + 8,
                        deviceFloorMap.x + dstFillWidth - 8,
                        deviceFloorMap.y + dstFillHeight - 8);
                mFloorCanvas.drawBitmap(repair, new Rect(0, 0, repair.getWidth(), repair.getHeight()), dst, paint);

            } else if (deviceFloorMap.state == DeviceSignalInfo.SQUETT_CLEAN) {
                paint.setColor(colorOn);
                int destRectWith = 38, destRectHeight = 48;
                int dstFillWidth = clean.getWidth() / 2 > destRectWith ? destRectWith : clean.getWidth() / 2;
                int dstFillHeight = clean.getHeight() / 2 > destRectHeight ? destRectHeight : clean.getHeight() / 2;
                dst = new RectF(deviceFloorMap.x - dstFillWidth + 8,
                        deviceFloorMap.y - dstFillHeight + 8,
                        deviceFloorMap.x + dstFillWidth - 8,
                        deviceFloorMap.y + dstFillHeight - 8);
                mFloorCanvas.drawBitmap(clean, new Rect(0, 0, clean.getWidth(), clean.getHeight()), dst, paint);
            }
        }
        Rect rcDraw = new Rect();
        Rect rcSrc = new Rect(0, 0, mFloorBitmap.getWidth(), mFloorBitmap.getHeight());
        int drawHeight = screenWidth * mFloorBitmap.getHeight() / mFloorBitmap.getWidth();
        rcDraw.set(0, (screenHeight - drawHeight) / 2, screenWidth, (screenHeight - drawHeight) / 2 + drawHeight);
        canvas.drawColor(getResources().getColor(R.color.background));
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(mFloorCanvasBitmap, rcSrc, rcDraw, paint);
    }

    /**
     * @param deviceFloorMap
     * @param bitmap1
     * @param bitmap2
     * @param bitmap3
     * @param paint
     */
    public void Fillicon(DeviceSignalInfo deviceFloorMap, Bitmap bitmap1, Bitmap bitmap2, Bitmap bitmap3, Paint paint) {
        int destRectWith = 38, destRectHeight = 48;
        if (deviceFloorMap.classs == DeviceSignalInfo.SIT_ON) { //坐便
            int dstFillWidth = bitmap1.getWidth() / 2 > destRectWith ? destRectWith : bitmap1.getWidth() / 2 - 8;
            int dstFillHeight = bitmap1.getHeight() / 2 > destRectHeight ? destRectHeight : bitmap1.getHeight() / 2 - 8;
            dst = new RectF(deviceFloorMap.x - dstFillWidth,
                    deviceFloorMap.y - dstFillHeight,
                    deviceFloorMap.x + dstFillWidth,
                    deviceFloorMap.y + dstFillHeight);
            mFloorCanvas.drawBitmap(bitmap1, new Rect(0, 0, bitmap1.getWidth(), bitmap1.getHeight()), dst, paint);
        } else if (deviceFloorMap.classs == DeviceSignalInfo.SQUETT_SPECIAL) { //第三卫生间
            mFloorCanvas.drawBitmap(bitmap2, new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight()), dst, paint);
        } else if (deviceFloorMap.classs == DeviceSignalInfo.SQUETT_TOILETS) {//蹲便
//            int dstFillWidth = bitmap3.getWidth() / 2 > destRectWith ? destRectWith - 8 : bitmap3.getWidth() / 2;
//            int dstFillHeight = bitmap3.getHeight() / 2 > destRectHeight ? destRectHeight - 8 : bitmap3.getHeight() / 2;
            int dstFillWidth = bitmap3.getWidth();
            int dstFillHeight = bitmap3.getHeight();
            dst = new RectF(deviceFloorMap.x - dstFillWidth,
                    deviceFloorMap.y - dstFillHeight,
                    deviceFloorMap.x + dstFillWidth,
                    deviceFloorMap.y + dstFillHeight);
            mFloorCanvas.drawBitmap(bitmap3, new Rect(0, 0, bitmap3.getWidth(), bitmap3.getHeight()), dst, paint);
        }
    }
}
