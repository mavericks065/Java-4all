package com.sss.helparound.ui;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.sss.helparound.R;
import com.sss.helparound.ui.components.items.EntryItem;
import com.sss.helparound.ui.components.items.Item;
import com.sss.helparound.ui.components.items.SectionItem;

public class EntryAdapter extends ArrayAdapter<Item> {
    private ArrayList<Item> items;
    private LayoutInflater vi;

    public EntryAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
        this.items = items;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final Item i = items.get(position);
        if (i != null) {
            if(i.isSection()){
                SectionItem si = (SectionItem)i;
                v = vi.inflate(R.layout.list_item_section, null);
                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);
                final TextView sectionView =
                        (TextView) v.findViewById(R.id.list_item_section_text);
                sectionView.setText(si.getTitle());
            } else {
                EntryItem ei = (EntryItem)i;
                v = vi.inflate(R.layout.list_item_entry, null);
                final TextView title =
                        (TextView)v.findViewById(R.id.list_item_entry_title);

                if (title != null) title.setText(ei.getTitle());

            }
        }
        return v;
    }

    /**
     * return the list of items
     * @return items
     */
    public ArrayList<Item> getItems() {
        return items;
    }
}