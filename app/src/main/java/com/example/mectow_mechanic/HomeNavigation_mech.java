package com.example.mectow_mechanic;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.mectow_mechanic.ui.about_us.AboutFragment;
import com.example.mectow_mechanic.ui.contact_us.ContactFragment;
import com.example.mectow_mechanic.ui.history.HistoryFragment;
import com.example.mectow_mechanic.ui.home.HomeFragment;
import com.example.mectow_mechanic.ui.notification.NotificationFragment;
import com.example.mectow_mechanic.ui.profile.ProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeNavigation_mech extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    Fragment temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_navigation_mech);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_history2, R.id.nav_notification,R.id.nav_profile,R.id.nav_contact_us,R.id.nav_about_us,R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case  R.id.nav_logout:
                    {
                        showPopup();
                        return true;

                    }

                    case R.id.nav_home:
                    {
                        Toast.makeText(HomeNavigation_mech.this,"Home",Toast.LENGTH_SHORT).show();
                        temp=new HomeFragment();
                        break;
                    }
                    case R.id.nav_about_us:
                    {
                        Toast.makeText(HomeNavigation_mech.this,"About Us",Toast.LENGTH_SHORT).show();
                        temp=new AboutFragment();
                        break;
                    }
                    case R.id.nav_history2:
                    {
                        Toast.makeText(HomeNavigation_mech.this,"History",Toast.LENGTH_SHORT).show();
                        temp=new HistoryFragment();
                        break;
                    }
                    case R.id.nav_contact_us:
                    {
                        Toast.makeText(HomeNavigation_mech.this,"Contact Us",Toast.LENGTH_SHORT).show();
                        temp=new ContactFragment();
                        break;
                    }
                    case R.id.nav_notification:
                    {
                        Toast.makeText(HomeNavigation_mech.this,"Notification",Toast.LENGTH_SHORT).show();
                        temp=new NotificationFragment();
                        break;
                    }
                    case R.id.nav_profile:
                    {
                        Toast.makeText(HomeNavigation_mech.this,"Profile",Toast.LENGTH_SHORT).show();
                        temp=new ProfileFragment();
                        break;
                    }
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,temp).commit();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_navigation_mech, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void showPopup() {
        AlertDialog.Builder alert = new AlertDialog.Builder(HomeNavigation_mech.this);
        alert.setMessage("Are you sure?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener()                 {

                    public void onClick(DialogInterface dialog, int which) {

                        logout(); // Last step. Logout function

                    }
                }).setNegativeButton("Cancel", null);

        AlertDialog alert1 = alert.create();
        alert1.show();
    }

    private void logout() {
        startActivity(new Intent(this, login_mechanic.class));
        Toast.makeText(HomeNavigation_mech.this,"Logged Out",Toast.LENGTH_SHORT).show();
        finish();
    }

}