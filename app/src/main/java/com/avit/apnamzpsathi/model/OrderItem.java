package com.avit.apnamzpsathi.model;

import java.util.List;

public class OrderItem {

    private String _id;
    private List<ShopItemData> orderItems;
    private UserInfo shopInfo;
    private UserInfo userInfo;

    public OrderItem(String _id, List<ShopItemData> orderItems, UserInfo shopInfo, UserInfo userInfo) {
        this._id = _id;
        this.orderItems = orderItems;
        this.shopInfo = shopInfo;
        this.userInfo = userInfo;
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
