package com.sg.hzy.idea.View.Activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.EMNoActiveCallException;
import com.hyphenate.media.EMCallSurfaceView;
import com.sg.hzy.idea.Model.GPModel;
import com.sg.hzy.idea.R;
import com.sg.hzy.idea.UI.mChronometer;
import com.superrtc.sdk.VideoView;

/**
 * Created by 胡泽宇 on 2018/12/23.
 */

public class VideoCallAty  extends AppCompatActivity{
    EMCallSurfaceView oview;
    EMCallSurfaceView myview;



    mChronometer chronometer;
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
        setContentView(R.layout.aty_videocall);


        mibanswer = findViewById(R.id.aty_callvideo_btn_answer);
        mibreject = findViewById(R.id.aty_callvideo_btn_reject);


        ll_recevice = findViewById(R.id.aty_callvideo_ll_receive);
        ll_sendCall = findViewById(R.id.aty_callvideo_ll_sendcall);
        getLl_sendCall_ov = findViewById(R.id.aty_callvideo_ll_sendcall_ov);


        mibcutout = findViewById(R.id.aty_callvideo_btn_cutout);
        mibopenvic = findViewById(R.id.aty_callvideo_btn_openvoide);

        myview=findViewById(R.id.aty_videocall_myview);
        oview=findViewById(R.id.aty_videocall_oview);
        chronometer = findViewById(R.id.aty_videocall_chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setFormat("%s");


        myview.setZOrderMediaOverlay(true);
        myview.setZOrderOnTop(true);
        oview.setScaleMode(VideoView.EMCallViewScaleMode.EMCallViewScaleModeAspectFill);
        myview.setScaleMode(VideoView.EMCallViewScaleMode.EMCallViewScaleModeAspectFit);
        EMClient.getInstance().callManager().setSurfaceView(myview, oview);


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
                VideoCallAty.this.finish();


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

                        Toast.makeText(VideoCallAty.this, "通话开始", Toast.LENGTH_SHORT).show();
                        VideoCallAty.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getLl_sendCall_ov.setVisibility(View.VISIBLE);
                                chronometer.start();
                            }
                        });

                        //TODO 开始计时
                        break;
                    case DISCONNECTED: // 电话断了

                        VideoCallAty.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chronometer.stop();
                            }
                        });

                        Toast.makeText(VideoCallAty.this, "通话结束", Toast.LENGTH_SHORT).show();
                        VideoCallAty.this.finish();
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
