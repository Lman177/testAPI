package vn.edu.usth.gmail;

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


public class ComposeActivity extends AppCompatActivity {

    private EditText mEditTextTo;
    private EditText mEditTextSubject;
    private EditText mEditTextMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Change status bar background to the corresponding
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.background_all));
        }
        setContentView(R.layout.activity_compose);

        ImageButton backIcon = findViewById(R.id.back_icon);
        ImageView horizIcon = findViewById(R.id.horiz_icon);
        ImageView sendIcon = findViewById(R.id.send_icon);
        ImageView linkIcon = findViewById(R.id.link_icon);
        TextView title = findViewById(R.id.toolbar_title);



        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the previous activity or fragment
                Intent intent = new Intent(ComposeActivity.this, MainActivity.class);
                startActivity(intent);

                // Optionally, finish the current activity to remove it from the stack
                finish();
            }
        });

        horizIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ComposeActivity.this, "You click on horiz icon", Toast.LENGTH_SHORT).show();
            }
        });

        sendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ComposeActivity.this, "You click on send icon", Toast.LENGTH_SHORT).show();
            }
        });

        linkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ComposeActivity.this, "You click on link icon", Toast.LENGTH_SHORT).show();
            }
        });


        mEditTextTo = findViewById(R.id.edit_text_to);
        mEditTextSubject = findViewById(R.id.edit_text_subject);
        mEditTextMessage = findViewById(R.id.edit_text_message);



    }



}