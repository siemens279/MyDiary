package com.a279.siemens.mydiary.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a279.siemens.mydiary.Diar;
import com.a279.siemens.mydiary.MyDBHelper;
import com.a279.siemens.mydiary.R;
import com.a279.siemens.mydiary.adapters.ShowOllAdapter;

import java.util.ArrayList;

public class f_show_oll extends Fragment {

    MyDBHelper db;
    RecyclerView rv;
    ShowOllAdapter adapter;
    ArrayList<Diar> diarList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_show_oll, container, false);
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
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
