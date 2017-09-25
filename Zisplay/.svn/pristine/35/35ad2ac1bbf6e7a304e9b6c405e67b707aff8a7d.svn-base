package vun.zisplay.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import vun.zisplay.R;
import vun.zisplay.managers.LocalData;
import vun.zisplay.models.ProductLikes;
import vun.zisplay.models.Product;
import vun.zisplay.network.OMGServices;
import vun.zisplay.network.RestClient;
import vun.zisplay.utils.AppConstants;
import vun.zisplay.utils.ImageDownloader;

public class LovedProductList extends ZisplayBaseActivity {

    ProductLikesListAdapter adapter;
    ListView list_ProductLikes;

    ProductLikesListAdapter cla;
    ProgressDialog pgdlg;

    LocalData localData=LocalData.getInstance();
    OMGServices omgService= RestClient.getInstance().getApiService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loved_product_list);
        analytics(this.getClass().getName());
        list_ProductLikes = (ListView) findViewById(R.id.lst_productList);
        adapter = new ProductLikesListAdapter(this,new ProductLikesList());
        list_ProductLikes.setAdapter(adapter);
        cla = adapter;
        pgdlg = ProgressDialog.show(this, "Please Wait", "Loading ProductLikess...", true);
        localData=LocalData.getInstance();
        loadProductLikess();
    }

    void loadProductLikess()
    {
        HashMap ProductLikesMap=new HashMap<String,String>();
        ProductLikesMap.put("userId",AppConstants.userId);


        omgService.getLovedProducts(ProductLikesMap, new Callback<ArrayList<ProductLikes>>() {
            @Override
            public void success(ArrayList<ProductLikes> ProductLikesResponse, Response response) {
                System.out.println("ProductLikes response " + ProductLikesResponse);
                if (pgdlg.isShowing())
                    pgdlg.dismiss();
                ProductLikesList pl = new ProductLikesList();//ProductLikesResponse);
                pl.setProductLikess(ProductLikesResponse);
                cla.gcl = pl;
                cla.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("error " + error.getMessage());
                if (pgdlg.isShowing())
                    pgdlg.dismiss();
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loved_product_list, menu);
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

    class ProductLikesList
    {

        private ArrayList<ProductLikes> ProductLikess = new ArrayList<ProductLikes>();

        public int getCount()
        {
            return this.ProductLikess.size();
        }
        public void addProductLikes(ProductLikes c)
        {
            this.ProductLikess.add(c);
        }
        public void removeProductLikes(ProductLikes c)
        {
            this.ProductLikess.remove(c);
        }
        public void removeProductLikes(int id)
        {
            for(int i=0;i<this.getCount();i++)
            {
                if(id==Integer.parseInt(this.ProductLikess.get(i).getId()))
                {
                    this.ProductLikess.remove(this.ProductLikess.get(i));
                }
            }
        }
        public ProductLikes getProductLikes(int id)
        {
            ProductLikes tmp=null;
            for(int i=0;i<this.getCount();i++)
            {
                if(id==Integer.parseInt(this.ProductLikess.get(i).getId()))
                {
                    return this.ProductLikess.get(i);
                }
            }
            return tmp;
        }
        public ArrayList<ProductLikes> getProductLikess()
        {
            return ProductLikess;
        }
        public void setProductLikess(ArrayList<ProductLikes> c)
        {
            this.ProductLikess=c;
        }

    }

    class ProductLikesListAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener
    {
        Context context;
        ProductLikesList gcl;
        ProductLikesList selectedContacts;
        public ProductLikesListAdapter(Context context,ProductLikesList gcl)
        {
            super();
            this.context = context;
            this.gcl=gcl;
            selectedContacts = new ProductLikesList();

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            final ProductLikes ProductLikes=gcl.getProductLikess().get(position);
            LayoutInflater inflater = (LayoutInflater)    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view_row = inflater.inflate(R.layout.product_list_layout, parent,false);
            ImageView img_ProductLikes = (ImageView) view_row.findViewById(R.id.imageView);
            TextView chk_contact = (TextView) view_row.findViewById(R.id.productView);

            chk_contact.setText(ProductLikes.getProduct().getDescription() + " ( "+ProductLikes.getProduct().getTitle() + ")");
            view_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = ProductLikes.getId();
                    Intent chatViewIntent = new Intent(LovedProductList.this, NewProductActivity.class);
                    chatViewIntent.putExtra("ProductLikesId", id);
                    startActivity(chatViewIntent);
                }
            });
            String imageIds[]=ProductLikes.getProduct().getImages();
            if(imageIds!=null && imageIds[0]!=null) {
                String imagePath= AppConstants.CLOUDINARY_URL+"w_100,h_100/"+imageIds[0]+".jpg";
                ImageDownloader imgDownloader= new ImageDownloader();
                imgDownloader.download(imagePath,img_ProductLikes);
            }
            return view_row;
        }
        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            ProductLikes t = gcl.getProductLikes(arg0.getId());
            if(t!=null && arg1)
            {
                if(!alreadySelected(t))
                    selectedContacts.addProductLikes(t);
            }
            else if(!arg1 && t!=null)
            {
                selectedContacts.removeProductLikes(arg0.getId());
            }
        }
        public boolean alreadySelected(ProductLikes t)
        {


            return false;
        }
        @Override
        public int getCount() {
            return gcl.getCount();
        }
        @Override
        public ProductLikes getItem(int arg0) {
            return gcl.getProductLikess().get(arg0);
        }
        @Override
        public long getItemId(int arg0) {
            return  arg0;
        }
    }
}
