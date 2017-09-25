package vun.zisplay.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import vun.zisplay.network.RestClient;
import vun.zisplay.utils.AppConstants;
import vun.zisplay.R;
import vun.zisplay.Zisplay;
import vun.zisplay.managers.LocalData;
import vun.zisplay.models.Product;
import vun.zisplay.network.OMGServices;
import vun.zisplay.utils.ImageDownloader;

public class Catalog extends ZisplayBaseActivity {
    ProductListAdapter adapter;
    ListView list_Product;

    ProductListAdapter cla;
    ProgressDialog pgdlg;

    LocalData localData=null;
    OMGServices omgService= RestClient.getInstance().getApiService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics(this.getClass().getName());
        setContentView(R.layout.activity_catalog);
        list_Product = (ListView) findViewById(R.id.lst_productList);
        adapter = new ProductListAdapter(this,new ProductList());
        list_Product.setAdapter(adapter);
        cla = adapter;
        pgdlg = ProgressDialog.show(this, "Please Wait", "Loading Products...", true);
        localData=LocalData.getInstance();
        loadCatalog();
    }

    void loadCatalog()
    {
        HashMap productMap=new HashMap<String,String>();
        productMap.put("filter[include]","productImage");
        productMap.put("filter[where][merchantId]",localData.getData("merchantId"));
        omgService.getProducts(productMap, new Callback<ArrayList<Product>>() {
            @Override
            public void success(ArrayList<Product> productResponse, Response response) {
                System.out.println("product response " + productResponse);
                if(pgdlg.isShowing())
                    pgdlg.dismiss();
                ProductList pl=new ProductList();//productResponse);
                    pl.setProducts(productResponse);
                cla.gcl=pl;
                cla.notifyDataSetChanged();
            }
            @Override
            public void failure(RetrofitError error) {
                System.out.println("error " + error.getMessage());
                if(pgdlg.isShowing())
                    pgdlg.dismiss();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
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


    class ProductList
    {

        private ArrayList<Product> products = new ArrayList<Product>();

        public int getCount()
        {
            return this.products.size();
        }
        public void addProduct(Product c)
        {
            this.products.add(c);
        }
        public void removeProduct(Product c)
        {
            this.products.remove(c);
        }
        public void removeProduct(int id)
        {
            for(int i=0;i<this.getCount();i++)
            {
                if(id==Integer.parseInt(this.products.get(i).getId()))
                {
                    this.products.remove(this.products.get(i));
                }
            }
        }
    public Product getProduct(int id)
    {
        Product tmp=null;
        for(int i=0;i<this.getCount();i++)
        {
            if(id==Integer.parseInt(this.products.get(i).getId()))
            {
                return this.products.get(i);
            }
        }
        return tmp;
    }
    public ArrayList<Product> getProducts()
    {
        return products;
    }
    public void setProducts(ArrayList<Product> c)
    {
        this.products=c;
    }

}

class ProductListAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener
{
    Context context;
    ProductList gcl;
    ProductList selectedContacts;
    public ProductListAdapter(Context context,ProductList gcl)
    {
        super();
        this.context = context;
        this.gcl=gcl;
        selectedContacts = new ProductList();

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Product product=gcl.getProducts().get(position);
        LayoutInflater inflater = (LayoutInflater)    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view_row = inflater.inflate(R.layout.product_list_layout, parent,false);
        ImageView img_product = (ImageView) view_row.findViewById(R.id.imageView);
        TextView chk_contact = (TextView) view_row.findViewById(R.id.productView);
        chk_contact.setText(product.getTitle().toString() + " ( "+product.getTitle() + ")");
        chk_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = product.getId();
                Intent chatViewIntent = new Intent(Catalog.this, NewProductActivity.class);
                chatViewIntent.putExtra("productId", id);
                startActivity(chatViewIntent);
            }
        });
        String imageIds[]=product.getImages();
        if(imageIds!=null && imageIds[0]!=null) {
            String imagePath= AppConstants.CLOUDINARY_URL+"w_100,h_100/"+imageIds[0]+".jpg";
            ImageDownloader imgDownloader= new ImageDownloader();
            imgDownloader.download(imagePath,img_product);
        }
        return view_row;
    }
    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
        Product t = gcl.getProduct(arg0.getId());
        if(t!=null && arg1)
        {
            if(!alreadySelected(t))
                selectedContacts.addProduct(t);
        }
        else if(!arg1 && t!=null)
        {
            selectedContacts.removeProduct(arg0.getId());
        }
    }
    public boolean alreadySelected(Product t)
    {
        boolean ret = false;
        if(selectedContacts.getProduct(Integer.parseInt(t.getId()))!=null)
            ret=true;
        return ret;
    }
    @Override
    public int getCount() {
        return gcl.getCount();
    }
    @Override
    public Product getItem(int arg0) {
        return gcl.getProducts().get(arg0);
    }
    @Override
    public long getItemId(int arg0) {
        return  arg0;
    }
}
}
