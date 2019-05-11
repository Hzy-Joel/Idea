package com.sg.hzy.idea;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.HxHelper;

import cn.bmob.v3.Bmob;

/**
 * Created by 胡泽宇 on 2018/10/27.
 * <p>
 * <p>
 * 初始化SDK
 */

public class InitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        EMOptions options = new EMOptions();

// 默认添加好友时，是不需要验证的
        options.setAcceptInvitationAlways(true);
// 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
// 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);
        HxHelper.Opts opts = new HxHelper.Opts();
        opts.showChatTitle = false;
        HxHelper.getInstance().init(this, opts);
        EaseUI.getInstance().init(this, options);
        EMClient.getInstance().callManager().getCallOptions().setIsSendPushIfOffline(true);

        Bmob.initialize(this, "00ad89c01f8c6cb4882554bc24ca1859");
        SDKInitializer.initialize(getApplicationContext());

    }

}
