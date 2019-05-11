package com.sg.hzy.idea.UI.presentation;

import android.graphics.PointF;
import android.util.Property;


class TagViewProperty extends Property<TagView,PointF>{

    /**
     * @param type
     * @param name
     */
    public TagViewProperty(Class<PointF> type, String name) {
        super(type, name);
    }

    @Override
    public PointF get(TagView tagView) {
        return tagView.getPoint();
    }

    @Override
    public void set(TagView tagView, PointF value) {
        tagView.setPoint(value);
    }
}
