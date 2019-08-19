package com.curtcaldwell.gotennachallenge;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class PinViewHolder extends RecyclerView.ViewHolder {

    private TextView pinName;


    PinViewHolder(@NonNull View itemView) {
        super(itemView);
        pinName = itemView.findViewById(R.id.pin_name);
    }

    void setData(Pin pin) {
        pinName.setText(pin.getName());

    }
}
