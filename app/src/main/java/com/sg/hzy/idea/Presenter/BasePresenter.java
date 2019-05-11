package com.sg.hzy.idea.Presenter;

import java.lang.ref.WeakReference;

/**
 * Created by 胡泽宇 on 2018/4/25.
 */

public class BasePresenter <V>{
    //基础presenter层

    protected WeakReference<V> weakReference;


    //使用弱引用绑定model防止内存泄漏
    public void attchView(V view) {
        weakReference=new WeakReference<V>(view);
    }
    //解除绑定
    public void unattchView(){
        weakReference.clear();
    }
}
