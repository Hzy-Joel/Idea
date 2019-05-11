package com.sg.hzy.idea.UI;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.plattysoft.leonids.ParticleSystem;
import com.sg.hzy.idea.R;
import com.sg.hzy.idea.Utils.ScreenUtils;

/**
 * Created by 胡泽宇 on 2018/11/25.
 */

public class LoveAnimation {

    public static LoveAnimation mLoveAnimation;
    public Activity mActivity;


    private LoveAnimation() {
    }


    public static synchronized LoveAnimation getInstance() {
        if (mLoveAnimation == null) {
            mLoveAnimation = new LoveAnimation();
        }
        return mLoveAnimation;
    }


    public void starAnimation(Activity context, final RelativeLayout relativeLayout) {
        mActivity = context;
        ScreenUtils.setScreen(context);
        AnimationDrawable animationDrawableBig = (AnimationDrawable) mActivity.getDrawable(R.drawable.love_animation_big);
        AnimationDrawable animationDrawableSmall = (AnimationDrawable) mActivity.getDrawable(R.drawable.love_animation_small);
        for (int i = 1; i <= 6; i++) {
            new ParticleSystem(mActivity, 1, animationDrawableBig, 700)
                    .setSpeedModuleAndAngleRange(0.10f, 0.10f, 60 * i, 60 * i)
                    .setRotationSpeed(60)
                    .setFadeOut(100)
                    .emit(ScreenUtils.WIDTH/ 2, ScreenUtils.HEIGHT / 2, 1, 1000);
        }

        new ParticleSystem(mActivity, 2000, R.drawable.circle_2, 200)
                .setSpeedModuleAndAngleRange(0.25f, 0.25f, 0, 360)
                .setRotationSpeed(60)
                .setFadeOut(100)
                .emit(ScreenUtils.WIDTH/ 2, ScreenUtils.HEIGHT/ 2, 1000000, 100);

        for (int i = 50; i <= 350 ; i += 60) {
            new ParticleSystem(mActivity, 1, animationDrawableSmall, 700)
                    .setSpeedModuleAndAngleRange(0.10f, 0.10f, i, i)
                    .setRotationSpeed(60)
                    .emit(ScreenUtils.WIDTH / 2, ScreenUtils.HEIGHT / 2, 1, 1000);
        }

        final Animation animatio0 = AnimationUtils.loadAnimation(mActivity, R.anim.love_animation_mid_0);
        final Animation animatio1 = AnimationUtils.loadAnimation(mActivity, R.anim.love_animation_mid_1);
        final Animation animatio2 = AnimationUtils.loadAnimation(mActivity, R.anim.love_animation_mid_2);


        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.CENTER_IN_PARENT);

        final ImageView imageView = new ImageView(mActivity);
        imageView.setLayoutParams(param);
        relativeLayout.addView(imageView);
        imageView.setImageResource(R.drawable.heart_big);
        imageView.startAnimation(animatio0);
        animatio0.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(animatio1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        animatio1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(animatio2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animatio2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.i("LoveAnimiation", "onAnimationEnd: 动画完成");
                relativeLayout.removeView(imageView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
