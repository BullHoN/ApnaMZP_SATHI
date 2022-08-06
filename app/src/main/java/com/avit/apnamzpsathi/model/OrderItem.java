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
    private boolean itemsOnTheWayCancelled;
    private int itemsOnTheWayActualCost;
    private boolean orderAcceptedByDeliverySathi;
    private boolean adminShopService;
    private int deliverySathiIncome;

    public OrderItem(String _id, List<ShopItemData> orderItems, UserInfo shopInfo, UserInfo userInfo, Integer orderStatus, List<String> itemsOnTheWay, boolean itemsOnTheWayVisible, Integer totalAmountToTake, Integer totalAmountToGive, boolean isPaid, boolean itemsOnTheWayCancelled, int itemsOnTheWayActualCost, boolean orderAcceptedByDeliverySathi, boolean adminShopService, int deliverySathiIncome) {
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
        this.itemsOnTheWayCancelled = itemsOnTheWayCancelled;
        this.itemsOnTheWayActualCost = itemsOnTheWayActualCost;
        this.orderAcceptedByDeliverySathi = orderAcceptedByDeliverySathi;
        this.adminShopService = adminShopService;
        this.deliverySathiIncome = deliverySathiIncome;
    }

    public OrderItem(String _id, List<ShopItemData> orderItems, UserInfo shopInfo, UserInfo userInfo, Integer orderStatus, List<String> itemsOnTheWay, boolean itemsOnTheWayVisible, Integer totalAmountToTake, Integer totalAmountToGive, boolean isPaid, boolean itemsOnTheWayCancelled, int itemsOnTheWayActualCost, boolean orderAcceptedByDeliverySathi, boolean adminShopService) {
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
        this.itemsOnTheWayCancelled = itemsOnTheWayCancelled;
        this.itemsOnTheWayActualCost = itemsOnTheWayActualCost;
        this.orderAcceptedByDeliverySathi = orderAcceptedByDeliverySathi;
        this.adminShopService = adminShopService;
    }

    public OrderItem(String _id, List<ShopItemData> orderItems, UserInfo shopInfo, UserInfo userInfo, Integer orderStatus, List<String> itemsOnTheWay, boolean itemsOnTheWayVisible, Integer totalAmountToTake, Integer totalAmountToGive, boolean isPaid, boolean itemsOnTheWayCancelled, int itemsOnTheWayActualCost, boolean orderAcceptedByDeliverySathi) {
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
        this.itemsOnTheWayCancelled = itemsOnTheWayCancelled;
        this.itemsOnTheWayActualCost = itemsOnTheWayActualCost;
        this.orderAcceptedByDeliverySathi = orderAcceptedByDeliverySathi;
    }

    public OrderItem(String _id, List<ShopItemData> orderItems, UserInfo shopInfo, UserInfo userInfo, Integer orderStatus, List<String> itemsOnTheWay, boolean itemsOnTheWayVisible, Integer totalAmountToTake, Integer totalAmountToGive, boolean isPaid, boolean itemsOnTheWayCancelled, int itemsOnTheWayActualCost) {
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
        this.itemsOnTheWayCancelled = itemsOnTheWayCancelled;
        this.itemsOnTheWayActualCost = itemsOnTheWayActualCost;
    }

    public OrderItem(String _id, List<ShopItemData> orderItems, UserInfo shopInfo, UserInfo userInfo, Integer orderStatus, List<String> itemsOnTheWay, boolean itemsOnTheWayVisible, Integer totalAmountToTake, Integer totalAmountToGive, boolean isPaid, boolean itemsOnTheWayCancelled) {
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
        this.itemsOnTheWayCancelled = itemsOnTheWayCancelled;
    }

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

    public int getDeliverySathiIncome() {
        return deliverySathiIncome;
    }

    public boolean isAdminShopService() {
        return adminShopService;
    }

    public boolean isOrderAcceptedByDeliverySathi() {
        return orderAcceptedByDeliverySathi;
    }

    public int getItemsOnTheWayActualCost() {
        return itemsOnTheWayActualCost;
    }

    public boolean isItemsOnTheWayCancelled() {
        return itemsOnTheWayCancelled;
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
