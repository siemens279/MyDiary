package com.a279.siemens.mydiary.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.a279.siemens.mydiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class f_registration extends Fragment{

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_registration, container, false);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        mAuth = FirebaseAuth.getInstance();
        //FirebaseUser user = mAuth.getInstance().getCurrentUser();
        //FirebaseDatabase db = FirebaseDatabase.getInstance();
        //myRef = db.getReference();

        final EditText etNane = (EditText) view.findViewById(R.id.editTextNane);
        final EditText etEmail = (EditText) view.findViewById(R.id.editTextEmail);
        final EditText etPas = (EditText) view.findViewById(R.id.editTextPassword);
        TextView tvRegistration = (TextView) view.findViewById(R.id.textViewResistration);

        tvRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etEmail.getText().length()>0) {
                    if (etPas.getText().length()>0) {
                        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPas.getText().toString()).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Log.d("MyLog", "createUserWithEmail:onComplete:" + task.isSuccessful());
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.

                                Toast.makeText(getContext(), "Вы успешно зарегистрировались", Toast.LENGTH_SHORT).show();
                                setFragment(f_show_oll.class, null);
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                                }
                                // ...
                            }
                        });
                    } else Toast.makeText(getContext(), "Введите пароль", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(getContext(), "Введите ваш емейл", Toast.LENGTH_SHORT).show();
            }
        });

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
