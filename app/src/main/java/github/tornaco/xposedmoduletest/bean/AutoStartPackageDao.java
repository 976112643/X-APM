package github.tornaco.xposedmoduletest.bean;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import github.tornaco.xposedmoduletest.bean.AutoStartPackage;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "AUTO_START_PACKAGE".
*/
public class AutoStartPackageDao extends AbstractDao<AutoStartPackage, Integer> {

    public static final String TABLENAME = "AUTO_START_PACKAGE";

    /**
     * Properties of entity AutoStartPackage.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Integer.class, "id", true, "ID");
        public final static Property PkgName = new Property(1, String.class, "pkgName", false, "PKG_NAME");
        public final static Property Allow = new Property(2, Boolean.class, "allow", false, "ALLOW");
    };


    public AutoStartPackageDao(DaoConfig config) {
        super(config);
    }
    
    public AutoStartPackageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"AUTO_START_PACKAGE\" (" + //
                "\"ID\" INTEGER PRIMARY KEY ," + // 0: id
                "\"PKG_NAME\" TEXT," + // 1: pkgName
                "\"ALLOW\" INTEGER);"); // 2: allow
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"AUTO_START_PACKAGE\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, AutoStartPackage entity) {
        stmt.clearBindings();
 
        Integer id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String pkgName = entity.getPkgName();
        if (pkgName != null) {
            stmt.bindString(2, pkgName);
        }
 
        Boolean allow = entity.getAllow();
        if (allow != null) {
            stmt.bindLong(3, allow ? 1L: 0L);
        }
    }

    /** @inheritdoc */
    @Override
    public Integer readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public AutoStartPackage readEntity(Cursor cursor, int offset) {
        AutoStartPackage entity = new AutoStartPackage( //
            cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // pkgName
            cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0 // allow
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, AutoStartPackage entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
        entity.setPkgName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAllow(cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Integer updateKeyAfterInsert(AutoStartPackage entity, long rowId) {
        return entity.getId();
    }
    
    /** @inheritdoc */
    @Override
    public Integer getKey(AutoStartPackage entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}