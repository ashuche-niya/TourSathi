package com.example.toursathi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.toursathi.Constant.ApiInterface;
import com.example.toursathi.Constant.ApiUtils;
import com.example.toursathi.ResponseModel.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    Button btnnext;
    EditText editmobileno;
    String mobileno;
    public ApiInterface apiInterface;
    public User user = new User();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        btnnext=findViewById(R.id.btnnext);
        editmobileno=findViewById(R.id.editmobileno);
        apiInterface= ApiUtils.getAPIService();
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileno=editmobileno.getText().toString();
                if (!mobileno.equals(""))
                {
                    Call<User> callsignin= apiInterface.getSignIn(mobileno);
                    callsignin.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful())
                            {
                                user= response.body();
                                if (user.getExistence().equals("notexist"))
                                {
                                    Intent intent = new Intent(SignInActivity.this,RegisterActivity.class);
                                    intent.putExtra("mobileno",mobileno);
                                    startActivity(intent);
                                }
                                else
                                {
                                    ((UserClient)getApplicationContext()).setUser(user);
                                    Intent intent= new Intent(SignInActivity.this,PermissionActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            else {
                                Toast.makeText(SignInActivity.this,String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(SignInActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {

                }

            }
        });
    }
}
