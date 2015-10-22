package info.izumin.android.sakeradar.module.app;

import android.content.Context;

import dagger.Component;
import info.izumin.android.sakeradar.App;

/**
 * Created by izumin on 10/22/15.
 */
@Component(
        modules = AppModule.class
)
public interface AppComponent {
    void inject(App app);

    Context context();
}
