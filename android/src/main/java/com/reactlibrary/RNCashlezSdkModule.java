package com.reactlibrary;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.cashlez.android.sdk.CLPayment;
import com.cashlez.android.sdk.bean.TransactionType;
import com.cashlez.android.sdk.payment.noncash.CLPaymentHandler;
import com.cashlez.android.sdk.payment.noncash.ICLPaymentHandler;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class RNCashlezSdkModule extends ReactContextBaseJavaModule {

  protected CLPaymentHandler paymentHandler;

  private ReactApplicationContext reactContext;
  private ApplicationState applicationState;
  private CLPayment payment;
  private LoginHandler loginHandler;
  private GopayHandler gopayHandler;
  private OvoHandler ovoHandler;
  private DanaHandler danaHandler;
  private LinkAjaHandler linkAjaHandler;
  private ShopeePayHandler shopeePayHandler;


  public RNCashlezSdkModule(ReactApplicationContext context) {
    super(context);
    this.reactContext = context;
  }

  @Override
  public String getName() {
    return "RNCashlezSdk";
  }

  @ReactMethod
  public void initState(final Callback callback){
    Activity activity = getCurrentActivity();
    if(activity != null){
      this.applicationState = new ApplicationState();
      this.applicationState.setCurrentContext(activity.getApplicationContext());
      callback.invoke("Success");
    } else {
      callback.invoke("Failed");
    }
  }

  @ReactMethod
  public void Authenticate(final String username, final String password, final Callback onSuccess, final Callback onFailed){
    Log.v("_auth", username + " " + password);
    Activity activity = getCurrentActivity();
    if (activity != null){
      loginHandler = new LoginHandler(activity);
      loginHandler.doLogin(this.applicationState, username, password, onSuccess, onFailed);
    }
  }

  @ReactMethod
  public void AggregatorLogin(final String publickey, final String privatekey, final String userId, final String aggregatorId, final Callback onSuccess, final Callback onFailed){
    Log.v("Login Aggregator", "LOGIN WITH AGGREGATOR");
    loginHandler = new LoginHandler(getCurrentActivity());
    loginHandler.doLoginAggregator(this.applicationState, publickey, privatekey, userId, aggregatorId, onSuccess, onFailed);
  }

  @ReactMethod
  public void doOvoPayment(final String phone, final String amount, final String desc, final Callback onSuccess, final Callback onFailed){
    Activity activity = getCurrentActivity();
    if (activity != null){
      Intent intent = activity.getIntent();
      paymentHandler = new CLPaymentHandler(activity, intent.getExtras());
      paymentHandler.doConnectLocationProvider();

      ovoHandler = new OvoHandler(activity);
      ovoHandler.doStartOvoHandler();
      ovoHandler.doPayment(this.applicationState, phone, amount, desc, onSuccess, onFailed);

      Log.v("doOvoPayment", "doOvoPayment " + phone + " " + amount + " " + desc);
    }
  }

  @ReactMethod
  public void GenerateQRGopay(final String amount, final String desc, final Callback onSuccess, final Callback onFailed){
    Activity activity = getCurrentActivity();
    if (activity != null){
      Intent intent = activity.getIntent();
      paymentHandler = new CLPaymentHandler(activity, intent.getExtras());
      paymentHandler.doConnectLocationProvider();

      gopayHandler = new GopayHandler(activity);
      gopayHandler.doStartGoPayHandler();
      gopayHandler.generateQR(this.applicationState, amount, desc, onSuccess, onFailed);
    }
  }

  @ReactMethod
  public void GenerateQRLinkAja(final String amount, final String desc, final Callback onSuccess, final Callback onFailed){
    Activity activity = getCurrentActivity();
    if (activity != null){
      Intent intent = activity.getIntent();
      paymentHandler = new CLPaymentHandler(activity, intent.getExtras());
      paymentHandler.doConnectLocationProvider();

      linkAjaHandler = new LinkAjaHandler(activity);
      linkAjaHandler.doStartLinkAjaHandler();
      linkAjaHandler.generateQR(this.applicationState, amount, desc, onSuccess, onFailed);
    }
  }

  @ReactMethod
  public void GenerateQRShopeePay(final String amount, final String desc, final Callback onSuccess, final Callback onFailed){
    Activity activity = getCurrentActivity();
    if (activity != null){
      Intent intent = activity.getIntent();
      paymentHandler = new CLPaymentHandler(activity, intent.getExtras());
      paymentHandler.doConnectLocationProvider();

      shopeePayHandler = new ShopeePayHandler(activity);
      shopeePayHandler.doStartShopeePayHandler();
      shopeePayHandler.generateQR(this.applicationState, amount, desc, onSuccess, onFailed);
    }
  }

  @ReactMethod
  public void GenerateQRDana(final String amount, final String desc, final Callback onSuccess, final Callback onFailed){
    Activity activity = getCurrentActivity();
    if (activity != null){
      Intent intent = activity.getIntent();
      paymentHandler = new CLPaymentHandler(activity, intent.getExtras());
      paymentHandler.doConnectLocationProvider();

      danaHandler = new DanaHandler(activity);
      danaHandler.doStartDanaHandler();
      danaHandler.generateQR(this.applicationState, amount, desc, onSuccess, onFailed);
    }
  }

  @ReactMethod
  public void CheckPaymentStatus(final String type, final Callback onSuccess, final Callback onFailed){
    switch (type){
      case "gopay":
        checkGopayStatus(onSuccess, onFailed);
      break;
      case "ovo":
        checkOvoStatus(onSuccess, onFailed);
        break;
      case "linkaja":
        checkLinkAjaStatus(onSuccess, onFailed);
        break;
      case "shopeepay":
        checkShopeePayStatus(onSuccess, onFailed);
        break;
      case "dana":
        checkDanaStatus(onSuccess, onFailed);
        break;
    }

  }

  private void checkGopayStatus(Callback onSuccess, Callback onFailed){
    if (gopayHandler != null){
      gopayHandler.checkPaymentStatus(onSuccess, onFailed);
    }
  }

  private void checkOvoStatus(Callback onSuccess, Callback onFailed){
    if (ovoHandler != null){
      ovoHandler.checkPaymentStatus(onSuccess, onFailed);
    }
  }

  private void checkLinkAjaStatus(Callback onSuccess, Callback onFailed){
    if (linkAjaHandler != null){
      linkAjaHandler.checkPaymentStatus(onSuccess, onFailed);
    }
  }

  private void checkShopeePayStatus(Callback onSuccess, Callback onFailed){
    if (shopeePayHandler != null){
      shopeePayHandler.checkPaymentStatus(onSuccess, onFailed);
    }
  }

  private void checkDanaStatus(Callback onSuccess, Callback onFailed){
    if (danaHandler != null){
      danaHandler.checkPaymentStatus(onSuccess, onFailed);
    }
  }
}
