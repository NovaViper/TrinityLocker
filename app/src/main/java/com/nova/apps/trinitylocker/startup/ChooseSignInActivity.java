package com.nova.apps.trinitylocker.startup;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.nova.apps.trinitylocker.R;
import com.nova.apps.trinitylocker.startup.setup.FirstSetupActivity;
import com.nova.apps.trinitylocker.util.AppLogger;
import com.nova.apps.trinitylocker.util.GoogleSignInSingleton;
import com.nova.apps.trinitylocker.util.permission.PermissionActivity;


//Based on Google's Sign In Example
public class ChooseSignInActivity extends PermissionActivity implements
		GoogleApiClient.OnConnectionFailedListener {
	private static final int RC_SIGN_IN = 9001;
	private static final int RC_PERMISSIONS = 1;

	private GoogleApiClient mGoogleApiClient;
	private ProgressDialog mProgressDialog;
	private GoogleSignInOptions gso;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_sign_in);

		//Configure sign-in to request the user's ID, email address, and basic profile.
		gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
				.requestScopes(new Scope(Scopes.PLUS_LOGIN))
				.requestEmail()
				.build();

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this, this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();

		createSignInButton(R.id.google_sign_in_button, new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
					case R.id.google_sign_in_button:
						signIn();
						break;
				}
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();

		permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
		permissions.add(Manifest.permission.READ_CALL_LOG);
		permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
		permissions.add(Manifest.permission.USE_FINGERPRINT);
		permissions.add(Manifest.permission.ACCESS_NOTIFICATION_POLICY);
		permissions.add(Manifest.permission.READ_CALENDAR);
		permissions.add(Manifest.permission.READ_SMS);
		permissions.add(Manifest.permission.RECEIVE_MMS);
		permissions.add(Manifest.permission.CAMERA);
		permissions.add(Manifest.permission.GET_ACCOUNTS);

		permissionUtils.checkPermissions(permissions, R.string.permission_rationale, RC_PERMISSIONS);

		OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
		if (opr.isDone()) {
			// If the user's cached credentials are valid, the OptionalPendingResult will be "done"
			// and the GoogleSignInResult will be available instantly.
			AppLogger.debug("Got cached sign-in");
			GoogleSignInResult result = opr.get();
			handleSignInResult(result);
		} else {
			// If the user has not previously signed in on this device or the sign-in has expired,
			// this asynchronous branch will attempt to sign in the user silently.  Cross-device
			// single sign-on will occur in this branch.
			showProgressDialog();
			opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
				@Override
				public void onResult(GoogleSignInResult googleSignInResult) {
					hideProgressDialog();
					handleSignInResult(googleSignInResult);
				}
			});
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			handleSignInResult(result);
		}
	}

	private void handleSignInResult(GoogleSignInResult result) {
		AppLogger.debug("handleSignInResult:" + result.isSuccess());
		if (result.isSuccess()) {
			setUpSignInAndFinish(result);
		} else {
			//TODO Add something here
		}
	}

	private void setUpSignInAndFinish(GoogleSignInResult result) { //TODO Save data in a better way
		GoogleSignInAccount acct = result.getSignInAccount();
		Intent i = new Intent(getApplicationContext(), FirstSetupActivity.class);
		//i.putExtra("Account", acct);
		GoogleSignInSingleton account = GoogleSignInSingleton.getInstance(acct);
		startActivity(i);
		finish();
	}

	private void signIn() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		AppLogger.debug("onConnectionFailed:" + connectionResult);
	}

	private void showProgressDialog() {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage(getString(R.string.loading));
			mProgressDialog.setIndeterminate(true);
		}
		mProgressDialog.show();
	}

	private void hideProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.hide();
		}
	}

	public void createSignInButton(int id, View.OnClickListener listener) {
		SignInButton signInButton = (SignInButton) findViewById(id);
		signInButton.setSize(SignInButton.SIZE_WIDE);
		signInButton.setOnClickListener(listener);
		signInButton.setScopes(gso.getScopeArray());
	}
}