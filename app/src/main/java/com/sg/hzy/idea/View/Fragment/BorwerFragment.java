package com.sg.hzy.idea.View.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sg.hzy.idea.Adapter.DynamicMessageListRAdapter;
import com.sg.hzy.idea.DataClass.DynamicMessage;
import com.sg.hzy.idea.View.Activity.ItemContextAty;
import com.sg.hzy.idea.View.Activity.MainAty;
import com.sg.hzy.idea.Model.BaseModel;
import com.sg.hzy.idea.Model.GPModel;
import com.sg.hzy.idea.R;
import com.sg.hzy.idea.UI.SmoothScrollLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 胡泽宇 on 2018/11/13.
 */

public class BorwerFragment extends android.support.v4.app.Fragment {
    SmartRefreshLayout sl;
    RecyclerView recyclerView;
    String TAG = "BorwerFragment";

    boolean islocation = false;

    DynamicMessageListRAdapter dynamicMessageListRAdapter;
    boolean isloading = false;
ImageView back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brower, null);
        dynamicMessageListRAdapter = new DynamicMessageListRAdapter(this.getContext());
        sl = view.findViewById(R.id.main_aty_fragment_refresh);
        recyclerView = view.findViewById(R.id.main_aty_fragment_recyclerView);
        back=view.findViewById(R.id.main_aty_Bfragment_back);
        sl.setRefreshFooter(new BallPulseFooter(this.getContext()));
        sl.autoRefresh();
        //下拉刷新
        sl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //
                GPModel.nowPage = 0;

                    GPModel.getInstance().GetMessageListByLabels(GPModel.getInstance().getCheckLabel(), new BaseModel.GetMessageListLinstener() {
                        @Override
                        public void fail(String error) {
                            Log.i(TAG, "fail: 获取标签动态失败" + error);

                            sl.finishRefresh();
                        }

                        @Override
                        public void success(List<DynamicMessage> dyanmicMessages) {
                            dynamicMessageListRAdapter.setDynamicMessages((ArrayList<DynamicMessage>) dyanmicMessages);
                            recyclerView.setAdapter(dynamicMessageListRAdapter);
                            dynamicMessageListRAdapter.notifyDataSetChanged();
                            sl.finishRefresh();

                        }
                    });


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainAty parentActivity = (MainAty ) getActivity();
                parentActivity.ChangeToMore();
            }
        });
        sl.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {


                    GPModel.getInstance().GetMessageListByLabels(GPModel.getInstance().getCheckLabel(), new BaseModel.GetMessageListLinstener() {
                        @Override
                        public void fail(String error) {
                            Log.i(TAG, "fail: 获取标签动态失败" + error);
                            Toast.makeText(BorwerFragment.this.getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                            sl.finishLoadmore();
                        }

                        @Override
                        public void success(List<DynamicMessage> dyanmicMessages) {
                            if (dyanmicMessages.size() == 0) {
                                sl.finishLoadmore(false);
                                dynamicMessageListRAdapter.addDynamicMessages((ArrayList<DynamicMessage>) dyanmicMessages);
                                //延迟
                                dynamicMessageListRAdapter.notifyDataSetChanged();
                                Toast.makeText(BorwerFragment.this.getContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();

                            } else {
                                sl.finishLoadmore();
                                int oldnum = dynamicMessageListRAdapter.getItemCount();
                                dynamicMessageListRAdapter.addDynamicMessages((ArrayList<DynamicMessage>) dyanmicMessages);
                                recyclerView.smoothScrollToPosition(oldnum + 1);
                                //延迟
                                dynamicMessageListRAdapter.notifyDataSetChanged();
                                Toast.makeText(BorwerFragment.this.getContext(), "加载成功", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

            }
        });


        DefaultItemAnimator animator = new DefaultItemAnimator();
        //设置动画时间
        animator.setAddDuration(2000);
        animator.setRemoveDuration(2000);
        SmoothScrollLayoutManager linearLayout = new SmoothScrollLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setItemAnimator(animator);
        dynamicMessageListRAdapter.setOnClicker(new DynamicMessageListRAdapter.OnClicker() {
            @Override
            public void OnClick(int postion) {
                Log.i(TAG, "OnClick: " + postion);
                GPModel.getInstance().setMdynamicMessage(dynamicMessageListRAdapter.getDynamicMessages().get(postion));

                BorwerFragment.this.getContext().startActivity(new Intent(BorwerFragment.this.getContext(), ItemContextAty.class));

            }
        });
//

        EMClient.getInstance().addConnectionListener(new MyConnectionListener());

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    MainAty parentActivity = (MainAty ) getActivity();
                    parentActivity.ChangeToMore();
                    return true;
                }
                return false;
            }
        });

        return view;

    }




    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                    } else {
                        if (NetUtils.hasNetwork(getContext())) {
                            //连接不到聊天服务器
                        } else {
                            //当前网络不可用，请检查网络设置

                        }
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
