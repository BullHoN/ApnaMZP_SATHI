package com.avit.apnamzpsathi.model;

import java.util.List;

public class OrderItem {

    private String _id;
    private List<ShopItemData> orderItems;
    private UserInfo shopInfo;
    private UserInfo userInfo;
    private Integer orderStatus;
    private List<String> itemsOnTheWay;
    private boolean itemsOnTheWayVisible;
    private Integer totalAmountToTake, totalAmountToGive;
    private boolean isPaid;

    public OrderItem(String _id, List<ShopItemData> orderItems, UserInfo shopInfo, UserInfo userInfo, Integer orderStatus, List<String> itemsOnTheWay, boolean itemsOnTheWayVisible, Integer totalAmountToTake, Integer totalAmountToGive, boolean isPaid) {
        this._id = _id;
        this.orderItems = orderItems;
        this.shopInfo = shopInfo;
        this.userInfo = userInfo;
        this.orderStatus = orderStatus;
        this.itemsOnTheWay = itemsOnTheWay;
        this.itemsOnTheWayVisible = itemsOnTheWayVisible;
        this.totalAmountToTake = totalAmountToTake;
        this.totalAmountToGive = totalAmountToGive;
        this.isPaid = isPaid;
    }

    public OrderItem(String _id, List<ShopItemData> orderItems, UserInfo shopInfo, UserInfo userInfo, Integer orderStatus, List<String> itemsOnTheWay, boolean itemsOnTheWayVisible, Integer totalAmountToTake, Integer totalAmountToGive) {
        this._id = _id;
        this.orderItems = orderItems;
        this.shopInfo = shopInfo;
        this.userInfo = userInfo;
        this.orderStatus = orderStatus;
        this.itemsOnTheWay = itemsOnTheWay;
        this.itemsOnTheWayVisible = itemsOnTheWayVisible;
        this.totalAmountToTake = totalAmountToTake;
        this.totalAmountToGive = totalAmountToGive;
    }

    public OrderItem(String _id, List<ShopItemData> orderItems, UserInfo shopInfo, UserInfo userInfo, Integer orderStatus, List<String> itemsOnTheWay, boolean itemsOnTheWayVisible, Integer totalAmountToTake) {
        this._id = _id;
        this.orderItems = orderItems;
        this.shopInfo = shopInfo;
        this.userInfo = userInfo;
        this.orderStatus = orderStatus;
        this.itemsOnTheWay = itemsOnTheWay;
        this.itemsOnTheWayVisible = itemsOnTheWayVisible;
        this.totalAmountToTake = totalAmountToTake;
    }

    public OrderItem(String _id, List<ShopItemData> orderItems, UserInfo shopInfo, UserInfo userInfo, Integer orderStatus, List<String> itemsOnTheWay) {
        this._id = _id;
        this.orderItems = orderItems;
        this.shopInfo = shopInfo;
        this.userInfo = userInfo;
        this.orderStatus = orderStatus;
        this.itemsOnTheWay = itemsOnTheWay;
        this.itemsOnTheWayVisible = false;
    }

    public OrderItem(String _id, List<ShopItemData> orderItems, UserInfo shopInfo, UserInfo userInfo) {
        this._id = _id;
        this.orderItems = orderItems;
        this.shopInfo = shopInfo;
        this.userInfo = userInfo;
    }

    public OrderItem(String _id, List<ShopItemData> orderItems, UserInfo shopInfo, UserInfo userInfo, Integer orderStatus) {
        this._id = _id;
        this.orderItems = orderItems;
        this.shopInfo = shopInfo;
        this.userInfo = userInfo;
        this.orderStatus = orderStatus;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public Integer getTotalAmountToGive() {
        return totalAmountToGive;
    }

    public Integer getTotalAmountToTake() {
        return totalAmountToTake;
    }

    public boolean isItemsOnTheWayVisible() {
        return itemsOnTheWayVisible;
    }

    public void setItemsOnTheWayVisible(boolean itemsOnTheWayVisible) {
        this.itemsOnTheWayVisible = itemsOnTheWayVisible;
    }

    public List<String> getItemsOnTheWay() {
        return itemsOnTheWay;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public String get_id() {
        return _id;
    }

    public List<ShopItemData> getOrderItems() {
        return orderItems;
    }

    public UserInfo getShopInfo() {
        return shopInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
