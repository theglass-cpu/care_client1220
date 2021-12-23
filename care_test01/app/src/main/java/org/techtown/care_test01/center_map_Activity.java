package org.techtown.care_test01;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class center_map_Activity extends AppCompatActivity implements
        OnMapReadyCallback,
        NaverMap.OnCameraChangeListener,
        NaverMap.OnCameraIdleListener,
        NaverMap.OnMapClickListener,
        ExampleBottomSheetDialog.BottomSheetListener,
        Overlay.OnClickListener{

    private static final String TAG = "Cannot invoke method length() on null object";

    //위치 잡는변수
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
    private FusedLocationSource locationSource;


    //레이아웃
    private NaverMap n_map;
    private ImageView search_bt;
    private TextView r_name;
    private Button data_list;


    //result_ok 값
    private int addresscode = 100;

    //click event
    View.OnClickListener clickListener;

    LatLng latLng;

    //gecode랑 세트
    List<Address> addressList = null;
    private Geocoder geocoder;

    //카메라 계속갱신해서 가져옴
    private boolean isCameraAnimated = false;

    //클릭이벤트 말풍선
    private InfoWindow infoWindow;

    //마커담을변수
    public static ArrayList<marker> naver_marker_arrayList = new ArrayList<>();

    //초기화시킬 마커
    private List<Marker> markerList = new ArrayList<Marker>();

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_map);

        search_bt = findViewById(R.id.search_bt);
        r_name = findViewById(R.id.r_name);
        data_list = findViewById(R.id.data_list);

        //지도 사용권한을 받아 온다.
        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.n_map);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.n_map, mapFragment).commit();
        }
        //getMapAsync를 호출하여 비동기로 onMapReady콜백 메서드 호출
        //onMapReady에서 NaverMap객체를 받음
        mapFragment.getMapAsync(this);

        //이것은 클릭리스너
        setClickListener();

    }


    @SuppressLint("LongLogTag")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨


            }
            n_map.setLocationTrackingMode(LocationTrackingMode.Follow);
            Log.e(TAG, "성공??" );

            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.n_map=naverMap;
        geocoder=new Geocoder(this, Locale.KOREAN);
        infoWindow=new InfoWindow();
        n_map.getUiSettings().setLocationButtonEnabled(false);
        n_map.setLocationSource(locationSource);
        n_map.getUiSettings().setLogoGravity(Gravity.BOTTOM);
        LocationButtonView locationButtonView = findViewById(R.id.n_location);
        locationButtonView.setMap(n_map);

        LatLng initialPosition = new LatLng(37.48538829999998, 126.97219670000005);
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(initialPosition);
        naverMap.moveCamera(cameraUpdate);
        ActivityCompat.requestPermissions(this, PERMISSIONS, 1000);

        ;
        //이것은 카메라가 움직일때
        n_map.addOnCameraChangeListener(this);
        //이것은 카메라 움직임이 멈췃을때
        n_map.addOnCameraIdleListener(this);

        infoWindow.setAdapter(new InfoWindow.DefaultViewAdapter(this) {
            @SuppressLint("LongLogTag")
            @NonNull
            @Override
            protected View getContentView(@NonNull InfoWindow infoWindow) {
                Marker marker = infoWindow.getMarker();
                View view = View.inflate(center_map_Activity.this,R.layout.view_info_window,null);
                assert marker != null;
                Log.e(TAG, "오 테그 가져옴: "+marker.getTag() );
                String str = Objects.requireNonNull(marker.getTag()).toString();
                String[] mobNum = str.split("\\|");
                ((TextView)view.findViewById(R.id.name)).setText(mobNum[0]);
                ((TextView)view.findViewById(R.id.address)).setText(mobNum[1]);
                ((TextView)view.findViewById(R.id.tell)).setText(mobNum[2]);
                ExampleBottomSheetDialog bottomSheetDialog = new ExampleBottomSheetDialog();
                Bundle args = new Bundle();
                args.putString("찾아갈위치",str);

                bottomSheetDialog.setArguments(args);
                bottomSheetDialog.show(getSupportFragmentManager(), "exampleBottomSheet");


                return view;
            }
        });

    }


    public void setClickListener(){
        clickListener=new View.OnClickListener() {
            @SuppressLint({"LongLogTag", "NonConstantResourceId"})
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                  case  R.id.search_bt:
                      Log.e(TAG, "지역검색버튼 누름" );
                      Log.e(TAG, "지역검색버튼 누름" );

                      Log.e(TAG, "주소 검색벝늨 클릭" );
                      int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                      if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                          Log.i("주소설정페이지", "주소입력창 클릭");
                          Intent i = new Intent(getApplicationContext(), AddressApiActivity.class);
                          // 화면전환 애니메이션 없애기
                          overridePendingTransition(0, 0);

                          // 주소결과
                          resultLauncher.launch(i);

                      } else {
                          Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                      }

                      break;

                    case R.id.data_list :
                        Log.e(TAG, "데이터리스트 클릭" );
                           Intent intent = new Intent(getApplicationContext(),center_data_list_Activity.class);
                                                        startActivity(intent);
                    break;
                }
            }
        };

        search_bt.setOnClickListener(clickListener);
        data_list.setOnClickListener(clickListener);

    }


    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() ==addresscode) {
                        assert result.getData() != null;
                        Bundle extras = result.getData().getExtras();
                        Log.e(TAG, "onActivityResult: " +extras.getString("data"));
                        String[] mobNum =extras.getString("data").split("\\|");
                        String adress = mobNum[1];
                        r_name.setText(adress);
                        try {
                            setAddressList(adress);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });


    @SuppressLint("LongLogTag")
    public void setAddressList(String name) throws IOException {
        addressList =geocoder.getFromLocationName(name , 10);
        if(addressList!=null){
            if(addressList.size()==0){
                Log.e(TAG, "nofind adress" );
            }else {

                latLng=new LatLng(addressList.get(0).getLatitude(),addressList.get(0).getLongitude());

                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng);
                n_map.moveCamera(cameraUpdate);
            }
        }
    }

    @Override
    public void onCameraChange(int reason , boolean animated) {
        isCameraAnimated = animated;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onCameraIdle() {

        if(isCameraAnimated){
            Log.e(TAG, "카메라이동" );
            String lat = String.valueOf(n_map.getCameraPosition().target.latitude);
            String lon = String.valueOf(n_map.getCameraPosition().target.longitude);

            Handler mHandler = new Handler();
            try {
                String result = new Naver_marker_data().execute(lon,lat,"").get();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // 비동기처리 방식으로 가져온 데이터를 바탕으로 마커를 찍어주자
                                drawMarker();

                            }
                        });


                    }
                });
                t.start();

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

    @SuppressLint("LongLogTag")
    public void  drawMarker() {
        resetMarkerList();
        //카메라 이동할때마다 마커가 쌓이면안돼니 마커를 지워주자
        if(naver_marker_arrayList.size()>0||naver_marker_arrayList!=null){
            Log.e(TAG, "drawMarker: "+naver_marker_arrayList.size() );
            for (int i = 0; i < naver_marker_arrayList.size(); i++){
                //마커 데이타 개수만큼 마커를 생성시켜주자
                Marker marker = new Marker();
                marker.setWidth(80);
                marker.setHeight(80);
                marker.setIcon(OverlayImage.fromResource(R.drawable.placeholder));
                marker.setTag(naver_marker_arrayList.get(i).getName()+"|"+
                        naver_marker_arrayList.get(i).getRoadAddress()+"|"+
                        naver_marker_arrayList.get(i).getTell()+"|"+
                        naver_marker_arrayList.get(i).getX()+"|"+
                        naver_marker_arrayList.get(i).getY());
                marker.setAnchor(new PointF(0.5f, 1.0f));
                //마커를 찍을 위치를 갱신시켜주자
                marker.setPosition(new LatLng(naver_marker_arrayList.get(i).getY(), naver_marker_arrayList.get(i).getX()));
                marker.setOnClickListener(this);
                //마커를 클릭하였을때

                marker.setMap(n_map);
                markerList.add(marker);

            }

        }
    }

    public void resetMarkerList(){



        if (markerList != null || markerList.size() > 0) {
            for (Marker marker : markerList) {
                marker.setMap(null);

            }
            markerList.clear();
        }else{

        }


    }

    @Override
    public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
        if(infoWindow.getMarker()!=null){
            infoWindow.close();
        }
    }


    @Override
    public void onButtonClicked(String text) {

    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onClick(@NonNull Overlay overlay) {
        if (overlay instanceof Marker) {
            Marker marker = (Marker) overlay;
            if (marker.getInfoWindow() != null) {
                infoWindow.close();
            } else {
                infoWindow.open(marker);
                Log.e(TAG, "어떤거 클릭햇는지 이름 가져옴 "+marker.getTag() );
            }
            return true;
        }


        return false;
    }

    public static class Naver_marker_data extends AsyncTask<String, String ,String> {

        JSONObject doc_jsonObject,marker_add_jsonObject;
        JSONArray doc_jsonArray,marker_add_jsonArray;

        ArrayList<marker> marker_arrayList = new ArrayList<>();
        String number ;

        private static final String TAG = "Cannot invoke method length() on null object";

        @Override
        protected String doInBackground(String... strings) {
            Log.d("Task3", "POST");
            String temp = "Not Gained";
            try {
                temp = GET(strings[0],strings[1],strings[2]);
                Log.d("REST", temp);
                return temp;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return temp;
        }

        @SuppressLint("LongLogTag")
        private String GET(String x, String y , String d) throws IOException, JSONException {

            Log.e(TAG, "x"+x );
            Log.e(TAG, "y"+y );

            String api = "https://map.naver.com/v5/api/search?caller=pcweb&query=요양원&type=all&searchCoord=" +
                    x+";" +
                    y+
                    "&page=1&displayCount=20&lang=ko";


            Log.e(TAG, "GET: "+api );

            Document doc = Jsoup.connect(api).ignoreContentType(true).get();
            //jsoup 으로 네이버 웹 데이터를 크롤링 해옴
            String text = doc.text();
            //테그사이의 문자열만 필요함으로 텍스트만 추출

            center_map_Activity.naver_marker_arrayList=new ArrayList<>();

            doc_jsonArray=new JSONArray();
            marker_add_jsonObject=new JSONObject();
            marker_add_jsonArray=new JSONArray();
            doc_jsonObject =new JSONObject(text);
            doc_jsonArray.put(doc_jsonObject);
            Log.e(TAG, "최종값"+doc_jsonArray.length());
            for (int i = 0; i < doc_jsonArray.length(); i++){
                doc_jsonObject=doc_jsonArray.getJSONObject(i);
                marker_add_jsonArray = new JSONArray(doc_jsonObject.getJSONObject("result").getJSONObject("place").getString("list"));

                for (int k = 0; k < marker_add_jsonArray.length(); k++){
                    marker_add_jsonObject = marker_add_jsonArray.getJSONObject(k);
                    if(!marker_add_jsonObject.getString("tel").equals("")){
                        number =marker_add_jsonObject.getString("tel");
                    }else{
                        number="";
                    }
                    naver_marker_arrayList.add(new marker(
                            marker_add_jsonObject.getString("name"),
                            marker_add_jsonObject.getString("roadAddress"),
                            number,
                            Double.parseDouble(marker_add_jsonObject.getString("x")),
                            Double.parseDouble(marker_add_jsonObject.getString("y"))



                    ));

                }


            }


            return marker_arrayList.toString();
        }



    }

}