package com.sg.hzy.idea.View.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sg.hzy.idea.DataClass.MUser;
import com.sg.hzy.idea.Model.BaseModel;
import com.sg.hzy.idea.Model.GPModel;
import com.sg.hzy.idea.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 胡泽宇 on 2018/11/15.
 */

public class SetMessageAty extends Activity {


    private ImageView iv;
    private EditText et_NickName;
    private EditText et_says;
    private EditText et_email;
    private RadioGroup rg;
    private RadioButton rbtn_b;
    private RadioButton rbtn_g;
    private Button btn_resbir;
    private TextView tv_bir;
    private Button btn_submit;
    private String birthday;

    private Boolean sex = true;

    private Uri imageuri;
    private String imagepath;
    private BmobFile bmobFile;
    final MUser user= GPModel.getCurrentUser(MUser.class);

    Calendar cal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_setmessage);

        initobject();
    }

    private void setMessage() {
        Glide.with(this)
                .load(user.getHeadPortrait()==null?"http://bmob-cdn-22226.b0.upaiyun.com/2018/11/13/f41d21e240cf22b380254e21d0c1f9f2.png":user.getHeadPortrait().getFileUrl())
                .into(iv);
        et_NickName.setText(user.getNickName());
        et_says.setText(user.getSign());
        et_email.setText(user.getEmail());
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            tv_bir.setText(user.getBirthday()==null?"":simpleDateFormat.format(simpleDateFormat.parse(user.getBirthday().getDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(user.getGender()!=null) {
            if (user.getGender()) {
                rbtn_g.setChecked(false);
                rbtn_b.setChecked(true);
            } else {
                rbtn_g.setChecked(true);
                rbtn_b.setChecked(false);
            }
        }else{
            rbtn_g.setChecked(false);
            rbtn_b.setChecked(true);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            imageuri = data.getData();
//文件指针
            Cursor cursor = this.getContentResolver().query(imageuri, null, null,
                    null, null);
            cursor.moveToFirst();
            imagepath = cursor.getString(1);
        }
        if (imageuri != null && imagepath != null) {
            iv.setImageURI(imageuri);
        }
    }

    private void AddMessage() {

        if(tv_bir.getText().toString().equals("")){
            Toast.makeText(SetMessageAty.this,"出生日不能为空!",Toast.LENGTH_SHORT).show();
        }else {

            user.setBirthday(BmobDate.createBmobDate("yyyy-MM-dd", tv_bir.getText().toString()));

            if (et_NickName.getText().toString().equals("")) {
                user.setNickName("未命名用户");
            } else {
                user.setNickName(et_NickName.getText().toString());
            } if (et_says.getText().toString().equals("")) {
                user.setSign(" ");
            } else {
                user.setSign(et_says.getText().toString());
            }if(et_email.getText().toString().equals("")||!GPModel.isEmail(et_email.getText().toString())) {
                Toast.makeText(SetMessageAty.this,"邮箱填写错误!",Toast.LENGTH_SHORT).show();

            }else{
                user.setGender(sex);
                user.setEmail(et_email.getText().toString());
                if (imagepath != null && imageuri != null||user.getHeadPortrait()!=null) {
                    GPModel.getInstance().SaveUserMessage(user, imagepath, new BaseModel.DoneListener() {
                        @Override
                        public void fail(String error) {
                            Toast.makeText(SetMessageAty.this,"修改信息失败"+error,Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void process() {

                        }

                        @Override
                        public void success() {
                            Toast.makeText(SetMessageAty.this,"修改信息成功",Toast.LENGTH_SHORT).show();
                            SetMessageAty.this.finish();
                        }
                    });
                } else {
                    Toast.makeText(SetMessageAty.this,"头像未设置！",Toast.LENGTH_SHORT).show();
                }
            }





            Log.i("生日", " " + user.getBirthday().getDate());
            Log.i("性别", " " + user.getGender());
            Log.i("格言", " " + user.getSign());
            Log.i("昵称", " " + user.getNickName());


        }

    }

    private void initobject() {
        iv = findViewById(R.id.SetMessage_aty_ivhead);
        et_NickName = findViewById(R.id.SetMessage_aty_et_nickname);
        tv_bir = findViewById(R.id.SetMessage_aty_tv_birthday);
        btn_resbir = findViewById(R.id.SetMessage_aty_btn_resetbir);
        et_says = findViewById(R.id.SetMessage_aty_et_sign);
        btn_submit = findViewById(R.id.SetMessage_aty_btn_submit);
        rg = findViewById(R.id.SetMessage_aty_rg);
        rbtn_g = findViewById(R.id.SetMessage_aty_rg_2);
        rbtn_b = findViewById(R.id.SetMessage_aty_rg_1);
        et_email=findViewById(R.id.SetMessage_aty_et_email);

        setMessage();

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
        btn_resbir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出日期选择器并转化日期
                ShowSelectDialog();

            }

        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMessage();
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == rbtn_g.getId()) {
                    sex = false;
                } else {
                    sex = true;
                }
            }
        });


    }

    private void ShowSelectDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this,R.layout.setmessage_aty_datedialog, null);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.SetMessage_aty_datedialog_dp);
        builder.setView(view);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                int year = datePicker.getYear();
                int monthTemp = datePicker.getMonth() + 1;
                int day = datePicker.getDayOfMonth();

                cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthTemp);
                cal.set(Calendar.DAY_OF_MONTH, day);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                tv_bir.setText(simpleDateFormat.format(cal.getTime()));
                dialog.cancel();
            }
        });
        Dialog dialog = builder.create();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.alpha = 1f;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }


}
