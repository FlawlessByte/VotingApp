package co.nexus.votingapp.Student;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
            editTextStudentDept, editTextStudentYOJ, editTextStudentYOS, editTextStudentPhone;
    private FirebaseAuth mAuth;
    private String username, password;
    private final String TAG = "StudentRegister";

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
                else{
                    Toast.makeText(getApplicationContext(), "Please fill all the inputs and try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        Student student = new Student(name, dob, phone, username, gender, admnNo, dept, yoj, yos, true, false, false);

        return student;

    }


//    private void writeRegisterInfoToDB(){
//        Student student = getUserObject();
//
//        mDatabase.child("students").child(mAuth.getUid()).setValue(student);
//
//        Toast.makeText(getApplicationContext(), "Successfully registered!", Toast.LENGTH_SHORT).show();
//    }

    private int checkInputs(){
        int errorCount = 0;
        if(TextUtils.isEmpty(editTextStudentName.getText()) || TextUtils.isEmpty(editTextStudentDOB.getText()) || TextUtils.isEmpty(editTextStudentAdmNo.getText())
        || TextUtils.isEmpty(editTextStudentYOJ.getText()) || TextUtils.isEmpty(editTextStudentYOS.getText()) || TextUtils.isEmpty(editTextStudentPhone.getText()))
            errorCount++;

        String ph_no = editTextStudentPhone.getText().toString();
        if(ph_no.length() != 10 || !TextUtils.isDigitsOnly(ph_no))
            errorCount++;

        if(editTextStudentDept.getText().toString().equals("Department"))
            errorCount++;

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

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Register");
    }
}
