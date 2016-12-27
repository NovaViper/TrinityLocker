package com.nova.apps.trinitylocker.core;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
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
import com.nova.apps.trinitylocker.util.GoogleSignInSingleton;


//Credit for providing the basis of the code goes to Ravi Tamada on AndroidHive!
public class MainScreenActivity extends AppCompatActivity {

	private NavigationView navigationView;
	private DrawerLayout drawer;
	private Toolbar toolbar;

	private boolean shouldLoadHomeFragOnBackPress = true;
	private Handler mHandler;

	private ImageView profilePic, headerBackground;
	private TextView accountName, accountEmail;

	public static int navItemIndex = 0;

	//---Tags used to attach the fragments---\\
	private static final String TAG_LOCKPROFILE = "lock_profile";
	private static final String TAG_VISUAL = "visual";
	private static final String TAG_NOTIFY = "notification";
	private static final String TAG_SECURITY = "security";
	private static final String TAG_MISC = "misc";
	public static String CURRENT_TAG = TAG_LOCKPROFILE;

	//Toolbar titles respected to selected nav menu item
	private String[] activityTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		mHandler = new Handler();
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		navigationView = (NavigationView) findViewById(R.id.nav_view);

		loadNavHeader();
		activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
		setUpNavigationView();

		if (savedInstanceState == null) {
			navItemIndex = 0;
			CURRENT_TAG = TAG_LOCKPROFILE;
			loadHomeFragment();
		}
	}

	/**Returns the respected fragment that user selected from navigation menu*/
	private void loadHomeFragment() {
		selectNavMenuItem();
		setActionBarTitleToCurrentFragment();

		//If user select the current navigation menu again, don't do anything and close the navigation drawer
		if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
			drawer.closeDrawers();
			return;
		}

		// Sometimes, when fragment has huge data, screen seems hanging when switching between navigation menus
		// So using runnable, the fragment is loaded with cross fade effect
		// This effect can be seen in Gmail
		Runnable mPendingRunnable = new Runnable() {
			@Override
			public void run() {
				// update the main content by replacing fragments
				Fragment fragment = getFragmentToRender();
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
						android.R.anim.fade_out);
				fragmentTransaction.replace(R.id.content_main_screen, fragment, CURRENT_TAG);
				fragmentTransaction.commitAllowingStateLoss();
			}
		};

		if (mPendingRunnable != null) {
			mHandler.post(mPendingRunnable);
		}

		drawer.closeDrawers();
		invalidateOptionsMenu();
	}

	/**Retrieves the fragment that is selected based on the navItemIndex*/
	private Fragment getFragmentToRender() {
		switch (navItemIndex) {
			case 0:
				LockProfileOptionFragment lockerFragment = new LockProfileOptionFragment();
				return lockerFragment;
			case 1:
				VisualOptionFragment visualFragment = new VisualOptionFragment();
				return visualFragment;
			case 2:
				NotificationOptionFragment notifyFragment = new NotificationOptionFragment();
				return notifyFragment;
			case 3:
				SecurityOptionFragment securityFragment = new SecurityOptionFragment();
				return securityFragment;
			case 4:
				MiscOptionFragment miscFragment = new MiscOptionFragment();
				return miscFragment;
			default:
				return new LockProfileOptionFragment();
		}
	}

	private void setActionBarTitleToCurrentFragment() {
		getSupportActionBar().setTitle(activityTitles[navItemIndex]);
	}

	private void selectNavMenuItem() {
		navigationView.getMenu().getItem(navItemIndex).setCheckable(true);
	}

	private void loadNavHeader() {
		GoogleSignInSingleton signInSingleton = GoogleSignInSingleton.getInstance(null);
		GoogleSignInAccount googleAccount = signInSingleton.getGoogleSignIn();
		//GoogleSignInAccount googleAccount = getIntent().getExtras().getParcelable("Account");

		String personName = googleAccount.getDisplayName();
		String personEmail = googleAccount.getEmail();
		Uri personPhoto = googleAccount.getPhotoUrl();

		View header = navigationView.getHeaderView(0);
		profilePic = (ImageView) header.findViewById(R.id.navProfilePic);
		accountName = (TextView) header.findViewById(R.id.navProfileName);
		accountEmail = (TextView) header.findViewById(R.id.navProfileEmail);

		Glide.with(this).load(personPhoto)
				.crossFade()
				.thumbnail(0.5f)
				.bitmapTransform(new CircleTransform(this))
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(profilePic);
			/*Glide.with(this).load(urlNavHeaderBg)
					.crossFade()
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.into(imgNavHeaderBg);*/
		accountName.setText(personName);
		accountEmail.setText(personEmail);
	}

	private void setupAccountManageButton() { //TODO Need to finish implemented Account manage header settings
		View header = navigationView.getHeaderView(0);
		ImageButton mangeAccountButton = (ImageButton) header.findViewById(R.id.manageAccountButton);
		mangeAccountButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {

			}
		});
	}

	private void setUpNavigationView() {
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {
				switch (menuItem.getItemId()) {
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
					case R.id.nav_tips_tricks: //TODO add activities here
						//startActivity(new Intent(MainScreenActivity.this, AboutUsActivity.class));
						drawer.closeDrawers();
						return true;
					case R.id.nav_backup_restore:
						//startActivity(new Intent(MainScreenActivity.this, PrivacyPolicyActivity.class));
						drawer.closeDrawers();
						return true;
					case R.id.nav_about_app:
						//startActivity(new Intent(MainScreenActivity.this, PrivacyPolicyActivity.class));
						drawer.closeDrawers();
						return true;
					default:
						navItemIndex = 0;
				}

				//Check if the item is in checked state or not, if not make it in checked state
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
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};

		drawer.addDrawerListener(actionBarDrawerToggle);
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
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_share) { //TODO Change Intent
			Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			String shareBodyText = "Check it out. Your message goes here";
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject here");
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
			startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
		} else if (id == R.id.action_rate) {
			return true;
		} else if (id == R.id.action_support) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
