package com.codepath.listly.view;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by paulina on 1/10/15.
 */
public class CustomTaskAdapter extends SimpleCursorAdapter {

    public CustomTaskAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

    public boolean setViewValue(View view, Cursor cursor, int column) {

        String index = cursor.getString(column);
        String textToDisplay;

        if (index.equals("0")) {
            textToDisplay = "HIGH";
        } else if (index.equals("1")) {
            textToDisplay = "MEDIUM";
        } else {
            textToDisplay = "LOW";
        }

        TextView tv = (TextView) view;
        tv.setText(textToDisplay);
        return true;
    }

}
