package com.sg.hzy.idea.UI;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 胡泽宇 on 2018/11/2.
 */

public class SpacesItemDecroation extends RecyclerView.ItemDecoration {

    // 分割距离
    private int space;

    public SpacesItemDecroation() {

    }

    public SpacesItemDecroation(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space;
        }
    }


}
