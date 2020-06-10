package com.reactlibrary;

import android.app.Activity;
import android.util.Log;

import com.cashlez.android.sdk.CLErrorResponse;
import com.cashlez.android.sdk.CLPayment;
import com.cashlez.android.sdk.bean.ApprovalStatus;
import com.cashlez.android.sdk.bean.TransactionType;
import com.cashlez.android.sdk.companion.printer.CLPrinterCompanion;
import com.cashlez.android.sdk.payment.CLShopeePayQrResponse;
import com.cashlez.android.sdk.payment.shopeepay.CLShopeePayQrHandler;
import com.cashlez.android.sdk.payment.shopeepay.ICLShopeePayQrHandler;
import com.cashlez.android.sdk.payment.shopeepay.ICLShopeePayQrService;
import com.cashlez.android.sdk.payment.voidpayment.CLVoidResponse;
import com.facebook.react.bridge.Callback;

public class ShopeePayHandler implements ICLShopeePayQrService {

  private ICLShopeePayQrHandler shopeePayHandler;
  private CLShopeePayQrResponse qrResponse;
  private CLPayment payment;
  private Callback onSuccess, onFailed;

  ShopeePayHandler(Activity context) {
    shopeePayHandler = new CLShopeePayQrHandler(context, context.getIntent().getExtras(), this);
  }

  void doStartShopeePayHandler() { shopeePayHandler.doStartHandlerShopeepay(); }
  void doResumeShopeePayHandler() { shopeePayHandler.doResumeHandlerShopeePay(); }
  void doStopShopeePayHandler() { shopeePayHandler.doStartHandlerShopeepay(); }

  void generateQR(ApplicationState paymentState, String amount, String desc, Callback success, Callback failed){
    onSuccess = success;
    onFailed = failed;

    payment = new CLPayment();
    payment.setAmount(amount);
    payment.setDescription(desc);
    payment.setTransactionType(TransactionType.QR);
    paymentState.setPayment(payment);
    shopeePayHandler.doProceedShopeePayQrPayment(payment);
  }

  void checkPaymentStatus(Callback success, Callback failed){
    onSuccess = success;
    onFailed = failed;
    if (qrResponse != null){
      shopeePayHandler.doInquiryShopeePayQr(qrResponse);
    }
  }

  @Override
  public void onShopeePayQrSuccess(CLShopeePayQrResponse clShopeePayQrResponse) {
    this.qrResponse = clShopeePayQrResponse;
    this.onSuccess.invoke(clShopeePayQrResponse.getQrCodeContent());
    Log.v("ShopeePay QR Content", clShopeePayQrResponse.getQrCodeContent());
    Log.v("ShopeePay QR trxId", clShopeePayQrResponse.getTransactionId());
    Log.v("ShopeePay QR msg", clShopeePayQrResponse.getMessage());
  }

  @Override
  public void onShopeePayQrError(CLErrorResponse clErrorResponse) {
    this.onFailed.invoke(clErrorResponse.getErrorMessage());
    doStopShopeePayHandler();
    Log.e("ShopeePay QR Error", clErrorResponse.getErrorMessage());
  }

  @Override
  public void onShopeePayQrCheckStatusSuccess(CLShopeePayQrResponse clShopeePayQrResponse) {
    int transactionStatus = clShopeePayQrResponse.getTransactionStatus();
    String message = ApprovalStatus.getStatus(transactionStatus).getMessage();
    this.onSuccess.invoke(message);
    Log.v("ShopeePay QR Success", clShopeePayQrResponse.getMessage());
  }

  @Override
  public void onShopeePayQrCheckStatusError(CLErrorResponse clErrorResponse) {
    this.onFailed.invoke(clErrorResponse.getErrorMessage());
    Log.e("ShopeePay QR Error", clErrorResponse.getErrorMessage());
  }

  @Override
  public void onShopeePayQrVoidSuccess(CLVoidResponse clVoidResponse) {

  }

  @Override
  public void onShopeePayQrVoidError(CLErrorResponse clErrorResponse) {

  }

  @Override
  public void onPrintingSuccess(CLPrinterCompanion clPrinterCompanion) {

  }

  @Override
  public void onPrintingError(CLErrorResponse clErrorResponse) {

  }
}
