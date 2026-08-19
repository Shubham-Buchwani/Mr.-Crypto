package com.example.cryptopulse.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.cryptopulse.data.local.dao.CoinMarketDao;
import com.example.cryptopulse.data.local.dao.CoinMarketDao_Impl;
import com.example.cryptopulse.data.local.dao.FavoriteCoinDao;
import com.example.cryptopulse.data.local.dao.FavoriteCoinDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CryptoPulseDatabase_Impl extends CryptoPulseDatabase {
  private volatile CoinMarketDao _coinMarketDao;

  private volatile FavoriteCoinDao _favoriteCoinDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `coin_markets` (`id` TEXT NOT NULL, `symbol` TEXT NOT NULL, `name` TEXT NOT NULL, `image` TEXT NOT NULL, `currentPrice` REAL NOT NULL, `marketCap` REAL NOT NULL, `marketCapRank` INTEGER NOT NULL, `totalVolume` REAL NOT NULL, `high24h` REAL NOT NULL, `low24h` REAL NOT NULL, `priceChange24h` REAL NOT NULL, `priceChangePercentage24h` REAL NOT NULL, `circulatingSupply` REAL NOT NULL, `totalSupply` REAL, `maxSupply` REAL, `ath` REAL NOT NULL, `athChangePercentage` REAL NOT NULL, `athDate` TEXT NOT NULL, `atl` REAL NOT NULL, `atlChangePercentage` REAL NOT NULL, `atlDate` TEXT NOT NULL, `lastUpdated` INTEGER NOT NULL, `currency` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `favorite_coins` (`coinId` TEXT NOT NULL, `addedAt` INTEGER NOT NULL, PRIMARY KEY(`coinId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '5d8b4f3c398c7bfa9028feb8d548f37c')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `coin_markets`");
        db.execSQL("DROP TABLE IF EXISTS `favorite_coins`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsCoinMarkets = new HashMap<String, TableInfo.Column>(23);
        _columnsCoinMarkets.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("symbol", new TableInfo.Column("symbol", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("image", new TableInfo.Column("image", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("currentPrice", new TableInfo.Column("currentPrice", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("marketCap", new TableInfo.Column("marketCap", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("marketCapRank", new TableInfo.Column("marketCapRank", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("totalVolume", new TableInfo.Column("totalVolume", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("high24h", new TableInfo.Column("high24h", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("low24h", new TableInfo.Column("low24h", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("priceChange24h", new TableInfo.Column("priceChange24h", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("priceChangePercentage24h", new TableInfo.Column("priceChangePercentage24h", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("circulatingSupply", new TableInfo.Column("circulatingSupply", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("totalSupply", new TableInfo.Column("totalSupply", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("maxSupply", new TableInfo.Column("maxSupply", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("ath", new TableInfo.Column("ath", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("athChangePercentage", new TableInfo.Column("athChangePercentage", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("athDate", new TableInfo.Column("athDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("atl", new TableInfo.Column("atl", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("atlChangePercentage", new TableInfo.Column("atlChangePercentage", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("atlDate", new TableInfo.Column("atlDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("lastUpdated", new TableInfo.Column("lastUpdated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoinMarkets.put("currency", new TableInfo.Column("currency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCoinMarkets = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCoinMarkets = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCoinMarkets = new TableInfo("coin_markets", _columnsCoinMarkets, _foreignKeysCoinMarkets, _indicesCoinMarkets);
        final TableInfo _existingCoinMarkets = TableInfo.read(db, "coin_markets");
        if (!_infoCoinMarkets.equals(_existingCoinMarkets)) {
          return new RoomOpenHelper.ValidationResult(false, "coin_markets(com.example.cryptopulse.data.local.entity.CoinMarketEntity).\n"
                  + " Expected:\n" + _infoCoinMarkets + "\n"
                  + " Found:\n" + _existingCoinMarkets);
        }
        final HashMap<String, TableInfo.Column> _columnsFavoriteCoins = new HashMap<String, TableInfo.Column>(2);
        _columnsFavoriteCoins.put("coinId", new TableInfo.Column("coinId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteCoins.put("addedAt", new TableInfo.Column("addedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFavoriteCoins = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFavoriteCoins = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFavoriteCoins = new TableInfo("favorite_coins", _columnsFavoriteCoins, _foreignKeysFavoriteCoins, _indicesFavoriteCoins);
        final TableInfo _existingFavoriteCoins = TableInfo.read(db, "favorite_coins");
        if (!_infoFavoriteCoins.equals(_existingFavoriteCoins)) {
          return new RoomOpenHelper.ValidationResult(false, "favorite_coins(com.example.cryptopulse.data.local.entity.FavoriteCoinEntity).\n"
                  + " Expected:\n" + _infoFavoriteCoins + "\n"
                  + " Found:\n" + _existingFavoriteCoins);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "5d8b4f3c398c7bfa9028feb8d548f37c", "57eaba0afc3f499a05ae2560809ea3fe");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "coin_markets","favorite_coins");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `coin_markets`");
      _db.execSQL("DELETE FROM `favorite_coins`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(CoinMarketDao.class, CoinMarketDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FavoriteCoinDao.class, FavoriteCoinDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public CoinMarketDao coinMarketDao() {
    if (_coinMarketDao != null) {
      return _coinMarketDao;
    } else {
      synchronized(this) {
        if(_coinMarketDao == null) {
          _coinMarketDao = new CoinMarketDao_Impl(this);
        }
        return _coinMarketDao;
      }
    }
  }

  @Override
  public FavoriteCoinDao favoriteCoinDao() {
    if (_favoriteCoinDao != null) {
      return _favoriteCoinDao;
    } else {
      synchronized(this) {
        if(_favoriteCoinDao == null) {
          _favoriteCoinDao = new FavoriteCoinDao_Impl(this);
        }
        return _favoriteCoinDao;
      }
    }
  }
}
