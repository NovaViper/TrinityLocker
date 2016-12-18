package com.nova.apps.trinitylocker.core;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.nova.apps.trinitylocker.R;
import com.nova.apps.trinitylocker.core.fragment.LockProfileOptionFragment;
import com.nova.apps.trinitylocker.core.fragment.VisualOptionFragment;
import com.nova.apps.trinitylocker.util.GoogleSignInSingleton;

public class MainScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

	private ImageView profilePic;
	private TextView accountName;
	private TextView accountEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

	    String s = getIntent().getExtras().getString("SignInType");
	    checkAccountSignIn(s);
    }

	private void checkAccountSignIn(String signInType) {
		if (String.valueOf(signInType).matches("google")) {
			//GoogleSignInSingleton signInSingleton = GoogleSignInSingleton.getInstance(null);
			//GoogleSignInAccount acct = signInSingleton.getGoogleSignIn();
			GoogleSignInAccount googleAccount = getIntent().getExtras().getParcelable("Account");

			String personName = googleAccount.getDisplayName();
			String personEmail = googleAccount.getEmail();
			Uri personPhoto = googleAccount.getPhotoUrl();

			profilePic = (ImageView) findViewById(R.id.navProfilePic);
			accountName = (TextView) findViewById(R.id.navProfileName);
			accountEmail = (TextView) findViewById(R.id.navProfileEmail);

			//Set Stuff for Google Sign In
			Glide.with(getApplicationContext()).load(personPhoto)
					.thumbnail(0.5f)
					.crossFade()
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.into(profilePic);
			accountName.setText(personName);
			accountEmail.setText(personEmail);

		} else if (String.valueOf(signInType).matches("facebook")) {

		} else {

		}
	}

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
	    FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_lock_profile) {
            fragmentManager.beginTransaction().replace(R.id.content_main_screen, new LockProfileOptionFragment()).commit();
        } else if (id == R.id.nav_visual) {
	        fragmentManager.beginTransaction().replace(R.id.content_main_screen, new VisualOptionFragment()).commit();
        } else if (id == R.id.nav_notify) {

        } else if (id == R.id.nav_security) {

        } else if(id == R.id.nav_misc){

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
