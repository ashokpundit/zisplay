package vun.zisplaymerchant.models;

import android.net.Uri;
import android.widget.Toast;

import java.io.File;


import retrofit.*;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;
import vun.zisplaymerchant.network.OMGServices;
import vun.zisplaymerchant.network.RestClient;
import vun.zisplaymerchant.utils.AppConstants;

/**
 * Created by ashok on 1/29/15.
 */
public class ZisplayImage {
    final OMGServices omgService= RestClient.getInstance().getApiService();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String id="";

    public String getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(String isPrimary) {
        this.isPrimary = isPrimary;
    }

    String isPrimary="false";
    String title="";
    String type="";
    String parentId="";
    String url="";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    String path="";

    public Uri getLocalImageUri() {
        return localImageUri;
    }

    public void setLocalImageUri(Uri localImageUri) {
        this.localImageUri = localImageUri;
    }

    Uri localImageUri=null;

    public int getLocalTag() {
        return localTag;
    }

    public void setLocalTag(int localTag) {
        this.localTag = localTag;
    }

    int localTag;

    public void uploadImage( )
    {
        System.out.println("in upload image " + localImageUri.toString());
        final TypedFile imageFile=new TypedFile("image/jpg",new File(localImageUri.toString()));

        omgService.uploadImage(AppConstants.userAccessToken,new TypedString(isPrimary),new TypedString(type), new TypedString(parentId),imageFile, new Callback<ZisplayImage>() {
            @Override
            public void success(ZisplayImage zisplayImage, Response response) {

                System.out.println("image created " );
                setId(zisplayImage.getId());
                if(getType().equalsIgnoreCase("merchant")) {
                    AppConstants.merchantObj.setImageId(zisplayImage.getId());
                }

            }
            @Override
            public void failure(retrofit.RetrofitError error) {

                System.out.println("error " + error.getMessage());


            }
        });
    }

}
