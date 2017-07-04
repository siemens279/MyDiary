package com.a279.siemens.mydiary.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.a279.siemens.mydiary.Diar;
import com.a279.siemens.mydiary.MainActivity;
import com.a279.siemens.mydiary.MyDBHelper;
import com.a279.siemens.mydiary.R;

public class f_add_item extends Fragment implements Toolbar.OnMenuItemClickListener {

    EditText etTema, etText;
    TextView tvDate;
    Button bAdd;
    MyDBHelper db;
    FloatingActionButton fab;
    Boolean action = true;
    MenuItem addMenuItem, saveMenuItem, settingsMenuItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_add_item, container, false);
        db = new MyDBHelper(getContext());
        etTema = (EditText) view.findViewById(R.id.editTextTema);
        etText = (EditText) view.findViewById(R.id.editTextText);
        tvDate = (TextView) view.findViewById(R.id.textViewDate);
        bAdd = (Button) view.findViewById(R.id.button);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Diar recieveDiar = (Diar) bundle.getSerializable("diar");
            etTema.setText(recieveDiar.getTema());
            etText.setText(recieveDiar.getText());
            tvDate.setText(recieveDiar.getDate());
            bAdd.setVisibility(View.GONE);
            fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_pencil));
            fab.setVisibility(View.VISIBLE);
            action = false;
        }

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Diar diar = new Diar();
                diar.setTema(etTema.getText().toString());
                diar.setText(etText.getText().toString());
                diar.setDate(String.valueOf(System.currentTimeMillis()));
                db.addDiary(diar);
                //Toast.makeText(getContext(), "Size:"+db.getCountDiar(), Toast.LENGTH_SHORT).show();
                setFragment(f_show_oll.class, null);
            }
        });

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        toolbar.setTitle("Add");
        toolbar.setOnMenuItemClickListener(this);

        DrawerLayout dl = (DrawerLayout) getActivity().findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), dl, toolbar, R.string.nav_open, R.string.nav_close);
        dl.setDrawerListener(toggle);
        toggle.syncState();

        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_toolbar, menu);
        addMenuItem = menu.findItem(R.id.mAdd);
        saveMenuItem = menu.findItem(R.id.mSave);
        settingsMenuItem = menu.findItem(R.id.mSettings);
        addMenuItem.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
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
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment, clas.getSimpleName());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.mSave:
                Log.d("MyLog", "----mSave-----");
                return true;
            case R.id.mSettings:
                Log.d("MyLog", "----mSettings----");
                return true;
        }
        return false;
    }
}
