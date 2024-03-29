package com.example.musicplay;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView listView;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView );

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        Toast.makeText(MainActivity.this, "Runtime Permission Granted", Toast.LENGTH_SHORT).show();
                        ArrayList<File> mySong = fetchSongs(Environment.getExternalStorageDirectory());
                        String [] items =  new String[mySong.size()];
                        for(int i=0;i<mySong.size();i++)
                        {
                            items[i] = mySong.get(i).getName().replace("mp3","");
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items);
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }
    public ArrayList<File> fetchSongs(File file)
    {
        ArrayList arrayList = new ArrayList();
        File [] songs = file.listFiles();
        if (songs != null)
        {
            for(File myFile: songs)
            {
                if(!myFile.isHidden() && myFile.isDirectory())
                {
                    arrayList.add(fetchSongs(myFile));
                }
                else
                {
                    if(myFile.getName().endsWith(".mp3") && !myFile.getName().startsWith("."))
                    {
                        arrayList.add(myFile);
                    }
                }
            }
        }
        return arrayList;
    }
}