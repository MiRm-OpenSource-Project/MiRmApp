package jp.mirm.mirmapp;

import android.app.Application;
import android.os.Bundle;

public class MainApplication extends Application {

    private static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static Application getInstance() {
        return application;
    }
}
