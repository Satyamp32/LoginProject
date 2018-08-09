package com.example.cool.loginpro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UsersMobileNo extends AppCompatActivity implements View.OnClickListener {
    private DatabaseHelper databaseHelper;
    private List<User> usersList;
    // private RecyclerView recyclerView;
    //  private MobileNoRecyclerViewAdapter adapter;
    private EditText msgEditText;
    private TextView textView;
    private Button sendButton;
    private String message;
    final int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_mobile_no);

        initiateViews();

        //  recyclerView.setHasFixedSize(true);
        //  recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initiateObjects();

        initiateListeners();

        //  recyclerView.setAdapter(adapter);

        usersList.clear();
        usersList.addAll(databaseHelper.getAllUserMobileNumber());


        for(User numbers : usersList){

            textView.append(numbers.getmMobileNo()+"\n");
        }



    }

    public void initiateViews(){
        // recyclerView = (RecyclerView) findViewById(R.id.recyclerview_mobile);

        textView=(TextView)findViewById(R.id.allMobileNumbers);

        msgEditText = (EditText) findViewById(R.id.edittext_message);
        sendButton = (Button) findViewById(R.id.sendButton);
    }
    public void initiateObjects(){

        usersList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);
        //  adapter = new MobileNoRecyclerViewAdapter(this,usersList);
    }
    public void initiateListeners(){

        sendButton.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sendButton:

                message = msgEditText.getText().toString().trim();
                String mnumbers = textView.getText().toString();
                String numbers[] = mnumbers.split("\n *");
                if (!TextUtils.isEmpty(message)) {

                    // Checking runtime permission
                    if (checkPermission(Manifest.permission.SEND_SMS)) {
                        SmsManager smsManager = SmsManager.getDefault();
                        // For MultipleNumbers
                        for (String numbers1 : numbers) {
                            smsManager.sendTextMessage(numbers1, null, message, null, null);
                        }
                        Toast.makeText(UsersMobileNo.this, "Message Sent", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(UsersMobileNo.this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }


                break;
        }

    }

    private boolean checkPermission(String permission) {
        int checkPermission = ContextCompat.checkSelfPermission(this, permission);
        return (checkPermission == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case SEND_SMS_PERMISSION_REQUEST_CODE :
            {
                if(grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    sendButton.setEnabled(true);
                }
                return;
            }
        }

    }
}
