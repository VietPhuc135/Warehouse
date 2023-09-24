package com.example.warehousemanagement.obj;

import java.util.ArrayList;
public class Order {
    private String id;
    private String ownerId;
    ArrayList <Product> lineItems = new ArrayList < Product > ();

    public ArrayList<Product> getLineItems() {
        return lineItems;
    }

    public void setLineItems(ArrayList<Product> lineItems) {
        this.lineItems = lineItems;
    }

    public ArrayList<Product> getAcceptedLineItems() {
        return acceptedLineItems;
    }

    public void setAcceptedLineItems(ArrayList<Product> acceptedLineItems) {
        this.acceptedLineItems = acceptedLineItems;
    }

    ArrayList < Product > acceptedLineItems = new ArrayList < Product > ();
    private String status;
    private String option;
    private String message = null;
    private String storageId;
    private String marketId;
    private String flag = null;
    private String flagMessage = null;


    // Getter Methods

    public String getId() {
        return id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getStatus() {
        return status;
    }

    public String getOption() {
        return option;
    }

    public String getMessage() {
        return message;
    }

    public String getStorageId() {
        return storageId;
    }

    public String getMarketId() {
        return marketId;
    }

    public String getFlag() {
        return flag;
    }

    public String getFlagMessage() {
        return flagMessage;
    }

    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setFlagMessage(String flagMessage) {
        this.flagMessage = flagMessage;
    }
}