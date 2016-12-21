package com.nova.apps.trinitylocker.core;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.nova.apps.trinitylocker.R;
import com.nova.apps.trinitylocker.core.fragment.LockProfileOptionFragment;
import com.nova.apps.trinitylocker.core.fragment.MiscOptionFragment;
import com.nova.apps.trinitylocker.core.fragment.NotificationOptionFragment;
import com.nova.apps.trinitylocker.core.fragment.SecurityOptionFragment;
import com.nova.apps.trinitylocker.core.fragment.VisualOptionFragment;
import com.nova.apps.trinitylocker.util.CircleTransform;

public class MainScreenActivity extends AppCompatActivity{

	private NavigationView navigationView;
	private DrawerLayout drawer;
	private View navHeader;
	private ImageView profilePic, headerBackground;
	private TextView accountName, accountEmail;
	private Toolbar toolbar;

	// index to identify current nav menu item
	public static int navItemIndex = 0;

	// tags used to attach the fragments
	private static final String TAG_LOCKPROFILE = "lock_profile";
	private static final String TAG_VISUAL = "visual";
	private static final String TAG_NOTIFY = "notification";
	private static final String TAG_SECURITY = "security";
	private static final String TAG_MISC = "misc";
	public static String CURRENT_TAG = TAG_LOCKPROFILE;

	// toolbar titles respected to selected nav menu item
	private String[] activityTitles;

	// flag to load home fragment when user presses back key
	private boolean shouldLoadHomeFragOnBackPress = true;
	private Handler mHandler;

	//Create Share intent
	private ShareActionProvider mShareActionProvider;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main_screen);
	    toolbar = (Toolbar) findViewById(R.id.toolbar);
	    setSupportActionBar(toolbar);

	    mHandler = new Handler();

	    drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
	    navigationView = (NavigationView) findViewById(R.id.nav_view);

	    //Load nav menu header data using the sign in information
	    String s = getIntent().getExtras().getString("SignInType");
	    checkAccountSignIn(s);

	    // load toolbar titles from string resources
	    activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

	    // initializing navigation menu
	    setUpNavigationView();

		if (savedInstanceState == null) {
		    navItemIndex = 0;
		    CURRENT_TAG = TAG_LOCKPROFILE;
		    loadHomeFragment();
	    }
    }

	/***
	 * Returns respected fragment that user
	 * selected from navigation menu
	 */
	private void loadHomeFragment() {
		// selecting appropriate nav menu item
		selectNavMenu();

		//Set Action Bar title to current fragment
		setActionBarTitle();

		// if user select the current navigation menu again, don't do anything and close the navigation drawer
		if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
			drawer.closeDrawers();

			// show or hide the fab button
			//toggleFab();
			return;
		}

		// Sometimes, when fragment has huge data, screen seems hanging when switching between navigation menus
		// So using runnable, the fragment is loaded with cross fade effect
		// This effect can be seen in GMail app
		Runnable mPendingRunnable = new Runnable() {
			@Override
			public void run() {
				// update the main content by replacing fragments
				Fragment fragment = getHomeFragment();
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
						android.R.anim.fade_out);
				fragmentTransaction.replace(R.id.content_main_screen, fragment, CURRENT_TAG);
				fragmentTransaction.commitAllowingStateLoss();
			}
		};

		// If mPendingRunnable is not null, then add to the message queue
		if (mPendingRunnable != null) {
			mHandler.post(mPendingRunnable);
		}

		//Close the drawer on item click
		drawer.closeDrawers();

		//Refresh toolbar menu
		invalidateOptionsMenu();
	}

	private Fragment getHomeFragment(){
		switch(navItemIndex){
			case 0:
				//Lock Proifle Settings Fragment
				LockProfileOptionFragment lockerFragment = new LockProfileOptionFragment();
				return lockerFragment;
			case 1:
				//Visual Settings Fragment
				VisualOptionFragment visualFragment = new VisualOptionFragment();
				return visualFragment;
			case 2:
				//Lock Porifle Fragment
				NotificationOptionFragment notifyFragment = new NotificationOptionFragment();
				return notifyFragment;
			case 3:
				//Lock Porifle Fragment
				SecurityOptionFragment securityFragment = new SecurityOptionFragment();
				return securityFragment;
			case 4:
				//Lock Porifle Fragment
				MiscOptionFragment miscFragment = new MiscOptionFragment();
				return miscFragment;
			default:
				return new LockProfileOptionFragment();
		}
	}

	private void setActionBarTitle(){
		getSupportActionBar().setTitle(activityTitles[navItemIndex]);
	}

	private void selectNavMenu(){
		navigationView.getMenu().getItem(navItemIndex).setCheckable(true);
	}

	private void checkAccountSignIn(String signInType) {
		if (String.valueOf(signInType).matches("google")) {
			//GoogleSignInSingleton signInSingleton = GoogleSignInSingleton.getInstance(null);
			//GoogleSignInAccount acct = signInSingleton.getGoogleSignIn();
			GoogleSignInAccount googleAccount = getIntent().getExtras().getParcelable("Account");

			String personName = googleAccount.getDisplayName();
			String personEmail = googleAccount.getEmail();
			Uri personPhoto = googleAccount.getPhotoUrl();

			View header = navigationView.getHeaderView(0);
			profilePic= (ImageView) header.findViewById(R.id.navProfilePic);
			accountName = (TextView) header.findViewById(R.id.navProfileName);
			accountEmail = (TextView) header.findViewById(R.id.navProfileEmail);

			//Set Stuff for Google Sign In
			Glide.with(this).load(personPhoto)
					.crossFade()
					.thumbnail(0.5f)
					.bitmapTransform(new CircleTransform(this))
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.into(profilePic);
			//Load header background image
			/*Glide.with(this).load(urlNavHeaderBg)
					.crossFade()
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.into(imgNavHeaderBg);*/
			accountName.setText(personName);
			accountEmail.setText(personEmail);

		} else if (String.valueOf(signInType).matches("facebook")) {

		} else {

		}
	}

	private void setUpNavigationView() {
		//Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

			// This method will trigger on item Click of navigation menu
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {

				//Check to see which item was being clicked and perform appropriate action
				switch (menuItem.getItemId()) {
					//Replacing the main content with ContentFragment Which is our Inbox View;
					case R.id.nav_lock_profile:
						navItemIndex = 0;
						CURRENT_TAG = TAG_LOCKPROFILE;
						break;
					case R.id.nav_visual:
						navItemIndex = 1;
						CURRENT_TAG = TAG_VISUAL;
						break;
					case R.id.nav_notify:
						navItemIndex = 2;
						CURRENT_TAG = TAG_NOTIFY;
						break;
					case R.id.nav_security:
						navItemIndex = 3;
						CURRENT_TAG = TAG_SECURITY;
						break;
					case R.id.nav_misc:
						navItemIndex = 4;
						CURRENT_TAG = TAG_MISC;
						break;
					case R.id.nav_tips_tricks:
						// launch new intent instead of loading fragment
						//startActivity(new Intent(MainScreenActivity.this, AboutUsActivity.class));
						drawer.closeDrawers();
						return true;
					case R.id.nav_backup_restore:
						// launch new intent instead of loading fragment
						//startActivity(new Intent(MainScreenActivity.this, PrivacyPolicyActivity.class));
						drawer.closeDrawers();
						return true;
					case R.id.nav_about_app:
						// launch new intent instead of loading fragment
						//startActivity(new Intent(MainScreenActivity.this, PrivacyPolicyActivity.class));
						drawer.closeDrawers();
						return true;
					default:
						navItemIndex = 0;
				}

				//Checking if the item is in checked state or not, if not make it in checked state
				if (menuItem.isChecked()) {
					menuItem.setChecked(false);
				} else {
					menuItem.setChecked(true);
				}
				menuItem.setChecked(true);

				loadHomeFragment();

				return true;
			}
		});


		ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

			@Override
			public void onDrawerClosed(View drawerView) {
				// Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				// Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
				super.onDrawerOpened(drawerView);
			}
		};

		//Setting the actionbarToggle to drawer layout
		drawer.addDrawerListener(actionBarDrawerToggle);

		//calling sync state is necessary or else your hamburger icon wont show up
		actionBarDrawerToggle.syncState();
	}

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
	    if (drawer.isDrawerOpen(GravityCompat.START)) {
		    drawer.closeDrawer(GravityCompat.START);
		    return;
	    }

	    // This code loads home fragment when back key is pressed when user is in other fragment than home
	    if (shouldLoadHomeFragOnBackPress) {
		    // checking if user is on other navigation menu
		    // rather than home
		    if (navItemIndex != 0) {
			    navItemIndex = 0;
			    CURRENT_TAG = TAG_LOCKPROFILE;
			    loadHomeFragment();
			    return;
		    }
	    }

	    super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
	    return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rate) {
            return true;
        }else if(id == R.id.action_support){
	        return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
