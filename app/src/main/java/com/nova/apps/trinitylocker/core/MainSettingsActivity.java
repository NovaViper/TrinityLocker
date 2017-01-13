package com.nova.apps.trinitylocker.core;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.nova.apps.trinitylocker.R;
import com.nova.apps.trinitylocker.core.fragment.LockProfileOptionFragment;
import com.nova.apps.trinitylocker.core.fragment.MiscOptionFragment;
import com.nova.apps.trinitylocker.core.fragment.NotificationOptionFragment;
import com.nova.apps.trinitylocker.core.fragment.SecurityOptionFragment;
import com.nova.apps.trinitylocker.core.fragment.VisualOptionFragment;
import com.nova.apps.trinitylocker.startup.ChooseSignInActivity;
import com.nova.apps.trinitylocker.util.AppLogger;
import com.nova.apps.trinitylocker.util.AuthManager;
import com.nova.apps.trinitylocker.util.CircleTransform;
import com.nova.apps.trinitylocker.util.Constants;
import com.nova.apps.trinitylocker.util.GoogleSignInSingleton;

public class MainSettingsActivity extends AppCompatActivity {

	private AccountHeader header = null;
	private Drawer result = null;

	private AuthManager authManager;
	GoogleSignInSingleton signInSingleton = GoogleSignInSingleton.getInstance(null);
	GoogleSignInAccount googleAccount = signInSingleton.getGoogleSignIn();
	private IProfile profile = new ProfileDrawerItem().withName(googleAccount.getDisplayName()).withEmail(googleAccount.getEmail()).withIcon(googleAccount.getPhotoUrl()).withIdentifier(Constants.defaultProfile);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_settings);

		authManager = AuthManager.getInstance();
		authManager.onCreate(this, null);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		createAccountHeader(googleAccount);
		createDrawer(toolbar);

	}

	private void createAccountHeader(GoogleSignInAccount googleAccount) {

		//initialize and create the image loader logic
		DrawerImageLoader.init(new AbstractDrawerImageLoader() {
			@Override
			public void set(ImageView imageView, Uri uri, Drawable placeholder) {
				Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
			}

			@Override
			public void cancel(ImageView imageView) {
				Glide.clear(imageView);
			}
		});

		header = new AccountHeaderBuilder()
				.withActivity(this)
				.withTranslucentStatusBar(true)
				.withHeaderBackground(R.drawable.header_background)
				.addProfiles(
						profile,
						new ProfileSettingDrawerItem().withName(getString(R.string.navHeaderSignOut)).withIdentifier(Constants.profileRemoveID)
				)
				.withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
					@Override
					public boolean onProfileChanged(View view, IProfile profile, boolean current) {
						//if the clicked item has the identifier 1 add a new profile ;)
						if (profile.getIdentifier() == Constants.profileRemoveID) {
							authManager.signOut();
							getSharedPreferences(Constants.preferenceKey, MODE_PRIVATE).edit().putBoolean(Constants.preferenceFirstRun, true).commit();
							Intent i = new Intent(getApplicationContext(), ChooseSignInActivity.class);
							startActivity(i);
							finish();
							Toast.makeText(getApplicationContext(), R.string.debugSignOut, Toast.LENGTH_LONG).show();
						}

						//false if you have not consumed the event and it should close the drawer
						return false;
					}
				})
				.build();

	}

	private void createDrawer(Toolbar toolbar) {
		result = new DrawerBuilder()
				.withActivity(this)
				.withToolbar(toolbar)
				.withAccountHeader(header)
				.addDrawerItems(
						new PrimaryDrawerItem().withName(R.string.nav_lock_profile_text).withIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_nav_lock_profile, getApplication().getTheme())).withIdentifier(Constants.navLocker),
						new PrimaryDrawerItem().withName(R.string.nav_visual_text).withIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu_gallery, getApplication().getTheme())).withIdentifier(Constants.navVisual),
						new PrimaryDrawerItem().withName(R.string.nav_notify_text).withIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_nav_notify, getApplication().getTheme())).withIdentifier(Constants.navNotify),
						new PrimaryDrawerItem().withName(R.string.nav_security_text).withIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_nav_security, getApplication().getTheme())).withIdentifier(Constants.navSecurity),
						new PrimaryDrawerItem().withName(R.string.nav_misc_text).withIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_nav_misc, getApplication().getTheme())).withIdentifier(Constants.navMisc),
						new DividerDrawerItem(),
						new PrimaryDrawerItem().withName(R.string.nav_tips_tricks_text).withIdentifier(Constants.navTips),
						new PrimaryDrawerItem().withName(R.string.nav_backup_restore_text).withIdentifier(Constants.navBackup),
						new PrimaryDrawerItem().withName(R.string.nav_about_text).withIdentifier(Constants.navAbout)
				)
				.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
					@Override
					public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
						Fragment fragment = null;
						FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
						Intent intent = null;
						if (drawerItem.getIdentifier() == Constants.navLocker) {
							fragment = new LockProfileOptionFragment();
						} else if (drawerItem.getIdentifier() == Constants.navVisual) {
							fragment = new VisualOptionFragment();
						} else if (drawerItem.getIdentifier() == Constants.navNotify) {
							fragment = new NotificationOptionFragment();
						} else if (drawerItem.getIdentifier() == Constants.navSecurity) {
							fragment = new SecurityOptionFragment();
						} else if (drawerItem.getIdentifier() == Constants.navMisc) {
							fragment = new MiscOptionFragment();
						} else if (drawerItem.getIdentifier() == Constants.navTips) {

						} else if (drawerItem.getIdentifier() == Constants.navBackup) {

						} else if (drawerItem.getIdentifier() == Constants.navAbout) {
							intent = new Intent(MainSettingsActivity.this, AboutAppActivity.class);
						}

						//Execute the fragments/intents when selected
						if (fragment != null) {
							fragmentTransaction.replace(R.id.main_settings_container, fragment).commit();
						}

						if (intent != null) {
							MainSettingsActivity.this.startActivity(intent);
						}

						return false;
					}
				})
				.build();

		result.setSelection(Constants.navLocker, true);
	}

	@Override
	public void onBackPressed() {
		if (result != null && result.isDrawerOpen()) {
			result.closeDrawer();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
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
