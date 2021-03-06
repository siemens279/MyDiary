package com.a279.siemens.mydiary.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.a279.siemens.mydiary.Diar;
import com.a279.siemens.mydiary.MyDBHelper;
import com.a279.siemens.mydiary.R;
import com.a279.siemens.mydiary.SaveImage;
import com.a279.siemens.mydiary.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class f_settings extends Fragment {

    DrawerLayout dl;
    Toolbar toolbar;
    MyDBHelper db;
    ImageView syncToServer, syncDownload;
    FirebaseAuth myAuth;
    DatabaseReference myRef;
    FirebaseDatabase myFirebase;
    FirebaseUser user;
    ArrayList<Diar> diars;
    private StorageReference mStorageRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_settings, container, false);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        drawableToggle();

        myAuth = FirebaseAuth.getInstance();
        myFirebase = FirebaseDatabase.getInstance();
        myRef = myFirebase.getReference("diars");
        user = myAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        db = new MyDBHelper(getContext());
        diars = new ArrayList<>(db.getAllDiar());

        syncToServer = (ImageView) view.findViewById(R.id.imageSyncToServer);
        syncToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    Query myQuery = myRef.child(user.getUid()).orderByChild("id");//.equalTo(diars.get(i).getId());
//                    myQuery.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            Log.d("MyLog", "onDataChange");
//                            for (DataSnapshot child: dataSnapshot.getChildren()) {
//                                Diar diar = child
//                            }
//                            for (int i = 0; i < diars.size(); i++) {
//                            }
//                        }
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            Log.d("MyLog", "onCancelled");}
//                    });
                    //Log.d("MyLog", "Oll diars:"+diars.size());
                    for (int i = 0; i < diars.size(); i++) {
                        //Log.d("MyLog", "-------------------------load diar "+i);
                        saveDiar(diars.get(i));
                    }
                    Toast.makeText(getContext(), "Записи сохранены", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),"Войдите или зарегистрируйтесь", Toast.LENGTH_SHORT).show();
                }
            }
        });
        syncDownload = (ImageView) view.findViewById(R.id.imageSyncDownload);
        syncDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDiar();
            }
        });

        return view;
    }
    public void saveDiar(Diar diar) {
        if (user != null) {
            Map<String, Object> postValues = diar.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put(diar.getId(), postValues);
            myRef.child(user.getUid()).updateChildren(childUpdates);
            if (diar.getImgArray()!=null) {
                //Log.d("MyLog", "image in diar: "+diar.getImgArray().size());
                for (int i=0;i<diar.getImgArray().size();i++) {
                    Uri file = Uri.fromFile(new File(getContext().getFilesDir(), diar.getImgArray().get(i)));
                    StorageReference storageRef = mStorageRef.child("images/"+user.getUid()+"/"+diar.getId()+"/"+diar.getImgArray().get(i));
                    UploadTask uploadTask = storageRef.putFile(file);
                    // устанавливаем слушатель и получаем снэпшот с информацией
                    uploadTask.addOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Object o) {
                            UploadTask.TaskSnapshot snapshot = (UploadTask.TaskSnapshot) o;
                            //Log.d("MyLog", snapshot.toString());
                        }
                    });
                }
            }
        }
    }
    public void loadDiar() {
        if (user!=null) {
            Query myQuery = myRef.child(user.getUid());
            myQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int count = 0;
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Diar diar = child.getValue(Diar.class);
                        if (diar != null) {
                            GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                            ArrayList<String> yourStringArray = child.child("img").getValue(t);
                            diar.setImgArray(yourStringArray);
                            if (!db.findDiarById(diar.getId())) {
                                db.addDiar(diar);
                                count++;
                            }
                        }
                    }
                    Toast.makeText(getContext(), "Загружено: " + count, Toast.LENGTH_SHORT).show();
                    loadImageFromServer();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("MyLog", "Error");
                }
            });
        } else {
            Toast.makeText(getContext(),"Войдите или зарегистрируйтесь", Toast.LENGTH_SHORT).show();
        }
    }
    public void loadImageFromServer() {
        final ArrayList<Diar> diarList = new ArrayList<>(db.getAllDiar());
        final SaveImage si = new SaveImage(getContext());
        if (diarList != null) {
            for (int i=0;i<diarList.size();i++) {
                if (diarList.get(i).getImgArray()!=null) {
                    for (int y=0;y<diarList.get(i).getImgArray().size();y++) {
                        final String name = diarList.get(i).getImgArray().get(y);
                        StorageReference mountainsRef = mStorageRef.child("images/" + user.getUid() + "/" + diarList.get(i).getId() + "/" + diarList.get(i).getImgArray().get(y));
                        //Log.d("MyLog", "-"+ mountainsRef.getPath());
                        // вызываем метод getDownloadUrl на файл и устаналиваем слушатель (прошло успешно или ошибка)
                        mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Uri uri = (Uri) o;
                                // получаем downloadUrl для 'users/me/profile.png'
                                Log.d("MyLog", "URi: " + uri.toString());
                                //Log.d("MyLog", "URi: " + uri.getLastPathSegment());
                                si.saveUri(uri.toString(), name);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.d("MyLog", "Ошибка: " + exception);
                            }
                        });
                    }
                } else Log.d("MyLog", "No Image in this diar");
            }
        } else Log.d("MyLog", "Polnaya hren");
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
