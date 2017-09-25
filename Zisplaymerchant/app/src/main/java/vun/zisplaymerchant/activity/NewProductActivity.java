package vun.zisplaymerchant.activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.aviary.android.feather.sdk.AviaryIntent;
import com.aviary.android.feather.sdk.internal.Constants;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import it.sephiroth.android.library.picasso.Picasso;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;
import vun.zisplaymerchant.R;
import vun.zisplaymerchant.fragments.ShareDialogFragment;
import vun.zisplaymerchant.managers.AnalyticsManager;
import vun.zisplaymerchant.managers.CategoryManager;
import vun.zisplaymerchant.managers.LocalData;
import vun.zisplaymerchant.models.Category;
import vun.zisplaymerchant.models.Product;
import vun.zisplaymerchant.models.ZisplayImage;
import vun.zisplaymerchant.network.OMGServices;
import vun.zisplaymerchant.network.RestClient;
import vun.zisplaymerchant.utils.AppConstants;
import vun.zisplaymerchant.utils.ImageDownloader;

public class NewProductActivity extends ZisplayBaseActivity {
    final OMGServices omgService=RestClient.getInstance().getApiService();
    LocalData localData=LocalData.getInstance();
    private UiLifecycleHelper uiHelper;
    String productId=null;
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    final static int AVIARY_CROP=3;
    ArrayList imageList=new ArrayList<ZisplayImage>();
    int imageCount=0;
    Uri imageUri = null;
    Uri imageUri2=null;
    public  static ImageView showImg  = null;

    LinearLayout imageRowLayout;

    ArrayList categoryArray=new ArrayList();
    Product product;
    int tagNumber=0;
    HashMap<String,String> categories= new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        analytics(this.getClass().getName());
        categoryArray= CategoryManager.getInstance().getCategories();

        Intent aviarIntent = AviaryIntent.createCdsInitIntent(getBaseContext());
        startService(aviarIntent);

        imageRowLayout = (LinearLayout) findViewById(R.id._imagesrow);
        Spinner categorySpiner = (Spinner) findViewById(R.id.category_spinner);

        ArrayAdapter dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, categoryArray);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpiner.setAdapter(dataAdapter);

        showImg = (ImageView) findViewById(R.id.showImg);
        Intent intent =getIntent();
        productId = intent.getStringExtra("productId");
        if(productId!=null) {
        loadProduct(productId);
        }
        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);
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

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }
    private void loadProduct(Product product)
    {
        final EditText productTitleText = (EditText) findViewById(R.id.productTitleEdit);
        final EditText productDescriptionText = (EditText) findViewById(R.id.ptoductDescriptionEdit);
        final EditText productPriceText = (EditText) findViewById(R.id.ptoductPriceEdit);
        final Spinner productCollectionSpiner = (Spinner) findViewById(R.id.category_spinner);
        final EditText productTagsText = (EditText) findViewById(R.id.ptoductTagsEdit);
        final CheckBox multiColor = (CheckBox) findViewById(R.id.multiColor);
        final CheckBox multiSize = (CheckBox) findViewById(R.id.multiSize);
        if(product.isMultiColor())
            multiColor.setChecked(true);
        if(product.isMultiSize())
            multiSize.setChecked(true);

        productTitleText.setText(product.getTitle());
        productDescriptionText.setText(product.getDescription());
        productTagsText.setText(product.getTags());
//        productCollectionSpiner..setText(product.getCatagoryId());
        productPriceText.setText(product.getPrice()+"");
        String[] imageIds=product.getImages();
        if(imageIds!=null) {
            for (int i = 0; i < imageIds.length; i++) {
                tagNumber++;
                ZisplayImage image=new ZisplayImage();
                image.setId(imageIds[i]);
                image.setType("product");
                image.setParentId(productId);
                image.setLocalTag(tagNumber);
                View cell = getLayoutInflater().inflate(R.layout.imagethumb, null);
                final ImageView imageView = (ImageView) cell.findViewById(R.id._image);
                imageView.setTag(tagNumber);
                System.out.println(AppConstants.CLOUDINARY_URL + "w_100,h_100/" + imageIds[i] + ".jpg");
                String imagePath= AppConstants.CLOUDINARY_URL+"w_100,h_100/"+imageIds[i]+".jpg";
                Picasso.with(NewProductActivity.this).load(imagePath).into(imageView);
//                ImageDownloader imgDownloader = new ImageDownloader();
//                imgDownloader.download(imagePath, imageView);

                imageRowLayout.addView(cell);
                imageList.add(image);


            }
        }

    }
    public void startCamera(View v)
    {


        HashMap event=new HashMap<String,String>();
        event.put("screenName","Product");
        event.put("label","Product Image");
        event.put("action","Camera");
        event.put("Category","Product Update");
        AnalyticsManager.getInstance().sendEvent(event);

        if(imageList.size()>=5)
        {
            Toast.makeText(NewProductActivity.this, "This product already have 5 images.", Toast.LENGTH_SHORT).show();
            return;
        }

        Random r = new Random();
        int i1 = (r.nextInt(10000));
        String fileName = i1+"-"+"product-image.jpg";
        String fileName2 = i1+"-"+"product-image-2.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION,"Product image");
        ContentValues values2 = new ContentValues();
        values2.put(MediaStore.Images.Media.TITLE, fileName2);
        values2.put(MediaStore.Images.Media.DESCRIPTION,"Product image");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        imageUri2 = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values2);
        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        if (intent.resolveActivity(getPackageManager()) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public void submitProduct(View v) {
        HashMap event=new HashMap<String,String>();
        event.put("screenName","Product");
        event.put("label","Product");
        event.put("action","click");
        event.put("Category","Create Product");

        HashMap productMap=new HashMap<String,String>();
        if(productId!=null && !productId.isEmpty()) {
            productMap.put("id", productId);
            event.put("Category","Update Product");
            event.put("Product Id",productId);
        }
        AnalyticsManager.getInstance().sendEvent(event);

        final EditText productTitleText = (EditText) findViewById(R.id.productTitleEdit);
        final EditText productDescriptionText = (EditText) findViewById(R.id.ptoductDescriptionEdit);
        final EditText productPriceText = (EditText) findViewById(R.id.ptoductPriceEdit);
        final Spinner productCategorySpineer = (Spinner) findViewById(R.id.category_spinner);
        final EditText productTagsText = (EditText) findViewById(R.id.ptoductTagsEdit);
        final CheckBox multiColor = (CheckBox) findViewById(R.id.multiColor);
        final CheckBox multiSize = (CheckBox) findViewById(R.id.multiSize);
        boolean multiSizeStatus=multiSize.isChecked();
        boolean multiColorStatus=multiColor.isChecked();
        productMap.put("access_token", AppConstants.userAccessToken);
        productMap.put("title", productTitleText.getText().toString());
        productMap.put("description",productDescriptionText.getText().toString());
        productMap.put("price",productPriceText.getText().toString());
        Category category=(Category)categoryArray.get(productCategorySpineer.getSelectedItemPosition());
        productMap.put("categoryId",category.getId());
        productMap.put("category",category.getTitle());
        productMap.put("merchantId",AppConstants.merchantId);
        productMap.put("tags",productTagsText.getText());
        productMap.put("isMultiSize",productTagsText.getText());
        productMap.put("isMultiColor",productTagsText.getText());
        omgService.createProduct(productMap, new Callback<Product>() {
            @Override
            public void success(Product productResponse, Response response) {
                if (productResponse != null) {
                    product=productResponse;
                    productId=productResponse.getId();
                    uploadImages();
                    Intent productViewIntent = new Intent(NewProductActivity.this, ProductView.class);
                    productViewIntent.putExtra("productId", productResponse.getId());
                    startActivity(productViewIntent);
//                    Intent share = new Intent(Intent.ACTION_SEND);
//                    share.setType("*/*");
//                    share.putExtra(Intent.EXTRA_STREAM,imageUri);
//                    share.putExtra(Intent.EXTRA_SUBJECT,productResponse.getTitle());
//                      share.putExtra(Intent.EXTRA_TITLE,productResponse.getTitle());
//                    share.putExtra(Intent.EXTRA_TEXT,productResponse.getDescription());
//                    startActivity(Intent.createChooser(share, "Share Product"));
//                    publishFeedDialog();
//                    ShareDialogFragment shareDialog=new ShareDialogFragment();
//                    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
//                    shareDialog.show(fm,"");
//                    Toast.makeText(NewProductActivity.this, "Product uploaded successfully.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(NewProductActivity.this, "Error in saving product.", Toast.LENGTH_SHORT).show();
                System.out.println("error " + error.getMessage());
            }
        });

    }


    private void uploadImages()
    {
        if(productId==null || productId.isEmpty())
            return;
        for(int i=0;i<imageList.size();i++)
        {
            ZisplayImage image=(ZisplayImage)imageList.get(i);
            if(image.getId().isEmpty())
            {
                if(image.getParentId()==null || image.getParentId().isEmpty())
                    image.setParentId(productId);

                if(image.getType()==null || image.getType().isEmpty())
                    image.setType("product");

                image.uploadImage();

            }
        }
    }


    private void performCrop(Uri picUri) {
        Intent newIntent = new AviaryIntent.Builder(this)
                .setData(picUri) // input image src
                .build();
        startActivityForResult(newIntent, AVIARY_CROP);
        return;
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if ( resultCode == RESULT_OK) {
                performCrop(imageUri);
            } else if ( resultCode == RESULT_CANCELED) {
                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == AVIARY_CROP) {
            if (resultCode == RESULT_OK) {
                Uri mImageUri = data.getData(); // generated output file
                Bundle extra = data.getExtras();
                if (null != extra) {
                    // image has been changed?
                    boolean changed = extra.getBoolean(Constants.EXTRA_OUT_BITMAP_CHANGED);
                }
                imageUri2=mImageUri;
                new LoadImagesFromSDCard().execute(mImageUri.toString());
            }
        }

    }

    class LoadImagesFromSDCard  extends AsyncTask<String, Void, Void> {
        private ProgressDialog Dialog = new ProgressDialog(NewProductActivity.this);
        Bitmap mBitmap;
        Uri uri = null;
        protected void onPreExecute() {
            Dialog.setMessage(" Loading image from Sdcard..");
            Dialog.show();
        }
        protected Void doInBackground(String... urls) {
            Bitmap bitmap = null;
            Bitmap newBitmap = null;
            try {
                File imageFile=new File(urls[0]);
                uri=Uri.parse(urls[0]);
                bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                if (bitmap != null) {
                    newBitmap = Bitmap.createScaledBitmap(bitmap, 170, 170, true);
                    bitmap.recycle();
                    if (newBitmap != null) {
                        mBitmap = newBitmap;
                    }
                }
            } catch (Exception e) {
                AnalyticsManager.getInstance().raiseException("Product",e);
                Dialog.dismiss();
                cancel(true);
            }
            return null;
        }
        protected void onPostExecute(Void unused) {
            Dialog.dismiss();
            if(mBitmap != null)
            {
                View cell = getLayoutInflater().inflate(R.layout.imagethumb, null);
                final ImageView imageView = (ImageView) cell.findViewById(R.id._image);
                tagNumber++;
                imageView.setImageBitmap(mBitmap);
                imageView.setTag(tagNumber);
                ZisplayImage image=new ZisplayImage();
                image.setType("product");
                image.setParentId(productId);
                image.setLocalImageUri(uri);
                image.setLocalTag(tagNumber);
                imageList.add(image);
                imageRowLayout.addView(cell);
                imageView.setOnClickListener(new OnImageClickListener(tagNumber));
                imageRowLayout.setVisibility(View.VISIBLE);
                HorizontalScrollView block=(HorizontalScrollView)findViewById(R.id.scrollview);
                block.setVisibility(View.VISIBLE);
                LinearLayout block2=(LinearLayout)findViewById(R.id.uploadBlock);
                block2.setVisibility(View.INVISIBLE);
            }
        }
    }
    class OnImageClickListener implements View.OnClickListener {

        int postion;
        int tag;

        String[] imageIds=new String[imageList.size()];

        public OnImageClickListener(int tag) {
            this.tag = tag;

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(NewProductActivity.this, FullScreenViewActivity.class);
            int  imageCount = imageList.size();

            for(int i=0; i< imageCount; i++ ){
                ZisplayImage image=(ZisplayImage)imageList.get(i);
                imageIds[i]=image.getId();
                if(this.tag==image.getLocalTag())
                {
                    postion=i;
                }

            }
            intent.putExtra("position", postion);
            intent.putExtra("images",imageIds);
            startActivity(intent);
        }

    }


    private void publishFeedDialog() {

        if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            // Publish the post using the Share Dialog
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                    .setLink("http://zisplay.com/p/" + productId)
                    .setCaption( product.getTitle())
                    .setDescription(product.getDescription())
                    .setPicture(AppConstants.CLOUDINARY_URL+"w_400,h_400/"+product.getImageId()+".jpg")
                    .build();
            uiHelper.trackPendingDialogCall(shareDialog.present());

        } else {
            // Fallback. For example, publish the post using the Feed Dialog

            Bundle params = new Bundle();
            params.putString("name", "Zisplay");
            params.putString("caption", product.getTitle());
            params.putString("description", product.getDescription());
            params.putString("link", "http://zisplay.com/p/" + productId);
            params.putString("picture", AppConstants.CLOUDINARY_URL+"w_400,h_400/"+product.getImageId()+".jpg");
            WebDialog feedDialog = (
                    new WebDialog.FeedDialogBuilder(NewProductActivity.this,R.string.facebook_app_id+"",
                            params))
                    .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                        @Override
                        public void onComplete(Bundle values,
                                               FacebookException error) {
                            if (error == null) {
                                // When the story is posted, echo the success
                                // and the post Id.
                                final String postId = values.getString("post_id");
                                if (postId != null) {
                                    Toast.makeText(NewProductActivity.this,
                                            "Posted story, id: " + postId,
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // User clicked the Cancel button
                                    Toast.makeText(NewProductActivity.this.getApplicationContext(),
                                            "Publish cancelled",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else if (error instanceof FacebookOperationCanceledException) {
                                // User clicked the "x" button
                                Toast.makeText(NewProductActivity.this.getApplicationContext(),
                                        "Publish cancelled",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // Generic, ex: network error
                                Toast.makeText(NewProductActivity.this.getApplicationContext(),
                                        "Error posting story",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                    })
                    .build();
            feedDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }


}


