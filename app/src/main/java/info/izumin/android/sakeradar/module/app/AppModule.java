package info.izumin.android.sakeradar.module.app;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by izumin on 10/22/15.
 */
@Module
public class AppModule {
    public static final String TAG = AppModule.class.getSimpleName();

    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    Context provideApplicationContext() {
        return mApplication.getApplicationContext();
    }
}
