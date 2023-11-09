package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SelectListener{
    RecyclerView recyclerView;
    List<File> fileList;
    File path = new File(Environment.getExternalStorageDirectory(), "Download");

    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermission();
    }
    public void askPermission(){
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE   )
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displayFiles();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(MainActivity.this, "Storage Permission is needed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void displayFiles() {
        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        fileList=new ArrayList<>();
        fileList.addAll(findVideos(path));
        customAdapter= new CustomAdapter(this,fileList,this);
        customAdapter.setHasStableIds(true);
        recyclerView.setAdapter(customAdapter);

    }
    public ArrayList<File> findVideos(File file) {
        ArrayList<File> myVideos = new ArrayList<>();
        Deque<File> stack = new LinkedList<>();
        stack.push(file);

        while (!stack.isEmpty()) {
            File currentDir = stack.pop();
            File[] allFiles = currentDir.listFiles();

            if (allFiles == null) {
                continue;
            }

            for (File singleFile : allFiles) {
                if (singleFile.isDirectory() && !singleFile.isHidden()) {
                    stack.push(singleFile);
                } else if (singleFile.getName().toLowerCase().endsWith(".mp4")) {
                    myVideos.add(singleFile);
                }
            }
        }
        return myVideos;
    }

    @Override
    public void onFileClicked(File file) {
        Intent intent = new Intent(this,PlayerActivity.class);
        intent.putExtra("fileUri", Uri.fromFile(file));
        startActivity(intent);
    }
}