package vun.zisplay.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import vun.zisplay.managers.LocalData;
import vun.zisplay.utils.AppConstants;

/**
 * Created by ashok on 2/25/15.
 */
public class RestClient {
    private static final String BASE_URL = AppConstants.API_BASE_URL;
    private OMGServices apiService;

    LocalData localData=LocalData.getInstance();
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
//                .setRequestInterceptor(new RequestInterceptor() {
//                    @Override
//                    public void intercept(RequestInterceptor.RequestFacade request) {
//                        request.addHeader("Accept", "application/json;versions=1");
//                        String accessToken=localData.getData("accessToken");
//                        if (accessToken!=null && !accessToken.isEmpty()) {
//                            request.addHeader("Authorization", accessToken);
//                        }
//                    }
//                })
                .build();

        apiService = restAdapter.create(OMGServices.class);
    }

    public OMGServices getApiService()
    {
        return apiService;
    }
}
