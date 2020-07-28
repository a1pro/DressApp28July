package com.dressapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dressapplication.Interface.GetContact;
import com.dressapplication.R;
import com.dressapplication.adapters.CustomAdapter;

import java.util.ArrayList;

public class InviteFriendsActivity extends AppCompatActivity {
    ListView listview ;
    ArrayList<String> StoreContacts ;
    ArrayAdapter<String> arrayAdapter,arrayAdapter1 ;

    ArrayList<String> phoneno;
    Cursor cursor ;
    String name, phonenumber ;
    public  static final int RequestPermissionCode  = 1 ;
    private ImageView iv_back;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        iv_back=findViewById(R.id.iv_back);

        listview = (ListView)findViewById(R.id.listview);

        StoreContacts = new ArrayList<String>();
        phoneno=new ArrayList<>();

        EnableRuntimePermission();

        GetContactsIntoArrayList();


        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), StoreContacts, phoneno,get);
        listview.setAdapter(customAdapter);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                InviteFriendsActivity.this,
                Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(InviteFriendsActivity.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(InviteFriendsActivity.this,new String[]{
                    Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {
            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(InviteFriendsActivity.this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(InviteFriendsActivity.this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    public void GetContactsIntoArrayList(){

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);

        while (cursor.moveToNext()) {

            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            StoreContacts.add(name + " "  + ":" + " " );
            phoneno.add(phonenumber);
        }

        cursor.close();

    }

    GetContact get=new GetContact() {
        @Override
        public void getcontactData(String phonr, String msg) {
            if (phonr!=null){
                sendSms(phonr);
            }
        }
    };


    private void sendSms(String phoneno){
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , new  String (phoneno));
        smsIntent.putExtra("sms_body"  , "DressApp ");

        try {
            startActivity(smsIntent);
                finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(InviteFriendsActivity.this,
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }
}
