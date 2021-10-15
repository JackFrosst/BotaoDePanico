package botaopanico.principal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RecebeAlertaBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context.getApplicationContext(), RecebeAlertaThread.class);
            pushIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(pushIntent);
        }
    }


}
