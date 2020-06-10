package com.reactlibrary;

import android.app.Activity;
import android.util.Log;

import com.cashlez.android.sdk.CLErrorResponse;
import com.cashlez.android.sdk.CLPayment;
import com.cashlez.android.sdk.bean.ApprovalStatus;
import com.cashlez.android.sdk.bean.TransactionType;
import com.cashlez.android.sdk.companion.printer.CLPrinterCompanion;
import com.cashlez.android.sdk.payment.CLPaymentResponse;
import com.cashlez.android.sdk.payment.ovo.CLOvoHandler;
import com.cashlez.android.sdk.payment.ovo.ICLOvoHandler;
import com.cashlez.android.sdk.payment.ovo.ICLOvoService;
import com.cashlez.android.sdk.payment.voidpayment.CLVoidResponse;
import com.facebook.react.bridge.Callback;

public class OvoHandler implements ICLOvoService {

  private ICLOvoHandler ovoHandler;
  private CLPayment payment;
  private CLPaymentResponse paymentResponse;
  private Callback onSuccess, onFailed;

  OvoHandler(Activity context) {
    ovoHandler = new CLOvoHandler(context, context.getIntent().getExtras(), this);
  }

  void doStartOvoHandler() { ovoHandler.doStartOvoHandler(); }
  void doResumeOvoHandler() { ovoHandler.doResumeOvoHandler(); }
  void doStopOvoHandler() { ovoHandler.doStopOvoHandler(); }

  void doPayment(ApplicationState paymentState, String phone, String amount, String desc, Callback success, Callback failed){
    onSuccess = success;
    onFailed = failed;

    payment = new CLPayment();
    payment.setAmount(amount);
    payment.setDescription(desc);
    payment.setCustomerMobilePhone(phone);
    paymentState.setPayment(payment);
    ovoHandler.doOvoPayment(payment);
  }

  void checkPaymentStatus(Callback success, Callback failed){
    onSuccess = success;
    onFailed = failed;

    if (paymentResponse != null){
      ovoHandler.doOvoInquiry(paymentResponse);
    } else {
      Log.e("Ovo CheckPayment", "paymentResponse is null");
    }
  }

  @Override
  public void onOvoPaymentSuccess(CLPaymentResponse clPaymentResponse) {
    this.paymentResponse = clPaymentResponse;
    this.onSuccess.invoke("Success");
  }

  @Override
  public void onOvoPaymentError(CLErrorResponse clErrorResponse) {
    this.onFailed.invoke(clErrorResponse.getErrorMessage());
    doStopOvoHandler();
    Log.e("OVO Payment Error", clErrorResponse.getErrorMessage());
  }

  @Override
  public void onOvoInquirySuccess(CLPaymentResponse clPaymentResponse) {
    int transactionStatus = clPaymentResponse.getTransactionStatus();
    String message = ApprovalStatus.getStatus(transactionStatus).getMessage();

    this.onSuccess.invoke(message);
    Log.e("OVO Inquiry Trx Status", message);
  }

  @Override
  public void onOvoInquiryError(CLErrorResponse clErrorResponse) {
    this.onFailed.invoke(clErrorResponse.getErrorMessage());
    Log.e("OVO Inquiry Error", clErrorResponse.getErrorMessage());
  }

  @Override
  public void onOvoVoidPaymentSuccess(CLVoidResponse clVoidResponse) {

  }

  @Override
  public void onOvoVoidPaymentError(CLErrorResponse clErrorResponse) {

  }

  @Override
  public void onPrintingSuccess(CLPrinterCompanion clPrinterCompanion) {

  }

  @Override
  public void onPrintingError(CLErrorResponse clErrorResponse) {

  }
}
