package com.example.myapplicationnhe;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NhatkiAdapter extends RecyclerView.Adapter<NhatkiAdapter.NhatkiViewHolder> {
    private List<Nhatki> nhatkiList;
    private Set<Integer> selectedNhatki;
    private Context context;
    private OnItemClickListener  listener;
    private boolean isSelectionMode = false;

    public interface OnItemClickListener{
        void onItemClick(Nhatki nhatki);
        void onItemLongClick(Nhatki nhatki);
        void onSelectionChanged(int selectedCount);
    }

    public NhatkiAdapter(Context context){
        this.context = context;
        this.nhatkiList = new ArrayList<>();
        this.selectedNhatki = new HashSet<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public NhatkiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nhatki_don,parent,false);
        return new NhatkiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NhatkiViewHolder holder, int position){
        Nhatki nhatki = nhatkiList.get(position);
        holder.bind(nhatki);
    }

    @Override
    public int getItemCount(){
        return nhatkiList.size();
    }

    public void setNhatkiList(List<Nhatki> list) {
        this.nhatkiList = list;
        notifyDataSetChanged();
    }

    public void setSelectionMode(boolean selectionMode){
        this.isSelectionMode = selectionMode;
        if(!selectionMode){
            selectedNhatki.clear();
        }
        notifyDataSetChanged();
    }

    public List<Nhatki> getSelectedNhatki() {
        List<Nhatki> selected = new ArrayList<>();
        for (Nhatki nhatki : nhatkiList) {
            if (selectedNhatki.contains(nhatki.getId())) {
                selected.add(nhatki);
            }
        }
        return selected;
    }

    public void clearSelection(){
        selectedNhatki.clear();
        setSelectionMode(false);
    }


    class NhatkiViewHolder extends RecyclerView.ViewHolder{
        ImageButton icon,iconChon;
        CheckBox checkBox;
        TextView tvTieude, tvNgay,tvGio, tvPhanloai;
        View itemContainer;
        public NhatkiViewHolder(@NonNull View itemView){
            super(itemView);
            icon=itemView.findViewById(R.id.icon);
            iconChon=itemView.findViewById(R.id.icon_chon);
            checkBox=itemView.findViewById(R.id.checkbox1);
            tvTieude=itemView.findViewById(R.id.tvTieude);
            tvNgay=itemView.findViewById(R.id.ngay);
            tvGio=itemView.findViewById(R.id.gio);
            tvPhanloai=itemView.findViewById(R.id.phanloai);
            itemContainer=itemView.findViewById(R.id.item_container);
        }

        public void bind (Nhatki nhatki){
            tvTieude.setText(nhatki.getTitle());
            tvNgay.setText(nhatki.getDate());
            tvGio.setText(nhatki.getTime());
            tvPhanloai.setText(nhatki.getType());


            try {
                int imgRes = nhatki.getSrcImage();
                int bgRes  = nhatki.getSrcBack();

                if (imgRes == 0) {
                    imgRes = R.drawable.icon_favorite_24px;
                }
                if (bgRes == 0 ) {
                    bgRes = R.drawable.back_circle_red;
                }

                icon.setImageResource(imgRes);
                iconChon.setImageResource(R.drawable.icon_check);

                icon.setBackgroundResource(bgRes);
                iconChon.setBackgroundResource(R.drawable.back_circle_black);

            } catch (Exception e) {
                icon.setImageResource(R.drawable.icon_favorite_24px);
                icon.setBackgroundResource(R.drawable.back_circle_red);
                iconChon.setImageResource(R.drawable.icon_check);
                iconChon.setBackgroundResource(R.drawable.back_circle_black);
            }

            //Gắn màu cho nhãn phân loại
            String type = nhatki.getType();
            tvPhanloai.setText(type);
            switch (type) {
                case "Ưu tiên 1":
                    tvPhanloai.setBackgroundResource(R.drawable.back_square_blue);
                    break;
                case "Ưu tiên 2":
                    tvPhanloai.setBackgroundResource(R.drawable.back_square_green_2);
                    break;
                case "Ưu tiên 3":
                    tvPhanloai.setBackgroundResource(R.drawable.back_square_yelow);
                    break;
                case "Ưu tiên 4":
                    tvPhanloai.setBackgroundResource(R.drawable.back_square_pink);
                    break;
                case "Ưu tiên 5":
                    tvPhanloai.setBackgroundResource(R.drawable.back_square_purple);
                    break;
                default:
                    tvPhanloai.setBackgroundResource(R.drawable.back_square_blue); // màu mặc định
                    break;
            }



            //Chế độ chọn từng item
            boolean isSelected = selectedNhatki.contains(nhatki.getId());

            //Click thường
            if (isSelectionMode) {
                if (isSelected) {
                    icon.setVisibility(View.INVISIBLE);
                    iconChon.setVisibility(View.VISIBLE);
                } else {
                    icon.setVisibility(View.VISIBLE);
                    iconChon.setVisibility(View.INVISIBLE);
                }
            } else {
                icon.setVisibility(View.VISIBLE);
                iconChon.setVisibility(View.INVISIBLE);
            }

            //Click bình thường
            itemContainer.setOnClickListener(v -> {
                if (isSelectionMode) {
                    toggleSelection(nhatki);
                } else {
                    if (listener != null) {
                        listener.onItemClick(nhatki);
                    }
                }
            });

            //Nhấn giữ lâu
            itemContainer.setOnLongClickListener(v -> {
                if (!isSelectionMode) {
                    setSelectionMode(true);
                    toggleSelection(nhatki);
                    if (listener != null) {
                        listener.onItemLongClick(nhatki);
                    }
                }
                return true;
            });


        }
        private void toggleSelection(Nhatki nhatki){
            int id = nhatki.getId();
            if (selectedNhatki.contains(id)) {
                selectedNhatki.remove(id);
            } else {
                selectedNhatki.add(id);
            }

            // Cập nhật UI ngay lập tức
            notifyItemChanged(getAdapterPosition());

            // Thông báo cho MainActivity về số lượng đã chọn
            if (listener != null) {
                listener.onSelectionChanged(selectedNhatki.size());
            }

            // Nếu không còn item nào được chọn, tự động tắt selection mode
            if (selectedNhatki.isEmpty() && isSelectionMode) {
                setSelectionMode(false);
            }
        }
    }

}