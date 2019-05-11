package com.sg.hzy.idea.Model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.sg.hzy.idea.DataClass.Comments;
import com.sg.hzy.idea.DataClass.DynamicMessage;
import com.sg.hzy.idea.DataClass.Labels;
import com.sg.hzy.idea.DataClass.MUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by 胡泽宇 on 2018/10/27.
 */

public class GPModel implements BaseModel {
    //个人主页的信息传递
    private MUser mUser;
    //用于详情页和动态定位的传递
    private DynamicMessage mdynamicMessage;
    public final static String TAG = "GPModel";
    static GPModel gpModel;
    public static boolean save = false;
    //默认加载3条数据
    public static int LoadPagerNum = 3;
    //已经加载了多少条数据
    public static int nowPage = 0;
    //当前查看的热词
    private  Labels checkLabel ;

    private String postheadpicurl;
    public static String Recevice_Voive="Recevice_Voive";


    public static String Call_Voive="Call_Voive";
    public static String Call_Type="Call_type";
    public static String Call_User="CallUser";
    public static GPModel getInstance() {
        if (gpModel == null) {
            gpModel = new GPModel();
        }
        return gpModel;
    }

    public  Labels getCheckLabel() {
        return checkLabel;
    }

    public  void setCheckLabel(Labels checkLabel) {
        this.checkLabel=checkLabel;
    }

    public DynamicMessage GetFindDynamicMessage() {
        if (mdynamicMessage != null) {
            return mdynamicMessage;
        }
        return null;
    }

    public MUser GetFindUser() {
        if (mUser != null) {
            return mUser;
        } else {
            return null;
        }
    }

    public void setmUser(MUser mUser) {
        this.mUser = mUser;
    }

    @Override
    public void UserRegistered(String nickname, String email, String phone, DoneListener registeredListener) {

    }

    /***
     * 登陆函数
     * @param account 账户
     * @param passwords 密码
     * @param doneListener
     */
    @Override
    public void Login(final String account, final String passwords, final DoneListener doneListener) {
        MUser user = new MUser();
        user.setUsername(account);
        user.setPassword(passwords);
        user.login(new SaveListener<MUser>() {
            @Override
            public void done(MUser user, BmobException e) {
                if (e == null) {
                    EMClient.getInstance().login(account, passwords, new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    doneListener.success();
                                    Log.i(TAG, "onSuccess: 登陆成功");
                                }
                            });
                        }

                        @Override
                        public void onError(int i, final String s) {
                            Log.i(TAG, "onError: 登陆失败" + s);
                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    doneListener.fail(s);
                                    Log.i(TAG, "onError: 登陆失败" + s);
                                }
                            });
                        }

                        @Override
                        public void onProgress(int i, final String s) {

                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    doneListener.process();
                                    Log.i(TAG, "onProgress: 登陆中");
                                }
                            });
                        }
                    });
                } else {
                    Log.i(TAG, "done：登陆失败 " + e.getMessage());
                    doneListener.fail(e.getMessage());
                }
            }
        });
    }

    /***
     * 获取当前用户的动态发布
     * @param getMessageList
     */
    @Override
    public void GetMessageList(MUser user, final GetMessageListLinstener getMessageList) {

        BmobQuery<DynamicMessage> query = new BmobQuery<DynamicMessage>();
        query.addWhereEqualTo("User", user);  // 查询用户的所有帖子
        query.include("User");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.order("-createdAt");
        query.findObjects(new FindListener<DynamicMessage>() {

            @Override
            public void done(List<DynamicMessage> object, BmobException e) {
                if (e == null) {
                    Log.i("获取当前用户的动态", "成功");
                    getMessageList.success(object);
                } else {
                    getMessageList.fail(e.getMessage());
                    Log.i("获取当前用户的动态", "失败：" + e.getMessage());
                }
            }

        });
    }

    @Override
    public void GetAllDynamicMessage(final GetMessageListLinstener getMessageListLinstener) {
        BmobQuery<DynamicMessage> query = new BmobQuery<DynamicMessage>();
        query.order("-createdAt");
        query.include("User");
        //设定跳过的条数
        query.setSkip(nowPage);
        query.include("User");
        //默认加载3条数据
        query.setLimit(LoadPagerNum);
        query.findObjects(new FindListener<DynamicMessage>() {

            @Override
            public void done(List<DynamicMessage> object, BmobException e) {
                if (e == null) {
                    Log.i("获取当前用户的动态", "成功");
                    nowPage+=object.size();
                    getMessageListLinstener.success(object);
                } else {
                    getMessageListLinstener.fail(e.getMessage());
                    Log.i("获取当前用户的动态", "失败：" + e.getMessage());
                }
            }

        });
    }

    /***
     * 通过标签获取消息
     *
     *
     */
    @Override
    public void GetMessageListByLabels(Labels label, final GetMessageListLinstener getMessageList) {

        BmobQuery<DynamicMessage> query = new BmobQuery<DynamicMessage>();
        //设定跳过的条数
        query.setSkip(nowPage);
        query.include("User");
        //默认加载3条数据
        query.setLimit(LoadPagerNum);
        query.order("-createdAt");
        query.addWhereEqualTo("Labels", label);
        query.findObjects(new FindListener<DynamicMessage>() {

            @Override
            public void done(List<DynamicMessage> object, BmobException e) {
                if (e == null) {
                    getMessageList.success(object);
                    nowPage+=object.size();
                    Log.i(TAG, "查询个数：" + object.size());
                } else {
                    getMessageList.fail(e.getMessage());
                    Log.i(TAG, "查询失败：" + e.getMessage());
                }
            }

        });
    }

    @Override
    public void GetCommentsByDyanmicMessage(DynamicMessage dyanmicMessage, final GetCommentsLinstener getComments) {

        BmobQuery<Comments> query = new BmobQuery<Comments>();
        query.addWhereEqualTo("WhichMessage", dyanmicMessage);
        query.include("author");

        query.order("-createdAt");
        query.findObjects(new FindListener<Comments>() {

            @Override
            public void done(List<Comments> object, BmobException e) {
                if (e == null) {
                    Log.i("获取动态的评论", "成功");
                    getComments.success(object);
                } else {
                    getComments.fail(e.getMessage());
                    Log.i("获取动态的评论", "失败：" + e.getMessage());
                }
            }

        });
    }

    /***
     * 发布动态html
     * @param textcontext
     * @param Content
     *@param doneListener
     */
    @Override
    public void PostDyanmicMessage(String title, StringBuilder textcontext, String Content, final Labels labels, final DoneListener doneListener) {


        final DynamicMessage dyanmicMessage = new DynamicMessage();
        BmobRelation bmobRelation = new BmobRelation();
        labels.setLabelHeat(labels.getLabelHeat()+1);
        bmobRelation.add(labels);
        dyanmicMessage.setFirstPicurl(GPModel.getInstance().postheadpicurl);
        dyanmicMessage.setTextContent(textcontext.toString());
        dyanmicMessage.setContent(Content);
        dyanmicMessage.setUser(BmobUser.getCurrentUser(MUser.class));
        dyanmicMessage.setLabels(bmobRelation);
        dyanmicMessage.setTitle(title);
        dyanmicMessage.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    doneListener.success();
                    Log.i(TAG, "done: 发布动态成功！");
                    labels.update();
                } else {
                    Log.i(TAG, "发布失败" + e.getMessage());
                    doneListener.fail(e.getMessage());
                }
            }
        });

    }


    /***
     * 发布评论
     * @param dyanmicMessage 评论的动态
     * @param Context 评论的内容
     * @param doneListener
     */
    @Override
    public void PostComments(DynamicMessage dyanmicMessage, String Context, final DoneListener doneListener) {
        Comments comments = new Comments();
        comments.setAuthor(BmobUser.getCurrentUser(MUser.class));
        comments.setContent(Context);
        comments.setWhichMessage(dyanmicMessage);
        comments.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {

                    doneListener.success();
                    Log.i(TAG, "done: 发布评论成功！");
                } else {
                    doneListener.fail(e.getMessage());
                    Log.i(TAG, "done: 发布评论失败！" + e.getMessage());
                }
            }
        });
    }

    /***
     * 收藏动态
     * @param dynamicMessage
     * @param doneListener
     */
    @Override
    public void LikeDyanmicMessage(DynamicMessage dynamicMessage, final DoneListener doneListener) {
        BmobRelation bmobRelation = new BmobRelation();
        bmobRelation.add(BmobUser.getCurrentUser(MUser.class));
        dynamicMessage.setLikers(bmobRelation);
        dynamicMessage.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i(TAG, "done: 收藏成功");
                    doneListener.success();
                } else {
                    Log.i(TAG, "收藏失败"+e.getMessage());
                    doneListener.fail(e.getMessage());
                }
            }
        });
    }

    /***
     * 更新头像昵称
     * @param path 头像路径
     * @param Nickname  昵称
     * @param doneListener
     */
    @Override
    public void SetHeadPortraitAndNickName(String path, String Nickname, String sign, Boolean sex, BmobDate bir, String email, final DoneListener doneListener) {
        final MUser u = BmobUser.getCurrentUser(MUser.class);
        u.setSign(sign);
        u.setEmail(email);
        u.setBirthday(bir);
        u.setNickName(Nickname);
        u.setGender(sex);
        if (path != null) {
            BmobFile bmobFile = new BmobFile(new File(path));
            u.setHeadPortrait(bmobFile);
            bmobFile.uploadblock(new UploadFileListener() {

                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        u.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Log.i(TAG, "done: 头像文件上传成功");

                                } else {
                                    Log.i(TAG, "done: 修改信息失败");
                                    doneListener.fail(e.getMessage());
                                }
                            }
                        });
                    } else {
                        Log.i(TAG, "done: 修改信息失败" + "文件上传失败");
                        doneListener.fail(e.getMessage());
                    }

                }

                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                }
            });
        } else {
            u.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e != null) {
                        doneListener.success();
                    } else {
                        doneListener.fail(e.getMessage());
                    }
                }
            });
        }
    }

    /***
     * 添加好友
     * @param who
     */
    @Override
    public void AddFriends(final MUser who, final String reson, final DoneListener doneListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().addContact(who.getUsername().trim(), null);
                    doneListener.success();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    doneListener.fail(e.toString());
                    Log.i(TAG, "AddFriends: " + e.getMessage());

                }

            }
        }).run();

    }

    /***
     * 删除好友
     * @param who
     * @param doneListener
     */
    @Override
    public void DeleteFriend(final MUser who, final DoneListener doneListener) {
        try {
            EMClient.getInstance().contactManager().deleteContact(who.getUsername());
            Log.i(TAG, "done:删除好友成功");
            doneListener.success();
        } catch (HyphenateException e1) {
            Log.i(TAG, "删除好友失败");
            doneListener.fail(e1.toString());
            e1.printStackTrace();
        }
    }

    /***
     * 获取此用户的好友列表
     * @param getFriendsListener
     */
    @Override
    public void FindFrineds(final GetFriendsLinstener getFriendsListener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    Log.i(TAG, "run: friends"+usernames.size());
                    final List<MUser> userlist = new ArrayList<>();
                    for (String name : usernames) {
                        BmobQuery<MUser> query = new BmobQuery<MUser>();
                        query.addWhereEqualTo("username", name);
                        query.include("HeadPortrait");
                        query.setLimit(1);
                        query.findObjects(new FindListener<MUser>() {
                            @Override
                            public void done(List<MUser> list, BmobException e) {
                                if (e == null) {
                                    userlist.add(list.get(0));
                                    if (userlist.size() == usernames.size())
                                        getFriendsListener.success(userlist, usernames);
                                } else {
                                    getFriendsListener.fail(e.getMessage());
                                }
                            }
                        });

                    }
                    getFriendsListener.over();

                } catch (HyphenateException e) {
                    getFriendsListener.fail(e.getErrorCode() + "");
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /***
     * 获取收藏此动态的用户
     * @param dynamicMessage
     * @param getUsers
     */
    @Override
    public void FindLikersByDynamicMessage(DynamicMessage dynamicMessage, final GetUsersLinstener getUsers) {
        BmobQuery<MUser> query = new BmobQuery<MUser>();

        query.addWhereRelatedTo("Likers", new BmobPointer(dynamicMessage));
        query.findObjects(new FindListener<MUser>() {

            @Override
            public void done(List<MUser> object, BmobException e) {
                if (e == null) {
                    getUsers.success(object);
                    Log.i(TAG, "查询收藏用户个数：" + object.size());
                } else {
                    getUsers.fail(e.getMessage());
                    Log.i(TAG, "查询收藏用户失败：" + e.getMessage());
                }
            }

        });
    }

    @Override
    public void SaveUserMessage(final MUser user, String path, final DoneListener doneListener) {
        if (path != null) {
            BmobFile bmobFile = new BmobFile(new File(path));
            user.setHeadPortrait(bmobFile);
            bmobFile.uploadblock(new UploadFileListener() {

                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        user.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Log.i(TAG, "done: 头像文件上传成功");

                                } else {
                                    Log.i(TAG, "done: 修改信息失败");
                                    doneListener.fail(e.getMessage());
                                }
                            }
                        });
                    } else {
                        Log.i(TAG, "done: 修改信息失败" + "文件上传失败");
                        doneListener.fail(e.getMessage());
                    }

                }

                @Override
                public void onProgress(Integer value) {
                    // 返回的上传进度（百分比）
                }
            });
            user.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e != null) {
                        doneListener.success();
                    } else {
                        doneListener.fail(e.getMessage());
                    }
                }
            });
        } else {
            user.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e != null) {
                        doneListener.success();
                    } else {
                        doneListener.fail(e.getMessage());
                    }
                }
            });
        }


    }

    @Override
    public void GetLikesDynamicMessageListByUsers(MUser user, final GetMessageListLinstener getMessageListLinstener) {
        BmobQuery<DynamicMessage> query = new BmobQuery<DynamicMessage>();

        query.addWhereEqualTo("Likers", user);
        query.findObjects(new FindListener<DynamicMessage>() {

            @Override
            public void done(List<DynamicMessage> object, BmobException e) {
                if (e == null) {
                    getMessageListLinstener.success(object);
                    Log.i(TAG, "查询收藏动态个数：" + object.size());
                } else {
                    getMessageListLinstener.fail(e.getMessage());
                    Log.i(TAG, "查询收藏动态失败：" + e.getMessage());
                }
            }

        });


    }

    /**
     * 当前是否点赞过该评论,点赞数量
     *
     * @param dynamicMessage
     * @param getBoolean
     */
    @Override
    public void GetIsLikers(DynamicMessage dynamicMessage, final GetBoolean getBoolean) {
        BmobQuery<MUser> query = new BmobQuery<MUser>();

        query.addWhereRelatedTo("Likers", new BmobPointer(dynamicMessage));
        query.findObjects(new FindListener<MUser>() {

            @Override
            public void done(List<MUser> object, BmobException e) {
                if (e == null) {
                    getBoolean.ResultNum(object.size());
                    for (MUser u : object) {
                        if (u.getObjectId().equals(GPModel.getCurrentUser(MUser.class).getObjectId())) {
                            getBoolean.Result(true);
                            Log.i(TAG, "done:该用户是否点赞过该评论查询成功，结果：点赞过");
                            return;
                        }
                    }
                    getBoolean.Result(false);
                    Log.i(TAG, "done:该用户是否点赞过该评论查询成功，结果：没点赞过");
                } else {
                    Log.i(TAG, "done:该用户是否点赞过该评论查询失败" + e.getMessage());

                }
            }

        });
    }

    @Override
    public void PostImg(String path, final GetImgPath getImgPath) {
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    getImgPath.success(bmobFile.getFileUrl());
                } else {
                    getImgPath.fail(e.getMessage());
                }

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
                getImgPath.process();
            }
        });
    }

    @Override
    public void CreateNewLabel(String Label, final CreateLabelListener doneListener) {
        if (!Label.equals("")) {
            final Labels labels = new Labels();
            labels.setLabel(Label);
            labels.setLabelHeat(0);
            labels.save(new SaveListener<String>() {

                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        doneListener.success(labels);
                    } else {
                        doneListener.fail(e.getMessage());
                    }
                }
            });

        }
    }

    @Override
    public void FindLabelByStr(String labelname, final GetLabelsLinstener getLabelsLinstener) {
        BmobQuery<Labels> query = new BmobQuery<Labels>();
        query.include("LabelHeat");
        query.addWhereEqualTo("Label",labelname);
        query.setLimit(1);
        query.findObjects(new FindListener<Labels>() {

            @Override
            public void done(List<Labels> object, BmobException e) {
                if (e == null) {
                    getLabelsLinstener.success(object);
                    Log.i(TAG, "获取的标签数" + object.size());
                } else {
                    getLabelsLinstener.fail(e.getMessage());
                    Log.i(TAG, "获取标签失败：" + e.getMessage());
                }
            }

        });
    }

    //通过动态找到其标签
    @Override
    public void FindLabelByDynamicMessage(DynamicMessage dynamicMessage, final GetLabelsLinstener getLabelsLinstener) {
        BmobQuery<Labels> query = new BmobQuery<Labels>();

        query.addWhereRelatedTo("Labels", new BmobPointer(dynamicMessage));
        query.findObjects(new FindListener<Labels>() {

            @Override
            public void done(List<Labels> object, BmobException e) {
                if (e == null) {
                    getLabelsLinstener.success(object);
                    Log.i(TAG, "查询标签：" + object.get(0).getLabel());
                } else {
                    getLabelsLinstener.fail(e.getMessage());
                    Log.i(TAG, "查询标签失败：" + e.getMessage());
                }
            }

        });
    }

    @Override
    public void FindUserByAccount(String account, final GetUsersLinstener getUsersLinstener) {
        BmobQuery<MUser> query = new BmobQuery<MUser>();
        query.include("HeadPortrait");
        query.addWhereEqualTo("username",account);
        query.setLimit(1);
        query.findObjects(new FindListener<MUser>() {

            @Override
            public void done(List<MUser> object, BmobException e) {
                if (e == null) {
                    getUsersLinstener.success(object);
                    Log.i(TAG, "获取的标签数" + object.size());
                } else {
                    getUsersLinstener.fail(e.getMessage());
                    Log.i(TAG, "获取标签失败：" + e.getMessage());
                }
            }

        });
    }

    @Override
    public void FindUserByNickName(String nickname, final GetUsersLinstener getUsersLinstener) {
        BmobQuery<MUser> query = new BmobQuery<MUser>();
        query.include("HeadPortrait");
        query.addWhereEqualTo("NickName",nickname);
        query.setLimit(1);
        query.findObjects(new FindListener<MUser>() {

            @Override
            public void done(List<MUser> object, BmobException e) {
                if (e == null) {
                    getUsersLinstener.success(object);
                    Log.i(TAG, "获取的标签数" + object.size());
                } else {
                    getUsersLinstener.fail(e.getMessage());
                    Log.i(TAG, "获取标签失败：" + e.getMessage());
                }
            }

        });
    }

    /***
     * 获取所有标签
     * @param labelsLinstener
     */
    @Override
    public void GetLabels(final GetLabelsLinstener labelsLinstener) {
        BmobQuery<Labels> query = new BmobQuery<Labels>();
        query.include("LabelHeat");
        query.findObjects(new FindListener<Labels>() {

            @Override
            public void done(List<Labels> object, BmobException e) {
                if (e == null) {
                    labelsLinstener.success(object);
                    Log.i(TAG, "获取的标签数" + object.size());
                } else {
                    labelsLinstener.fail(e.getMessage());
                    Log.i(TAG, "获取标签失败：" + e.getMessage());
                }
            }

        });
    }
    @Override
    public void GetHotLabels(final GetLabelsLinstener labelsLinstener) {
        BmobQuery<Labels> query = new BmobQuery<Labels>();
        query.include("LabelHeat");
        query.setLimit(6);
        query.order("-LabelHeat");
        query.findObjects(new FindListener<Labels>() {

            @Override
            public void done(List<Labels> object, BmobException e) {
                if (e == null) {
                    labelsLinstener.success(object);
                    Log.i(TAG, "获取的标签数" + object.size());
                } else {
                    labelsLinstener.fail(e.getMessage());
                    Log.i(TAG, "获取标签失败：" + e.getMessage());
                }
            }

        });
    }
    /***
     * 注册函数
     * @param account 昵称
     * @param passwords 密码
     * @param registeredListener
     */
    @Override
    public void UserRegistered(final String account, final String passwords, String email, Boolean sex, final DoneListener registeredListener) {

        MUser user = new MUser();
        user.setUsername(account);
        user.setPassword(passwords);
        user.setEmail(email);
        user.setGender(sex);
        user.signUp(new SaveListener<MUser>() {
            @Override
            public void done(MUser user, BmobException e) {
                if (e == null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(account, passwords);
                                Handler mainHandler = new Handler(Looper.getMainLooper());
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //已在主线程中，可以更新UI
                                        registeredListener.success();
                                    }
                                });
                            } catch (final HyphenateException e) {
                                Handler mainHandler = new Handler(Looper.getMainLooper());
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //已在主线程中，可以更新UI
                                        registeredListener.fail(e.toString());
                                    }
                                });
                            }
                        }
                    }).start();
                } else {
                    registeredListener.fail(e.getMessage());
                }
            }
        });

    }

    /***
     *判断邮箱格式是否正确
     *
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }


    public static <T> T getCurrentUser(Class<T> clazz) {
        return BmobUser.getCurrentUser(clazz);
    }

    public void setMdynamicMessage(DynamicMessage mdynamicMessage) {
        this.mdynamicMessage = mdynamicMessage;
    }

    public String getPostheadpicurl() {
        return postheadpicurl;
    }

    public void setPostheadpicurl(String postheadpicurl) {
        this.postheadpicurl = postheadpicurl;
    }
}
