package com.nova.apps.trinitylocker.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
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
import com.nova.apps.trinitylocker.R;
import com.nova.apps.trinitylocker.core.fragment.LockProfileOptionFragment;
import com.nova.apps.trinitylocker.core.fragment.MiscOptionFragment;
import com.nova.apps.trinitylocker.core.fragment.NotificationOptionFragment;
import com.nova.apps.trinitylocker.core.fragment.SecurityOptionFragment;
import com.nova.apps.trinitylocker.core.fragment.VisualOptionFragment;
import com.nova.apps.trinitylocker.util.AppLogger;
import com.nova.apps.trinitylocker.util.GoogleAPIClientSingleton;
import com.nova.apps.trinitylocker.util.GoogleSignInSingleton;

public class MainSettingsActivity extends AppCompatActivity {

	private AccountHeader header = null;
	private Drawer result = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GoogleSignInSingleton signInSingleton = GoogleSignInSingleton.getInstance(null);
		GoogleSignInAccount googleAccount = signInSingleton.getGoogleSignIn();

		setContentView(R.layout.activity_main_settings);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		createAccountHeader(googleAccount);
		createDrawer(toolbar);

	}

	private void createAccountHeader(GoogleSignInAccount googleAccount){
		header = new AccountHeaderBuilder()
				.withActivity(this)
				.withTranslucentStatusBar(true)
				.withHeaderBackground(R.drawable.header_background)
				.addProfiles(
						new ProfileDrawerItem().withName(googleAccount.getDisplayName()).withEmail(googleAccount.getEmail()).withIcon(googleAccount.getPhotoUrl()),
						new ProfileSettingDrawerItem().withName("Add Account"),
						new ProfileSettingDrawerItem().withName("Manage Accounts")
				)
				.build();
	}

	private void createDrawer(Toolbar toolbar){
		result = new DrawerBuilder()
				.withActivity(this)
				.withToolbar(toolbar)
				.withAccountHeader(header)
				.addDrawerItems(
						new PrimaryDrawerItem().withName(R.string.nav_lock_profile_text).withIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_nav_lock_profile, getApplication().getTheme())).withIdentifier(1),
						new PrimaryDrawerItem().withName(R.string.nav_visual_text).withIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu_gallery, getApplication().getTheme())).withIdentifier(2),
						new PrimaryDrawerItem().withName(R.string.nav_notify_text).withIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_nav_notify, getApplication().getTheme())).withIdentifier(3),
						new PrimaryDrawerItem().withName(R.string.nav_security_text).withIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_nav_security, getApplication().getTheme())).withIdentifier(4),
						new PrimaryDrawerItem().withName(R.string.nav_misc_text).withIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_nav_misc, getApplication().getTheme())).withIdentifier(5),
						new DividerDrawerItem(),
						new PrimaryDrawerItem().withName(R.string.nav_tips_tricks_text).withIdentifier(6),
						new PrimaryDrawerItem().withName(R.string.nav_backup_restore_text).withIdentifier(7),
						new PrimaryDrawerItem().withName(R.string.nav_about_text).withIdentifier(8)
				)
				.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
					@Override
					public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
						Fragment fragment = null;
						FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
						Intent intent = null;
						if (drawerItem.getIdentifier() == 1) {
							fragment = new LockProfileOptionFragment();
						} else if (drawerItem.getIdentifier() == 2) {
							fragment = new VisualOptionFragment();
						} else if (drawerItem.getIdentifier() == 3) {
							fragment = new NotificationOptionFragment();
						} else if (drawerItem.getIdentifier() == 4) {
							fragment = new SecurityOptionFragment();
						} else if (drawerItem.getIdentifier() == 5) {
							fragment = new MiscOptionFragment();
						} else if (drawerItem.getIdentifier() == 6) {

						} else if (drawerItem.getIdentifier() == 7) {

						} else if (drawerItem.getIdentifier() == 8) {
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
				.withSelectedItem(1)
				.build();
	}

	private void signOut() {
		GoogleAPIClientSingleton googleAPIClientSingleton = GoogleAPIClientSingleton.getInstance(null);
		GoogleApiClient googleApiClient = googleAPIClientSingleton.getGoogleApiClient();

		Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
				new ResultCallback<Status>() {
					@Override
					public void onResult(Status status) {
						AppLogger.debug("signOut:onResult:" + status);
					}
				});
	}

	private void revokeAccess() {
		GoogleAPIClientSingleton googleAPIClientSingleton = GoogleAPIClientSingleton.getInstance(null);
		GoogleApiClient googleApiClient = googleAPIClientSingleton.getGoogleApiClient();

		Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(
				new ResultCallback<Status>() {
					@Override
					public void onResult(Status status) {
						AppLogger.debug("revokeAccess:onResult:" + status);
					}
				});
	}

	@Override
	public void onBackPressed() {
		if (result != null && result.isDrawerOpen()) {
			result.closeDrawer();
		} else {
			super.onBackPressed();
		}
	}
}
