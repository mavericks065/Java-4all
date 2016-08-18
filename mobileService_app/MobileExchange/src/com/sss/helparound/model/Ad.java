package com.sss.helparound.model;

import java.io.Serializable;
import java.util.List;

import com.sss.helparound.ui.components.items.EntryItem;

public class Ad implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private String price;
    private List<EntryItem> categories;
    private User owner;
    private OperatingPlace place;

    public Ad() {
    }

    public Ad(final String title, final String description, final String price,
              final List<EntryItem> categories, final User owner) {
        super();
        this.title = title;
        this.description = description;
        this.price = price;
        this.categories = categories;
        this.owner = owner;
    }

    public Ad(final String  title, final String  description, final String  price, final List<EntryItem> categories,
              final User owner, final OperatingPlace place) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.categories = categories;
        this.owner = owner;
        this.place = place;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(final String price) {
        this.price = price;
    }

    public List<EntryItem> getCategories() {
        return categories;
    }

    public void setCategories(final List<EntryItem> categories) {
        this.categories = categories;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public OperatingPlace getPlace() {
        return place;
    }

    public void setPlace(final OperatingPlace place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return title;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (Ad.class != obj.getClass()) {
            return false;
        }
        Ad other = (Ad) obj;
        if (owner == null) {
            if (other.owner != null)
                return false;
        } else if (!owner.equals(other.owner)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        } else if (!title.equals(other.title)) {
            return false;
        }
        return true;
    }

}
