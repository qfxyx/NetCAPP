package com.example.jinanuniversity.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by Administrator on 2015/10/23.
 */
public class TestActivity extends Activity {

    private String TAG=".activity.testActivity";
    String timestamp;
    String returnMsg = "";
    String signTest;
    private TextView textView_message;
    private TextView textView_return;
    private Button button;
    private Button Message_button;
    EditText account;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        initView();

    }

    private void initView (){
        account=(EditText)findViewById(R.id.account);
        password=(EditText)findViewById(R.id.password);
        textView_message=(TextView)findViewById(R.id.useful_message_textview);
        textView_message.setText(" The test message will be display here");
        textView_return=(TextView)findViewById(R.id.return_message_textview);
        textView_return.setText("the return message for test will be display here");
        button =(Button)findViewById(R.id.test_buttton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              logInTest();
                //postTest2();
            }
        });
        Message_button=(Button)findViewById(R.id.test_message_buttton);
        Message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s =ParamsManager.getMd5sign("a");
                //textView_message.setText(signTest);
                textView_message.setText(timestamp);
                textView_return.setText(returnMsg);
            }
        });


    }
    private void logInTest (){
        new Thread(new Runnable() {
            @Override
            public void run() {
                timestamp = ParamsManager.getTime();
                String sign = "";
                String password_send = ParamsManager.enCode(ParamsManager.
                        getMd5sign("cx051451"));
                sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY + timestamp +
                        Config.VER + "2012051451" + password_send);
                signTest = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY +
                        Config.VER + "2012051451" + password_send);
                IEasyHttpApiV1 iEasyHttpApiV1 = new IEasyHttpApiV1();
                IEasy iEasy = new IEasy(iEasyHttpApiV1);
                returnMsg=iEasy.invitationCodeLogin(Config.APPKEY, timestamp, sign, Config.VER,
                        "2012051451",password_send);

            }
        }).start();

    }

}
