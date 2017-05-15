package com.soyoung.butler.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.soyoung.butler.R;
import com.soyoung.butler.application.ApplicationController;
import com.soyoung.butler.login.LoginInfo;
import com.soyoung.butler.main.MainActivity;
import com.soyoung.butler.network.NetworkService;

import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    EditText email;
    EditText pwd;
    EditText pwdconfirm;
    EditText name;
    Button nextBtn;

    NetworkService service;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_signup);

        email = (EditText) findViewById(R.id.email2);
        name = (EditText) findViewById(R.id.name2);
        pwd = (EditText) findViewById(R.id.pwd2);
        pwdconfirm = (EditText) findViewById(R.id.pwdconfirm);
        nextBtn = (Button) findViewById(R.id.nextBtn);

        LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        callbackManager = CallbackManager.Factory.create();

        mProgressDialog = new ProgressDialog(SignupActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("등록 중...");
        mProgressDialog.setIndeterminate(true);

        // TODO: 2016. 11. 21. 미리 retrofit를 build 한 것을 가져온다.
        service = ApplicationController.getInstance().getNetworkService();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("result", object.toString());
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

                startActivity(intent);
                finish();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.e("LoginErr", error.toString());
            }
        });

        pwdconfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = pwd.getText().toString();
                String confirm = pwdconfirm.getText().toString();

                if (password.equals(confirm)) {
                    Toast.makeText(SignupActivity.this, "비밀번호가 일치합니다!", Toast.LENGTH_SHORT);
                }
                else {
                    Toast.makeText(SignupActivity.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이메일 입력 확인
                if( email.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "이메일을 입력하세요!", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                    return;
                }
                // 이름 입력 확인
                if (name.getText().toString().length() == 0) {
                    Toast.makeText(SignupActivity.this, "이름을 입력하세요!", Toast.LENGTH_SHORT).show();
                    name.requestFocus();
                    return;
                }
                // 비밀번호 입력 확인
                if( pwd.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                    pwd.requestFocus();
                    return;
                }

                // 비밀번호 확인 입력 확인
                if( pwdconfirm.getText().toString().length() == 0 ) {
                    Toast.makeText(SignupActivity.this, "비밀번호 확인을 입력하세요!", Toast.LENGTH_SHORT).show();
                    pwdconfirm.requestFocus();
                    return;
                }

                // 비밀번호 일치 확인
                if( !pwd.getText().toString().equals(pwdconfirm.getText().toString()) ) {
                    Toast.makeText(SignupActivity.this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                    pwd.setText("");
                    pwdconfirm.setText("");
                    pwd.requestFocus();
                    return;
                }

                String emails = email.getText().toString();
                String password = pwd.getText().toString();
                String names = name.getText().toString();

                RegisterData registerData = new RegisterData(emails, password, names);

                Call<RegisterResult> registerResultCall=service.requestRegister(registerData);
                registerResultCall.enqueue(new Callback<RegisterResult>() {
                    @Override
                    public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                        if (response.isSuccessful()) {
                            RegisterResult result = response.body();
                            if (result.result) {
                                Intent intent = new Intent(getApplicationContext(), LoginInfo.class);
                                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResult> call, Throwable t) {
                        Log.d("result", "fail");
                    }
                });
            }
        });
    }
}
