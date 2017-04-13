package com.gmail.dleemcewen.tandemfieri.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import java.util.ArrayList;


public class MonthlyReportAdapter extends ArrayAdapter<String> {
    public MonthlyReportAdapter(Context context, int textViewResourceID, ArrayList<String> values) {
        super(context, textViewResourceID, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        CheckedTextView tv = (CheckedTextView) view.findViewById(android.R.id.text1);
        tv.setTextColor(Color.BLACK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setCheckedTextVIewTintList(tv);
        }

        return view;
    }

    @TargetApi(21)
    private void setCheckedTextVIewTintList(CheckedTextView tv) {
        int[][] states = new int[][] {new int[] {}};
        int[]  colors = new int[] {Color.BLACK};
        ColorStateList csl = new ColorStateList(states, colors);

        tv.setCheckMarkTintList(csl);
    }
}