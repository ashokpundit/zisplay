package vun.zisplay.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
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
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import vun.zisplay.network.RestClient;
import vun.zisplay.utils.AppConstants;
import vun.zisplay.R;
import vun.zisplay.Zisplay;
import vun.zisplay.managers.LocalData;
import vun.zisplay.models.ChatItem;
import vun.zisplay.models.Product;
import vun.zisplay.network.OMGServices;

public class ConversastionList extends ZisplayBaseActivity {

    OMGServices omgService= RestClient.getInstance().getApiService();
    ChatListAdapter adapter;
    ListView list_Product;

    ChatListAdapter cla;
    ProgressDialog pgdlg;
    LocalData localData=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics(this.getClass().getName());
        setContentView(R.layout.activity_conversastion_list);
        localData=LocalData.getInstance();
        list_Product = (ListView) findViewById(R.id.lst_conversastionList);
        adapter = new ChatListAdapter(this,new ChatList());
        list_Product.setAdapter(adapter);
        cla = adapter;
        pgdlg = ProgressDialog.show(this, "Please Wait", "Loading Products...", true);
        if(localData.getData("userId")==null||localData.getData("userId").isEmpty())
            localData.saveData("userId", "54e44bc2daaa8adf2836d525");


        loadConversastion();
    }


    void loadConversastion()
    {
        HashMap productMap=new HashMap<String,String>();
        System.out.println("userId " + localData.getData("userId").toString());
        productMap.put("filter[where][users]",localData.getData("userId").toString());
        omgService.getChats(productMap, new Callback<ArrayList<ChatItem>>() {
            @Override
            public void success(ArrayList<ChatItem> productResponse, Response response) {
                System.out.println("product response " + productResponse);
                if (pgdlg.isShowing())
                    pgdlg.dismiss();
                ChatList pl = new ChatList();//productResponse);
                pl.setProducts(productResponse);
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
        getMenuInflater().inflate(R.menu.menu_conversastion_list, menu);
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
    class ChatList
    {
        private ArrayList<ChatItem> chatItems = new ArrayList<ChatItem>();

        public int getCount()
        {
            return this.chatItems.size();
        }
        public void addChatItem(ChatItem c)
        {
            this.chatItems.add(c);
        }
        public void removeChatItem(ChatItem c)
        {
            this.chatItems.remove(c);
        }
        public void removeChatItem(int id)
        {
            for(int i=0;i<this.getCount();i++)
            {
                if(id==Integer.parseInt(this.chatItems.get(i).getId()))
                {
                    this.chatItems.remove(this.chatItems.get(i));
                }
            }
        }
        public ChatItem getChatItem(int id)
        {
            ChatItem tmp=null;
            for(int i=0;i<this.getCount();i++)
            {
                if(id==Integer.parseInt(this.chatItems.get(i).getId()))
                {
                    return this.chatItems.get(i);
                }
            }
            return tmp;
        }
        public ArrayList<ChatItem> getChatItems()
        {
            return chatItems;
        }
        public void setProducts(ArrayList<ChatItem> c)
        {
            this.chatItems=c;
        }
    }


    class ChatListAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener, View.OnClickListener
    {
        Context context;
        ChatList gcl;
        ChatList selectedChats;
        public ChatListAdapter(Context context,ChatList gcl)
        {
            super();
            this.context = context;
            this.gcl=gcl;
            selectedChats = new ChatList();
        }
        /*Custom View Generation(You may modify this to include other Views) */
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
           final ChatItem chatItem=gcl.getChatItems().get(position);
            LayoutInflater inflater = (LayoutInflater)    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view_row = inflater.inflate(R.layout.chat_layout, parent,false);
            view_row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String id=chatItem.getId();
                    String[] users=chatItem.getUsers();
                    String[] userNames=chatItem.getUserNames();
                    Intent chatViewIntent=new Intent(ConversastionList.this, ChatHistory.class);
                    chatViewIntent.putExtra("chatId",id);
                    chatViewIntent.putExtra("users",users);
                    chatViewIntent.putExtra("userNames",userNames);
                    startActivity(chatViewIntent);
                }
            });
            ImageView img_product = (ImageView) view_row.findViewById(R.id.chatImageView);
            TextView chk_contact = (TextView) view_row.findViewById(R.id.chatTextView);
            String messageType=gcl.getChatItems().get(position).getLastMessageType();
            if(messageType.equalsIgnoreCase("image")) {
                String imageIds = gcl.getChatItems().get(position).getLastMessage();
                Uri uri = new Uri.Builder().path(AppConstants.CLOUDINARY_URL + imageIds + ".jpg").build();
                img_product.setImageURI(uri);
                chk_contact.setText(gcl.getChatItems().get(position).getUserNames()[0].toString() );
            }
            else
            {
                chk_contact.setText(gcl.getChatItems().get(position).getUserNames()[0].toString() + " ( "+gcl.getChatItems().get(position).getLastMessage() + ")");
            }
            return view_row;
        }
        @Override
        public void onClick(View v)
        {

        }

        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
        }
        public boolean alreadySelected(Product t)
        {
            return false;
        }
        @Override
        public int getCount() {

            return gcl.getCount();
        }
        @Override
        public ChatItem getItem(int arg0) {
            return gcl.getChatItems().get(arg0);
        }
        @Override
        public long getItemId(int arg0) {
            return  Long.parseLong(gcl.getChatItems().get(arg0).getId());
        }
    }



}
