package vun.zisplay.activity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;
import vun.zisplay.R;
import vun.zisplay.managers.AnalyticsManager;
import vun.zisplay.managers.LocalData;
import vun.zisplay.models.Product;
import vun.zisplay.models.ZisplayImage;
import vun.zisplay.network.OMGServices;
import vun.zisplay.network.RestClient;
import vun.zisplay.utils.AppConstants;
import vun.zisplay.utils.ImageDownloader;

public class NewProductActivity extends ZisplayBaseActivity {
    final OMGServices omgService=RestClient.getInstance().getApiService();
    String productId=null;
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    final static int AVIARY_CROP=3;
    final int PIC_CROP = 2;
    ArrayList imagePaths=new ArrayList<String[]>();
    int imageCount=0;
    boolean isImageUploadPending=false;
    static String Path=null;
    Uri imageUri = null;
    Uri imageUri2=null;
    public  static ImageView showImg  = null;
    NewProductActivity CameraActivity = null;
    LinearLayout imageRowLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        analytics(this.getClass().getName());
        CameraActivity = this;
        imageRowLayout = (LinearLayout) findViewById(R.id._imagesrow);
        showImg = (ImageView) findViewById(R.id.showImg);
        Intent intent =getIntent();
        productId = intent.getStringExtra("productId");
        if(productId!=null) {
        loadProduct(productId);
        }
    }

    private void loadProduct(String productId)
    {
        omgService.getProduct(productId, new Callback<Product>() {
            @Override
            public void success(Product productResponse, Response response) {
                if (productResponse != null) {
                    loadProduct(productResponse);
                }
            }
            @Override
            public void failure(RetrofitError error) {
                System.out.println("error " + error.getMessage());
            }
        });
//        loadProduct(productId);
    }
    private void loadProduct(Product product)
    {
        final EditText productTitleText = (EditText) findViewById(R.id.productTitle);
        final EditText productDescriptionText = (EditText) findViewById(R.id.productDescription);
        final EditText productPriceText = (EditText) findViewById(R.id.productPrice);
        final EditText productCollectionText = (EditText) findViewById(R.id.productCollection);
        productTitleText.setText(product.getTitle());
        productDescriptionText.setText(product.getDescription());
        productCollectionText.setText(product.getCatagoryId());
        productPriceText.setText(product.getPrice()+"");
        String[] imageIds=product.getImages();
        if(imageIds!=null) {
            for (int i = 0; i < imageIds.length; i++) {
                ImageDownloader imgDownloader = new ImageDownloader();
                View cell = getLayoutInflater().inflate(R.layout.imagethumb, null);
                final ImageView imageView = (ImageView) cell.findViewById(R.id._image);
                System.out.println(AppConstants.CLOUDINARY_URL + "w_100,h_100/" + imageIds[i] + ".jpg");
                String imagePath= AppConstants.CLOUDINARY_URL+"w_100,h_100/"+imageIds[i]+".jpg";
                imgDownloader.download(imagePath, imageView);
                imagePaths.add(imageIds[i]);
                imageRowLayout.addView(cell);
            }
        }

    }



}

