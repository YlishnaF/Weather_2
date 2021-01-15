package com.example.weather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.weather.Key.WEATHER_API_KEY;

public class MapsFragment extends Fragment {
    private Button currentLoc;
    private Button btnWeatherOnMap;
    private static final int PERMISSION_REQUEST_CODE = 10;
    private GoogleMap mMap;
    private Marker currentMarker;
    private String latitude;
    private String longitude;
    private OpenWeatherCoord openWeatherCoord;
    Double lat;
    Double lnt;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            LatLng sydney = new LatLng(-34, 151);
            currentMarker = googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    mMap.addMarker(new MarkerOptions().position(latLng));
                    lat = latLng.latitude;
                    lnt = latLng.longitude;
                    String strLat = lat.toString();
                    String strLnt = lnt.toString();
                    Log.d("MyLog", strLat);
                    Log.d("MyLog", strLnt);
                   // requestRetrifit(lat.floatValue(), lnt.floatValue(), WEATHER_API_KEY);
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        currentLoc = v.findViewById(R.id.btn_loc_coord);
        btnWeatherOnMap = v.findViewById(R.id.btn_whether_in_map);
        btnWeatherOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRetrifit(lat.floatValue(), lnt.floatValue(), WEATHER_API_KEY);
            }
        });
        currentLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocation();
                requestRetrifit(Float.parseFloat(latitude), Float.parseFloat(longitude), WEATHER_API_KEY);
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        requestPermissions();
        initRetorfit();

    }

    private void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        } else {
            requestLocationPermissions();
        }
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Permissions required", Toast.LENGTH_LONG).show();
            return;
        }
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 1000, 10000, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    double lat = location.getLatitude();
                    latitude = Double.toString(lat);
                    Log.d("MyLog", latitude);

                    double lng = location.getLongitude();
                    longitude = Double.toString(lng);
                    Log.d("MyLog", longitude);


                    String accuracy = Float.toString(location.getAccuracy());
                    LatLng currentPosition = new LatLng(lat, lng);
                    currentMarker.setPosition(currentPosition);

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, (float) 12));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {

                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {

                }
            });
        }
    }

    private void requestLocationPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_CODE){
            if(grantResults.length == 2 && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                requestLocation();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initRetorfit(){
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        openWeatherCoord = retrofit.create(OpenWeatherCoord.class);
    }

    private void requestRetrifit(Float lat, Float lon, String apiKey){
        Log.d("MyLog", "here");
        openWeatherCoord.loadWeather(lat, lon, apiKey)
                .enqueue(new Callback<WeatherRequest>() {
                    @Override
                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
                        final WeatherRequest request = response.body();
                        if(request!=null){
                            float result = response.body().getMain().getTemp() - 273;
                            float pressure = response.body().getMain().getPressure();
                            float humidity = response.body().getMain().getHumidity();
                            float wind = response.body().getWind().getSpeed();

                            Bundle bundle = new Bundle();
                            bundle.putString("key", response.body().getName());
                            bundle.putString("temperature", Float.toString(result));
                            bundle.putString("pressure", Float.toString(pressure));
                            bundle.putString("humidity", Float.toString(humidity));
                            bundle.putString("wind", Float.toString(wind));
                            HomeFragment homeFragment = new HomeFragment();
                            homeFragment.setArguments(bundle);
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, homeFragment).commit();

                            Toast.makeText(getContext(),Float.toString(result), Toast.LENGTH_SHORT).show();
                            Log.d("MyLog", Float.toString(result));
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
                        Toast.makeText(getContext(),"error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
