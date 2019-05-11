package com.sg.hzy.idea;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sg.hzy.idea.Model.BaseModel;
import com.sg.hzy.idea.Model.GPModel;

public class SignupActivity extends AppCompatActivity {
    EditText sign_account;
    EditText sign_email;
    EditText sign_passworld;
    Button btn_signup;
    TextView to_login;
    RadioGroup radioGroup;
    Boolean sex=true;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_signup);
        sign_account=findViewById(R.id.aty_signup_input_name);
        sign_email=findViewById(R.id.aty_signup_input_email);
        sign_passworld=findViewById(R.id.aty_signup_input_password);
        btn_signup=findViewById(R.id.aty_signup_btn_signup);
        to_login=findViewById(R.id.aty_signup_link_login);
        radioGroup=findViewById(R.id.aty_signup_rg);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupActivity.this.finish();
            }
        });
    }
    public void signup(){
        String account=sign_account.getText().toString();
        String passwords=sign_passworld.getText().toString();
        String email= (GPModel.isEmail(sign_email.getText().toString()))?sign_email.getText().toString():null;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.aty_signup_rg_btn_male:
                        //男
                        sex=true;
                        break;
                    case R.id.aty_signup_rg_btn_famale:
                        sex=false;
                        //女
                        break;
                }
            }
        });
        if(account!=null||passwords!=null){
                if(email==null||!GPModel.isEmail(email)){
                    Toast.makeText(this,"请填写正确的邮箱！",Toast.LENGTH_SHORT).show();
                }else{
                    GPModel.getInstance().UserRegistered(account, passwords, email, sex, new BaseModel.DoneListener() {
                        @Override
                        public void fail(String error) {
                            Toast.makeText(SignupActivity.this,"注册失败！"+error,Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void process() {

                        }

                        @Override
                        public void success() {
                            Toast.makeText(SignupActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                            SignupActivity.this.finish();
                        }
                    });



                }
        }else{
            Toast.makeText(this,"请填写必选项",Toast.LENGTH_SHORT).show();
        }
    }
}
