package com.example.vivify_technocrats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.vivify_technocrats.ModelClass;
import com.example.vivify_technocrats.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    boolean optionsEnabled = true;
    LinearLayout next_button;
    int defaultButtonColor;

    public static ArrayList<ModelClass> list;

    DatabaseReference databaseReference;
    DatabaseReference userScoresReference; // Add this line

    CountDownTimer countDownTimer;
    ProgressBar progressBar;
    int timerValue = 10;

    ModelClass modelClass;
    int index = 0;
    TextView card_question;
    RadioGroup radioGroupOptions;
    RadioButton radioOptionA, radioOptionB, radioOptionC, radioOptionD;

    int correctCount = 0;
    int wrongCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Hooks();

        // Store the default color of the next button
        defaultButtonColor = next_button.getSolidColor();

        list = new ArrayList<>();

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Question");
        userScoresReference = FirebaseDatabase.getInstance().getReference().child("UserScores"); // Add this line

        // Retrieve data from Firebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the list before adding new data
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelClass modelClass = snapshot.getValue(ModelClass.class);
                    list.add(modelClass);
                }
                // Once data retrieval is complete, set the first question
                setQuestion(index);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setMax(100);

        countDownTimer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerValue--;
                int progress = (int) (100 * (millisUntilFinished / 20000.0));
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                //showTimeOutDialog();
            }
        }.start();

        next_button = findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToNextQuestion();
                radioGroupOptions.clearCheck();
                enableOptions();
            }
        });

        // Set click listeners for options
        radioGroupOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (optionsEnabled) {
                    RadioButton selectedRadioButton = findViewById(checkedId);
                    if (selectedRadioButton != null) {
                        checkAnswer(selectedRadioButton.getText().toString());
                    }
                }
            }
        });
    }

    private void Hooks() {
        card_question = findViewById(R.id.card_question);
        radioGroupOptions = findViewById(R.id.radio_group_options);
        radioOptionA = findViewById(R.id.cardA);
        radioOptionB = findViewById(R.id.cardB);
        radioOptionC = findViewById(R.id.cardC);
        radioOptionD = findViewById(R.id.cardD);
        next_button = findViewById(R.id.next_button); // Add this line to hook the next_button
    }

    private void checkAnswer(String selectedAnswer) {
        optionsEnabled = false; // Disable further selections

        String correctAnswer = list.get(index).getAns();
        if (selectedAnswer.equals(correctAnswer)) {
            correctCount++;
        } else {
            wrongCount++;
        }
    }

    private void enableOptions() {
        optionsEnabled = true;
    }

    private void setQuestion(int index) {
        if (index < list.size()) {
            modelClass = list.get(index);
            card_question.setText(modelClass.getQuestion());
            radioOptionA.setText(modelClass.getqA());
            radioOptionB.setText(modelClass.getqB());
            radioOptionC.setText(modelClass.getqC());
            radioOptionD.setText(modelClass.getqD());
            // Reset the button color to its default color
            next_button.setBackgroundColor(defaultButtonColor);
        } else {
            gameWon();
        }
    }

    private void moveToNextQuestion() {
        if (index < list.size() - 1) {
            index++;
            setQuestion(index);

            // Apply animation to the next button
            animateNextButton();
        } else {
            gameWon();
        }
    }

    private void animateNextButton() {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(500);
        next_button.startAnimation(fadeIn);
    }

    private void gameWon() {
        updateScoresInFirebase(); // Add this line to update the scores in Firebase
        Intent intent = new Intent(DashboardActivity.this, ResultActivity.class);
        intent.putExtra("correct", correctCount);
        intent.putExtra("wrong", wrongCount);
        startActivity(intent);
    }

    private void updateScoresInFirebase() {
        String userId = "userId"; // Replace with the actual user ID
        String sessionId = "sessionId"; // Replace with the actual session ID

        Map<String, Object> scoreData = new HashMap<>();
        scoreData.put("correctCount", correctCount);
        scoreData.put("wrongCount", wrongCount);

        userScoresReference.child(userId).child(sessionId).updateChildren(scoreData);
    }

    @Override
    public void onBackPressed() {
        // Show an exit confirmation dialog
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit the quiz?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, which) -> DashboardActivity.super.onBackPressed())
                .setNegativeButton("No", null)
                .show();
    }
}
