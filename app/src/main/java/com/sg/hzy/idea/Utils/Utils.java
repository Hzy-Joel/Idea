package com.sg.hzy.idea.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 胡泽宇 on 2018/11/2.
 */

public  class Utils {
    static String TAG="Utils";
    /***
     * 将bmobFiles文件转化为uri路径
     * @param bmobFiles
     * @return
     */
    public static List<String> BmobFilesToStrings(List<BmobFile> bmobFiles)
    {
       List<String> strings=new ArrayList<>();
       if(bmobFiles==null){
           return null;
       }
        for(BmobFile file:bmobFiles){
            strings.add(file.getFileUrl());
           // Log.i(TAG, "BmobFilesToStrings: "+file.getFileUrl());
        }
        return strings;
    };
}
