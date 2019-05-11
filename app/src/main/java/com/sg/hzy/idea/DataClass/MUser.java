package com.sg.hzy.idea.DataClass;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 胡泽宇 on 2018/10/27.
 * <p>
 * //用户类
 * <p>
 * <p>
 * 用户名
 * // private String username;
 * // 密码
 * // private String password;
 * // 邮箱
 * // private String email;
 * // 邮箱是否认证
 * // private Boolean emailVerified;
 * // private String sessionToken;
 * // 手机号
 * // private String mobilePhoneNumber;
 * // 手机号是否认证
 * // private Boolean mobilePhoneNumberVerified;
 */

public class MUser extends BmobUser {
    //头像
    private BmobFile HeadPortrait;
    //昵称
    private String NickName;

    //性别
    private Boolean Gender;
    //个性签名
    private String Sign;

    private BmobDate Birthday;

    public BmobFile getHeadPortrait() {
        return HeadPortrait;
    }

    public void setHeadPortrait(BmobFile headPortrait) {
        HeadPortrait = headPortrait;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }


    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }


    public BmobDate getBirthday() {
        return Birthday;
    }

    public void setBirthday(BmobDate birthday) {
        Birthday = birthday;
    }

    public Boolean getGender() {
        return Gender;
    }

    public void setGender(Boolean gender) {
        Gender = gender;
    }


}
