package info.izumin.android.sakeradar;

import android.app.Application;
import android.content.Context;

import info.izumin.android.sakeradar.module.app.AppComponent;
import info.izumin.android.sakeradar.module.app.AppModule;
import info.izumin.android.sakeradar.module.app.DaggerAppComponent;

/**
 * Created by izumin on 10/22/15.
 */
public class App extends Application {
    public static final String TAG = App.class.getSimpleName();

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setupGraph();
    }

    private void setupGraph() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        mAppComponent.inject(this);
    }

    public AppComponent getComponent() {
        return mAppComponent;
    }

    public static App getApp(Context context) {
        return (App) context.getApplicationContext();
    }
}
