package com.example.myapplicationnhe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class MainActivity extends AppCompatActivity implements NhatkiAdapter.OnItemClickListener{


    private RecyclerView recyclerView;
    private NhatkiAdapter adapter;
    private NhatkiDatabase database;

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
        database = NhatkiDatabase.getInstance(this);
        recyclerView = findViewById(R.id.nhatkidon_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NhatkiAdapter(this);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        //Sự kiện cho button thêm nhật ký
        ImageButton btn_add = findViewById(R.id.add);
        btn_add.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, addNhatki.class);
            addEditLauncher.launch(intent);
        });

        loadData();
    }
    private void loadData() {
        // Dùng thread riêng để không block UI
        new Thread(() -> {
            List<Nhatki> list = database.nhatkiDao().getAll();
            runOnUiThread(() -> {
                adapter.setNhatkiList(list);
                if (list.isEmpty()) {
                    Toast.makeText(this, "Chưa có nhật ký nào! Nhấn + để thêm nhé", Toast.LENGTH_LONG).show();
                }
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
        Toast.makeText(this, "Đang chọn nhiều...", Toast.LENGTH_SHORT).show();
        LinearLayout bottombar = findViewById(R.id.bottomBar);
        bottombar.setVisibility(View.VISIBLE);
    }

    public void onSelectionChanged(int selectedCount) {
        if (selectedCount > 0) {
            setTitle(selectedCount + " đã chọn");
        } else {
            setTitle("Nhật ký của tôi");
        }
    }

}