package com.a279.siemens.mydiary;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.a279.siemens.mydiary.fragments.f_add_item;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initNavigationDraver();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initNavigationDraver() {
        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, toolbar, R.string.nav_open, R.string.nav_close);
        dl.setDrawerListener(toggle);
        toggle.syncState();
    }
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mAdd:
                        //Toast.makeText(MainActivity.this, "Add", Toast.LENGTH_SHORT).show();
                        setFragment(f_add_item.class, null);
                        break;
                    case R.id.mSettings:
                        Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        toolbar.inflateMenu(R.menu.menu_toolbar);
        toolbar.setTitle("MyDiary");
    }
    public void setFragment(Class clas, Bundle bundle) {
        //Class fragmentClass = clas;
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
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
