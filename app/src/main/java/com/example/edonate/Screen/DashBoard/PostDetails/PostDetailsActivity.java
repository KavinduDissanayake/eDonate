package com.example.edonate.Screen.DashBoard.PostDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.edonate.Connection.OkHttpSingleton;
import com.example.edonate.Connection.URL_OF_DB;
import com.example.edonate.R;
import com.example.edonate.Screen.DashBoard.Home.Adapters.CommentAdapter;
import com.example.edonate.Screen.DashBoard.Home.Model.Comment;
import com.example.edonate.Screen.DashBoard.UserInfo.UserActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostDetailsActivity extends AppCompatActivity {
    ImageView headerImage;
    ImageView profile_anime;
    ImageView profile_crUser;
    TextView description;
    TextView Time;
    TextView user_name;
    Button button;

    EditText edit_comment;
    RecyclerView commentRV;
    SharedPreferences prf;

    String post_id;
    String c_uid="1";
    String c_uname="";
    String c_uimg="";

    String postUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_details_activity);


        init();
        //sessions set
        prf = getSharedPreferences("user_details",MODE_PRIVATE);
//        c_uid=  prf.getString("cu_id",null);
//        c_uname=  prf.getString("cu_name",null);
//        c_uimg=  prf.getString("cu_img",null);
        catchData();
        getComments();
    }

    private void getComments() {
        List<Comment> commentList= new ArrayList<>(  );

        RequestBody reqestBody= new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("postid",post_id)
                .build();

        Request request = new Request.Builder()
                .url(URL_OF_DB.getInstance().getcomments)
                .post(reqestBody)
                .build();
        OkHttpSingleton.getInstance().getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    PostDetailsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //mTextViewResult.setText(myResponse);
                            try{
                                String JSON_STRING= myResponse;
                                JSONArray mJsonArray = new JSONArray(JSON_STRING);
                                JSONObject mJsonObject = null;
//                                String success = mJsonObject.getString("cname");


                                Log.e("FilterJobs",myResponse);
                                for(int i = 0; i < mJsonArray.length(); i++){
                                    mJsonObject=  mJsonArray.getJSONObject(i);
                                    Log.d( "Commets",mJsonObject.getString( "id" ) );
                                    String  content=mJsonObject.getString( "content" );
                                    String  puimg=mJsonObject.getString( "puimg" );
                                    String  puname=mJsonObject.getString( "puname" );
                                    String  puid=mJsonObject.getString( "puid" );
                                    String  time=mJsonObject.getString( "time" );

                                    Comment comment= new Comment( content,puid,puimg,puname ,time);
                                    commentList.add( comment );

                                }

                                //setup adapter view

                                CommentAdapter commentAdapter= new CommentAdapter(PostDetailsActivity.this,commentList);
                                commentRV.setLayoutManager( new LinearLayoutManager( PostDetailsActivity.this ) );
                                commentRV.setAdapter( commentAdapter );
                            }
                            catch(JSONException e){
                                Log.e("FilterJobs",e.toString());
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
                else {
                    Log.d("FilterJobs","Error");
                }
            }
        });
    }

    private void catchData() {

        post_id = getIntent().getExtras().getString("post_id") ;
        String title = getIntent().getExtras().getString("title") ;
        String desc = getIntent().getExtras().getString("des") ;
        //posted user id
        postUid = getIntent().getExtras().getString("uid") ;


        String post_img = getIntent().getExtras().getString("post_img") ;
        String uname = getIntent().getExtras().getString("uname") ;
        String uimg = getIntent().getExtras().getString("uimg") ;
        String time = getIntent().getExtras().getString("time") ;

        final CollapsingToolbarLayout collapsingToolbarLayout=findViewById( R.id.collapsingToolbarLayout1 );
        collapsingToolbarLayout.setTitleEnabled( true );
        collapsingToolbarLayout.setTitle(title );

        description.setText( desc );
        Time.setText( time );
        user_name.setText( uname );


        try {


            if(! post_img.isEmpty()) {
                Picasso.get().load( post_img )
                        .networkPolicy( NetworkPolicy.NO_CACHE)
                        .memoryPolicy( MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                        .resize( 400, 400 )
                        .into( headerImage );
            }


            if(! uimg.isEmpty()) {
                Picasso.get().load( uimg )
                        .networkPolicy( NetworkPolicy.NO_CACHE)
                        .memoryPolicy( MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                        .resize( 400, 400 )
                        .into( profile_anime );
            }

            if(! c_uimg.isEmpty()) {
                Picasso.get().load( c_uimg )
                        .networkPolicy( NetworkPolicy.NO_CACHE)
                        .memoryPolicy( MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                        .resize( 400, 400 )
                        .into( profile_crUser );
            }


        }catch (Exception e){

        }


    }

    private void init() {
        headerImage=findViewById( R.id.headerImage );
        profile_anime=findViewById( R.id.profile_anime );
        profile_crUser=findViewById( R.id.profile_crUser );
        edit_comment=findViewById( R.id.edit_comment );
        button=findViewById( R.id.button );
        description=findViewById( R.id.description );
        Time=findViewById( R.id.time );
        commentRV=findViewById( R.id.commentRV );
        user_name=findViewById( R.id.user_name );

//        Toolbar toolbar=findViewById( R.id.toolbaranim );
//        setSupportActionBar( toolbar );
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    public void addComment(View view) {

        String comment =edit_comment.getText().toString();
        button.setVisibility( View.INVISIBLE );

        OkHttpClient client = new OkHttpClient();

        RequestBody reqestBody = new MultipartBody.Builder()
                .setType( MultipartBody.FORM )
                .addFormDataPart( "content", comment )
                .addFormDataPart( "puimg", c_uimg )
                .addFormDataPart( "puname", c_uname )
                .addFormDataPart( "postid", post_id )
                .addFormDataPart( "puid", c_uid )
                .build();

        Request request = new Request.Builder()
                .url( URL_OF_DB.getInstance().addcomments )
                .post( reqestBody )
                .build();



        client.newCall( request ).enqueue( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    final String myResponse = response.body().string();
                    PostDetailsActivity.this.runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String JSON_STRING = myResponse;
                                JSONArray mJsonArray = new JSONArray( JSON_STRING );
                                JSONObject mJsonObject = mJsonArray.getJSONObject( 0 );
                                String success = mJsonObject.getString( "success" );
                                Log.e( "Add Comment", myResponse );
                                Log.d( "Add Comment", success );
                                if (success.equals( "true" )) {
                                    Toast.makeText( PostDetailsActivity.this, " Commented Success! ", Toast.LENGTH_LONG ).show();
                                } else {
                                    Toast.makeText(PostDetailsActivity.this, " failed! Please try again later", Toast.LENGTH_LONG ).show();
                                }

                                button.setVisibility( View.VISIBLE );
                            } catch (JSONException e) {
                                Log.e( "Add Comment", e.toString() );
                                Toast.makeText( PostDetailsActivity.this, e.toString(), Toast.LENGTH_LONG ).show();
                                button.setVisibility( View.VISIBLE );
                            }
                        }
                    } );
                } else {
                    Log.d( "Res", "Error" );
                }
            }
        } );
    }

    public void goToUserProfile(View view) {

        Intent i = new Intent(PostDetailsActivity.this, UserActivity.class);
        i.putExtra("user_id",postUid);
        startActivity(i);
    }
}