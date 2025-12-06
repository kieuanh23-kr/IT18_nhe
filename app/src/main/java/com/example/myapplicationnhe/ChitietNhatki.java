package com.example.myapplicationnhe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChitietNhatki extends AppCompatActivity {

    //Khai báo các view
    EditText etTieude;
    EditText etChitiet;
    EditText etGio;
    EditText etPhut;

    NhatkiDatabase database;
    ExecutorService executorService;
    Nhatki currentNhatki;
    Toolbar toolbar;
    Spinner spinner_phanloai;
    ImageButton imbtn_icon;
    ImageButton imbtn_Sua;
    ImageButton imbtn_Xoa;
    ImageButton imbtn_share;
    EditText etDate;
    int currentNhatkiId = -1;

    private final ActivityResultLauncher<Intent> editLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Reload lại dữ liệu sau khi sửa
                    if (currentNhatkiId != -1) {
                        loadNhatki(currentNhatkiId);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.read_nhatki);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.readNhatki), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Set up thông tin các view và trạng thái của activity
        etTieude = findViewById(R.id.etTieude);
        etChitiet = findViewById(R.id.etChitiet);
        etGio = findViewById(R.id.etgio);
        etPhut = findViewById(R.id.etphut);

        toolbar = findViewById(R.id.toolbar_3);
        spinner_phanloai = findViewById(R.id.phanloai);
        imbtn_icon = findViewById(R.id.btn_icon);
        etDate = findViewById(R.id.etDate);
        imbtn_Sua=findViewById(R.id.icon_edit);
        imbtn_Xoa=findViewById(R.id.icon_delete);
        imbtn_share=findViewById(R.id.icon_share);

        setupDatabase();
        setEditableMode();

        currentNhatkiId = getIntent().getIntExtra("nhatki_id", -1);
        if (currentNhatkiId != -1) {
            loadNhatki(currentNhatkiId);
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Chi tiết nhật ký");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        String[] loai = {"Ưu tiên 1", "Ưu tiên 2", "Ưu tiên 3", "Ưu tiên 4", "Ưu tiên 5"};
        ArrayAdapter<String> adapter_phanloai = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loai);
        adapter_phanloai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_phanloai.setAdapter(adapter_phanloai);
        spinner_phanloai.setEnabled(false);

        //Disable các button
        imbtn_icon.setEnabled(false);
        etDate.setEnabled(false);

        //Sự kiện cho button sửa
        imbtn_Sua.setOnClickListener(v -> {
            Intent intent = new Intent(ChitietNhatki.this, ThemNhatki.class);
            intent.putExtra("nhatki_id",currentNhatkiId);
            editLauncher.launch(intent);
        });

        //Sự kiện cho button xóa
        imbtn_Xoa.setOnClickListener(v ->
            new AlertDialog.Builder(this,R.style.MyAlertDialogTheme)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn chắc chắn muốn xóa nhật ký này?")
                    .setPositiveButton("Xóa", (dialog, which) -> xoaNhatki())
                    .setNegativeButton("Hủy", null)
                    .show()
        );

        //Sự kiện cho button chia sẻ ảnh
        imbtn_share.setOnClickListener(v -> {
            String shareText = "CHIA SẺ NHẬT KÝ"+"\n"+"Tiêu đề: " + etTieude.getText().toString()
                    + "\n" +"Chi tiết: "+ etChitiet.getText().toString()
                    + "\n" +"Phân loại: "+ spinner_phanloai.getSelectedItem().toString()
                    +"\n" +"Thời gian: "+ etGio.getText().toString()+":"+etPhut.getText().toString()+" "+etDate.getText().toString();

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

            // Hiển thị bottom sheet chia sẻ
            startActivity(Intent.createChooser(shareIntent, "Chia sẻ nhật ký qua..."));

        });

    }

    private void xoaNhatki(){
        new Thread(() -> {
            Nhatki nhatki = database.nhatkiDao().getById(currentNhatkiId);
            if (nhatki != null) {
                database.nhatkiDao().delete(nhatki);
            }
            runOnUiThread(() -> {
                setResult(RESULT_OK);
                finish();
                Toast.makeText(getApplicationContext(),
                        "Xóa nhật ký thành công!",
                        Toast.LENGTH_LONG).show();
            });
        }).start();
    }

    private void setEditableMode(){
        etTieude.setEnabled(false);
        etChitiet.setEnabled(false);
        etGio.setEnabled(false);
        etPhut.setEnabled(false);
        etDate.setEnabled(false);
        spinner_phanloai.setEnabled(false);
        imbtn_icon.setEnabled(false);
    }

    private void  setupDatabase(){
        database = NhatkiDatabase.getInstance(this);
        executorService = Executors.newFixedThreadPool(4);
    }


    private void loadNhatki(int nhatkiId){
        executorService.execute(()->{
            currentNhatki = database.nhatkiDao().getById(nhatkiId);
            runOnUiThread(()->{
                if(currentNhatki!=null){
                    etTieude.setText(currentNhatki.getTitle());
                    etChitiet.setText(currentNhatki.getContent());
                    etDate.setText(currentNhatki.getDate());
                    String time = currentNhatki.getTime();
                    String[] parts = time.split(":");
                    String a = parts[0];
                    String b = parts[1];
                    etGio.setText(a);
                    etPhut.setText(b);
                    imbtn_icon.setImageResource(currentNhatki.getSrcImage());
                    imbtn_icon.setBackgroundResource(currentNhatki.getSrcBack());
                    String type = currentNhatki.getType();
                    if (type != null) {
                        ArrayAdapter adapter = (ArrayAdapter) spinner_phanloai.getAdapter();
                        int position = adapter.getPosition(type);
                        if (position >= 0) {
                            spinner_phanloai.setSelection(position);
                        }
                    }
                }
            });
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}

