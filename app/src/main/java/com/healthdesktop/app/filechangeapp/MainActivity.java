package com.healthdesktop.app.filechangeapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.utils.FileUtils;
import com.healthdesktop.app.filechangeapp.bean.VideoBean;

import org.litepal.LitePal;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.healthdesktop.app.filechangeapp.MyApplication.getListData;
import static com.healthdesktop.app.filechangeapp.MyApplication.orderByDate;
import static com.healthdesktop.app.filechangeapp.MyApplication.setListData;

public class MainActivity extends AppCompatActivity {

    EditText etFrom,etTo;

    CustomToast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFrom = (EditText) findViewById(R.id.et_from);
        etTo = (EditText) findViewById(R.id.et_to);

        etFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setListData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etFrom.setText(getListData());

        Intent intentt = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intentt.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intentt,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            toast = new CustomToast(this);
            toast.showToast();
        }
    }



    public void clear(View v){
//        FileUtils.deleteDir(fromPath);
        orderByDate();
    }

    public void remove(View v){
        List<File> mlist= FileUtils.listFilesInDir(getListData(),false);
        for (File f:mlist) {
            File ff = new File(etTo.getText().toString()+"/"+f.getName());
            Toast.makeText(MainActivity.this,""+f.renameTo(new File(etTo.getText().toString()+"/"+f.getName())),Toast.LENGTH_SHORT).show();
        }
    }

     public void inputdb(View v){
         File file = new File(getListData());
         File[] fs = file.listFiles();
         int num = 0;
         if (fs != null && fs.length > 0) {
             for (int i = 0; i < fs.length; i++) {
                 if (fs[i].getName().toLowerCase().endsWith(".mp4")) {
                     if(LitePal.where("videoid=?",fs[i].getName().substring(0,fs[i].getName().indexOf("."))).find(VideoBean.class).size()==0){
                         VideoBean vbItem = new VideoBean();
                         vbItem.setVideoid(fs[i].getName().substring(0,fs[i].getName().indexOf(".")));
                         vbItem.save();
                         num++;
                     }
                 }
             }
         }
         Toast.makeText(MainActivity.this,"入库："+num,Toast.LENGTH_SHORT).show();
     }

}
