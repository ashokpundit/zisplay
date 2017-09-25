package vun.zisplaymerchant.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import vun.zisplaymerchant.Zisplay;
import vun.zisplaymerchant.utils.AppConstants;

/**
 * Created by ashok on 2/25/15.
 */
public class RestClient {
    private static final String BASE_URL = AppConstants.API_BASE_URL;
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

        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        File cacheDirectory = new File(Zisplay.zisplay.getCacheDir().getAbsolutePath(), "HttpCache");
        Cache cache =null;

        try {
            cache = new Cache(cacheDirectory, cacheSize);
        }catch(Exception e){}
        OkHttpClient client = new OkHttpClient();
        client.setCache(cache);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(client))
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
