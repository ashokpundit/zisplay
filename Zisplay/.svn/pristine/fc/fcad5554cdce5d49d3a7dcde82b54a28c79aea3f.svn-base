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
import vun.zisplay.managers.AnalyticsManager;
import vun.zisplay.managers.LocalData;
import vun.zisplay.models.Merchant;
import vun.zisplay.models.Merchant;
import vun.zisplay.network.OMGServices;
import vun.zisplay.network.RestClient;
import vun.zisplay.utils.AppConstants;
import vun.zisplay.utils.ImageDownloader;


public class FollowersList extends ZisplayBaseActivity {


    MerchantListAdapter adapter;
    ListView list_Users;

    MerchantListAdapter cla;
    ProgressDialog pgdlg;

    AnalyticsManager analyticsManager=AnalyticsManager.getInstance();
    LocalData localData=null;
    OMGServices omgService= RestClient.getInstance().getApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_list);
        analytics(this.getClass().getName());

        list_Users = (ListView) findViewById(R.id.lst_followersList);
        adapter = new MerchantListAdapter(this,new MerchantList());
        list_Users.setAdapter(adapter);
        cla = adapter;

        localData= LocalData.getInstance();
        loadFollowers();
    }

    void loadFollowers()
    {
        String merchantId=localData.getData("userId");


        if(merchantId==null)
            return;
        pgdlg = ProgressDialog.show(this, "Please Wait", "Loading Merchants...", true);
        omgService.getFollowing(merchantId, new Callback<ArrayList<Merchant>>() {
            @Override
            public void success(ArrayList<Merchant> merchants, Response response) {
                System.out.println("following response " + merchants);
                if(pgdlg.isShowing())
                    pgdlg.dismiss();
                MerchantList pl=new MerchantList();//productResponse);
                pl.setMerchants(merchants);
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

    public void inviteFollowers(View v)
    {

        Intent chatViewIntent = new Intent(FollowersList.this, InviteFollowers.class);
        startActivity(chatViewIntent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_followers_list, menu);
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

    class MerchantList
    {

        private ArrayList<Merchant> products = new ArrayList<Merchant>();

        public int getCount()
        {
            return this.products.size();
        }
        public void addMerchant(Merchant c)
        {
            this.products.add(c);
        }
        public void removeMerchant(Merchant c)
        {
            this.products.remove(c);
        }
        public void removeMerchant(int id)
        {
            for(int i=0;i<this.getCount();i++)
            {
                if(id==Integer.parseInt(this.products.get(i).getId()))
                {
                    this.products.remove(this.products.get(i));
                }
            }
        }
        public Merchant getMerchant(int id)
        {
            Merchant tmp=null;
            for(int i=0;i<this.getCount();i++)
            {
                if(id==Integer.parseInt(this.products.get(i).getId()))
                {
                    return this.products.get(i);
                }
            }
            return tmp;
        }
        public ArrayList<Merchant> getMerchants()
        {
            return products;
        }
        public void setMerchants(ArrayList<Merchant> c)
        {
            this.products=c;
        }

    }

    class MerchantListAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener
    {
        Context context;
        MerchantList gcl;
        MerchantList selectedContacts;
        public MerchantListAdapter(Context context,MerchantList gcl)
        {
            super();
            this.context = context;
            this.gcl=gcl;
            selectedContacts = new MerchantList();

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            final Merchant merchnat=gcl.getMerchants().get(position);
            LayoutInflater inflater = (LayoutInflater)    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view_row = inflater.inflate(R.layout.followerthumb, parent,false);
            ImageView img_product = (ImageView) view_row.findViewById(R.id._followerImage);
            TextView chk_contact = (TextView) view_row.findViewById(R.id._followerName);
            chk_contact.setText(merchnat.getTitle()+" "+merchnat.getMobileNo() );
            chk_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = merchnat.getId();
                    Intent chatViewIntent = new Intent(FollowersList.this, MerchantProfile.class);
                    chatViewIntent.putExtra("merchantId", id);
                    startActivity(chatViewIntent);
                }
            });
            String imageIds=merchnat.getImageId();
            if(imageIds!=null ) {
                String imagePath= AppConstants.CLOUDINARY_URL+"w_100,h_100/"+imageIds+".jpg";
                ImageDownloader imgDownloader= new ImageDownloader();
                imgDownloader.download(imagePath,img_product);
            }
            return view_row;
        }
        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            Merchant t = gcl.getMerchant(arg0.getId());
            if(t!=null && arg1)
            {
                if(!alreadySelected(t))
                    selectedContacts.addMerchant(t);
            }
            else if(!arg1 && t!=null)
            {
                selectedContacts.removeMerchant(arg0.getId());
            }
        }
        public boolean alreadySelected(Merchant t)
        {
            boolean ret = false;
            if(selectedContacts.getMerchant(Integer.parseInt(t.getId()))!=null)
                ret=true;
            return ret;
        }
        @Override
        public int getCount() {
            return gcl.getCount();
        }
        @Override
        public Merchant getItem(int arg0) {
            return gcl.getMerchants().get(arg0);
        }
        @Override
        public long getItemId(int arg0) {
            return  arg0;
        }
    }
}
