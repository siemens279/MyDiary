package com.a279.siemens.mydiary.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a279.siemens.mydiary.Diar;
import com.a279.siemens.mydiary.MyDBHelper;
import com.a279.siemens.mydiary.R;
import com.a279.siemens.mydiary.User;
import com.a279.siemens.mydiary.adapters.ShowOllAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class f_show_oll extends Fragment {

    MyDBHelper db;
    RecyclerView rv;
    ShowOllAdapter adapter;
    ArrayList<Diar> diarList;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    LinearLayout llIn, llOut;
    TextView textSingIn, textRegistration, textInName, textInEmail, textInOut;
    DrawerLayout dl;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_show_oll, container, false);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        drawableToggle();

        NavigationView nv = (NavigationView) getActivity().findViewById(R.id.navigation);
        View header = nv.getHeaderView(0);
        llIn = (LinearLayout) header.findViewById(R.id.llSingIn);
        textSingIn = (TextView) header.findViewById(R.id.textViewIn);
        textRegistration = (TextView) header.findViewById(R.id.textViewReg);
        llOut = (LinearLayout) header.findViewById(R.id.llSingOut);
        textInName = (TextView) header.findViewById(R.id.textViewOutName);
        textInEmail = (TextView) header.findViewById(R.id.textViewOutEmail);
        textInOut = (TextView) header.findViewById(R.id.textViewOut);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //Log.d("MyLog", "onAuthStateChanged:signed_in:" + user.getUid());
                    llIn.setVisibility(View.INVISIBLE);
                    llOut.setVisibility(View.VISIBLE);
                    getName();
                    textInEmail.setText(user.getEmail());
                } else {
                    //Log.d("MyLog", "onAuthStateChanged:signed_out");
                    llOut.setVisibility(View.INVISIBLE);
                    llIn.setVisibility(View.VISIBLE);
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

        textSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.closeDrawers();
                setFragment(f_sign_in.class, null);
            }
        });
        textRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.closeDrawers();
                setFragment(f_registration.class, null);
            }
        });
        textInOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.closeDrawers();
                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setTitle("Выйти с аккаунта").setCancelable(true)
                        .setMessage("Вы действительно хотите выйти с аккаунта?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAuth.signOut();
                                Toast.makeText(getContext(), "Вы успешно вышли", Toast.LENGTH_SHORT).show();
                                llOut.setVisibility(View.INVISIBLE);
                                llIn.setVisibility(View.VISIBLE);
                            }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alert = b.create();
                alert.show();
            }
        });

        db = new MyDBHelper(getContext());
        rv = (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        diarList = new ArrayList<>(db.getAllDiar());
        adapter = new ShowOllAdapter(diarList, new ShowOllAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.linearLayoutItem:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("diar", diarList.get(position));
                        setFragment(f_add_item.class, bundle);
                        break;
                }
            }
        });
        rv.setAdapter(adapter);
        return view;
    }
    public void getName() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        Query myTopPostsQuery = myRef.child("users").child(user.getUid());
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                //Log.d("MyLog", "Result:"+u.getName());
                if (u != null) textInName.setText(u.getName());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("MyLog", "Error");
            }
        });
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
}
