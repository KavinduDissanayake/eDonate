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
import com.example.edonate.Screen.DashBoard.Home.Model.Post;
import com.example.edonate.Screen.DashBoard.PostDetails.PostDetailsActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context mContext;
    List<Post> postList;

    public PostAdapter(Context mContext, List<Post> postList) {
       this.mContext = (Context) mContext;
        this.postList = postList;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from( mContext ).inflate( R.layout.row_post,parent,false );
        final MyViewHolder viewHolder = new MyViewHolder(row) ;


        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, PostDetailsActivity.class);
                i.putExtra("post_id",postList.get(viewHolder.getAdapterPosition()).getId());
                i.putExtra("title",postList.get(viewHolder.getAdapterPosition()).getTitle());
                i.putExtra("des",postList.get(viewHolder.getAdapterPosition()).getDes());
                i.putExtra("post_img",postList.get(viewHolder.getAdapterPosition()).getPostimg());
                i.putExtra("uid",postList.get(viewHolder.getAdapterPosition()).getUid());
                i.putExtra("uname",postList.get(viewHolder.getAdapterPosition()).getUname());
                i.putExtra("uimg",postList.get(viewHolder.getAdapterPosition()).getUimg());
                i.putExtra("time",postList.get(viewHolder.getAdapterPosition()).getTime());
//


                mContext.startActivity(i);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {


                try{
                    if(!postList.get(position).getPostimg().isEmpty()){
                        Picasso.get().load( postList.get( position ).getPostimg() )
                                .networkPolicy( NetworkPolicy.NO_CACHE )
                                .memoryPolicy( MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE )
                                .into( holder.imgPost );
                    }


                    if(!postList.get(position).getUimg().isEmpty()){
                        Picasso.get().load( postList.get( position ).getUimg() )
                                .networkPolicy( NetworkPolicy.NO_CACHE )
                                .memoryPolicy( MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE )
                                .into( holder.post_userPhoto );
                    }
                }catch (Exception e){

                }
        holder.Title.setText(postList.get( position ).getTitle());
        holder.Time.setText(postList.get( position ).getTime());
        holder.Usertype.setText(postList.get( position ).getUtype());




    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    public class MyViewHolder  extends RecyclerView.ViewHolder{

        TextView Title;
        TextView Time;
        TextView Usertype;

        ImageView imgPost;
        ImageView post_userPhoto;

        LinearLayout view_container;


        public MyViewHolder(@NonNull View itemView) {
            super( itemView );

            Title=itemView.findViewById( R.id.post_title );

            Time=itemView.findViewById( R.id.time );

            imgPost=itemView.findViewById( R.id.post_image );
            post_userPhoto=itemView.findViewById( R.id.post_userPhoto );
            view_container = itemView.findViewById(R.id.container);
            Usertype = itemView.findViewById(R.id.usertype);

        }
    }



}
