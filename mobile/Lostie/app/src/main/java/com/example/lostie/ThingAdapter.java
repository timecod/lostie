package com.example.lostie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.yandex.mapkit.geometry.Point;

import java.util.Calendar;
import java.util.List;

public class ThingAdapter extends RecyclerView.Adapter<ThingAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Thing> things;

    ThingAdapter(Context context, List<Thing> things) {
        this.things = things;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.thing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Thing thing = things.get(position);
        holder.name.setText(thing.name);
        holder.time.setText(thing.last_time.toString());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return things.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, time;
        Button button;
        public ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.name);
            time = view.findViewById(R.id.time);
            button = view.findViewById(R.id.button);
        }
    }
}
