package com.sg.hzy.idea.View.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.sg.hzy.idea.ChatViewActivity;
import com.sg.hzy.idea.MainAty;
import com.sg.hzy.idea.R;

import java.util.List;

/**
 * Created by 胡泽宇 on 2018/2/26.
 * <p>
 * 会话界面
 */

public class SessionFragment extends Fragment {
    private EaseConversationListFragment easeConversationListFragment;
    EMMessageListener msgListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session, null);
        initView();
        AddListener();
        return view;
    }

    private void AddListener() {
        msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                MainAty aty= (MainAty) SessionFragment.this.getActivity();
                aty.setBadgeView();
                easeConversationListFragment.refresh();
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }


    private void initView() {

        easeConversationListFragment = new EaseConversationListFragment();
        easeConversationListFragment.hideTitleBar();
        easeConversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                //启动会话Aty;TODO
//                Log.i("会话列表点击事件", "onListItemClicked: "+conversation.getType()+" "+conversation.conversationId());

                Intent intent=new Intent(getActivity(), ChatViewActivity.class);
                Bundle args = new Bundle();
                args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                args.putString(EaseConstant.EXTRA_USER_ID,conversation.conversationId());

                intent.putExtras(args);
                startActivity(intent);
            }
        });

        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_session_replace_fl, easeConversationListFragment).commit();

    }

    @Override
    public void onDestroy() {
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        MainAty aty= (MainAty) SessionFragment.this.getActivity();
        aty.setBadgeView();
        easeConversationListFragment.refresh();
        super.onResume();
    }
}
