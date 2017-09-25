package vun.zisplaymerchant.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

import com.aviary.android.feather.sdk.AviaryIntent;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import it.sephiroth.android.library.picasso.Picasso;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import vun.zisplaymerchant.R;
import vun.zisplaymerchant.models.Product;
import vun.zisplaymerchant.network.OMGServices;
import vun.zisplaymerchant.network.RestClient;
import vun.zisplaymerchant.utils.AppConstants;

public class FullScreenViewActivity extends ZisplayBaseActivity {
    Toolbar toolbar;
    int[]  imageIds = new int[3];
    String[] imageArr ;
    ProgressDialog pgdlg;
    ImageView productImage;
    int currentImage=0;
    GestureDetector gestureDetector;
    OMGServices omgService= RestClient.getInstance().getApiService();
    TextView titleText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_view);
        analytics(this.getClass().getName());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent =getIntent();
        imageArr =intent.getStringArrayExtra("images");
        currentImage = intent.getIntExtra("position",0);
        toolbar.setTitle((currentImage+1)+" of "+imageArr.length);
//        imageIds[0]=R.drawable.testbanner;
//        imageIds[1]=R.drawable.testbanner2;
//        imageIds[2]=R.drawable.testbanner2;
        ZisplayGestureListener zisplayGestureListener=new ZisplayGestureListener();
        gestureDetector=new GestureDetector(zisplayGestureListener);

        titleText=(TextView)findViewById(R.id.toolTitle);
        titleText.setText((currentImage+1)+" of "+imageArr.length);

        productImage = (ImageView) findViewById(R.id.ProductImageSlide);
       // productImage.setImageResource(imageIds[0]);

        String imagePath= AppConstants.CLOUDINARY_URL+""+imageArr[currentImage]+".jpg";
        Picasso.with(FullScreenViewActivity.this).load(imagePath).into(productImage);


    }
    public void setAsPrimary(View V)
    {
        pgdlg = ProgressDialog.show(this, "Please Wait", "Saving...", true);
        omgService.setPrimaryImage(AppConstants.userAccessToken,imageArr[currentImage],new Callback<JsonObject>() {
            @Override
            public void success(JsonObject responseObj, Response response) {
                if (pgdlg.isShowing())
                    pgdlg.dismiss();
                if (responseObj != null) {
                    Toast.makeText(FullScreenViewActivity.this, responseObj.get("message").toString(), Toast.LENGTH_LONG);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (pgdlg.isShowing())
                    pgdlg.dismiss();
                Toast.makeText(FullScreenViewActivity.this, error.getMessage(), Toast.LENGTH_LONG);
                System.out.println("error " + error.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_full_screen_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return gestureDetector.onTouchEvent(event);
    }




        class ZisplayGestureListener extends SimpleOnGestureListener {

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {

                float sensitvity = 50;

                // TODO Auto-generated method stub
                if ((e1.getX() - e2.getX()) > sensitvity) {
                    currentImage++;
                    if (currentImage >= imageArr.length) {
                        currentImage = 0;
                    }

                    //swipe += "Swipe Left\n";
                } else if ((e2.getX() - e1.getX()) > sensitvity) {
                    currentImage--;
                    if (currentImage < 0) {
                        currentImage = imageArr.length - 1;

                    }

                    //swipe += "Swipe Right\n";
                } else {
                    //swipe += "\n";
                }
                titleText.setText((currentImage+1)+" of "+imageArr.length);
                // productImage.setImageResource(currentImage);
                String imagePath = AppConstants.CLOUDINARY_URL + "" + imageArr[currentImage] + ".jpg";
                System.out.println(imagePath);
                Picasso.with(FullScreenViewActivity.this).load(imagePath).into(productImage);


                return super.onFling(e1, e2, velocityX, velocityY);
            }
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {

                super.onLongPress(e);
            }
            @Override
            public boolean onDoubleTap(MotionEvent e) {
              return super.onDoubleTap(e);
            }

        }
}
