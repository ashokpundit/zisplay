package vun.zisplaymerchant.activity;

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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import it.sephiroth.android.library.picasso.Picasso;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;
import vun.zisplaymerchant.managers.AnalyticsManager;
import vun.zisplaymerchant.models.Message;
import vun.zisplaymerchant.models.PaymentLink;
import vun.zisplaymerchant.network.RestClient;
import vun.zisplaymerchant.utils.AppConstants;
import vun.zisplaymerchant.R;
import vun.zisplaymerchant.managers.LocalData;
import vun.zisplaymerchant.network.OMGServices;


public class ChatHistory extends ZisplayBaseActivity {


    OMGServices omgService= RestClient.getInstance().getApiService();
    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    final static int GENERATE_PAYMENT_LINK = 2;
    static String Path=null;
    TypedFile imageFile=null;
    public  static ImageView showImg  = null;
    Uri imageUri                      = null;
    MessageListAdapter adapter;
    ListView list_message;


    MessageListAdapter cla;
    ProgressDialog pgdlg;
    LocalData localData=null;
    String chatId=null;//"54ed7200e4e3137a5fc07d7e";
    String[] users=null;//{"54eade97dcc01cdb218149a3","54e44bc2daaa8adf2836d527"};
    String[] userNames=null;//{"ashok","rakesh"};
    String merchantId=null;//"54eade97dcc01cdb218149a3";
    String merchantName=null;//"ashok";
    String otherUserId=null;//"54e44bc2daaa8adf2836d527";
    String otherUserName=null;//"rakesh";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        analytics(this.getClass().getName());
        setContentView(R.layout.activity_chat_history);
        Intent intent = getIntent();
        localData = LocalData.getInstance();
        if(users==null) {
            chatId = intent.getStringExtra("chatId");
            users = intent.getStringArrayExtra("users");
            userNames = intent.getStringArrayExtra("userNames");
            merchantId = localData.getData("merchantId");
            merchantName = localData.getData("zisplayHandle");
        }
        list_message = (ListView) findViewById(R.id.lst_chatHistoryList);
        adapter = new MessageListAdapter(this,new MessageList());
            list_message.setAdapter(adapter);
        cla = adapter;
        pgdlg = ProgressDialog.show(this, "Please Wait", "Loading Products...", true);
        if(localData.getData("merchantId")==null||localData.getData("merchantId").isEmpty())
            localData.saveData("merchantId", "54eade97dcc01cdb218149a3");

        showImg = (ImageView) findViewById(R.id.newImageView);
        loadConversastion();
    }


    void loadConversastion()
    {
        HashMap messageMap=new HashMap<String,String>();
//        productMap.put("{\"where\":{\"users\":\"54e44bc2daaa8adf2836d525\"}}","productImage");
        System.out.println("chat id " + chatId.toString());
        messageMap.put("filter[where][chatId]",chatId.toString());
        omgService.getMessages(messageMap, new Callback<ArrayList<Message>>() {
            @Override
            public void success(ArrayList<Message> messageList, Response response) {
                System.out.println("Message response " + messageList);
                if (pgdlg.isShowing())
                    pgdlg.dismiss();

                if(users[0].equalsIgnoreCase(merchantId))
                {
                    otherUserId=users[1];
                    otherUserName=userNames[1];
                }
                else
                {
                    otherUserId=users[0];
                    otherUserName=userNames[0];
                }
                MessageList pl = new MessageList();//productResponse);
                pl.setMessages(messageList);
                    cla.gcl = pl;
                    cla.notifyDataSetChanged();
                omgService.markMessageRead(AppConstants.userAccessToken,otherUserId,new Callback<String>() {
                    @Override
                    public void success(String message, Response response) {

                    }
                    @Override
                    public void failure(RetrofitError error) {

                }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("error " + error.getMessage());
                if (pgdlg.isShowing())
                    pgdlg.dismiss();
            }

        });
    }

    public void createPaymentLink(View V)
    {
        String accessToken=localData.getData("accessToken");
        String merchantId=localData.getData("merchantId");
        String price="90";
        String productId="54f14c8ac28d9dc5290715dc";


        omgService.createPaymentLink(accessToken,merchantId,productId,price, new Callback<PaymentLink>() {
            @Override
            public void success(PaymentLink paymentLink, Response response) {
                System.out.println("Message response " + paymentLink);
                if (pgdlg.isShowing())
                    pgdlg.dismiss();

                final EditText messageTextView = (EditText) findViewById(R.id.messageText);
                messageTextView.setText(AppConstants.SERVER_BASE_URL+"/pay/"+paymentLink.getId());

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
        getMenuInflater().inflate(R.menu.menu_chat_history, menu);
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


    public void startCamera(View v)
    {
        Random r = new Random();
        int i1 = (r.nextInt(10000));
        String fileName = i1+"-"+"chat-image.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION,"chat image");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        if (intent.resolveActivity(getPackageManager()) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if ( resultCode == RESULT_OK) {
//                performCrop(imageUri);

                String imageId = convertImageUriToFile( imageUri,ChatHistory.this);
                new LoadImagesFromSDCard().execute(""+imageId);
            } else if ( resultCode == RESULT_CANCELED) {
                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public static String convertImageUriToFile ( Uri imageUri, Activity activity )
    {
        Cursor cursor = null;

        int imageID = 0;

        try {
            String [] proj={
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Thumbnails._ID,
                    MediaStore.Images.ImageColumns.ORIENTATION
            };
            cursor = activity.managedQuery(
                    imageUri,
                    proj,
                    null,
                    null,
                    null
            );
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int columnIndexThumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
            int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int size = cursor.getCount();
            if (size == 0) {
//            imageDetails.setText("No Image");
            }
            else
            {
                int thumbID = 0;
                if (cursor.moveToFirst()) {
                    imageID= cursor.getInt(columnIndex);
                    thumbID= cursor.getInt(columnIndexThumb);
                    Path = cursor.getString(file_ColumnIndex);
                    String CapturedImageDetails = " CapturedImageDetails : \n\n"
                            +" ImageID :"+imageID+"\n"
                            +" ThumbID :"+thumbID+"\n"
                            +" Path :"+Path+"\n";
//            imageDetails.setText( CapturedImageDetails );
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return ""+imageID;
    }




    public void sendMessage(View v)
    {
        final EditText messageTextView = (EditText) findViewById(R.id.messageText);
        final String message=messageTextView.getText().toString();

//        System.out.println("Product Created "+imageUri.toString());

        if(Path!=null)
        imageFile=new TypedFile("image/jpg",new File(Path));//"54e40e2234fd564a1696c352"

        omgService.sendMessage(AppConstants.userAccessToken,new TypedString(otherUserId), new TypedString(merchantId), new TypedString(otherUserName), new TypedString(merchantName), new TypedString(message), imageFile, new Callback<Message>() {
                        @Override
                        public void success(Message message, Response response) {

                            System.out.println("image created ");
                            messageTextView.clearComposingText();
                            Path=null;
                            imageFile=null;
                            imageUri=null;
                            cla.gcl.addMessage(message);
                            cla.notifyDataSetChanged();

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            System.out.println("error " + error.getMessage());
                        }
                    });
    }


    class MessageList
    {
        private ArrayList<Message> messages = new ArrayList<Message>();

        public int getCount()
        {
            return this.messages.size();
        }
        public void addMessage(Message c)
        {
            this.messages.add(c);
        }
        public void removeMessage(Message c)
        {
            this.messages.remove(c);
        }
        public void removeMessage(String id)
        {
            for(int i=0;i<this.getCount();i++)
            {
                if(id==this.messages.get(i).getId())
                {
                    this.messages.remove(this.messages.get(i));
                }
            }
        }
        public Message getMessage(String id)
        {
            Message tmp=null;
            for(int i=0;i<this.getCount();i++)
            {
                if(id==this.messages.get(i).getId())
                {
                    return this.messages.get(i);
                }
            }
            return tmp;
        }
        public ArrayList<Message> getMessages()
        {
            return messages;
        }
        public void setMessages(ArrayList<Message> c)
        {
            this.messages=c;
        }
    }


    class MessageListAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener, View.OnClickListener
    {
        Context context;
        MessageList gcl;
        MessageList selectedChats;
        public MessageListAdapter(Context context,MessageList gcl)
        {
            super();
            this.context = context;
            this.gcl=gcl;
            selectedChats = new MessageList();
        }

        public Bitmap getBitmapFromURL(String src) {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                AnalyticsManager.getInstance().raiseException("ChatHistory",e);
                // Log exception
                e.getStackTrace();
                return null;
            }
        }
        /*Custom View Generation(You may modify this to include other Views) */


        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            final Message Message=gcl.getMessages().get(position);
            LayoutInflater inflater = (LayoutInflater)    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view_row = inflater.inflate(R.layout.chat_list_layout, parent,false);
            view_row.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String id=Message.getId();
                    Intent chatViewIntent=new Intent(ChatHistory.this, ChatHistory.class);
                    chatViewIntent.putExtra("chatId",id);
                    startActivity(chatViewIntent);
                }
            });

            ImageView img_product = (ImageView) view_row.findViewById(R.id.imageView);
            TextView chk_contact = (TextView) view_row.findViewById(R.id.textView);
            String imegId=gcl.getMessages().get(position).getImage();
            if(imegId!=null) {
                String imagePath=AppConstants.CLOUDINARY_URL + imegId + ".jpg";
                Picasso.with(ChatHistory.this).load(imagePath).into(img_product);
                URL url = null;
                Bitmap image=null;
//                Picasso.with(ChatHistory.this).load(imagePath).into(img_product);
//                ImageDownloader imgD= new ImageDownloader();
//                imgD.download(imagePath,img_product);
            }
            else
            {
                chk_contact.setText(gcl.getMessages().get(position).getText());
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
        public boolean alreadySelected(Message t)
        {
            return false;
        }
        @Override
        public int getCount() {

            return gcl.getCount();
        }
        @Override
        public Message getItem(int arg0) {
            return gcl.getMessages().get(arg0);
        }
        @Override
        public long getItemId(int arg0) {
            return  arg0;
        }
    }


    class LoadImagesFromSDCard  extends AsyncTask<String, Void, Void> {

        private ProgressDialog Dialog = new ProgressDialog(ChatHistory.this);

        Bitmap mBitmap;

        protected void onPreExecute() {
            /****** NOTE: You can call UI Element here. *****/

            // Progress Dialog
            Dialog.setMessage(" Loading image from Sdcard..");
            Dialog.show();
        }


        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {

            Bitmap bitmap = null;
            Bitmap newBitmap = null;
            Uri uri = null;


            try {

                /**  Uri.withAppendedPath Method Description
                 * Parameters
                 *    baseUri  Uri to append path segment to
                 *    pathSegment  encoded path segment to append
                 * Returns
                 *    a new Uri based on baseUri with the given segment appended to the path
                 */

                uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + urls[0]);

                /**************  Decode an input stream into a bitmap. *********/
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

                if (bitmap != null) {

                    /********* Creates a new bitmap, scaled from an existing bitmap. ***********/

                    newBitmap = Bitmap.createScaledBitmap(bitmap, 170, 170, true);

                    bitmap.recycle();

                    if (newBitmap != null) {

                        mBitmap = newBitmap;
                    }
                }
            } catch (IOException e) {
                AnalyticsManager.getInstance().raiseException("ChatHistory",e);
                // Error fetching image, try to recover

                /********* Cancel execution of this task. **********/
                cancel(true);
            }

            return null;
        }


        protected void onPostExecute(Void unused) {

            // NOTE: You can call UI Element here.

            // Close progress dialog
            Dialog.dismiss();

            if(mBitmap != null)
            {
                // Set Image to ImageView

                showImg.setImageBitmap(mBitmap);
            }

        }

    }

}
