package com.example.jinanuniversity.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.jinanuniversity.R;

/**
 * Created by Administrator on 2015/12/14.
 */
public class VersionDetail extends Activity {

    Button buttonBack;
    TextView textView;
    String version_text_1 ;

    @Override
    public void onCreate(Bundle savedInstanceStated){
        super.onCreate(savedInstanceStated);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.version_detail);
        initView();

    }
    private void initView(){
        buttonBack=(Button)findViewById(R.id.version_detail_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        textView=(TextView)findViewById(R.id.version_detail);
        version_text_1="<h1><font color=\"#0000CD\">软件更改及修复</font></h1>" +
                "<font color=\"#030303\">1.软件名称更改为“校园网维护”，将“维护单列表”界面作为进入软件的首页，" +
                "服务器设置页面中新增显示接口信息的功能。<br>" +
                "2.删除了上一版本的信息模块、图片库功能，去除维护单明细界面下并无作用的打印按钮。<br>" +
                "3.解决了上一版本“历史维护查询”功能中由于选择次序问题而不能根据用户名查询维修单的bug。<br>" +
                "4.解决了上一版本“用户查询”功能中要先按用户编号查询后才能使用姓名等其他方式查询的bug。<br>" +
                "5.解决“首次点击‘维护’界面时，维护单重复”的BUG。<br>" +
                "6.解决“ Sam3在线状态未显示”BUG。<br>" +
                "7.修复上一版本无法更改用户Sam3模板的bug。<br>" +
                "8.优化了部分代码。<br></font>" +
                "<h1><font color=\"#0000CD\">软件新增：</font></h1>" +
                "<font color=\"#030303\">1.在程序主界面按返回键或者直接返回桌面时" +
                "，不退出软件，在“其他”界面新增退出按钮。<br>" +
                "2.新增判断Session失效功能，当Session失效时能自动获取新的Session，" +
                "以保证用户在一段时间没有打开软件时，" +
                "获取新的订单无需再重新登录验证。<br>" +
                "3.在手机不联网的情况下，维护单页面会提示用户手机尚未联网，" +
                "并允许用户点击后跳转到网络设置的相关页面。<br>" +
                "4.新增修改用户密码接口，当重置用户密码时，" +
                "App端自动为用户生成并显示六位复杂密码，以替代上一版本中的默认密码。<br>" +
                "5.维护单中新增南校区机动组的订单，且能够看到商业模板用户的单<br>" +
                "6.新增部分本部以及南校区的楼栋信息。</font>" ;
        Spanned versionDetail= Html.fromHtml(version_text_1);
        textView.setText(versionDetail);

    }
}
