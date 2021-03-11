package com.example.edonate.Screen.DashBoard.Home.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.edonate.R;
import com.example.edonate.Screen.DashBoard.Home.Model.Comment;
import com.example.edonate.Screen.DashBoard.UserInfo.UserActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;



import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {


private Context mContext;
private List<Comment> commentList;

    public CommentAdapter(Context mContext, List<Comment> commentList) {

        this.commentList=commentList;
        this.mContext=mContext;

    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from( mContext ).inflate( R.layout.row_comment ,parent,false);

        final CommentViewHolder viewHolder = new CommentViewHolder(row) ;
        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, UserActivity.class);
                i.putExtra("user_id",commentList.get(viewHolder.getAdapterPosition()).getUid());
                mContext.startActivity(i);
            }
        });
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        holder.comment_uname.setText( commentList.get( position ).getUname() );
        holder.comment_content.setText( commentList.get( position ).getContent() );

        holder.comment_Time.setText( commentList.get( position ).getTime() );



        if(! commentList.get( position ).getUimg().isEmpty() ) {
            Picasso.get().load( commentList.get( position ).getUimg() )
                    .networkPolicy( NetworkPolicy.NO_CACHE )
                    .memoryPolicy( MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE )

                    .into( holder.profile_crUser_comment );
        }

    }

    @Override
    public int getItemCount() { return commentList.size(); }


    public class  CommentViewHolder extends RecyclerView.ViewHolder{
        ImageView profile_crUser_comment;
        TextView comment_uname;
        TextView comment_content;
        TextView comment_Time;

        LinearLayout view_container;

        public CommentViewHolder(@NonNull View itemView) {
         super( itemView );

         profile_crUser_comment=itemView.findViewById( R.id.profile_crUser_comment );
         comment_uname=itemView.findViewById( R.id.comment_uname );
         comment_content=itemView.findViewById( R.id.comment_content );
         comment_Time=itemView.findViewById( R.id.comment_Time );
         view_container = itemView.findViewById(R.id.container1);

     }
 }




}
