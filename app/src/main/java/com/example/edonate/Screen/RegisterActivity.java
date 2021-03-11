package com.example.edonate.Screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.edonate.Connection.OkHttpSingleton;
import com.example.edonate.Connection.URL_OF_DB;
import com.example.edonate.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static final String[] paths = {"donor", "recipient"};
    private static final String[] paths2 = { "alpitiya", "rabukana","yodagama"};

    private Spinner spinner,spinner2;

    String utype;
    String gsdev;

    EditText Username,Email,Password,FullName;
    ProgressBar progressBar;



    Button registerbutton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_register);

        init();
    }

    private void init() {

        spinner = (Spinner)findViewById(R.id.spinReg);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this,
                android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);




        spinner2 = (Spinner)findViewById(R.id.spinRegGSdev);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(RegisterActivity.this,
                android.R.layout.simple_spinner_dropdown_item,paths2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);



        Username=findViewById(R.id.username_reg);
        Email=findViewById(R.id.email_reg);
        Password=findViewById(R.id.password_reg);
        progressBar=findViewById(R.id.progressReg);
        FullName=findViewById(R.id.fullname_reg);
        registerbutton= findViewById( R.id.registerbutton );

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        Spinner spin = (Spinner)parent;
        Spinner spin2 = (Spinner)parent;
        if(spin.getId() == R.id.spinReg)
        {
            utype=paths[position];
        }
        else if(spin2.getId() == R.id.spinRegGSdev)
        {
            gsdev=paths2[position];
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void Register(View view) {

        //Register to user to database
        progressBar.setVisibility( View.VISIBLE );
        registerbutton.setVisibility( View.INVISIBLE );

        String fname=FullName.getText().toString();
        String email=Email.getText().toString();
        String uname=Username.getText().toString();
        String pass=Password.getText().toString();



//validations
        if (TextUtils.isEmpty(fname)) {
            FullName.setError("FullName is required !");
            progressBar.setVisibility( View.INVISIBLE );
            registerbutton.setVisibility( View.VISIBLE );
            return;
        }
        if (TextUtils.isEmpty(uname)) {
            Username.setError("User Name is required !");
            progressBar.setVisibility( View.INVISIBLE );
            registerbutton.setVisibility( View.VISIBLE );
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Email.setError("Email is required !");
            progressBar.setVisibility( View.INVISIBLE );
            registerbutton.setVisibility( View.VISIBLE );
            return;
        }

        if(pass.length()<8 &&!isValidPassword(pass)){
            Password.setError( "Password must be 8 characters" );
            progressBar.setVisibility( View.INVISIBLE );
            registerbutton.setVisibility( View.VISIBLE );
            return;
        }


        RequestBody reqestBody = new MultipartBody.Builder()
                .setType( MultipartBody.FORM )
                // .addFormDataPart( "image", file_path.substring( file_path.lastIndexOf( "/" ) + 1 ), file_body )
                .addFormDataPart( "name", fname)
                .addFormDataPart( "email", email )
                .addFormDataPart( "uname", uname )
                .addFormDataPart( "pass", pass)
                .addFormDataPart( "cnumber", "" )
                .addFormDataPart( "division", gsdev )
                .addFormDataPart( "usertype", utype )
                .build();

        Request request = new Request.Builder()


                .url(URL_OF_DB.getInstance().userRegisterUrl)
                .post( reqestBody )
                .build();



        OkHttpSingleton.getInstance().getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e( "Add Register", e.getMessage() );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                    if (response.isSuccessful()) {

                        final String myResponse = response.body().string();
                        RegisterActivity.this.runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String JSON_STRING = myResponse;
                                    JSONArray mJsonArray = new JSONArray( JSON_STRING );
                                    JSONObject mJsonObject = mJsonArray.getJSONObject( 0 );
                                    String success = mJsonObject.getString( "success" );
                                    String email = mJsonObject.getString( "email" );
                                   // Log.e( "AdsPost", myResponse );

                                    Log.d( "AdsPost", success );

                                    if (success.equals( "true" )) {
                                        Toast.makeText( RegisterActivity.this, " Success! ", Toast.LENGTH_LONG ).show();
                                        if (email.equals( "true" )) {
                                            Toast.makeText( RegisterActivity.this, " Activation email send! ", Toast.LENGTH_LONG ).show();

                                            startActivity(  new Intent( RegisterActivity.this,LoginActivity.class ) );
                                            finish();



                                        } else {
                                            Toast.makeText( RegisterActivity.this, " Activation email not send!", Toast.LENGTH_LONG ).show();
                                        }
                                    } else {
                                        Toast.makeText( RegisterActivity.this, " failed! user name is already taken", Toast.LENGTH_LONG ).show();
                                    }






                                    progressBar.setVisibility( View.INVISIBLE );
                                    registerbutton.setVisibility( View.VISIBLE );
                                } catch (JSONException e) {
                                    Log.e( "AdsPost", e.toString() );
                                    Toast.makeText( RegisterActivity.this, e.toString(), Toast.LENGTH_LONG ).show();
                                    progressBar.setVisibility( View.INVISIBLE );
                                    registerbutton.setVisibility( View.VISIBLE );
                                }
                            }
                        } );
                    } else {
                        Log.d( "Res", "Error" );

                    }


            }
        });
    }

    public void goToLogin(View view) {

        startActivity(  new Intent( RegisterActivity.this,LoginActivity.class ) );
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