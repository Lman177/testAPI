package vn.edu.usth.gmail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;
    Button buttonReg, uncheck;
    FirebaseAuth mAuth;
    TextView textView, emailStatus;
    DatabaseReference mRootRef;
    static int usernameFlag = 0;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Change status bar background to the corresponding color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.background_login));
        }

        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.btn_register);
        textView = findViewById(R.id.loginNow);
        emailStatus = findViewById(R.id.availability_email);
        uncheck = findViewById(R.id.check_email);

        pd = new ProgressDialog(this);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(email, password);
                }
            }
        });

        uncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGmailAvailability();
            }
        });
    }

    private void checkGmailAvailability() {
        final String txt_email = editTextEmail.getText().toString();

        emailStatus.setVisibility(View.VISIBLE); // Always set it to visible at the beginning

        DatabaseReference mUsersRed = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersRed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null && txt_email.equals(user.getEmail())) {
                        usernameFlag = 1;
                        emailStatus.setText("Unavailable! Username already taken! Try something else.");
                        return;
                    } else if (txt_email.equals("")) {
                        usernameFlag = 1;
                        emailStatus.setText("Unavailable! Please enter an email.");
                        return;
                    }
                }
                if (usernameFlag == 0) {
                    emailStatus.setText("Available!"); // Set it to "Available!" only when the email is available
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error here
            }
        });
    }


    private void registerUser(String email, String password) {
        pd.setMessage("Please Wait!");
        pd.show();

        if (usernameFlag == 0) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                if (firebaseUser != null) {
                                    String userid = firebaseUser.getUid();

                                    mRootRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("id", userid);

                                    mRootRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                pd.dismiss();
                                                emailStatus.setText("Available!");
                                                Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }
                            } else {
                                pd.dismiss();
                                Toast.makeText(RegisterActivity.this, "You can't register with this email and password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            pd.dismiss();
            Toast.makeText(this, "Please change the email!", Toast.LENGTH_SHORT).show();
        }
    }
}
