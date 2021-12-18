package com.apuliacreativehub.eculturetool.ui.paths;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apuliacreativehub.eculturetool.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PathsAdapter extends BaseAdapter {

    private List<PathViewModel> paths = getPathsList();
    private List<PathViewModel> listPaths;
    private Boolean filterPlaces;
    private Boolean filterName;
    private Boolean filterDate;

    private final Context mContext;
    private int mResource;
    private final String outputLastDate;

    public PathsAdapter(Context context) {
        mContext = context;
        outputLastDate = context.getString(R.string.list_paths_last_used);
        paths = getPathsList();
        listPaths = paths;
        filterPlaces = true;
        filterName = true;
        filterDate = true;
    }

    public void setFilterPlaces(Boolean filterPlaces) {
        this.filterPlaces = filterPlaces;
    }

    public void setFilterName(Boolean filterName) {
        this.filterName = filterName;
    }

    public void setFilterDate(Boolean filterDate) {
        this.filterDate = filterDate;
    }

    public boolean getFilterPlaces() { return this.filterPlaces; }

    public boolean getFilterName() { return this.filterName; }

    public boolean getFilterDate() { return this.filterDate; }


    public void applyFilter(String query) {
        listPaths = new ArrayList<PathViewModel>();
        Boolean canAdd;
        for (PathViewModel path : paths) {
            canAdd = (filterPlaces && findString(path.getPlace(), query)) ||
                    (filterDate && findString(path.getLastUsed().toString(), query)) ||
                    (filterName && findString(path.getName(), query));

            if (canAdd) listPaths.add(path);
        }
    }

    public void restoreAll() {
        listPaths = new ArrayList<>(paths);
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

    /**
     * TODO: We have to implement the API to fetch data
     */
    private List<PathViewModel> getPathsList() {
        return new ArrayList<>(Arrays.asList(
                new PathViewModel("Museo d'arte Bari", "Percorso Mio", new Date()),
                new PathViewModel("Museo d'arte Torino", "Percorso Standard", new Date()),
                new PathViewModel("Museo Scultura Matera", "Percorso Avventuriero", new Date()),
                new PathViewModel("Museo d'arte Bari", "Percorso Standard", new Date())));
    }

    private static Boolean findString(String source, String matcher) {
        return source.toLowerCase(Locale.ROOT).contains(matcher.toLowerCase(Locale.ROOT));
    }
}