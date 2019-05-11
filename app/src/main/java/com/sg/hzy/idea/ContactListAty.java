package com.sg.hzy.idea;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.sg.hzy.idea.DataClass.MUser;
import com.sg.hzy.idea.Model.BaseModel;
import com.sg.hzy.idea.Model.GPModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 胡泽宇 on 2018/11/15.
 */

public class ContactListAty extends FragmentActivity {
    private HashMap<String, EaseUser> map;

    private EaseContactListFragment easeContactListFragment;
    String TAG = "ContactListAty";

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            ContactListAty.this.getSupportFragmentManager().beginTransaction().add(com.sg.hzy.idea.R.id.aty_contactlist_fl, easeContactListFragment).commit();


            return true;
        }
    });


    private void initView() {
        findViewById(com.sg.hzy.idea.R.id.aty_contactlist_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactListAty.this.finish();
            }
        });
        findViewById(R.id.aty_contactlist_find).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查找用户
                FindStr();
            }
        });
        map = new HashMap<>();
        easeContactListFragment = new EaseContactListFragment();
        initmap();
//设置item点击事件
        easeContactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {
                Log.i(TAG, "onListItemClicked: " + user.getUsername());
                Intent intent = new Intent(ContactListAty.this, ChatViewActivity.class);
                Bundle args = new Bundle();
                args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                args.putString(EaseConstant.EXTRA_USER_ID, user.getUsername());
                intent.putExtras(args);
                startActivity(intent);
            }
        });
        //
    }

    /**
     * 从服务器获取好友列表，装填map
     */
    private void initmap() {
        //TODO
        GPModel.getInstance().FindFrineds(new BaseModel.GetFriendsLinstener() {
            @Override
            public void fail(String error) {
                Log.i("Contactlist", "失败" + error);
            }

            @Override
            public void over() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }

            @Override
            public void success(List<MUser> userList, List<String> id) {
                Log.i("Contactlist", "success: " + userList.size());
                EaseUser user = new EaseUser(userList.get(0).getUsername());
                user.setAvatar(userList.get(0).getHeadPortrait() != null ? userList.get(0).getHeadPortrait().getFileUrl() : "http://bmob-cdn-22226.b0.upaiyun.com/2018/11/13/f41d21e240cf22b380254e21d0c1f9f2.png");
                map.put(id.get(0), user);
                
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);

            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.sg.hzy.idea.R.layout.aty_contactlist);
        initView();
    }

    //通过昵称查询
    boolean n = true;
    EditText etfind;

    private void FindStr() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("查找用户");
        final View view = View.inflate(this, R.layout.contact_list_finduser, null);
        builder.setView(view);
        ImageButton find = view.findViewById(R.id.contact_list_aty_btn_find);
        etfind = view.findViewById(R.id.contact_list_aty_findet);
        RadioGroup rg = view.findViewById(R.id.contact_list_aty_rg);
        final RadioButton tg_nich = view.findViewById(R.id.contact_list_aty_findbynickname);
        final Dialog dialog = builder.create();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.alpha = 1f;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == tg_nich.getId()) {
                    n = true;
                    etfind.setHint("昵称");
                } else {
                    etfind.setHint("账户");
                    n = false;
                }
            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (n) {
                    //通过昵称查询
                    GPModel.getInstance().FindUserByNickName(etfind.getText().toString(), new BaseModel.GetUsersLinstener() {
                        @Override
                        public void fail(String error) {
                            Toast.makeText(ContactListAty.this, "查找失败" + error, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void success(List<MUser> userList) {
                            if (userList != null && userList.size() != 0) {
                                dialog.dismiss();
                                GPModel.getInstance().setmUser(userList.get(0));
                                Intent intent = new Intent(ContactListAty.this, FoorPrintAty.class);
                                ContactListAty.this.startActivity(intent);
                            } else {
                                Toast.makeText(ContactListAty.this, "该用户不存在", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                } else {
                    //通过账户查询
                    GPModel.getInstance().FindUserByAccount(etfind.getText().toString(), new BaseModel.GetUsersLinstener() {
                        @Override
                        public void fail(String error) {
                            Toast.makeText(ContactListAty.this, "查找失败" + error, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void success(List<MUser> userList) {
                            if (userList != null && userList.size() != 0) {
                                dialog.dismiss();
                                GPModel.getInstance().setmUser(userList.get(0));
                                Intent intent = new Intent(ContactListAty.this, FoorPrintAty.class);
                                ContactListAty.this.startActivity(intent);
                            } else {
                                Toast.makeText(ContactListAty.this, "该用户不存在", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

    }

}
