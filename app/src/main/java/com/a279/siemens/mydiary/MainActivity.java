package com.a279.siemens.mydiary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a279.siemens.mydiary.fragments.f_add_item;
import com.a279.siemens.mydiary.fragments.f_registration;
import com.a279.siemens.mydiary.fragments.f_settings;
import com.a279.siemens.mydiary.fragments.f_show_oll;
import com.a279.siemens.mydiary.fragments.f_sign_in;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.a279.siemens.mydiary.R.id.fab;
import static com.a279.siemens.mydiary.R.id.toolbar;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout dl;
    private MenuItem addMenuItem;
    private MenuItem saveMenuItem;
    private MenuItem settingsMenuItem;
    private MenuItem deleteMenuItem;
    long back_pressed = 0;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    LinearLayout llIn, llOut;
    TextView textSingIn, textRegistration, textInName, textInEmail, textInOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("MyDiary");
        setSupportActionBar(toolbar);
        togle();
        setFragment(f_show_oll.class, null);

        NavigationView nv = (NavigationView) findViewById(R.id.navigation);
//        View header = nv.getHeaderView(0);
//        llIn = (LinearLayout) header.findViewById(R.id.llSingIn);
//        textSingIn = (TextView) header.findViewById(R.id.textViewIn);
//        textRegistration = (TextView) header.findViewById(R.id.textViewReg);
//        llOut = (LinearLayout) header.findViewById(R.id.llSingOut);
//        textInName = (TextView) header.findViewById(R.id.textViewOutName);
//        textInEmail = (TextView) header.findViewById(R.id.textViewOutEmail);
//        textInOut = (TextView) header.findViewById(R.id.textViewOut);
        //llIn.setVisibility(View.INVISIBLE);
        //llOut.setVisibility(View.INVISIBLE);

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
                        //setFragment(f_accaunt.class, null);
                        break;
                    case R.id.nSettings:
                        setFragment(f_settings.class, null);
                        break;
                }
                return true;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //Log.d("MyLog", "onAuthStateChanged:signed_in:" + user.getUid());
                    //llIn.setVisibility(View.INVISIBLE);
                    //textInEmail.setText(user.getEmail());
                } else {
                    //Log.d("MyLog", "onAuthStateChanged:signed_out");
                    //llOut.setVisibility(View.INVISIBLE);
                }
                // ...
            }
        };
        mAuth.addAuthStateListener(mAuthListener);


//        textSingIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dl.closeDrawers();
//                setFragment(f_sign_in.class, null);
//            }
//        });
//        textRegistration.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dl.closeDrawers();
//                setFragment(f_registration.class, null);
//            }
//        });
//        textInOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dl.closeDrawers();
//                //setFragment(f_registration.class, null);
//                mAuth.signOut();
//                Toast.makeText(getApplicationContext(), "Вы успешно вышли", Toast.LENGTH_SHORT).show();
//                llOut.setVisibility(View.INVISIBLE);
//                llIn.setVisibility(View.VISIBLE);
//            }
//        });

    }
    public void togle() {
        dl = (DrawerLayout) findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, toolbar, R.string.nav_open, R.string.nav_close);
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
                                setSupportActionBar(toolbar);
                                togle();
                                break;
                            case "f_settings":
                                //Log.d("MyLog", "f_settings");
                                setSupportActionBar(toolbar);
                                togle();
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
}
