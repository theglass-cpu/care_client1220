package org.techtown.care_test01;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PolylineOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class directions_Activity extends AppCompatActivity implements
        OnMapReadyCallback,
        NaverMap.OnCameraChangeListener,
        NaverMap.OnCameraIdleListener{


    private static final String TAG = "Cannot invoke method length() on null object";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
    private FusedLocationSource locationSource;
    //?????????????????????????????????
    private NaverMap d_map;
    private Geocoder geocoder;

    private int addresscode = 100;
    String appKey = "l7xxff589d8eecc140d089572773a958ab84";

    int a = 0;

    //????????????
    TextView start_point_text,end_point_text,route_text;


    ImageView car_bt,walk_bt;

    Button d_result_bt;
    View.OnClickListener clickListener;
    GpsTracker gpsTracker;

    LatLng start_latLng,end_latlng;
    List<Address> addressList = null;
    double currentLatitude,currentLongitude;
    private List<PolylineOverlay> polylineOverlayList =new ArrayList<PolylineOverlay>();

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        start_point_text=findViewById(R.id.start_point_text);
        end_point_text=findViewById(R.id.end_point_text);
        d_result_bt =findViewById(R.id.d_result_bt);
        car_bt=findViewById(R.id.car_bt);
        route_text=findViewById(R.id.route_text);
        walk_bt=findViewById(R.id.walk_bt);
        //?????? ??????????????? ?????? ??????.
        walk_bt.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.wswsws));
        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MapFragment mapFragment= (MapFragment) fragmentManager.findFragmentById(R.id.d_map);

        if(mapFragment==null){
            mapFragment = MapFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.d_map, mapFragment).commit();
        }
        //getMapAsync??? ???????????? ???????????? onMapReady?????? ????????? ??????
        //onMapReady?????? NaverMap????????? ??????
        mapFragment.getMapAsync(this);

        setClickListener();




        end_latlng =new LatLng(Double.parseDouble(getIntent().getStringExtra("?????????x")),
                Double.parseDouble(getIntent().getStringExtra("?????????y")));
        Log.e(TAG, "???????????????: "+end_latlng );
        end_point_text.setText(getIntent().getStringExtra("???????????????"));

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // ?????? ?????????


            }
            d_map.setLocationTrackingMode(LocationTrackingMode.Follow);
            Log.e(TAG, "????????" );

            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.d_map =naverMap;
        geocoder=new Geocoder(this, Locale.KOREAN);
        d_map.getUiSettings().setLocationButtonEnabled(false);
        d_map.setLocationSource(locationSource);
        d_map.getUiSettings().setLogoGravity(Gravity.BOTTOM);
        LocationButtonView locationButtonView = findViewById(R.id.d_location);
        locationButtonView.setMap(d_map);
        Marker marker =new Marker();
        marker.setWidth(80);
        marker.setHeight(80);
        marker.setIcon(OverlayImage.fromResource(R.drawable.placeholder));
        marker.setAnchor(new PointF(0.5f, 1.0f));
        marker.setPosition(end_latlng);
        marker.setMap(d_map);
        LatLng initialPosition = new LatLng(37.48538829999998, 126.97219670000005);
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(initialPosition);
        naverMap.moveCamera(cameraUpdate);
        ActivityCompat.requestPermissions(this, PERMISSIONS, 1000);

        //????????? ???????????? ????????????
        d_map.addOnCameraChangeListener(this);
        //????????? ????????? ???????????? ????????????
        d_map.addOnCameraIdleListener(this);


        //???????????? gps tracher;
        gpsTracker = new GpsTracker(directions_Activity.this);
        currentLatitude= gpsTracker.getLatitude();
        currentLongitude = gpsTracker.getLongitude();
        start_latLng =new LatLng(currentLatitude,currentLongitude);
        Log.e(TAG, "gps t  ???????????? ??? ????????????"+start_latLng);

    }


    //click_event

    public void setClickListener(){
        clickListener=new View.OnClickListener() {
            @SuppressLint({"LongLogTag", "NonConstantResourceId"})
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.start_point_text:

                        Log.e(TAG, "onClick: " );

                        int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                        if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                            Log.i("?????????????????????", "??????????????? ??????");
                            Intent i = new Intent(getApplicationContext(), AddressApiActivity.class);
                            // ???????????? ??????????????? ?????????
                            overridePendingTransition(0, 0);

                            // ????????????
                            resultLauncher.launch(i);

                        } else {
                            Toast.makeText(getApplicationContext(), "????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                        }

                    break;


                    case R.id.walk_bt:
                        car_bt.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.car));
                        walk_bt.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.wswsws));
                        a=0;
                        route_text.setText("????????????");
                        break;


                    case  R.id.car_bt:
                        car_bt.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.cccc));
                        walk_bt.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.wwwww));
                        a=1;
                        route_text.setText("????????? ??????");
                        break;


                    case R.id.d_result_bt:

                        //   Log.e(TAG, "????????? ?????????"+start_latLng);
                        Log.e(TAG, "????????? ?????????"+end_latlng);

                        MapTask mapTask =new MapTask();
                        mapTask.execute();

                        break;
                }

            }
        };
        start_point_text.setOnClickListener(clickListener);
        d_result_bt.setOnClickListener(clickListener);
        car_bt.setOnClickListener(clickListener);
        walk_bt.setOnClickListener(clickListener);

    }

    ActivityResultLauncher<Intent> resultLauncher =registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==addresscode){
                        assert result.getData() != null;
                        Bundle bundle = result.getData().getExtras();
                        String[] mobNum =bundle.getString("data").split("\\|");
                        Log.e(TAG, "??????????: "+bundle.getString("data") );
                        String adress = mobNum[1];
                        start_point_text.setText(adress);
                        try {
                            setAddressList(adress);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }


                }
            }
    );
    @SuppressLint("LongLogTag")
    public void setAddressList(String name) throws IOException {
        addressList =geocoder.getFromLocationName(name , 10);
        if(addressList!=null){
            if(addressList.size()==0){
                Log.e(TAG, "nofind adress" );
            }else {

                currentLatitude=addressList.get(0).getLatitude();
                currentLongitude=addressList.get(0).getLongitude();

                start_latLng=new LatLng(currentLatitude,currentLongitude);

                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(start_latLng);
                d_map.moveCamera(cameraUpdate);


                Marker marker =new Marker();
                marker.setWidth(100);
                marker.setHeight(100);
                marker.setIcon(OverlayImage.fromResource(R.drawable.rerere));
                marker.setAnchor(new PointF(0.5f, 1.0f));
                marker.setPosition(start_latLng);
                marker.setMap(d_map);

            }
        }
    }

    @SuppressLint("LongLogTag")
    public String TMapURL(LatLng startPoint, LatLng endPoint) throws UnsupportedEncodingException {
        String url = null;
        String car = null;


        String startX = new Double(startPoint.longitude).toString();
        String startY = new Double(startPoint.latitude).toString();
        String endX = new Double(endPoint.longitude).toString();
        String endY = new Double(endPoint.latitude).toString();

        Log.e(TAG, "Tmap url_______startX"+startX );
        Log.e(TAG, "Tmap url_______startY"+startY );
        Log.e(TAG, "Tmap url_______endX"+endX );
        Log.e(TAG, "Tmap url_______endY"+endY );
        String startName = URLEncoder.encode("?????????", "UTF-8");
        String endName = URLEncoder.encode("?????????", "UTF-8");

        if(a==0){
            url = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&callback=result&appKey=" + appKey
                    + "&startX=" + startX + "&startY=" + startY + "&endX=" + endX + "&endY=" + endY
                    + "&startName=" + startName + "&endName=" + endName;

        }else{
            url = "https://apis.openapi.sk.com/tmap/routes?version=1&callback=result&appKey=" + appKey
                    + "&startX=" + startX + "&startY=" + startY + "&endX=" + endX + "&endY=" + endY
                    + "&startName=" + startName + "&endName=" + endName;


        }






        return url;
    }
    public class MapTask extends AsyncTask<Void, Void , String> {


        @Override
        protected String doInBackground(Void... voids) {
            String result ="";
            return result ;
        }

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e(TAG, "onPostExecute: "+gpsTracker.getLocation() );
            start_latLng = new LatLng(currentLatitude,currentLongitude);
            try {
                String url =TMapURL(start_latLng, end_latlng);
                Log.e(TAG, "onPostExecute: "+url );
                //????????? url??? ????????? ???????????? ????????????
                NTask nTask = new NTask(url, null);
                nTask.execute();


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }




        }
    }


    //URL??? ????????? ???????????? ?????????

    public  class NTask extends  AsyncTask<Void, Void , String> {

        private String url;
        private ContentValues values;
        ArrayList<LatLng> latLngArrayList=new ArrayList<LatLng>();
        Marker marker = new Marker();

        public NTask(String url,ContentValues values){
            this.url = url;
            this.values = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(Void... params) {
            String result;
            // ?????? ????????? ????????? ??????.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values);
            // ?????? URL??? ?????? ???????????? ????????????.
            Log.e(TAG, "doInBackground: "+result);
            return result;
        }

        @SuppressLint({"LongLogTag", "SetTextI18n"})
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e(TAG, "onPostExecute: "+start_latLng );
            Log.e(TAG, "onPostExecute: "+end_latlng);

            try {
                JSONObject root =new JSONObject(s);
               // Log.e(TAG, "????????????"+root );

                //??? ?????? ?????? featuresArray??? ??????
                //features ???????????? ?????? ?????? ?????? ??? json array ??? ????????????
                JSONArray featuresArray = root.getJSONArray("features");

                for (int i = 0; i < featuresArray.length(); i++){
                    JSONObject featuresIndex = (JSONObject) featuresArray.get(i);
                    //???????????? ??? ??????????????? ??? ??????
                    JSONObject geometry =  featuresIndex.getJSONObject("geometry");
                    String type =  geometry.getString("type");



                    //????????? ????????? ???????????? ???????????? ?????? ?????? ??????????????????
                    if(type.equals("LineString")){
                        JSONArray coordinatesArray = geometry.getJSONArray("coordinates");
                        for (int j = 0; j < coordinatesArray.length(); j++){
                            JSONArray pointArray = (JSONArray) coordinatesArray.get(j);
                            double longitude =Double.parseDouble(pointArray.get(0).toString());
                            double latitude =Double.parseDouble(pointArray.get(1).toString());
                         //   Log.e(TAG, "onPostExecute:???????????? ??????  "+j+";;;;;" +longitude+"\n"+latitude  );
                            latLngArrayList.add(new LatLng(latitude, longitude));
                        }

                    }

                    if(type.equals("Point")) {
                        JSONObject properties = featuresIndex.getJSONObject("properties");
                        try {
                            double totalDistance = Integer.parseInt(properties.getString("totalDistance"));
                            Log.e(TAG, "??? ?????? :" + totalDistance / 1000 + " km");
                            int totalTime = Integer.parseInt(properties.getString("totalTime"));
                            int day = totalTime / (60 * 60 * 24);
                            int hour = (totalTime - day * 60 * 60 * 24) / (60 * 60);
                            int minute = (totalTime - day * 60 * 60 * 24 - hour * 3600) / 60;
                            int second = totalTime % 60;
                            route_text.setText("??? ?????? ?????? "+day + "??? " + hour + "?????? " + minute + "??? " + second
                                    + "???");
                            System.out.println(day + "??? " + hour + "?????? " + minute + "??? " + second
                                    + "???");



//                            Log.e(TAG, "onPostExecute: "+totalTime/(1000*60*60));
//                            Log.e(TAG, "??? ?????? :" + totalTime / 60 + "???");
                            msg.toast(getApplicationContext(),day + "??? " + hour + "?????? " + minute + "??? " + second
                                    + "???");
                            String pointType = properties.getString("pointType");


                        }catch (Exception e){

                        }

                        String pointType = properties.getString("pointType");
                        double longitude =  Double.parseDouble(geometry.getJSONArray("coordinates").get(0).toString());
                        double latitude =  Double.parseDouble(geometry.getJSONArray("coordinates").get(1).toString());
                        if(pointType.equals("SP")){
                            System.out.println("??????????????????");

                        }
                        else if(pointType.equals("GP")){

                            System.out.println("??????????????????");
                        }
                        else if(pointType.equals("EP")){

                            System.out.println("???????????????");

                        }

                    }
                 //   Log.e(TAG, i+"??? ?????? ????????? ????????? ?????? ???????????? ??????" );




                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            resetline_List();

            PolylineOverlay polylineOverlay = new PolylineOverlay();
            polylineOverlay.setCoords(latLngArrayList);
            polylineOverlay.setWidth(10);
            if(a==1){
                polylineOverlay.setColor(Color.parseColor("#ff0d05"));
            }else{
                polylineOverlay.setColor(Color.parseColor("#316900"));
            }
            polylineOverlay.setCapType(PolylineOverlay.LineCap.Round);
            polylineOverlay.setJoinType(PolylineOverlay.LineJoin.Round);

            polylineOverlay.setMap(d_map);
            polylineOverlayList.add(polylineOverlay);
            Log.e(TAG, "onPostExecute: "+polylineOverlayList.size());


        }
    }
    public void resetline_List(){

        if(polylineOverlayList!=null||polylineOverlayList.size()>0){
            for (PolylineOverlay polylineOverlay : polylineOverlayList){
                polylineOverlay.setMap(null);
            }
            polylineOverlayList.clear();

        }


    }


    @Override
    public void onCameraChange(int i, boolean b) {

    }

    @Override
    public void onCameraIdle() {

    }


}