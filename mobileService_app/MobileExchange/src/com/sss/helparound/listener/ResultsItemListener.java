package com.sss.helparound.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import com.sss.helparound.constant.Extra;
import com.sss.helparound.model.Ad;
import com.sss.helparound.ui.AdDescriptionActivity;
import com.sss.helparound.ui.ListAdActivity;
import com.sss.helparound.ui.MyAdDescriptionActivity;

public class ResultsItemListener implements ListView.OnItemClickListener {

	private ArrayList<Ad> results;
	private Context context;

	public ResultsItemListener(final ArrayList<Ad> results, final Context context) {
		super();
		this.results = results;
		this.context = context;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

        Intent intent = null;

        if (ListAdActivity.mIsMyAd == false) {
            intent = new Intent(context, AdDescriptionActivity.class);
            Bundle b = new Bundle();
            b.putSerializable(Extra.RESULT, results.get(position));
            intent.putExtras(b);
            context.startActivity(intent);

        } else {
            intent = new Intent(context, MyAdDescriptionActivity.class);
            Bundle b = new Bundle();
            b.putSerializable(Extra.RESULT, results.get(position));
            intent.putExtras(b);
            ((Activity)context).startActivityForResult(intent, 1);
        }
	}

}
