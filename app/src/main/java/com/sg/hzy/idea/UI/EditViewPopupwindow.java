package com.sg.hzy.idea.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.sg.hzy.idea.R;

/**
 * Created by 胡泽宇 on 2018/11/12.
 */

public class EditViewPopupwindow extends PopupWindow {
    private Context mContext;

    private View view;

    private Button btn_send;


    public EditText et_context;
    private View.OnClickListener itemsOnClick;

    public EditViewPopupwindow(Context Context) {

        this.mContext = Context;

        this.view = LayoutInflater.from(mContext).inflate(R.layout.main_aty_editview_popupwindow, null);

        btn_send =  view.findViewById(R.id.main_aty_editview_popupwindow_btnsend);
        et_context =  view.findViewById(R.id.main_aty_editview_popupwindow_tvcontext);




        // 设置外部可点击
        this.setOutsideTouchable(true);


        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);

        // 设置弹出窗体的宽和高
          /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */

        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        et_context.requestFocus();
        //弹出输入法
        InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }
    public boolean getETcontextisEmpty(){
        return et_context.getText().toString().trim().isEmpty();
    }

    public View.OnClickListener getItemsOnClick() {
        return itemsOnClick;
    }

    public void setItemsOnClick(View.OnClickListener itemsOnClick) {
        this.itemsOnClick = itemsOnClick;
        btn_send.setOnClickListener(itemsOnClick);
    }
}
