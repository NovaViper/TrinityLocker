package com.nova.apps.trinitylocker.util.permission;

import java.util.ArrayList;

interface PermissionResultCallback {

	void PermissionGranted(int request_code);

	void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions);

	void PermissionDenied(int request_code);

	void NeverAskAgain(int request_code);
}