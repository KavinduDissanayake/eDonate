package com.example.edonate.Screen.DashBoard.AddPost;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.edonate.Connection.OkHttpSingleton;
import com.example.edonate.Connection.URL_OF_DB;
import com.example.edonate.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class AddPostsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    public  AddPostsFragment(){}



    private static final String[] paths = {"Education", "Health","Job","Home","Food"};

    ImageView prop_profile;
    ImageView prop_add;
    ImageView prop_post_img;
    ProgressBar propup_progressBar;
    EditText propup_description;
    View root;
    Bitmap imageBitmap;
    Spinner spinner;
    ProgressDialog pd;
    boolean isSelected;
    static final   int  PICK_FROM_GALLERY=123;

    //user data session

    String c_uid="";
    String c_uname="";
    String c_uimg="";
    String c_utype="";
    SharedPreferences prf;


    String title;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        prf = getActivity().getSharedPreferences("user_details",MODE_PRIVATE);
        c_uid=  prf.getString("cu_id",null);
        c_uname=  prf.getString("cu_name",null);
        c_uimg=  prf.getString("cu_img",null);
        c_utype=  prf.getString("cu_utype",null);
        root = inflater.inflate( R.layout.add_posts_fragment, container, false );

        init();



        return root;
    }

    private void init() {

        pd = new ProgressDialog(getContext());

        spinner = root.findViewById(R.id.spinTitle);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( getActivity(),
                android.R.layout.simple_spinner_item,paths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        prop_profile = root.findViewById( R.id.prop_profile );
        prop_add = root.findViewById( R.id.prop_add );
        propup_progressBar = root.findViewById( R.id.propup_progressBar );
        propup_description = root.findViewById( R.id.propup_description );
        prop_post_img = root.findViewById( R.id.prop_post_img );

        try {

            if(!c_uimg.isEmpty()){
                Picasso.get().load(c_uimg)
                        .networkPolicy( NetworkPolicy.NO_CACHE)
                        .memoryPolicy( MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                        .resize(400, 400)
                        .into(prop_profile);

            }


        }catch (Exception e)
        {

        }

        prop_post_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        prop_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upLoadPost();
            }
        });



    }

    private void upLoadPost() {

        String description=propup_description.getText().toString();
        if (isSelected == true) {

            pd.setMessage("Uploading loading");
            pd.show();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,75, byteArrayOutputStream);
            byte[] imageInByte = byteArrayOutputStream.toByteArray();
            String encodedImage =  Base64.encodeToString(imageInByte,Base64.DEFAULT);


            RequestBody reqestBody = new MultipartBody.Builder()
                    .setType( MultipartBody.FORM )
                    .addFormDataPart( "image", encodedImage )
                    .addFormDataPart( "title", title )
                    .addFormDataPart( "des", description )
                    .addFormDataPart( "uid", c_uid )
                    .addFormDataPart( "uname", c_uname )
                    .addFormDataPart( "uimg", c_uimg )
                    .addFormDataPart( "utype", c_utype )
                    .build();

            Request request = new Request.Builder()


                    .url(URL_OF_DB.getInstance().addpost )
                    .post( reqestBody )
                    .build();


            OkHttpSingleton.getInstance().getClient().newCall( request ).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {

                        final String myResponse = response.body().string();
                        getActivity().runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String JSON_STRING = myResponse;
                                    JSONArray mJsonArray = new JSONArray( JSON_STRING );
                                    JSONObject mJsonObject = mJsonArray.getJSONObject( 0 );
                                    String success = mJsonObject.getString( "success" );
                                    //   Log.e( "AdsPost", myResponse );
                                    Log.d( "AdsPost", success );
                                    if (success.equals( "true" )) {
                                        pd.dismiss();
                                        Toast.makeText( getActivity(), " Success! ", Toast.LENGTH_LONG ).show();
                                    } else {
                                        pd.dismiss();
                                        Toast.makeText( getActivity(), " failed! Please try again later", Toast.LENGTH_LONG ).show();
                                    }

                                    propup_progressBar.setVisibility( View.INVISIBLE );
                                    prop_add.setVisibility( View.VISIBLE );
                                } catch (JSONException e) {
                                    pd.dismiss();
                                    Log.e( "AdsPost", e.toString() );
                                    Toast.makeText( getActivity(), e.toString(), Toast.LENGTH_LONG ).show();
                                    propup_progressBar.setVisibility( View.INVISIBLE );
                                    prop_add.setVisibility( View.VISIBLE );
                                }
                            }
                        } );
                    } else {
                        pd.dismiss();
                        Log.d( "Res", "Error" );
                    }
                }
            } );


        }else {
            propup_progressBar.setVisibility( View.INVISIBLE );
            prop_add.setVisibility( View.VISIBLE );
            Toast.makeText( getActivity(), " Please select a PostImage before submitting", Toast.LENGTH_LONG ).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        title = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_FROM_GALLERY){
            if (resultCode == RESULT_OK){

                Uri path = data.getData();
                try {
                    imageBitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),path);
                    prop_post_img.setImageBitmap(imageBitmap);
                    Log.e("Image path",imageBitmap+"");
                    isSelected=true;
                 //   UpLoadingImage();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}