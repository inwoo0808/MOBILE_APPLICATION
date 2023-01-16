package ddwu.mobile.lbs.locationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.pm.PackageManager;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";
    final int REQ_PERMISSION_CODE = 100; // 권한 식별을 위한 상수 코드

    // Google Play Services 에서 제공하는 위치 확인 관련 클래스:최종 위치, 현재 위치 확인 가능
    FusedLocationProviderClient flpClient;
    Location mLastLocation; // 최종 위치를 담을 Location 변수 선언
    TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvText = findViewById(R.id.tvText);

        // FusedLocationProviderClient 객체 생성
        flpClient = LocationServices.getFusedLocationProviderClient(this);
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
                executeGeocoding(mLastLocation);
                // 실시간 위치나 최종 위치를 매개변수로 넣는다.
                break;
            case R.id.btn_start:
                // 위치정보 수신 시작
                // 위치 정보 확인하는 FusedLocationProviderClient 객체에서 위치 확인 수행 메소드 requestLocationUpdates 에
                // LocationRequest, LocationCallback 객체 매개변수에 넣어 호출
                flpClient.requestLocationUpdates(
                        getLocationRequest(),
                        mLocCallback,
                        Looper.getMainLooper() // 언제 수신할지 몰라서 Looper 객체가 계속 기다림. 시스템이 제공하는 Looper 사용
                );
                break;
            case R.id.btn_stop:
                // 위치정보 수신 종료
                flpClient.removeLocationUpdates(mLocCallback);
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        // 위치정보 수신 종료 - 사용자에 의해 종료가 안 될 경우를 대비해서 onPause()에 구현
        flpClient.removeLocationUpdates(mLocCallback);
    }

    // 권한 확인
    private void checkPermission() {
        // 권환 있나 확인 -> checkSelfPermission
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            // 권한이 있을 경우 수행할 동작
            Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show();
        } else { // 권한 없을 경우
            // 권한 요청 -> requestPermissions
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION_CODE);
        }
    }

    // 퍼미션 결과 요청 올 때 수행되는 메소드 -> 권한 식별 코드 requestCode로 구별, 퍼미션 승인 결과가 매개변수로 들어옴옴
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION_CODE:
                // 퍼미션 승인 결과 없을 수 있어서(granResults) 0개 초과인지 아닌지 확인한다.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "위치권한 획득 완료", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "위치권한 미획득", Toast.LENGTH_SHORT).show();
                }
        }
    }

    // FusedLocationProviderClient 객체에 매개변수로 넣을 값 중 하나
    // 위치 정보 수신 조건을 위한 매개변수 지정
    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest(); // LocationRequest 객체 생성
        locationRequest.setInterval(5000); // 위치 정보 업데이트 간격을 ms 로 지정
        locationRequest.setFastestInterval(1000); // 위치 정보 업데이트의 최소간격 지정(1초)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // 요청 우선순위 및 source 지정(GPS)
        return locationRequest;
    }

    // FusedLocationProviderClient 객체에 매개변수로 넣을 값 중 하나
    // 위치 변경 정보를 수신하는 객체 생성
    LocationCallback mLocCallback = new LocationCallback() {
        // onLocationResult 재정의 -> 위치정보 수신 시 동작 실행
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            // 매개변수로  들어오는 LocationResult는 위치 정보들을 보관하는 클래스 -> 위치 정보 getLocations로 꺼낸다
            // locationResult.getLocations() : 위도, 경도 같아도 위치 정보 여러 개 담길 수 있으므로 List<Location> 객체를 반환함
            for (Location loc : locationResult.getLocations()) {
                double lat = loc.getLatitude();
                double lng = loc.getLongitude();
//                setTvText(String.format("(%.6f, %.6f)", lat, lng));
                mLastLocation = loc; // 최종 위치
                executeGeocoding(mLastLocation);
            }
        }
    };


    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // Location 응답 수신이 성공했을 때 Listener 설정
        flpClient.getLastLocation().addOnSuccessListener(
                new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) { // Location은 위치정보(위도/경도) 들어있음
                        // 최종 위치정보 수신 시 수행할 동작 지정
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            setTvText(String.format("최종위치: (%.6f, %.6f)", latitude, longitude));
                            mLastLocation = location; // 멤버변수로 선언한 Location 객체에 담음
                        } else {
                            Toast.makeText(MainActivity.this, "No location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Location 응답 수신이 실패했을 때 Listener 설정
        flpClient.getLastLocation().addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Unknown");
                    }
                }
        );
    }


    private void executeGeocoding(Location location) {
        if (Geocoder.isPresent()) { // Geocoder 서비스 사용가능 가부 확인
            Toast.makeText(this, "Run Geocoder", Toast.LENGTH_SHORT).show();
            if (location != null)  { // 위치 있으면 ayncTask 실행
                new GeoTask().execute(location); // 객체 생성 및 실행
            }
        } else {
            Toast.makeText(this, "No Geocoder", Toast.LENGTH_SHORT).show();
        }
    }


    class GeoTask extends AsyncTask<Location, Void, List<Address>> {
        // Geocoder 객체 생성 & 사용 -> Geocoder는 네트워크를 통해 정보 획득, 별도 스레드에서 실행
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

        @Override
        protected List<Address> doInBackground(Location... locations) {
            List<Address> addresses = null;
            try {
                // Geocoding 수행 -> getFromLocation에 위도, 경도, 결과 개수를 넣어 호출 -> <List>Address 반환
                addresses = geocoder.getFromLocation(locations[0].getLatitude(),
                        locations[0].getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) { // 결과 사용
            Address address = addresses.get(0);
            Toast.makeText(MainActivity.this, address.getAddressLine(0), Toast.LENGTH_SHORT).show();
            setTvText(address.getAddressLine(0));
            // address 객체를 보면, . 누르면 멤버들 나오는데 많은 정보가 있다. 주소 정보 여러 가지 있음. getAddressLine은 주소를 의미.
        }
    }

    private void setTvText(String text) {
        String before = tvText.getText() + System.getProperty("line.separator");
        tvText.setText(before + text);
    }

}