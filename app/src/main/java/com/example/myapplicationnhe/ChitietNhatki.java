package com.example.myapplicationnhe;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChitietNhatki extends AppCompatActivity {

    //Khai báo các view
    EditText etTieude;
    EditText etChitiet;
    EditText etGio;
    EditText etPhut;
    Button btnHuy;
    Button btnThem;
    NhatkiDatabase database;
    ExecutorService executorService;
    Nhatki currentNhatki;
    Toolbar toolbar;
    Spinner spinner_phanloai;
    ImageButton imbtn_icon;
    EditText etDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.read_nhatki);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.readNhatki), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etTieude = findViewById(R.id.etTieude);
        etChitiet = findViewById(R.id.etChitiet);
        etGio = findViewById(R.id.etgio);
        etPhut = findViewById(R.id.etphut);
        btnHuy = findViewById(R.id.btnHuy);
        btnThem = findViewById(R.id.btnThem);
        toolbar = findViewById(R.id.toolbar_3);
        spinner_phanloai = findViewById(R.id.phanloai);
        imbtn_icon = findViewById(R.id.btn_icon);
        etDate = findViewById(R.id.etDate);
        setupDatabase();
        checkEditMode();
    }
    private void  setupDatabase(){
        database = NhatkiDatabase.getInstance(this);
        executorService = Executors.newFixedThreadPool(4);
    }
    private void checkEditMode() {
        int nhatkiId = getIntent().getIntExtra("nhatki_id", -1);
        loadNhatki(nhatkiId);
    }
    private void loadNhatki(int nhatkiId){
        executorService.execute(()->{
            currentNhatki = database.nhatkiDao().getById(nhatkiId);
            runOnUiThread(()->{
                if(currentNhatki!=null){
                    etTieude.setText(currentNhatki.getTitle());
                    etChitiet.setText(currentNhatki.getContent());
                    String time = currentNhatki.getTime();
                    String[] parts = time.split(":");
                    String a = parts[0];
                    String b = parts[1];
                    etGio.setText(a);
                    etPhut.setText(b);
                    imbtn_icon.setImageResource(currentNhatki.getSrcImage());
                    imbtn_icon.setBackgroundResource(currentNhatki.getSrcBack());
                }
            });
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }

}
