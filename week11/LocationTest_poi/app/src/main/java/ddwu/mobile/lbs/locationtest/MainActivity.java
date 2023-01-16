package ddwu.mobile.lbs.locationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";
    final int REQ_PERMISSION_CODE = 100;

    TextView tvText;
    FakeParser parser;
    FusedLocationProviderClient flpClient; // 위치 정보 수신
    Location mLastLocation; // 최종 위치 저장할 멤버 변수

    private GoogleMap mGoogleMap;       // 지도를 저장할 멤버변수 GoogleMap 객체
    private Marker mCenterMarker;         // 중앙 표시 Marker
    private Marker poiMarker;
    private Polyline mPolyline;     // Google Map library 에서 제공하는 지도 상 line 그릴 수 있는 객체

    ArrayList<Marker> markerList = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvText = findViewById(R.id.tvText);

        parser = new FakeParser();      // 모의 parser 생성

        flpClient = LocationServices.getFusedLocationProviderClient(this); // 위치 정보 수신

        // Map Fragment 가져와서 지도 로딩 실행
        SupportMapFragment mapFragment
                = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallback); // Google Map을 서버로부터 받아오는데, Async 사용해서 비동기로 받아온다.
    }

    // getMapAsync로 map 정보가 언제 다 날라올지 모르기 때문에 비동기 처리로 받고, OnMapReadyCallback 객체 생성
    OnMapReadyCallback mapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mGoogleMap = googleMap;
            // 이 시점부터 코드 상에서 지도 제어 및 사용 가능

            // 지도 위치 이동하기 - 1. 특정 위치로 이동하기
            LatLng latLng = new LatLng(37.606320, 127.041808);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

            // 지도 특정 위치에 마커 추가 - 지도 준비되고 마커 추가해야 하므로 onMapReady 에 작성
            // 마커 옵션 설정
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title("현재 위치")
                    .snippet("이동중")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            // 지도에 마커 추가 - 반환 값은 Marker (제거, 이동 등 제어 가능)
            mCenterMarker = mGoogleMap.addMarker(markerOptions);
            mCenterMarker.showInfoWindow();


            // 지도 롱클릭 이벤트 처리
            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(@NonNull LatLng latLng) {
                    executeGeocoding(latLng);
                }
            });

            // 마커 클릭 이벤트 - 마커 위의 윈도우 클릭 이벤트 처리
            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(@NonNull Marker marker) {
                    Toast.makeText(MainActivity.this, "marker window click : " + marker.getId() , Toast.LENGTH_SHORT).show();
                }
            });
        }
    };


    // FusedLocationProviderClient 객체 생성할 때 넣는 매개변수 중 하나
    LocationCallback mLocCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) { // 전달받은 LocationResult 로 지도 위치 이동
            for (Location loc : locationResult.getLocations()) {
                double lat = loc.getLatitude();
                double lng = loc.getLongitude();
//                setTvText(String.format("(%.6f, %.6f)", lat, lng));
                
                // 지도 위치 이동하기 - 2. GPS 수신 위치로 이동하기 : FusedLocationProviderClient, LocationCallback 사용
                mLastLocation = loc;
                LatLng currentLoc = new LatLng(lat, lng);
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));

                executeGeocoding(currentLoc); // 출력

                // 지도 마커 위치 이동
                mCenterMarker.setPosition(currentLoc);

                // 지도 선을 그리기 위한 지점(위도/경도) 추가
                List<LatLng> latLngs = mPolyline.getPoints(); // Google Map 의 Polyline 에서 getPoints()로 지점 get
                latLngs.add(currentLoc); // 위치 리스트에 현재 위치 추가
                mPolyline.setPoints(latLngs); // 추가된 위치 리스트를 Polyline 에 setPoints()로 다시 set
            }
        }
    };


    // FusedLocationProviderClient 객체 생성할 때 넣는 매개변수 중 하나
    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_permission:
                checkPermission();
                break;
            case R.id.btn_get_last:
                getLastLocation();
                break;
            case R.id.btn_geocoding:
                LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                executeGeocoding(latLng);
                break;
            case R.id.btn_start:
                flpClient.requestLocationUpdates(
                        getLocationRequest(),
                        mLocCallback,
                        Looper.getMainLooper()
                );
//            지도 poly line 추가 -> 선그리기 준비
//            강의자료 내용과 별도의 내용으로 polyOption 을 멤버변수로 선언하는 것이 아닌
//            지도에 추가 후 반환하는 PolyLine 을 멤버변수로 선언하고 관리
                PolylineOptions polylineOptions = new PolylineOptions()
                        .color(Color.BLUE)
                        .width(5);
                mPolyline = mGoogleMap.addPolyline(polylineOptions); // 선 그리기 수행
                break;
            case R.id.btn_stop:
                flpClient.removeLocationUpdates(mLocCallback);

                // 지도 polyline 제거
                mPolyline.remove();
                break;
            case R.id.btn_poi:
                String url = "fake url";
                new NetworkAsyncTask().execute(url);
                break;

        }
    }


    // 위치 정보 수신 종료 안 될 때를 대비해 onPause()에도 작성
    @Override
    protected void onPause() {
        super.onPause();
        flpClient.removeLocationUpdates(mLocCallback);
    }


    // 요청 권한 결과 올 때 
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "위치권한 획득 완료", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "위치권한 미획득", Toast.LENGTH_SHORT).show();
                }
        }
    }


    // 권환 획득 
    private void checkPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            // 권한이 있을 경우 수행할 동작
            Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show();
        } else {
            // 권한 요청
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION_CODE);
        }
    }


    // 최종 위치 확인
    private void getLastLocation() {
        // 권한
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // 최종 위치 확인 되었을 때
        flpClient.getLastLocation().addOnSuccessListener(
                new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            setTvText(String.format("최종위치: (%.6f, %.6f)", latitude, longitude));
                            mLastLocation = location;
                        } else {
                            Toast.makeText(MainActivity.this, "No location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        flpClient.getLastLocation().addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Unknown");
                    }
                }
        );

    }


    // Geocoding 실행
    private void executeGeocoding(LatLng latLng) {
        if (Geocoder.isPresent()) {
            Toast.makeText(this, "Run Geocoder", Toast.LENGTH_SHORT).show();
            if (latLng != null)  new GeoTask().execute(latLng); // AsyncTask로 Geocoding 실행
        } else {
            Toast.makeText(this, "No Geocoder", Toast.LENGTH_SHORT).show();
        }
    }


    // AsyncTask로 Geocoding 실행
    class GeoTask extends AsyncTask<LatLng, Void, List<Address>> {
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        @Override
        protected List<Address> doInBackground(LatLng... latLngs) {
            List<Address> addresses = null;
            try { // Geocoding 실행 : 위도 경도 -> 실 주소
                addresses = geocoder.getFromLocation(latLngs[0].latitude,
                        latLngs[0].longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        // Geocoding 결과 이용 -> 실 주소 출력
        @Override
        protected void onPostExecute(List<Address> addresses) {
            if (addresses != null) {
                Address address = addresses.get(0);
//                Toast.makeText(MainActivity.this, address.getAddressLine(0), Toast.LENGTH_SHORT).show();
                setTvText(address.getAddressLine(0));
            }
        }
    }


    private void setTvText(String text) {
        String before = System.getProperty("line.separator") + tvText.getText() ;
        tvText.setText(text + before );
    }


//    실제 앱을 구현할 때는 네트워크 AsyncTask로 구현할 것
    class NetworkAsyncTask extends AsyncTask<String, Integer, String> {

        public final static String TAG = "NetworkAsyncTask";
        private ProgressDialog apiProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            apiProgressDialog = ProgressDialog.show(MainActivity.this, "Wait", "Downloading...");
        }

        @Override
        protected String doInBackground(String... strings) {

//            NetworkAsyncTask 는 네트워크 작업을 실제 실행하지는 않으며 잠시 시간 대기만 수행
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Open API search is completed";
        }

        @Override
        protected void onPostExecute(String result) {

//            작업 수행 후 가상 parsing 수행
            List<POI> poiList = parser.parse(result);
            apiProgressDialog.dismiss();

//            poiList 의 POI 로 마커 추가 기능 수행
            for (POI poi : poiList) {
                // poi address 역 geocoding해서 위도 경도 알아내고 set해준다
                if (poi != null)  new GeoTask2().execute(poi);

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(poi.getLatitude(), poi.getLongitude()))
                        .title(poi.getTitle())
                        .snippet(poi.getPhone())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                poiMarker = mGoogleMap.addMarker(markerOptions);
                poiMarker.showInfoWindow();

                poiMarker.setPosition(new LatLng(poi.getLatitude(), poi.getLongitude()));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(poi.getLatitude(), poi.getLongitude())));

                markerList.add(poiMarker); // 보관
            }
        }
    }

    // POI 객체를 전달 받아 위도 경도를 확인하는 Geocoder 를 사용하는 AsyncTask 구현
    class GeoTask2 extends AsyncTask<POI, Void, LatLng> {
        Geocoder geocoder = new Geocoder(MainActivity.this);
        @Override
        protected LatLng doInBackground(POI... pois) {
            List<Address> addresses = null;
            LatLng latLng = null;
            try {
                addresses = geocoder.getFromLocationName(pois[0].getAddress(), 1);
                pois[0].setLatitude(addresses.get(0).getLatitude());
                pois[0].setLongitude(addresses.get(0).getLongitude());
                latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return latLng;
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            if (latLng != null) {

            }
        }
    }


}