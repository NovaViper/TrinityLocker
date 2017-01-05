package com.nova.apps.trinitylocker.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.nova.apps.trinitylocker.R;


//Based on Google's Sign In Example
public class AuthManager implements GoogleApiClient.OnConnectionFailedListener {
	private static final AuthManager INSTANCE = new AuthManager();
	private static final int RC_SIGN_IN = 9001;

	private GoogleApiClient mGoogleApiClient;
	private ProgressDialog mProgressDialog;
	private GoogleSignInOptions gso;
	private FragmentActivity currentActivity;
	private Class<?> newActivity;

	public static AuthManager getInstance(){
		return INSTANCE;
	}

	public void onCreate(FragmentActivity fragmentActivity, @Nullable Class<?> newActivity) {
		this.currentActivity = fragmentActivity;
		this.newActivity = newActivity;

		//Configure sign-in to request the user's ID, email address, and basic profile.
		gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
				.requestEmail()
				.build();

		mGoogleApiClient = new GoogleApiClient.Builder(currentActivity)
				.enableAutoManage(currentActivity, this)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.build();
	}

	public void onStart() {
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			handleSignInResult(result);
		}
	}

	private void handleSignInResult(GoogleSignInResult result) {
		AppLogger.debug("handleSignInResult:" + result.isSuccess());
		if (result.isSuccess()) {
			GoogleSignInAccount acct = result.getSignInAccount();
			Intent i = new Intent(currentActivity.getApplicationContext(), newActivity);
			//i.putExtra("Account", acct);
			GoogleSignInSingleton account = GoogleSignInSingleton.getInstance(acct);
			currentActivity.startActivity(i);
			currentActivity.finish();
		} else {
			//TODO Add something here
		}
	}
	public void signIn() {
		Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
		currentActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	public void revokeAccess() {

		Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
				new ResultCallback<Status>() {
					@Override
					public void onResult(Status status) {
						AppLogger.debug("revokeAccess:onResult:" + status);
					}
				});
	}

	public void signOut(){
		Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
				new ResultCallback<Status>() {
					@Override
					public void onResult(Status status) {
						AppLogger.debug("signOut:onResult:" + status);
					}
				});
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		AppLogger.debug("onConnectionFailed:" + connectionResult);
	}

	private void showProgressDialog() {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(currentActivity);
			mProgressDialog.setMessage(currentActivity.getString(R.string.loading));
			mProgressDialog.setIndeterminate(true);
		}
		mProgressDialog.show();
	}

	private void hideProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.hide();
		}
	}
}