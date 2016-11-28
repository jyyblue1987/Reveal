package com.dating.reveal.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dating.reveal.R;

/**
 * Created by JonIC on 2016-11-18.
 */
public class frienddialog extends Dialog {

    String retvalue;

    private Button post;
    private Button delete;
    private Button cancel;
    private EditText _nameField ;
    private OnDismissListener _listener ;
    private String __facebookid;

    public frienddialog(Context context, String facebookid) {
        super(context);
        __facebookid = facebookid;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate( Bundle $savedInstanceState ) {
        super.onCreate( $savedInstanceState ) ;
        setContentView( R.layout.friend_dialog ) ;
//        setTitle( "Enter Your Name" ) ;
        post = (Button)findViewById(R.id.btn_post_notification);
        delete = (Button)findViewById(R.id.btn_delete_friend);
        cancel = (Button)findViewById(R.id.btn_cancel);
        //////
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if( _listener == null ) {} else {
                    retvalue = "post";
                    _listener.onDismiss( frienddialog.this ) ;
                }
                retvalue = "post";
//                Toast.makeText(getContext(), "Post Notification", Toast.LENGTH_SHORT).show();
                dismiss() ;
            }
        });

        ///////
        delete.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if( _listener == null ) {} else {
                    retvalue = "delete";
                    _listener.onDismiss( frienddialog.this ) ;
                }
                retvalue = "delete";
//                Toast.makeText(getContext(), "Delete Friend", Toast.LENGTH_SHORT).show();
                dismiss() ;
            }
        }) ;
        cancel.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if( _listener == null ) {} else {
                    retvalue = "cancel";
                    _listener.onDismiss( frienddialog.this ) ;
                }
                retvalue = "cancel";
//                Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                dismiss() ;
            }
        }) ;
    }

    public void setOnDismissListener( OnDismissListener $listener ) {
        _listener = $listener ;
    }

    public String getName() {
        return retvalue ;
    }

    public String get__facebookid(){return __facebookid;}
}