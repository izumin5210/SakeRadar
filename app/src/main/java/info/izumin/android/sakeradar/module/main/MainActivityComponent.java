package info.izumin.android.sakeradar.module.main;

import javax.inject.Singleton;

import dagger.Component;
import info.izumin.android.sakeradar.module.app.AppComponent;

/**
 * Created by izumin on 10/22/15.
 */
@Component(
        modules = MainActivityModule.class,
        dependencies = AppComponent.class
)
@Singleton
interface MainActivityComponent {
    void inject(MainActivity activity);

    MainActivityHelper helper();
}
