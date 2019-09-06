package sh.slst.anroidtv;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.appindexing.Thing;
//import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import sh.slst.anroidtv.bean.DeviceSignalInfo;
import sh.slst.anroidtv.bean.MQTTConfig;
import sh.slst.anroidtv.utils.FileUtils;
import sh.slst.anroidtv.utils.utils;
import sh.slst.anroidtv.view.FloorMapView;

public class MainNewActivity extends AppCompatActivity implements MqttCallback, ISubscibeConnectMessage {

    private String TAG = "MainNewActivity";
    private FloorMapView floorMapView;

    private SubscribeClient mClient;
    private MQTTConfig mqttConfig;
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
    private String s;
    private Context context;
    private int visitors = 0;
    private TextView text_useposition;
    private int allcounts = 0;
    private int useposition;
    private AlertDialog alertDialog;
    private int state;
    private String signal;
    private String code;
    private MqttMessage message;
    private LinearLayout lay_left;
    private String toiltId;
    private DeviceSignalInfo dbDeviceFloorMap;
    private String Filename = "map";
    private boolean isChang = false;
    private SharedPreferences sPreferences;

    private List<String> mListDebugInfo = new ArrayList<>();
    private DebugMessageAdapter mAdapter;
    Timer timer = new Timer();
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    initTime();
                    break;
                case 2:
                    //更新累计人数
                    break;
                case 3:
                    text_useposition.setText(useposition + "/" + allcounts);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_news);

        sPreferences = MainNewActivity.this.getSharedPreferences("STATE", MODE_PRIVATE);
        String wmessage = sPreferences.getString("wrongmessage", "");
//      updataLog(wmessage);

       /* SharedPreferences.Editor editor = sPreferences.edit();
        editor.putBoolean("ischang", isChang);
        editor.commit();*/

        initConfig();

        weekWords = getResources().getStringArray(R.array.week);
        tvYear = (TextView) findViewById(R.id.tv_year);
        tvMonth = (TextView) findViewById(R.id.tv_month);
        tvDay = (TextView) findViewById(R.id.tv_day);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvTime = (TextView) findViewById(R.id.tv_time);
        text_useposition = (TextView) findViewById(R.id.text_useposition);
        lay_left = (LinearLayout) findViewById(R.id.lay_left);
        lay_left.setPadding(20, utils.px2dip(this, (float) 80), 0, utils.px2dip(this, (float) 42));

        initTime();

        floorMapView = (FloorMapView) findViewById(R.id.img_floor);
        txtDebug = (TextView) findViewById(R.id.txt_debug);
        //ListView listDebug = (ListView) findViewById(R.id.list_debug);
        //mAdapter = new DebugMessageAdapter(this, mListDebugInfo);
        // listDebug.setAdapter(mAdapter);

        floorMapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        float x = event.getX();
                        float y = event.getY();
//                      Toast.makeText(MainNewActivity.this, "X=" + x + "Y=" + y, Toast.LENGTH_SHORT).show();
                        JudgmentScope(x, y);
                        break;
                }
                return true;
            }
        });

        if (mqttConfig != null) {
            mClient = new SubscribeClient(mqttConfig.ip, mqttConfig.port);
            mClient.setMessageNotify(this);
            topic = "/" + mqttConfig.dev.fsu_code + "/" + mqttConfig.dev.dev_code;
            mClient.subscribe(topic, topic + "*_*", this, null);
        }
        if (listDeviceFloorMaps == null) {
            listDeviceFloorMaps = new ArrayList<>();
        }
        initFloorBitmap();
        floorMapView.setData(listDeviceFloorMaps, bmpFloor);
        timer.schedule(task, 0, 10 * 1000);
    }


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
                dbDeviceFloorMap = findDevice(code);
                dbDeviceFloorMap.code = toiltId;
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

    private void saveDataToFile(String data, String fileName) {

        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
            isChang = true;
            SharedPreferences.Editor editor = sPreferences.edit();
            editor.putBoolean("ischang", isChang);
            editor.commit();
            Log.i(TAG, "changstate");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSingleAlertDialog() {

        final String[] items = {"无人", "有人", "清扫", "维修", "修改ID"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("请设置厕所状态");
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
                    String jsonContent = getJsonContent("6000", code, state + "");
                    Log.i("jsonContent", jsonContent);
                    //图标状态改变
                    dbDeviceFloorMap = findDevice(code);
                    dbDeviceFloorMap.state = state;
                    updataLog();
                    //发送消息
                    mClient.publish("/datain" + "/" + mqttConfig.dev.fsu_code + "/" + mqttConfig.dev.dev_code, jsonContent);
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

    private void readDeviceConfig() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("assets/" + "config/mqtt_config.json"); //E1  E2   NV  NAN
            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
            Type type = new TypeToken<MQTTConfig>() {
            }.getType();
            mqttConfig = gson.fromJson(jsonReader, type);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    private void initTime() {

        SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("zh"));
        String sDate = shortDateFormat.format(new Date(System.currentTimeMillis()));
        Log.i(TAG, "DATA=" + sDate);

        Calendar calendar = Calendar.getInstance();
        String date = weekWords[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        tvYear.setText(sDate.substring(0, 4));
        tvMonth.setText(sDate.substring(5, 7));
        tvDay.setText(sDate.substring(8, 10));
        tvDate.setText(date);
        tvTime.setText(sDate.substring(11, 16));
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };


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
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("assets/" + "map/mapnvce2.json"); //map1  map
                Gson gson = new Gson();
                JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
                Type type = new TypeToken<List<DeviceSignalInfo>>() {
                }.getType();
                listDeviceFloorMaps = gson.fromJson(jsonReader, type);
                Log.i("canvas1", listDeviceFloorMaps.size() + "");
            } catch (
                    Exception e
            ) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    //判断点击的范围
    public void JudgmentScope(float x, float y) {
        for (DeviceSignalInfo floorMap : listDeviceFloorMaps) {
            if (floorMap.y + 47 > y && y > floorMap.y - 47 && floorMap.x + 38 > x && x > floorMap.x - 38) { //男厕 x> 660     //女厕X>516
                Log.i("x y", "floorMapy=" + floorMap.y + "floorMap.x=" + "-------" + floorMap.x + "x=" + floorMap.x + "y=" + y);
                code = floorMap.code;
                signal = floorMap.signal;
                showSingleAlertDialog();
            }
        }
    }

    public JSONArray listToArry() {
        JSONArray jsonArray = new JSONArray();
        getCacheDir();
        for (DeviceSignalInfo floorMap : listDeviceFloorMaps) {
            JSONObject scenes = new JSONObject();
            try {
//              scenes.put("signal", floorMap.signal);
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

    private void initFloorBitmap() {
        try {
            InputStream inputStream = getAssets().open("floormap/nvce_right.png");
            bmpFloor = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        //失去连接
        Log.e(TAG, "connectionLost " + throwable.getMessage());
        onMessage("connectionLost" + throwable.getCause());
        mClient.startReconnect();
    }

    /**
     * @param s
     * @param mqttMessage System.out.println("接收消息主题:" + topic + " Qos:" + message.getQos());
     *                    System.out.println("接收消息内容:" + new String(message.getPayload()));
     * @Override public void connectionLost(Throwable throwable) {
     * //失去连接
     * Log.e(TAG, "connectionLost " + throwable.getMessage());
     * onMessage("connectionLost" + throwable.getCause());
     * mClient.startReconnect();
     * }
     * {"signal":"6001", "visitors":1192}  访问人数统计 一分钟一次
     * <p>
     * {"signal":"6000", "code":"80DFA114004B1200", "state":0} 蹲位状态 实时 1有人 0离开
     */
    @Override
    public void messageArrived(String s, final MqttMessage mqttMessage) throws Exception {
        final String msgEntity = new String(mqttMessage.getPayload());
        Gson gson = new Gson();
        DeviceSignalInfo deviceSignalInfo = gson.fromJson(msgEntity, DeviceSignalInfo.class);
        /* Log.i("接收消息主题 : ", topic);
        Log.i("接收消息Qos : ", mqttMessage.getQos() + "");
        Log.i("接收消息内容 : ", new String(mqttMessage.getPayload()));*/
        if (deviceSignalInfo.signal.equals("6000")) { //蹲位状态变化
            DeviceSignalInfo dbDeviceFloorMap = findDevice(deviceSignalInfo.code);
            dbDeviceFloorMap.state = deviceSignalInfo.state;
            useposition = getUseratio();
            Message message = new Message();
            message.what = 3;
            handler.sendMessage(message);
        }
        if (deviceSignalInfo.signal.equals("6001")) {
            visitors = deviceSignalInfo.visitors;
            Message message = new Message();
            message.what = 2;
            handler.sendMessage(message);
        }
        updataLog(msgEntity);
    }

    private String getJsonContent(String signal, String code, String state) {
        try {
            JSONObject scenes = new JSONObject();
            scenes.put("signal", signal);
            scenes.put("code", code);
            scenes.put("state", state);
            return scenes.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void updataLog(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
             /* mListDebugInfo.add("messageArrived :" + message + simpleDateFormat.format(Calendar.getInstance().getTime()));
                mAdapter.notifyDataSetChanged();*/
                floorMapView.updateDeviceState();
            }
        });
    }

    public void updataLog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                floorMapView.updateDeviceState();
            }
        });
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        Log.d(TAG, "deliveryComplete");
    }

    public int getUseratio() {
        int position = 0;
        allcounts = listDeviceFloorMaps.size();
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

//    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void onMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mListDebugInfo.add(message + "" + simpleDateFormat.format(Calendar.getInstance().getTime()));
//                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClient.disconnect();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}
