package sh.slst.anroidtv.bean;

/**
 * Created by liuchengran on 2018/6/22.
 * <p>
 * <p>
 * {
 * "ip":"192.168.0.1",
 * "port":"1883"
 * "dev":{
 * "fsu_code":"0101002",
 * "dev_code":"E1"     /datain/0101002/E1
 * }
 * }
 */

public class MQTTConfig {
    public String ip;
    public int port;
    public Device dev;
    public String tclass;

    public static class Device {
        public String fsu_code;
        public String dev_code;
    }
}
