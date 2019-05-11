package com.sg.hzy.idea.View.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.sg.hzy.idea.DataClass.Labels;
import com.sg.hzy.idea.MainAty;
import com.sg.hzy.idea.Model.BaseModel;
import com.sg.hzy.idea.Model.GPModel;
import com.sg.hzy.idea.R;
import com.sg.hzy.idea.ReleaseAty;
import com.sg.hzy.idea.UI.presentation.PresentationLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 胡泽宇 on 2018/12/26.
 */

public class LabelsFragment extends android.support.v4.app.Fragment {
     private PresentationLayout mPresentationLayout;
     List<Labels> LabelList;
     List<Labels> AllLabelList;
     Handler handler=new Handler(new Handler.Callback() {
         @Override
         public boolean handleMessage(Message msg) {
             switch (msg.what){
                 case 1:
                     LabelList= (List<Labels>) msg.obj;
                     break;
                 case 2:
                     AllLabelList=(List<Labels>) msg.obj;
                     break;
             }

             return true;
         }
     });
    private String[] labels;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.aty_labels, null);
        mPresentationLayout = (PresentationLayout) view.findViewById(R.id.presentation);
        final List<PresentationLayout.Tag> mSource = new ArrayList<>();
        GPModel.getInstance().GetHotLabels(new BaseModel.GetLabelsLinstener() {
            @Override
            public void fail(String error) {

            }

            @Override
            public void success(List<Labels> userList) {
                Message message=new Message();
                message.obj=userList;
                message.what=1;
                handler.sendMessageAtTime(message,0);
                for(Labels t:userList){
                    PresentationLayout.Tag tag = new PresentationLayout.Tag(t.getLabel(),0);
                    mSource.add(tag);
                }
                PresentationLayout.Tag tag = new PresentationLayout.Tag("更多...",0);
                mSource.add(tag);
                mPresentationLayout.inputTags(mSource);
            }
        });
        mPresentationLayout.performClick();
        mPresentationLayout.setOnClick(new PresentationLayout.TAGOnClick() {
            @Override
            public void OnClick(int which) {
                if(LabelList!=null) {
                    if (which >= LabelList.size()) {
                        Log.i("mPresentationLayout", "OnClick: 更多");
                        ShowFindLabels();
                    } else {
                        //切换
                        Log.i("mPresentationLayout", "OnClick: " + LabelList.get(which).getLabel());
                        GPModel.getInstance().setCheckLabel(LabelList.get(which));
                        MainAty parentActivity = (MainAty ) getActivity();
                        parentActivity.ChangeToMore();
                    }
                }

            }
        });
        return view;
    }

    private void ShowFindLabels() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("查找标签");
        View view = View.inflate(this.getActivity(), R.layout.select_label, null);
        final AutoCompleteTextView autotvlabel = (AutoCompleteTextView) view.findViewById(R.id.select_label_auto);
        GPModel.getInstance().GetLabels(new BaseModel.GetLabelsLinstener() {
            @Override
            public void fail(String error) {

            }

            @Override
            public void success(List<Labels> userList) {
                Message message=new Message();
                message.obj=userList;
                message.what=2;
                handler.sendMessageAtTime(message,0);
                labels = new String[userList.size()];
                for (int i = 0; i < userList.size(); i++) {
                    labels[i] = userList.get(i).getLabel();
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LabelsFragment.this.getActivity(), android.R.layout.simple_dropdown_item_1line, labels);
                autotvlabel.setAdapter(arrayAdapter);

            }
        });
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                int i=0;
                for(i=0;i<AllLabelList.size();i++){
                    if(AllLabelList.get(i).getLabel().equals(autotvlabel.getText().toString())){
                        break;
                    }
                }
               if(i>=AllLabelList.size()){
                   Toast.makeText(LabelsFragment.this.getActivity(), "没有该标签的信息", Toast.LENGTH_SHORT).show();
               }else{
                   GPModel.getInstance().setCheckLabel(AllLabelList.get(i));
                   MainAty parentActivity = (MainAty ) getActivity();
                   parentActivity.ChangeToMore();
               }
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.alpha = 1f;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

}
