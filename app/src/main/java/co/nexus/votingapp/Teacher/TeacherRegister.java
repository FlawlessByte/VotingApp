package co.nexus.votingapp.Teacher;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.nexus.votingapp.Helpers.Teacher;
import co.nexus.votingapp.R;
import co.nexus.votingapp.Login.PhoneNoActivity;

public class TeacherRegister extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private Button buttonRegister;
    private RadioGroup radioGroupTeacherGender;
    private EditText editTextTeacherName, editTextTeacherDOB, editTextTeacherDept, editTextTeacherYOJ, editTextTeacherPhone, editTextTeacherID;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");



        buttonRegister = findViewById(R.id.buttonTeacherRegister);
        editTextTeacherName= findViewById(R.id.editTextTeacherName);
        editTextTeacherDOB= findViewById(R.id.editTextTeacherDOB);
        editTextTeacherDept = findViewById(R.id.editTextTeacherDepartment);
        editTextTeacherYOJ = findViewById(R.id.editTextTeacherYOJ);
        editTextTeacherPhone = findViewById(R.id.editTextTeacherPhone);
        radioGroupTeacherGender = findViewById(R.id.radioGroupTeacherGender);
        editTextTeacherID = findViewById(R.id.editTextTeacherId);

        editTextTeacherDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Departments Clicked!", Toast.LENGTH_SHORT).show();
                showDepartmentDialog(v);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputs() == 0){
                    // No errors, insert into database

                    Teacher teacher = getTeacherObject();

                    Intent intent = new Intent(TeacherRegister.this, PhoneNoActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    intent.putExtra("phone",editTextTeacherPhone.getText().toString());
                    intent.putExtra("user", teacher);
                    intent.putExtra("category", "teacher");
                    startActivity(intent);

                }
                else{
                    Toast.makeText(getApplicationContext(), "Please fill all the inputs and try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private Teacher getTeacherObject(){
        String name, dob, phone, gender, id, dept;
        name = editTextTeacherName.getText().toString();
        dob = editTextTeacherDOB.getText().toString();
        id = editTextTeacherID.getText().toString();
        phone = editTextTeacherPhone.getText().toString();
        int selectedId = radioGroupTeacherGender.getCheckedRadioButtonId();
        gender = ((RadioButton)findViewById(selectedId)).getText().toString();
        int yoj = Integer.parseInt(editTextTeacherYOJ.getText().toString());
        dept = editTextTeacherDept.getText().toString();

        Teacher teacher = new Teacher(name, dob, phone, username, gender, id, dept, yoj, false, false);

        return  teacher;
    }


//    private void writeRegisterInfoToDB(){
//
//        Teacher teacher = getTeacherObject();
//
//
//        mDatabase.child("teachers").child(id).setValue(teacher);
//
//        Toast.makeText(getApplicationContext(), "Successfully registered!", Toast.LENGTH_SHORT).show();
//    }

    private int checkInputs(){
        int errorCount = 0;
        if(TextUtils.isEmpty(editTextTeacherName.getText()) || TextUtils.isEmpty(editTextTeacherDOB.getText()) || TextUtils.isEmpty(editTextTeacherID.getText())
                || TextUtils.isEmpty(editTextTeacherYOJ.getText()) || TextUtils.isEmpty(editTextTeacherPhone.getText()))
            errorCount++;

        String ph_no = editTextTeacherPhone.getText().toString();
        if(ph_no.length() != 10 || !TextUtils.isDigitsOnly(ph_no))
            errorCount++;

        if(editTextTeacherDept.getText().toString().equals("Department"))
            errorCount++;

        return errorCount;

    }


    private void showDepartmentDialog(final View v) {
        final String[] array = new String[]{
                "Mathematics", "Physics", "Chemistry", "BCA", "CS", "Zoology"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Department");
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
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