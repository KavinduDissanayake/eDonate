package com.example.edonate.Screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.edonate.Connection.OkHttpSingleton;
import com.example.edonate.Connection.URL_OF_DB;
import com.example.edonate.R;
import com.example.edonate.Screen.DashBoard.HomeMainActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {



    public static final String MyPREFERENCES = "user_details" ;
    private EditText username_log, Password;
    private ProgressBar progressBar;
    Button loginbtn;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        init();
        //session
        sharedpreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    private void init() {


        username_log = findViewById(R.id.username_log);
        Password = findViewById(R.id.password_login);
        progressBar = findViewById(R.id.progressBarLogin);
        loginbtn = findViewById(R.id.loginbtn);
    }

    public void Login(View view) {
        //Login Function with database

        String  username = username_log.getText().toString().trim();
        String password = Password.getText().toString().trim();
        Log.d("Register  user name ", username);
        Log.d("Register  pass" , password);

        if (TextUtils.isEmpty(username)) {
            username_log.setError("User name is required !");
            return;
        }
        if(password.length()<8 &&!isValidPassword(password)){
            Password.setError( "Password must be 8 characters" );
            return;
        }
//
        loginbtn.setVisibility( View.INVISIBLE );
        progressBar.setVisibility(View.VISIBLE);

        RequestBody reqestBody= new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //username
                .addFormDataPart("uname",username)
                //password
                .addFormDataPart("pass",password)
                .build();



        Request request = new Request.Builder()
                .url(URL_OF_DB.getInstance().auth)
                .post(reqestBody)
                .build();



        OkHttpSingleton.getInstance().getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {

                    final String myResponse = response.body().string();
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //mTextViewResult.setText(myResponse);
                            try{
                                String JSON_STRING= myResponse;
                                JSONArray mJsonArray = new JSONArray(JSON_STRING);
                                JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                                String success = mJsonObject.getString("success");
                                String name = mJsonObject.getString("name");
                                String img = mJsonObject.getString("img");
                                String id = mJsonObject.getString("id");
                                String utype = mJsonObject.getString("usertype");

//                                Log.d("Login",name);
//                                Log.d("Login",img);
//                                Log.d("Login",id);
                               // Log.e("Login",myResponse);
                                Log.d("Login",success);
                                if(success.equals("true")){
                                    Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.INVISIBLE);

                                    //create sessions ! ----
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("cu_utype", utype);
                                    editor.putString("cu_name", name);
                                    editor.putString("cu_id", id);
                                    editor.putString("cu_img", img);
                                    editor.commit();
                                    startActivity(  new Intent( LoginActivity.this, HomeMainActivity.class ) );
//                                    finish();

                                }else{

                                    loginbtn.setVisibility( View.VISIBLE );
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), "Login failed! invalid username or password", Toast.LENGTH_LONG).show();
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            catch(JSONException e){
                                Toast.makeText(getApplicationContext(), "Login failed!"+e.getMessage(), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                loginbtn.setVisibility( View.VISIBLE );

                            }
                        }
                    });
                }}
        });


    }

    public void goToRegister(View view) {

        startActivity(  new Intent( LoginActivity.this,RegisterActivity.class ) );
        finish();
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}