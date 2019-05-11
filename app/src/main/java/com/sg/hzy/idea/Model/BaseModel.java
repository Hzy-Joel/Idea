package com.sg.hzy.idea.Model;

import com.sg.hzy.idea.DataClass.Comments;
import com.sg.hzy.idea.DataClass.DynamicMessage;
import com.sg.hzy.idea.DataClass.Labels;
import com.sg.hzy.idea.DataClass.MUser;

import java.util.List;

import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by 胡泽宇 on 2018/10/27.
 */

public interface BaseModel {
    /***
     * 回调接口
     */
    interface GetBoolean {
        void Result(Boolean r);

        void ResultNum(int num);
    }
    interface GetHtml {

        //任务执行中
        void process();

        //任务成功执行
        void success(StringBuilder html);
    }
    interface GetImgPath {
        //任务失败
        void fail(String error);

        //任务执行中
        void process();

        //任务成功执行
        void success(String uripath);
    }

    interface DoneListener {
        //任务失败
        void fail(String error);

        //任务执行中
        void process();

        //任务成功执行
        void success();
    }
    interface CreateLabelListener {
        //任务失败
        void fail(String error);

        //任务执行中
        void process();

        //任务成功执行
        void success(Labels labels);
    }

    interface GetUsersLinstener {
        void fail(String error);

        void success(List<MUser> userList);
    }

    interface GetLabelsLinstener {
        void fail(String error);

        void success(List<Labels> userList);
    }

    interface GetFriendsLinstener {
        void fail(String error);
        void over();
        void success(List<MUser> userList, List<String> id);
    }

    interface GetCommentsLinstener {
        void fail(String error);

        void success(List<Comments> commentsList);
    }

    interface GetMessageListLinstener {
        void fail(String error);

        void success(List<DynamicMessage> dyanmicMessages);
    }

    void GetLabels(GetLabelsLinstener labelsLinstener);
    void GetHotLabels(GetLabelsLinstener labelsLinstener);
    /***
     * 调用此函数注册用户
     * @param account 昵称
     * @param passwords 密码
     */
    void UserRegistered(String account, String passwords, String email, Boolean Sex, DoneListener registeredListener);

    /***
     * @param email 邮箱
     * @param phone 手机号
     */
    void UserRegistered(String account, String email, String phone, DoneListener registeredListener);

    /***
     * 登陆密
     * @param account 账户
     * @param passwords 密码
     */
    void Login(String account, String passwords, DoneListener doneListener);

    /***
     * 获取用户的动态列表
     */
    void GetMessageList(MUser user, GetMessageListLinstener getMessageList);
    void GetAllDynamicMessage(GetMessageListLinstener getMessageListLinstener);
    /***
     *
     *
     *
     * @param getMessageList
     */
    void GetMessageListByLabels(Labels label, GetMessageListLinstener getMessageList);

    /***
     * 通过动态找到消息的评论
     * @param dyanmicMessage
     * @param getComments
     */
    void GetCommentsByDyanmicMessage(DynamicMessage dyanmicMessage, GetCommentsLinstener getComments);

    /***
     *
     * 发布动态
     * @param textcontext
     * @param Content
     *
     *
     */

    void PostDyanmicMessage(String title, StringBuilder textcontext, String Content, Labels labels, DoneListener doneListener);

    /***
     * 发布评论
     * @param dyanmicMessage 评论的动态
     * @param Context 评论的内容
     * @param doneListener
     */
    void PostComments(DynamicMessage dyanmicMessage, String Context, DoneListener doneListener);

    /***
     * 收藏动态
     * @param dynamicMessage
     * @param doneListener
     */
    void LikeDyanmicMessage(DynamicMessage dynamicMessage, DoneListener doneListener);

    /***
     * 更新头像和昵称
     * @param path 头像路径
     * @param Nickname  昵称
     * @param doneListener
     */
    void SetHeadPortraitAndNickName(String path, String Nickname, String sign, Boolean sex, BmobDate birthday, String email, DoneListener doneListener);

    /***
     * 添加好友功能
     * @param who
     * 理由
     * @param reson
     * \
     */
    void AddFriends(MUser who, String reson, DoneListener doneListener);

    /***
     * 删除好友
     * @param who
     * @param doneListener
     */
    void DeleteFriend(MUser who, DoneListener doneListener);

    /***
     * 获取好友列表
     */
    void FindFrineds(GetFriendsLinstener getFriendsListener);

    /**
     * 通过动态获取收藏这条动态的用户
     *
     * @param dynamicMessage
     * @param getUsers
     */
    void FindLikersByDynamicMessage(DynamicMessage dynamicMessage, GetUsersLinstener getUsers);


    /***
     * 保存用户修改信息
     * @param user
     * @param path  头像路径
     * @param doneListener
     */
    void SaveUserMessage(MUser user, String path, DoneListener doneListener);

    /**
     * 获取用户喜欢的动态
     *
     * @param user
     * @param getMessageListLinstener
     */
    void GetLikesDynamicMessageListByUsers(MUser user, GetMessageListLinstener getMessageListLinstener);


    /***
     *
     */
    void GetIsLikers(DynamicMessage dynamicMessage, GetBoolean getBoolean);

    void PostImg(String path,GetImgPath getImgPath);
    void CreateNewLabel(String Label,CreateLabelListener doneListener);
    void FindLabelByStr(String labelname,GetLabelsLinstener getLabelsLinstener);
    void FindLabelByDynamicMessage(DynamicMessage dynamicMessage,GetLabelsLinstener getLabelsLinstener);
    void FindUserByAccount(String account,GetUsersLinstener getUsersLinstener);

    void FindUserByNickName(String nickname,GetUsersLinstener getUsersLinstener);
}
