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
import vun.zisplay.models.Message;
import vun.zisplay.models.Product;
import vun.zisplay.models.ZisplayImage;
import vun.zisplay.network.OMGServices;
import vun.zisplay.network.RestClient;
import vun.zisplay.utils.AppConstants;
import vun.zisplay.utils.ImageDownloader;

public class NewProductActivity extends ZisplayBaseActivity {
   final LocalData localData=LocalData.getInstance();
    final OMGServices omgService=RestClient.getInstance().getApiService();
    String productId=null;

    Product product=null;
    ArrayList imagePaths=new ArrayList<String[]>();
    int imageCount=0;
    ProgressDialog pgdlg;

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


        String action = intent.getAction();
        Uri data = intent.getData();

        if(productId!=null) {
        loadProduct(productId);
        }
    }

    private void loadProduct(String productId)
    {
        pgdlg = ProgressDialog.show(this, "Please Wait", "Loading Products...", true);
        omgService.getProduct(productId, new Callback<Product>() {
            @Override
            public void success(Product productResponse, Response response) {
                if (productResponse != null) {
                    product=productResponse;
                    loadProduct(productResponse);
                }
                if(pgdlg.isShowing())
                    pgdlg.dismiss();
            }
            @Override
            public void failure(RetrofitError error) {
                if(pgdlg.isShowing())
                    pgdlg.dismiss();
                Toast.makeText(NewProductActivity.this,error.getMessage(),Toast.LENGTH_LONG);
                System.out.println("error " + error.getMessage());
            }
        });
//        loadProduct(productId);
    }
    private void loadProduct(Product product)
    {
        final TextView productTitleText = (TextView) findViewById(R.id.productTitle);
        final TextView productDescriptionText = (TextView) findViewById(R.id.productDescription);
        final TextView productPriceText = (TextView) findViewById(R.id.productPrice);
        final TextView productCollectionText = (TextView) findViewById(R.id.productCollection);
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
    public void startChat(View v)
    {



        final String userId=localData.getData("userId");
        final String username=localData.getData("name");
        final String message="Hi user "+username+" is interested in product  "+product.getTitle();
        omgService.sendMessageWithProduct(new TypedString(product.getMerchantId()), new TypedString(userId), new TypedString(product.getMerchantId()), new TypedString(username), new TypedString(message), new TypedString(productId), new Callback<Message>() {
            @Override
            public void success(Message message, Response response) {

                System.out.println("image created ");
                String chatId=message.getChatId();

                String[] users={userId,product.getMerchantId()};
                String[] userNames={username,product.getMerchantId()};
                Intent chatViewIntent=new Intent(NewProductActivity.this, ChatHistory.class);
                chatViewIntent.putExtra("chatId",chatId);
                chatViewIntent.putExtra("users",users);
                chatViewIntent.putExtra("userNames",userNames);
                startActivity(chatViewIntent);

            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("error " + error.getMessage());
                Toast.makeText(NewProductActivity.this,error.getMessage(),Toast.LENGTH_LONG);
            }
        });
    }



}

