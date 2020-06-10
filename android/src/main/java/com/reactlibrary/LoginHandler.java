package com.reactlibrary;

import android.content.Context;
import android.util.Log;

import com.cashlez.android.sdk.CLErrorResponse;
import com.cashlez.android.sdk.login.CLLoginHandler;
import com.cashlez.android.sdk.login.CLLoginResponse;
import com.cashlez.android.sdk.login.ICLLoginService;

import com.facebook.react.bridge.Callback;

public class LoginHandler implements ICLLoginService {

  private ApplicationState applicationState;
  private CLLoginHandler loginHandler;
  private Callback onSuccess, onFailed;

  public LoginHandler(Context context) {
    loginHandler = new CLLoginHandler(context, this);
  }

  public void doLogin(ApplicationState state, String username, String password, Callback success, Callback failed) {
    onSuccess = success;
    onFailed = failed;
    applicationState = state;
    loginHandler.doLogin(username, password);
  }

  public void doLoginAggregator(ApplicationState state, String publickey, String privatekey, String userId, String aggregatorId, Callback success, Callback failed){
    onSuccess = success;
    onFailed = failed;
    applicationState = state;
    loginHandler.doLogin(publickey, privatekey, userId, aggregatorId);
  }

  @Override
  public void onLoginSuccess(CLLoginResponse clLoginResponse) {
    this.applicationState.setPaymentCapability(clLoginResponse.getPaymentCapability());
    this.onSuccess.invoke(clLoginResponse.getMessage());
  }

  @Override
  public void onLoginError(CLErrorResponse errorResponse) {
    this.onFailed.invoke(errorResponse.getErrorMessage());
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
