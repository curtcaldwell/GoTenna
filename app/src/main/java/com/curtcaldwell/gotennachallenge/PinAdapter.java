package com.curtcaldwell.gotennachallenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PinAdapter extends RecyclerView.Adapter<PinViewHolder> {
    private List<Pin> resultList = new ArrayList<>();
    private Context context;


    PinAdapter(Context c) {
        context = c;
    }


    @NonNull
    @Override
    public PinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.result_item, viewGroup, false);
        return new PinViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PinViewHolder feedViewHolder, int i) {
        feedViewHolder.setData(resultList.get(i));
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    void updateList(List<Pin> list) {
        resultList.addAll(list);
        notifyDataSetChanged();
    }
}

