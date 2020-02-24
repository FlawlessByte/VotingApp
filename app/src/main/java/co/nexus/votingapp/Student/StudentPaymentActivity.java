package co.nexus.votingapp.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import co.nexus.votingapp.R;

public class StudentPaymentActivity extends AppCompatActivity {
    private RadioButton paytm, gpay, card;
    private EditText paymentEditText;
    private boolean selected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_payment);

        paytm = findViewById(R.id.radioButtonPaytm);
        gpay = findViewById(R.id.radioButtonGPay);
        card = findViewById(R.id.radioButtonCard);
        paymentEditText = findViewById(R.id.editTextPaymentAmount);


        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paytm.setChecked(true);
                gpay.setChecked(false);
                card.setChecked(false);
                selected = true;
            }
        });

        gpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paytm.setChecked(false);
                gpay.setChecked(true);
                card.setChecked(false);
                selected = true;
            }
        });

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paytm.setChecked(false);
                gpay.setChecked(false);
                card.setChecked(true);
                selected = true;
            }
        });

    }

    public void paymentButtonClicked(View v){

        int amount = Integer.parseInt(paymentEditText.getText().toString());

        if(amount < 500){
            Toast.makeText(this, "Enter an amount greater than 500", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!selected){
            Toast.makeText(this, "Please select a payment method!", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show();
    }
}
