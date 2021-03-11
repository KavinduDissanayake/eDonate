package com.example.edonate.Screen.DashBoard.Home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.edonate.Connection.OkHttpSingleton;
import com.example.edonate.Connection.URL_OF_DB;
import com.example.edonate.R;
import com.example.edonate.Screen.DashBoard.Home.Adapters.PostAdapter;
import com.example.edonate.Screen.DashBoard.Home.Model.Post;

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


public class HomeFragment extends Fragment {
    View root;
    private RecyclerView postRv;
    private PostAdapter postAdapter;

    EditText serach;
    ImageButton serachbtn;
    String title="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate( R.layout.home_fragment, container, false );
        init();
        loadPosts();
        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        loadPosts();
    }


    private void init() {
        postRv=root.findViewById( R.id.post_RV );
        serach=root.findViewById( R.id.serach );
        serachbtn=root.findViewById( R.id.serachbtn );


        serachbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPosts();
            }
        } );
    }

    private void loadPosts() {


        title=serach.getText().toString();
        List<Post> postList = new ArrayList<>() ;
        postList.clear();

        RequestBody reqestBody= new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title",title)
                .build();



        Request request = new Request.Builder()
                .url(URL_OF_DB.getInstance().getPosts)
                .post( reqestBody )
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
                    getActivity().runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            try{
                                String JSON_STRING= myResponse;
                                JSONArray mJsonArray = new JSONArray(JSON_STRING);
                                JSONObject mJsonObject = null;
//                                String success = mJsonObject.getString("cname");


                                //   Log.d("FilterJobs",myResponse);
                                for(int i = 0; i < mJsonArray.length(); i++){
                                    mJsonObject=  mJsonArray.getJSONObject(i);
                                    String id=mJsonObject.getString( "id" );
                                    String title=mJsonObject.getString( "title" );
                                    String des=mJsonObject.getString( "des" );
                                    String postimg=mJsonObject.getString( "postimg" );
                                    String uid=mJsonObject.getString( "uid" );
                                    String uname=mJsonObject.getString( "uname" );
                                    String uimg=mJsonObject.getString( "uimg" );
                                    String time=mJsonObject.getString( "time" );
                                    String usertype=mJsonObject.getString( "usertype" );

                                    Post post = new Post(id,title,des,postimg,uid,uname,uimg,time,usertype);
                                    postList.add( post );

                                }

                                postAdapter = new PostAdapter( getActivity(),postList);
                                postRv.setLayoutManager( new LinearLayoutManager( getActivity() ) );
                                postRv.setAdapter( postAdapter );

                            }
                            catch(JSONException e){
                                Log.e("FilterJobs",e.toString());
                                Toast.makeText(getActivity(), "No Result ! \n  try again "+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
                else {
                    Log.d("Res","Error");
                }
            }
        });

    }



}