package com.example.weather;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.google.android.material.textfield.TextInputEditText;

import java.nio.Buffer;

public class LocationFragment extends Fragment{

    //private OnFragmetLocationListener listener;

    private TextInputEditText location_et;
    private Button button_ok;

      @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
          View v = inflater.inflate(R.layout.fragment_location, container, false);
          location_et =v.findViewById(R.id.location_et);
          button_ok = v.findViewById(R.id.oklocationbtn);
          button_ok.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Bundle bundle = new Bundle();
                  bundle.putString("key", location_et.getText().toString());
                  HomeFragment homeFragment = new HomeFragment();
                  homeFragment.setArguments(bundle);
                  getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, homeFragment).commit();
              }
          });
          return v;
    }

//    interface OnFragmetLocationListener{
//          void  onFragmentLocation(String text);
//    }
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            listener = (OnFragmetLocationListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString()
//                    + " должен реализовывать интерфейс OnFragmetLocationListener");
//        }
//    }
//    public void updateDetail() {
//        String loc = location_et.getText().toString();
//        listener.onFragmentLocation(loc);
//    }

}