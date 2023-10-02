package vn.edu.usth.gmail.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import vn.edu.usth.gmail.MainActivity;
import vn.edu.usth.gmail.R;


public class MeetFragment extends Fragment {

    AppCompatButton newMeeting, joinMeeting;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meet, container, false);

        newMeeting = view.findViewById(R.id.new_meeting_btn);
        joinMeeting = view.findViewById(R.id.join_meeting_btn);


        newMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "New meeting is not available right now", Toast.LENGTH_SHORT).show();
            }
        });

        joinMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Join meeting is not available right now", Toast.LENGTH_SHORT).show();
            }
        });






        // Inflate the layout for this fragment
        return view;
    }
}