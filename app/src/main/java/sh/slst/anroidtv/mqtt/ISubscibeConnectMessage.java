package sh.slst.anroidtv.mqtt;

/**
 * Created by liuchengran on 2018/6/25.
 */

public interface ISubscibeConnectMessage {
    void onMessage(String message);
}
