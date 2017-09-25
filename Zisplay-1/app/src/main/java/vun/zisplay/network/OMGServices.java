package vun.zisplay.network;

import java.util.ArrayList;
import java.util.Map;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;
import vun.zisplay.models.AppUser;
import vun.zisplay.models.ChatItem;
import vun.zisplay.models.Merchant;
import vun.zisplay.models.Message;
import vun.zisplay.models.MobileVerification;
import vun.zisplay.models.Product;
import vun.zisplay.models.ZisplayImage;

/**
 * Created by ashok on 2/5/15.
 */
public interface OMGServices {
    @GET("/MobileVerifications/findone")
    void getMobileVerificationCode(@Query("filter[mobileNo]") String mobileNo, @Query("filter[validationCode]") String validationCode,
                    Callback<MobileVerification> callback);

    @FormUrlEncoded
    @POST("/MobileVerifications")
    void postMobileVerificationCode(@Field("mobileNo") String mobileNo,
                    Callback<MobileVerification> callback);

    @FormUrlEncoded
    @POST("/AppUsers")
    void postUser(@Field("userName") String userName,
                  @Field("mobileNo") String mobileNo,
                  @Field("isMerchant") String isMerchant,
                  @Field("password") String password,
                  Callback<AppUser> callback);

    @FormUrlEncoded
    @POST("/Merchants")
    void postMerchant(@Field("mobileNo") String mobileNo,
                  @Field("password") String password,
                  @Field("title") String storeTitle,
                  Callback<Merchant> callback);

    @FormUrlEncoded
    @POST("/Merchants/register")
    void registerMerchant(@Field("mobileno") String mobileNo,
                      @Field("code") String password,
                      @Field("title") String storeTitle,
                      Callback<AppUser> callback);

    @FormUrlEncoded
    @PUT("/Merchants/{merchantId}")
    void updateMerchant(@Path("merchantId") String merchantId,
//                        @Body Merchant merchant,
                        @FieldMap Map<String, String> params,
                          Callback<Merchant> callback);


    @GET("/Merchants/{merchantId}")
    void getMerchant(@Path("merchantId") String merchantId,
                        Callback<Merchant> callback);

    @FormUrlEncoded
    @PUT("/Products")
    void createProduct(@FieldMap Map<String, String> params,
                        Callback<Product> callback);

    @GET("/Products")
    void getProducts(@QueryMap Map<String, String> params,Callback<ArrayList<Product>> callback);

    @GET("/Products/{productId}")
    void getProduct(@Path("productId") String productId,
                    Callback<Product> callback);

    @GET("/AppUsers/{userId}")
    void getUser(@Path("userId") String userId,
                    Callback<AppUser> callback);

    @GET("/chats")
    void getChats(@QueryMap Map<String, String> params,Callback<ArrayList<ChatItem>> callback);

    @GET("/messages")
    void getMessages(@QueryMap Map<String, String> params,Callback<ArrayList<Message>> callback);

    @Multipart
    @POST("/images")
    void uploadImage(@Part("isPrimary") TypedString isPrimary,@Part("type") TypedString type,@Part("parentId") TypedString parentId, @Part("image") TypedFile image,
                       Callback<ZisplayImage> callback);

    @Multipart
    @POST("/messages")
    void sendMessage(@Part("to") TypedString to,
    @Part("from") TypedString from,
    @Part("toName") TypedString toName,
                          @Part("fromName") TypedString fromName,
                          @Part("text") TypedString text,
                          @Part("image")TypedFile image,
                          Callback<Message> callback);
}
