package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.DatabaseMetaData;
import java.util.HashMap;

public class MainActivity<DatabaseReference> extends AppCompatActivity {
    private StorageReference Folder;
    private static final int ImageBack=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Folder= FirebaseStorage.getInstance().getReference().child("ImageFolder");
    }

    public void UploadData(View view)
    {
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/");
        startActivityForResult(intent,ImageBack);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==ImageBack)
        {
            if (resultCode==RESULT_OK)
            {
                Uri ImageData=data.getData();

                final StorageReference Imagename=Folder.child("image"+ImageData.getLastPathSegment());
                Imagename.putFile(ImageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                com.google.firebase.database.DatabaseReference imagestore= FirebaseDatabase.getInstance().getReference().child("Image");
                                HashMap<String,String> hashMap=new HashMap<>();
                                hashMap.put("imageurl",String.valueOf(uri));

                                imagestore.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(MainActivity.this, "Finally Zalll", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        });

                    }
                });
            }
        }
    }
}
