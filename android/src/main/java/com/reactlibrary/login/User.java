package com.reactlibrary.login;

/**
 * Created by Taslim_Hartmann on 5/13/2017.
 */

public class User {
    private String userName;
    private String pin;
    private String aggregatorId;
    private String privateKey;
    private String publicKey;

    public User(String userName, String pin) {
        this.userName = userName;
        this.pin = pin;
    }

    public User(String publicKey, String privateKey, String userName, String aggregatorId) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.userName = userName;
        this.aggregatorId = aggregatorId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getAggregatorId() {
        return aggregatorId;
    }

    public void setAggregatorId(String aggregatorId) {
        this.aggregatorId = aggregatorId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
