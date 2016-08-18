package com.sss.helparound.model;

import com.sss.helparound.ui.components.items.EntryItem;

public class Filter {

    private EntryItem category;
    private User user;
    private String query;

    public Filter() {
    }

    public EntryItem getCategory() {
        return category;
    }

    public void setCategory(EntryItem category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
