package info.izumin.android.sakeradar.module.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import info.izumin.android.sakeradar.App;
import info.izumin.android.sakeradar.R;
import info.izumin.android.sakeradar.module.app.AppComponent;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Inject MainActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupComponent(App.getApp(this).getComponent());
        mHelper.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHelper.onResume();
    }

    @Override
    protected void onStop() {
        mHelper.onStop();
        super.onStop();
    }

    private void setupComponent(AppComponent component) {
        DaggerMainActivityComponent.builder()
                .appComponent(component)
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
    }
}
