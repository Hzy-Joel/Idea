package com.sg.hzy.idea;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.HxHelper;
import com.hyphenate.easeui.ui.EaseBaiduMapActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.exceptions.EMServiceNotReadyException;
import com.sg.hzy.idea.DataClass.MUser;
import com.sg.hzy.idea.Model.BaseModel;
import com.sg.hzy.idea.Model.GPModel;

import java.util.List;

/**
 * Created by 胡泽宇 on 2018/11/13.
 */

public class ChatViewActivity extends FragmentActivity {
    String towho;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.sg.hzy.idea.R.layout.fragment_session);

        initView();
    }

    //定义辅助类实现EaseChatFragment.EaseChatFragmentHelper的onSetMessageAttributes(EMMessage message)方法
    private class ChatHelper implements EaseChatFragment.EaseChatFragmentHelper {

        private String Nickname;
        private String imguri;

        public void setStr(String s1, String s2) {
            if(s1==null){
                Nickname="未命名用户";

            }else {
                Nickname = s1;
            }
            if(s2==null) {
                imguri="http://bmob-cdn-22226.b0.upaiyun.com/2018/11/13/5f448a354031a9f0801322decfd66c1c.png";
            }else {
                imguri=s2;
            }
        }

        @Override
        public void onSetMessageAttributes(EMMessage message) {
            MUser user = GPModel.getCurrentUser(MUser.class);
            if (null == user) {
                return;
            }

            if(user.getNickName()==null){
                Nickname="未命名用户";

            }else {
                Nickname = user.getNickName();
            }
            if(user.getHeadPortrait()==null) {
                imguri="http://bmob-cdn-22226.b0.upaiyun.com/2018/11/13/f41d21e240cf22b380254e21d0c1f9f2.png";     }else {
                imguri=user.getHeadPortrait().getFileUrl();
            }
//            Log.i("扩展消息", "onSetMessageAttributes: "+userMassage.getNickName()+" "+userMassage.getImg().getFileUrl());
            //设置自己的头像和昵称到消息扩展中
            Log.i("外部设值", "setstr: " + Nickname + " " + imguri);
            message.setAttribute(HxHelper.MSG_EXT_NICKNAME, Nickname);
            message.setAttribute(HxHelper.MSG_EXT_AVATAR, imguri);
        }


        @Override
        public void onEnterToChatDetails() {

        }

        @Override
        public void onAvatarClick(String username) {

        }

        @Override
        public void onAvatarLongClick(String username) {

        }

        @Override
        public boolean onMessageBubbleClick(EMMessage message) {
            return false;
        }

        @Override
        public void onMessageBubbleLongClick(EMMessage message) {

        }

        @Override
        public boolean onExtendMenuItemClick(int itemId, View view) {
            switch (itemId) {
                case 4:
                    Log.i("Chat", "onExtendMenuItemClick: 语音");
                    try {//单参数


                        Intent intent=new Intent(ChatViewActivity.this,VoiceCallActicity.class);
                        intent.putExtra(GPModel.Call_Type,GPModel.Call_Voive);
                        intent.putExtra(GPModel.Call_User,towho);

                        ChatViewActivity.this.startActivity(intent);
                        EMClient.getInstance().callManager().makeVoiceCall(towho);
                    } catch (EMServiceNotReadyException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return true;
                case 5:
                    Log.i("Chat", "onExtendMenuItemClick: 视频");
                    try {//单参数
                        Intent intent1 = new Intent(ChatViewActivity.this, VideoCallAty.class);
                        intent1.putExtra(GPModel.Call_Type,GPModel.Call_Voive);
                        intent1.putExtra(GPModel.Call_User,towho);
                        ChatViewActivity.this.startActivity(intent1);
                        EMClient.getInstance().callManager().makeVideoCall(towho);
                    } catch (EMServiceNotReadyException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return true;
            }
            return false;
        }


        @Override
        public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
            return null;
        }
    }

    private void initView() {
        EaseChatFragment chatFragment = new EaseChatFragment();
        chatFragment.setArguments(getIntent().getExtras());
        towho=getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);
        ChatHelper chatHelper = new ChatHelper();
        chatFragment.setChatFragmentHelper(chatHelper);


        getSupportFragmentManager().beginTransaction().add(com.sg.hzy.idea.R.id.fragment_session_replace_fl, chatFragment).commit();
    }


}


