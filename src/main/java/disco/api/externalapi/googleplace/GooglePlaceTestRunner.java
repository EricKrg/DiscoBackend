package disco.api.externalapi.googleplace;

import java.util.*;

public class GooglePlaceTestRunner {

    public static void main(String[] args) {

        GooglePlaceAPIService googlePlaceAPIService = new GooglePlaceAPIService(
                //50.9787,
                48.858093,
                //11.03283,
                2.294694,
                5000
        );

        List<GooglePlaceGETResponse> googlePlaceGETResponses = googlePlaceAPIService.getAllPointOfInterestsLocation();
        return;

    }

}
