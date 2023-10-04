package vn.edu.usth.gmail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.View;
import android.view.Window;

import androidx.core.view.GravityCompat;

import android.view.MenuItem;
import java.util.List;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import vn.edu.usth.gmail.Adapter.CustomAdapter;
import vn.edu.usth.gmail.fragment.AllMailFragment;
import vn.edu.usth.gmail.fragment.ChatFragment;
import vn.edu.usth.gmail.fragment.InboxFragment;
import vn.edu.usth.gmail.fragment.MeetFragment;
import vn.edu.usth.gmail.fragment.SentFragment;
import vn.edu.usth.gmail.fragment.SettingsFragment;
import vn.edu.usth.gmail.fragment.SnoozedFragment;
import vn.edu.usth.gmail.fragment.SpacesFragment;
import vn.edu.usth.gmail.fragment.SpamFragment;
import vn.edu.usth.gmail.fragment.StarFragment;
import vn.edu.usth.gmail.fragment.TrashFragment;

public class MainActivity extends AppCompatActivity implements SelectListener,KeyboardVisibilityUtils.OnKeyboardVisibilityListener{

    RecyclerView recyclerView;
    List<String> emaiList;
    List<Email> emailList;
    CustomAdapter customAdapter;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ExtendedFloatingActionButton compose_button;
    CoordinatorLayout coordinatorLayout;
    BottomAppBar bottomAppBar;
    BottomNavigationView bottomNavigationView;
    KeyboardVisibilityUtils keyboardVisibilityUtils;
    EditText editText;
    SearchView searchView;


    private Fragment currentFragment;



    private boolean isMeetItemSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Change status bar background to the corresponding
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.background_all));
        }

        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        compose_button = findViewById(R.id.Compose);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Recycler View
        searchView = findViewById(R.id.search_view);
        displayItems();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        //End Recyclerview
        TextView textView_meetings = new TextView(this);

        // Enable the Menu Icon to toggle the Menu Bar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        View rootView = findViewById(android.R.id.content);
        keyboardVisibilityUtils = new KeyboardVisibilityUtils(rootView, this);


        // Set a click listener for the navigation button in the toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the navigation drawer on the start (left) side is open
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    // If it's open, close it
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // If it's not open, open it
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });


        compose_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ComposeActivity.class);
                startActivity(intent);
                finish();
            }
        });



        // Side-bar navigation
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawer(GravityCompat.START);
                if (item.getItemId() == R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
                else if (item.getItemId() == R.id.inbox){
                    recyclerView.setVisibility(View.VISIBLE);
                    openFragment(new InboxFragment());
                    return true;
                }
                else if (item.getItemId() == R.id.star){
                    recyclerView.setVisibility(View.GONE);
                    openFragment(new StarFragment());
                    return true;
                }
                else if (item.getItemId() == R.id.settings){
                    recyclerView.setVisibility(View.GONE);
                    openFragment(new SettingsFragment());
                    return true;
                }
                else if (item.getItemId() == R.id.sent){
                    recyclerView.setVisibility(View.GONE);
                    openFragment(new SentFragment());
                    return true;
                }
                else if (item.getItemId() == R.id.allmail){
                    recyclerView.setVisibility(View.VISIBLE);
                    openFragment(new AllMailFragment());
                    return true;
                }
                else if (item.getItemId() == R.id.spam){
                    recyclerView.setVisibility(View.GONE);
                    openFragment(new SpamFragment());
                    return true;
                }
                else if (item.getItemId() == R.id.snoozed){
                    recyclerView.setVisibility(View.GONE);
                    openFragment(new SnoozedFragment());
                    return true;
                }
                else if (item.getItemId() == R.id.trash){
                    recyclerView.setVisibility(View.GONE);
                    openFragment(new TrashFragment());
                    return true;
                }
                return false;
            }
        });



        // Bottom bar navigation
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                textView_meetings.setText(R.string.meet);
                textView_meetings.setTextColor(getResources().getColor(R.color.black_with_alpha_70));
                textView_meetings.setTextSize(20);

                boolean isMeetFragment = currentFragment instanceof MeetFragment;

                if (itemId == R.id.home) {
                    isMeetItemSelected = false;
                    recyclerView.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.VISIBLE);
                    searchView.setQueryHint(getString(R.string.search_in_mail));
                    compose_button.setText(R.string.compose);
                    compose_button.setIconResource(R.drawable.ic_pencil);
                    compose_button.setVisibility(View.VISIBLE);
                    toolbar.removeView(textView_meetings);
                    openFragment(new InboxFragment());
                    return true;
                } else if (itemId == R.id.chat) {
                    isMeetItemSelected = false;
                    recyclerView.setVisibility(View.GONE);
                    searchView.setVisibility(View.VISIBLE);
                    searchView.setQueryHint(getString(R.string.search_in_chat_and_spaces));
                    compose_button.setText(R.string.new_chat);
                    compose_button.setIconResource(R.drawable.chat_icon_compose);
                    compose_button.setVisibility(View.VISIBLE);
                    toolbar.removeView(textView_meetings);
                    openFragment(new ChatFragment());
                    return true;
                } else if (itemId == R.id.space) {
                    isMeetItemSelected = false;
                    recyclerView.setVisibility(View.GONE);
                    searchView.setVisibility(View.VISIBLE);
                    searchView.setQueryHint(getString(R.string.search_in_chat_and_spaces));
                    compose_button.setText(R.string.new_space);
                    compose_button.setIconResource(R.drawable.plus_compose);
                    compose_button.setVisibility(View.VISIBLE);
                    toolbar.removeView(textView_meetings);
                    openFragment(new SpacesFragment());
                    return true;
                } else if (itemId == R.id.meet) {
                        currentFragment = new MeetFragment();
                        isMeetItemSelected = true;
                        recyclerView.setVisibility(View.GONE);
                        searchView.setVisibility(View.GONE);
                        compose_button.setVisibility(View.GONE);
                        toolbar.addView(textView_meetings, new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
                        openFragment(new MeetFragment());
                    return true;
                }

                return false;
            }
        });


        // Add a scroll listener to the RecyclerView
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 && compose_button.isExtended()) {
                    // Scrolling down, and FAB is extended, so shrink it
                    compose_button.shrink();
                    bottomAppBar.setVisibility(View.GONE);

                } else if (dy < 0 && !compose_button.isExtended()) {
                    // Scrolling up, and FAB is not extended, so extend it
                    compose_button.extend();
                    bottomAppBar.setVisibility(View.VISIBLE);

                }
            }
        });

    }
//  Search bar
    private void filter(String newText) {
        List<Email> filteredList = new ArrayList<>();
        for (Email item : emailList){
            if (item.getName().toLowerCase().startsWith(newText.toLowerCase())){
                filteredList.add(item);
            }
        }
        customAdapter.filterList(filteredList);
    }


    @Override
    public void onKeyboardVisibilityChanged(boolean isVisible) {
        if (isVisible) {
            // Keyboard is open, hide the BottomAppBar and FAB
            bottomAppBar.setVisibility(View.GONE);
            compose_button.setVisibility(View.GONE);
        } else {
            // Keyboard is closed, show the BottomAppBar and FAB
            bottomAppBar.setVisibility(View.VISIBLE);
            if (isMeetItemSelected == true){
                compose_button.setVisibility(View.GONE);
        }
            else{
                compose_button.setVisibility(View.VISIBLE);
            }

    }}



    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen((GravityCompat.START))){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
        super.onBackPressed();
    }}
    //Display items recyclerview
    private void displayItems() {
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        emailList = new ArrayList<>();

//        emailList.add(new Email("USTH Student Services", "Đăng kí gian hàng hội chợ", "Thân gửi em, để chào mừng tân sinh viên", R.drawable.a, "Sep 22"));
//        emailList.add(new Email("GED Dept", "CHECK ATTENDANCE LIST", "Find your name in the Excel file below", R.drawable.f,));
//        emailList.add(new Email("Phan Anh", "INTERNSHIP", "Urgent", R.drawable.a));
//        emailList.add(new Email("Hoang Thi Van Anh", "Machine Learning Checklist Attendance", "Dear all, the ICT Department",R.drawable.a,"Sep 21"));
//        emailList.add(new Email("Bui Duc", "GPA", "How about you?", R.drawable.e,"Sep 20"));
//        emailList.add(new Email("Google", "Security Alert", "A new signing on Iphone 15", R.drawable.e,"Sep 20"));
//        emailList.add(new Email("Trinh Thi Thu Trang", "Đăng kí môn lựa chọn", "Dear students", R.drawable.f,"Sep 19"));
//        emailList.add(new Email("Trong Duc", "Retake ADS", "Can you go retake with me?", R.drawable.c,"Sep 19"));
//        emailList.add(new Email("USTH Student Association", "Mid-Autumn Festival ", "Do nothing", R.drawable.a,"Sep 18"));
//        emailList.add(new Email("USTH Student Services", "New Member Recruitment", "Nobody wants to join", R.drawable.b,"Sep 17"));
//        emailList.add(new Email("Trong Duc", "Best TFT Comps", "Học hỏi đi", R.drawable.c,"Sep 15"));
//        emailList.add(new Email("Google", "Security Alert", "A new signing on Iphone 15", R.drawable.e,"Sep 13"));
//        emailList.add(new Email("Student Services", "PHÒNG CHÁY CHỮA CHÁY", "Thân gửi các em sinh viên", R.drawable.f,"Sep 12"));
//        emailList.add(new Email("Hoang Thi Van Anh", "OOAD Student Checklish", "Dear Students,", R.drawable.a, "Sep 11"));
//        emailList.add(new Email("Trinh Thi Thu Trang", "Web Applied Developments", "Find your name in the Excel file below", R.drawable.b, "Sep 10"));
//        emailList.add(new Email("Trong Duc", "SOS", "Bài này khó", R.drawable.c,"Sep 9"));
//        emailList.add(new Email("Bui Duc", "Hi", "Let's play basketball", R.drawable.e,"Sep 8"));
//        emailList.add(new Email("Hoang Nam", "Helloooo", "Welcome!!", R.drawable.f,"Sep 8"));
//        emailList.add(new Email("USTH Student Services", "Đăng kí gian hàng hội chợ", "Thân gửi em, để chào mừng tân sinh viên ", R.drawable.a,"Sep 7"));
//        emailList.add(new Email("GED Dept", "CHECK ATTENDANCE LIST", "Find your name in the Excel file below", R.drawable.f,"Sep 7"));
//        emailList.add(new Email("Phan Anh", "INTERNSHIP", "Urgent", R.drawable.a,"Sep 6"));
//        emailList.add(new Email("Hoang Thi Van Anh", "Machine Learning Checklist Attendance", "Dear all, the ICT Department",R.drawable.a,"Sep 5"));
//        emailList.add(new Email("Bui Duc", "GPA", "How about you?", R.drawable.e,"Sep 5"));
//        emailList.add(new Email("Google", "Security Alert", "A new signing on Iphone 15", R.drawable.e,"Sep 4"));
//        emailList.add(new Email("Trinh Thi Thu Trang", "Đăng kí môn lựa chọn", "Dear students", R.drawable.f,"Sep4"));
//        emailList.add(new Email("Trong Duc", "Retake ADS", "Can you go retake with me?", R.drawable.c,"Sep 3"));
//        emailList.add(new Email("USTH Student Association", "Mid-Autumn Festival ", "Do nothing", R.drawable.a,"Sep 2"));
//        emailList.add(new Email("USTH Student Services", "New Member Recruitment", "Nobody wants to join", R.drawable.b,"Sep 1"));
//        emailList.add(new Email("Trong Duc", "Best TFT Comps", "Học hỏi đi", R.drawable.c,"Sep 1"));
//        emailList.add(new Email("Google", "Security Alert", "A new signing on Iphone 15", R.drawable.e,"Aug 31"));
//        emailList.add(new Email("Student Services", "PHÒNG CHÁY CHỮA CHÁY", "Thân gửi các em sinh viên", R.drawable.f,"Sep 31"));
//        emailList.add(new Email("Hoang Thi Van Anh", "OOAD Student Checklish", "Dear Students,", R.drawable.a,"Sep 30"));
//        emailList.add(new Email("Trinh Thi Thu Trang", "Web Applied Developments", "Find your name in the Excel file below", R.drawable.b,"Sep 29"));
//        emailList.add(new Email("Trong Duc", "SOS", "Bài này khó", R.drawable.c,"Sep 28"));
//        emailList.add(new Email("Bui Duc", "Hi", "Let's play basketball", R.drawable.e,"Sep 28"));
//        emailList.add(new Email("Hoang Nam", "Helloooo", "Welcome!!", R.drawable.f,"Sep 27"));


        customAdapter = new CustomAdapter(this, emailList, this);
        recyclerView.setAdapter(customAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    //Swipe to do delete and archive
    Email deletedMail = null;
    List<String> archivedMail = new ArrayList<>();
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    // Handle left swipe (delete)
                    deletedMail = emailList.get(position); // Get the deleted email
                    emailList.remove(position); // Remove it from the list
                    customAdapter.notifyItemRemoved(position); // Notify the adapter

                    // Show a Snackbar with an undo option
                    Snackbar.make(recyclerView, "Email deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // User clicked "Undo," so add the deleted email back to the list
                                    if (deletedMail != null) {
                                        emailList.add(position, deletedMail);
                                        customAdapter.notifyItemInserted(position);
                                    }
                                }
                            }).show();
                    break;
                case ItemTouchHelper.RIGHT:
                    final Email email = emailList.get(position); // Corrected variable name
                    archivedMail.add(String.valueOf(email)); // Use the correct list name
                    emailList.remove(position);
                    customAdapter.notifyItemRemoved(position);

                    Snackbar make = Snackbar.make(recyclerView, email + ", Archived.", Snackbar.LENGTH_LONG);
                    make.setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            archivedMail.remove(archivedMail.lastIndexOf(email));
                            emailList.add(position, email);
                            customAdapter.notifyItemInserted(position);
                        }
                    });
                    make.show();

                    break;
            }

        }
    };

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(MainActivity.this, Detail_1.class);
        intent.putExtra("Name", emailList.get(position).getName());
        intent.putExtra("Head Mail", emailList.get(position).getHead_mail());
        intent.putExtra("Content", emailList.get(position).getContent());
        intent.putExtra("Image", emailList.get(position).getImage());
        startActivity(intent);
    }

    @Override
    public void onLongItemClick(int position) {

    }


    private void openFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }




}


