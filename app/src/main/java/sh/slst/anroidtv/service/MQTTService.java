package sh.slst.anroidtv.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTService extends Service {
    public static final String TAG = MQTTService.class.getSimpleName();
    private static Context context;
    private static MqttClient client;
    private static MqttConnectOptions conOpt;

    private static String ip;
    private static int port;
    private static String myTopic;
    private static String clientId = System.currentTimeMillis() + myTopic;//客户端标识
    private static IGetMessageCallBack iGetMessageCallBack;

    @Override
    public IBinder onBind(Intent intent) {
        Log.w(TAG, "onBind");
        return new CustomBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Log.w(TAG, "onCreate");
        try {
            SharedPreferences sPreferences = getSharedPreferences("STATE", Context.MODE_MULTI_PROCESS);
            ip = sPreferences.getString("mqttIP", "192.168.0.1");
            port = sPreferences.getInt("mqttPort", 1883);
            myTopic = sPreferences.getString("mqttTopic", "/TOILET");
            init();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void publish(String msg) {
        int qos = 0;
        boolean retained = false;
        try {
            if (client != null) {
                client.publish(myTopic, msg.getBytes(), qos, retained);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private static void init() throws MqttException {
        String host = "tcp://" + ip + ":" + port;
        // 服务器地址（协议+地址+端口号）
        client = new MqttClient(host, clientId, new MemoryPersistence());

        conOpt = new MqttConnectOptions();
        // 清除缓存
        conOpt.setCleanSession(true);
        // 设置超时时间，单位：秒
        conOpt.setConnectionTimeout(10);
        // 心跳包发送间隔，单位：秒
        conOpt.setKeepAliveInterval(60);
        // 设置MQTT监听并且接受消息
        client.setCallback(mqttCallback);
        // 用户名
//        conOpt.setUserName(userName);
        // 密码 将字符串转换为字符串数组
//        conOpt.setPassword(passWord.toCharArray());

        // last will message
//        boolean doConnect = true;
//        String message = "{\"terminal_uid\":\"" + clientId + "\"}";
//        Log.e(getClass().getName(), "message是:" + message);
//        String topic = myTopic;
//        Integer qos = 0;
//        Boolean retained = false;
//        if (message.equals("") || topic.equals("")) {
        // MQTT本身就是为信号不稳定的网络设计的，所以难免一些客户端会无故的和Broker断开连接。
        //当客户端连接到Broker时，可以指定LWT，Broker会定期检测客户端是否有异常。
        //当客户端异常掉线时，Broker就往连接时指定的topic里推送当时指定的LWT消息。
//            try {
//                conOpt.setWill(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
//            } catch (Exception e) {
//                Log.i(TAG, "Exception Occured", e);
//        doConnect = false;
//                iMqttActionListener.onFailure(null, e);
//            }
//        }

//        if (doConnect) {
//        doClientConnection();
//        }

    }

    /**
     * 重定向地址
     *
     * @param ip
     * @param port
     * @param topic
     */
    public static void reLocate(String ip, int port, String topic) {
        if (client.isConnected()) {
            try {
                client.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        try {
            MQTTService.ip = ip;
            MQTTService.port = port;
            MQTTService.myTopic = topic;
            init();
            doClientConnection(ip, port, topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接MQTT服务器
     */
    public static void doClientConnection(String ip, int port, String topic) {
        String host = "tcp://" + ip + ":" + port;
        if (!client.isConnected() && isConnectIsNormal()) {
            try {
                client.connect(conOpt);
                if (client.isConnected()) {
                    Log.i("isConnected to host ", host);
                    if (iGetMessageCallBack != null) {
                        iGetMessageCallBack.setMessage("isConnected to host " + host);
                    }
                    subscribe(topic);
                }
            } catch (MqttException e) {
                e.printStackTrace();
                if (iGetMessageCallBack != null) {
                    iGetMessageCallBack.setMessage(e.getCause() + "");
                }
            }
        }

    }

    /**
     * 订阅
     */
    private static void subscribe(String topic) {
        try {
            String[] arr = {topic};
            client.subscribe(arr);
            Log.i("subscribe Topic ", topic);
            if (iGetMessageCallBack != null) {
                iGetMessageCallBack.setMessage("subscribe Topic " + topic);
            }
        } catch (MqttException e) {
            e.printStackTrace();
//            startReconnect();
        }
    }

    /**
     * 判断网络是否连接
     */
    private static boolean isConnectIsNormal() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = null;
        if (connectivityManager != null) {
            info = connectivityManager.getActiveNetworkInfo();
        }
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "当前网络名称：" + name);
            return true;
        } else {
            Log.i(TAG, "没有可用网络");
            return false;
        }
    }

    // MQTT是否连接成功
//    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {
//
//        @Override
//        public void onSuccess(IMqttToken arg0) {
//            Log.i(TAG, "连接成功 " + arg0);
//            try {
//                // 订阅myTopic话题
//                client.subscribe(myTopic, 1);
//                Log.i("subscribe Topic ", myTopic);
//            } catch (MqttException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onFailure(IMqttToken arg0, Throwable arg1) {
//            Log.i(TAG, "连接失败 " + host);
//            arg1.printStackTrace();
//            // 连接失败，重连
//        }
//    };
    // MQTT监听并且接受消息
    private static MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void messageArrived(String topic, MqttMessage message) {

            String str1 = new String(message.getPayload());
            Log.i(TAG, "messageArrived:" + str1);
            if (iGetMessageCallBack != null) {
                iGetMessageCallBack.setMessage(str1);
            }
//            String str2 = topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained();
//            Log.i(TAG, str2);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        public void connectionLost(Throwable arg0) {
            // 失去连接，重连
            startReconnect();
        }
    };

    public void setIGetMessageCallBack(IGetMessageCallBack iGetMessageCallBack) {
        MQTTService.iGetMessageCallBack = iGetMessageCallBack;
    }

    class CustomBinder extends Binder {
        MQTTService getService() {
            return MQTTService.this;
        }
    }

    private static boolean isExit = false;

    /**
     * 断开链接
     */
    public void disconnect() {
        try {
            if (client.isConnected()) {
                client.disconnect();
                isExit = true;
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    /**
     * 重新链接
     */
    public static void startReconnect() {
        new Thread() {
            @Override
            public void run() {
//                super.run();
                do {
                    if (iGetMessageCallBack == null) {
                        return;
                    }
                    iGetMessageCallBack.setMessage("开始重连");
                    if (!client.isConnected() && !isExit) {
                        try {
                            iGetMessageCallBack.setMessage("重连中。。。");
                            client.connect(conOpt);
                            iGetMessageCallBack.setMessage("重连成功");
//                            isExit = false;
                            subscribe(myTopic);
                            break;
                        } catch (MqttSecurityException e) {
                            e.printStackTrace();
                            iGetMessageCallBack.setMessage("重连失败  " + e.getCause());
                        } catch (MqttException e) {
                            iGetMessageCallBack.setMessage("重连失败 " + e.getCause());
                            e.printStackTrace();
                        }
                    }
                } while (!isExit);
            }
        }.start();
    }
}
