package com.sg.hzy.idea.DataClass;


import cn.bmob.v3.BmobObject;

/**
 * Created by 胡泽宇 on 2018/12/5.
 */

public class Labels extends BmobObject {
    //标签名
    private String Label;
    //标签热度
    private Integer LabelHeat=0;

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public int getLabelHeat() {
        return LabelHeat;
    }

    public void setLabelHeat(int labelHeat) {
        LabelHeat = labelHeat;
    }
}
