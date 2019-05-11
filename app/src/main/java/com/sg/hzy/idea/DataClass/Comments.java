package com.sg.hzy.idea.DataClass;

import cn.bmob.v3.BmobObject;

/**
 * Created by 胡泽宇 on 2018/10/27.
 *
 * 评论类
 */

public class Comments extends BmobObject {
    //评论的是那条动态
    private DynamicMessage WhichMessage;
    //评论的内容
    private  String Content;
    //评论的发布者
    private MUser author;


    public DynamicMessage getWhichMessage() {
        return WhichMessage;
    }

    public void setWhichMessage(DynamicMessage whichMessage) {
        WhichMessage = whichMessage;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public MUser getAuthor() {
        return author;
    }

    public void setAuthor(MUser author) {
        this.author = author;
    }
}
