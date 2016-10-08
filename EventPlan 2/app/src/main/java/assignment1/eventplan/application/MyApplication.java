package assignment1.eventplan.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.MapsInitializer;

import assignment1.eventplan.services.ContactBackupService;


public class MyApplication extends Application {
    static volatile MyApplication mInstance;


    /**
     * @return MyApplication
     */
    public static MyApplication get() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if (isMyApplicationProcess(this)) {
            startService(new Intent(this, ContactBackupService.class));
            final int connectCode = MapsInitializer.initialize(this);
            Log.e("MapsInitializer", "isConnected:" + (connectCode == ConnectionResult.SUCCESS) + ",code:" + connectCode);
        }

    }


    public static boolean isMyApplicationProcess(Context context) {
        return context.getPackageName().equals(fetchMyPid(context));
    }

    @NonNull
    public static String fetchMyPid(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }
}
