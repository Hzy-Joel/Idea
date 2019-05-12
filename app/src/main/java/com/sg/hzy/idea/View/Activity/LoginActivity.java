package com.sg.hzy.idea.View.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sg.hzy.idea.Model.BaseModel;
import com.sg.hzy.idea.Model.GPModel;
import com.sg.hzy.idea.R;
import com.sg.hzy.idea.Utils.PermisionUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {
    EditText account;
    EditText passworld;
    Button btn_login;
    TextView to_signup;
    ProgressBar pb;
    CheckBox cbautologin;
    View.OnClickListener cl;
    boolean setM=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取权限
        PermisionUtils.verifyStoragePermissions(this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//设置全屏但不隐藏状态栏
            decorView.setSystemUiVisibility(option);
        }
        setContentView(R.layout.aty_login);
        setM=false;
        //初始化控件
        cbautologin = findViewById(R.id.login_aty_cbautologin);
        account = findViewById(R.id.login_aty_account);
        passworld = findViewById(R.id.login_aty_passwords);
        btn_login = findViewById(R.id.login_aty_login);
        to_signup = findViewById(R.id.login_aty_to_sign_up);
        pb = findViewById(R.id.login_aty_pb);
        cl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        };
        btn_login.setOnClickListener(cl);
        to_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSignUp();
            }
        });

        setLoginInfo();
    }

    private void toSignUp() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    //登陆
    public void login() {
        //关闭软键盘
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(passworld.getWindowToken(),0);
        imm.hideSoftInputFromWindow(account.getWindowToken(),0);
        btn_login.setClickable(false);
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("登陆中");
        pDialog.setCancelable(false);
        pDialog.show();
        pb.setVisibility(View.VISIBLE);
        final String account = this.account.getText().toString().trim();
        final String passwords = this.passworld.getText().toString().trim();
        if(account!=""&&passwords!=""){
            GPModel.getInstance().Login(account, passwords, new BaseModel.DoneListener() {
                @Override
                public void fail(String error) {
                    Toast.makeText(LoginActivity.this, "登陆失败:" + error, Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.INVISIBLE);
                    btn_login.setClickable(true);
                    pDialog.dismiss();
                }

                @Override
                public void process() {

                }

                @Override
                public void success() {
                    pb.setVisibility(View.INVISIBLE);
                    //保存登陆信息
                    if (cbautologin.isChecked()) {
                        saveLoginInfo(LoginActivity.this, account, passwords);
                    }
                    pDialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, MainAty.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }
            });
        }else{
            pb.setVisibility(View.INVISIBLE);
            btn_login.setClickable(true);
            if(setM){
                Toast.makeText(LoginActivity.this, "账户和密码不能为空！", Toast.LENGTH_SHORT).show();

            }
        }

    }

    //保存账户信息置本地
    public void saveLoginInfo(Context context, String account, String passwords) {
        //获取SharedPreferences对象
        SharedPreferences sharedPre = context.getSharedPreferences("LoginMessage", MODE_PRIVATE);
        //获取Editor对象

        SharedPreferences.Editor editor = sharedPre.edit();
        //设置参数
        editor.putString("account", account);
        editor.putString("passwords", passwords);
        //提交
        editor.apply();
        Log.i("login", "saveLoginInfo: 保存信息" + account + " " + passwords);
    }

    //设置账户和密码
    private void setLoginInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginMessage", MODE_PRIVATE);
        if (sharedPreferences != null) {
            account.setText(sharedPreferences.getString("account", ""));
            passworld.setText(sharedPreferences.getString("passwords", ""));
            cbautologin.setChecked(true);
            setM=true;
            cl.onClick(null);
        }
        Log.i("login", "setLoginInfo: 设置信息" + account.getText() + " " + passworld.getText());
    }



}
