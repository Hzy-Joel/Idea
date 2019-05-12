package com.sg.hzy.idea.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sg.hzy.idea.Model.GPModel;
import com.sg.hzy.idea.DataClass.Comments;
import com.sg.hzy.idea.View.Activity.FoorPrintAty;
import com.sg.hzy.idea.R;
import com.sg.hzy.idea.UI.CircleImageView;


import java.util.ArrayList;

/**
 * Created by 胡泽宇 on 2018/11/11.
 */

public class MainRecycleItemCommentsAdapter extends RecyclerView.Adapter <MainRecycleItemCommentsAdapter.MainRecycleItemCommentsAdapterViewHolder>{
    Context mcontext;
    ArrayList<Comments> comments;
    public MainRecycleItemCommentsAdapter(Context context){
        mcontext=context;
    }
    public void setComments(ArrayList<Comments> comments) {
        this.comments = comments;
    }
    @Override
    public MainRecycleItemCommentsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MainRecycleItemCommentsAdapterViewHolder holder = new MainRecycleItemCommentsAdapterViewHolder(LayoutInflater.from(
                mcontext).inflate(R.layout.item_main_content_item_commentlist, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MainRecycleItemCommentsAdapterViewHolder holder, final int position) {
       if(comments!=null) {
           holder.tvautoor.setText(comments.get(position).getAuthor().getNickName()!=null?comments.get(position).getAuthor().getNickName()+":":"未命名用户：");
           holder.tvcontext.setText(comments.get(position).getContent());
           holder.tvdate.setText(comments.get(position).getCreatedAt());
           holder.tvautoor.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   GPModel.getInstance().setmUser(comments.get(position).getAuthor());
                   Intent intent=new Intent(mcontext, FoorPrintAty.class);
                   mcontext.startActivity(intent);
               }
           });
           String HeadPic=comments.get(position).getAuthor().getHeadPortrait()!=null?comments.get(position).getAuthor().getHeadPortrait().getFileUrl():null;
           if (HeadPic != null) {
               Glide.with(mcontext)
                       .load(HeadPic)
                       .asBitmap()
                       .error(R.drawable.head_portrait_deafult)
                       .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                       .into(holder.cvhead);

           }
       }
    }

    @Override
    public int getItemCount() {
        if(comments!=null) {
            return comments.size();
        }else{
            return 0;
        }
    }



    class MainRecycleItemCommentsAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView tvautoor;
        TextView tvcontext;
        TextView tvdate;
        CircleImageView cvhead;
        public MainRecycleItemCommentsAdapterViewHolder(View itemView) {
            super(itemView);
            tvautoor = itemView.findViewById(R.id.main_aty_item_context_item_commentslist_tvauthor);
            tvcontext = itemView.findViewById(R.id.main_aty_item_context_item_commentslist_tvsays);
            tvdate = itemView.findViewById(R.id.main_aty_item_context_item_commentslist_tvdate);
            cvhead=itemView.findViewById(R.id.main_aty_item_context_item_commentslist_cv);
        }
    }

}
