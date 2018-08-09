package com.example.cool.loginpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mButtonLogin;
    private String id1="admin";
    private String password1="password";
    EditText mId;
    EditText mPassword;
    RelativeLayout container;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initViews();
        initListeners();
        if(! new AdminPerfManager(this).isUserLogOut())
        {
            startHomeActivity();
        }
    }
    private void initViews()
    {
        checkBox =(CheckBox)findViewById(R.id.adminRememberMe);
        mButtonLogin=(Button)findViewById(R.id.adminLogin);
        mId=(EditText)findViewById(R.id.adminEmail);
        mPassword=(EditText)findViewById(R.id.adminPassword);
        container=(RelativeLayout)findViewById(R.id.container);

    }
    private void initListeners()
    {
        mButtonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.adminLogin:
                String id=mId.getText().toString();
                String password=mPassword.getText().toString();
                if(id.equals(""))
                {
                    mId.setError("Id is Mandatory");
                }
                else if(password.equals(""))
                {
                    mPassword.setError("Password is Mandatory");
                }
                else if(id.equals(id1)&& password.equals(password1))
                {
                    //startHomeActivity();
                    attemptLogin();
                }
                else
                {
                    Snackbar.make(container,"Email or Password not Matching",Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }
    public void startHomeActivity()
    {
        Intent intentAdminLogin =new Intent(AdminActivity.this,AllRegisteredUserActivity.class);
        startActivity(intentAdminLogin);
        finish();
    }
    private void attemptLogin()
    {
        String email=mId.getText().toString();
        String password =mPassword.getText().toString();
        if(checkBox.isChecked())
        {
            saveLoginDetails(email,password);
            startHomeActivity();
        }
        else
        {
            startHomeActivity();
        }

    }
    private void saveLoginDetails(String email, String password) {
        new AdminPerfManager(this).saveLoginDetails(email, password);
    }

}