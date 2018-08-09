package com.example.cool.loginpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener  {
    private TextView mTextWelcome,mTextTodayWillBeGreat;
    private Button mButtonLogin;
    private EditText mEmail,mPassword;
    DatabaseHelper databaseHelper;
    RelativeLayout nestedScrollView;
    User user;
    CheckBox mcheckBox;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_register:
                    Intent in=new Intent(getBaseContext(),RegisterActivity.class);
                    startActivity(in);
                    return true;

                case R.id.navigation_admin:
                    Intent i=new Intent(getBaseContext(),AdminActivity.class);
                    startActivity(i);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mTextWelcome = (TextView) findViewById(R.id.welcome);
        mTextTodayWillBeGreat = (TextView) findViewById(R.id.today_will_be_a_great_day);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initViews();
        initListeners();
        initObjects();
        if(! new UserPerfManager(this).isUserLogOut())
        {
            startHomeActivity();
        }
    }

    private void initViews()
    {
        mcheckBox=(CheckBox)findViewById(R.id.loginRemember_me);
        mButtonLogin=(Button)findViewById(R.id.Login);
        mEmail =(EditText)findViewById(R.id.LoginEmail);
        mPassword=(EditText)findViewById(R.id.loginPassword);
        nestedScrollView=(RelativeLayout)findViewById(R.id.container);
    }

    private void initListeners()
    {
        mButtonLogin.setOnClickListener(this);
    }
    private void initObjects()
    {
        databaseHelper=new DatabaseHelper(this);
        user=new User();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.Login:
                verifyFromSQLite();

                break;
        }
    }
    public void verifyFromSQLite()
    {
        String email=mEmail.getText().toString().trim();
        String password =mPassword.getText().toString().trim();
        if(email.equals(""))
        {
            mEmail.setError("Please Enter Email Id");
        }

        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            mEmail.setError("Please Enter correct Email Format");
        }
        else if( password.equals("") )
        {
            mPassword.setError("Please Enter Password");
        }
        else if(databaseHelper.checkCandidate(mEmail.getText().toString().trim(),mPassword.getText().toString().trim()))
        {
            attemptLoginActivity();
        }

        else
        {
            Snackbar.make(nestedScrollView,"Email or Password not matching",Snackbar.LENGTH_LONG).show();
        }

    }

    public void attemptLoginActivity()
    {
        String email=mEmail.getText().toString().trim();
        String password=mPassword.getText().toString().trim();
        if(mcheckBox.isChecked())
        {
            saveLoginDetails(email,password);
            startHomeActivity();
        }
        else
            startHomeActivity();
    }
    public void saveLoginDetails(String email,String password)
    {
        new UserPerfManager(this).saveLoginDetails(email,password);
    }

    public void startHomeActivity()
    {
        Intent accountsIntent = new Intent(LoginActivity.this, LoginUserDetailsActivity.class);
        accountsIntent.putExtra("EMAIL", mEmail.getText().toString().trim());
        startActivity(accountsIntent);
        emptyEditText();
        finish();
    }
    private void emptyEditText()
    {
        mEmail.setText("");
        mPassword.setText("");
    }


}
