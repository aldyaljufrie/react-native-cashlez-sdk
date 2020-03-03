package com.reactlibrary.login;

import android.content.Context;
import android.util.Log;

import com.cashlez.android.sdk.CLErrorResponse;
import com.cashlez.android.sdk.login.CLLoginHandler;
import com.cashlez.android.sdk.login.CLLoginResponse;
import com.cashlez.android.sdk.login.ICLLoginService;

public class LoginHandler implements ICLLoginService {

//  private final ReactApplicationContext reactContext;
  private CLLoginHandler loginHandler;

  public LoginHandler(Context context) {
    loginHandler = new CLLoginHandler(context, this);
  }

  public void doLogin(com.reactlibrary.login.User user) {
    loginHandler.doLogin(user.getUserName(), user.getPin());
  }

  @Override
  public void onLoginSuccess(CLLoginResponse clLoginResponse) {
      Log.v("onLoginSuccess", clLoginResponse.getMessage());
  }

  @Override
  public void onLoginError(CLErrorResponse errorResponse) {
    Log.e("errorResponse", errorResponse.getErrorMessage());
  }

  @Override
  public void onStartActivation(String mobileUpdateURL) {

  }

  @Override
  public void onNewVersionAvailable(CLErrorResponse errorResponse) {

  }

  @Override
  public void onApplicationExpired(CLErrorResponse errorResponse) {

  }
}
