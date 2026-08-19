package com.example.cryptopulse.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.cryptopulse.data.local.entity.CoinMarketEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CoinMarketDao_Impl implements CoinMarketDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CoinMarketEntity> __insertionAdapterOfCoinMarketEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public CoinMarketDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCoinMarketEntity = new EntityInsertionAdapter<CoinMarketEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `coin_markets` (`id`,`symbol`,`name`,`image`,`currentPrice`,`marketCap`,`marketCapRank`,`totalVolume`,`high24h`,`low24h`,`priceChange24h`,`priceChangePercentage24h`,`circulatingSupply`,`totalSupply`,`maxSupply`,`ath`,`athChangePercentage`,`athDate`,`atl`,`atlChangePercentage`,`atlDate`,`lastUpdated`,`currency`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CoinMarketEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getSymbol());
        statement.bindString(3, entity.getName());
        statement.bindString(4, entity.getImage());
        statement.bindDouble(5, entity.getCurrentPrice());
        statement.bindDouble(6, entity.getMarketCap());
        statement.bindLong(7, entity.getMarketCapRank());
        statement.bindDouble(8, entity.getTotalVolume());
        statement.bindDouble(9, entity.getHigh24h());
        statement.bindDouble(10, entity.getLow24h());
        statement.bindDouble(11, entity.getPriceChange24h());
        statement.bindDouble(12, entity.getPriceChangePercentage24h());
        statement.bindDouble(13, entity.getCirculatingSupply());
        if (entity.getTotalSupply() == null) {
          statement.bindNull(14);
        } else {
          statement.bindDouble(14, entity.getTotalSupply());
        }
        if (entity.getMaxSupply() == null) {
          statement.bindNull(15);
        } else {
          statement.bindDouble(15, entity.getMaxSupply());
        }
        statement.bindDouble(16, entity.getAth());
        statement.bindDouble(17, entity.getAthChangePercentage());
        statement.bindString(18, entity.getAthDate());
        statement.bindDouble(19, entity.getAtl());
        statement.bindDouble(20, entity.getAtlChangePercentage());
        statement.bindString(21, entity.getAtlDate());
        statement.bindLong(22, entity.getLastUpdated());
        statement.bindString(23, entity.getCurrency());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM coin_markets WHERE currency = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<CoinMarketEntity> coins,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCoinMarketEntity.insert(coins);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final String currency, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, currency);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CoinMarketEntity>> getAllCoins(final String currency) {
    final String _sql = "SELECT * FROM coin_markets WHERE currency = ? ORDER BY marketCapRank ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, currency);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"coin_markets"}, new Callable<List<CoinMarketEntity>>() {
      @Override
      @NonNull
      public List<CoinMarketEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
          final int _cursorIndexOfCurrentPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "currentPrice");
          final int _cursorIndexOfMarketCap = CursorUtil.getColumnIndexOrThrow(_cursor, "marketCap");
          final int _cursorIndexOfMarketCapRank = CursorUtil.getColumnIndexOrThrow(_cursor, "marketCapRank");
          final int _cursorIndexOfTotalVolume = CursorUtil.getColumnIndexOrThrow(_cursor, "totalVolume");
          final int _cursorIndexOfHigh24h = CursorUtil.getColumnIndexOrThrow(_cursor, "high24h");
          final int _cursorIndexOfLow24h = CursorUtil.getColumnIndexOrThrow(_cursor, "low24h");
          final int _cursorIndexOfPriceChange24h = CursorUtil.getColumnIndexOrThrow(_cursor, "priceChange24h");
          final int _cursorIndexOfPriceChangePercentage24h = CursorUtil.getColumnIndexOrThrow(_cursor, "priceChangePercentage24h");
          final int _cursorIndexOfCirculatingSupply = CursorUtil.getColumnIndexOrThrow(_cursor, "circulatingSupply");
          final int _cursorIndexOfTotalSupply = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSupply");
          final int _cursorIndexOfMaxSupply = CursorUtil.getColumnIndexOrThrow(_cursor, "maxSupply");
          final int _cursorIndexOfAth = CursorUtil.getColumnIndexOrThrow(_cursor, "ath");
          final int _cursorIndexOfAthChangePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "athChangePercentage");
          final int _cursorIndexOfAthDate = CursorUtil.getColumnIndexOrThrow(_cursor, "athDate");
          final int _cursorIndexOfAtl = CursorUtil.getColumnIndexOrThrow(_cursor, "atl");
          final int _cursorIndexOfAtlChangePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "atlChangePercentage");
          final int _cursorIndexOfAtlDate = CursorUtil.getColumnIndexOrThrow(_cursor, "atlDate");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final List<CoinMarketEntity> _result = new ArrayList<CoinMarketEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CoinMarketEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSymbol;
            _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpImage;
            _tmpImage = _cursor.getString(_cursorIndexOfImage);
            final double _tmpCurrentPrice;
            _tmpCurrentPrice = _cursor.getDouble(_cursorIndexOfCurrentPrice);
            final double _tmpMarketCap;
            _tmpMarketCap = _cursor.getDouble(_cursorIndexOfMarketCap);
            final int _tmpMarketCapRank;
            _tmpMarketCapRank = _cursor.getInt(_cursorIndexOfMarketCapRank);
            final double _tmpTotalVolume;
            _tmpTotalVolume = _cursor.getDouble(_cursorIndexOfTotalVolume);
            final double _tmpHigh24h;
            _tmpHigh24h = _cursor.getDouble(_cursorIndexOfHigh24h);
            final double _tmpLow24h;
            _tmpLow24h = _cursor.getDouble(_cursorIndexOfLow24h);
            final double _tmpPriceChange24h;
            _tmpPriceChange24h = _cursor.getDouble(_cursorIndexOfPriceChange24h);
            final double _tmpPriceChangePercentage24h;
            _tmpPriceChangePercentage24h = _cursor.getDouble(_cursorIndexOfPriceChangePercentage24h);
            final double _tmpCirculatingSupply;
            _tmpCirculatingSupply = _cursor.getDouble(_cursorIndexOfCirculatingSupply);
            final Double _tmpTotalSupply;
            if (_cursor.isNull(_cursorIndexOfTotalSupply)) {
              _tmpTotalSupply = null;
            } else {
              _tmpTotalSupply = _cursor.getDouble(_cursorIndexOfTotalSupply);
            }
            final Double _tmpMaxSupply;
            if (_cursor.isNull(_cursorIndexOfMaxSupply)) {
              _tmpMaxSupply = null;
            } else {
              _tmpMaxSupply = _cursor.getDouble(_cursorIndexOfMaxSupply);
            }
            final double _tmpAth;
            _tmpAth = _cursor.getDouble(_cursorIndexOfAth);
            final double _tmpAthChangePercentage;
            _tmpAthChangePercentage = _cursor.getDouble(_cursorIndexOfAthChangePercentage);
            final String _tmpAthDate;
            _tmpAthDate = _cursor.getString(_cursorIndexOfAthDate);
            final double _tmpAtl;
            _tmpAtl = _cursor.getDouble(_cursorIndexOfAtl);
            final double _tmpAtlChangePercentage;
            _tmpAtlChangePercentage = _cursor.getDouble(_cursorIndexOfAtlChangePercentage);
            final String _tmpAtlDate;
            _tmpAtlDate = _cursor.getString(_cursorIndexOfAtlDate);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            _item = new CoinMarketEntity(_tmpId,_tmpSymbol,_tmpName,_tmpImage,_tmpCurrentPrice,_tmpMarketCap,_tmpMarketCapRank,_tmpTotalVolume,_tmpHigh24h,_tmpLow24h,_tmpPriceChange24h,_tmpPriceChangePercentage24h,_tmpCirculatingSupply,_tmpTotalSupply,_tmpMaxSupply,_tmpAth,_tmpAthChangePercentage,_tmpAthDate,_tmpAtl,_tmpAtlChangePercentage,_tmpAtlDate,_tmpLastUpdated,_tmpCurrency);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllCoinsList(final String currency,
      final Continuation<? super List<CoinMarketEntity>> $completion) {
    final String _sql = "SELECT * FROM coin_markets WHERE currency = ? ORDER BY marketCapRank ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, currency);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CoinMarketEntity>>() {
      @Override
      @NonNull
      public List<CoinMarketEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
          final int _cursorIndexOfCurrentPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "currentPrice");
          final int _cursorIndexOfMarketCap = CursorUtil.getColumnIndexOrThrow(_cursor, "marketCap");
          final int _cursorIndexOfMarketCapRank = CursorUtil.getColumnIndexOrThrow(_cursor, "marketCapRank");
          final int _cursorIndexOfTotalVolume = CursorUtil.getColumnIndexOrThrow(_cursor, "totalVolume");
          final int _cursorIndexOfHigh24h = CursorUtil.getColumnIndexOrThrow(_cursor, "high24h");
          final int _cursorIndexOfLow24h = CursorUtil.getColumnIndexOrThrow(_cursor, "low24h");
          final int _cursorIndexOfPriceChange24h = CursorUtil.getColumnIndexOrThrow(_cursor, "priceChange24h");
          final int _cursorIndexOfPriceChangePercentage24h = CursorUtil.getColumnIndexOrThrow(_cursor, "priceChangePercentage24h");
          final int _cursorIndexOfCirculatingSupply = CursorUtil.getColumnIndexOrThrow(_cursor, "circulatingSupply");
          final int _cursorIndexOfTotalSupply = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSupply");
          final int _cursorIndexOfMaxSupply = CursorUtil.getColumnIndexOrThrow(_cursor, "maxSupply");
          final int _cursorIndexOfAth = CursorUtil.getColumnIndexOrThrow(_cursor, "ath");
          final int _cursorIndexOfAthChangePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "athChangePercentage");
          final int _cursorIndexOfAthDate = CursorUtil.getColumnIndexOrThrow(_cursor, "athDate");
          final int _cursorIndexOfAtl = CursorUtil.getColumnIndexOrThrow(_cursor, "atl");
          final int _cursorIndexOfAtlChangePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "atlChangePercentage");
          final int _cursorIndexOfAtlDate = CursorUtil.getColumnIndexOrThrow(_cursor, "atlDate");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final List<CoinMarketEntity> _result = new ArrayList<CoinMarketEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CoinMarketEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSymbol;
            _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpImage;
            _tmpImage = _cursor.getString(_cursorIndexOfImage);
            final double _tmpCurrentPrice;
            _tmpCurrentPrice = _cursor.getDouble(_cursorIndexOfCurrentPrice);
            final double _tmpMarketCap;
            _tmpMarketCap = _cursor.getDouble(_cursorIndexOfMarketCap);
            final int _tmpMarketCapRank;
            _tmpMarketCapRank = _cursor.getInt(_cursorIndexOfMarketCapRank);
            final double _tmpTotalVolume;
            _tmpTotalVolume = _cursor.getDouble(_cursorIndexOfTotalVolume);
            final double _tmpHigh24h;
            _tmpHigh24h = _cursor.getDouble(_cursorIndexOfHigh24h);
            final double _tmpLow24h;
            _tmpLow24h = _cursor.getDouble(_cursorIndexOfLow24h);
            final double _tmpPriceChange24h;
            _tmpPriceChange24h = _cursor.getDouble(_cursorIndexOfPriceChange24h);
            final double _tmpPriceChangePercentage24h;
            _tmpPriceChangePercentage24h = _cursor.getDouble(_cursorIndexOfPriceChangePercentage24h);
            final double _tmpCirculatingSupply;
            _tmpCirculatingSupply = _cursor.getDouble(_cursorIndexOfCirculatingSupply);
            final Double _tmpTotalSupply;
            if (_cursor.isNull(_cursorIndexOfTotalSupply)) {
              _tmpTotalSupply = null;
            } else {
              _tmpTotalSupply = _cursor.getDouble(_cursorIndexOfTotalSupply);
            }
            final Double _tmpMaxSupply;
            if (_cursor.isNull(_cursorIndexOfMaxSupply)) {
              _tmpMaxSupply = null;
            } else {
              _tmpMaxSupply = _cursor.getDouble(_cursorIndexOfMaxSupply);
            }
            final double _tmpAth;
            _tmpAth = _cursor.getDouble(_cursorIndexOfAth);
            final double _tmpAthChangePercentage;
            _tmpAthChangePercentage = _cursor.getDouble(_cursorIndexOfAthChangePercentage);
            final String _tmpAthDate;
            _tmpAthDate = _cursor.getString(_cursorIndexOfAthDate);
            final double _tmpAtl;
            _tmpAtl = _cursor.getDouble(_cursorIndexOfAtl);
            final double _tmpAtlChangePercentage;
            _tmpAtlChangePercentage = _cursor.getDouble(_cursorIndexOfAtlChangePercentage);
            final String _tmpAtlDate;
            _tmpAtlDate = _cursor.getString(_cursorIndexOfAtlDate);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            _item = new CoinMarketEntity(_tmpId,_tmpSymbol,_tmpName,_tmpImage,_tmpCurrentPrice,_tmpMarketCap,_tmpMarketCapRank,_tmpTotalVolume,_tmpHigh24h,_tmpLow24h,_tmpPriceChange24h,_tmpPriceChangePercentage24h,_tmpCirculatingSupply,_tmpTotalSupply,_tmpMaxSupply,_tmpAth,_tmpAthChangePercentage,_tmpAthDate,_tmpAtl,_tmpAtlChangePercentage,_tmpAtlDate,_tmpLastUpdated,_tmpCurrency);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CoinMarketEntity>> getCoinsByIds(final List<String> ids, final String currency) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM coin_markets WHERE id IN (");
    final int _inputSize = ids.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(") AND currency = ");
    _stringBuilder.append("?");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 1 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : ids) {
      _statement.bindString(_argIndex, _item);
      _argIndex++;
    }
    _argIndex = 1 + _inputSize;
    _statement.bindString(_argIndex, currency);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"coin_markets"}, new Callable<List<CoinMarketEntity>>() {
      @Override
      @NonNull
      public List<CoinMarketEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
          final int _cursorIndexOfCurrentPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "currentPrice");
          final int _cursorIndexOfMarketCap = CursorUtil.getColumnIndexOrThrow(_cursor, "marketCap");
          final int _cursorIndexOfMarketCapRank = CursorUtil.getColumnIndexOrThrow(_cursor, "marketCapRank");
          final int _cursorIndexOfTotalVolume = CursorUtil.getColumnIndexOrThrow(_cursor, "totalVolume");
          final int _cursorIndexOfHigh24h = CursorUtil.getColumnIndexOrThrow(_cursor, "high24h");
          final int _cursorIndexOfLow24h = CursorUtil.getColumnIndexOrThrow(_cursor, "low24h");
          final int _cursorIndexOfPriceChange24h = CursorUtil.getColumnIndexOrThrow(_cursor, "priceChange24h");
          final int _cursorIndexOfPriceChangePercentage24h = CursorUtil.getColumnIndexOrThrow(_cursor, "priceChangePercentage24h");
          final int _cursorIndexOfCirculatingSupply = CursorUtil.getColumnIndexOrThrow(_cursor, "circulatingSupply");
          final int _cursorIndexOfTotalSupply = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSupply");
          final int _cursorIndexOfMaxSupply = CursorUtil.getColumnIndexOrThrow(_cursor, "maxSupply");
          final int _cursorIndexOfAth = CursorUtil.getColumnIndexOrThrow(_cursor, "ath");
          final int _cursorIndexOfAthChangePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "athChangePercentage");
          final int _cursorIndexOfAthDate = CursorUtil.getColumnIndexOrThrow(_cursor, "athDate");
          final int _cursorIndexOfAtl = CursorUtil.getColumnIndexOrThrow(_cursor, "atl");
          final int _cursorIndexOfAtlChangePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "atlChangePercentage");
          final int _cursorIndexOfAtlDate = CursorUtil.getColumnIndexOrThrow(_cursor, "atlDate");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final List<CoinMarketEntity> _result = new ArrayList<CoinMarketEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CoinMarketEntity _item_1;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSymbol;
            _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpImage;
            _tmpImage = _cursor.getString(_cursorIndexOfImage);
            final double _tmpCurrentPrice;
            _tmpCurrentPrice = _cursor.getDouble(_cursorIndexOfCurrentPrice);
            final double _tmpMarketCap;
            _tmpMarketCap = _cursor.getDouble(_cursorIndexOfMarketCap);
            final int _tmpMarketCapRank;
            _tmpMarketCapRank = _cursor.getInt(_cursorIndexOfMarketCapRank);
            final double _tmpTotalVolume;
            _tmpTotalVolume = _cursor.getDouble(_cursorIndexOfTotalVolume);
            final double _tmpHigh24h;
            _tmpHigh24h = _cursor.getDouble(_cursorIndexOfHigh24h);
            final double _tmpLow24h;
            _tmpLow24h = _cursor.getDouble(_cursorIndexOfLow24h);
            final double _tmpPriceChange24h;
            _tmpPriceChange24h = _cursor.getDouble(_cursorIndexOfPriceChange24h);
            final double _tmpPriceChangePercentage24h;
            _tmpPriceChangePercentage24h = _cursor.getDouble(_cursorIndexOfPriceChangePercentage24h);
            final double _tmpCirculatingSupply;
            _tmpCirculatingSupply = _cursor.getDouble(_cursorIndexOfCirculatingSupply);
            final Double _tmpTotalSupply;
            if (_cursor.isNull(_cursorIndexOfTotalSupply)) {
              _tmpTotalSupply = null;
            } else {
              _tmpTotalSupply = _cursor.getDouble(_cursorIndexOfTotalSupply);
            }
            final Double _tmpMaxSupply;
            if (_cursor.isNull(_cursorIndexOfMaxSupply)) {
              _tmpMaxSupply = null;
            } else {
              _tmpMaxSupply = _cursor.getDouble(_cursorIndexOfMaxSupply);
            }
            final double _tmpAth;
            _tmpAth = _cursor.getDouble(_cursorIndexOfAth);
            final double _tmpAthChangePercentage;
            _tmpAthChangePercentage = _cursor.getDouble(_cursorIndexOfAthChangePercentage);
            final String _tmpAthDate;
            _tmpAthDate = _cursor.getString(_cursorIndexOfAthDate);
            final double _tmpAtl;
            _tmpAtl = _cursor.getDouble(_cursorIndexOfAtl);
            final double _tmpAtlChangePercentage;
            _tmpAtlChangePercentage = _cursor.getDouble(_cursorIndexOfAtlChangePercentage);
            final String _tmpAtlDate;
            _tmpAtlDate = _cursor.getString(_cursorIndexOfAtlDate);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            _item_1 = new CoinMarketEntity(_tmpId,_tmpSymbol,_tmpName,_tmpImage,_tmpCurrentPrice,_tmpMarketCap,_tmpMarketCapRank,_tmpTotalVolume,_tmpHigh24h,_tmpLow24h,_tmpPriceChange24h,_tmpPriceChangePercentage24h,_tmpCirculatingSupply,_tmpTotalSupply,_tmpMaxSupply,_tmpAth,_tmpAthChangePercentage,_tmpAthDate,_tmpAtl,_tmpAtlChangePercentage,_tmpAtlDate,_tmpLastUpdated,_tmpCurrency);
            _result.add(_item_1);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getCoinsByIdsList(final List<String> ids, final String currency,
      final Continuation<? super List<CoinMarketEntity>> $completion) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM coin_markets WHERE id IN (");
    final int _inputSize = ids.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(") AND currency = ");
    _stringBuilder.append("?");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 1 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : ids) {
      _statement.bindString(_argIndex, _item);
      _argIndex++;
    }
    _argIndex = 1 + _inputSize;
    _statement.bindString(_argIndex, currency);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CoinMarketEntity>>() {
      @Override
      @NonNull
      public List<CoinMarketEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
          final int _cursorIndexOfCurrentPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "currentPrice");
          final int _cursorIndexOfMarketCap = CursorUtil.getColumnIndexOrThrow(_cursor, "marketCap");
          final int _cursorIndexOfMarketCapRank = CursorUtil.getColumnIndexOrThrow(_cursor, "marketCapRank");
          final int _cursorIndexOfTotalVolume = CursorUtil.getColumnIndexOrThrow(_cursor, "totalVolume");
          final int _cursorIndexOfHigh24h = CursorUtil.getColumnIndexOrThrow(_cursor, "high24h");
          final int _cursorIndexOfLow24h = CursorUtil.getColumnIndexOrThrow(_cursor, "low24h");
          final int _cursorIndexOfPriceChange24h = CursorUtil.getColumnIndexOrThrow(_cursor, "priceChange24h");
          final int _cursorIndexOfPriceChangePercentage24h = CursorUtil.getColumnIndexOrThrow(_cursor, "priceChangePercentage24h");
          final int _cursorIndexOfCirculatingSupply = CursorUtil.getColumnIndexOrThrow(_cursor, "circulatingSupply");
          final int _cursorIndexOfTotalSupply = CursorUtil.getColumnIndexOrThrow(_cursor, "totalSupply");
          final int _cursorIndexOfMaxSupply = CursorUtil.getColumnIndexOrThrow(_cursor, "maxSupply");
          final int _cursorIndexOfAth = CursorUtil.getColumnIndexOrThrow(_cursor, "ath");
          final int _cursorIndexOfAthChangePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "athChangePercentage");
          final int _cursorIndexOfAthDate = CursorUtil.getColumnIndexOrThrow(_cursor, "athDate");
          final int _cursorIndexOfAtl = CursorUtil.getColumnIndexOrThrow(_cursor, "atl");
          final int _cursorIndexOfAtlChangePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "atlChangePercentage");
          final int _cursorIndexOfAtlDate = CursorUtil.getColumnIndexOrThrow(_cursor, "atlDate");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final List<CoinMarketEntity> _result = new ArrayList<CoinMarketEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CoinMarketEntity _item_1;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSymbol;
            _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpImage;
            _tmpImage = _cursor.getString(_cursorIndexOfImage);
            final double _tmpCurrentPrice;
            _tmpCurrentPrice = _cursor.getDouble(_cursorIndexOfCurrentPrice);
            final double _tmpMarketCap;
            _tmpMarketCap = _cursor.getDouble(_cursorIndexOfMarketCap);
            final int _tmpMarketCapRank;
            _tmpMarketCapRank = _cursor.getInt(_cursorIndexOfMarketCapRank);
            final double _tmpTotalVolume;
            _tmpTotalVolume = _cursor.getDouble(_cursorIndexOfTotalVolume);
            final double _tmpHigh24h;
            _tmpHigh24h = _cursor.getDouble(_cursorIndexOfHigh24h);
            final double _tmpLow24h;
            _tmpLow24h = _cursor.getDouble(_cursorIndexOfLow24h);
            final double _tmpPriceChange24h;
            _tmpPriceChange24h = _cursor.getDouble(_cursorIndexOfPriceChange24h);
            final double _tmpPriceChangePercentage24h;
            _tmpPriceChangePercentage24h = _cursor.getDouble(_cursorIndexOfPriceChangePercentage24h);
            final double _tmpCirculatingSupply;
            _tmpCirculatingSupply = _cursor.getDouble(_cursorIndexOfCirculatingSupply);
            final Double _tmpTotalSupply;
            if (_cursor.isNull(_cursorIndexOfTotalSupply)) {
              _tmpTotalSupply = null;
            } else {
              _tmpTotalSupply = _cursor.getDouble(_cursorIndexOfTotalSupply);
            }
            final Double _tmpMaxSupply;
            if (_cursor.isNull(_cursorIndexOfMaxSupply)) {
              _tmpMaxSupply = null;
            } else {
              _tmpMaxSupply = _cursor.getDouble(_cursorIndexOfMaxSupply);
            }
            final double _tmpAth;
            _tmpAth = _cursor.getDouble(_cursorIndexOfAth);
            final double _tmpAthChangePercentage;
            _tmpAthChangePercentage = _cursor.getDouble(_cursorIndexOfAthChangePercentage);
            final String _tmpAthDate;
            _tmpAthDate = _cursor.getString(_cursorIndexOfAthDate);
            final double _tmpAtl;
            _tmpAtl = _cursor.getDouble(_cursorIndexOfAtl);
            final double _tmpAtlChangePercentage;
            _tmpAtlChangePercentage = _cursor.getDouble(_cursorIndexOfAtlChangePercentage);
            final String _tmpAtlDate;
            _tmpAtlDate = _cursor.getString(_cursorIndexOfAtlDate);
            final long _tmpLastUpdated;
            _tmpLastUpdated = _cursor.getLong(_cursorIndexOfLastUpdated);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            _item_1 = new CoinMarketEntity(_tmpId,_tmpSymbol,_tmpName,_tmpImage,_tmpCurrentPrice,_tmpMarketCap,_tmpMarketCapRank,_tmpTotalVolume,_tmpHigh24h,_tmpLow24h,_tmpPriceChange24h,_tmpPriceChangePercentage24h,_tmpCirculatingSupply,_tmpTotalSupply,_tmpMaxSupply,_tmpAth,_tmpAthChangePercentage,_tmpAthDate,_tmpAtl,_tmpAtlChangePercentage,_tmpAtlDate,_tmpLastUpdated,_tmpCurrency);
            _result.add(_item_1);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLastUpdatedTime(final String currency,
      final Continuation<? super Long> $completion) {
    final String _sql = "SELECT MAX(lastUpdated) FROM coin_markets WHERE currency = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, currency);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getCount(final String currency, final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM coin_markets WHERE currency = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, currency);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
