package info.izumin.android.sakeradar.module.main;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by izumin on 10/22/15.
 */
@Module
public class MainActivityModule {
    public static final String TAG = MainActivityModule.class.getSimpleName();

    private final MainActivity mActivity;

    public MainActivityModule(MainActivity activity) {
        mActivity = activity;
    }

    @Provides
    @Singleton
    MainActivityHelper provideMainActivityHelper() {
        return new MainActivityHelper(mActivity);
    }
}
