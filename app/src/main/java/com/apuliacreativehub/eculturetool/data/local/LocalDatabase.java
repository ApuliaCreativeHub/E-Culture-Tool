package com.apuliacreativehub.eculturetool.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.apuliacreativehub.eculturetool.data.entity.IsPresentIn;
import com.apuliacreativehub.eculturetool.data.entity.Object;
import com.apuliacreativehub.eculturetool.data.entity.Path;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.entity.VisitorIsPresentIn;
import com.apuliacreativehub.eculturetool.data.entity.VisitorPath;
import com.apuliacreativehub.eculturetool.data.entity.Zone;

@Database(entities = {Place.class, Zone.class, Object.class, IsPresentIn.class, Path.class, VisitorIsPresentIn.class, VisitorPath.class}, version = 1, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract LocalPlaceDAO placeDAO();

    public abstract LocalZoneDAO zoneDAO();

    public abstract LocalObjectDAO objectDAO();

    public abstract LocalPathDAO pathDAO();

    public abstract LocalIsPresentInDAO isPresentInDAO();

    public abstract VisitorPathDAO visitorPathDAO();

    public abstract VisitorIsPresentInDAO visitorIsPresentInDAO();

    public void clearTables() {
        pathDAO().clearTable();
        isPresentInDAO().clearTable();
    }

    // Add other DAOs abstract methods here
}
