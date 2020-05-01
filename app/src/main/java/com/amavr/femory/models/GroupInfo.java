package com.amavr.femory.models;

import java.util.ArrayList;
import java.util.List;

public class GroupInfo {
    private String id;
    private String name;

    private List<UserInfo> users = new ArrayList<UserInfo>();
    private List<ItemInfo> items = new ArrayList<ItemInfo>();

    public String getId(){
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserInfo> getUsers(){
        return this.users;
    }

    public void setUsers(List<UserInfo> users){
        this.users.clear();
        this.users.addAll(users);
    }

    public List<ItemInfo> getItems(){
        return this.items;
    }

    public void setItems(List<ItemInfo> items){
        this.items.clear();
        this.items.addAll(items);
    }
}
