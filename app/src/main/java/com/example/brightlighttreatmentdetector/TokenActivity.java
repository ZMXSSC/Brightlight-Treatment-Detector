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
    private static String userToken; // This is a really clever way to share data between activities(without using intent)!!!!!
    private EditText userInput; // Where the user input the data

    private Button submitButton; // Submit button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);
        // To keep the phone awake while the app is running:
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // Keep the screen awake

        userInput = (EditText) findViewById(R.id.ID_num);
        submitButton = (Button) findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userToken = userInput.getText().toString();
                if(!userToken.isEmpty()){
                    Intent intent = new Intent(TokenActivity.this, MainActivity.class);
                    startActivity(intent); //Switch to the main activity
                }
                else{
                    // Warning message
                    Toast.makeText(getApplicationContext(),"Your ID cannot be empty. Please try again.",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public static String getToken(){ // get the token from another activity
        return userToken;
    }
}