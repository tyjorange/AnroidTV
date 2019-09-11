package sh.slst.anroidtv;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {

    private String TAG = "BootBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive Boot");
        Intent bootIntent = new Intent(context, MainNanActivity.class);
        bootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(bootIntent);
    }
}
