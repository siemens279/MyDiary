package com.a279.siemens.mydiary.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.a279.siemens.mydiary.Diar;
import com.a279.siemens.mydiary.MyDBHelper;
import com.a279.siemens.mydiary.R;

public class f_add_item extends Fragment {

    EditText etTema, etText;
    TextView tvDate;
    Button bAdd;
    MyDBHelper db;
    FloatingActionButton fab;

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
            //fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_pencil));
            fab.setVisibility(View.VISIBLE);
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

        return view;
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
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
