package vun.zisplaymerchant.network;

import com.google.gson.JsonObject;

import org.json.JSONObject;

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
import vun.zisplaymerchant.models.AppUser;
import vun.zisplaymerchant.models.Category;
import vun.zisplaymerchant.models.ChatItem;
import vun.zisplaymerchant.models.Follower;
import vun.zisplaymerchant.models.Merchant;
import vun.zisplaymerchant.models.Message;
import vun.zisplaymerchant.models.MobileVerification;
import vun.zisplaymerchant.models.PayUPayment;
import vun.zisplaymerchant.models.PaymentLink;
import vun.zisplaymerchant.models.Product;
import vun.zisplaymerchant.models.ProductLikes;
import vun.zisplaymerchant.models.ZisplayImage;

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
    @POST("/Merchants/register2")
    void registerMerchant2(@Field("mobileno") String mobileNo,
                           @Field("code") String verificationCode,
                          @Field("handle") String handle,
                          @Field("title") String title,
                          @Field("address") String address,
                          @Field("lat") String lat,
                          @Field("lng") String lng,
                          Callback<AppUser> callback);

    @FormUrlEncoded
    @PUT("/Merchants/{merchantId}")
    void updateMerchant(
                @Query("access_token") String access_token,
                @Path("merchantId") String merchantId,
//                        @Body Merchant merchant,
                        @FieldMap Map<String, String> params,
                          Callback<Merchant> callback);


    @FormUrlEncoded
    @PUT("/AppUsers/{userId}")
    void updateAppUser(
            @Query("access_token") String access_token,
            @Path("userId") String userId,
//                        @Body Merchant merchant,
            @FieldMap Map<String, String> params,
            Callback<AppUser> callback);

    @GET("/Merchants/{merchantId}")
    void getMerchant(
            @Query("access_token") String accessToken,
            @Path("merchantId") String merchantId,
                        Callback<Merchant> callback);

    @FormUrlEncoded
    @PUT("/Products")
    void createProduct(@FieldMap Map<String, String> params,
                        Callback<Product> callback);

    @GET("/Products")
    void getProducts(@QueryMap Map<String, String> params,Callback<ArrayList<Product>> callback);

    @GET("/Products/count")
    void getProductsCount(@QueryMap Map<String, String> params,Callback<JSONObject> callback);

    @GET("/messages/count")
    void getMessageCount(@QueryMap Map<String, String> params,Callback<JSONObject> callback);


    @GET("/Products/{productId}")
    void getProduct(@Path("productId") String productId,
                    Callback<Product> callback);

    @GET("/AppUsers/{userId}")
    void getUser(@Path("userId") String userId,
                    Callback<AppUser> callback);

    @GET("/AppUsers/publicProfile/")
    void getUserPublicProfile(@Query("userId") String userId,
                 Callback<AppUser> callback);


    @GET("/Merchants/getFollowers")
    void getFollowers(@Query("merchantId") String merchantId,
                 Callback<ArrayList<AppUser>> callback);

    @GET("/chats")
    void getChats(@QueryMap Map<String, String> params,Callback<ArrayList<ChatItem>> callback);

    @GET("/PayuResponses/getMerchnatPayments")
    void getPayments(@QueryMap Map<String, String> params,Callback<ArrayList<PayUPayment>> callback);

    @GET("/messages")
    void getMessages(@QueryMap Map<String, String> params,Callback<ArrayList<Message>> callback);

    @Multipart
    @POST("/images")
    void uploadImage(@Query("access_token")String access_token, @Part("isPrimary") TypedString isPrimary,@Part("type") TypedString type,@Part("parentId") TypedString parentId, @Part("image") TypedFile image,
                       Callback<ZisplayImage> callback);

    @Multipart
    @POST("/messages")
    void sendMessage(
            @Query("access_token") String access_token,
            @Part("to") TypedString to,
             @Part("from") TypedString from,
                 @Part("toName") TypedString toName,
                          @Part("fromName") TypedString fromName,
                          @Part("text") TypedString text,
                          @Part("image")TypedFile image,
                          Callback<Message> callback);

    @FormUrlEncoded
    @POST("/AppUsers/loginfbuser")
    void loginFBUser(@Field("fbId") String fbId,
                     @Field("accessToken") String accessToken,
                     Callback<AppUser> callback);

    @GET("/ProductLikes/getMerchantLikes")
    void getLovedProducts(@QueryMap Map<String, String> params,Callback<ArrayList<ProductLikes>> callback);

    @FormUrlEncoded
    @POST("/Merchants/addFBPage")
    void connectFBPage(@Field("merchantId") String merchnatId,
                       @Field("facebookId") String facebookId,
                        @Field("userAccessToken") String userAccessToken,
                       @Field("pageId") String pageId,
                     @Field("pageToken") String pageToken,
                     Callback<String> callback);


    @FormUrlEncoded
    @POST("/PaymentLinks/createPaymentLink")
    void createPaymentLink(
                        @Field("access_token") String accessToken,
                        @Field("merchant_id") String merchnatId,
                        @Field("product_id") String productId,
                        @Field("amount") String amount,
                        Callback<PaymentLink> callback);



    @GET("/images/setImagePrimary")
    void setPrimaryImage(
            @Query("access_token") String accessToken,
            @Query("image_id") String imageId,
            Callback<JsonObject> callback);

    @GET("/Products/updateStatus")
    void updatePrductStatus(
            @Query("access_token") String accessToken,
            @Query("product_id") String imageId,
            @Query("status") String status,
            Callback<String> callback);

    @GET("/messages/markReadForUser")
    void markMessageRead(
            @Query("access_token") String accessToken,
            @Query("from_user_id") String OtherUserId,
            Callback<String> callback);

    @GET("/catagories?deleted=false")
    void getCategoryList(
            Callback<ArrayList<Category>> callback);

    @GET("/Merchants/checkHandleAvailability")
    void checkHandleAvailability(
            @Query("handle") String handle,
            Callback<JsonObject> callback);

    @GET("/Merchants/codeVerify")
    void verifyCode(
            @Query("mobileNo") String mobileNo,
            @Query("code") String code,
            Callback<JsonObject> callback);

    @POST("/Merchants/codeVerify")
    void updateGCMCode(
            @Field("access_token") String access_token,
            @Field("code") String code,
            Callback<JsonObject> callback);


}
