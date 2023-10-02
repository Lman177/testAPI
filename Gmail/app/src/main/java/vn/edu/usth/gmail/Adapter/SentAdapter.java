package vn.edu.usth.gmail.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import vn.edu.usth.gmail.Email;
import vn.edu.usth.gmail.MainActivity;
import vn.edu.usth.gmail.R;
import vn.edu.usth.gmail.User;

public class SentAdapter extends RecyclerView.Adapter<SentAdapter.ViewHolder> {
    private Context mContext;
    private List<Email> email;

    String emailid;

    private FirebaseUser firebaseUser;

    public SentAdapter(Context mContext, List<Email> email, String emailid, FirebaseUser firebaseUser) {
        this.mContext = mContext;
        this.email = email;
        this.emailid = emailid;
        this.firebaseUser = firebaseUser;
    }

    @NonNull
    @Override
    public SentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_sent, parent, false);
        return new SentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Email emailItem = email.get(position);


        holder.textName.setText(emailItem.getName());
        holder.textHmail.setText(emailItem.getHead_mail());
        holder.textContent.setText(emailItem.getContent());
        holder.textDate.setText(emailItem.getDate());

        FirebaseDatabase.getInstance().getReference().child("Users").child(email.subList()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                holder.textName.setText(user.getEmail());
                if (user.getImage().equals("default")) {
                    holder.imageView.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(user.getImageurl()).into(holder.imageProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.emailItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("publisherId", email.subList());
                mContext.startActivity(intent);
            }
        });

        
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (email.subList().equals(firebaseUser.getUid())) {
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("Delete this comment?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            FirebaseDatabase.getInstance().getReference().child("Comments")
                                    .child(postid).child(email.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(mContext, "Comment deleted successfully!", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                        }
                    });

                    alertDialog.show();
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return email.size();
    }



    public class ViewHolder  extends RecyclerView.ViewHolder{
        public TextView textName, textHmail, textContent, textDate;
        public ImageView imageView;
        public View emailItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.name);
            textHmail = itemView.findViewById(R.id.head_email);
            textContent = itemView.findViewById(R.id.content);
            imageView = itemView.findViewById(R.id.imageview);
            textDate= itemView.findViewById(R.id.date);


        }
    }
}
