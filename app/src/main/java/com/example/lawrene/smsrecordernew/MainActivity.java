package com.example.lawrene.smsrecordernew;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import com.vistrav.ask.Ask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 100;
    private static final int REQUEST_CODE_FOR_SOURCE = 100;
    DatabaseAccess databaseAccess;
    String smsBody, smsSender, finalResult;
    HttpParse httpParse = new HttpParse();
    Boolean state;
    HashMap<String, String> hashmap = new HashMap<>();
    String HttpURL = "http://www.caurix.net/smsRecorder.php";

    List<SMSData> smsDataList = new ArrayList<>();
    List<SMSData> responseList = new ArrayList<>();
    MyAppAdapter recordedAdapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkpermission();

        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();

        smsDataList = databaseAccess.getSMS();
        listView = findViewById(R.id.listItem);

        recordedAdapter = new MyAppAdapter(responseList, this );
        listView.setAdapter(recordedAdapter);

        getRequest();

    }

    private void checkpermission() {
        Ask.on(this)
                .id(PERMISSION_REQUEST) // in case you are invoking multiple time Ask from same activity or fragment
                .forPermissions(Manifest.permission.READ_SMS
                        , Manifest.permission.RECEIVE_SMS)
                .withRationales("In other for the app to work perfectly you have to give it permission") //optional
                .go();
    }

    public void getSMS() {
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        startManagingCursor(c);

        // Read the sms data and store it in the list
        if (c.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {

                smsSender = c.getString(c.getColumnIndexOrThrow("address"));
                smsBody = c.getString(c.getColumnIndexOrThrow("body"));

                if(smsBody.contains("*")){
                    StringTokenizer stringTokenizer = new StringTokenizer(smsBody, "*");
                    stringTokenizer.nextToken();

                    String client = stringTokenizer.nextToken();
                    String firstTwo = client.substring(0, 2);

                    if(firstTwo.equals("77") || firstTwo.equals("78")){
                        databaseAccess.add(smsSender, smsBody, "pending");
                    }
                }
                c.moveToNext();
            }
        }
//        c.close();
    }

    public void getRequest() {
        String url = "http://www.caurix.net/smsRecorderJSON.php";


        final AsyncHttpClient client = new AsyncHttpClient();


        client.get(this, url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONArray arr = new JSONArray(responseString);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject eachObject = arr.getJSONObject(i);

                        String phone_number = eachObject.getString("subdistributor");
                        String message = eachObject.getString("client");

                        responseList.add(new SMSData(phone_number, message, "sent"));
                        recordedAdapter = new MyAppAdapter(responseList, MainActivity.this);
                        listView.setAdapter(recordedAdapter);
//                        if(smsDataList.size()!= arr.length()){
//                            updateRecords(smsDataList.get(arr.length()-1).getNumber(), smsDataList.get(arr.length()-1).getBody());
//                        }
//                        smsDataList = databaseAccess.getSMS();
//
//

//                        SMSData currentSMSonline = new SMSData(phone_number, message);
//                        for(int j = 0; j< smsDataList.size(); j ++){
//                            SMSData currentSMsLocal = smsDataList.get(j);
//
//                            if(currentSMSonline.getBody().equals(currentSMsLocal.getBody())){
//                                Toast.makeText(MainActivity.this, "Contans", Toast.LENGTH_SHORT).show();
//                            }
//
//
//                        }
////                            Toast.makeText(MainActivity.this, "kjihkjhkjhk", Toast.LENGTH_SHORT).show();
//                        }
//                        if(){
//
//                        }
//                        if(arr.length()){
//
//                        }
//                        if (phonenumber.equals(phone_number)) {
//                            if (Integer.parseInt(balance) > Integer.parseInt(amount)) {
//                                Toast.makeText(MainActivity.this, "Records have been Updated", Toast.LENGTH_SHORT).show();
//                                updateRecords(phone_number, bank_id, bank_name, amount, confirmationid);
//
//                                String newBalance = String.valueOf(Integer.parseInt(balance) - Integer.parseInt(amount));
//                                changeBalance(phone_number, newBalance);
//
//
//                                databaseAccess.addTransaction(phone_number, amount,confirmationid, newBalance);
//
//                                transactionList = databaseAccess.getTransactions();
//                                transactionAdapter = new TransactionAdapter(MainActivity.this, transactionList);
//                                transactionListView.setAdapter(transactionAdapter);
//                                transactionAdapter.notifyDataSetChanged();
//
//                            }
//                        }
//                        Toast.makeText(MainActivity.this, "" + phone_number, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(MainActivity.this, "The phone numbers are" + phone_number, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateRecords(String number, final String message) {
        class UpdateRecords extends AsyncTask<String, Void, String> {

            @Override
            protected void onCancelled() {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(MainActivity.this, "" + s.toString(), Toast.LENGTH_SHORT).show();
                Log.i("TAG", "" + s.toString());

            }

            @Override
            protected String doInBackground(String... params) {
                hashmap.put("phoneNumber", params[0]);
                hashmap.put("message", params[1]);

                finalResult = httpParse.postRequest(hashmap, HttpURL);
                return finalResult;
            }
        }


        smsDataList = databaseAccess.getSMS();
        UpdateRecords updateRecords = new UpdateRecords();

        for(int j = 0; j < smsDataList.size(); j++ ){
//                            SMSData smsData = smsDataList.get(j);
//
//                            if(smsData.getStatus().equals("pending")){
////                                Toas
            SMSData smsData = smsDataList.get(j);

            if(smsData.getStatus().equals("pending")){
                updateRecords.execute(smsData.getNumber(), smsData.getBody());
                databaseAccess.updateStatus(smsData.getBody());
                getRequest();
            }
        }

    }

    Handler h = new Handler();
    int delay = 20000; //60 seconds
    Runnable runnable;

    @Override
    protected void onResume() {
        state = true;
        h.postDelayed(new Runnable() {
            public void run() {
                //do something
                getSMS();

                updateRecords("", "");
//                getRequest();

                Log.i("15", "60 seconds reached!");
                runnable = this;

                h.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();


    }

    @Override
    protected void onPause() {
        state = false;
        h.postDelayed(new Runnable() {
            public void run() {
                //do something
//                getRequest();
                getSMS();

                updateRecords("", "");

                Log.i("60", "App paused!");
                runnable = this;

                h.postDelayed(runnable, delay);
            }
        }, delay);
        super.onResume();

        super.onPause();
    }

    public class MyAppAdapter extends BaseAdapter {

        public class ViewHolder {
            TextView  subText, clientText;
        }

        public List<SMSData> parkingList;

        public Context context;
        ArrayList<SMSData> arraylist;

        private MyAppAdapter(List<SMSData> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<SMSData>();
            arraylist.addAll(parkingList);

        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;
            ViewHolder viewHolder;

            if (rowView == null) {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.list_item, null);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.subText = (TextView) rowView.findViewById(R.id.subdistributor);
                viewHolder.clientText = (TextView) rowView.findViewById(R.id.client);
                rowView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.subText.setText(parkingList.get(position).getNumber() + "");
            viewHolder.clientText.setText(parkingList.get(position).getBody() + "");
            return rowView;


        }

        public void filter(String charText) {

            charText = charText.toLowerCase(Locale.getDefault());

            parkingList.clear();
            if (charText.length() == 0) {
                parkingList.addAll(arraylist);

            } else {
                for (SMSData postDetail : arraylist) {
                    if (charText.length() != 0 && postDetail.getNumber().toLowerCase(Locale.getDefault()).contains(charText)) {
                        parkingList.add(postDetail);
                    } else if (charText.length() != 0 && postDetail.getNumber().toLowerCase(Locale.getDefault()).contains(charText)) {
                        parkingList.add(postDetail);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //*** setOnQueryTextFocusChangeListener ***
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                recordedAdapter.filter(searchQuery.toString().trim());
                listView.invalidate();
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

