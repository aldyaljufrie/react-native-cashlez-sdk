package com.reactlibrary;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import com.reactlibrary.login.LoginHandler;
import com.reactlibrary.login.User;

import com.cashlez.android.sdk.CLErrorResponse;
import com.cashlez.android.sdk.login.CLLoginHandler;
import com.cashlez.android.sdk.login.CLLoginResponse;
import com.cashlez.android.sdk.login.ICLLoginService;

public class RNCashlezSdkModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private LoginHandler loginHandler;

  public RNCashlezSdkModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNCashlezSdk";
  }

  @ReactMethod
  public void Authentication(final String username, final String password){
      _Authentication(username, password);
  }

  private void _Authentication (final String username, final String password){
    Log.v("_auth", username + " " + password);
    loginHandler = new LoginHandler(this.reactContext);
    loginHandler.doLogin(new User(username, password));
  }
}
