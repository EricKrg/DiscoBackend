package disco.api.externalapi.orsPoi;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class OrsPoiApiService {

    private static Logger logger = LogManager.getRootLogger();

    private double lat;
    private double lng;
    private double rad;

    public OrsPoiApiService(double lat, double lng, double rad){
        this.lat = lat;
        this.lng = lng;
        this.rad = rad;
    }
    public List<OrsPoiGETResponse> getAllPois() {

        logger.info("Ors Poi request: ");
        logger.info("Latitude: " + lat);
        logger.info("Longitude: " + lng);
        logger.info("Radius: " + rad);

        logger.info("lol you set a radius but i am not gonna use it.");


        String orsAPIUrl = this.buildOrsAPIURL();
        logger.info("orsAPIURL: " + orsAPIUrl);

        if(orsAPIUrl == null) {
            logger.warn("orsApi is null");
            return null;
        }

        JSONArray httpResponse = null;
        httpResponse = accessOrsAPIURL(orsAPIUrl);

        if(httpResponse != null) {
            logger.info("recived ORS response!");
            return this.formatJSONarray(httpResponse);
        } else {
            logger.warn("Wrong HTTP Response Code");
            return null;
        }

    }

    private String buildOrsAPIURL() {

        final String basicURL = "https://api.openrouteservice.org/pois?api_key=";
        final String envOrsAccessTokenKey = "ORS_KEY"; // env key
        Map<String, String> env = System.getenv();

        if(env.containsKey(envOrsAccessTokenKey)) {
            String envOrsAccessTokenValue = env.get(envOrsAccessTokenKey);
            logger.info("The Env-Variable " + envOrsAccessTokenKey + " = " + envOrsAccessTokenValue );

            StringBuilder urlBuild = new StringBuilder();

            urlBuild.append(basicURL);
            urlBuild.append(envOrsAccessTokenValue);
            urlBuild.append("&Content-Type=application/json");

            return urlBuild.toString();
        }
        logger.warn("Wrong api auth for ORS");
        return null;
    }
    private String buildPayload(double lat, double lng){
        return  "{\n" +
                "  \"request\": \"pois\",\n" +
                "  \"geometry\": {\n" +
                "    \"geojson\": {\n" +
                "      \"type\": \"Point\",\n" +
                "      \"coordinates\": ["+ lng +","+ lat + "]\n" +
                "    },\n" +
                "    \"buffer\": 2000\n" + // default to 2000
                "  },\n" +
                "  \"limit\": 50\n" + // limit output to 50
                "}";

    }
    private  JSONArray toJSONarray(String responseString){
        JSONObject obj = new JSONObject(responseString);
        JSONArray jsonArray = obj.getJSONArray("features");
        return jsonArray;
    }
    private JSONArray accessOrsAPIURL(String orsApiUrl) {
        try {
            String payload = this.buildPayload(this.lat,this.lng);
            StringEntity entity = new StringEntity(payload,
                    ContentType.APPLICATION_JSON);

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(orsApiUrl);
            request.setEntity(entity);

            org.apache.http.HttpResponse response = httpClient.execute(request);
            logger.info("The HTTP Response Code is " + response.getStatusLine().getStatusCode());

            String resJson = EntityUtils.toString(response.getEntity(), "UTF-8");
            JSONArray responseArray = this.toJSONarray(resJson);

            return responseArray;
        } catch (IOException e) {
            logger.warn("Run into: " + e);
            return null;
        }
    }

    private List<OrsPoiGETResponse> formatJSONarray(JSONArray responseArray) {
        int size = responseArray.length();
        List<OrsPoiGETResponse> orsGETResponseList = new ArrayList<>(size); // limit is 50

        for(int i = 0; i < size ; i++){
            JSONObject t = responseArray.getJSONObject(i);
            JSONObject geom = t.getJSONObject("geometry");
            JSONArray  coords = geom.getJSONArray("coordinates");

            JSONObject prop = t.getJSONObject("properties");
            Set<String> keys = prop.keySet();
            Map<String, Object> props = new HashMap<>();


            for(String key: keys){
                Object temp = prop.get(key);
                props.put(key, temp.toString());
            }
            OrsPoiGETResponse orsPoiResponse = new OrsPoiGETResponse(
                    coords.getDouble(1),
                    coords.getDouble(0),
                    props
                    );
            orsGETResponseList.add(orsPoiResponse);
        }
        return orsGETResponseList;
    }


}






