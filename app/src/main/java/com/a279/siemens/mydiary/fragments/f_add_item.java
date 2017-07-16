package com.a279.siemens.mydiary.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a279.siemens.mydiary.Diar;
import com.a279.siemens.mydiary.MainActivity;
import com.a279.siemens.mydiary.MyDBHelper;
import com.a279.siemens.mydiary.R;
import com.a279.siemens.mydiary.SaveImage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class f_add_item extends Fragment implements Toolbar.OnMenuItemClickListener {

    EditText etTema, etText;
    TextView tvDate;
    MyDBHelper db;
    //FloatingActionButton fab;
    MenuItem addMenuItem, saveMenuItem, settingsMenuItem, deleteMenuItem;
    Diar recieveDiar = null;
    Bundle bundle;
    DrawerLayout dl;
    Toolbar toolbar;
    LinearLayout llAdd;
    Button bAddImage;
    LinearLayout.LayoutParams lpView;
    SaveImage si;
    public ArrayList<String> imgArray = new ArrayList<>();

    private ImageView image;
    private static final int REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_add_item, container, false);
        db = new MyDBHelper(getContext());
        etTema = (EditText) view.findViewById(R.id.editTextTema);
        etText = (EditText) view.findViewById(R.id.editTextText);
        tvDate = (TextView) view.findViewById(R.id.textViewDate);
        //fab = (FloatingActionButton) view.findViewById(R.id.fab);
        llAdd = (LinearLayout) view.findViewById(R.id.linearlayoutAddImage);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        toolbar.setTitle("Add");
        toolbar.setOnMenuItemClickListener(this);
        drawableToggle();

        lpView = new LinearLayout.LayoutParams(200, LinearLayout.LayoutParams.MATCH_PARENT);
        lpView.leftMargin = 5;
        si = new SaveImage(getContext());

        bundle = getArguments();
        if (bundle != null) {
            recieveDiar = (Diar) bundle.getSerializable("diar");
            etTema.setText(recieveDiar.getTema());
            etText.setText(recieveDiar.getText());
            tvDate.setText(formatDate(Long.parseLong(recieveDiar.getDate())));
            //fab.setImageDrawable(getResources().getDrawable(R.mipmap.ic_pencil));
            //fab.setVisibility(View.VISIBLE);
            if (recieveDiar.getImgArray()!=null) {
                //Log.d("MyLog", "image in bd:"+recieveDiar.getImgArray().size());
                for (int i=0; i<recieveDiar.getImgArray().size(); i++) {
                    ImageView iv = new ImageView(getContext());
                    llAdd.addView(iv, lpView);
                    iv.setImageBitmap(si.load(recieveDiar.getImgArray().get(i)));
                    imgArray = recieveDiar.getImgArray();
                }
            } //else Log.d("MyLog", "No image in bd");
        } else tvDate.setText(formatDate(System.currentTimeMillis()));

        //image = (ImageView) view.findViewById(R.id.imageView2);
        bAddImage = (Button) view.findViewById(R.id.buttonAddImage);
        bAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, REQUEST);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Bitmap img = null;
        if (requestCode == REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                img = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView iv = new ImageView(getContext());
            llAdd.addView(iv, lpView);
            iv.setImageBitmap(img);

            long date = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
            String name = sdf.format(date);
            si.save(img, name);
            imgArray.add(name);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_toolbar, menu);
        addMenuItem = menu.findItem(R.id.mAdd);
        saveMenuItem = menu.findItem(R.id.mSave);
        settingsMenuItem = menu.findItem(R.id.mSettings);
        deleteMenuItem = menu.findItem(R.id.mDelete);
        addMenuItem.setVisible(false);
        if (bundle == null) deleteMenuItem.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.mSave:
                Diar diar = new Diar();
                diar.setTema(etTema.getText().toString());
                diar.setText(etText.getText().toString());
                diar.setDate(String.valueOf(System.currentTimeMillis()));
//                if (imgArray!=null) {
//                    for (int i=0;i<imgArray.size();i++) {
//                        Log.d("MyLog", "image"+i+" :"+imgArray.get(i));
//                    }
//                }
                diar.setImgArray(imgArray);
                if (recieveDiar!=null) {
                    if(db.findDiarById(recieveDiar.getId())) {
                        diar.setId(recieveDiar.getId());
                        db.updateDiarById(diar);
                        setFragment(f_show_oll.class, null);
                        Toast.makeText(getContext(), "Запись обновлена", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (etTema.getText().length()>0) {
                        if (etText.getText().length()>0) {
                            db.addDiar(diar);
                            setFragment(f_show_oll.class, null);
                            Toast.makeText(getContext(), "Запись сохранена", Toast.LENGTH_SHORT).show();
                        } else Toast.makeText(getContext(), "Запись пустая", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(getContext(), "Введите тему", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.mSettings:
                setFragment(f_settings.class, null);
                return true;
            case R.id.mDelete:
                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setTitle("Удаление").setCancelable(true)
                        .setMessage("Вы действительно хотите удалить эту дапись?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.deleteDiar(recieveDiar);
                                Toast.makeText(getContext(), "Запись удалена", Toast.LENGTH_SHORT).show();
                                setFragment(f_show_oll.class, null);

                                if (recieveDiar.getImgArray()!=null) {
                                    for (int i2=0; i2<recieveDiar.getImgArray().size(); i2++) {
                                        si.delete(recieveDiar.getImgArray().get(i2));
                                        Log.d("MyLog", "Delete "+i2);
                                    }
                                } //else Log.d("MyLog", "No image in bd");
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
                return true;
        }
        return false;
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
    public String formatDate(long l) {
        SimpleDateFormat form = new SimpleDateFormat("dd.MM.yyyy kk:mm");
        return form.format(l);
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
