package com.overtech.lenovo.activity.business.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.MainActivity;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.EditTextWithDelete;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button mDoLogin;
    private TextView mDoLostPassword;
    private TextView mDoRegister;
    private EditTextWithDelete etLoginName;
    private EditTextWithDelete etLoginPwd;
    private UIHandler uiHandler;

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_login;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        uiHandler = new UIHandler(this);
        mDoLogin = (Button) findViewById(R.id.btn_login);
        mDoRegister = (TextView) findViewById(R.id.tv_register_account);
        mDoLostPassword = (TextView) findViewById(R.id.tv_lost_password);
        etLoginName = (EditTextWithDelete) findViewById(R.id.et_login_username);
        etLoginPwd = (EditTextWithDelete) findViewById(R.id.et_login_password);

        mDoLogin.setOnClickListener(this);
        mDoLostPassword.setOnClickListener(this);
        mDoRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_login:
                doLogin();
                break;
            case R.id.tv_lost_password:
                doLogout();
//                intent.setClass(this, GetSmsCodeAndValicateActivity.class);
//                intent.putExtra("flag", Projects.LOST_PASSWORD);
//                startActivity(intent);
                break;
            case R.id.tv_register_account:
                testNetInterface();
//                intent.setClass(this, RegisterUserAgreementActivity.class);
//                intent.putExtra("flag", Projects.REGISTER);
//                startActivity(intent);
                break;
            default:
                break;
        }

    }

    private void doLogin() {
        String name = etLoginName.getText().toString().trim();
        String pwd = etLoginPwd.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Utilities.showToast("用户名不能为空", this);
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Utilities.showToast("密码不能为空", this);
            return;
        }
        startProgress("登录中...");
        Requester requester = new Requester();
        requester.cmd = 1;
        requester.uid = name;
        requester.pwd = pwd;
        Request request = httpEngine.createRequest(SystemConfig.IP, new Gson().toJson(requester));
        Call call = httpEngine.createRequestCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Logger.e(request.toString());
                stopProgress();
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String json = response.body().string();
                Logger.e(json);
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            stopProgress();
                            if (response.isSuccessful()) {
                                JSONObject jsonObject = new JSONObject(json);
                                int st = jsonObject.getInt("st");
                                if (st == 0) {
                                    JSONObject body = jsonObject.getJSONObject("body");
                                    SharePreferencesUtils.put(LoginActivity.this, SharedPreferencesKeys.UID, body.getInt("userid")+"");
                                    Intent intent = new Intent();
                                    intent.setClass(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (st == 1) {
                                    Utilities.showToast("用户名或者密码错误", LoginActivity.this);
                                } else {
                                    Utilities.showToast("illegal request", LoginActivity.this);
                                }
                            } else {
                                Utilities.showToast("服务器异常", LoginActivity.this);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }


    private void testNetInterface() {//测试获取用户列表接口
        startProgress("加载中");
        Requester requester = new Requester();
        requester.cmd = 10000;
        requester.uid = (String)SharePreferencesUtils.get(this, SharedPreferencesKeys.UID,"-1" );
        Request request = httpEngine.createRequest(SystemConfig.IP, new Gson().toJson(requester));
        Call call = httpEngine.createRequestCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                stopProgress();
                Logger.e(request.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String json=response.body().string();
                Logger.e("10000的接口返回值"+json);
                stopProgress();
                if(response.isSuccessful()){

                }
            }
        });
    }

    private void doLogout() {
        startProgress("正在退出");
        Requester requester = new Requester();
        requester.uid = (String)SharePreferencesUtils.get(this, SharedPreferencesKeys.UID, "-1");
        requester.cmd = 2;

        Request request = httpEngine.createRequest(SystemConfig.IP, new Gson().toJson(requester));
        Call call = httpEngine.createRequestCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                stopProgress();
                Logger.e(request.toString());
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String json = response.body().string();
                Logger.e("登录"+ json);
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        stopProgress();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                int st = jsonObject.getInt("st");
                                if (st == 0) {
                                    //退出成功
                                } else if (st == -1) {
                                    //未登录
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Utilities.showToast("服务器异常", LoginActivity.this);
                        }
                    }
                });

            }
        });
    }

}

