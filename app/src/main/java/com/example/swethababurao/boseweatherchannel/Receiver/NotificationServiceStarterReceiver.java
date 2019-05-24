package com.example.swethababurao.boseweatherchannel.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationServiceStarterReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationEventReceiver.setupAlarm(context);
    }
}
