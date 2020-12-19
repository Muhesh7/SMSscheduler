package com.example.smsscheduler;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsscheduler.databinding.CardLayoutBinding;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyHolder> {

    private ArrayList<Model> mModels=new ArrayList<>();

    public recyclerAdapter(ArrayList<Model> models) {
        mModels = models;
    }

    @NonNull
    @Override
    public recyclerAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        CardLayoutBinding cardLayoutBinding= DataBindingUtil.inflate(layoutInflater
                ,R.layout.card_layout,parent,false);
        return new MyHolder(cardLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyHolder holder, int position) {
            holder.mCardLayoutBinding.setMode(mModels.get(position));
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CardLayoutBinding mCardLayoutBinding;
        public MyHolder(@NonNull CardLayoutBinding itemView) {
            super(itemView.getRoot());
            this.mCardLayoutBinding=itemView;
        }
    }
}
