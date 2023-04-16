package com.example.lostie.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import java.util.List;

public class ViewModel extends AndroidViewModel {

        private DatabaseHelper mRepository;

        private final LiveData<List<Object>> mAll;

        public ViewModel (Application application) {
            super(application);
            mRepository = new DatabaseHelper(application);
            mAll = mRepository.getAll();
        }

        public LiveData<List<Object>> getAll() { return mAll; }

        public void insert(Object object) { mRepository.insert(object); }

}
