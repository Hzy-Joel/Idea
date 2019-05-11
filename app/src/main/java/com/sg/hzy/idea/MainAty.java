package com.sg.hzy.idea;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.HxHelper;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.util.NetUtils;
import com.sg.hzy.idea.UI.BadgeView;
import com.sg.hzy.idea.Utils.ScreenUtils;
import com.sg.hzy.idea.View.Fragment.BorwerFragment;
import com.sg.hzy.idea.View.Fragment.LabelsFragment;
import com.sg.hzy.idea.View.Fragment.SessionFragment;
import com.sg.hzy.idea.DataClass.MUser;
import com.sg.hzy.idea.Model.GPModel;
import com.sg.hzy.idea.UI.CircleImageView;
import com.sg.hzy.idea.UI.DepthPageTransformer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by 胡泽宇 on 2018/11/10.
 */

public class MainAty extends AppCompatActivity implements EMConnectionListener {
    Toolbar toolbar;
    CircleImageView ivhead;

    NavigationView navigationView;
    DrawerLayout drawerLayout;

    //侧滑菜单头部控件
    CircleImageView nvhead;
    TextView nvnickname;
    TextView nvsign;
    ImageView nvheadBcakground;
    FragmentPagerAdapter fragmentPagerAdapter;

    ArrayList<Fragment> arrayList;

    ImageButton ib_session;
    CircleImageView cv_post;
    ImageButton ib_borwer;

    String TAG = "MainAty";
    BadgeView badgeView;
    ViewPager vp;

    CallReceiver mcallReceiver;
    //是否是更多的界面
    boolean is_more=false;
    //是否需要刷新界面
    boolean need_fresh=false;
    private long exitTime;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Intent intent;
            switch (msg.what) {
                case 0:
                    intent = new Intent(MainAty.this, ContactListAty.class);
                    MainAty.this.startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(MainAty.this, FoorPrintAty.class);
                    MainAty.this.startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(MainAty.this, MyLikeAty.class);
                    MainAty.this.startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(MainAty.this, SetMessageAty.class);
                    MainAty.this.startActivity(intent);
                    break;
                case 4:
                    intent = new Intent(MainAty.this, AboutUsAty.class);
                    MainAty.this.startActivity(intent);
                    break;
                case 5:
                    SharedPreferences sp = getSharedPreferences("LoginMessage", MODE_PRIVATE);
                    if (sp != null) {
                        sp.edit().clear().apply();
                    }
                    BmobUser.logOut();
                    EMClient.getInstance().logout(true);
                    intent = new Intent(MainAty.this, LoginActivity.class);
                    MainAty.this.startActivity(intent);
                    MainAty.this.finish();
                    break;
                case 6:
                    int Num = 0;
                    Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
                    Iterator iterator = conversations.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(entry.getKey().toString(), EMConversation.EMConversationType.Chat, true);
                        Num += conversation.getUnreadMsgCount();

                        Log.i(TAG, "setBadgeView: " + conversation.getUnreadMsgCount());
                    }
                    Log.i(TAG, "setBadgeView: " + Num);

                    badgeView.setBadgeCount(Num);
                    break;


            }
            return true;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.aty_main);
        setWindowsColor();



        vp = findViewById(R.id.main_aty_vp);
        badgeView = new BadgeView(MainAty.this);
        toolbar = findViewById(R.id.main_aty_tl);
        ivhead = findViewById(R.id.main_aty_cv);
        navigationView = findViewById(R.id.main_aty_navigation_view);
        drawerLayout = findViewById(R.id.main_aty_drawer_layout);
        nvhead = navigationView.getHeaderView(0).findViewById(R.id.main_aty_nv_headcv);
        nvnickname = navigationView.getHeaderView(0).findViewById(R.id.main_aty_nv_nickname);
        nvsign = navigationView.getHeaderView(0).findViewById(R.id.main_aty_nv_sign);
        nvheadBcakground = navigationView.getHeaderView(0).findViewById(R.id.main_aty_nv_headcv_bcakground);
        ib_borwer = findViewById(R.id.main_aty_vp_btn_brower);
        cv_post = findViewById(R.id.main_aty_vp_btn_post);
        ib_session = findViewById(R.id.main_aty_vp_btn_session);

        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();

        init();
    }

    private void setWindowsColor() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
        View statusBarView = new View(window.getContext());
        int statusBarHeight = ScreenUtils.getStatusBarHeight(window.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(getResources().getColor(R.color.bg));
        decorViewGroup.addView(statusBarView);
    }

    private void init() {
        navigationView.setItemIconTintList(null);
        toolbar.setVisibility(View.GONE);
        ib_borwer.setImageResource(R.drawable.btn_browse_after);
        ib_session.setImageResource(R.drawable.btn_message_before);
        //头像点击事件
        ivhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        badgeView.setTargetView(findViewById(R.id.main_aty_vp_btn_session_ll));
        badgeView.setBadgeGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        setBadgeView();
        //侧滑菜单点击事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Message message;
//                    item.setChecked(true);设置后分组中的按钮按下后会消失
                switch (item.getItemId()) {
                    case R.id.main_aty_navigation_item_firenders:
                        //联系人
                        message = new Message();
                        message.what = 0;
                        handler.sendMessageAtTime(message, 1000);

                        break;
                    case R.id.main_aty_navigation_item_Myd:
                        //我的足迹
                        message = new Message();
                        message.what = 1;
                        handler.sendMessageAtTime(message, 1000);
                        GPModel.getInstance().setmUser(GPModel.getCurrentUser(MUser.class));

                        break;
                    case R.id.main_aty_navigation_item_Mylike:
                        //我的赞
                        message = new Message();
                        message.what = 2;
                        handler.sendMessageAtTime(message, 1000);

                        break;
                    case R.id.main_aty_navigation_item_setting:
                        //个人设置
                        message = new Message();
                        message.what = 3;
                        handler.sendMessageAtTime(message, 1000);

                        break;
                    case R.id.main_aty_navigation_item_aboutus:
                        //关于我们
                        message = new Message();
                        message.what = 4;
                        handler.sendMessageAtTime(message, 1000);

                        break;
                    case R.id.main_aty_navigation_item_loginout:
                        //退出登陆
                        //清除保存数据
                        message = new Message();
                        message.what = 5;
                        handler.sendMessageAtTime(message, 1000);

                        break;


                }
                drawerLayout.closeDrawers();
                return true;
            }
        });


        //TODO vp设置与监听
        arrayList = new ArrayList<>();
        arrayList.add(new LabelsFragment());
        arrayList.add(new SessionFragment());
        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                return arrayList.get(position);
            }

            @Override
            public int getItemPosition(Object object) {
                if(need_fresh){
                    need_fresh=true;
                    return -2;
                }
                return super.getItemPosition(object);
            }

            /***
             * 设置用于Tablayout展现
             * @param position
             * @return
             */
            @Override
            public CharSequence getPageTitle(int position) {
                return "";
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                if (need_fresh) {//根据需求添加更新标示，UI更新完成后改回false，看不懂的回家种田
                    //得到缓存的fragment
                    if(position==0) {
                        Fragment fragment = (Fragment) super.instantiateItem(container, position);
//得到tag，这点很重要
                        String fragmentTag = fragment.getTag(); //这里的tag是系统自己生产的，我们直接取就可以
//如果这个fragment需要更新
                        android.support.v4.app.FragmentTransaction ft = MainAty.this.getSupportFragmentManager().beginTransaction();
//移除旧的fragment
                        ft.remove(fragment);
//换成新的fragment
                        fragment = arrayList.get(position);
//添加新fragment时必须用前面获得的tag，这点很重要
                        ft.add(container.getId(), fragment, fragmentTag);
                        ft.attach(fragment);
                        ft.commit();
                        need_fresh=false;
                        return fragment;
                    }
                    return super.instantiateItem(container, position);

                } else {
                    return super.instantiateItem(container, position);
                }
            }

            @Override
            public int getCount() {
                return arrayList.size();
            }
        };
        //初始化适配器

        //设置适配器
        vp.setAdapter(fragmentPagerAdapter);
        vp.setPageTransformer(true, new DepthPageTransformer());

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    //006cfa
                    toolbar.setVisibility(View.VISIBLE);
                    ib_borwer.setImageResource(R.drawable.btn_browse_before);
                    ib_session.setImageResource(R.drawable.btn_message_after);
                }
                if (position == 0) {
                    toolbar.setVisibility(View.GONE);
                    ib_borwer.setImageResource(R.drawable.btn_browse_after);
                    ib_session.setImageResource(R.drawable.btn_message_before);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        findViewById(R.id.main_aty_vp_btn_session_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(1);
            }
        });
        findViewById(R.id.main_aty_vp_btn_brower_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(0);

            }
        });
        loadusermessage();

        //头像设置
        new Thread(new Runnable() {
            @Override
            public void run() {
                EaseUI.getInstance().setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
                    @Override
                    public EaseUser getUser(String username) {
                        MUser user = GPModel.getCurrentUser(MUser.class);
                        //如果是当前用户，就设置自己的昵称和头像
                        if (null != user && TextUtils.equals(user.getUsername(), username)) {
                            EaseUser eu = new EaseUser(username);
                            String Nickname;
                            String imguri;
                            if (user.getNickName() == null) {
                                Nickname = "未命名用户" + user.getObjectId();

                            } else {
                                Nickname = user.getNickName();
                            }
                            if (user.getHeadPortrait() == null) {
                                imguri = "http://bmob-cdn-22226.b0.upaiyun.com/2018/11/13/f41d21e240cf22b380254e21d0c1f9f2.png";
                            } else {
                                imguri = user.getHeadPortrait().getFileUrl();
                            }
                            eu.setNickname(username);
                            eu.setAvatar(imguri);
                            return eu;
                        }
                        //否则交给HxHelper处理，从消息中获取昵称和头像
                        return HxHelper.getInstance().getUser(username);

                    }
                });
                EMClient.getInstance().addConnectionListener(MainAty.this);
            }
        }).run();


        cv_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(MainAty.this, NewsActivity.class);
//                startActivity(i);
                Intent i = new Intent(MainAty.this, ReleaseAty.class);
                startActivity(i);
            }
        });
        //注册广播
        mcallReceiver=new CallReceiver();
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        registerReceiver(mcallReceiver, callFilter);



    }

    void loadusermessage() {
        BmobFile head = GPModel.getCurrentUser(MUser.class).getHeadPortrait();
        String HeadPic = null;
        if (head != null) {
            HeadPic = head.getFileUrl();
        }
        String Nickname = GPModel.getCurrentUser(MUser.class).getNickName();
        String Sign = GPModel.getCurrentUser(MUser.class).getSign();

        //加载头像
        if (HeadPic != null) {
            Glide.with(this)
                    .load(HeadPic)
                    .asBitmap()
                    .error(R.drawable.head_portrait_deafult)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    //头像设置
                    if (resource != null) {
                        ivhead.setImageBitmap(resource);
                        nvhead.setImageBitmap(resource);
                    }

                }
            });
            Glide.with(MainAty.this).load(HeadPic)
                    .bitmapTransform(new BlurTransformation(MainAty.this, 25), new CenterCrop(MainAty.this))
                    .into(nvheadBcakground);
        }
        if (Nickname != null) {
            nvnickname.setText(Nickname);
        }
        if (Sign != null) {
            nvsign.setText(Sign);
        }


    }
    public void ChangeToMore(){
        is_more=!is_more;
        need_fresh=true;
        if(is_more){
            //
            arrayList.set(0,new BorwerFragment());

            fragmentPagerAdapter.notifyDataSetChanged();
        }else{
            arrayList.set(0,new LabelsFragment());
            fragmentPagerAdapter.notifyDataSetChanged();
        }
    }
    public void setBadgeView() {

        Message message = new Message();
        message.what = 6;
        handler.sendMessageAtTime(message, 1000);

    }

    @Override
    protected void onDestroy() {
        BmobUser.logOut();
        EMClient.getInstance().logout(true);
        unregisterReceiver(mcallReceiver);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        loadusermessage();
        super.onResume();
    }

    // 让菜单同时显示图标和文字
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onConnected() {
    }

    @Override
    public void onDisconnected(final int error) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (error == EMError.USER_REMOVED) {
                    // 显示帐号已经被移除
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    // 显示帐号在其他设备登录
                } else {
                    if (NetUtils.hasNetwork(MainAty.this)) {

                        //连接不到聊天服务器
                    } else {

                        //当前网络不可用，请检查网络设置
                        Toast.makeText(MainAty.this, "网络不可用！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private class CallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 拨打方username
            String from = intent.getStringExtra("from");
            // call type
            String type = intent.getStringExtra("type");
            //跳转到通话页
            //vedio
            //voice
            Toast.makeText(context,from+"  "+type,Toast.LENGTH_SHORT).show();

            Intent intent1;
            switch (type){
                case  "video":
                    intent1=new Intent(MainAty.this,VideoCallAty.class);
                    intent1.putExtra(GPModel.Call_Type,GPModel.Recevice_Voive);
                    intent1.putExtra(GPModel.Call_User,from);
                    MainAty.this.startActivity(intent1);
                    break;
                case "voice":
                    intent1=new Intent(MainAty.this,VoiceCallActicity.class);
                    intent1.putExtra(GPModel.Call_Type,GPModel.Recevice_Voive);
                    intent1.putExtra(GPModel.Call_User,from);
                    MainAty.this.startActivity(intent1);
                    break;
            }


//            MainAty.this.unregisterReceiver(mcallReceiver);
//            mcallReceiver=new CallReceiver();
//            IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
//            registerReceiver(mcallReceiver, callFilter);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainAty.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                //记录最后一次按键时间
                exitTime = System.currentTimeMillis();
            } else {
//                    这里是我做了保存数据操作
//                    BLEDeviceLab.get(getActivity()).saveDevices();
//                    mEditor.putString("ip", ipAddress);
//                    mEditor.commit();
                MainAty.this.finish();
                System.exit(0);
            }
            return true;
        }
        return false;

    }
}
