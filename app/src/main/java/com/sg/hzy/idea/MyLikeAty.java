package com.sg.hzy.idea;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.sg.hzy.idea.Adapter.DynamicMessageListRAdapter;
import com.sg.hzy.idea.Adapter.FootPrintAdapter;
import com.sg.hzy.idea.DataClass.DynamicMessage;
import com.sg.hzy.idea.DataClass.MUser;
import com.sg.hzy.idea.Model.BaseModel;
import com.sg.hzy.idea.Model.GPModel;
import com.sg.hzy.idea.UI.SpacesItemDecroation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 胡泽宇 on 2018/11/15.
 */

public class MyLikeAty extends AppCompatActivity {
    RecyclerView recyclerView;
    String TAG = "MyLikeAty";
    ImageView button;
    DynamicMessageListRAdapter footPrintAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_like);
        button = findViewById(R.id.mylike_aty_back);
        recyclerView = findViewById(R.id.mylike_aty_fl);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new SpacesItemDecroation(15));
        footPrintAdapter = new DynamicMessageListRAdapter(MyLikeAty.this);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(footPrintAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLikeAty.this.finish();
            }
        });
        GPModel.getInstance().GetLikesDynamicMessageListByUsers(GPModel.getCurrentUser(MUser.class), new BaseModel.GetMessageListLinstener() {
            @Override
            public void fail(String error) {
                Log.i(TAG, "fail: " + error);
            }

            @Override
            public void success(List<DynamicMessage> dyanmicMessages) {
                Log.i(TAG, "success: 查询成功" + dyanmicMessages.size());
                //设置适配器
                footPrintAdapter.setDynamicMessages((ArrayList<DynamicMessage>) dyanmicMessages);
                footPrintAdapter.setOnClicker(new DynamicMessageListRAdapter.OnClicker() {
                    @Override
                    public void OnClick(int postion) {
                        Log.i(TAG, "OnClick: "+postion);
                        GPModel.getInstance().setMdynamicMessage(footPrintAdapter.getDynamicMessages().get(postion));

                        MyLikeAty.this.startActivity(new Intent(MyLikeAty.this,ItemContextAty.class));

                    }
                });
                footPrintAdapter.notifyDataSetChanged();
            }
        });
    }
}
