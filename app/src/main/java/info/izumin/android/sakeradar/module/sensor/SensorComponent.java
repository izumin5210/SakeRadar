package info.izumin.android.sakeradar.module.sensor;

import javax.inject.Singleton;

import dagger.Component;
import info.izumin.android.sakeradar.module.app.AppComponent;
import info.izumin.android.sakeradar.module.main.MainActivityHelper;

/**
 * Created by izumin on 10/22/15.
 */
@Component(
        modules = SensorModule.class,
        dependencies = AppComponent.class
)
@Singleton
public interface SensorComponent {
    void inject(MainActivityHelper helper);

    Accelerometer accelerometer();
    MagneticFieldMeter magneticFieldMeter();
}
