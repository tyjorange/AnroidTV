package sh.slst.anroidtv;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.logging.Logger;

/*
        订阅  /0101002/E1  FSU号+设备号

		{"signal":"6001", "visitors":1192}  访问人数统计 一分钟一次

		{"signal":"6000", "code":"80DFA114004B1200", "state":0} 蹲位状态 实时 1有人 0离开
*/

public class SubscribeClient {

    private String HOST = "tcp://192.168.0.1:1883";  //192.168.0.1

    private MqttClient s_client;
    private MqttConnectOptions options;
//    private ScheduledExecutorService scheduler;
//    private MqttTopic topic11;

    private ISubscibeConnectMessage iSubscibeConnectMessage;
    private String mTopic;
    private boolean isExit = false;

    public SubscribeClient(String ip, int port) {
        HOST = "tcp://" + ip + ":" + port;
    }

    public void subscribe(String topic, String clientid, MqttCallback callback, String sendcontent) {
        mTopic = topic;
        start(topic, clientid, callback, sendcontent);
    }


    public void setMessageNotify(ISubscibeConnectMessage connectMessage) {
        iSubscibeConnectMessage = connectMessage;
    }

    /**
     * @param topic
     * @param clientid
     */
    private void start(String topic, String clientid, MqttCallback callback, String sendcontent) {
        try {
            // host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            s_client = new MqttClient(HOST, clientid, new MemoryPersistence());
            // MQTT的连接设置
            options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
            // options.setUserName(userName);
            // 设置连接的密码
            // options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(60);
            // 设置回调
            s_client.setCallback(callback);
            // MqttTopic mtopic = client.getTopic(topic);
            // setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
            // options.setWill(mtopic, "client close".getBytes(), 0, true);
            s_client.connect(options);
            if (s_client.isConnected()) {
                Log.i("isConnected to host", HOST);
                iSubscibeConnectMessage.onMessage("isConnected to host " + HOST);
            }
            subscribe();
            // 订阅消息
        } catch (Exception e) {
            if (iSubscibeConnectMessage != null) {
                iSubscibeConnectMessage.onMessage(e.getCause() + "topic=" + topic + "  clientid=" + clientid + " host=" + HOST);
            }
//            startReconnect();
        }
    }


    private void subscribe() {
        try {
            String[] arr = {mTopic};
            s_client.subscribe(arr);
            Log.i("subscribe Topic ", mTopic);
            iSubscibeConnectMessage.onMessage("subscribe Topic " + mTopic);
        } catch (MqttException e) {
            e.printStackTrace();
//            startReconnect();
        }
    }

    public void publish(String topic, String msg) {
        try {
            if (s_client != null && s_client.isConnected()) {
                MqttMessage message = new MqttMessage();
                message.setQos(0);
                message.setRetained(false);
                message.setPayload(msg.getBytes());
                s_client.publish(topic, message);
            }
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开链接
     */
    public void disconnect() {
        try {
            if (s_client.isConnected()) {
                s_client.disconnect();
                isExit = true;
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新链接
     */
    public void startReconnect() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                do {
                    if (iSubscibeConnectMessage != null) {
                        iSubscibeConnectMessage.onMessage("开始重连");
                    }
                    if (!s_client.isConnected() && !isExit) {
                        try {
                            if (iSubscibeConnectMessage != null) {
                                iSubscibeConnectMessage.onMessage("重连中。。。");
                            }
                            s_client.connect(options);
                            if (iSubscibeConnectMessage != null) {
                                iSubscibeConnectMessage.onMessage("重连成功");
                            }
                            subscribe();
                            break;
                        } catch (MqttSecurityException e) {
                            e.printStackTrace();
                            if (iSubscibeConnectMessage != null) {
                                iSubscibeConnectMessage.onMessage("重连失败  " + e.getCause());
                            }
                        } catch (MqttException e) {
                            if (iSubscibeConnectMessage != null) {
                                iSubscibeConnectMessage.onMessage("重连失败 " + e.getCause());
                            }
                            e.printStackTrace();
                        }
                    }
                } while (!isExit);
            }
        }.start();
    }

//    /**
//     * 订阅示例
//     *
//     * @param args
//     * @throws MqttException
//     */
//    public static void main(String[] args) throws MqttException {
//		SubscribeClient client_1 = new SubscribeClient();
//		String topic1 = "RFID/SET";
//		String clientid1 = "RFIDPub01";
//		client_1.start(topic1, clientid1);
//		SubscribeClient client_2 = new SubscribeClient();
//		String topic2 = "GNSS/LOC";
//		String clientid2 = "GnssPub01";
//		client_2.start(topic2, clientid2);
//	}

}
