package vun.zisplaymerchant.managers;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


import vun.zisplaymerchant.models.Product;
import vun.zisplaymerchant.network.OMGServices;
import vun.zisplaymerchant.network.RestClient;

/**
 * Created by ashok on 3/26/15.
 */
public class ProductListManager {




    ArrayList<Product> publishedProductList;
    ArrayList<Product> unpublishedProductList;
    LocalData localData=LocalData.getInstance();
    final OMGServices omgService=RestClient.getInstance().getApiService();
    private static ProductListManager ourInstance = new ProductListManager();
    boolean requestInProcess=false;

    public ArrayList<Product> getProductList(int type) {

        if(type==1)
        {
            if (unpublishedProductList == null) {
                String categoryDaya = localData.getData(unpublishedstorageKey);
                Gson gson = new Gson();
                Type collectionType = new TypeToken<ArrayList<Product>>() {
                }.getType();
                unpublishedProductList = gson.fromJson(categoryDaya, collectionType);
                if (unpublishedProductList == null || unpublishedProductList.size() == 0) {
                    //                updateProducts();
                    unpublishedProductList = new ArrayList<Product>();
                }

            }

            return unpublishedProductList;
        }
            if (publishedProductList == null) {
                String categoryDaya = localData.getData(publishedstorageKey);
                Gson gson = new Gson();
                Type collectionType = new TypeToken<ArrayList<Product>>() {
                }.getType();
                publishedProductList = gson.fromJson(categoryDaya, collectionType);
                if (publishedProductList == null || publishedProductList.size() == 0) {
//                updateProducts();
                    publishedProductList = new ArrayList<Product>();
                }

            }

        return publishedProductList;


    }

    public void updateProducts()
    {

        HashMap productMap=new HashMap<String,String>();
//        productMap.put("filter[include]","productImage");
        productMap.put("filter[where][isDeleted]",false);
        productMap.put("filter[where][status]","published");
        productMap.put("filter[limit]","10");
        productMap.put("filter[where][merchantId]",localData.getData("merchantId"));
        if(!requestInProcess) {
            requestInProcess = true;
            omgService.getProducts(productMap, new Callback<ArrayList<Product>>() {
                @Override
                public void success(ArrayList<Product> products, Response response) {
                    requestInProcess = false;
                    if (products != null && products.size() != 0) {
                        publishedProductList = products;
                        Gson gson = new Gson();
                        Type collectionType = new TypeToken<ArrayList<Product>>() {
                        }.getType();
                        if (products != null && products.size() > 0) {
                            setProductList(products,0);
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    requestInProcess = false;
                    System.out.println("error " + error.getMessage());
                }
            });
        }
    }



    public static ProductListManager getInstance() {
        return ourInstance;
    }

    private ProductListManager() {
    }

    public void setProductList(ArrayList<Product> products,int type) {


        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<Product>>(){}.getType();
        String productsData=gson.toJson(products, collectionType);
        if(type==0) {
           publishedProductList =products;
            localData.saveData(publishedstorageKey, productsData);
        }
        else if(type==1)
        {
            unpublishedProductList=products;
            localData.saveData(unpublishedstorageKey, productsData);
        }


    }
    public void addProductList(ArrayList<Product> products,int type) {
        ArrayList<Product> temp=null;
        String tempKey="";
        if(type==0)
        {
            publishedProductList.addAll(products);
            temp=publishedProductList;
            tempKey=publishedstorageKey;
        }
        else if(type==1)
        {
            unpublishedProductList.addAll(products);
            temp=unpublishedProductList;
            tempKey=unpublishedstorageKey;
        }
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<Product>>(){}.getType();
         String productsData = gson.toJson(temp, collectionType);
         localData.saveData(tempKey, productsData);
    }

        final String publishedstorageKey="productListData_0";
        final String unpublishedstorageKey="productListData_1";

}
