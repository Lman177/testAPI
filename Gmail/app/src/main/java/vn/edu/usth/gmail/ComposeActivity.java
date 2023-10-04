package vn.edu.usth.gmail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.sql.Timestamp;


public class ComposeActivity extends AppCompatActivity {

    private EditText mEditTextTo;
    private EditText mEditTextSubject;
    private EditText mEditContent, mFrom;
    ImageButton backbtn,moreBtn,sendBtn,linkBtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Change status bar background to the corresponding
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.background_all));
        }
        setContentView(R.layout.activity_compose);

         backbtn = findViewById(R.id.back_icon);
         moreBtn = findViewById(R.id.horiz_icon);
         sendBtn = findViewById(R.id.send_icon);
         linkBtn = findViewById(R.id.link_icon);




        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the previous activity or fragment
                Intent intent = new Intent(ComposeActivity.this, MainActivity.class);
                startActivity(intent);

                // Optionally, finish the current activity to remove it from the stack
                finish();
            }
        });

        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ComposeActivity.this, "You click on horiz icon", Toast.LENGTH_SHORT).show();
            }
        });

        sendBtn.setOnClickListener((v)-> sendEmail());

        linkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ComposeActivity.this, "You click on link icon", Toast.LENGTH_SHORT).show();
            }
        });


        mEditTextTo = findViewById(R.id.txt_to);
        mEditTextSubject = findViewById(R.id.txt_subject);
        mEditContent = findViewById(R.id.txt_content);
        mFrom = findViewById(R.id.txt_from);

    }

    void sendEmail(){
        String to = mEditTextTo.getText().toString();
        String subject = mEditTextSubject.getText().toString();
        String from = mFrom.getText().toString();
        String content = mEditContent.getText().toString();

        if(to==null||to.isEmpty()){
            mEditTextTo.setError("Recipent is require");
            return;
        }
        if(subject==null||subject.isEmpty()){
            mEditTextSubject.setError("Subject is require");
            return;
        }
        if(content==null||content.isEmpty()){
            mEditContent.setError("Recipent is require");
            return;
        }
        Email email = new Email();
        email.setContent(content);
        long currentTimeMillis = System.currentTimeMillis();
        email.setTimestamp(new Timestamp(currentTimeMillis));

        saveEmailToFirebase(email);
    }

    void saveEmailToFirebase(Email email){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionForEmail().document();
        documentReference.set(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utility.showToast(ComposeActivity.this, "Email sent successfully");
                    finish();
                }
                else{
                    Utility.showToast(ComposeActivity.this, "Email sent Fail");
                }
            }
        });;
    }

}