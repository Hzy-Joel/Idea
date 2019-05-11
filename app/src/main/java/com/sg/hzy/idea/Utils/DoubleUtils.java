package com.sg.hzy.idea.Utils;

import java.math.BigDecimal;

/**
 * Created by 胡泽宇 on 2018/11/19.
 */

public class DoubleUtils {
    public static double changedouble(double s){
        BigDecimal bg = new BigDecimal(s);
        double f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    };
}
