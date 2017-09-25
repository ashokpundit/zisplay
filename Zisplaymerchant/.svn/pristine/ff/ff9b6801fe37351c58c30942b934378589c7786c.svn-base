package vun.zisplaymerchant.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import vun.zisplaymerchant.R;
import vun.zisplaymerchant.managers.LocalData;
import vun.zisplaymerchant.models.PayUPayment;
import vun.zisplaymerchant.models.Product;
import vun.zisplaymerchant.network.OMGServices;
import vun.zisplaymerchant.network.RestClient;
import vun.zisplaymerchant.utils.AppConstants;
import vun.zisplaymerchant.utils.ImageDownloader;

public class PaymentList extends ZisplayBaseActivity {
    static FragmentManager fm;
    static ActionBar actionBar;
    PayUPaymentListAdapter adapter;
    ListView list_Product;
    PayUPaymentListAdapter cla;
    ProgressDialog pgdlg;
    LocalData localData=null;
    OMGServices omgService= RestClient.getInstance().getApiService();
    ViewPager mViewPager;
    TabsAdapter mTabsAdapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_list);
        analytics(this.getClass().getName());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.canShowOverflowMenu();
        setSupportActionBar(toolbar);
        localData=LocalData.getInstance();
        list_Product = (ListView) findViewById(R.id.lst_paymentList);
        adapter = new PayUPaymentListAdapter(this,new PayUPaymentList());
        list_Product.setAdapter(adapter);
        cla = adapter;
        fm=getSupportFragmentManager();
//        setTabs(); ,
        loadPayments();
    }

    public void showPending(View V)
    {

        TextView tab=(TextView)findViewById(R.id.pendingTab);
        TextView tab2=(TextView)findViewById(R.id.shippedTab);
        TextView tab3=(TextView)findViewById(R.id.paidTab);
        tab.setTextColor(R.color.selected_tab_text);
        tab.setBackgroundResource(R.drawable.tablayout_heighlight);
        tab2.setTextColor(R.color.default_tab_text);
        tab3.setTextColor(R.color.default_tab_text);
//        changeTab(0);

    }
    public void showShipped(View V)
    {

        TextView tab=(TextView)findViewById(R.id.pendingTab);
        TextView tab2=(TextView)findViewById(R.id.shippedTab);
        TextView tab3=(TextView)findViewById(R.id.paidTab);
        tab2.setTextColor(R.color.selected_tab_text);
        tab2.setBackgroundResource(R.drawable.tablayout_heighlight);
        tab.setTextColor(R.color.default_tab_text);
        tab3.setTextColor(R.color.default_tab_text);
//        changeTab(0);

    }
    public void showPaid(View V)
    {

        TextView tab=(TextView)findViewById(R.id.pendingTab);
        TextView tab2=(TextView)findViewById(R.id.shippedTab);
        TextView tab3=(TextView)findViewById(R.id.paidTab);
        tab3.setTextColor(R.color.selected_tab_text);
        tab3.setBackgroundResource(R.drawable.tablayout_heighlight);
        tab.setTextColor(R.color.default_tab_text);
        tab2.setTextColor(R.color.default_tab_text);
//        changeTab(0);

    }
    void setTabs()
    {
        actionBar = getSupportActionBar();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        getSupportActionBar().setSelectedNavigationItem(position);
                    }
                });
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        };
        actionBar.addTab(actionBar.newTab().setText("PENDING").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("SHIPPED").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("PAID").setTabListener(tabListener));
    }
    void loadPayments()
    {
        pgdlg = ProgressDialog.show(this, "Please Wait", "Loading Payments...", true);
        HashMap productMap=new HashMap<String,String>();
        productMap.put("merchantId",localData.getData("merchantId"));
        omgService.getPayments(productMap, new Callback<ArrayList<PayUPayment>>() {
            @Override
            public void success(ArrayList<PayUPayment> productResponse, Response response) {
                System.out.println("product response " + productResponse);
                if(pgdlg.isShowing())
                    pgdlg.dismiss();
                PayUPaymentList pl=new PayUPaymentList();
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
        getMenuInflater().inflate(R.menu.menu_payment_list, menu);
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


    public static class TabsAdapter extends FragmentPagerAdapter
            implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final ActionBar mActionBar;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        static final class TabInfo {
            private final Class<?> clss;
            private final Bundle args;
            TabInfo(Class<?> _class, Bundle _args) {
                clss = _class;
                args = _args;
            }
        }

        public TabsAdapter(Activity activity, ViewPager pager) {
            super(fm);
            mContext = activity;
            mActionBar = actionBar;
            mViewPager = pager;
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
            TabInfo info = new TabInfo(clss, args);
            tab.setTag(info);
            tab.setTabListener(this);
            mTabs.add(info);
            mActionBar.addTab(tab);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mActionBar.setSelectedNavigationItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            Object tag = tab.getTag();
            for (int i=0; i<mTabs.size(); i++) {
                if (mTabs.get(i) == tag) {
                    mViewPager.setCurrentItem(i);
                }
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
    }
}
