package com.kocapplication.pixeleye.kockoc.intro;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kocapplication.pixeleye.kockoc.R;
import com.kocapplication.pixeleye.kockoc.login.LoginActivity;
import com.kocapplication.pixeleye.kockoc.main.MainActivity;
import com.kocapplication.pixeleye.kockoc.util.connect.BasicValue;

import java.security.MessageDigest;
import java.util.ArrayList;

/**
 * Created by pixeleye02 on 2016-06-27.
 */
public class IntroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setPermissions()
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key", something);
            }
        } catch (Exception e) {
            Log.e("name not found", e.toString());
        }

        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // 3G나 LTE등 데이터 네트워크에 연결된 상태
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // 와이파이에 연결된 상태

        try {
            autoLogin();
        } catch (Exception e) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("안내");
            dialog.setMessage("인터넷 연결상태를 확인해주세요");
            dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.show();
            e.printStackTrace();
        }

    }

    private void autoLogin() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        if (pref.getInt("login", -1) != -1) {
            int login = pref.getInt("login", -1);
            if (login != -1) {
                BasicValue.getInstance().setUserNo(login);

                Intent intent = new Intent(this, MainActivity.class);
                Uri uri = getIntent().getData();
                try {
                    intent.putExtra("kakaoLinkBoardNo", Integer.parseInt(uri.getQueryParameter("boardNo")));
                    intent.putExtra("kakaoLinkCourseNo", Integer.parseInt(uri.getQueryParameter("courseNo")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        } else {
            initialize();
        }
    }

    private void initialize() {
        Handler handler = new Handler() {   //introActivity배경사진의 시간을 늘려줌
            @Override
            public void handleMessage(Message msg) {
                finish();
                Intent login_intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(login_intent);
            }
        };
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        }


    };

    @Override
    protected void onResume() {
        super.onResume();

    }


}
