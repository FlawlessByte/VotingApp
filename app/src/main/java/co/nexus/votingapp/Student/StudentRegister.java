package co.nexus.votingapp.Student;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.obsez.android.lib.filechooser.ChooserDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import co.nexus.votingapp.Helpers.Constants;
import co.nexus.votingapp.Helpers.Student;
import co.nexus.votingapp.Login.PhoneNoActivity;
import co.nexus.votingapp.R;

public class StudentRegister extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private Button buttonRegister;
    private RadioGroup radioGroupStudentGender;
    private EditText editTextStudentName, editTextStudentDOB, editTextStudentAdmNo,
            editTextStudentDept, editTextStudentYOJ, editTextStudentYOS, editTextStudentPhone, editTextTeacherId;
    private FirebaseAuth mAuth;
    private String username, password;
    private final String TAG = "StudentRegister";
    private LinearLayout studentPhotoLinearLayout;
    private ImageView studentProfileImageView;
    private TextView studentImagePathTextView;
    private boolean isDOBValid = false;
    private int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        initToolbar();
        setContentView(R.layout.activity_student_register);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");

        buttonRegister = findViewById(R.id.buttonStudentRegister);
        editTextStudentName = findViewById(R.id.editTextStudentName);
        editTextStudentDOB = findViewById(R.id.editTextStudentDOB);
        editTextStudentAdmNo = findViewById(R.id.editTextStudentAdmNo);
        editTextStudentDept = findViewById(R.id.editTextStudentDepartment);
        editTextStudentYOJ = findViewById(R.id.editTextStudentYOJ);
        editTextStudentYOS = findViewById(R.id.editTextStudentYOS);
        editTextStudentPhone = findViewById(R.id.editTextStudentPhone);
        radioGroupStudentGender = findViewById(R.id.radioGroupStudentGender);
        studentPhotoLinearLayout = findViewById(R.id.studentPhotoLinearLayout);
        studentProfileImageView = findViewById(R.id.studentProfileImageView);
        studentImagePathTextView = findViewById(R.id.studentImagePathTextView);
        editTextTeacherId = findViewById(R.id.editTextStudentTeacherId);



        editTextStudentDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "DOB clicked");
                showDatePickerDialog();
            }
        });


        studentPhotoLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Student photo linear layout clicked");
                doProfileImageSelectionStuff();
            }
        });


        editTextStudentDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Departments Clicked!", Toast.LENGTH_SHORT).show();
                showDepartmentDialog(v);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Register button pressed");
                if(checkInputs() == 0){
                    // No errors, insert into database
//                    writeRegisterInfoToDB();

                    Student student = getUserObject();

                    Intent intent = new Intent(StudentRegister.this, PhoneNoActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    intent.putExtra("phone",editTextStudentPhone.getText().toString());
                    intent.putExtra("user", student);
                    intent.putExtra("category", "teacher");
                    startActivity(intent);
                }
            }
        });

    }


    private void showDatePickerDialog(){
        DatePickerDialog pickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                Log.d(TAG, "onDateSet");

                //validate age
                Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -18);
                if(minAdultAge.before(userAge)){
                    //User age is less than 18
                    Log.d(TAG, "User age is below 18");
                    Toast.makeText(StudentRegister.this, "The user age cannot be less than 18!", Toast.LENGTH_SHORT).show();
                    isDOBValid = false;
                }
                else{
                    editTextStudentDOB.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    isDOBValid = true;
                }


            }
        }, 2000, 1, 1);

        pickerDialog.show(getSupportFragmentManager(), "DatePickerDialog");
    }


    private void doProfileImageSelectionStuff(){

        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, PICK_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            //TODO: action
            Log.d(TAG,"Image selected");
            //data.getData return the content URI for the selected Image
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();
            //Get the column index of MediaStore.Images.Media.DATA
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            //Gets the String value in the column
            String path = cursor.getString(columnIndex);
            cursor.close();
            Log.d(TAG, "File chosen : "+path);
            studentImagePathTextView.setText(path);
//            studentProfileImageView.setImageBitmap(BitmapFactory.decodeFile(path));
            Glide.with(this).load(new File(path)).into(studentProfileImageView);
        }


    }


    private Student getUserObject(){
        String name, dob, phone, gender, admnNo, dept;
        name = editTextStudentName.getText().toString();
        dob = editTextStudentDOB.getText().toString();
        phone = editTextStudentPhone.getText().toString();
        int selectedId = radioGroupStudentGender.getCheckedRadioButtonId();
        gender = ((RadioButton)findViewById(selectedId)).getText().toString();
        admnNo = editTextStudentAdmNo.getText().toString();
        dept = editTextStudentDept.getText().toString();
        int yoj, yos;
        yoj = Integer.parseInt(editTextStudentYOJ.getText().toString());
        yos = Integer.parseInt(editTextStudentYOS.getText().toString());
        String path = studentImagePathTextView.getText().toString();
        String tid = editTextTeacherId.getText().toString();

        Student student = new Student(name, dob, phone, username, gender, admnNo, dept, yoj, yos, true, false, false, path, tid);

        return student;

    }


    private int checkInputs(){
        int errorCount = 0;

        if(TextUtils.isEmpty(editTextStudentName.getText())) {
            editTextStudentName.setError("Please provide a name!");
            errorCount++;
        }

        if(TextUtils.isEmpty(editTextStudentDOB.getText())) {
            editTextStudentDOB.setError("Please provide a date of birth!");
            errorCount++;
        }


        if(TextUtils.isEmpty(editTextStudentAdmNo.getText())) {
            editTextStudentAdmNo.setError("Please provide the admission number!");
            errorCount++;
        }

        if(TextUtils.isEmpty(editTextStudentYOJ.getText())) {
            editTextStudentYOJ.setError("Please provide the joining year!");
            errorCount++;
        }

        if(TextUtils.isEmpty(editTextStudentYOS.getText())) {
            editTextStudentYOS.setError("Please provide the year of study!");
            errorCount++;
        }

        if(TextUtils.isEmpty(editTextStudentPhone.getText())) {
            editTextStudentPhone.setError("Please provide a phone number!");
            errorCount++;
        }

        if(TextUtils.isEmpty(editTextTeacherId.getText())) {
            editTextStudentPhone.setError("Please enter your tutor's ID!");
            errorCount++;
        }


        String ph_no = editTextStudentPhone.getText().toString();
        if(ph_no.length() != 10 || !TextUtils.isDigitsOnly(ph_no)) {
            errorCount++;
            editTextStudentPhone.setError("Enter a valid phone number!");
        }

        if(editTextStudentDept.getText().toString().equals("Department")) {
            Toast.makeText(this, "Please select a department!", Toast.LENGTH_SHORT).show();
            errorCount++;
        }

        if(studentImagePathTextView.getText().toString().equals("Add Photo")){
            Toast.makeText(this, "Please select a photo!", Toast.LENGTH_SHORT).show();
            errorCount++;
        }



        return errorCount;

    }


    private void showDepartmentDialog(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Department");
        builder.setSingleChoiceItems(Constants.departments, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(Constants.departments[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

}
