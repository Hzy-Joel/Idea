package com.sg.hzy.idea;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sg.hzy.idea.Adapter.MainRecycleItemCommentsAdapter;
import com.sg.hzy.idea.DataClass.Comments;
import com.sg.hzy.idea.DataClass.DynamicMessage;
import com.sg.hzy.idea.DataClass.Labels;
import com.sg.hzy.idea.Model.BaseModel;
import com.sg.hzy.idea.Model.GPModel;
import com.sg.hzy.idea.UI.CircleImageView;
import com.sg.hzy.idea.UI.DividerItemDecoration;
import com.sg.hzy.idea.UI.EditViewPopupwindow;
import com.sg.hzy.idea.UI.LoveAnimation;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;

/**
 * Created by 胡泽宇 on 2018/11/22.
 */

public class ItemContextAty extends AppCompatActivity {
    CircleImageView cvhead;
    TextView nickname;
    TextView date;
    String TAG = "ItemContextAty";

    TextView tv_label;
    EditViewPopupwindow editViewPopupwindow;

    WebView context;
    //点赞
    ImageButton ibgood;
    //评论
    ImageButton ibcomments;
    //共有几条评论
    TextView tvnum;
    //评论显示
    RecyclerView commentsRV;
    //点赞数
    TextView tvgoodNum;
    RelativeLayout rl;
    int goodnum = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dms_item);
        tv_label = findViewById(R.id.main_aty_item_tv_label);
        cvhead = findViewById(com.sg.hzy.idea.R.id.main_aty_item_headIV);
        nickname = findViewById(com.sg.hzy.idea.R.id.main_aty_item_nicknametv);
        date = findViewById(com.sg.hzy.idea.R.id.main_aty_item_date);
        rl = findViewById(R.id.main_aty_item_rl);
        context = findViewById(com.sg.hzy.idea.R.id.main_aty_item_context);
        ibgood = findViewById(com.sg.hzy.idea.R.id.main_aty_item_good);
        ibcomments = findViewById(com.sg.hzy.idea.R.id.main_aty_item_say);
        tvnum = findViewById(com.sg.hzy.idea.R.id.main_aty_item_commentsNum);
        commentsRV = findViewById(com.sg.hzy.idea.R.id.main_aty_item_commentsRV);
        tvgoodNum = findViewById(com.sg.hzy.idea.R.id.main_aty_item_goodnum);
        commentsRV.setLayoutManager(new LinearLayoutManager(this));
        commentsRV.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        int screenDensity = getResources().getDisplayMetrics().densityDpi;
        WebSettings webSettings = context.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        findViewById(R.id.itemcontent_aty_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemContextAty.this.finish();
            }
        });

        initobject();

    }

    private void initobject() {
        final DynamicMessage dynamicMessage = GPModel.getInstance().GetFindDynamicMessage();
        if (dynamicMessage != null) {
            nickname.setText(dynamicMessage.getUser().getNickName() != null ? dynamicMessage.getUser().getNickName() : "未命名用户");
            context.loadDataWithBaseURL(null, dynamicMessage.getContent(), "text/html", "utf-8", null);
            date.setText(dynamicMessage.getCreatedAt());
            nickname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GPModel.getInstance().setmUser(dynamicMessage.getUser());
                    Intent intent = new Intent(ItemContextAty.this, FoorPrintAty.class);
                    ItemContextAty.this.startActivity(intent);
                }
            });
            cvhead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GPModel.getInstance().setmUser(dynamicMessage.getUser());
                    Intent intent = new Intent(ItemContextAty.this, FoorPrintAty.class);
                    ItemContextAty.this.startActivity(intent);
                }
            });
            GPModel.getInstance().FindLabelByDynamicMessage(dynamicMessage, new BaseModel.GetLabelsLinstener() {
                @Override
                public void fail(String error) {

                }

                @Override
                public void success(List<Labels> userList) {
                    if (userList != null) {
                        tv_label.setText(" #" + userList.get(0).getLabel());
                    }
                }
            });
            BmobFile bmobFile = dynamicMessage.getUser().getHeadPortrait();
            if (bmobFile != null) {
                Glide.with(ItemContextAty.this)
                        .load(bmobFile.getFileUrl())
                        .asBitmap()
                        .error(com.sg.hzy.idea.R.drawable.head_portrait_deafult)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(cvhead);
            }


            final MainRecycleItemCommentsAdapter mainRecycleItemCommentsAdapter = new MainRecycleItemCommentsAdapter(ItemContextAty.this);

            //TODO 设置按钮点击事件   holder.ibcomments
            ibcomments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editViewPopupwindow = new EditViewPopupwindow(ItemContextAty.this);

                    View.OnClickListener click = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!editViewPopupwindow.getETcontextisEmpty()) {
                                GPModel.getInstance().PostComments(dynamicMessage, editViewPopupwindow.et_context.getText().toString(), new BaseModel.DoneListener() {
                                    @Override
                                    public void fail(String error) {

                                        Toast.makeText(ItemContextAty.this, "评论失败!" + error, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void process() {

                                    }

                                    @Override
                                    public void success() {
                                        Toast.makeText(ItemContextAty.this, "评论成功!", Toast.LENGTH_SHORT).show();

                                        GPModel.getInstance().GetCommentsByDyanmicMessage(dynamicMessage, new BaseModel.GetCommentsLinstener() {
                                            @Override
                                            public void fail(String error) {

                                            }

                                            @Override
                                            public void success(List<Comments> commentsList) {
                                                mainRecycleItemCommentsAdapter.setComments((ArrayList<Comments>) commentsList);
                                                mainRecycleItemCommentsAdapter.notifyDataSetChanged();
                                                if (commentsList != null) {
                                                    tvnum.setText("共有" + commentsList.size() + "条评论");
                                                } else {
                                                    commentsRV.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                                    }
                                });
                                editViewPopupwindow.dismiss();
                            } else {
                                Toast.makeText(ItemContextAty.this, "评论内容不能为空！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                    editViewPopupwindow.setItemsOnClick(click);

                    editViewPopupwindow.showAtLocation(ItemContextAty.this.findViewById(com.sg.hzy.idea.R.id.main_aty_item_commentsRV), Gravity.BOTTOM, 0, 0);

                }
            });
            ibgood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ibgood.setClickable(false);
                    GPModel.getInstance().LikeDyanmicMessage(dynamicMessage, new BaseModel.DoneListener() {
                        @Override
                        public void fail(String error) {
                            Log.i(TAG, "fail: 点赞失败");
                            ibgood.setClickable(true);
                        }

                        @Override
                        public void process() {

                        }

                        @Override
                        public void success() {
                            Toast.makeText(ItemContextAty.this, "收藏成功!", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "success: 点赞成功");
                            LoveAnimation.getInstance().starAnimation(ItemContextAty.this, rl);
                            ibgood.startAnimation(AnimationUtils.loadAnimation(ItemContextAty.this, R.anim.like));
                            ibgood.setBackground(ItemContextAty.this.getResources().getDrawable(R.drawable.love_after));
                            ibgood.setClickable(false);
                            tvgoodNum.setText((goodnum + 1) + "");
                        }
                    });

                }
            });
            GPModel.getInstance().GetIsLikers(dynamicMessage, new BaseModel.GetBoolean() {
                @Override
                public void Result(Boolean r) {
                    if (r) {
                        ibgood.setClickable(false);
                        ibgood.setBackground(ItemContextAty.this.getResources().getDrawable(com.sg.hzy.idea.R.drawable.love_after));
                    }
                }

                @Override
                public void ResultNum(int num) {
                    //点赞的数量
                    tvgoodNum.setText(num + "");
                    goodnum = num;
                    Log.i(TAG, dynamicMessage.getContent() + "ResultNum: 点赞的数量" + num);
                }
            });
            //设置评论列表
            GPModel.getInstance().GetCommentsByDyanmicMessage(dynamicMessage, new BaseModel.GetCommentsLinstener() {
                @Override
                public void fail(String error) {

                }

                @Override
                public void success(List<Comments> commentsList) {
                    mainRecycleItemCommentsAdapter.setComments((ArrayList<Comments>) commentsList);
                    commentsRV.setAdapter(mainRecycleItemCommentsAdapter);
                    mainRecycleItemCommentsAdapter.notifyDataSetChanged();
                    if (commentsList != null) {
                        tvnum.setText("共有" + commentsList.size() + "条评论");
                    } else {
                        commentsRV.setVisibility(View.GONE);
                    }
                }
            });

        }

    }
}
