package com.a279.siemens.mydiary.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a279.siemens.mydiary.MyDBHelper;
import com.a279.siemens.mydiary.R;

public class f_show_oll extends Fragment {

    MyDBHelper db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_show_oll, container, false);
        db = new MyDBHelper(getContext());
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv);

        return view;
    }
}
