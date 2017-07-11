package com.a279.siemens.mydiary;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.a279.siemens.mydiary.fragments.f_accaunt;
import com.a279.siemens.mydiary.fragments.f_add_item;
import com.a279.siemens.mydiary.fragments.f_settings;
import com.a279.siemens.mydiary.fragments.f_show_oll;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout dl;
    private MenuItem addMenuItem;
    private MenuItem saveMenuItem;
    private MenuItem settingsMenuItem;
    private MenuItem deleteMenuItem;
    long back_pressed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("MyDiary");
        setSupportActionBar(toolbar);
        drawableToggle();
        setFragment(f_show_oll.class, null);

        NavigationView nv = (NavigationView) findViewById(R.id.navigation);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                dl.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nMain:
                        setFragment(f_show_oll.class, null);
                        break;
                    case R.id.nAdd:
                        setFragment(f_add_item.class, null);
                        break;
                    case R.id.nAccaunt:
                        setFragment(f_accaunt.class, null);
                        break;
                    case R.id.nSettings:
                        setFragment(f_settings.class, null);
                        break;
                }
                return true;
            }
        });

    }
    public void drawableToggle() {
        dl = (DrawerLayout) findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, toolbar, R.string.nav_open, R.string.nav_close) {
            @Override
            public void onDrawerClosed(View view) {
                //Log.d("MyLog", "00000000000000000000000000-");
                Toast.makeText(MainActivity.this, "Закрыто с главной активити", Toast.LENGTH_SHORT).show();
                super.onDrawerClosed(view);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                //Log.d("MyLog", "1111111111111111111111111111-");
                Toast.makeText(MainActivity.this, "Открыто с главной активити", Toast.LENGTH_SHORT).show();
                super.onDrawerOpened(drawerView);
            }
        };
        dl.setDrawerListener(toggle);
        toggle.syncState();
    }
    @Override
    public void onBackPressed() {
        String[] array = {"f_add_item","f_show_oll","f_settings"};
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        } else {
            Fragment main = getSupportFragmentManager().findFragmentByTag("f_show_oll");
            if (main.isVisible()) {
                if (back_pressed + 2000 > System.currentTimeMillis()) finish();
                else {
                    Toast.makeText(getBaseContext(), "Нажмите еще раз чтобы выйти", Toast.LENGTH_SHORT).show();
                    back_pressed = System.currentTimeMillis();
                }
            } else {
                super.onBackPressed();
                for (int i = 0; i < array.length; i++) {
                    Fragment frag = getSupportFragmentManager().findFragmentByTag(array[i]);
                    if (frag != null) {
                        switch (array[i]) {
                            case "f_add_item":
                                break;
                            case "f_show_oll":
                                //setSupportActionBar(toolbar);
                                //drawableToggle();
                                break;
                            case "f_settings":
                                //Log.d("MyLog", "f_settings");
                                //setSupportActionBar(toolbar);
                                //drawableToggle();
                                break;
                        }
                    }
                }
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.mAdd:
                 //Toast.makeText(MainActivity.this, "Add", Toast.LENGTH_SHORT).show();
                 setFragment(f_add_item.class, null);
                 return true;
            case R.id.mSave:
                 Toast.makeText(MainActivity.this, "Save", Toast.LENGTH_SHORT).show();
                 return true;
            case R.id.mSettings:
                 //Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                 setFragment(f_settings.class, null);
                 return true;
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        addMenuItem = menu.findItem(R.id.mAdd);
        saveMenuItem = menu.findItem(R.id.mSave);
        settingsMenuItem = menu.findItem(R.id.mSettings);
        deleteMenuItem = menu.findItem(R.id.mDelete);
        addMenuItem.setVisible(true);
        saveMenuItem.setVisible(false);
        settingsMenuItem.setVisible(true);
        deleteMenuItem.setVisible(false);
        return true;
    }
    public void setFragment(Class clas, Bundle bundle) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) clas.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bundle!=null) {
            fragment.setArguments(bundle);
        }
        if (fragment!=null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment, clas.getSimpleName());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
