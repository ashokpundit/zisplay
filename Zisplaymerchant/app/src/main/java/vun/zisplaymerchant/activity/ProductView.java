package vun.zisplaymerchant.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import vun.zisplaymerchant.R;
import vun.zisplaymerchant.managers.LocalData;
import vun.zisplaymerchant.models.Category;
import vun.zisplaymerchant.models.Product;
import vun.zisplaymerchant.models.ZisplayImage;
import vun.zisplaymerchant.network.OMGServices;
import vun.zisplaymerchant.network.RestClient;
import vun.zisplaymerchant.utils.AppConstants;

public class ProductView extends ZisplayBaseActivity {

    private UiLifecycleHelper uiHelper;
    final OMGServices omgService = RestClient.getInstance().getApiService();
    final LocalData localData = LocalData.getInstance();
    String[] imageIds;
    Toolbar toolbar;
    LinearLayout imageRowLayout;
    String productId = null;
    Product product = null;
    ProgressDialog pgdlg;
    AlertDialog alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_view);
        analytics(this.getClass().getName());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Followers");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);
        imageRowLayout = (LinearLayout) findViewById(R.id._imagesrow);
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        if (productId != null) {
            loadProduct(productId);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_product_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goBack(View V)
    {
        super.onBackPressed();
    }
    private void loadProduct(String productId) {
        pgdlg = ProgressDialog.show(this, "Please Wait", "Loading Product...", true);
        omgService.getProduct(productId, new Callback<Product>() {
            @Override
            public void success(Product productResponse, Response response) {
                if (pgdlg.isShowing())
                    pgdlg.dismiss();
                if (productResponse != null) {
                    product = productResponse;
                    loadProduct(productResponse);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (pgdlg.isShowing())
                    pgdlg.dismiss();
                System.out.println("error " + error.getMessage());
            }
        });
//        loadProduct(productId);
    }

    private void loadProduct(Product product) {

        String productStatus = product.getStatus();
        final TextView productTitleText = (TextView) findViewById(R.id.productTitle);
        final TextView productDescriptionText = (TextView) findViewById(R.id.productDescription);
        final TextView productPriceText = (TextView) findViewById(R.id.productPrice);
        final TextView inquireText = (TextView) findViewById(R.id.inquireCount);
        final TextView purchaseCountText = (TextView) findViewById(R.id.purchaseCount);
        final TextView loveCountText = (TextView) findViewById(R.id.loveCount);
        final TextView viewsCountText = (TextView) findViewById(R.id.viewsCount);
        if (productStatus.equalsIgnoreCase("publish")) {

            inquireText.setText(product.getInquiries() + " INQUIRES");
            purchaseCountText.setText(product.getLikes() + " SOLD");
            loveCountText.setText(product.getLikes() + " LIKES");
            viewsCountText.setText(product.getLikes() + " VIEWS");
            Button publishButton = (Button) findViewById(R.id.publishButton);
            publishButton.setVisibility(View.GONE);
            LinearLayout shareOption = (LinearLayout) findViewById(R.id.shareOption);
            shareOption.setVisibility(View.GONE);
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.productActionBlock);
            rl.setVisibility(View.VISIBLE);
            RelativeLayout rl1 = (RelativeLayout) findViewById(R.id.productactionSold);
            rl1.setVisibility(View.VISIBLE);
            Button shareButton = (Button) findViewById(R.id.shareButton);
            shareButton.setVisibility(View.VISIBLE);

        } else {
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.productActionBlock);
            rl.setVisibility(View.GONE);
            RelativeLayout rl1 = (RelativeLayout) findViewById(R.id.productactionSold);
            rl1.setVisibility(View.GONE);
            Button shareButton = (Button) findViewById(R.id.shareButton);
            shareButton.setVisibility(View.GONE);

            LinearLayout shareOption = (LinearLayout) findViewById(R.id.shareOption);
            shareOption.setVisibility(View.VISIBLE);
            Button publishButton = (Button) findViewById(R.id.publishButton);
            publishButton.setVisibility(View.VISIBLE);

        }
        productTitleText.setText(product.getTitle());
        productDescriptionText.setText(product.getDescription());
        productPriceText.setText(product.getPrice() + "");
        imageIds = product.getImages();
        String imageId = product.getImageId();

        if ((imageId == null || imageId.isEmpty()) && (imageIds != null && imageIds.length > 0)) {
            imageId = imageIds[0];
        }
        if (imageIds != null) {

            int mainImageId = 0;
            final ImageView imageViewFirst = (ImageView) findViewById(R.id.topPreProductViewImage);
            String imagePathFirst = AppConstants.CLOUDINARY_URL + "" + imageId + ".jpg";
            Log.d("firstimage", imagePathFirst);
            Picasso.with(ProductView.this).load(imagePathFirst).into(imageViewFirst);


            for (int i = 0; i < imageIds.length; i++) {

                View cell = getLayoutInflater().inflate(R.layout.product_view_thumb, null);
                final ImageView imageView = (ImageView) cell.findViewById(R.id._image);
                System.out.println(AppConstants.CLOUDINARY_URL + "w_100,h_100,c_fill/" + imageIds[i] + ".jpg");
                String imagePath = AppConstants.CLOUDINARY_URL + "w_100,h_100,c_fill/" + imageIds[i] + ".jpg";
                Picasso.with(ProductView.this).load(imagePath).into(imageView);
                imageView.setOnClickListener(new OnImageClickListener(i));
                if (imageIds[i] == imageId)
                    mainImageId = i;
//                ImageDownloader imgDownloader = new ImageDownloader();
//                imgDownloader.download(imagePath, imageView);

                imageRowLayout.addView(cell);
            }
            imageViewFirst.setOnClickListener(new OnImageClickListener(mainImageId));
        }

    }


    class OnImageClickListener implements View.OnClickListener {

        int postion;
        int tag;

        //        String[] imageIds=new String[imageList.size()];
        // constructor
        public OnImageClickListener(int tag) {
            this.tag = tag;

        }

        @Override
        public void onClick(View v) {
            // on selecting grid view image
            // launch full screen activity
            Intent intent = new Intent(ProductView.this, FullScreenViewActivity.class);
            intent.putExtra("position", tag);
            intent.putExtra("images", imageIds);
            startActivity(intent);
        }

    }

    public void publishProduct(View V) {
        HashMap<String, String> productMap = new HashMap<>();
        productMap.put("access_token", AppConstants.userAccessToken);
        productMap.put("id", productId);
        productMap.put("merchantId", AppConstants.merchantId);
        productMap.put("status", "publish");
        omgService.createProduct(productMap, new Callback<Product>() {
            @Override
            public void success(Product productResponse, Response response) {
                if (productResponse != null) {
                    product = productResponse;
//                    productId=productResponse.getId();

                    product.setStatus("publish");
                    loadProduct(product);
                    Toast.makeText(ProductView.this, "Product published successfully.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ProductView.this, "Error in saving product.", Toast.LENGTH_SHORT).show();
                System.out.println("error " + error.getMessage());
            }
        });


    }

    public void shareProduct(View V) {

        showShareDialog();
//
//        Intent share = new Intent(Intent.ACTION_SEND);
//        share.setType("*/*");
//        share.putExtra(Intent.EXTRA_STREAM, AppConstants.CLOUDINARY_URL + product.getImageId() + ".jpg");
//        share.putExtra(Intent.EXTRA_SUBJECT, product.getTitle());
//        share.putExtra(Intent.EXTRA_TITLE, product.getTitle());
//        share.putExtra(Intent.EXTRA_TEXT, product.getDescription());
//        startActivity(Intent.createChooser(share, "Share Product"));
    }

    public void shareOnWhatsApp(View V)
    {

        Uri imageUri = Uri.parse(AppConstants.CLOUDINARY_URL+"w_560,h_560"+product.getImageId()+".jpg");
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage("com.whatsapp");
        shareIntent.putExtra(Intent.EXTRA_TEXT, product.getTitle());
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Whatsapp have not been installed.", Toast.LENGTH_LONG);
        }
    }

    public void shareOnInstagram(View V)
    {

        Uri file = Uri.parse(AppConstants.CLOUDINARY_URL+"w_560,h_560"+product.getImageId()+".jpg");
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM,file);
        shareIntent.putExtra(Intent.EXTRA_TITLE, product.getTitle());
        shareIntent.setPackage("com.instagram.android");
        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Whatsapp have not been installed.", Toast.LENGTH_LONG);
        }
    }
    public void shareOnPintrest(View V)
    {
        String shareUrl = "http://zisplay.com/p-" + product.getId();
        String mediaUrl = AppConstants.CLOUDINARY_URL+"w_560,h_560"+product.getImageId()+".jpg";
        String description = product.getTitle();
        String url = String.format(
                "https://www.pinterest.com/pin/create/button/?url=%s&media=%s&description=%s",
                urlEncode(shareUrl), urlEncode(mediaUrl), description);
        Intent shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        Context context=getApplicationContext();
        filterByPackageName(context, shareIntent, "com.pinterest");
        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Whatsapp have not been installed.", Toast.LENGTH_LONG);
        }
    }
public void shareOnTwitter(View V)
{
    String url = "http://www.twitter.com/intent/tweet?url="+"http://zisplay.com/p-" + product.getId()+"&text="+product.getTitle();
    Intent shareIntent = new Intent(Intent.ACTION_VIEW);
    shareIntent.setData(Uri.parse(url));
    try {
        startActivity(shareIntent);
    } catch (android.content.ActivityNotFoundException ex) {
        Toast.makeText(this, "Whatsapp have not been installed.", Toast.LENGTH_LONG);
    }
}
    public void shareOnFacebook(View V)
    {
        if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            // Publish the post using the Share Dialog
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                    .setLink("http://zisplay.com/p-" + product.getId())
                    .setCaption(product.getTitle())
                    .setDescription(product.getDescription())
                    .setPicture(AppConstants.CLOUDINARY_URL+"w_560,h_560"+product.getImageId()+".jpg")
                    .build();
            uiHelper.trackPendingDialogCall(shareDialog.present());

        } else {
            // Fallback. For example, publish the post using the Feed Dialog

            Bundle params = new Bundle();
            params.putString("name", AppConstants.merchantObj.getName());
            params.putString("caption", product.getTitle());
            params.putString("description", product.getDescription());
            params.putString("link", "http://zisplay.com/f/" + localData.getData("handle"));
            params.putString("picture", AppConstants.CLOUDINARY_URL+"w_560,h_560"+product.getImageId()+".jpg");
           WebDialog feedDialog = (
                    new WebDialog.FeedDialogBuilder(ProductView.this,
                            AppConstants.FACEBOOK_ID,
                            params))
                    .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                        @Override
                        public void onComplete(Bundle values,
                                               FacebookException error) {
                            if (error == null) {
                                final String postId = values.getString("post_id");
                                if (postId != null) {
                                    Toast.makeText(ProductView.this,
                                            "Posted story, id: " + postId,
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ProductView.this.getApplicationContext(),
                                            "Publish cancelled",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else if (error instanceof FacebookOperationCanceledException) {
                                // User clicked the "x" button
                                Toast.makeText(ProductView.this.getApplicationContext(),
                                        "Publish cancelled",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProductView.this.getApplicationContext(),
                                        "Error posting story",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                    })
                    .build();
            feedDialog.show();
        }
    }
    public static void filterByPackageName(Context context, Intent intent, String prefix) {
        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith(prefix)) {
                intent.setPackage(info.activityInfo.packageName);
                return;
            }
        }
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            Log.wtf("", "UTF-8 should always be supported", e);
            return "";
        }
    }

    public void showShareDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_product_share, null);

        alert = new AlertDialog.Builder(this).create();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        alert.setCancelable(true);
        alert.setView(promptsView);
        alert.show();

    }

    public void hideDialog(View V)
    {
        alert.cancel();
    }

    public void editProduct(View V)
    {
        Intent productEditIntent = new Intent(ProductView.this, NewProductActivity.class);
        productEditIntent.putExtra("productId", productId);
        startActivity(productEditIntent);
    }
}
