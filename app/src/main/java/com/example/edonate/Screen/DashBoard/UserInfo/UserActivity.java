package com.example.edonate.Screen.DashBoard.UserInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edonate.Connection.OkHttpSingleton;
import com.example.edonate.Connection.URL_OF_DB;
import com.example.edonate.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserActivity extends AppCompatActivity {
    TextView userTypePro,contactPro,emailPro,userNamePro;
    ImageView profile;

    String cnumber;

    private static final int REQEST_CALL=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);
        Intent i = getIntent();
        String cid=i.getStringExtra( "user_id" );

        Log.d("user Id ",cid);
        loadUsers(cid);
        init();
    }

    private void loadUsers(String cid) {

        RequestBody reqestBody= new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("cid",cid)
                .build();


        Request request = new Request.Builder()
                .url(URL_OF_DB.getInstance().loadUsers)
                .post(reqestBody)
                .build();


        OkHttpSingleton.getInstance().getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    UserActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                String JSON_STRING= myResponse;
                                JSONArray mJsonArray = new JSONArray(JSON_STRING);
                                JSONObject mJsonObject = mJsonArray.getJSONObject(0);

                                //Log.e( "User Details",myResponse );
                                String name = mJsonObject.getString("name");
                                String email = mJsonObject.getString("email");
                                cnumber = mJsonObject.getString("cnumber");
                                String usertype = mJsonObject.getString("usertype");
                                String img = mJsonObject.getString("img");


                                userTypePro.setText( usertype );
                                contactPro.setText( cnumber );
                                emailPro.setText( email );
                                userNamePro.setText( name);


                                try {
                                    if(!img.isEmpty()){
                                        Picasso.get().load(img)
                                                .networkPolicy( NetworkPolicy.NO_CACHE)
                                                .memoryPolicy( MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                                                .resize(400, 400)
                                                .into(profile);

                                    }
                                }catch (Exception e){

                                }


                                final CollapsingToolbarLayout collapsingToolbarLayout=findViewById( R.id.collapsingToolbarLayoutUser );
                                collapsingToolbarLayout.setTitleEnabled( true );
                                collapsingToolbarLayout.setTitle(name );

                            }
                            catch(JSONException e){
                                Toast.makeText( UserActivity.this, e.toString(), Toast.LENGTH_LONG ).show();
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

    private void init() {

        profile=findViewById( R.id.user );
        userTypePro=findViewById( R.id.userTypePro );
        contactPro=findViewById( R.id.contactPro );
        emailPro=findViewById( R.id.emailPro );
        userNamePro=findViewById( R.id.userNamePro );
    }

    public void callUser(View view) {
        if(!cnumber.isEmpty()) {
            Intent callinent = new Intent( Intent.ACTION_CALL );
            final String tel = "tel:" + cnumber;
            Log.d( "Calling", tel );
            callinent.setData( Uri.parse( tel ) );
            if (ActivityCompat.checkSelfPermission( UserActivity.this,
                    Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( UserActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQEST_CALL );
            } else {
                startActivity( callinent );
            }
        }
    }
}