package sh.slst.anroidtv.act;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import sh.slst.anroidtv.DebugMessageAdapter;
import sh.slst.anroidtv.MainActivity;
import sh.slst.anroidtv.mqtt.ISubscibeConnectMessage;
import sh.slst.anroidtv.R;
import sh.slst.anroidtv.mqtt.SubscribeClient;
import sh.slst.anroidtv.bean.DeviceSignalInfo;
import sh.slst.anroidtv.bean.MQTTConfig;
import sh.slst.anroidtv.bean.MessageBean;
import sh.slst.anroidtv.service.IGetMessageCallBack;
import sh.slst.anroidtv.service.MQTTService;
import sh.slst.anroidtv.service.MyServiceConnection;
import sh.slst.anroidtv.utils.DunViewHelper;
import sh.slst.anroidtv.utils.FileUtils;
import sh.slst.anroidtv.utils.utils;

public abstract class BaseActivity extends AppCompatActivity implements IGetMessageCallBack {
    private String TAG = getClass().getSimpleName();
    //    private FloorMapView floorMapView;
//    private SubscribeClient mClient;
    //    private MQTTConfig mqttConfig;
    private List<DeviceSignalInfo> listDeviceFloorMaps;

    private String topic;
    private Bitmap bmpFloor;

    private TextView
            txtDebug,
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
    private String[] weekWords;
    private Context context;
    //    private int visitors = 0;
    private TextView text_useposition_left;
    private TextView text_useposition_right;
    //    private int allcounts_nan;
//    private int allcounts_nv;
//    private int useposition_nan;
//    private int useposition_nv;
    private AlertDialog alertDialog;
    private int state;
    private String signal;
    private String code;
    //    private MqttMessage message;
    private LinearLayout lay_left;
    private String toiltId;
    //    private DeviceSignalInfo dbDeviceFloorMap;
//    private DeviceSignalInfo deviceSignalInfo;
    private String Filename = "map";
    private boolean isChang = false;
    private SharedPreferences sPreferences;
    protected DunViewHelper dunViewHelper;
    //debug
    private List<String> mListDebugInfo = new ArrayList<>();
    private ListView listDebug;
    private DebugMessageAdapter mAdapter;
    //timer
    private Timer timer = new Timer();

    private static final int fTime = 1;//刷新时钟
    private static final int fCount = 2;//更新累计人数
    private static final int fUse = 3;//更新蹲位使用情况
    private static final int cMqtt = 4;//启动时连接MQTT & 订阅消息
    private String mqttIP;
    private Integer mqttPort;
    private String mqttTopic;

    private MyServiceConnection serviceConnection;

    // 实例化一个MyHandler对象
    private BaseActivity.MyHandler handler = new BaseActivity.MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<BaseActivity> activityWeakReference;

        MyHandler(BaseActivity activity) {
            activityWeakReference = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final BaseActivity theActivity = activityWeakReference.get();
            switch (msg.what) {
                case fTime:
                    theActivity.initTime();
                    break;
                case fCount:
                    break;
                case fUse:
                    theActivity.changeDun();
                    theActivity.flushStatus();
                    break;
                case cMqtt:
//                    theActivity.initMqtt();
                    break;
            }
        }
    }

//    private void initMqtt() {
//        if (mClient == null) {
//            doConnect();
//        } else {
//            if (mClient.isConnected()) {
//                mClient.disconnect();
//            }
//            doConnect();
//        }
//        listDebug.setVisibility(View.VISIBLE);
//    }

    private void doConnect() {
//        mClient = new SubscribeClient(mqttIP, mqttPort);
//        mClient.setMessageNotify(this);
//        theActivity.topic = "/" + theActivity.mqttConfig.dev.fsu_code + "/" + theActivity.mqttConfig.dev.dev_code;
//        mClient.subscribe(mqttTopic, mqttTopic + "*_*", this, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
        setContentView(getContentViewID());

        sPreferences = BaseActivity.this.getSharedPreferences("STATE", MODE_PRIVATE);
        mqttIP = sPreferences.getString("mqttIP", "192.168.0.1");
        mqttPort = sPreferences.getInt("mqttPort", 1883);
        mqttTopic = sPreferences.getString("mqttTopic", "/TOILET");
//      updataLog(wmessage);

       /* SharedPreferences.Editor editor = sPreferences.edit();
        editor.putBoolean("ischang", isChang);
        editor.commit();*/

//        initConfig();

        weekWords = getResources().getStringArray(R.array.week);
//        tvYear = (TextView) findViewById(R.id.tv_year);
//        tvMonth = (TextView) findViewById(R.id.tv_month);
//        tvDay = (TextView) findViewById(R.id.tv_day);
//        tvDate = (TextView) findViewById(R.id.tv_date);
//        tvTime = (TextView) findViewById(R.id.tv_time);
        text_useposition_left = (TextView) findViewById(R.id.text_useposition_left);
        text_useposition_right = (TextView) findViewById(R.id.text_useposition_right);
        lay_left = (LinearLayout) findViewById(R.id.lay_left);
//        lay_left.setPadding(20, utils.px2dip(this, (float) 80), 0, utils.px2dip(this, (float) 42));

        initTime();

//        floorMapView = (FloorMapView) findViewById(R.id.img_floor);
//        txtDebug = (TextView) findViewById(R.id.txt_debug);

        // Debug 视图
        listDebug = (ListView) findViewById(R.id.list_debug);
        mAdapter = new DebugMessageAdapter(this, mListDebugInfo);
        listDebug.setAdapter(mAdapter);
        findViewById(R.id.iv_ad_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listDebug.getVisibility() == View.VISIBLE) {
                    listDebug.setVisibility(View.GONE);
                } else {
                    listDebug.setVisibility(View.VISIBLE);
                }
            }
        });
        // 切换地图
        findViewById(R.id.iv_ad_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity();
//                startActivity(new Intent(MainNanActivity.this, MainNvActivity.class));
//                finish();
            }
        });
        // 修改配置
        findViewById(R.id.tv_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogConfig();
            }
        });
//        floorMapView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        float x = event.getX();
//                        float y = event.getY();
//                        Toast.makeText(MainNanActivity.this, "X=" + x + " Y=" + y, Toast.LENGTH_SHORT).show();
//                        JudgmentScope(x, y);
//                        break;
//                }
//                return true;
//            }
//        });

        Message message = new Message();
        message.what = cMqtt;
        handler.sendMessage(message);
        if (listDeviceFloorMaps == null) {
            listDeviceFloorMaps = new ArrayList<>();
        }
//        initFloorBitmap();
//        floorMapView.setData(listDeviceFloorMaps, bmpFloor);
        timer.schedule(task1, 0, 1000);


        //初始化蹲位
        dunViewHelper = DunViewHelper.getInstance(this);
        flushStatus();
        //初始化蹲位图片 和点击事件
        List<Integer> allNan = dunViewHelper.getAllNan();
        JSONArray nanStatusList = dunViewHelper.getNanStatusList();
        for (int i = 0; i < allNan.size(); i++) {
            Integer integer = allNan.get(i);
            ImageView viewById = findViewById(integer);
            viewById.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CharSequence contentDescription = view.getContentDescription();
                    showSingleAlertDialog(contentDescription.toString());
                }
            });
            try {
                int status = nanStatusList.getInt(i);
                viewById.setImageDrawable(status == 1 ? getResources().getDrawable(R.mipmap.nan1new) : getResources().getDrawable(R.mipmap.nan2new));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        List<Integer> allNv = dunViewHelper.getAllNv();
        JSONArray nvStatusList = dunViewHelper.getNvStatusList();
        for (int i = 0; i < allNv.size(); i++) {
            Integer integer = allNv.get(i);
            ImageView viewById = findViewById(integer);
            viewById.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CharSequence contentDescription = view.getContentDescription();
                    showSingleAlertDialog(contentDescription.toString());
                }
            });
            try {
                int status = nvStatusList.getInt(i);
                viewById.setImageDrawable(status == 1 ? getResources().getDrawable(R.mipmap.nv1new) : getResources().getDrawable(R.mipmap.nv2new));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        getDensity();

        serviceConnection = new MyServiceConnection();
        serviceConnection.setIGetMessageCallBack(BaseActivity.this);
        Intent intent = new Intent(this, MQTTService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 获取屏幕尺寸
     */
    private void getDensity() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        int widthPixel = outMetrics.widthPixels;
        int heightPixel = outMetrics.heightPixels;
        float densityDpi = outMetrics.densityDpi;
        String s1 = utils.getSystemLanguage();
        String s2 = utils.getSystemVersion();
        String s3 = utils.getDeviceBrand();
        String s4 = utils.getSystemModel();
        String s5 = outMetrics.toString();
        String s6 = "widthPixel = " + widthPixel + ",heightPixel = " + heightPixel;
        String s7 = "densityDpi = " + densityDpi;
        Log.i(TAG, s1);
        Log.i(TAG, s2);
        Log.i(TAG, s3);
        Log.i(TAG, s4);
        Log.i(TAG, s5);
        Log.i(TAG, s6);
        Log.i(TAG, s7);
        postDebug(" ", s1);
        postDebug(" ", s2);
        postDebug(" ", s3);
        postDebug(" ", s4);
        postDebug(" ", s5);
        postDebug(" ", s6);
        postDebug(" ", s7);
    }

    int getHeightPixel() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 刷新左右统计状态
     */
    private void flushStatus() {
        text_useposition_left.setText("当前使用：" + getLeft());
        text_useposition_right.setText("当前使用：" + getRight());
    }

    /**
     * 修改蹲位ID
     */
    private void dialogPutId() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_ensure, null);
        final EditText content = (EditText) v.findViewById(R.id.txt_tip_desc);
        TextView btn_sure = (TextView) v.findViewById(R.id.txt_ok);
        TextView btn_cancel = (TextView) v.findViewById(R.id.txt_cancel);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(utils.dp2px(getApplicationContext(), 300), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(v);
        //dialog.getWindow().setGravity(Gravity.CENTER);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //修改ID号
                toiltId = content.getText().toString();
//                dbDeviceFloorMap = findDevice(code);
//                if (dbDeviceFloorMap != null) {
//                    dbDeviceFloorMap.code = toiltId;
//                }
                //List转化为JsonArray
                JSONArray jsonArray = listToArry();
                //JsonArray写入文件
                FileUtils.getFilePath(getApplicationContext(), "map");
                saveDataToFile(jsonArray.toString(), "map");
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 修改配置
     */
    private void dialogConfig() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_config, null);
        final EditText et_ip = (EditText) v.findViewById(R.id.txt_tip_ip);
        final EditText et_port = (EditText) v.findViewById(R.id.txt_tip_port);
        final EditText et_topic = (EditText) v.findViewById(R.id.txt_tip_topic);
        et_ip.setText(mqttIP);
        et_port.setText(String.valueOf(mqttPort));
        et_topic.setText(mqttTopic);
        TextView btn_sure = (TextView) v.findViewById(R.id.txt_ok);
        TextView btn_cancel = (TextView) v.findViewById(R.id.txt_cancel);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(utils.dp2px(getApplicationContext(), 300), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(v);
        //dialog.getWindow().setGravity(Gravity.CENTER);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_ip = et_ip.getText().toString();
                if (utils.isIP(str_ip)) {
                    mqttIP = str_ip;
                } else {
                    Toast.makeText(context, "IP格式不正确", Toast.LENGTH_LONG).show();
                    return;
                }
                String str_port = et_port.getText().toString();
                if (utils.isPort(str_port)) {
                    mqttPort = Integer.valueOf(str_port);
                } else {
                    Toast.makeText(context, "端口格式不正确", Toast.LENGTH_LONG).show();
                    return;
                }
                mqttTopic = et_topic.getText().toString();
                SharedPreferences.Editor editor = sPreferences.edit();
                editor.putString("mqttIP", mqttIP.toString()).apply();
                editor.putInt("mqttPort", mqttPort).apply();
                editor.putString("mqttTopic", mqttTopic.toString()).apply();

//                Toast.makeText(context, "修改配置成功", Toast.LENGTH_LONG).show();
                Message message = new Message();
                message.what = cMqtt;
                handler.sendMessage(message);
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    private void saveDataToFile(String data, String fileName) {

        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
            isChang = true;
            SharedPreferences.Editor editor = sPreferences.edit();
            editor.putBoolean("ischang", isChang);
            editor.apply();
            Log.i(TAG, "changstate");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSingleAlertDialog(final String code) {
//        final String[] items = {"无人", "有人", "清扫", "维修", "修改ID"};
        final String[] items = {"无人", "有人", "清扫", "维修"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("请设置厕所状态:" + code);
        state = 0;
        alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                state = i;
            }
        });

        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (state == 4) {
                    alertDialog.dismiss();
                    dialogPutId();
                } else {
                    final String jsonContent = getJsonContent(code, String.valueOf(state), System.currentTimeMillis() + "");
//                    final String topic = "/" + mqttConfig.dev.fsu_code + "/" + mqttConfig.dev.dev_code;
                    final String topic = mqttTopic;
                    Log.i("topic", topic);
                    Log.i("jsonContent", jsonContent);
                    //图标状态改变
//                    dbDeviceFloorMap = findDevice(code);
//                    if (dbDeviceFloorMap != null) {
//                        dbDeviceFloorMap.state = state;
//                    }
//                    updataLog();
                    postDebug(" publish:", (topic + " # " + jsonContent));
                    //发送消息
                    MQTTService.publish(jsonContent);
                }
            }
        });
        alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private void initConfig() {
        readDeviceConfig();
        readDeviceMap();
    }

    /**
     * 加载配置文件json
     */
    private void readDeviceConfig() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("assets/" + "config/mqtt_config.json"); //E1  E2   NV  NAN
            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
            Type type = new TypeToken<MQTTConfig>() {
            }.getType();
//            mqttConfig = gson.fromJson(jsonReader, type);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * 加载蹲位布局json
     */
    private void readDeviceMap() {
        StringBuilder sb = new StringBuilder("");
        boolean changstate = sPreferences.getBoolean("ischang", false);
        Log.i(TAG, "isChang--" + changstate);
        changstate = false;
        if (changstate) {
            //获取文件在内存卡中files目录下的路径
            Filename = getApplicationContext().getFilesDir() + File.separator + Filename;
            //打开文件输入流
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(Filename);
                Gson gson = new Gson();
                JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
                Type type = new TypeToken<List<DeviceSignalInfo>>() {
                }.getType();
                listDeviceFloorMaps = gson.fromJson(jsonReader, type);
                for (DeviceSignalInfo floorMap : listDeviceFloorMaps) {
                    Log.i(TAG, "daochu===" + floorMap.getCode() + "----" + floorMap.getState() + "---" + floorMap.getX() + "---" + floorMap.getY());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("assets/" + "map/nan_left.json"); //map1  map
                Gson gson = new Gson();
                JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
                Type type = new TypeToken<List<DeviceSignalInfo>>() {
                }.getType();
                listDeviceFloorMaps = gson.fromJson(jsonReader, type);
                Log.i("canvas1", listDeviceFloorMaps.size() + "");
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy年MM月dd日-HH:mm:ss", Locale.getDefault());

    private void initTime() {
        String sDate = shortDateFormat.format(new Date(System.currentTimeMillis()));
        String[] split = sDate.split("-");
        Calendar calendar = Calendar.getInstance();
        String date = "     星期" + weekWords[calendar.get(Calendar.DAY_OF_WEEK) - 1] + "   ";
        String ss = split[0] + date + split[1];
        TextView tv_date = findViewById(R.id.tv_date);
        tv_date.setText(ss);
//        tvYear.setText(sDate.substring(0, 4));
//        tvMonth.setText(sDate.substring(5, 7));
//        tvDay.setText(sDate.substring(8, 10));
//        tvDate.setText(date);
//        tvTime.setText(sDate.substring(11, 16));
    }

    private TimerTask task1 = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = fTime;
            handler.sendMessage(message);
        }
    };


    //判断点击的范围
    public void JudgmentScope(float _x_, float _y_) {
        for (DeviceSignalInfo floorMap : listDeviceFloorMaps) {
            if (floorMap.y + 47 > _y_ && _y_ > floorMap.y - 47 && floorMap.x + 38 > _x_ && _x_ > floorMap.x - 38) { //男厕 _x_> 660     //女厕X>516
                Log.i("_x_ _y_", " floorMap._x_=" + floorMap.x + " floorMapy=" + floorMap.y);
                Log.i("_x_ _y_", " _x_=" + _x_ + " _y_=" + _y_);
                code = floorMap.code;
                signal = floorMap.signal;
//                showSingleAlertDialog();
            }
        }
    }

    public JSONArray listToArry() {
        JSONArray jsonArray = new JSONArray();
        getCacheDir();
        for (DeviceSignalInfo floorMap : listDeviceFloorMaps) {
            JSONObject scenes = new JSONObject();
            try {
                scenes.put("signal", floorMap.signal);
                scenes.put("code", floorMap.code);
                scenes.put("x", floorMap.getX());
                scenes.put("y", floorMap.getY());
                scenes.put("state", floorMap.state);
                scenes.put("classs", floorMap.getClasss());
                jsonArray.put(scenes);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    /**
     * 加载地图
     */
    private void initFloorBitmap() {
        try {
//            InputStream inputStream = getAssets().open("floormap/nvce_right.png");
            InputStream inputStream = getAssets().open("floormap/nan_left.png");
            bmpFloor = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

//    @Override
//    public void connectionLost(Throwable throwable) {
//        throwable.printStackTrace();
//        //失去连接
//        Log.e(TAG, "connectionLost " + throwable.getMessage());
//        onMessage("connectionLost " + throwable.getCause());
////        mClient.startReconnect();
//    }

    private List<MessageBean.Closet> mCloset;
    private MessageBean.Traffic mTraffic;

    //    @Override
//    public void messageArrived(String topic, final MqttMessage mqttMessage) {
//        final String msgEntity = new String(mqttMessage.getPayload());
//        updataLog(msgEntity);
//    }
    @Override
    public void setMessage(String msgEntity) {
        if (msgEntity.startsWith("{") & msgEntity.endsWith("}")) {
            Gson gson = new Gson();
            MessageBean messageBean = gson.fromJson(msgEntity, MessageBean.class);
            List<MessageBean.Closet> closet = messageBean.closet;
            if (closet != null) { //蹲位状态变化
                mCloset = closet;
                Message message = new Message();
                message.what = fUse;
                handler.sendMessage(message);
            }
            MessageBean.Traffic traffic = messageBean.traffic;
            if (traffic != null) {// 人流统计消息
                mTraffic = traffic;
                Message message = new Message();
                message.what = fCount;
                handler.sendMessage(message);
            }
            if (signal != null) {
                if (signal.equals("6000")) { //蹲位状态变化
//            DeviceSignalInfo dbDeviceFloorMap = findDevice(deviceSignalInfo.code);
//            if (dbDeviceFloorMap != null) {
//                dbDeviceFloorMap.state = deviceSignalInfo.state;
//            }
//            useposition = getUseratio();
//            useposition = getUseratio();
                    Message message = new Message();
                    message.what = fUse;
                    handler.sendMessage(message);
                }
                if (signal.equals("6001")) {
//                    visitors = deviceSignalInfo.visitors;
                    Message message = new Message();
                    message.what = fCount;
                    handler.sendMessage(message);
                }
            }
        }
        postDebug(" messageArrived :", msgEntity);
    }

    /**
     * 改变蹲位状态
     */
    private void changeDun() {
        for (MessageBean.Closet c : mCloset) {
            Integer nvDunViewId = dunViewHelper.getNvDunViewByKey(c.closet_id);
            ImageView viewById;
            if (nvDunViewId != null) {
                // 改女蹲位
                viewById = findViewById(nvDunViewId);
                dunViewHelper.changeNvStatus(nvDunViewId, c.state);
                viewById.setImageDrawable(c.state == 1 ? getResources().getDrawable(R.mipmap.nv1new) : getResources().getDrawable(R.mipmap.nv2new));
            }
            Integer nanDunViewId = dunViewHelper.getNanDunViewByKey(c.closet_id);
            if (nanDunViewId != null) {
                // 改男蹲位
                viewById = findViewById(nanDunViewId);
                dunViewHelper.changeNanStatus(nanDunViewId, c.state);
                viewById.setImageDrawable(c.state == 1 ? getResources().getDrawable(R.mipmap.nan1new) : getResources().getDrawable(R.mipmap.nan2new));
            }
        }
    }

    private String getJsonContent(String closet_id, String state, String time) {
        try {
            JSONObject outer = new JSONObject();
            JSONArray array = new JSONArray();
            JSONObject inner = new JSONObject();
            inner.put("closet_id", closet_id);
            inner.put("state", state);
            inner.put("time", time);
            array.put(inner);
            outer.put("closet", array);
            return outer.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void updataLog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                floorMapView.updateDeviceState();
            }
        });
    }

//    @Override
//    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//        Log.d(TAG, "deliveryComplete");
//    }

    public int getUseratio() {
        int position = 0;
//        allcounts = listDeviceFloorMaps.size();
        for (DeviceSignalInfo floorMap : listDeviceFloorMaps) {
            if (floorMap.state == DeviceSignalInfo.STATE_ON) {
                position++;
            }
        }
        return position;
    }

    private DeviceSignalInfo findDevice(String code) {
        int position = 0;
        for (DeviceSignalInfo floorMap : listDeviceFloorMaps) {
            if (floorMap.code.equals(code)) {
                return floorMap;
            }
            position++;
        }
        return null;
    }

    public void updataLog(final String message) {
        postDebug(" updataLog :", message);
    }

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[HH:mm:ss]", Locale.CHINA);

//    @Override
//    public void postDebug(final String tag, final String msg) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mListDebugInfo.add(simpleDateFormat.format(Calendar.getInstance().getTime()) + tag + msg);
//                mAdapter.notifyDataSetChanged();
//            }
//        });
//    }

//    @Override
//    public void onMessage(final String message) {
//        postDebug(" onMessage :", message);
//    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (mClient != null) {
//            mClient.disconnect();
//        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
//        if (mClient != null) {
//            mClient.disconnect();
//        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 添加到DEBUG视图
     *
     * @param tag
     * @param msg
     */
    public void postDebug(final String tag, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListDebugInfo.add(simpleDateFormat.format(Calendar.getInstance().getTime()) + tag + msg);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public abstract int getContentViewID();

    public abstract String getLeft();

    public abstract String getRight();

    public abstract void changeActivity();

}
