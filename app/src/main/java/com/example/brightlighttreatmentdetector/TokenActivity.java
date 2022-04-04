package com.example.brightlighttreatmentdetector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TokenActivity extends AppCompatActivity {
    private static String userToken; //This is a really clever way to share data between activities!!!!!
    private EditText userInput;

    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);
        // To keep the phone awake while the app is running:
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        userInput = (EditText) findViewById(R.id.ID_num);
        submitButton = (Button) findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userToken = userInput.getText().toString();
                if(!userToken.isEmpty()){
                    Intent intent = new Intent(TokenActivity.this, MainActivity.class);
                    startActivity(intent); //Switch to main activity.
                }
                else{
                    Toast.makeText(getApplicationContext(),"Your ID cannot be empty. Please try again.",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public static String getToken(){
        return userToken;
    }
}