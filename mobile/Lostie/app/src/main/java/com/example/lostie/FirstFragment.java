package com.example.lostie;

import static com.yandex.runtime.Runtime.getApplicationContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.lostie.databinding.FragmentFirstBinding;

import java.util.List;

public class FirstFragment extends Fragment {
    private RecyclerView rc;
    private AppDatabase db;
    private ThingAdapter ta;
    private FragmentFirstBinding binding;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);


        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //recview
        rc = view.findViewById(R.id.recyclerView);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database").allowMainThreadQueries().build();
        ta = new ThingAdapter(this.getContext(), db.thingsDao().getAll());
        rc.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rc.setAdapter(ta);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

