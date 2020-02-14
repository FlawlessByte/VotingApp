package co.nexus.votingapp.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import co.nexus.votingapp.R;

public class StudentPaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_payment);
    }

    public void paymentButtonClicked(View v){
        Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show();
    }
}
