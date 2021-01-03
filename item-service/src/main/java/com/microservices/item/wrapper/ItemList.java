package com.microservices.item.wrapper;

import com.microservices.item.entity.Item;

import java.util.List;

public class ItemList {

    private List<Item> itemList;



    public ItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
