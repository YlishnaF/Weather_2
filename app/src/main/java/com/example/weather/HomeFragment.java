package com.example.weather;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class HomeFragment extends Fragment {
    private FragmentHomeListner listener;
    TextView location_tv;

    public interface FragmentHomeListner{
        void onInputHomeSent(CharSequence input);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        location_tv = v.findViewById(R.id.locationshowtv);

        Bundle bundle = this.getArguments();
        if(bundle!=null){
            String data = bundle.getString("key");
            location_tv.setText(data);
        }
        return v;
    }

}