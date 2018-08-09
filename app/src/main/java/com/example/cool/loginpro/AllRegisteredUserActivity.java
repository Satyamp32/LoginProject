package com.example.cool.loginpro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class AllRegisteredUserActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    List<User> list;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_registered_users);
        initViews();
        initObjects();
        recyclerView.setLayoutManager(new LinearLayoutManager(AllRegisteredUserActivity.this));
        recyclerView.setAdapter(adapter);
        getDataFromSQLite();
    }
    @SuppressLint("StaticFieldLeak")
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                list.clear();
                list.addAll(databaseHelper.getAllUser());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private void initObjects()
    {
        databaseHelper =new DatabaseHelper(this);
        list=new ArrayList<>();
        adapter =new RecyclerViewAdapter(this, list);
    }

    private void initViews()
    {
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logOut:
                alertDialog();
                break;

            case R.id.user_mobile:

                Intent intent = new Intent(AllRegisteredUserActivity.this,UsersMobileNo.class);
                startActivity(intent);

                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void alertDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this,R.style.AppTheme2);
        builder.setTitle("Alert Dialog");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new AdminPerfManager(getApplicationContext()).clear();
                Intent intent=new Intent(AllRegisteredUserActivity.this,AdminActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setMessage("Are You want to logout");
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        alertDialogBackPress();
    }
    public void alertDialogBackPress() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this,R.style.AppTheme2);
        builder.setTitle("Alert Dialog");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setMessage("Are You want to Exit");
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

}
