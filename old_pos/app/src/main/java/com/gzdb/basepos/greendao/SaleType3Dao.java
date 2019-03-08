package com.gzdb.basepos.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.gzdb.sale.bean.SaleType3;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SALE_TYPE3".
*/
public class SaleType3Dao extends AbstractDao<SaleType3, Long> {

    public static final String TABLENAME = "SALE_TYPE3";

    /**
     * Properties of entity SaleType3.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ActivityId = new Property(1, int.class, "activityId", false, "ACTIVITY_ID");
        public final static Property Price = new Property(2, double.class, "price", false, "PRICE");
        public final static Property Discount = new Property(3, double.class, "discount", false, "DISCOUNT");
    }


    public SaleType3Dao(DaoConfig config) {
        super(config);
    }
    
    public SaleType3Dao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SALE_TYPE3\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"ACTIVITY_ID\" INTEGER NOT NULL ," + // 1: activityId
                "\"PRICE\" REAL NOT NULL ," + // 2: price
                "\"DISCOUNT\" REAL NOT NULL );"); // 3: discount
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SALE_TYPE3\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SaleType3 entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getActivityId());
        stmt.bindDouble(3, entity.getPrice());
        stmt.bindDouble(4, entity.getDiscount());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SaleType3 entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getActivityId());
        stmt.bindDouble(3, entity.getPrice());
        stmt.bindDouble(4, entity.getDiscount());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public SaleType3 readEntity(Cursor cursor, int offset) {
        SaleType3 entity = new SaleType3( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // activityId
            cursor.getDouble(offset + 2), // price
            cursor.getDouble(offset + 3) // discount
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SaleType3 entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setActivityId(cursor.getInt(offset + 1));
        entity.setPrice(cursor.getDouble(offset + 2));
        entity.setDiscount(cursor.getDouble(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(SaleType3 entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(SaleType3 entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SaleType3 entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
