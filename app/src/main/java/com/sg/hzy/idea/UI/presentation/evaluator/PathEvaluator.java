package com.sg.hzy.idea.UI.presentation.evaluator;

import android.animation.TypeEvaluator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;

public class PathEvaluator implements TypeEvaluator<PointF> {
    private PathMeasure mPathMeasure;
    private float mPathLength;
    private float points[] = new float[2];
    private PointF mPointF = new PointF();
    public PathEvaluator(Path path){
        mPathMeasure = new PathMeasure(path,false);
        mPathLength = mPathMeasure.getLength();
    }


    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        if(mPathMeasure.getPosTan(mPathLength*fraction,points,null)) {
            mPointF.set(points[0], points[1]);
        }
        return mPointF;
    }
}
