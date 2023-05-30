package com.example.internet.adapter.style;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SpinnerTextAdapter extends ArrayAdapter<String> {

    private Context context;
    private int textSize;

    public SpinnerTextAdapter(Context context, List<String> items, int textSize) {
        super(context, android.R.layout.simple_spinner_item, items);
        this.context = context;
        this.textSize = textSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        return view;
    }
}
