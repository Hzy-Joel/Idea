package com.sg.hzy.idea.View.Activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.EMNoActiveCallException;
import com.sg.hzy.idea.DataClass.MUser;
import com.sg.hzy.idea.Model.BaseModel;
import com.sg.hzy.idea.Model.GPModel;
import com.sg.hzy.idea.R;
import com.sg.hzy.idea.UI.CircleImageView;
import com.sg.hzy.idea.UI.mChronometer;

import java.util.List;

/**
 * Created by 胡泽宇 on 2018/12/12.
 */

public class VoiceCallActicity extends AppCompatActivity {
    ImageView mwaitiv;

    CircleImageView mcvhead;
    TextView tvnickname;
    mChronometer chronometer;


    private AnimationDrawable waitAnimation;

    MUser mUser;
    String TAG = "VoiceCallActicity";
    String Nickname;
    String HeadPic;

    LinearLayout ll_recevice;
    LinearLayout ll_sendCall;
    LinearLayout getLl_sendCall_ov;

    //扬声器
    ImageButton mibopenvic;
    //接听
    ImageButton mibanswer;
    //拒听
    ImageButton mibreject;
    //挂断
    ImageButton mibcutout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_callvoice);
        mUser = GPModel.getInstance().GetFindUser();

        mibanswer = findViewById(R.id.aty_callvoice_btn_answer);
        mibreject = findViewById(R.id.aty_callvoice_btn_reject);


        ll_recevice = findViewById(R.id.aty_callvoice_ll_receive);
        ll_sendCall = findViewById(R.id.aty_callvoice_ll_sendcall);
        getLl_sendCall_ov = findViewById(R.id.aty_callvoice_ll_sendcall_ov);
        mwaitiv = findViewById(R.id.aty_callvoice_imagewait);
        tvnickname = findViewById(R.id.aty_callvoice_nickname);

        mibcutout = findViewById(R.id.aty_callvoice_btn_cutout);
        mibopenvic = findViewById(R.id.aty_callvoice_btn_openvoide);

        mcvhead = findViewById(R.id.aty_callvoice_headImageView);

        chronometer = findViewById(R.id.aty_callvoice_timer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setFormat("%s");
        GPModel.getInstance().FindUserByAccount(getIntent().getStringExtra(GPModel.Call_User), new BaseModel.GetUsersLinstener() {
            @Override
            public void fail(String error) {
                Log.i(TAG, "fail: 获取用户失败");
            }

            @Override
            public void success(List<MUser> userList) {
                if (userList.get(0) != null) {
                    mUser = userList.get(0);
                    Nickname = mUser.getNickName() != null ? mUser.getNickName() : "未命名用户";
                    HeadPic = mUser.getHeadPortrait() != null ? mUser.getHeadPortrait().getFileUrl() : null;
                    //设置数据
                    //加载头像
                    if (HeadPic != null) {
                        Glide.with(VoiceCallActicity.this)
                                .load(HeadPic)
                                .asBitmap()
                                .error(R.drawable.head_portrait_deafult)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mcvhead);
                    }
                    if (Nickname != null) {
                        tvnickname.setText(Nickname);
                    }

                }
            }
        });
        mwaitiv.setVisibility(View.VISIBLE);
        waitAnimation = (AnimationDrawable) this.getDrawable(R.drawable.waitconnectanimation);
        mwaitiv.setBackground(waitAnimation);
        waitAnimation.start();
        Log.i(TAG, "onCreate: " + getIntent().getStringExtra(GPModel.Call_Type));
        if (getIntent().getStringExtra(GPModel.Call_Type).equals(GPModel.Call_Voive)) {
            //拨打电话
            ll_sendCall.setVisibility(View.VISIBLE);
            ll_recevice.setVisibility(View.GONE);
            getLl_sendCall_ov.setVisibility(View.GONE);

        } else {
            //接听电话
            //第一个按钮是接听电话
            //第二个是拒听
            ll_sendCall.setVisibility(View.GONE);
            ll_recevice.setVisibility(View.VISIBLE);

        }
        mibreject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拒听
                try {
                    EMClient.getInstance().callManager().rejectCall();
                    ll_sendCall.setVisibility(View.VISIBLE);
                    ll_recevice.setVisibility(View.GONE);
                } catch (EMNoActiveCallException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });
        mibanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //接听电话
                try {
                    EMClient.getInstance().callManager().answerCall();
                    ll_sendCall.setVisibility(View.VISIBLE);
                    ll_recevice.setVisibility(View.GONE);
                    getLl_sendCall_ov.setVisibility(View.GONE);

                } catch (EMNoActiveCallException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        mibcutout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //挂断按钮
                try {
                    EMClient.getInstance().callManager().endCall();

                } catch (EMNoActiveCallException e) {
                    e.printStackTrace();
                }
                VoiceCallActicity.this.finish();


            }
        });
        mibopenvic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //音量打开

//
            }

        });
        EMClient.getInstance().callManager().addCallStateChangeListener(new EMCallStateChangeListener() {
            @Override
            public void onCallStateChanged(CallState callState, CallError error) {
                switch (callState) {
                    case CONNECTING: // 正在连接对方

                        break;
                    case CONNECTED: // 双方已经建立连接

                        break;

                    case ACCEPTED: // 电话接通成功

                        Toast.makeText(VoiceCallActicity.this, "通话开始", Toast.LENGTH_SHORT).show();
                        VoiceCallActicity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getLl_sendCall_ov.setVisibility(View.VISIBLE);
                                chronometer.start();
                            }
                        });

                        //TODO 开始计时
                        break;
                    case DISCONNECTED: // 电话断了
                        VoiceCallActicity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chronometer.stop();
                            }
                        });
                        Toast.makeText(VoiceCallActicity.this, "通话结束", Toast.LENGTH_SHORT).show();
                        VoiceCallActicity.this.finish();
                        break;
                    case NETWORK_UNSTABLE: //网络不稳定
                        if (error == CallError.ERROR_NO_DATA) {
                            //无通话数据
                        } else {
                        }
                        break;
                    case NETWORK_NORMAL: //网络恢复正常

                        break;
                    default:
                        break;
                }

            }
        });

    }
}
