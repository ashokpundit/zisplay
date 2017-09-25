package vun.zisplaymerchant.managers;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import vun.zisplaymerchant.models.Category;
import vun.zisplaymerchant.models.Merchant;
import vun.zisplaymerchant.models.Product;
import vun.zisplaymerchant.network.OMGServices;
import vun.zisplaymerchant.network.RestClient;
import vun.zisplaymerchant.utils.AppConstants;

/**
 * Created by ashok on 3/26/15.
 */
public class CategoryManager {

    public ArrayList<Category> getCategories() {
        if(categories==null)
        {
                String categoryDaya=localData.getData(storageKey);
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<Category>>(){}.getType();
            categories = gson.fromJson(categoryDaya, collectionType);
            if(categories==null || categories.size()==0)
            {
                updateCategories();
                categories=new ArrayList<Category>();
            }

        }
        return categories;
    }

    public void updateCategories()
    {

        omgService.getCategoryList(new Callback<ArrayList<Category>>() {
            @Override
            public void success(ArrayList<Category> categoryList, Response response) {
                if (categoryList != null && categoryList.size()!=0) {
                    setCategories(categoryList);
                }
            }
            @Override
            public void failure(RetrofitError error) {
                System.out.println("error " + error.getMessage());
            }
        });
    }
    public void setCategories(ArrayList<Category> categoryList) {
        categories=categoryList;
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<Category>>(){}.getType();
        String categoryData=gson.toJson(categoryList, collectionType);
        localData.saveData(storageKey,categoryData);


    }

    ArrayList<Category> categories;
    LocalData localData=LocalData.getInstance();
    final OMGServices omgService=RestClient.getInstance().getApiService();
    private static CategoryManager ourInstance = new CategoryManager();

    public static CategoryManager getInstance() {
        return ourInstance;
    }

    private CategoryManager() {
    }

    final String storageKey="categoryData";
}
