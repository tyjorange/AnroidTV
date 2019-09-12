package sh.slst.anroidtv.bean;

/**
 * Created by liuchengran on 2018/6/22.
 * <p>
 * 设备与平面图位置对应
 * "code":"26AAA114004B1200",
 * "x":"0",
 * "y":"0",
 * "state":"0",
 */
public class DeviceSignalInfo {

    public String signal;//6000 蹲位状态变化  6001 访问人数统计
    public int visitors;

    public String code;
    public float x;
    public float y;

    public int state;
    public int classs;

    public DeviceSignalInfo() {
    }

//    public DeviceSignalInfo(String signal, int visitors, String code, float x, float y, int state, int classs) {
//        this.signal = signal;
//        this.visitors = visitors;
//        this.code = code;
//        this.x = x;
//        this.y = y;
//        this.state = state;
//        this.classs = classs;
//    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public int getVisitors() {
        return visitors;
    }

    public void setVisitors(int visitors) {
        this.visitors = visitors;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getClasss() {
        return classs;
    }

    public void setClasss(int classs) {
        this.classs = classs;
    }

    public static int STATE_LEAVE = 0; //离开
    public static int STATE_ON = 1;  //占用
    public static int SQUETT_CLEAN = 2;  //清洁
    public static int SQUETT_REPAIR = 3;  //维修
    public static int SQUETT_FAULT = 4;  //故障

    public static int SIT_ON = 5;   //坐便
    public static int SQUETT_TOILETS = 6;  //蹲便
    public static int SQUETT_SPECIAL = 7;  //第三卫生间
}
