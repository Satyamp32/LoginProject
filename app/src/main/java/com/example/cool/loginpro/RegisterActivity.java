package com.example.cool.loginpro;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

class RegisterActivity  extends AppCompatActivity implements View.OnClickListener{

    private static final int PICTURE_SELECTED=1;
    private final AppCompatActivity activity=RegisterActivity.this;
    private ScrollView mScrollView;
    private TextView mLink;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmailId;
    private EditText mMobileNo;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private EditText mDateOfBirth;
    private ImageView imageView;
    private DatabaseHelper databaseHelper;
    private User user;
    private Button mRegister;

    DatePickerDialog picker;
    TextView error;
    TextInputLayout textInputLayout;
    Bitmap bitmap;
    String finalDate;

    private Uri filePath;
    // private InputValidation inputValidation;

    private static final int PICK_IMAGE_REQUEST = 234;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initListeners();
        initObjects();
        mDateOfBirth.setKeyListener(null);
    }
    public void register(View view)
    {
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    private void initObjects()
    {
        databaseHelper=new DatabaseHelper(this);
        user=new User();
    }
    private void initViews()
    {
        textInputLayout =(TextInputLayout)findViewById(R.id.textinputLayout);
        error=(TextView)findViewById(R.id.error);
        mLink=(TextView)findViewById(R.id.textViewLoginLink);
        mRegister=(Button)findViewById(R.id.register);
        mFirstName=(EditText)findViewById(R.id.firstname);
        mLastName=(EditText)findViewById(R.id.lastname);
        mEmailId=(EditText)findViewById(R.id.email_id);
        mPassword=(EditText)findViewById(R.id.password);
        mMobileNo=(EditText)findViewById(R.id.phone_number);
        mDateOfBirth=(EditText)findViewById(R.id.date_of_birth);
        mConfirmPassword=(EditText)findViewById(R.id.confirm_password);
        imageView =(ImageView)findViewById(R.id.imageview);
    }

    private void initListeners()
    {
        mRegister.setOnClickListener( this);
        mLink.setOnClickListener(this);
        imageView.setOnClickListener(this);
        mDateOfBirth.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.register:
                postDataToSQLite();
                break;
            case R.id.textViewLoginLink:
                Intent intent1=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent1);
                finish();
                break;


            case R.id.imageview:
                // Intent in=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //  in.putExtra("crop",true);
                // in.putExtra("outputX",100);
                //  in.putExtra("outputY",100);
                //  in.putExtra("scale",true);
                //  in.putExtra("return-data",true);
                // startActivityForResult(in,PICTURE_SELECTED);
                selectProfileImage();
                break;
            case R.id.date_of_birth:
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(RegisterActivity.this,R.style.AppTheme2, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mDateOfBirth.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        finalDate=mDateOfBirth.getText().toString();
                    }
                }, year, month, day);
                picker.show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 4;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Log.d("filepath", filePath.getPath());
                Log.d("Bitmap", String.valueOf(bitmap));
                Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setShader(shader);
                paint.setAntiAlias(true);
                Canvas c = new Canvas(circleBitmap);
                c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);

                imageView.setImageBitmap(circleBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 85, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void requestCameraPermission() {


        // Camera permission has not been granted yet. Request it directly.
        ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
    public void gallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void postDataToSQLite()
    {

        String fName = mFirstName.getText().toString();
        String lName = mLastName.getText().toString();
        String email = mEmailId.getText().toString();
        String mobile= mMobileNo.getText().toString();
        String password=mPassword.getText().toString();
        String confirmpassword=mConfirmPassword.getText().toString();
        String date1 =mDateOfBirth.getText().toString();

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        bitmap=imageView.getDrawingCache();
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        byte [] bytes=outputStream.toByteArray();
        error.setVisibility(View.INVISIBLE);

        mFirstName.setCursorVisible(true);
        mLastName.setCursorVisible(true);
        mEmailId.setCursorVisible(true);
        mDateOfBirth.setCursorVisible(true);
        mMobileNo.setCursorVisible(true);
        mPassword.setCursorVisible(true);
        mConfirmPassword.setCursorVisible(true);

        if(imageView.getDrawable() ==null )
        {

            mFirstName.setCursorVisible(false);
            mLastName.setCursorVisible(false);
            mEmailId.setCursorVisible(false);
            mDateOfBirth.setCursorVisible(false);
            mMobileNo.setCursorVisible(false);
            mPassword.setCursorVisible(false);
            mConfirmPassword.setCursorVisible(false);

            //textInputLayout.setError("image is mandatory");
            Toast.makeText(getApplicationContext(),"image is mandatory",Toast.LENGTH_LONG).show();
            error.setVisibility(View.VISIBLE);
        }

        else if (fName.equals("")&& imageView.getDrawable()!=null )
        {
            mFirstName.setError("First Name is Mandatory");
        }
        else if(lName.equals("") && !fName.equals("")&& imageView.getDrawable()!=null)
        {
            mLastName.setError("Last Name is Mandatory");
        }
        else if(email.equals("")|| !Patterns.EMAIL_ADDRESS.matcher(email).matches() && !lName.equals("") && !fName.equals("")&& imageView.getDrawable()!=null)
        {
            mEmailId.setError("Enter Correct Email Format");
        }
        else if(mobile.equals("")||!Patterns.PHONE.matcher(mobile).matches() && !email.equals("")|| !Patterns.EMAIL_ADDRESS.matcher(email).matches() && !lName.equals("") && !fName.equals("")&& imageView.getDrawable()!=null)
        {
            mMobileNo.setError(" Mobile Number is Mandatory");
        }
        else if (mobile.length()!=10 && !mobile.equals("")||!Patterns.PHONE.matcher(mobile).matches() && !email.equals("")|| !Patterns.EMAIL_ADDRESS.matcher(email).matches() && !lName.equals("") && !fName.equals("")&& imageView.getDrawable()!=null)
        {
            mMobileNo.setError("Please Enter 10 digit Mobile Number");
        }
        else if(date1.equals("") /*&& finalDate.equals("")*/)
        {
            mDateOfBirth.setError("Date of birth is mandatory");
        }
        else if(password.equals(""))
        {
            mPassword.setError("Password is Mandatory");
        }

        else if(confirmpassword.equals("")|| !confirmpassword.equals(password))
        {
            mConfirmPassword.setError("Password and ConfirmPassword Should Match");
        }

        else if(!databaseHelper.checkUser(mEmailId.getText().toString().trim()))
        {

            user.setmFirstName(mFirstName.getText().toString().trim());
            user.setmLastName(mLastName.getText().toString().trim());
            user.setmEmailId(mEmailId.getText().toString().trim());
            user.setmPassword(mPassword.getText().toString().trim());
            user.setmDob(mDateOfBirth.getText().toString().trim());
            user.setmMobileNo(mMobileNo.getText().toString().trim());
//            user.setImage(bytes);
            user.setImage(getStringImage(bitmap));
            databaseHelper.addUser(user,bytes);
            Toast.makeText(getApplicationContext(),"Registration successful",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
            intent.putExtra("EMAIL", mEmailId.getText().toString().trim());
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Email Already Exists",Toast.LENGTH_LONG).show();
        }

    }
}