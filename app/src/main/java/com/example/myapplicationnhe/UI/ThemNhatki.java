package com.example.myapplicationnhe.UI;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplicationnhe.Model.Nhatki;
import com.example.myapplicationnhe.Database.NhatkiDatabase;
import com.example.myapplicationnhe.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThemNhatki extends AppCompatActivity {


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
    boolean isEditMode = false;
    Toolbar toolbar;
    Spinner spinner_phanloai;
    ImageButton imbtn_icon;
    EditText etDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_nhatki);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addNhatki), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Set up thông tin các view và trạng thái của activity
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

        //Khi click button back;
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        //Tạo các item cho dropdown phân loại
        String[] loai = {"Ưu tiên 1", "Ưu tiên 2", "Ưu tiên 3", "Ưu tiên 4", "Ưu tiên 5"};
        ArrayAdapter<String> adapter_phanloai = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loai);
        adapter_phanloai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_phanloai.setAdapter(adapter_phanloai);

        //Tạo sự kiện khi click button chọn icon
        imbtn_icon.setOnClickListener(v -> {
            Bottomsheet_icon bottomSheet = new Bottomsheet_icon((backgroundResId, imageResId) -> {
                // Thay ảnh
                imbtn_icon.setImageResource(imageResId);
                // Thay background
                imbtn_icon.setBackgroundResource(backgroundResId);

                imbtn_icon.setTag(imageResId + "," + backgroundResId);
            });
            bottomSheet.show(getSupportFragmentManager(), "Bottomsheet_icon");
        });

        //Tạo sự kiện khi click button chọn ngày
        etDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Hiển thị DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ThemNhatki.this,
                    (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                        // Gán ngày được chọn vào EditText
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        etDate.setText(date);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        //Sự kiện click cho button Thêm
        btnThem.setOnClickListener(v -> luuNhatki());
        btnHuy.setOnClickListener(v -> finish());
    }


    //Tạo class Bottomsheet
    public static class Bottomsheet_icon extends BottomSheetDialogFragment {
        public interface OnImageSelectedListener {
            void onImageSelected(int backgroundResId, int imageResId);
        }

        final private OnImageSelectedListener listener;

        public Bottomsheet_icon(OnImageSelectedListener listener) {
            this.listener = listener;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.bottom_sheet_icon, container, false);

            ImageButton imgOption1 = view.findViewById(R.id.icon1);
            ImageButton imgOption2 = view.findViewById(R.id.icon2);
            ImageButton imgOption3 = view.findViewById(R.id.icon3);
            ImageButton imgOption4 = view.findViewById(R.id.icon4);
            ImageButton imgOption5 = view.findViewById(R.id.icon5);
            ImageButton imgOption6 = view.findViewById(R.id.icon6);
            ImageButton imgOption7 = view.findViewById(R.id.icon7);
            ImageButton imgOption8 = view.findViewById(R.id.icon8);
            ImageButton imgOption9 = view.findViewById(R.id.icon9);
            ImageButton imgOption10 = view.findViewById(R.id.icon10);
            ImageButton imgOption11 = view.findViewById(R.id.icon11);
            ImageButton imgOption12 = view.findViewById(R.id.icon12);

            imgOption1.setOnClickListener(v -> {
                listener.onImageSelected(R.drawable.back_circle_red, R.drawable.icon_favorite_24px);
                dismiss();
            });
            imgOption2.setOnClickListener(v -> {
                listener.onImageSelected(R.drawable.back_circle_green, R.drawable.icon_eco);
                dismiss();
            });
            imgOption3.setOnClickListener(v -> {
                listener.onImageSelected(R.drawable.back_circle_blue,R.drawable.icon_work);
                dismiss();
            });
            imgOption4.setOnClickListener(v -> {
                listener.onImageSelected(R.drawable.back_circle_orage,R.drawable.icon_money);
                dismiss();
            });
            imgOption5.setOnClickListener(v -> {
                listener.onImageSelected(R.drawable.back_circle_green,R.drawable.icon_game);
                dismiss();
            });
            imgOption6.setOnClickListener(v -> {
                listener.onImageSelected(R.drawable.back_circle_orage,R.drawable.icon_school_24px);
                dismiss();
            });
            imgOption7.setOnClickListener(v -> {
                listener.onImageSelected(R.drawable.back_circle_red,R.drawable.icon_group);
                dismiss();
            });
            imgOption8.setOnClickListener(v -> {
                listener.onImageSelected(R.drawable.back_circle_blue,R.drawable.icon_health_and_safety_24px);
                dismiss();
            });
            imgOption9.setOnClickListener(v -> {
                listener.onImageSelected(R.drawable.back_circle_purple,R.drawable.icon_car);
                dismiss();
            });
            imgOption10.setOnClickListener(v -> {
                listener.onImageSelected(R.drawable.back_circle_blue_thin,R.drawable.icon_makeup);
                dismiss();
            });
            imgOption11.setOnClickListener(v -> {
                listener.onImageSelected(R.drawable.back_circle_green_strong,R.drawable.icon_emotion_calm_24px);
                dismiss();
            });
            imgOption12.setOnClickListener(v -> {
                listener.onImageSelected(R.drawable.back_circle_orage,R.drawable.icon_emotion_satisfied_24px);
                dismiss();
            });
            return view;
        }

    }

    private void  setupDatabase(){
        database = NhatkiDatabase.getInstance(this);
        executorService = Executors.newFixedThreadPool(4);
    }

    private void checkEditMode() {
        int nhatkiId = getIntent().getIntExtra("nhatki_id", -1);
        if (nhatkiId != -1) {
            isEditMode = true;
            toolbar.setTitle("Chỉnh sửa nhật ký");
            btnThem.setText("SỬA NHẬT KÝ");
            loadNhatki(nhatkiId);
        } else {
            isEditMode = false;
            toolbar.setTitle("Thêm nhật ký mới");
            btnThem.setText("THÊM NHẬT KÝ");
        }
    }
    private void loadNhatki(int nhatkiId){
        executorService.execute(()->{
            currentNhatki = database.nhatkiDao().getById(nhatkiId);
            runOnUiThread(()->{
                if(currentNhatki!=null){
                    etTieude.setText(currentNhatki.getTitle());
                    etChitiet.setText(currentNhatki.getContent());
                    String time = currentNhatki.getTime();
                    etDate.setText(currentNhatki.getDate());
                    imbtn_icon.setTag(currentNhatki.getSrcImage() + "," + currentNhatki.getSrcBack());

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

    private void luuNhatki(){
        String tieuDe = etTieude.getText().toString().trim();
        String chiTiet = etChitiet.getText().toString().trim();
        String gioStr = etGio.getText().toString().trim();
        String phutStr = etPhut.getText().toString().trim();
        String ngay = etDate.getText().toString().trim();
        String phanloai = spinner_phanloai.getSelectedItem().toString();

        //Lấy id của image button
        Object tagObj = imbtn_icon.getTag();
        String tag = tagObj != null ? tagObj.toString() :
                R.drawable.icon_favorite_24px + "," + R.drawable.back_circle_red;

        String[] parts = tag.split(",");
        int srcResId = Integer.parseInt(parts[0]);
        int bgResId  = Integer.parseInt(parts[1]);

        //Kiểm tra rỗng
        if (tieuDe.isEmpty() || chiTiet.isEmpty() || gioStr.isEmpty() || phutStr.isEmpty() || ngay.isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Vui lòng nhập đầy đủ thông tin!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        //Kiểm tra giá trị giờ, phút
        int gio, phut;
        try {
            gio = Integer.parseInt(gioStr);
            phut = Integer.parseInt(phutStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giờ/phút không hợp lệ!", Toast.LENGTH_LONG).show();
            return;
        }

        if((gio<0||gio>24)||(phut>60 || phut<0)){
            Toast.makeText(getApplicationContext(),
                    "Vui lòng nhập đúng định dạng giờ/phút!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String thoiGian = String.format("%02d:%02d", gio, phut);
        executorService.execute(()->{
            try{
                if(isEditMode &&currentNhatki!=null){
                    currentNhatki.setTitle(tieuDe);
                    currentNhatki.setContent(chiTiet);
                    currentNhatki.setType(phanloai);
                    currentNhatki.setDate(ngay);
                    currentNhatki.setTime(thoiGian);
                    currentNhatki.setSrcImage(srcResId);
                    currentNhatki.setSrcBack(bgResId);

                    database.nhatkiDao().update(currentNhatki);

                } else {

                    Nhatki nhatkimoi = new Nhatki(tieuDe,chiTiet,srcResId,bgResId,ngay,thoiGian,phanloai);
                    database.nhatkiDao().insert(nhatkimoi);
                }

                runOnUiThread(() -> {
                    Toast.makeText(ThemNhatki.this,
                            isEditMode ? "Cập nhật nhật ký thành công!" : "Thêm nhật ký thành công!",
                            Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                });
            } catch (Exception e){
                e.printStackTrace();
                runOnUiThread(()->{
                    Toast.makeText(ThemNhatki.this,"Lỗi:" + e.toString(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}

