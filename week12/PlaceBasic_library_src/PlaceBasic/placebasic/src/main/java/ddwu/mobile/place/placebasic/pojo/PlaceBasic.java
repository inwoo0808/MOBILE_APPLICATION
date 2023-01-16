package ddwu.mobile.place.placebasic.pojo;

public class PlaceBasic {
    private Geometry geometry;

    private String place_id;
    private String name;
    private String vicinity;

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getPlaceId() {
        return place_id;
    }

    public void setPlaceId(String place_id) {
        this.place_id = place_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public double getLatitude() {
        return geometry.getLocation().getLat();
    }

    public double getLongitude() {
        return geometry.getLocation().getLng();
    }


    @Override
    public String toString() {
        return "PlaceBasic{" +
                "geometry=" + geometry +
                ", place_id='" + place_id + '\'' +
                ", name='" + name + '\'' +
                ", vicinity='" + vicinity + '\'' +
                '}';
    }
}
