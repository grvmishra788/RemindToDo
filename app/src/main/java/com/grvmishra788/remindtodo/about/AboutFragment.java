package com.grvmishra788.remindtodo.about;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grvmishra788.remindtodo.R;

public class AboutFragment extends Fragment {

    private static final String TAG = AboutFragment.class.getName(); //constant Class TAG
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "OnCreateView() called!");
        super.onCreate(savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_about, container, false);
        Log.d(TAG, "OnCreateView() call completed!");
        return view;
    }
}
