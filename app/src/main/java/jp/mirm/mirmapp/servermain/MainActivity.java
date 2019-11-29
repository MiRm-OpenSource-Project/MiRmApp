package jp.mirm.mirmapp.servermain;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import jp.mirm.mirmapp.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback, MainContract.View {

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);
        presenter.initialize();
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.selectOptionItem(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        presenter.selectItem(item.getItemId());
        return true;
    }

    @Override
    public void onSuperOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    public MainActivity getActivity() {
        return this;
    }
}
