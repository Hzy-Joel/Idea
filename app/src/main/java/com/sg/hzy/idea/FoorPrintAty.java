package com.sg.hzy.idea;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.platform.comapi.map.D;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.hyphenate.easeui.EaseConstant;
import com.sg.hzy.idea.Adapter.DynamicMessageListRAdapter;
import com.sg.hzy.idea.Adapter.FootPrintAdapter;
import com.sg.hzy.idea.DataClass.DynamicMessage;
import com.sg.hzy.idea.DataClass.MUser;
import com.sg.hzy.idea.Model.BaseModel;
import com.sg.hzy.idea.Model.GPModel;
import com.sg.hzy.idea.UI.CircleImageView;
import com.sg.hzy.idea.UI.SpacesItemDecroation;
import com.sg.hzy.idea.View.Fragment.BorwerFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import jp.wasabeef.glide.transformations.BlurTransformation;

//个人主页
public class FoorPrintAty extends AppCompatActivity {
    public final static String TAG = "FoorPrinyAty";
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    CircleImageView imageView;
    BmobFile picfile;
    Toolbar toolbar;
    String headpicuri = "";
    ImageButton ibtn_back;
    Button ibtn_add;
    Button ibtn_send;
    ImageView ivb;
    TextView tvnick;
    DynamicMessageListRAdapter footPrintAdapter;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.i(TAG, "onCreate: " + headpicuri);
                    headpicuri = msg.obj.toString();
                    Glide.with(FoorPrintAty.this)
                            .load(headpicuri)
                            .error(R.drawable.head_portrait_deafult)
                            .into(imageView);
                    Glide.with(FoorPrintAty.this).load(headpicuri)
                            .bitmapTransform(new BlurTransformation(FoorPrintAty.this, 25), new CenterCrop(FoorPrintAty.this))
                            .into(ivb);

                    return true;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.aty_foorprint);
        ivb = findViewById(R.id.foorprint_aty_image_background);
        imageView = findViewById(R.id.foorprint_aty_cv);
        swipeRefreshLayout = findViewById(R.id.foorprint_aty_sl);
        recyclerView = findViewById(R.id.foorprint_aty_rl);
        toolbar = findViewById(R.id.foorprint_aty_tb);
        ibtn_back = findViewById(R.id.foorprint_aty_btnback);
        ibtn_add = findViewById(R.id.foorprint_aty_btnadd);
        tvnick = findViewById(R.id.foorprint_aty_nickname);
        ibtn_send = findViewById(R.id.foorprint_aty_ibsendmessage);
        initobject();
    }


    public void initobject() {
        footPrintAdapter = new DynamicMessageListRAdapter(FoorPrintAty.this);
        footPrintAdapter.setOnClicker(new DynamicMessageListRAdapter.OnClicker() {
            @Override
            public void OnClick(int postion) {
                Log.i(TAG, "OnClick: "+postion);
                GPModel.getInstance().setMdynamicMessage(footPrintAdapter.getDynamicMessages().get(postion));

                FoorPrintAty.this.startActivity(new Intent(FoorPrintAty.this,ItemContextAty.class));

            }
        });
        picfile = GPModel.getInstance().GetFindUser() != null ? GPModel.getInstance().GetFindUser().getHeadPortrait() : null;
        if (picfile != null) {
            Message message = new Message();
            message.what = 1;
            message.obj = picfile.getFileUrl();
            handler.sendMessageAtTime(message, 1000);
        }
        //设置控件
        toolbar.setTitle("");
        this.setSupportActionBar(toolbar);
        swipeRefreshLayout.setColorSchemeColors(Color.RED);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.addItemDecoration(new SpacesItemDecroation(15));
        recyclerView.setAdapter(footPrintAdapter);
        //传入一个MUser类显示主页
        final MUser user = GPModel.getInstance().GetFindUser();
        if (user != null) {
            tvnick.setText(user.getNickName() != null ? user.getNickName() : "未命名用户" + user.getObjectId());
            final SwipeRefreshLayout.OnRefreshListener sllinstener = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.i(TAG, "onRefresh: 刷新");
                    GPModel.getInstance().GetMessageList(user, new BaseModel.GetMessageListLinstener() {
                        @Override
                        public void fail(String e) {
                            Log.i(TAG, "fail: 失败" + e);
                        }

                        @Override
                        public void success(List<DynamicMessage> dyanmicMessages) {

                            Log.i(TAG, "success: ");
                            footPrintAdapter.setDynamicMessages((ArrayList<DynamicMessage>) dyanmicMessages);
                            if (dyanmicMessages == null) {
                                findViewById(R.id.foorprint_aty_tv_none).setVisibility(View.VISIBLE);
                            } else {
                                if (dyanmicMessages.size() == 0) {
                                    findViewById(R.id.foorprint_aty_tv_none).setVisibility(View.VISIBLE);
                                } else {
                                    findViewById(R.id.foorprint_aty_tv_none).setVisibility(View.GONE);
                                }
                            }
                            footPrintAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            };
            swipeRefreshLayout.setOnRefreshListener(sllinstener);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    sllinstener.onRefresh();
                }
            });
        }
        if (GPModel.getInstance().GetFindUser() != null) {
            if (GPModel.getCurrentUser(MUser.class).getObjectId().equals(GPModel.getInstance().GetFindUser().getObjectId())) {
                ibtn_add.setVisibility(View.INVISIBLE);
                ibtn_send.setVisibility(View.INVISIBLE);

            }
        }


        ibtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 添加好友
                Log.i(TAG, "onClick: 添加的好友是" + user.getNickName());
                GPModel.getInstance().AddFriends(GPModel.getInstance().GetFindUser(), "添加好友理由:", new BaseModel.DoneListener() {
                    @Override
                    public void fail(String error) {
                        Log.i(TAG, "fail: 添加好友" + error);
                    }

                    @Override
                    public void process() {

                    }

                    @Override
                    public void success() {
                        Log.i(TAG, " 添加好友成功");
                        FoorPrintAty.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FoorPrintAty.this, "添加好友成功", Toast.LENGTH_SHORT).show();
                                ibtn_add.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });
            }
        });

        ibtn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转对话
                Intent intent = new Intent(FoorPrintAty.this, ChatViewActivity.class);
                Bundle args = new Bundle();
                args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                args.putString(EaseConstant.EXTRA_USER_ID, GPModel.getInstance().GetFindUser().getUsername());
                intent.putExtras(args);
                startActivity(intent);
            }
        });
        ibtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FoorPrintAty.this.finish();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(FoorPrintAty.this).resumeRequests();//恢复Glide加载图片
                } else {
                    Glide.with(FoorPrintAty.this).pauseRequests();//禁止Glide加载图片
                }
            }
        });

    }
}
