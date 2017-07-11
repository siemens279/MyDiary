package com.a279.siemens.mydiary.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import com.a279.siemens.mydiary.R;
import com.a279.siemens.mydiary.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class f_accaunt extends Fragment{

    private FirebaseAuth mAuth;
    DatabaseReference myRef;
    FirebaseUser user;
    FirebaseDatabase database;
    EditText etName, etFam, etAge;
    Button save;
    DrawerLayout dl;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_accaunt, container, false);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        drawableToggle();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        user = mAuth.getInstance().getCurrentUser();
        if (user != null ) loadUser();
        etName = (EditText) view.findViewById(R.id.editTextAccauntName);
        etFam = (EditText) view.findViewById(R.id.editTextAccauntFam);
        etAge = (EditText) view.findViewById(R.id.editTextAccauntAge);
        save = (Button) view.findViewById(R.id.buttonSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUser();
            }
        });
        return view;
    }
    public void drawableToggle() {
        dl = (DrawerLayout) getActivity().findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), dl, toolbar, R.string.nav_open, R.string.nav_close) {
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                hideKeyBoard();
                super.onDrawerOpened(drawerView);
            }
        };
        dl.setDrawerListener(toggle);
        toggle.syncState();
    }
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public void loadUser() {
        Query myTopPostsQuery = myRef.child("users").child(user.getUid());
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                //Log.d("MyLog", "Result:"+u.getName());
                if (u != null) {
                    etName.setText(u.getName());
                    etFam.setText(u.getFam());
                    etAge.setText(u.getAge());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("MyLog", "Error");
            }
        });
    }
    public void saveUser() {
        if (user != null) {
            User u = new User(user.getUid(), etName.getText().toString(), etFam.getText().toString(), etAge.getText().toString());
            Map<String, Object> postValues = u.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(u.getId(), postValues);
            myRef.child("users").updateChildren(childUpdates);
        }
    }
}
