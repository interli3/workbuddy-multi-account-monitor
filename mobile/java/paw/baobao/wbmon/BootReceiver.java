package io.github.workbuddymonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context c, Intent i) {
        Scheduler.schedule(c);
        WbWidget.pushAll(c);
    }
}
