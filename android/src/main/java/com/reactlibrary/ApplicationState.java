package com.reactlibrary;


import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.cashlez.android.sdk.CLPayment;
import com.cashlez.android.sdk.CLPaymentCapability;

public class ApplicationState extends Application {

  private Context context;
  private CLPayment payment;
  private CLPaymentCapability paymentCapability;
  private boolean isGpn;

  @Override
  public void onCreate() {
    super.onCreate();
  }

  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  public Context getContext() {
    return context;
  }

  public void setCurrentContext(Context currentContext) {
    this.context = currentContext;
  }

  public CLPayment getPayment() {
    return payment;
  }

  public void setPayment(CLPayment payment) {
    this.payment = payment;
  }

  public CLPaymentCapability getPaymentCapability() {
    return paymentCapability;
  }

  public void setPaymentCapability(CLPaymentCapability paymentCapability) {
    this.paymentCapability = paymentCapability;
  }

  public boolean isGpn() {
    return isGpn;
  }

  public void setGpn(boolean gpn) {
    isGpn = gpn;
  }
}
