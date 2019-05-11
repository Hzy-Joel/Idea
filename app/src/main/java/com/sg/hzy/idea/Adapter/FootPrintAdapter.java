package com.sg.hzy.idea.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sg.hzy.idea.DataClass.DynamicMessage;
import com.sg.hzy.idea.R;
import com.sg.hzy.idea.UI.GlideRoundTransform;
import com.sg.hzy.idea.UI.ZoomOutPageTransformer;
import com.sg.hzy.idea.Utils.ScreenUtils;

import java.util.ArrayList;

/**
 * Created by 胡泽宇 on 2018/11/2.
 */

public class FootPrintAdapter extends RecyclerView.Adapter<FootPrintAdapter.FootPrintViewHolder> {
    ArrayList<DynamicMessage> dynamicMessages;
    Context context;


    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;

    }
    public interface OnItemClickListener{
        void onItemClick(View v,int pos);
        void mLongClick(View v,int pos);
    }
    public void setDynamicMessages(ArrayList<DynamicMessage> dynamicMessages) {
        this.dynamicMessages = dynamicMessages;
    }

    public FootPrintAdapter(Context context) {
        this.context = context;
    }

    @Override
    public FootPrintViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        FootPrintViewHolder holder = new FootPrintViewHolder(LayoutInflater.from(
                context).inflate(R.layout.ryview_foorprint_item, parent,
                false));
        return holder;

    }

    @Override
    public void onBindViewHolder(final FootPrintViewHolder holder, int position) {
        View itemView = ((LinearLayout) holder.itemView).getChildAt(0);
        if (listener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    listener.onItemClick(holder.itemView, position);
                }
            });
        }
        if(dynamicMessages!=null) {
            holder.tv_context.setText(dynamicMessages.get(position).getContent());
            holder.tv_date.setText(dynamicMessages.get(position).getCreatedAt());
            final Vpadapter vpadapter = new Vpadapter();

            holder.vp.setAdapter(vpadapter);
            holder.vp.setPageTransformer(true, new ZoomOutPageTransformer());
            holder.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    holder.ll.getChildAt(vpadapter.mNum).setEnabled(false);
                    holder.ll.getChildAt(position).setEnabled(true);
                    vpadapter.mNum = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            vpadapter.notifyDataSetChanged();
        }

    }

    @Override
    public int getItemCount() {
        if(dynamicMessages!=null) {
            return dynamicMessages.size();
        }else{
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class FootPrintViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date;
        TextView tv_context;
        ViewPager vp;
        LinearLayout ll;


        public FootPrintViewHolder(View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.ryview_foorprint_item_tv_date);
            tv_context = itemView.findViewById(R.id.ryview_foorprint_item_tv_context);
            vp = itemView.findViewById(R.id.ryview_foorpint_item_vp);
            ll = itemView.findViewById(R.id.ryview_foorpint_item_ll);
        }
    }

    class Vpadapter extends PagerAdapter {
        ArrayList<ImageView> pics;
        int mNum = 0;

        public void setPics(ArrayList<String> picpaths, final ViewPager vp) {
            if (picpaths != null) {
                if (pics == null) {
                    pics = new ArrayList<>();
                }
                for (String str : picpaths) {
                    final ImageView imageView = new ImageView(context);
                    Glide.with(context)
                            .load(str)
                            .asBitmap()
                            .transform(new GlideRoundTransform(context, 4))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            float scale = (float) resource.getHeight() / resource.getWidth();
                            DisplayMetrics dm= ScreenUtils.getScreenSize(context);
                            int width=dm.widthPixels;
                            int height=dm.heightPixels;
                            int vpw=width/5*2;
                            ViewPager.LayoutParams para = new ViewPager.LayoutParams();
                            para.height = (int) (vpw*scale);
                            para.width = vpw;
                            imageView.setLayoutParams(para);
                            imageView.setImageBitmap(resource);
                            FrameLayout.LayoutParams para1= (FrameLayout.LayoutParams) vp.getLayoutParams();
                            para1.height=(int)(vpw*scale);
                            para1.width=vpw;
                            vp.setLayoutParams(para1);
                        }
                    });

                    pics.add(imageView);
                }
            }
        }

        @Override
        public int getCount() {
            if (pics != null) {
                return pics.size();
            } else {
                return 0;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (pics != null) {
                container.removeView(pics.get(position));
            }
        }

        @Override
        public ImageView instantiateItem(ViewGroup container, int position) {
            if (pics != null) {
                container.addView(pics.get(position));
                return pics.get(position);
            }
            return null;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return pics.indexOf(object);
        }
    }

}

