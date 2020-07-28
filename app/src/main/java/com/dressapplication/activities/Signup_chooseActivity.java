package com.dressapplication.activities;

import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.dressapplication.Interface.ApiInterface;
import com.dressapplication.Model.GetUserByName;
import com.dressapplication.Model.LoginData;
import com.dressapplication.R;
import com.dressapplication.Retrofit.ApiUtils;
import com.dressapplication.SharedPrefrence.DataProccessor;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup_chooseActivity extends AppCompatActivity {
    private Button btn_email_phone, loginiwthgoogle, bt_fb;
    private GoogleSignInClient mGoogleSignInClient;
    private String idToken, name, email;
    private LoginButton fb_login_button;
    CallbackManager callbackManager;
    private RelativeLayout rl_login_with_phone;
    private ImageView iv_back;
    private ProgressBar progressBar;
    //  private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_choose);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        progressBar = findViewById(R.id.progressBar);
        iv_back = findViewById(R.id.iv_back);
        bt_fb = findViewById(R.id.bt_fb);
        fb_login_button = findViewById(R.id.fb_login_button);
        rl_login_with_phone = findViewById(R.id.rl_login_with_phone);
        btn_email_phone = findViewById(R.id.btn_email_phone);
        loginiwthgoogle = findViewById(R.id.loginiwthgoogle);
        fb_login_button.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(Signup_chooseActivity.this, gso);

        loginiwthgoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });
        printHashKey();

        rl_login_with_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup_chooseActivity.this, LoginWithEmailActivity.class);
                startActivity(intent);
            }
        });


        btn_email_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup_chooseActivity.this, signupActivity.class);
                startActivity(intent);
            }
        });

        bt_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fb_login_button.performClick();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fb_login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //loginResult.getAccessToken();
                //loginResult.getRecentlyDeniedPermissions()
                //loginResult.getRecentlyGrantedPermissions()
                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                Log.d("API123", loggedIn + " ??");
                getUserProfile(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException error) {

            }


        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        printHashKey();
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = null;
            account = task.getResult(ApiException.class);
            handleSignInResult(account);
        } catch (
                ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            Log.w("ErrorGoogle", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void handleSignInResult(GoogleSignInAccount result) {
        idToken = result.getIdToken();
        name = result.getDisplayName();
        email = result.getEmail();
        Log.e("nameofuser", name);
        Log.e("emailofuser", email);
        Toast.makeText(this, "" + name, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "" + email, Toast.LENGTH_SHORT).show();

        setSocial(name, email, "g");
    }

    private void setSocial(String name, String email, String socialType) {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiInteface;
        apiInteface = ApiUtils.getAPIService(Signup_chooseActivity.this);
        apiInteface.socialLogin(email, name, "454", socialType, "Android", getDeviceid(), "30.55", "37.215").enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    LoginData responseData = response.body();
                    if (responseData != null) {
                        if (responseData.getCode().equalsIgnoreCase("201")) {
                                     Toast.makeText(Signup_chooseActivity.this, "" + responseData.getStatus(), Toast.LENGTH_SHORT).show();
                            DataProccessor dataProccessor = new DataProccessor(Signup_chooseActivity.this);
                            dataProccessor.setEmail("email", responseData.getData().get(0).getEmail());
                            dataProccessor.setUserid("userid", responseData.getData().get(0).getId());
                            dataProccessor.setpassword("password", responseData.getData().get(0).getPassword());
                            dataProccessor.setusername("username", responseData.getData().get(0).getUsername());
                            savelogindata(responseData.getData().get(0).getEmail());
                            saveuserid(responseData.getData().get(0).getId());
                            Intent intent = new Intent(Signup_chooseActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            DataProccessor dataProccessor = new DataProccessor(Signup_chooseActivity.this);
                            dataProccessor.setEmail("email", email);
                            dataProccessor.setusername("username", name);
                            savelogindata(email);
                         //   Log.e("fbname",name);
                            getUserid(name);


                      //     Toast.makeText(Signup_chooseActivity.this, "" + responseData.getStatus(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("failure", "" + t.getMessage());
                Toast.makeText(Signup_chooseActivity.this, "social" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getDeviceid() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Signup_chooseActivity.this);
        return preferences.getString("registration_id", null);
    }

    private void savelogindata(String email) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email", email);
        editor.apply();

    }

    private void saveuserid(String userid) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userid", userid);
        editor.apply();

    }

    private void printHashKey() {
        try {
            PackageInfo info = Objects.requireNonNull(Signup_chooseActivity.this).getPackageManager().getPackageInfo(Signup_chooseActivity.this.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e("fb", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (Exception e) {
            Log.e("fb", "printHashKey()", e);
        }
    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            setSocial(first_name, email, "f");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void getUserid(String username) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiInteface;
            apiInteface = ApiUtils.getAPIService(Signup_chooseActivity.this);
            apiInteface.getUsername(username).enqueue(new Callback<GetUserByName>() {
                @Override
                public void onResponse(Call<GetUserByName> call, Response<GetUserByName> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        GetUserByName data = response.body();
                        if (data != null) {
                            if (data.getCode().equalsIgnoreCase("201")) {
                                if (data.getUserData() != null) {
                                    DataProccessor dataProccessor = new DataProccessor(Signup_chooseActivity.this);
                                    dataProccessor.setUserid("userid", data.getUserData().get(0).getId());
                                    saveuserid(data.getUserData().get(0).getId());
                                    Intent intent=new Intent(Signup_chooseActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(Signup_chooseActivity.this, "" + data.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetUserByName> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Signup_chooseActivity.this, "user" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("follower failure", "" + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
