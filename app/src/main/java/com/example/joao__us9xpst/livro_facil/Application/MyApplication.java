package com.example.joao__us9xpst.livro_facil.Application;

import android.app.Application;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.drawee.backends.pipeline.Fresco;

public class MyApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        //Facebook Login
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
