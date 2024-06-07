package com.example.appainterappcollusionattack;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editText;
    private Button saveButton;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editText = findViewById(R.id.editText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    String username = editTextUsername.getText().toString();
                    String password = editTextPassword.getText().toString();
                    String data = editText.getText().toString();
                    saveData(username, password, data);
                } else {
                    requestPermission();
                }
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String data = editText.getText().toString();
                saveData(username, password, data);
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveData(String username, String password, String data) {
        if (isExternalStorageWritable()) {
            File file = new File(Environment.getExternalStorageDirectory(), "userdata.txt");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                String fileContent = "Username: " + username + "\nPassword: " + password + "\nData: " + data;
                fos.write(fileContent.getBytes());
                Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
}
