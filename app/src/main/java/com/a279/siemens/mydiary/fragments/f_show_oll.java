package com.a279.siemens.mydiary.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a279.siemens.mydiary.Diar;
import com.a279.siemens.mydiary.MyDBHelper;
import com.a279.siemens.mydiary.R;
import com.a279.siemens.mydiary.adapters.ShowOllAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_show_oll, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        dl = (DrawerLayout) getActivity().findViewById(R.id.drawerlayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), dl, toolbar, R.string.nav_open, R.string.nav_close);
        dl.setDrawerListener(toggle);
        toggle.syncState();

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
                    Log.d("MyLog", "onAuthStateChanged:signed_in:" + user.getUid());
                    llIn.setVisibility(View.INVISIBLE);
                    llOut.setVisibility(View.VISIBLE);
                    textInEmail.setText(user.getEmail());
                } else {
                    Log.d("MyLog", "onAuthStateChanged:signed_out");
                    llOut.setVisibility(View.INVISIBLE);
                    llIn.setVisibility(View.VISIBLE);
                }
                // ...
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
                //setFragment(f_registration.class, null);
                mAuth.signOut();
                Toast.makeText(getContext(), "Вы успешно вышли", Toast.LENGTH_SHORT).show();
                llOut.setVisibility(View.INVISIBLE);
                llIn.setVisibility(View.VISIBLE);
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
}
