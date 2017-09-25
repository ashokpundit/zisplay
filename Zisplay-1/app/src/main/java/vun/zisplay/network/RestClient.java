package vun.zisplay.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import vun.zisplay.utils.AppConstants;

/**
 * Created by ashok on 2/25/15.
 */
public class RestClient {
    private static final String BASE_URL = AppConstants.SERVER_BASE_URL;
    private OMGServices apiService;

    private static RestClient restClient=null;
    public static RestClient getInstance()
    {
        if(restClient==null)
        {
            restClient=new RestClient();
        }
        return restClient;
    }
    RestClient()
    {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();

        apiService = restAdapter.create(OMGServices.class);
    }

    public OMGServices getApiService()
    {
        return apiService;
    }
}
