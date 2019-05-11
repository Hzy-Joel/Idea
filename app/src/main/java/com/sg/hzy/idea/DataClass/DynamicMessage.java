package com.sg.hzy.idea.DataClass;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by 胡泽宇 on 2018/10/27.
 *
 * 创建时间
 *   private String createdAt;
 */

public class DynamicMessage extends BmobObject {
    //动态的发布者
    private MUser User ;
    //动态的标签
    private BmobRelation Labels;
    //动态的标题
    private String Title;
    //动态的内容
    private String TextContent;
    private String Content;
    //喜欢这个动态的读者
    private String FirstPicurl;
    private BmobRelation Likers;

    public MUser getUser() {
        return User;
    }

    public void setUser(MUser user) {
        User = user;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public BmobRelation getLikers() {
        return Likers;
    }

    public void setLikers(BmobRelation likers) {
        this.Likers = likers;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public BmobRelation getLabels() {
        return Labels;
    }

    public void setLabels(BmobRelation labels) {
        Labels = labels;
    }

    public String getTextContent() {
        return TextContent;
    }

    public void setTextContent(String textContent) {
        TextContent = textContent;
    }


    public String getFirstPicurl() {
        return FirstPicurl;
    }

    public void setFirstPicurl(String firstPicurl) {
        FirstPicurl = firstPicurl;
    }
}
