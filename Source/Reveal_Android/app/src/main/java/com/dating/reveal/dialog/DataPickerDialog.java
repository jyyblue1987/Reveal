package com.dating.reveal.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dating.reveal.R;
import com.dating.reveal.utility.DataUtils;
import com.dating.reveal.wheelview.OnWheelChangedListener;
import com.dating.reveal.wheelview.WheelView;
import com.dating.reveal.wheelview.adpater.ArrayWheelAdapter;
import com.dating.reveal.wheelview.adpater.NumericWheelAdapter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by JonIC on 2016-11-26.
 */
public class DataPickerDialog extends Dialog {

    int retvalue;

    Button mdone;
    Button mcancel;
    WheelView month;
    WheelView year ;
    WheelView day;
    Calendar calendar;

    private OnDismissListener _listener ;
    private String __facebookid;

    public DataPickerDialog(Context context, String facebookid) {
        super(context);
        __facebookid = facebookid;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate( Bundle $savedInstanceState ) {
        super.onCreate( $savedInstanceState ) ;
        setContentView( R.layout.datapicker_dialog ) ;
//
        mdone = (Button) findViewById(R.id.txt_done);
        mcancel = (Button) findViewById(R.id.txt_cancel);
        calendar = Calendar.getInstance();

        month = (WheelView) findViewById(R.id.month);
        year = (WheelView) findViewById(R.id.year);
        day = (WheelView) findViewById(R.id.day);


        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(year, month, day);
            }
        };


        // month
        int curMonth = calendar.get(Calendar.MONTH);
        String months[] = new String[] {"January", "February", "March", "April", "May",
                "June", "July", "August", "September", "October", "November", "December"};
        month.setViewAdapter(new DateArrayAdapter(getContext(), months, curMonth));
        month.setCyclic(true);
        month.setCurrentItem(curMonth);
        month.addChangingListener(listener);

        // year
        int curYear = calendar.get(Calendar.YEAR);
        year.setViewAdapter(new DateNumericAdapter(getContext(), 0, 10000, 0));
        year.setCyclic(false);
        year.setCurrentItem(curYear);
        year.addChangingListener(listener);

        //day
        updateDays(year, month, day);
        day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        day.setCyclic(true);

        //////
        mcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if( _listener == null ) {} else {
                    retvalue = -1;
                    _listener.onDismiss( DataPickerDialog.this ) ;
                }
                retvalue = -1;
                dismiss() ;
            }
        });

        ///////
        mdone.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int y = getBrithday(year,month,day);
                int nowdate = calendar.get(Calendar.YEAR);

                if( _listener == null ) {} else {
                    retvalue = nowdate - y;
                    _listener.onDismiss( DataPickerDialog.this ) ;
                }
                retvalue = nowdate - y;
                dismiss() ;
            }
        }) ;
    }

    public void setOnDismissListener( OnDismissListener $listener ) {
        _listener = $listener ;
    }

    public int getAge() {
        return retvalue ;
    }


    ////////////////////////////////////////////////////

    /**
     * Updates day wheel. Sets max days according to selected month and year
     */
    void updateDays(WheelView year, WheelView month, WheelView day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(getContext(), 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }
    protected int getBrithday(WheelView year, WheelView month, WheelView day){
        int yy = year.getCurrentItem();
        int mm = month.getCurrentItem();
        int dd = day.getCurrentItem();
        Date date = new Date(yy,mm,dd);
        String strdate = date.toString();
        DataUtils.savePreference("date", strdate);
        return yy;
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(20);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
//                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(20);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
//                view.setTextColor(0xFF0000F0);//FF0000F0
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    // datapicker related functions.

    ////////////////////////////////////////////////////
}