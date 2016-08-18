package com.sss.helparound.ui.components.items;



public class SectionItem implements Item{

	private static final long serialVersionUID = 1L;
	
	private final String title;

    public SectionItem(final String title){
        this.title = title;
    }

    @Override
    public boolean isSection(){
        return true;
    }

    public String getTitle() {
        return title;
    }

}
