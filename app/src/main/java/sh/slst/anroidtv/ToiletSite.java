package sh.slst.anroidtv;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by SH on 2018/3/1.
 */

public class ToiletSite {

    private int number;
    private boolean using;
    private int imageId;

    private static final int[] IDS = {
            R.mipmap.man_unused,
            R.mipmap.man_using,
            R.mipmap.woman_unused,
            R.mipmap.woman_using,
    };

    public ToiletSite(int number, int id) {
        this.number = number;
        this.imageId = IDS[id];
        this.using = id % 2 == 0;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isUsing() {
        return using;
    }

    public void setUsing(boolean using) {
        this.using = using;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public static List<ToiletSite> getTestData(int isMan) {
        List<ToiletSite> sites = new ArrayList<>();
        for (int i = 1; i < 35; i++) {
            sites.add(new ToiletSite(i, new Random().nextInt(2) + isMan*2));
        }
        return sites;
    }

}
