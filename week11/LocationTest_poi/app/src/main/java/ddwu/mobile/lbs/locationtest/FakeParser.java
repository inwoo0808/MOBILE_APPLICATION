package ddwu.mobile.lbs.locationtest;

import java.util.ArrayList;
import java.util.List;

/**
 * 실제 앱을 개발할 때는 적절한 Parser 로 구현할 것
 */

public class FakeParser {

    public List parse(String xml) {
        List poiList = new ArrayList<POI>();

        poiList.add(new POI("이디야 커피 동덕여대점", "02-941-6402", "서울 성북구 화랑로 99"));
        poiList.add(new POI("마놀린", "02-919-7904", "서울 성북구 화랑로 13길 38"));
        poiList.add(new POI("카페어바웃", "02-916-8001", "서울 성북구 장월로1마길 5"));
        poiList.add(new POI("메가MGC커피 월곡역점", "02-943-8640", "서울 성북구 화랑로13길 7"));
        poiList.add(new POI("빽다방 월곡역점", "02-6369-2666", "서울 성북구 화랑로 91"));

        return poiList;
    }

}
