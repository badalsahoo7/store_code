package com.example.vivify_technocrats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {
    private ImageView profileImageView;
    private Button uploadImageButton;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Button buttonback;
    private Button checkBoxHelmetYes, checkBoxHelmetNo;
    private Button checkBoxShoeYes, checkBoxShoeNo;
    private TextInputEditText editTextText, dateEditText, timeEditText;
    private Button button3;
    private Spinner locationSpinner;
    private CheckBox checkBoxHelmet, checkBoxShoe;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference usersRef = db.getReference().child("Users2");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Initialize views
        editTextText = findViewById(R.id.editTextText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        locationSpinner = findViewById(R.id.locationSpinner);
        checkBoxHelmet = findViewById(R.id.checkBoxHelmet);
        checkBoxShoe = findViewById(R.id.checkBoxShoe);
        checkBoxHelmetYes = findViewById(R.id.checkBoxHelmetYes);
        checkBoxHelmetNo = findViewById(R.id.checkBoxHelmetNo);
        checkBoxShoeYes = findViewById(R.id.checkBoxShoeYes);
        checkBoxShoeNo = findViewById(R.id.checkBoxShoeNo);
        buttonback = findViewById(R.id.buttonback);
        button3 = findViewById(R.id.button3);

        // Set up location spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.locations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        // Button back listener
        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Checkbox listeners
        checkBoxHelmetYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxHelmet.setChecked(true);
            }
        });

        checkBoxHelmetNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxHelmet.setChecked(false);
            }
        });

        checkBoxShoeYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxShoe.setChecked(true);
            }
        });

        checkBoxShoeNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxShoe.setChecked(false);
            }
        });

        // Submit button listener
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextText.getText().toString();
                String date = dateEditText.getText().toString();
                String time = timeEditText.getText().toString();
                String location = locationSpinner.getSelectedItem().toString();
                String helmet = checkBoxHelmet.isChecked() ? "Yes" : "No";
                String shoe = checkBoxShoe.isChecked() ? "Yes" : "No";

                if (!name.isEmpty() && !date.isEmpty() && !time.isEmpty()) {
                    User2 user2 = new User2(name, date, time, location, helmet, shoe);
                    usersRef.push().setValue(user2);
                    Toast.makeText(UserActivity.this, "Details Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserActivity.this, PlayingVideos.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(UserActivity.this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Date picker dialog
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Time picker dialog
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        // for image
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        profileImageView = findViewById(R.id.profileImageView);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    // Method to show date picker dialog
    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(UserActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        dateEditText.setText(date);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    // Method to show time picker dialog
    private void showTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(UserActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String time = hourOfDay + ":" + minute;
                        timeEditText.setText(time);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);
                // Retrieve the user ID for this profile picture
                String userId = getCurrentUserId(); // Implement this method to get the current user's ID
                uploadImage(userId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    // Method to upload image to Firebase Storage
    private void uploadImage(String userId) {
        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child("profile_images/" + userId + "/" + System.currentTimeMillis() + ".jpg");
            fileReference.putFile(imageUri)
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return fileReference.getDownloadUrl();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                if (downloadUri != null) {
                                    String imageUrl = downloadUri.toString();
                                    // Save the imageUrl in Firebase Database under the user's profile
                                    usersRef.child(userId).child("profileImageUrl").setValue(imageUrl);
                                    Toast.makeText(UserActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(UserActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    // Method to update user score in Firebase Database
    private void updateUserScore(String userId, int correctCount, int wrongCount) {
        DatabaseReference userScoreRef = db.getReference().child("UserScores").child(userId);
        Map<String, Object> scoreUpdates = new HashMap<>();
        scoreUpdates.put("correctCount", correctCount);
        scoreUpdates.put("wrongCount", wrongCount);
        userScoreRef.updateChildren(scoreUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    fetchUserNameAndDisplay(userId);
                } else {
                    Toast.makeText(UserActivity.this, "Failed to Update Score", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to fetch user name from Users2 node and display it
    private void fetchUserNameAndDisplay(String userId) {
        DatabaseReference userRef = db.getReference().child("Users2").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String userName = snapshot.child("name").getValue(String.class);
                    displayUserName(userName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserActivity.this, "Failed to fetch user name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to display user name
    private void displayUserName(String userName) {
        Toast.makeText(UserActivity.this, "User Name: " + userName, Toast.LENGTH_SHORT).show();
    }
}
