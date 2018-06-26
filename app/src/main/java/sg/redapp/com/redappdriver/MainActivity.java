package sg.redapp.com.redappdriver;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.support.design.widget.NavigationView;
import android.widget.CompoundButton;
import android.widget.TextView;

import sg.redapp.com.redappdriver.HomeFragments.FAQ;
import sg.redapp.com.redappdriver.HomeFragments.History;
import sg.redapp.com.redappdriver.HomeFragments.Home;
import sg.redapp.com.redappdriver.HomeFragments.Settings;
import sg.redapp.com.redappdriver.HomeFragments.Support;
import sg.redapp.com.redappdriver.HomeFragments.Wallet;
import sg.redapp.com.redappdriver.login.ActivityStartPage;


public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    FragmentTransaction ft;
    Toolbar toolbar;
    TextView toolbartitle;
    TextView onlineStatus;
    SwitchCompat setOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureNavigationDrawer();
        configureToolbar();
        configureSwitch();
        toolbartitle = findViewById(R.id.toolbartitle);
        setTitle(getString(R.string.app_logo_name));
        if (savedInstanceState == null) {
            replacefragment(new Home());
            setTitle(getString(R.string.navbar_home));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void configureSwitch(){
        onlineStatus = findViewById(R.id.onlineStatus);
        setOnline = findViewById(R.id.setOnline);
        setOnline.setChecked(false);
        if(setOnline.isChecked()){
            onlineStatus.setText("online\u2022 ");
            onlineStatus.setTextColor(getResources().getColor(R.color.swampgreen));
            Log.e("Online Status: ","Online");
        }else{
            onlineStatus.setText("offline\u2022 ");
            onlineStatus.setTextColor(getResources().getColor(R.color.darkgrey));
            Log.e("Online Status: ","Offline\u2022 ");
        }
        setOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(setOnline.isChecked()){
                    onlineStatus.setText("online\u2022 ");
                    onlineStatus.setTextColor(getResources().getColor(R.color.swampgreen));
                    Log.e("Online Status: ","Online");
                }else{
                    onlineStatus.setText("offline\u2022 ");
                    onlineStatus.setTextColor(getResources().getColor(R.color.darkgrey));
                    Log.e("Online Status: ","Offline");
                }
            }
        });

    }
    private void configureNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.left_drawer);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId){
                    case R.id.home:
                        replacefragment(new Home());
                        setTitle("Home");
                        CloseDrawer();
                        return true;
                    case R.id.wallet:
                        replacefragment(new Wallet());
                        setTitle("Wallet");
                        CloseDrawer();
                        return true;
                    case R.id.job_history:
                        replacefragment(new History());
                        setTitle("Job History");
                        CloseDrawer();
                        return true;
                    case R.id.settings:
                        replacefragment(new Settings());
                        setTitle("Settings");
                        CloseDrawer();
                        return true;
                    case R.id.faq:
                        replacefragment(new FAQ());
                        setTitle("FAQ");
                        CloseDrawer();
                        return true;
                    case R.id.support:
                        replacefragment(new Support());
                        setTitle("Support");
                        CloseDrawer();
                        return true;
                    case R.id.logout:
                        startActivity(new Intent(MainActivity.this, ActivityStartPage.class));
                        finish();
                        return true;
                }
            return false;
            }
        });
//        navView.setNavigationItemSelectedListener(menuItem -> {
//            int itemId = menuItem.getItemId();
//            return false;
//        });

    }
    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            // Android home
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    CloseDrawer();
                    return true;
                } else {
                    OpenDrawer();
                    return true;
                }
                // manage other entries if you have it ...
        }
        return false;
    }
    public void replacefragment(Fragment fragment) {
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, fragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }
    public void CloseDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    public void OpenDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void setTitle(String title){
        toolbartitle.setText(title);
    }
}
 