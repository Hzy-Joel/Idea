package com.sg.hzy.idea.Presenter;


import com.sg.hzy.idea.Model.GPModel;
import com.sg.hzy.idea.View.UiSetData;

/**
 * Created by 胡泽宇 on 2018/4/25.
 */

public  class SetTvPresenter<V extends UiSetData>extends BasePresenter<V>{
    GPModel getDataMobel;
    public SetTvPresenter(){
        getDataMobel=new GPModel();
    }
    public  void SetData() {
        if (weakReference.get() != null) {


        }
    }

}
