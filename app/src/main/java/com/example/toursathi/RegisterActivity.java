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

public class RegisterActivity extends AppCompatActivity {

    String mobileno,name,email;
    EditText editname,editemail;
    Button btnregister;
    public ApiInterface apiInterface;
    public User user = new User();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();
        mobileno = intent.getStringExtra("mobileno");
        btnregister = findViewById(R.id.btnregister);
        editemail= findViewById(R.id.editemail);
        editname = findViewById(R.id.editname);
        apiInterface = ApiUtils.getAPIService();
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editname.getText().toString().trim();
                email= editemail.getText().toString().trim();
                if (name.equals("")||email.equals(""))
                {
                    Toast.makeText(RegisterActivity.this,"please enter all field",Toast.LENGTH_SHORT).show();
                }
                else {
                    Call<User> callregister = apiInterface.getRegister(mobileno,name,email);
                    callregister.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful())
                            {
                                user = response.body();
                                ((UserClient)getApplicationContext()).setUser(user);
                                Intent intent= new Intent(RegisterActivity.this,PermissionActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else {
                                Toast.makeText(RegisterActivity.this,String.valueOf(response.code()),Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
