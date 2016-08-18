package com.sss.helparound.ui.components.items;

import com.sss.helparound.constant.ItemType;

public class EntryItem implements Item{

	private static final long serialVersionUID = 1L;
	
	private String title;
	private ItemType itemType;

    public EntryItem(String title, ItemType itemType) {
        this.title = title;
        this.itemType = itemType;
    }
    
    public ItemType getItemType() {
		return itemType;
	}

	@Override
    public boolean isSection() {
        return false;
    }

    public String getTitle(){
        return title;
    }
}
