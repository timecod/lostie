package com.example.lostie.database;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.lostie.activity.AddNewObjectActivity;

public class MyListAdapter extends ListAdapter<Object, ViewHolder> {

        public MyListAdapter(@NonNull DiffUtil.ItemCallback<Object> diffCallback) {
            super(diffCallback);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return ViewHolder.create(parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Object current = getItem(position);
            Log.d(TAG, current.name+ "--------------------------------------------------------------------" );
            holder.bind(current.name);
        }

       public static class MyDiff extends DiffUtil.ItemCallback<Object> {

            @Override
            public boolean areItemsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
                return oldItem.name.equals(newItem.name);
            }
        }

}
