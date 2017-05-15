package com.soyoung.butler.plan;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.soyoung.butler.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class PlanActivity extends AppCompatActivity implements OnMapReadyCallback {

    static final LatLng NEWYORK = new LatLng(40.671143, -73.963707);
    private GoogleMap googleMap;
    private static final String APP_ID = "b9eca3f1ff3fbbdd654363ad0102e3fe";
    TextView condDescr;
    TextView temp;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // 아래의 OnMapReady() 에서 처리를 GoogleMap 객체관련 처리를 함


        //여기부턴 날씨구현

        double lat = 40.712774, lon = -74.006091;
        String units = "imperial";
        String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
                lat, lon, units, APP_ID);

        TextView textView = (TextView) findViewById(R.id.textView);
        new GetWeatherTask(textView).execute(url);
    }

    private class GetWeatherTask extends AsyncTask<String, Void, String> {
        private TextView textView;

        public GetWeatherTask(TextView textView) {
            this.textView = textView;
        }

        @Override
        protected String doInBackground(String... strings) {
            String weather = "UNDEFINED";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                JSONObject topLevel = new JSONObject(builder.toString());
                JSONObject main = topLevel.getJSONObject("main");
                weather = String.valueOf(main.getDouble("temp"));

                urlConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(String temp) {
            String str = String.format("%.1f", (Double.parseDouble(temp) - 32) / 1.8);
            textView.setText("Current Weather: " + str + "도");
        }
    }


    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;

        Marker brooklyn = googleMap.addMarker(new MarkerOptions().position(NEWYORK)
                .title("브루클린 박물관"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(NEWYORK));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        // marker 표시
        // market 의 위치, 타이틀, 짧은설명 추가 가능.
        MarkerOptions marker = new MarkerOptions();
        marker .position(new LatLng(40.630657, -73.954367))
                .title("브루클린 대학")
                .snippet("Brooklyn College");
        googleMap.addMarker(marker).showInfoWindow(); // 마커추가,화면에출력


        map.addPolyline(new PolylineOptions()
                .add(new LatLng(40.671143, -73.963707), new LatLng(40.630657, -73.954367))
                .width(5)
                .color(Color.RED));

        // 마커클릭 이벤트 처리
        // GoogleMap 에 마커클릭 이벤트 설정 가능.
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // 마커 클릭시 호출되는 콜백 메서드
                Toast.makeText(getApplicationContext(),
                        marker.getTitle() + " 클릭했음"
                        , Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

}
