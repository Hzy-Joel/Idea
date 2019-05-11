package com.hyphenate.easeui;

import android.app.Application;
import android.text.TextUtils;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.List;
import java.util.Map;

/**
 * Created by 胡泽宇 on 2018/3/3.
 * 保存自己的配置
 */

public class HxHelper {
    //扩展消息-昵称
    public static final String MSG_EXT_NICKNAME = "hx_nickname";
    //扩展消息-头像
    public static final String MSG_EXT_AVATAR = "hx_avatar";

    private volatile static HxHelper instance;

    public Application app;
    public Opts mOpts;

    //所有的会话集合
    private Map<String, EMConversation> mConvMap;

    private HxHelper() {
        if (null != instance)
            throw new IllegalStateException("Can not instantiate singleton class.");
    }

    /**
     * 初始化
     *
     * @param application Application
     * @param opts        配置项
     */
    public void init(Application application, Opts opts) {
        app = application;
        mOpts = opts;
    }

    /**
     * 单例模式
     *
     * @return 单例实例
     */
    public static HxHelper getInstance() {
        if (null == instance) {
            synchronized (HxHelper.class) {
                if (null == instance) {
                    instance = new HxHelper();
                }
            }
        }
        return instance;
    }

    public EaseUser getUser(String username) {
        EaseUser user = new EaseUser(username);

        //获取到所有会话
        mConvMap = EMClient.getInstance().chatManager().getAllConversations();

        if (null != mConvMap) {
            List<EMMessage> msgList = null;
            for (Map.Entry<String, EMConversation> et : mConvMap.entrySet()) {
                msgList = et.getValue().getAllMessages();
                //遍历消息列表，从消息扩展中获取昵称和头像
                if (null != msgList && !msgList.isEmpty()) {
                    for (EMMessage msg : msgList) {
                        if (!TextUtils.equals(username, msg.getFrom())) {
                            //如果该条消息不是该用户的，就遍历下一条
                            continue;
                        }
                        //设置昵称和用户名
                        try {
                            user.setNickname(msg.getStringAttribute(MSG_EXT_NICKNAME));
                            user.setAvatar(msg.getStringAttribute(MSG_EXT_AVATAR));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return user;
    }
    public static class Opts {
        public boolean showChatTitle;
    }
}
