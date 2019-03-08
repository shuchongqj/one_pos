package com.gzdb.basepos.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.gzdb.supermarket.been.GoodTypesBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GOOD_TYPES_BEAN".
*/
public class GoodTypesBeanDao extends AbstractDao<GoodTypesBean, Long> {

    public static final String TABLENAME = "GOOD_TYPES_BEAN";

    /**
     * Properties of entity GoodTypesBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property AutoId = new Property(0, Long.class, "autoId", true, "_id");
        public final static Property Id = new Property(1, String.class, "id", false, "ID");
        public final static Property Title = new Property(2, String.class, "title", false, "TITLE");
        public final static Property TypeCount = new Property(3, int.class, "typeCount", false, "TYPE_COUNT");
        public final static Property ItemTypeId = new Property(4, String.class, "itemTypeId", false, "ITEM_TYPE_ID");
    }


    public GoodTypesBeanDao(DaoConfig config) {
        super(config);
    }
    
    public GoodTypesBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GOOD_TYPES_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: autoId
                "\"ID\" TEXT," + // 1: id
                "\"TITLE\" TEXT," + // 2: title
                "\"TYPE_COUNT\" INTEGER NOT NULL ," + // 3: typeCount
                "\"ITEM_TYPE_ID\" TEXT);"); // 4: itemTypeId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GOOD_TYPES_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, GoodTypesBean entity) {
        stmt.clearBindings();
 
        Long autoId = entity.getAutoId();
        if (autoId != null) {
            stmt.bindLong(1, autoId);
        }
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(2, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
        stmt.bindLong(4, entity.getTypeCount());
 
        String itemTypeId = entity.getItemTypeId();
        if (itemTypeId != null) {
            stmt.bindString(5, itemTypeId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, GoodTypesBean entity) {
        stmt.clearBindings();
 
        Long autoId = entity.getAutoId();
        if (autoId != null) {
            stmt.bindLong(1, autoId);
        }
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(2, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
        stmt.bindLong(4, entity.getTypeCount());
 
        String itemTypeId = entity.getItemTypeId();
        if (itemTypeId != null) {
            stmt.bindString(5, itemTypeId);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public GoodTypesBean readEntity(Cursor cursor, int offset) {
        GoodTypesBean entity = new GoodTypesBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // autoId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // title
            cursor.getInt(offset + 3), // typeCount
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // itemTypeId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, GoodTypesBean entity, int offset) {
        entity.setAutoId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTypeCount(cursor.getInt(offset + 3));
        entity.setItemTypeId(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(GoodTypesBean entity, long rowId) {
        entity.setAutoId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(GoodTypesBean entity) {
        if(entity != null) {
            return entity.getAutoId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(GoodTypesBean entity) {
        return entity.getAutoId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
