package disco.api.externalapi.orsPoi;

import org.json.JSONObject;

import java.util.Map;

public class OrsPoiGETResponse {

    private double lat;
    private double lng;
    private Map<String, Object> properties;

    public OrsPoiGETResponse(double lat, double lng, Map<String, Object> properties) {
        this.lat = lat;
        this.lng = lng;
        this.properties = properties;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public Map<String, Object> getProp() { return  this.properties; }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setProp(Map<String, Object> prop) {this.properties = prop;}


}
