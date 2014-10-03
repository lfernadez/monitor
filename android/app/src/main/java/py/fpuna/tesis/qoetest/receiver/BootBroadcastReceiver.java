package py.fpuna.tesis.qoetest.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import py.fpuna.tesis.qoetest.services.MonitoringService;

public class BootBroadcastReceiver extends BroadcastReceiver {
    public BootBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent intentService = new Intent(context, MonitoringService.class);
            context.startService(intentService);
        }
    }
}
