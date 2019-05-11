package com.sg.hzy.idea.UI.presentation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.sg.hzy.idea.UI.presentation.utils.Util;


/**
 * 用于显示标签(TextView 并绘制圆形背景)
 * 有3种大小 根据内容的长度计算 长度最长为5个字符
 * 1:一个字符
 * 2:三个字符
 * 3:4~5个字符
 */
class TagView extends View {
    private int originPaddingLeft;
    private int originPaddingRight;
    private int originPaddingTop;
    private int originPaddingBottom;

    private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private Paint mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mTextSize;
    private int mTextColor;
    private int mBgColor;
    private int mBorderColor;
    private int mBorderWidth;
    PresentationLayout.Tag tag;
    private StaticLayout mStaticLayout;
    private Rect mBounds = new Rect();
    private float mRadius;
    boolean shouldWander = true;

    public TagView(Context context) {
        super(context);
        init();
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        mTextPaint.setTextSize(mTextSize);
//        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setStyle(Paint.Style.FILL);              //填充内部...
        mBgPaint.setColor(0x88888888);
        mBorderPaint.setStyle(Paint.Style.STROKE);          //描边...
    }

    public void initOriginPadding(int left,int top,int right, int bottom){
        originPaddingBottom = bottom;
        originPaddingLeft = left;
        originPaddingRight = right;
        originPaddingTop = top;
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
    }

    void setSource(PresentationLayout.Tag tag){
        this.tag = tag;
        String summary = tag.getTag();
        /*int count = tag.getCount();
        String countWithBracket;
        if(count<=99){
            countWithBracket = "("+count+")";
        }else {
            countWithBracket = "(99+)";
        }
        String source;
        int width;
        if(summary.length()<=3){
            source = summary+"\n"+countWithBracket;
            width = (int) Math.max(mTextPaint.measureText(countWithBracket),mTextPaint.measureText(summary));

        }else {
            source = summary.substring(0,2)+"\n"+summary.substring(2)+"\n"+countWithBracket;
            width = (int) Math.max(mTextPaint.measureText(countWithBracket),mTextPaint.measureText(summary.substring(0,3)));
        }*/

        String source;
        int width;
        if(summary.length()<=3){
            source = summary;
            width = (int)mTextPaint.measureText(summary);

        }else {
            source = summary.substring(0, 2) + "\n" + summary.substring(2);
            width = (int) mTextPaint.measureText(summary.substring(0, 3));
        }
        mStaticLayout = new StaticLayout(source,mTextPaint,width, Layout.Alignment.ALIGN_CENTER,1f,0,false);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(mStaticLayout == null){
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        }else {//不关心parent的属性 来测量高宽
            int width = mStaticLayout.getWidth() + originPaddingLeft + originPaddingRight;
            int height = mStaticLayout.getHeight() + originPaddingTop + originPaddingBottom;
            int diameter = (int) Math.hypot(width, height);     //圆内直角三角形的斜边是直径...
            mRadius = 0.5f*diameter;
            int paddingLeft = (diameter - width) / 2 + originPaddingLeft;
            int paddingRight = (diameter - width) / 2 + originPaddingRight;
            int paddingTop = (diameter - height) / 2 + originPaddingTop;
            int paddingBottom = (diameter - height) / 2 + originPaddingBottom;
            //将控制点设为中心 方便之后动画效果的展示
            setPivotX(mRadius);
            setPivotY(mRadius);
            //设置新的padding大小保证圆形背景的绘制
            setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom);
            setMeasuredDimension(diameter, diameter);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();          //用来保存Canvas的状态,save()方法之后的代码，能够调用Canvas的平移、放缩、旋转、裁剪等操作...

        /*  public void drawCircle (float cx, float cy, float radius, Paint paint);
            cx：圆心的x坐标。
            cy：圆心的y坐标。
            radius：圆的半径。
            paint：绘制时所使用的画笔
        */

        //绘制圆形背景...
        canvas.drawCircle(mRadius,mRadius,mRadius-mBorderWidth,mBgPaint);
        //绘制圆形边框...
        canvas.drawCircle(mRadius,mRadius,mRadius-mBorderWidth,mBorderPaint);
        canvas.translate(getPaddingLeft(),getPaddingTop());   //把当前画布的原点移到(PaddingLeft,PaddingTop),后面的操作都以(10,PaddingTop)作为参照点，默认原点为(0,0)...
        //绘制文字...
        mStaticLayout.draw(canvas);         //用来恢复Canvas之前保存的状态,防止save()方法代码之后对Canvas运行的操作...
        canvas.restore();
    }


    /**
     * 设置背景颜色
     * @param color
     */
    public void setBgColor(int color){
        mBgColor = color;
        Drawable bg = getBackground();
        if(bg instanceof ShapeDrawable){            //判断其左边对象是否为其右边类的实例，返回boolean类型的数据
            ((ShapeDrawable) bg).getPaint().setColor(mBgColor);
        }else {
            ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
            shapeDrawable.getPaint().setColor(mBgColor);
            Util.setBackground( this,shapeDrawable);
        }
    }

    /**
     * 获取背景颜色
     * @return
     */
    public int getBgColor(){
        return  mBgColor;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        mTextSize = textSize;
        mTextPaint.setTextSize(textSize);
        invalidate();           //刷新重新绘制...
    }

    void innerInit(int textColor,int textSize,int bgColor,int borderColor,int borderWidth){
        mTextColor = textColor;
        mTextSize = textSize;
        mBgColor = bgColor;
        mBorderColor = borderColor;
        mBorderWidth = borderWidth;
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        mBgPaint.setColor(bgColor);
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStrokeWidth(borderWidth);
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        mTextPaint.setColor(mTextColor);
        invalidate();
    }

    void setPoint(PointF pointF){
        float left = pointF.x - getWidth()/2;
        float top = pointF.y - getHeight()/2;
        setX(left);
        setY(top);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
    }

    private PointF mPointF = new PointF();
    PointF getPoint(){
        mPointF.set(getLeft()+getWidth()/2,getTop()+getHeight()/2);
        return mPointF;
    }
}
