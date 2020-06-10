package com.reactlibrary;

import android.app.Activity;
import android.util.Log;

import com.cashlez.android.sdk.CLErrorResponse;
import com.cashlez.android.sdk.CLPayment;
import com.cashlez.android.sdk.bean.ApprovalStatus;
import com.cashlez.android.sdk.bean.TransactionType;
import com.cashlez.android.sdk.companion.printer.CLPrinterCompanion;
import com.cashlez.android.sdk.payment.CLDanaResponse;
import com.cashlez.android.sdk.payment.dana.CLDanaHandler;
import com.cashlez.android.sdk.payment.dana.ICLDanaHandler;
import com.cashlez.android.sdk.payment.dana.ICLDanaService;
import com.facebook.react.bridge.Callback;

public class DanaHandler implements ICLDanaService {

  private ICLDanaHandler danaHandler;
  private CLDanaResponse qrResponse;
  private CLPayment payment;
  private Callback onSuccess, onFailed;

  DanaHandler(Activity context) {
    danaHandler = new CLDanaHandler(context, context.getIntent().getExtras(), this);
  }

  void doStartDanaHandler() { danaHandler.doStartDanaQRPayHandler(); }
  void doResumeDanaHandler() { danaHandler.doResumeDanaQRPayHandler(); }
  void doStopDanaHandler() { danaHandler.doStopDanaQRPayHandler(); }

  void generateQR(ApplicationState paymentState, String amount, String desc, Callback success, Callback failed){
    onSuccess = success;
    onFailed = failed;

    payment = new CLPayment();
    payment.setAmount(amount);
    payment.setDescription(desc);
    payment.setTransactionType(TransactionType.QR);
    paymentState.setPayment(payment);
    danaHandler.doProceedDanaQRPayment(payment);
  }

  void checkPaymentStatus(Callback success, Callback failed){
    onSuccess = success;
    onFailed = failed;
    if (qrResponse != null){
      danaHandler.doCheckStatusDanaQRPay(qrResponse);
    }
  }

  @Override
  public void onDanaQRSuccess(CLDanaResponse clDanaResponse) {
    this.qrResponse = clDanaResponse;
    this.onSuccess.invoke(clDanaResponse.getQrCodeContent());
    Log.v("DANA QR Content", clDanaResponse.getQrCodeContent());
    Log.v("DANA QR trxId", clDanaResponse.getTransactionId());
    Log.v("DANA QR msg", clDanaResponse.getMessage());
  }

  @Override
  public void onDanaQRError(CLErrorResponse clErrorResponse) {
    this.onFailed.invoke(clErrorResponse.getErrorMessage());
    doStopDanaHandler();
    Log.e("DANA QR Error", clErrorResponse.getErrorMessage());
  }

  @Override
  public void onDanaQRCheckStatusSuccess(CLDanaResponse clDanaResponse) {
    int transactionStatus = clDanaResponse.getTransactionStatus();
    String message = ApprovalStatus.getStatus(transactionStatus).getMessage();
    this.onSuccess.invoke(message);
    Log.v("DANA QR Success", clDanaResponse.getMessage());
  }

  @Override
  public void onDanaQRCheckStatusError(CLErrorResponse clErrorResponse) {
    this.onFailed.invoke(clErrorResponse.getErrorMessage());
    Log.e("DANA QR Error", clErrorResponse.getErrorMessage());
  }

  @Override
  public void onPrintingSuccess(CLPrinterCompanion clPrinterCompanion) {

  }

  @Override
  public void onPrintingError(CLErrorResponse clErrorResponse) {

  }
}
