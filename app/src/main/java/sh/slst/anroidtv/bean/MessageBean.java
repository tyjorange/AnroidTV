package sh.slst.anroidtv.bean;

import java.util.List;

/**
 * 蹲位消息
 * <p>
 * {"closet":[{"closet_id":"hunan_baoqing_nan_nv09","state":"1","time":"yyyy-mm-dd hh:mm:ss"}]}
 * <p>
 * 日人流统计消息
 * <p>
 * {"traffic":{"count":"21","time":"yyyy-mm-dd hh:mm:ss"}}
 */
public class MessageBean {
    public List<Closet> closet;
    public Traffic traffic;

    public class Closet {
        public String closet_id;
        public int state;
        public String time;
    }

    public class Traffic {
        public int count;
        public String time;
    }
}
