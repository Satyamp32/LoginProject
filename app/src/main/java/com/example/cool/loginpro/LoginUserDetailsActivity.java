package com.example.cool.loginpro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class LoginUserDetailsActivity  extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener   {

    DatePickerDialog picker;
    private static final int PICTURE_SELECTED = 1;
    private Button mEdit,mUpdate;
    private EditText mFirstName, mLastName, mEmail, mPassword, mMobileNo, mDob, mId;
    private DatabaseHelper databaseHelper;
    private User user;
    private DatePickerDialog datePickerDialog;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView nav_username,nav_email;
    private ImageView imageView,nav_image;
    String emailFromPref,email,emailFromIntent;
    TextView error;
    Bitmap bitmap;
    String finalDateOfBirth ="";
    private Uri filePath;
    private static final int PICK_IMAGE_REQUEST = 234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        gettingMail();
        emailFromIntent = getIntent().getStringExtra("EMAIL");
        initViews();
        initListeners();
        falseEditText();
        trueEditText();
        initObjects();
        mDob.setKeyListener(null);
        if(emailFromPref.isEmpty())
        {
            email=emailFromIntent;
        }
        else
            email=emailFromPref;
        getAllCandidate();

        //mEdit.setVisibility(View.VISIBLE);
        //mUpdate.setVisibility(View.GONE);
    }

    public void gettingMail()
    {
        emailFromPref=  new UserPerfManager(this).getEmail();
    }

    private void initViews() {
        error=(TextView)findViewById(R.id.error1);
        mId = (EditText) findViewById(R.id.editTextId);
        imageView = (ImageView) findViewById(R.id.imageview3);
        mEdit = (Button) findViewById(R.id.buttonEdit);
        mUpdate = (Button) findViewById(R.id.buttonUpdate);
        mFirstName = (EditText) findViewById(R.id.editTextFirstname);
        mLastName = (EditText) findViewById(R.id.editTextLastname);
        mEmail = (EditText) findViewById(R.id.editTextEmail_id);
        mPassword = (EditText) findViewById(R.id.editTextPassword);
        mMobileNo = (EditText) findViewById(R.id.editTextPhone_number);
        mDob = (EditText) findViewById(R.id.editTextDate_of_birth);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        nav_username=(TextView)navigationView.getHeaderView(0).findViewById(R.id.nav_username);
        nav_email=(TextView)navigationView.getHeaderView(0).findViewById(R.id.nav_email);
        nav_image=(ImageView)navigationView.getHeaderView(0).findViewById(R.id.imageView1);
    }

    private void initListeners() {
        mEdit.setOnClickListener( this);
        mUpdate.setOnClickListener( this);
        imageView.setOnClickListener( this);
        mDob.setOnClickListener( this);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void initObjects() {
        databaseHelper = new DatabaseHelper(this);
        user = new User();
    }



    // public void edit(View view) {
    // Intent intent = new Intent(LoginUserDetailsActivity.this, UpdateDetailsActivity.class);
    //  startActivity(intent);
    // }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_user_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout1:
                alertDialog();
                break;
            case R.id.home:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonEdit:
                trueEditText();
                mEdit.setVisibility(View.GONE);
                mUpdate.setVisibility(View.VISIBLE);
                break;
            case R.id.buttonUpdate:
                updateButton();
                //falseEditText();
                break;
            case R.id.imageview3:
                selectProfileImage();
                break;
            case R.id.editTextDate_of_birth:
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(LoginUserDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mDob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        finalDateOfBirth= mDob.getText().toString();

                    }
                }, year, month, day);
                picker.show();
                break;
        }
    }

    private void falseEditText() {
        imageView.setEnabled(false);
        mId.setEnabled(false);
        mFirstName.setEnabled(false);
        mLastName.setEnabled(false);
        mEmail.setEnabled(false);
        mPassword.setEnabled(false);
        mMobileNo.setEnabled(false);
        mDob.setEnabled(false);
    }

    private void trueEditText() {
        imageView.setEnabled(true);
        mId.setEnabled(true);
        mFirstName.setEnabled(true);
        mLastName.setEnabled(true);
        mEmail.setEnabled(true);
        mPassword.setEnabled(true);

        mMobileNo.setEnabled(true);
        mDob.setEnabled(true);
    }

    public void gallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 4;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Log.d("filepath",filePath.getPath());
                Log.d("Bitmap", String.valueOf(bitmap));
                Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setShader(shader);
                paint.setAntiAlias(true);
                Canvas c = new Canvas(circleBitmap);
                c.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);

                imageView.setImageBitmap(circleBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    public void updateButton() {
        String fname=mFirstName.getText().toString();
        String lname=mLastName.getText().toString();
        String password=mPassword.getText().toString();

        String dateofbirth=mDob.getText().toString();
        String phoneNo =mMobileNo.getText().toString();
        String email =mEmail.getText().toString();

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        bitmap=imageView.getDrawingCache();
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        byte [] data=outputStream.toByteArray();
        error.setVisibility(View.INVISIBLE);
        mFirstName.setCursorVisible(true);
        mLastName.setCursorVisible(true);
        mEmail.setCursorVisible(true);
        mDob.setCursorVisible(true);
        mMobileNo.setCursorVisible(true);
        mPassword.setCursorVisible(true);

        /*if(finalDateOfBirth.isEmpty())
        {
            mDob.setError("date is mandatory");
        }*/
        if(imageView.getDrawable() ==null )
        {
            mFirstName.setCursorVisible(false);
            mLastName.setCursorVisible(false);
            mEmail.setCursorVisible(false);
            mDob.setCursorVisible(false);
            mMobileNo.setCursorVisible(false);
            mPassword.setCursorVisible(false);

            //textInputLayout.setError("image is mandatory");
            Toast.makeText(getApplicationContext(),"image is mandatory",Toast.LENGTH_LONG).show();
            error.setVisibility(View.VISIBLE);
        }


        else if (fname.equals("")&& imageView.getDrawable()!=null )
        {
            mFirstName.setError("First Name is Mandatory");
        }
        else if(lname.equals("") && !fname.equals("")&& imageView.getDrawable()!=null)
        {
            mLastName.setError("Last Name is Mandatory");
        }
        else if(email.equals("")|| !Patterns.EMAIL_ADDRESS.matcher(email).matches() && !lname.equals("") && !fname.equals("")&& imageView.getDrawable()!=null)
        {
            mEmail.setError("Enter Correct Email Format");
        }
        else if(phoneNo.equals("")||!Patterns.PHONE.matcher(phoneNo).matches() && !email.equals("")|| !Patterns.EMAIL_ADDRESS.matcher(email).matches() && !lname.equals("") && !fname.equals("")&& imageView.getDrawable()!=null)
        {
            mMobileNo.setError(" Mobile Number is Mandatory");
        }
        else if (phoneNo.length()!=10 && !phoneNo.equals("")||!Patterns.PHONE.matcher(phoneNo).matches() && !email.equals("")|| !Patterns.EMAIL_ADDRESS.matcher(email).matches() && !lname.equals("") && !fname.equals("")&& imageView.getDrawable()!=null)
        {
            mMobileNo.setError("Please Enter 10 digit Mobile Number");
        }
        else if(dateofbirth.equals(""))
        {
            mDob.setError("Date of birth is mandatory");
        }
        else if(password.equals(""))
        {
            mPassword.setError("Password is Mandatory");
        }

        {
            user.setId(mId.getId());
            user.setmFirstName(mFirstName.getText().toString().trim());
            user.setmLastName(mLastName.getText().toString().trim());
            user.setmDob(mDob.getText().toString().trim());
            user.setmEmailId(mEmail.getText().toString().trim());
            user.setmMobileNo(mMobileNo.getText().toString().trim());
            user.setmPassword(mPassword.getText().toString().trim());

            user.setImage(getStringImage(bitmap));
            if (databaseHelper.updateCandidate(user,data)) {
                Toast.makeText(LoginUserDetailsActivity.this, "updated successfully", Toast.LENGTH_LONG).show();
                falseEditText();
                mEdit.setVisibility(View.VISIBLE);
                mUpdate.setVisibility(View.GONE);
            }
            else
            {
                Toast.makeText(LoginUserDetailsActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {

        alertDialogBackPressed();
    }

    public void getAllCandidate() {
        //String s = getIntent().getStringExtra("EMAIL");
        databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from durga WHERE candidate_email = '" + email + "'", null);
        cursor.moveToFirst();
        mId.setText(cursor.getString(0));
        mFirstName.setText(cursor.getString(1));
        mLastName.setText(cursor.getString(2));
        mDob.setText(cursor.getString(3));
        mEmail.setText(cursor.getString(4));
        mMobileNo.setText(cursor.getString(5));
        mPassword.setText(cursor.getString(6));
        byte b[] = cursor.getBlob(7);
        if (b != null) {
            Bitmap bp = BitmapFactory.decodeByteArray(b, 0, b.length);
            imageView.setImageBitmap(bp);
        } else {
            imageView.setImageResource(R.drawable.user_icon);
        }
        Bitmap bp = BitmapFactory.decodeByteArray(b, 0, b.length);
        //setting user name and Email id of the user to navigation drawer header
        nav_username.setText(cursor.getString(1));
        nav_email.setText(cursor.getString(4));
        nav_image.setImageBitmap(bp);

    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.logout_nav:

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this,R.style.AppTheme2);
                alertDialog.setTitle("Confirm Logout");
                alertDialog.setMessage("Are you sure you want to Logout?");
                alertDialog.setIcon(R.drawable.alert);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new UserPerfManager(getApplicationContext()).clear();
                        Intent intent = new Intent(LoginUserDetailsActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();

                break;

            case R.id.nav_call:

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+919880447564"));
                startActivity(intent);
                break;

            case R.id.nav_msg:

                Intent intent1 = new Intent(Intent.ACTION_MAIN);
                intent1.addCategory(Intent.CATEGORY_APP_MESSAGING);
                startActivity(intent1);
                break;

            case R.id.nav_email:

                Intent intent2 = new Intent(Intent.ACTION_SENDTO);
                //intent2.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent2.setData(Uri.parse("mailto:vgowthamteddy@gmail.com"));
                startActivity(intent2);
                break;

            case R.id.webview_nav:

                Intent intent3 = new Intent(LoginUserDetailsActivity.this,WebViewActivity.class);
                startActivity(intent3);
                break;


        }
        return false;

    }


    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 85, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    public void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AppTheme2);
        builder.setTitle("Alert Dialog");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new UserPerfManager(getApplicationContext()).clear();
                Intent intent = new Intent(LoginUserDetailsActivity.this, LoginActivity.class);
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
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void alertDialogBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AppTheme2);
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
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    private void requestCameraPermission() {


        // Camera permission has not been granted yet. Request it directly.
        ActivityCompat.requestPermissions(LoginUserDetailsActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                0);

    }

    public void selectProfileImage()
    {

        //Toast.makeText(getApplicationContext(),"welcome",Toast.LENGTH_LONG).show();

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)

        {
            requestCameraPermission();
            gallery();
        }
        else{
            //Toast.makeText(getApplicationContext(),"not working",Toast.LENGTH_LONG).show();
            gallery();

        }
    }



}