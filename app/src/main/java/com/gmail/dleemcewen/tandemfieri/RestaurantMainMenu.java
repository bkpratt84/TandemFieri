package com.gmail.dleemcewen.tandemfieri;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gmail.dleemcewen.tandemfieri.Adapters.ManageRestaurantExpandableListAdapter;
import com.gmail.dleemcewen.tandemfieri.Entities.NotificationMessage;
import com.gmail.dleemcewen.tandemfieri.Entities.Restaurant;
import com.gmail.dleemcewen.tandemfieri.Entities.User;
import com.gmail.dleemcewen.tandemfieri.Logging.LogWriter;
import com.gmail.dleemcewen.tandemfieri.Repositories.NotificationMessages;
import com.gmail.dleemcewen.tandemfieri.Tasks.TaskResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.logging.Level;

public class RestaurantMainMenu extends AppCompatActivity {

    private User user;
    private NotificationMessages<NotificationMessage> notificationsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_main_menu);

        notificationsRepository = new NotificationMessages<>(RestaurantMainMenu.this);

        Bundle bundle = this.getIntent().getExtras();
        user = (User) bundle.getSerializable("User");

        int notificationId = bundle.getInt("notificationId");
        if (notificationId != 0) {
            NotificationManager notificationManager =
                    (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

            notificationManager.cancel(notificationId);

            notificationsRepository
                .find("notificationId = '" + notificationId + "'")
                .addOnCompleteListener(RestaurantMainMenu.this, new OnCompleteListener<TaskResult<NotificationMessage>>() {
                    @Override
                    public void onComplete(@NonNull Task<TaskResult<NotificationMessage>> task) {
                        List<NotificationMessage> messages = task.getResult().getResults();
                        if (!messages.isEmpty()) {
                            notificationsRepository.remove(messages.get(0));
                        }
                    }
                });
        }

        LogWriter.log(getApplicationContext(), Level.INFO, "The user is " + user.getEmail());
    }//end onCreate

    @Override
    protected void onStart() {
        super.onStart();
        if (notificationsRepository == null) {
            notificationsRepository = new NotificationMessages<>(RestaurantMainMenu.this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        notificationsRepository.finalize();
        notificationsRepository = null;
    }

    //create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.restaurant_owner_menu, menu);
        return true;
    }

    //determine which menu option was selected and call that option's action method
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sign_out:
                signOut();
                return true;
            case R.id.edit_personal_info:
                editPersonalInformation();
                return true;
            case R.id.edit_password:
                editPassword();
                return true;
            case R.id.manage_restaurants:
                goToManageRestaurants();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //called when user selects sign out from the drop down menu
    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Exit me", true);
        startActivity(intent);
        finish();
    }

    //called when user selects edit information from the drop down menu
    private  void editPersonalInformation(){
        //need to send user type so that the user can be located in the database
        Bundle bundle = new Bundle();
        Intent intent = new Intent(RestaurantMainMenu.this, EditAccountActivity.class);
        bundle.putSerializable("User", user);
        intent.putExtras(bundle);
        intent.putExtra("UserType", "Restaurant");
        startActivity(intent);
    }

    //called when user selects edit password from the drop down menu
    private void editPassword(){
        //need to send user type so that the user can be located in the database
        Bundle bundle = new Bundle();
        Intent intent = new Intent(RestaurantMainMenu.this, EditPasswordActivity.class);
        bundle.putSerializable("User", user);
        intent.putExtras(bundle);
        intent.putExtra("UserType", "Restaurant");
        startActivity(intent);
    }

    //called when user selects manage restaurants from the drop down menu
    //for now this goes to main menu until I get the name of the activity - look on git hub?
    private void goToManageRestaurants(){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(RestaurantMainMenu.this, ManageRestaurants.class);
        bundle.putSerializable("User", user);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}//end Activity
