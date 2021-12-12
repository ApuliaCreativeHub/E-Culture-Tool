package com.apuliacreativehub.eculturetool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.viewmodels.PathViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PathsAdapter extends BaseAdapter {

    /**
    private List<PathViewModel> listPaths = new ArrayList<>(Arrays.asList(
            new PathViewModel("Museo d'arte Bari", "Percorso Mio", new Date()),
            new PathViewModel("Museo d'arte Torino", "Percorso Standard", new Date()),
            new PathViewModel("Museo Scultura Matera", "Percorso Avventuriero", new Date()),
            new PathViewModel("Museo d'arte Bari", "Percorso Standard", new Date())));
     **/
    private List<PathViewModel> listPaths = new ArrayList<>();

    private Context mContext;
    private int mResource;
    private String outputLastDate;

    public PathsAdapter(Context context) {
        mContext = context;
        outputLastDate = context.getString(R.string.list_paths_last_used);
    }

    @Override
    public int getCount() {
        return listPaths.size();
    }

    @Override
    public PathViewModel getItem(int i) {
        return listPaths.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.list_paths, viewGroup, false);
        }

        PathViewModel path = getItem(i);
        return setOneViewItem(path, view);
    }

    private View setOneViewItem(PathViewModel path, View view) {
        ((TextView) view.findViewById(R.id.textListPathName)).setText(path.getName());
        ((TextView) view.findViewById(R.id.textListPathPlace)).setText(path.getPlace());

        Calendar pathLastUsed = Calendar.getInstance();
        pathLastUsed.setTime(path.getLastUsed());
        ((TextView) view.findViewById(R.id.textListPathLastDate)).setText(
                outputLastDate + " " + pathLastUsed.get(Calendar.DAY_OF_MONTH) + "/" + pathLastUsed.get(Calendar.MONTH) + "/" + pathLastUsed.get(Calendar.YEAR)
        );
        return view;
    }

}
