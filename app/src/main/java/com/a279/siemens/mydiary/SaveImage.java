package com.a279.siemens.mydiary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class SaveImage {

    public Context context;
    public String DIR_SD = "MyDiar";
    public SaveImage(Context con) {
        context = con;
    }

    public void save(Bitmap bitmap, String name) {
        try {
            //открываем файл в приватном каталоге нашей аппы
            OutputStream stream = context.openFileOutput(name, MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);// пишем битмап на PNG с качеством 70%
            //Bitmap myBitmap = BitmapFactory.decodeStream(input);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Bitmap load(String name) {
        Bitmap bmp = null;
        try {
            File file = new File(context.getFilesDir(), name); //+".png"
            InputStream is = new BufferedInputStream(new FileInputStream(file));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
            bmp = BitmapFactory.decodeStream(bufferedInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmp;
    }
    public void delete(String name) {
        context.deleteFile(name);
    }

    public void saveUri(String uri, String name) {
        MyTask mt = new MyTask();
        mt.execute(uri, name);
    }

    class MyTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //tvInfo.setText("Begin");
            Log.d("MyLog", "Start load");
        }
        @Override
        protected Void doInBackground(String... urls) {
            try {
                //int cnt = 0;
                //for (String url : urls) {
                    // загружаем файл
                Log.d("MyLog", "urls[0]:"+urls[0]);
                Log.d("MyLog", "urls[1]:"+urls[1]);
                    downloadFile(urls[0], urls[1]);
                    // выводим промежуточные результаты
                    //publishProgress(++cnt);
                //}
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //tvInfo.setText("Downloaded " + values[0] + " files");
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //tvInfo.setText("End");
            Log.d("MyLog", "End load");
        }

        private void downloadFile(String url, String name) throws InterruptedException {
            Bitmap img = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                img = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.d("MyLog", "Error:"+e.getMessage());
                //e.printStackTrace();
            }
            save(img, name);
        }
    }






    public void writeFile(String FILENAME, Bitmap bitmap) {
        File file = new File(Environment.getExternalStoragePublicDirectory("MyDiar"), FILENAME);
        try {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } finally {
                if (fos != null) fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void readFile(String FILENAME) {

        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FILENAME);
            InputStream is = new BufferedInputStream(new FileInputStream(file));
            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
            Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        try {
//            // открываем поток для чтения
//            BufferedReader br = new BufferedReader(new InputStreamReader(context.openFileInput(FILENAME)));
//            String str = "";
//            // читаем содержимое
//            while ((str = br.readLine()) != null) {
//                //Log.d(LOG_TAG, str);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    public void writeFileSD(String FILENAME_SD) {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            // пишем данные
            bw.write("Содержимое файла на SD");
            // закрываем поток
            bw.close();
            //Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readFileSD(String FILENAME_SD) {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                //Log.d(LOG_TAG, str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
