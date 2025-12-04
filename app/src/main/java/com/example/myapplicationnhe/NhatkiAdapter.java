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
import java.util.List;

public class NhatkiAdapter extends RecyclerView.Adapter<NhatkiAdapter.NhatkiViewHolder> {
    private List<Nhatki> nhatkiList;
    private List<Nhatki> selectedNhatki;
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
        this.selectedNhatki = new ArrayList<>();
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public void setNhatkiList(List<Nhatki> list) {
        this.nhatkiList = list != null ? new ArrayList<>(list) : new ArrayList<>();
        selectedNhatki.clear();
        isSelectionMode = false;
        notifyDataSetChanged();
        if (listener != null) listener.onSelectionChanged(0);
    }

    public void setSelectionMode(boolean selectionMode){
        this.isSelectionMode = selectionMode;
        if(!selectionMode){
            selectedNhatki.clear();
        }
        notifyDataSetChanged();
    }

    public List<Nhatki> getSelectedNhatki(){
        return new ArrayList<>(selectedNhatki);
    }

    public void clearSelection(){
        selectedNhatki.clear();
        setSelectionMode(false);
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

    class NhatkiViewHolder extends RecyclerView.ViewHolder{
        ImageButton icon,iconChon;
        CheckBox checkBox;
        TextView tvTieude, tvNgay,tvGio, tvPhanloai;
        public NhatkiViewHolder(@NonNull View itemView){
            super(itemView);
            icon=itemView.findViewById(R.id.icon);
            iconChon=itemView.findViewById(R.id.icon_chon);
            checkBox=itemView.findViewById(R.id.checkbox1);
            tvTieude=itemView.findViewById(R.id.tvTieude);
            tvNgay=itemView.findViewById(R.id.ngay);
            tvGio=itemView.findViewById(R.id.gio);
            tvPhanloai=itemView.findViewById(R.id.phanloai);
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

            //Chọn hay không chọn
            boolean isSelected = selectedNhatki.contains(nhatki);
            checkBox.setChecked(isSelected);
            if(isSelectionMode && isSelected){
                icon.setVisibility(View.INVISIBLE);
                iconChon.setVisibility(View.VISIBLE);
            } else {
                iconChon.setVisibility(View.GONE);
            }

            //Click thường hoặc nhấn giữ
            itemView.setOnClickListener(v->{
                if(isSelectionMode){
                    toggleSelection(nhatki);
                } else if(listener != null){
                    listener.onItemClick(nhatki);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if(!isSelectionMode){
                    setSelectionMode(true);
                    toggleSelection(nhatki);
                    if(listener!=null){
                        listener.onItemLongClick(nhatki);
                    }
                }
                return true;
            });

            checkBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                if(isChecked){
                    if(!selectedNhatki.contains(nhatki)) selectedNhatki.add(nhatki);
                } else {
                    selectedNhatki.remove(nhatki);
                }
                if(listener!=null){
                    listener.onSelectionChanged(selectedNhatki.size());
                }
            }));

        }
        private void toggleSelection(Nhatki nhatki){
            if(selectedNhatki.contains(nhatki)){
                selectedNhatki.remove(nhatki);
            } else {
                selectedNhatki.add(nhatki);
            }
            checkBox.setChecked(selectedNhatki.contains(nhatki));
            if(listener!=null){
                listener.onSelectionChanged(selectedNhatki.size());
            }
        }
    }

}