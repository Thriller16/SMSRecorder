package com.example.lawrene.smsrecordernew;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

//public class RecordedAdapter extends ArrayAdapter<SMSData> {
//    public RecordedAdapter(@NonNull Context context, @NonNull List<SMSData> objects) {
//        super(context, 0, objects);
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        if(convertView == null){
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
//        }
//
//        SMSData smsData = getItem(position);
//
//        TextView subText = convertView.findViewById(R.id.subdistributor);
//        TextView clientText = convertView.findViewById(R.id.client);
//
//        subText.setText(smsData.getNumber());
//        clientText.setText(smsData.getBody());
//
//        return convertView;
//    }
//}
