package com.example.edonate.Connection;

public class URL_OF_DB {
    private static URL_OF_DB singletonInstance;

    public static URL_OF_DB getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new URL_OF_DB();
        }
        return singletonInstance;
    }


    public  String BaseURL="https://edonateadmin.000webhostapp.com/MobileApp/";
//    public  String BaseURL="http://192.168.1.13/eDonate/MobileApp/";


    public String userRegisterUrl=BaseURL+"usersregister.php";
    public String auth=BaseURL+"auth.php";
    public String getPosts=BaseURL+"getPosts.php";
    public String getcomments=BaseURL+"getcomments.php";
    public String addcomments=BaseURL+"addcomments.php";
    public String loadUsers=BaseURL+"loadProfile.php";
    public String addpost=BaseURL+"addpost.php";

}
