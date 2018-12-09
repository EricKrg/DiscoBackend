package disco.api.externalapi.orsPoi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class OrsTestRunner {

    public static void main(String args[]){

        Logger logger = LogManager.getRootLogger();

        OrsPoiApiService orsAPIService = new OrsPoiApiService(
                51.34391,
                12.376,
                5000
        );


        try {
            List<OrsPoiGETResponse> orsGETResponseList = orsAPIService.getAllPois();
            logger.info("SUCCESSFUL");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
