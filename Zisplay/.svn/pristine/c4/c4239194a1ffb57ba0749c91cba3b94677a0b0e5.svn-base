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
import vun.zisplay.models.PayUPayment;
import vun.zisplay.models.Product;
import vun.zisplay.network.OMGServices;
import vun.zisplay.network.RestClient;
import vun.zisplay.utils.AppConstants;
import vun.zisplay.utils.ImageDownloader;

public class PaymentList extends ZisplayBaseActivity {

    PayUPaymentListAdapter adapter;
    ListView list_Product;

    PayUPaymentListAdapter cla;
    ProgressDialog pgdlg;

    LocalData localData=null;
    OMGServices omgService= RestClient.getInstance().getApiService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_list);
        analytics(this.getClass().getName());

        list_Product = (ListView) findViewById(R.id.lst_paymentList);
        adapter = new PayUPaymentListAdapter(this,new PayUPaymentList());
        list_Product.setAdapter(adapter);
        cla = adapter;
        pgdlg = ProgressDialog.show(this, "Please Wait", "Loading Payments...", true);
        localData=LocalData.getInstance();
        loadPayments();
    }
    void loadPayments()
    {
        HashMap productMap=new HashMap<String,String>();
//        productMap.put("filter[include]","productImage");
        productMap.put("userId",localData.getData("userId"));
        omgService.getPayments(productMap, new Callback<ArrayList<PayUPayment>>() {
            @Override
            public void success(ArrayList<PayUPayment> productResponse, Response response) {
                System.out.println("paymentlist response " + productResponse);
                if(pgdlg.isShowing())
                    pgdlg.dismiss();
                PayUPaymentList pl=new PayUPaymentList();//productResponse);
                pl.setPayUPayments(productResponse);
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
        getMenuInflater().inflate(R.menu.menu_payment_list, menu);
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


    class PayUPaymentList
    {

        private ArrayList<PayUPayment> products = new ArrayList<PayUPayment>();

        public int getCount()
        {
            return this.products.size();
        }
        public void addPayUPayment(PayUPayment c)
        {
            this.products.add(c);
        }
        public void removePayUPayment(PayUPayment c)
        {
            this.products.remove(c);
        }
        public void removePayUPayment(int id)
        {
            for(int i=0;i<this.getCount();i++)
            {
                if(id==Integer.parseInt(this.products.get(i).getId()))
                {
                    this.products.remove(this.products.get(i));
                }
            }
        }
        public PayUPayment getPayUPayment(int id)
        {
            PayUPayment tmp=null;
            for(int i=0;i<this.getCount();i++)
            {
                if(id==Integer.parseInt(this.products.get(i).getId()))
                {
                    return this.products.get(i);
                }
            }
            return tmp;
        }
        public ArrayList<PayUPayment> getPayUPayments()
        {
            return products;
        }
        public void setPayUPayments(ArrayList<PayUPayment> c)
        {
            this.products=c;
        }

    }

    class PayUPaymentListAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener
    {
        Context context;
        PayUPaymentList gcl;
        PayUPaymentList selectedContacts;
        public PayUPaymentListAdapter(Context context,PayUPaymentList gcl)
        {
            super();
            this.context = context;
            this.gcl=gcl;
            selectedContacts = new PayUPaymentList();

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            final PayUPayment payment=gcl.getPayUPayments().get(position);
            LayoutInflater inflater = (LayoutInflater)    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view_row = inflater.inflate(R.layout.paymentlistthumb, parent,false);

            TextView chk_contact = (TextView) view_row.findViewById(R.id.paymentItem);
            chk_contact.setText(payment.getAmount() + " ( "+payment.getCreated() + ")");
            chk_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            return view_row;
        }
        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            PayUPayment t = gcl.getPayUPayment(arg0.getId());
            if(t!=null && arg1)
            {
                if(!alreadySelected(t))
                    selectedContacts.addPayUPayment(t);
            }
            else if(!arg1 && t!=null)
            {
                selectedContacts.removePayUPayment(arg0.getId());
            }
        }
        public boolean alreadySelected(PayUPayment t)
        {
            boolean ret = false;
            if(selectedContacts.getPayUPayment(Integer.parseInt(t.getId()))!=null)
                ret=true;
            return ret;
        }
        @Override
        public int getCount() {
            return gcl.getCount();
        }
        @Override
        public PayUPayment getItem(int arg0) {
            return gcl.getPayUPayments().get(arg0);
        }
        @Override
        public long getItemId(int arg0) {
            return  arg0;
        }
    }
}
