package com.example.vivify_technocrats;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {
    Button userhistory;
    TextView textViewCorrectCount,textViewWrongCount;
    ImageView back, exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        userhistory = findViewById(R.id.userhistory);
        userhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this,UserHistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
        textViewCorrectCount = findViewById(R.id.text_correct_count);
        textViewWrongCount = findViewById(R.id.text_wrong_count);

        // Get the correct and wrong counts from the Intent
        int correctCount = getIntent().getIntExtra("correct", 0);
        int wrongCount = getIntent().getIntExtra("wrong", 0);

        // Display the counts
        textViewCorrectCount.setText("Correct: " + correctCount);
        textViewWrongCount.setText("Wrong: " + wrongCount);

        // Initialize WebView



        back = findViewById(R.id.ic_back2);
        exit = findViewById(R.id.ic_exitbutton3);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitDialog();
            }
        });

        TextView textView = findViewById(R.id.textView);
        Animation slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        textView.startAnimation(slideUpAnimation);
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity(); // Exit the application
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Dismiss the dialog
            }
        });
        builder.create().show(); // Show the dialog
    }
}
