package com.sg.hzy.idea.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hyphenate.easeui.widget.EaseImageView;
import com.sg.hzy.idea.DataClass.Comments;
import com.sg.hzy.idea.DataClass.DynamicMessage;
import com.sg.hzy.idea.Model.BaseModel;
import com.sg.hzy.idea.Model.GPModel;
import com.sg.hzy.idea.R;
import com.sg.hzy.idea.Utils.ImageUtils;
import com.sg.hzy.idea.Utils.ScreenUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 胡泽宇 on 2018/12/7.
 */

public class DynamicMessageListRAdapter extends RecyclerView.Adapter<DynamicMessageListRAdapter.DynamicMessageListRAdapterViewHolder> {
    Context mcontext;
    private ArrayList<DynamicMessage> dynamicMessages;
    OnClicker onClicker;
    //图片宽高
    int ivWidth;
    int ivHeight;

    int Type_foot=-3;

//    //将dp转化为int
//    ivWidth=((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, mcontext.getResources().getDisplayMetrics()));
//
//    //120dp的高度
//    ivHeight=((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, mcontext.getResources().getDisplayMetrics()));

    @Override
    public DynamicMessageListRAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        DynamicMessageListRAdapterViewHolder holder = new DynamicMessageListRAdapterViewHolder(LayoutInflater.from(
                mcontext).inflate(R.layout.dms_ryview_item, parent,
                false));
        return holder;
    }

    public void setOnClicker(OnClicker onClicker) {
        this.onClicker = onClicker;
    }

    public void setDynamicMessages(ArrayList<DynamicMessage> dynamicMessages) {
        this.dynamicMessages = dynamicMessages;
    }

    public ArrayList<DynamicMessage> getDynamicMessages() {
        return dynamicMessages;
    }

    @Override
    public void onBindViewHolder(final DynamicMessageListRAdapterViewHolder holder, final int position) {
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClicker.OnClick(position);
            }
        });
        holder.Title.setText(dynamicMessages.get(position).getTitle());
        // holder.Context.loadDataWithBaseURL(null, dynamicMessages.get(position).getContent(), "text/html", "utf-8", null);
        holder.Context.setText(dynamicMessages.get(position).getTextContent());
        holder.date.setText(dynamicMessages.get(position).getCreatedAt());
        //时间戳转换
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long time=simpleDateFormat.parse(dynamicMessages.get(position).getCreatedAt()).getTime();
            holder.date.setText(simpleDateFormat.format(new Date(time)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GPModel.getInstance().GetIsLikers(dynamicMessages.get(position), new BaseModel.GetBoolean() {
            @Override
            public void Result(Boolean r) {
                if (r) {
                    holder.islike.setBackground(mcontext.getResources().getDrawable(R.drawable.love_after));
                    holder.islike.setClickable(false);
                }
            }

            @Override
            public void ResultNum(int num) {
                holder.likenum.setText("共有"+num+"个人收藏");
            }
        });
        String HeadPic = dynamicMessages.get(position).getFirstPicurl();


        //设置评论列表
        GPModel.getInstance().GetCommentsByDyanmicMessage(dynamicMessages.get(position), new BaseModel.GetCommentsLinstener() {
            @Override
            public void fail(String error) {

            }

            @Override
            public void success(List<Comments> commentsList) {

                if (commentsList != null) {
                    holder.commnetsnum.setText("共有" + commentsList.size() + "条评论");
                } else {

                }
            }
        });

        if (HeadPic != null) {
            Log.i("DynamicMessageList", HeadPic);
            Glide.with(mcontext)
                    .load(HeadPic)
                    .asBitmap()
                    .error(R.drawable.head_portrait_deafult)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                           if(resource!=null) {
                                //如果图片宽度不够，先扩大
                               if(resource.getWidth()<ivWidth){
                                   resource= ImageUtils.zoomImg(resource,ivWidth,ivWidth/resource.getWidth()*resource.getHeight());
                               }
                               //如果图片的高度大于阈值，截取一部分显示(中心截取)
                               if(resource.getHeight()>ivHeight){
                                   resource=Bitmap.createBitmap(resource,resource.getWidth()/2-ivWidth/2,resource.getHeight()/2-ivHeight/2,ivWidth,ivHeight);
                               }
                               holder.ivfirstpic.setImageBitmap(resource);
                           }
                        }
                    });
        }


    }

    @Override
    public long getItemId(int position)

    {
        return position;
    }

    public DynamicMessageListRAdapter(Context context) {
        this.mcontext = context;
        ScreenUtils.setScreen(context);
        //宽度为5/7;
        ivWidth=ScreenUtils.WIDTH;
        //高度为1/4;
        ivHeight=ScreenUtils.HEIGHT/4;
    }

    @Override
    public int getItemCount() {
        if(dynamicMessages!=null) {
            return dynamicMessages.size();
        }else {
            return 0;
        }
    }

    public void addDynamicMessages(ArrayList<DynamicMessage> dyanmicMessages) {

        this.dynamicMessages.addAll(dyanmicMessages);


    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    class DynamicMessageListRAdapterViewHolder extends ViewHolder {
        TextView Title;
        TextView Context;
        ImageView islike;
        TextView likenum;
        LinearLayout ll;
        EaseImageView ivfirstpic;
        TextView date;
        TextView commnetsnum;
        public DynamicMessageListRAdapterViewHolder(View itemView) {
            super(itemView);
            ll = itemView.findViewById(R.id.dms_ryview_item_ll);
            Title = itemView.findViewById(R.id.dms_ryview_item_tv_title);
            Context = itemView.findViewById(R.id.dms_ryview_item_contextview);
            islike = itemView.findViewById(R.id.dms_ryview_item_imglike);
            likenum = itemView.findViewById(R.id.dms_ryview_item_likenum);
            ivfirstpic = itemView.findViewById(R.id.dms_ryview_item_iv_pic);
            date=itemView.findViewById(R.id.dms_ryview_item_tv_date);
            commnetsnum=itemView.findViewById(R.id.dms_ryview_item_comments_num);
        }


    }

    public interface OnClicker {
        void OnClick(int postion);
    }
}
