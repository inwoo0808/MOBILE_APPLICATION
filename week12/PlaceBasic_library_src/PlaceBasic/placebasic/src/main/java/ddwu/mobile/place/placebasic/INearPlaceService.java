package ddwu.mobile.place.placebasic;

import ddwu.mobile.place.placebasic.pojo.PlaceBasicRoot;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface INearPlaceService {
    @GET("/maps/api/place/nearbysearch/json")
    Call<PlaceBasicRoot> getPlaceBasicInfo(@Query("key") String key,
                                           @Query("language") String lang,
                                           @Query("location") String location,
                                           @Query("radius") int radius,
                                           @Query("type") String type);
}
