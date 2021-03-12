package com.example.edonate.Screen.DashBoard.Profile;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edonate.Connection.OkHttpSingleton;
import com.example.edonate.Connection.URL_OF_DB;
import com.example.edonate.R;
import com.example.edonate.Screen.LoginActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    EditText emailEditPro,editContact,userNameProEdit;
    TextView userTypeProEdit;
    ImageView profile;
    ImageView log_out;
    String c_uid="";

    SharedPreferences prf;
    View root;

    boolean isSelected;
    ProgressDialog pd;

    FloatingActionButton uploadbutton;
    static final   int  PICK_FROM_GALLERY=123;
    Bitmap imageBitmap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        prf = getActivity().getSharedPreferences("user_details",MODE_PRIVATE);
        c_uid=  prf.getString("cu_id",null);
        root = inflater.inflate( R.layout.profile_fragment, container, false );
        init();

        loadUsers();

        return root;
    }

    private void init() {
        pd = new ProgressDialog(getContext());


        userTypeProEdit=root.findViewById( R.id.userTypeProEdit );
        userNameProEdit=root.findViewById( R.id.userNameProEdit );
        emailEditPro=root.findViewById( R.id.emailEditPro );
        editContact=root.findViewById( R.id.editContact );
        profile=root.findViewById( R.id.profileEdit );
        log_out=root.findViewById( R.id.log_out );
        uploadbutton=root.findViewById( R.id.uploadbutton );

        //log out setup must be session clear
        log_out.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sessions clear
                SharedPreferences.Editor editor = prf.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent( getActivity(), LoginActivity.class ));
                getActivity().finish();
            }
        } );


        uploadbutton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        } );

        profile.setOnClickListener( new View.OnClickListener() {
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
        } );

    }
    private void editProfile() {
        uploadbutton.setVisibility( View.INVISIBLE );
        String email= emailEditPro.getText().toString();
        String cnumber= editContact.getText().toString();
        String name= userNameProEdit.getText().toString();

        if (isSelected == true) {


            pd.setMessage("Uploading loading");
            pd.show();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,75, byteArrayOutputStream);
            byte[] imageInByte = byteArrayOutputStream.toByteArray();
            String encodedImage =  Base64.encodeToString(imageInByte,Base64.DEFAULT);


            OkHttpClient client = new OkHttpClient();
            RequestBody reqestBody = new MultipartBody.Builder()
                    .setType( MultipartBody.FORM )
                    .addFormDataPart( "image",encodedImage )
                    .addFormDataPart( "cid", c_uid )
                    .addFormDataPart( "name", name )
                    .addFormDataPart( "email", email )
                    .addFormDataPart( "cnumber", cnumber )
                    .build();
            Request request = new Request.Builder()
                    .url( URL_OF_DB.getInstance().editProfile )
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
                        getActivity().runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String JSON_STRING = myResponse;
                                    JSONArray mJsonArray = new JSONArray( JSON_STRING );
                                    JSONObject mJsonObject = mJsonArray.getJSONObject( 0 );
                                    String success = mJsonObject.getString( "success" );
                                    Log.e( "AdsPost", myResponse );
                                    Log.d( "AdsPost", success );
                                    if (success.equals( "true" )) {
                                        pd.dismiss();
                                        Toast.makeText( getActivity(), " Success! ", Toast.LENGTH_LONG ).show();
                                    } else {
                                        pd.dismiss();
                                        Toast.makeText( getActivity(), " failed! Please try again later", Toast.LENGTH_LONG ).show();
                                    }


                                    uploadbutton.setVisibility( View.VISIBLE );
                                } catch (JSONException e) {
                                    Log.e( "AdsPost", e.toString() );
                                    Toast.makeText( getActivity(), e.toString(), Toast.LENGTH_LONG ).show();
                                    pd.dismiss();
                                    uploadbutton.setVisibility( View.VISIBLE );
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

            uploadbutton.setVisibility( View.VISIBLE );
            Toast.makeText( getActivity(), " Please select a PostImage before submitting", Toast.LENGTH_LONG ).show();
        }


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_FROM_GALLERY){
            if (resultCode == RESULT_OK){

                Uri path = data.getData();
                try {
                    imageBitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),path);
                    profile.setImageBitmap(imageBitmap);
                    Log.e("Image path",imageBitmap+"");
                    isSelected=true;
                    //   UpLoadingImage();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    private void loadUsers() {
        RequestBody reqestBody= new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("cid",c_uid)
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
                    getActivity().runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            try{
                                String JSON_STRING= myResponse;
                                JSONArray mJsonArray = new JSONArray(JSON_STRING);
                                JSONObject mJsonObject = mJsonArray.getJSONObject(0);

                                Log.e( "User Details",myResponse );
                                String name = mJsonObject.getString("name");
                                String email = mJsonObject.getString("email");
                                String cnumber = mJsonObject.getString("cnumber");
                                String usertype = mJsonObject.getString("usertype");
                                String img = mJsonObject.getString("img");


                                userTypeProEdit.setText( usertype );
                                editContact.setText( cnumber );
                                emailEditPro.setText( email );
                                userNameProEdit.setText( name);

                                if(!img.isEmpty()){
                                    Picasso.get().load(img)
                                            .networkPolicy( NetworkPolicy.NO_CACHE)
                                            .memoryPolicy( MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                                            .into(profile);

                                }

                                final CollapsingToolbarLayout collapsingToolbarLayout=root.findViewById( R.id.collapsingToolbarLayoutProEdit );
                                collapsingToolbarLayout.setTitleEnabled( true );
                                collapsingToolbarLayout.setTitle(name );

                            }
                            catch(JSONException e){
                                Toast.makeText( getActivity(), e.toString(), Toast.LENGTH_LONG ).show();
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