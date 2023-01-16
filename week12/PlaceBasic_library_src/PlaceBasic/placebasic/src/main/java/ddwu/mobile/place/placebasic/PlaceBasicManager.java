package ddwu.mobile.place.placebasic;

import android.util.Log;

import java.util.List;

import ddwu.mobile.place.placebasic.pojo.PlaceBasic;
import ddwu.mobile.place.placebasic.pojo.PlaceBasicRoot;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceBasicManager {
    final static String TAG = "BasicPlaceManager";
    final String BASIC_URL  = "https://maps.googleapis.com/";
    final String LANGUAGE = "ko";

    private String googleKey;

    private Retrofit retrofit;
    private INearPlaceService iNearPlaceService;

    private OnPlaceBasicResult onPlaceBasicResult;


    public PlaceBasicManager(String key) {
        this.googleKey = key;
        if (retrofit == null) {
            try {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASIC_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        iNearPlaceService = retrofit.create(INearPlaceService.class);
    }

    public void searchPlaceBasic(double latitude, double longitude, int radius, String type) {
        String loc = latitude + "," + longitude;
        Call<PlaceBasicRoot> apiCall
                = iNearPlaceService.getPlaceBasicInfo(googleKey, LANGUAGE, loc, radius, type);
        apiCall.enqueue(placeBasicInfoCallback);
        Log.d(TAG, String.format("Search %s at %s in %d m", type, loc, radius));
    }

    Callback<PlaceBasicRoot> placeBasicInfoCallback = new Callback<PlaceBasicRoot>() {
        @Override
        public void onResponse(Call<PlaceBasicRoot> call, Response<PlaceBasicRoot> response) {
            if (response.isSuccessful()) {
                PlaceBasicRoot placeBasicRoot = response.body();
                List<PlaceBasic> placeBasicList = placeBasicRoot.getResults();

                if (placeBasicList.size() == 0) {
                    Log.e(TAG, "ERROR!!! CHECK YOUR API KEY or SEARCH TYPE");
                }
                onPlaceBasicResult.onPlaceBasicResult(placeBasicList);
            }
        }

        @Override
        public void onFailure(Call<PlaceBasicRoot> call, Throwable t) {
            t.printStackTrace();
        }
    };

    public void setOnPlaceBasicResult(OnPlaceBasicResult onPlaceBasicResult) {
        this.onPlaceBasicResult = onPlaceBasicResult;
    }
}
