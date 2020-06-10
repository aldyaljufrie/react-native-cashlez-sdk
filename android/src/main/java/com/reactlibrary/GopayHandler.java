package com.reactlibrary;

import android.app.Activity;
import android.telecom.Call;
import android.util.Log;

import com.cashlez.android.sdk.CLErrorResponse;
import com.cashlez.android.sdk.CLPayment;
import com.cashlez.android.sdk.bean.ApprovalStatus;
import com.cashlez.android.sdk.bean.TransactionType;
import com.cashlez.android.sdk.companion.printer.CLPrinterCompanion;
import com.cashlez.android.sdk.payment.CLGoPayQRResponse;
import com.cashlez.android.sdk.payment.gopay.CLGoPayQRHandler;
import com.cashlez.android.sdk.payment.gopay.ICLGoPayQRHandler;
import com.cashlez.android.sdk.payment.gopay.ICLGoPayQRService;
import com.cashlez.android.sdk.payment.noncash.CLPaymentHandler;
import com.facebook.react.bridge.Callback;

public class GopayHandler implements ICLGoPayQRService {

  private ICLGoPayQRHandler goPayQRHandler;
  private CLGoPayQRResponse qrResponse;
  private CLPayment payment;
  private Callback onSuccess, onFailed;

  GopayHandler(Activity context) {
    goPayQRHandler = new CLGoPayQRHandler(context, context.getIntent().getExtras(), this);
  }

  void doStartGoPayHandler() { goPayQRHandler.doStartGoPayHandler(); }
  void doResumeGoPayHandler() { goPayQRHandler.doResumeGoPayHandler(); }
  void doStopGoPayHandler() { goPayQRHandler.doStopGoPayHandler(); }

  void generateQR(ApplicationState paymentState, String amount, String desc, Callback success, Callback failed){
    onSuccess = success;
    onFailed = failed;

    payment = new CLPayment();
    payment.setAmount(amount);
    payment.setDescription(desc);
    payment.setTransactionType(TransactionType.QR);
    paymentState.setPayment(payment);
    goPayQRHandler.doProceedGoPayPayment(payment);
  }

  void checkPaymentStatus(Callback success, Callback failed){
    onSuccess = success;
    onFailed = failed;
    if (qrResponse != null){
      goPayQRHandler.doCheckGoPayQRStatus(qrResponse);
    }
  }

  @Override
  public void onGoPayQRSuccess(CLGoPayQRResponse clGoPayQRResponse) {
    this.qrResponse = clGoPayQRResponse;
    this.onSuccess.invoke(clGoPayQRResponse.getQrCodeContent());
    Log.v("Gopay QR Content", clGoPayQRResponse.getQrCodeContent());
    Log.v("Gopay QR trxId", clGoPayQRResponse.getTransactionId());
    Log.v("Gopay QR msg", clGoPayQRResponse.getMessage());
  }

  @Override
  public void onGoPayQRError(CLErrorResponse clErrorResponse) {
    this.onFailed.invoke(clErrorResponse.getErrorMessage());
    doStopGoPayHandler();
    Log.e("Gopay QR Error", clErrorResponse.getErrorMessage());
  }

  @Override
  public void onCheckGoPayStatusSuccess(CLGoPayQRResponse clGoPayQRResponse) {
    int transactionStatus = clGoPayQRResponse.getTransactionStatus();
    String message = ApprovalStatus.getStatus(transactionStatus).getMessage();
    this.onSuccess.invoke(message);
    Log.v("Gopay QR Success", clGoPayQRResponse.getMessage());
  }

  @Override
  public void onCheckGoPayStatusError(CLErrorResponse clErrorResponse) {
    this.onFailed.invoke(clErrorResponse.getErrorMessage());
    Log.e("Gopay QR Error", clErrorResponse.getErrorMessage());
  }

  @Override
  public void onPrintingSuccess(CLPrinterCompanion clPrinterCompanion) {

  }

  @Override
  public void onPrintingError(CLErrorResponse clErrorResponse) {

  }
}
