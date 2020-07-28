package com.dressapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.dressapplication.Interface.GetContact;
import com.dressapplication.R;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> storeContacts;
    ArrayList<String> phoneno;
    LayoutInflater inflter;
    GetContact get;



    public CustomAdapter(Context context, ArrayList<String> storeContacts, ArrayList<String> phoneno, GetContact get) {
        inflter = (LayoutInflater.from(context));
        this.context = context;
        this.phoneno=phoneno;
        this.storeContacts=storeContacts;
        this.get=get;
    }

    @Override
    public int getCount() {
        return storeContacts.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.contact_items_listview, null);
        TextView country = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_no = (TextView) view.findViewById(R.id.tv_no);
        Button bt_invite=(Button) view.findViewById(R.id.bt_invite);
        country.setText(storeContacts.get(i));
        tv_no.setText(phoneno.get(i));

        bt_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get.getcontactData(phoneno.get(i),storeContacts.get(i));
            }
        });

        return view;
    }


}