package pt.picaponto.app.Api;

import android.app.Application;
import android.content.Context;

public class PicaPonto extends Application {
    private static PicaPonto instance;

    public static PicaPonto getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}