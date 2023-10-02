package vn.edu.usth.gmail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Detail_1 extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    @SuppressLint("RestrictedApi")
    private boolean isStarSelected = false; // Variable to track star selection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Change status bar background to the corresponding
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.background_all));
        }
        setContentView(R.layout.activity_detail1);



        String name = getIntent().getStringExtra("Name");
        String headMail = getIntent().getStringExtra("Head Mail");
        String content = getIntent().getStringExtra("Content");
        int image = getIntent().getIntExtra("Image", 0);

        TextView Dname = findViewById(R.id.D_name);
        TextView DheadMail = findViewById(R.id.D_head_email);
        TextView Dcontent = findViewById(R.id.D_content);
        ImageView Dimage = findViewById(R.id.D_imageview);

        Dname.setText(name);
        DheadMail.setText(headMail);
        Dcontent.setText(content);
        Dimage.setImageResource(image);

        //back to mainpage

        ImageButton backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the previous activity or fragment
                Intent intent = new Intent(Detail_1.this, MainActivity.class);
                startActivity(intent);

                // Optionally, finish the current activity to remove it from the stack
                finish();
            }
        });

        //popup row_down
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageButton popupButton = findViewById(R.id.row_down);
        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
        //popup more
        ImageButton popupButton2 = findViewById(R.id.more);
        popupButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                morePopup(view);
            }
        });

        //popup more_vert
        ImageButton popupButton3 = findViewById(R.id.more_vert);
        popupButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                morevertPopup(view);
            }
        });

        //star change color
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageButton starButton = findViewById(R.id.star);

        starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStarSelected) {
                    // The star is currently selected, so unselect it
                    starButton.setSelected(false);
                    isStarSelected = false;
                    starButton.setColorFilter(getResources().getColor(R.color.unselectedstarcolor));
                } else {
                    // The star is not selected, so select it
                    starButton.setSelected(true);
                    isStarSelected = true;
                    starButton.setColorFilter(getResources().getColor(R.color.selectedstarcolor));
                }
            }
        });
    }

    public void morePopup(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.options_head);
        popup.show();
    }

    public void showPopup(View view){
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_row_down);
        popup.show();
    }

    public void morevertPopup(View view){
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.options_more_vert);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.from_popup) {
            Toast.makeText(this, "From: hoangnam@gmail.com", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.reply_popup) {
            Toast.makeText(this, "Reply-To: quangdung@gmail.com", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.Bcc_popup) {
            Toast.makeText(this, "Bcc: bi12-ict@st.usth.edu.vn", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.date_popup) {
            Toast.makeText(this, "Date: Sep 20, 2023, 10:12 AM", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }
}
