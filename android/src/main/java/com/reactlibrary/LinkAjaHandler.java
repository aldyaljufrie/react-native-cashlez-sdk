package com.reactlibrary;

import android.app.Activity;
import android.util.Log;

import com.cashlez.android.sdk.CLErrorResponse;
import com.cashlez.android.sdk.CLPayment;
import com.cashlez.android.sdk.bean.ApprovalStatus;
import com.cashlez.android.sdk.bean.TransactionType;
import com.cashlez.android.sdk.companion.printer.CLPrinterCompanion;
import com.cashlez.android.sdk.payment.CLTCashQRResponse;
import com.cashlez.android.sdk.payment.tcashqr.CLTCashQRHandler;
import com.cashlez.android.sdk.payment.tcashqr.ICLTCashQRHandler;
import com.cashlez.android.sdk.payment.tcashqr.ICLTCashQRService;
import com.facebook.react.bridge.Callback;

public class LinkAjaHandler implements ICLTCashQRService {

  private ICLTCashQRHandler linkAjaHandler;
  private CLTCashQRResponse qrResponse;
  private CLPayment payment;
  private Callback onSuccess, onFailed;

  LinkAjaHandler(Activity context) {
    linkAjaHandler = new CLTCashQRHandler(context, context.getIntent().getExtras(), this);
  }

  void doStartLinkAjaHandler() { linkAjaHandler.doStartTCashHandler(); }
  void doResumeLinkAjaHandler() { linkAjaHandler.doResumeTCashHandler(); }
  void doStopLinkAjaHandler() { linkAjaHandler.doStopTCashHandler(); }

  void generateQR(ApplicationState paymentState, String amount, String desc, Callback success, Callback failed){
    onSuccess = success;
    onFailed = failed;

    payment = new CLPayment();
    payment.setAmount(amount);
    payment.setDescription(desc);
    payment.setTransactionType(TransactionType.QR);
    paymentState.setPayment(payment);
    linkAjaHandler.doProceedTCashQRPayment(payment);
  }

  void checkPaymentStatus(Callback success, Callback failed){
    onSuccess = success;
    onFailed = failed;
    if (qrResponse != null){
      linkAjaHandler.doCheckTCashQRStatus(qrResponse);
    }
  }

  @Override
  public void onTCashQRSuccess(CLTCashQRResponse cltCashQRResponse) {
    this.qrResponse = cltCashQRResponse;
    this.onSuccess.invoke(cltCashQRResponse.getQrCodeContent());
    Log.v("LinkAja QR Content", cltCashQRResponse.getQrCodeContent());
    Log.v("LinkAja QR trxId", cltCashQRResponse.getTransactionId());
    Log.v("LinkAja QR msg", cltCashQRResponse.getMessage());
  }

  @Override
  public void onTCashQRError(CLErrorResponse clErrorResponse) {
    this.onFailed.invoke(clErrorResponse.getErrorMessage());
    doStopLinkAjaHandler();
    Log.e("LinkAja QR Error", clErrorResponse.getErrorMessage());
  }

  @Override
  public void onCheckTCashQRStatusSuccess(CLTCashQRResponse cltCashQRResponse) {
    int transactionStatus = cltCashQRResponse.getTransactionStatus();
    String message = ApprovalStatus.getStatus(transactionStatus).getMessage();
    this.onSuccess.invoke(message);
    Log.v("LinkAja QR Success", cltCashQRResponse.getMessage());
  }

  @Override
  public void onCheckTCashQRStatusError(CLErrorResponse clErrorResponse) {
    this.onFailed.invoke(clErrorResponse.getErrorMessage());
    Log.e("LinkAja QR Error", clErrorResponse.getErrorMessage());
  }

  @Override
  public void onPrintingSuccess(CLPrinterCompanion clPrinterCompanion) {

  }

  @Override
  public void onPrintingError(CLErrorResponse clErrorResponse) {

  }
}
