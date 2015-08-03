package in.nearfox.nearfox.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by tangbang on 5/25/2015.
 */
public class RestClient {

    public static final String BASE_URL = "http://52.74.84.225/api/v1.2";
    public static final String BASE_URL1 = "http://52.74.225.112/api/v2.1/app";
    public static final String LOCATION_BASE_URL1 = "http://52.74.225.112/api/v2.1/locations";
    //    private static final String BASE_URL = "http://52.74.28.176/tango" ;
//    private static final String BASE_URL = "http://192.168.1.39:5000/api/v1.2" ;
//    private static final String BASE_URL = "http://192.168.0.110" ;
    private ApiServices.ApiService apiService;
    private ApiServices.ApiService apiService1;

    public RestClient() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();

        RestAdapter restAdapter1 = new RestAdapter.Builder()
                .setEndpoint(BASE_URL1)
                .setConverter(new GsonConverter(gson))
                .build();

        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        restAdapter1.setLogLevel(RestAdapter.LogLevel.FULL);

        apiService = restAdapter.create(ApiServices.ApiService.class);
        apiService1 = restAdapter1.create(ApiServices.ApiService.class);
    }

    public RestClient(String url) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(url)
                .setConverter(new GsonConverter(gson))
                .build();
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

        apiService = restAdapter.create(ApiServices.ApiService.class);

    }

    public ApiServices.ApiService getApiService() {
        return apiService;
    }

    public ApiServices.ApiService getApiService1() {
        return apiService1;
    }
}
