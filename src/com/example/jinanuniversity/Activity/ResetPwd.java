package com.example.jinanuniversity.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.Service;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2015/12/7.
 */
public class ResetPwd extends Activity {

    Button button_back;
    Button button_generate;
    Button button_reset;
    TextView textView_pwd;

    private String account;
    private String userId;


    String pwd = "";
    ProgressDialog progressDialog;

    private final String TAG = ".activity.resetpwd";
    private final int SET_PWD = 1;
    private final int RERSET_RESULT = 2;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case SET_PWD:
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    textView_pwd.setText(pwd);
                    break;
                case RERSET_RESULT:
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        String relust = (String)message.obj;
                        if (!relust.isEmpty()&&relust!=null){
                            try {
                                JSONObject jsonObject = new JSONObject(relust);
                                int code = jsonObject.getInt("code");
                                String info = jsonObject.getString("info");
                                Log.i(TAG, "return info = "+info);
                                if(code==0){
                                    Toast.makeText(ResetPwd.this,"修改密码成功！",Toast.LENGTH_LONG).show();
                                }else if (code==1){
                                    Toast.makeText(ResetPwd.this,"修改失败",Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                Toast.makeText(ResetPwd.this,"修改失败,程序发生未知错误",Toast.LENGTH_SHORT).show();
                        }


                        }else {
                            Toast.makeText(ResetPwd.this,"修改失败",Toast.LENGTH_SHORT).show();
                        }

                    }

            }
        }
    };

    @Override
    public void onCreate(Bundle savedInsatnceState) {
        super.onCreate(savedInsatnceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reset_password);
        initView();
        getPwd(10);
        Intent intent = getIntent();
        account = intent.getStringExtra("account");
        userId = intent.getStringExtra("userId");
        Log.i(TAG, "activity is starting getting data,account = " + account + ", userId = " + userId);
    }

    private void initView() {
        //设置窗口大小
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = (int) (display.getHeight() * 0.4);
        params.width = (int) (display.getWidth() * 0.9);
        getWindow().setAttributes(params);

        textView_pwd = (TextView) findViewById(R.id.reset_pwd_text);
        button_back = (Button) findViewById(R.id.reset_pwd_back);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        button_generate = (Button) findViewById(R.id.reset_pwd_generate);
        button_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProgressDialog("生成密码", "正在生成随机密码中...", false);
                getPwd(500);

            }
        });

        button_reset = (Button) findViewById(R.id.reset_pwd_reset);
        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // resetPwdTest();
                Spanned title = Html.fromHtml("<font color=\"#0000EE\">" +
                        "&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp" +
                        " 警告！</font>");

                Spanned message = Html.fromHtml("<font color=\"#242424\">用户密码即将被重置为: " +
                        "</font><br><font color=\"#EE3B3B\"><big><big>" + pwd
                        + "</big></big></font><br><font color=\"#242424\">" +
                        "请确认已经将该密码告知用户!</font>");
                showDialog(title, message, "取消", "确定");

            }
        });

    }

    private void getPwd(final int sleepTime) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                String string = "";
                for (int i = 0; i < 6; i++) {
                    String NumOrChar = random.nextInt(2) % 2 == 0 ? "char" : "num";
                    if ("char".equalsIgnoreCase(NumOrChar)) {
                        int charType = random.nextInt(2) % 2 == 0 ? 65 : 97;
                        string += (char) (random.nextInt(26) + charType);
                    } else if ("num".equalsIgnoreCase(NumOrChar)) {
                        string += String.valueOf(random.nextInt(10));
                    }
                }
                pwd = string;
                try {
                    Thread.sleep(sleepTime);
                    Message message = new Message();
                    message.what = SET_PWD;
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }

    private void createProgressDialog(String title, String message, boolean canCancel) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(canCancel);
        progressDialog.show();

    }

    private void resetPwd() {
        final Config config = new Config();
        String url = config.PWDRESET;
        Log.i(TAG, "reset start and get the url = " + url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String timestamp = ParamsManager.getTime();
                String password = ParamsManager.enCode(pwd);
                String sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY + timestamp
                        + Config.VER + account + userId + password);
                IEasy ieasy = new IEasy(new IEasyHttpApiV1());
                String string = ieasy.resetPwd(new Config().PWDRESET, Config.APPKEY, sign, timestamp,
                        Config.VER, account, userId, password);
                Log.i(TAG, "reset return message is  " + string);
                Message message = new Message();
                message.obj = string;
                message.what = RERSET_RESULT;
                handler.sendMessage(message);
            }
        }).start();
    }

    private void resetPwdTest() {
        final Config config = new Config();
      //  final String url= config.PWDRESET;
        //final String url = config.USERDETAIL;
        final String url="http://netcapi1.jnu.edu.cn/netcapi/netcUser/pwdResetNew.do";
        //String url="http://202.116.9.18:8080/netcapi//netcUser/detail.do";
        Log.i(TAG, "reset start and get the url = " + url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String timestamp = ParamsManager.getTime();
                String password = ParamsManager.enCode(pwd);
                String sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY + timestamp
                        + Config.VER + account + userId+password);
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(url);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("appkey", Config.APPKEY));
                    params.add(new BasicNameValuePair("sign", sign));
                    params.add(new BasicNameValuePair("timestamp", timestamp));
                    params.add(new BasicNameValuePair("ver", Config.VER));
                    params.add(new BasicNameValuePair("account", account));
                    params.add(new BasicNameValuePair("newPwd", password));
                    params.add(new BasicNameValuePair("userId", userId));
                    httpPost.setHeader("Cookie", "JSESSIONID=" + Service.getInstance().getJSESSIONID());
                    httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpPost.setHeader("User-Agent", "netcapi1.jnu.edu.cn");
                    HttpResponse httpResponse1 = httpClient.execute(httpPost);
                    HttpEntity httpEntityPost = httpResponse1.getEntity();
                    String string = EntityUtils.toString(httpEntityPost, HTTP.UTF_8);
                    Log.i(TAG, "reset return message is  " + string);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                Message message = new Message();
                //message.obj=string;
                message.what = RERSET_RESULT;
                handler.sendMessage(message);
            }
        }).start();
    }

    private void showDialog(Spanned title, Spanned mesage, String positiveWords,
                            String negativeWords) {

        AlertDialog alertDialog = new myAlertDialog.Builder(this, R.style.myAlertDialogStyle)
                .setTitle(title).setMessage(mesage)
                .setPositiveButton(positiveWords, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //do nothing

                    }
                }).setNegativeButton(negativeWords, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //resetPwdTest();
                        resetPwd();
                    }
                }).create();
        alertDialog.show();


    }

    class myAlertDialog extends AlertDialog {
        protected myAlertDialog(Context context, int theme) {
            super(context, theme);
        }
    }
}
