package com.example.vkoauth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.vkoauth.responses.AccessTokenResponse;
import com.example.vkoauth.responses.VkUsersResponse;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String CLIENT_ID = "7466279";
    private static String CLIENT_SECRET = "DcgtmDbciiFP4NkgH3tz";
    private static String REDIRECT_URI = "http://87bc5c08.ngrok.io";
    private static String VK_API_VERSION = "5.103";
    public static String OAUTH_VK_URL = "https://oauth.vk.com/authorize";
    Button login;
    SharedPreferences pref;
    VkUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.login);
        pref = getSharedPreferences("auth_data", MODE_PRIVATE);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final Dialog auth_dialog = new Dialog(this);
        auth_dialog.setContentView(R.layout.oauth_dialog);
        WebView web = auth_dialog.findViewById(R.id.web);
        web.getSettings().setJavaScriptEnabled(true);
        String url = OAUTH_VK_URL +"?client_id=" + CLIENT_ID
                                  +"&client_secret=" + CLIENT_SECRET
                                  +"&redirect_uri=" + REDIRECT_URI;
        web.loadUrl(url);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //url = http://vk.com?code=123456&param2=val2
                if (url.contains("?code")) {
                    Uri uri = Uri.parse(url);
                    String code = uri.getQueryParameter("code");
                    if (code != null){
                        Toast.makeText(MainActivity.this, "Код авторизации получен", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("code", code);
                        edit.apply();
                        getAccessTokenAndLoadUser();
                    }
                    auth_dialog.dismiss();
                } else if (url.contains("error=access_denied")) {
                    Toast.makeText(MainActivity.this, "Доступ щапрещен", Toast.LENGTH_SHORT).show();
                    auth_dialog.dismiss();
                }
            }
        });
        auth_dialog.show();
        auth_dialog.setCancelable(true);
    }
    public void getAccessTokenAndLoadUser() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Соединение с вк");
        dialog.setCancelable(true);
        dialog.show();
        try {
            String code = pref.getString("code", "");
            VkService vk = VkServer.oauth().create(VkService.class);
            Call<AccessTokenResponse> call = vk.getAccessToken(code, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
            call.enqueue(new Callback<AccessTokenResponse>() {
                @Override
                public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                    AccessTokenResponse atr = response.body();
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("token", atr.access_token);
                    edit.apply(); //edit.commit()
                    Toast.makeText(MainActivity.this, "Token is" + atr.access_token, Toast.LENGTH_SHORT).show();
                    getUserProfile();
                }
                @Override
                public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dialog.dismiss();
        }
    }
    public void getUserProfile() {
        if (user != null) {
            showProfile();
            return;
        }
        VkService vk = VkServer.api().create(VkService.class);
        String fields = "photo_big";

        Call<VkUsersResponse> call = vk.getUserProfile(VK_API_VERSION, fields, pref.getString("token",""));
        call.enqueue(new Callback<VkUsersResponse>() {
            @Override
            public void onResponse(Call<VkUsersResponse> call, Response<VkUsersResponse> response) {
                System.out.println(Arrays.toString(response.body().response));
                user = response.body().response[0];
                showProfile();
            }

            @Override
            public void onFailure(Call<VkUsersResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void showProfile() {
        if (user == null) {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
