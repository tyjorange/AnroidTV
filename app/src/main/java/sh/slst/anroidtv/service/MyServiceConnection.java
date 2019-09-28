package sh.slst.anroidtv.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;

public class MyServiceConnection implements ServiceConnection {

    private MQTTService mqttService;
    private IGetMessageCallBack IGetMessageCallBack;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mqttService = ((MQTTService.CustomBinder) iBinder).getService();
        mqttService.setIGetMessageCallBack(IGetMessageCallBack);
        if (!mqttService.isConnected()) {
            SharedPreferences sPreferences = mqttService.getSharedPreferences("STATE", Context.MODE_MULTI_PROCESS);
            String mqttIP = sPreferences.getString("mqttIP", "192.168.0.1");
            int mqttPort = sPreferences.getInt("mqttPort", 1883);
            String mqttTopic = sPreferences.getString("mqttTopic", "/TOILET");
            MQTTService.doClientConnection(mqttIP, mqttPort, mqttTopic);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

//    public MQTTService getMqttService() {
//        return mqttService;
//    }

    public void setIGetMessageCallBack(IGetMessageCallBack IGetMessageCallBack) {
        this.IGetMessageCallBack = IGetMessageCallBack;
    }
}
