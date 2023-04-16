package com.example.lostie.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lostie.R;

public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView objectItemView;

        private ViewHolder(View itemView) {
            super(itemView);
            objectItemView = itemView.findViewById(R.id.textView);
        }

        public void bind(String text) {
            objectItemView.setText(text);
        }

        static ViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item, parent, false);
            return new ViewHolder(view);
        }

}
