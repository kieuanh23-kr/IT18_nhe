package com.example.myapplicationnhe.UI;

import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplicationnhe.Model.Nhatki;
import com.example.myapplicationnhe.Adapter.NhatkiAdapter;
import com.example.myapplicationnhe.Database.NhatkiDatabase;
import com.example.myapplicationnhe.R;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NhatkiAdapter.OnItemClickListener {


    private RecyclerView recyclerView;
    private NhatkiAdapter adapter;
    private NhatkiDatabase database;
    Toolbar toolbar;
    LinearLayout bottombar;

    private final ActivityResultLauncher<Intent> addEditLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadData(); // Reload lại danh sách khi thêm/sửa thành công
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottombar = findViewById(R.id.bottomBar);


        database = NhatkiDatabase.getInstance(this);
        recyclerView = findViewById(R.id.nhatkidon_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NhatkiAdapter(this);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        loadData();

        //Sự kiện cho button thêm nhật ký
        ImageButton btn_add = findViewById(R.id.add);
        btn_add.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ThemNhatki.class);
            addEditLauncher.launch(intent);
        });

        //Sự kiện cho button close ở selection mode
        ImageButton btn_close = findViewById(R.id.icon_close);
        btn_close.setOnClickListener(v->tatSelectionmode());

        //Sự kiện cho button xóa
        ImageButton btn_delete = findViewById(R.id.icon_delete);
        btn_delete.setOnClickListener(v->{
            List<Nhatki> selectedItems = adapter.getSelectedNhatki();
            new AlertDialog.Builder(this,R.style.MyAlertDialogTheme)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn chắc chắn muốn xóa "+selectedItems.size() +" nhật ký này?")
                    .setPositiveButton("Xóa", (dialog, which) -> xoaNhatki())
                    .setNegativeButton("Hủy", null)
                    .show();

        });

    }
    private void xoaNhatki(){
        List<Nhatki> selectedItems = adapter.getSelectedNhatki();
        if (!selectedItems.isEmpty()) {
            new Thread(() -> {
                for (Nhatki nhatki : selectedItems) {
                    database.nhatkiDao().delete(nhatki);
                }
                // Sau khi xóa xong, reload lại danh sách trên UI
                runOnUiThread(() -> {
                    tatSelectionmode();
                    loadData();
                    Toast.makeText(MainActivity.this,
                            "Đã xóa " + selectedItems.size() + " nhật ký!",
                            Toast.LENGTH_SHORT).show();
                });
            }).start();
        }
    }
    private void tatSelectionmode(){
        adapter.setSelectionMode(false);
        getSupportActionBar().setTitle("Nhật ký đa phương tiện");
        bottombar.setVisibility(View.GONE);
    }

    private void loadData() {
        TextView tvThongbao = findViewById(R.id.tvThongbao);
        // Dùng thread riêng để không block UI
        new Thread(() -> {
            List<Nhatki> list = database.nhatkiDao().getAll();
            runOnUiThread(() -> {
                adapter.setNhatkiList(list);
                if (list.isEmpty()) {
                    tvThongbao.setVisibility(View.VISIBLE);
                } else{tvThongbao.setVisibility(View.GONE);}
            });
        }).start();
    }

    @Override
    public void onItemClick(Nhatki nhatki){
        Intent intent = new Intent(this, ChitietNhatki.class);
        intent.putExtra("nhatki_id", nhatki.getId());
        addEditLauncher.launch(intent);
    }
    @Override
    public void onItemLongClick(Nhatki nhatki) {
        bottombar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSelectionChanged(int selectedCount) {

        if (selectedCount > 0) {
            toolbar.setTitle("Đã chọn " +  selectedCount);
            bottombar.setVisibility(View.VISIBLE);
        } else {
            toolbar.setTitle("Nhật ký đa phương tiện");
            bottombar.setVisibility(View.GONE);
        }
        if(selectedCount==0){
            bottombar.setVisibility(View.GONE);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

}