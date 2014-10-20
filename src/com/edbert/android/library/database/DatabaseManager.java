package com.edbert.android.library.database;

/**
 * This class is used to remeber all the DBHelpers
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.edbert.library.database.DatabaseHelperInterface;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class DatabaseManager {

	private static List<DatabaseHelperInterface> recipients = new ArrayList<DatabaseHelperInterface>();

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	// on get instance, we should populate
	public static final String SQL_INSERT_OR_REPLACE = "__sql_insert_or_replace__";

	private static DatabaseManager instance;

	public DatabaseManager getInstance(Context c, String authority) {
		if (instance == null)
			instance = new DatabaseManager(c, authority);

		return instance;
	}

	private String authority;

	private Context context;

	public DatabaseManager(Context context, String authority) {
		for (int i = 0; i < recipients.size(); i++) {

			String tableName = recipients.get(i).getTableName();
			int identifier = hashStringToInt(tableName);

			sURIMatcher.addURI(authority, tableName, identifier);

			int id_uri = idToURIID(i);

			sURIMatcher.addURI(authority, tableName + "/#", id_uri);

		}
		this.authority = authority;
		this.context = context;
	}

	private int hashStringToInt(String s) {
		int hash = 7;
		for (int i = 0; i < s.length(); i++) {
			hash = hash * 31 + s.charAt(i);
		}
		return Math.abs(hash);
	}

	private int idToURIID(int i) {
		return i + 1;
	}

	public static void deleteAllTables(SQLiteDatabase db) {
		for (int i = 0; i < recipients.size(); i++) {
			String createTableCommand = recipients.get(i).getTableName();
			db.execSQL("DROP TABLE IF EXISTS '" + createTableCommand + "'");
		}
	}

	public static void createAllTables(SQLiteDatabase db) {
		for (int i = 0; i < recipients.size(); i++) {
			String createTableCommand = recipients.get(i)
					.getCreateTableCommand();
			createTableCommand = createTableCommand.replace("CREATE TABLE",
					"CREATE TABLE IF NOT EXISTS");

			db.execSQL(createTableCommand);
		}
	}

	public SQLiteQueryBuilder queryAllTables(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		// we have the URIType. Now we have to conver it back into the value we
		// want
		for (int i = 0; i < recipients.size(); i++) {
			int tableHash = hashStringToInt(recipients.get(i).getTableName());
			if (uriType == tableHash) {
				// success for table name
				queryBuilder.setTables(recipients.get(i).getTableName());
				return queryBuilder;
			}

			// now try for the ID?
			int tableIDHash = idToURIID(tableHash);

			if (uriType == tableIDHash) {
				queryBuilder.setTables(recipients.get(i).getTableName());

				queryBuilder.appendWhere(recipients.get(i).getColumnID() + "="
						+ uri.getLastPathSegment());
				return queryBuilder;
			}
		}
		throw new IllegalArgumentException("Unknown URI: " + uri);
	}

	public static boolean isEmpty() {
		return recipients.isEmpty();
	}

	public static void register(DatabaseHelperInterface stratFact) {
		recipients.add(stratFact);
	}

	public static int numOfEntries() {
		return recipients.size();
	}

	public long bulkInsert(Uri uri, ContentValues[] values, SQLiteDatabase sqlDB) {
		int uriType = sURIMatcher.match(uri);
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		// we have the URIType. Now we have to conver it back into the value we
		// want
		
		int numInserted = 0;
		for (int i = 0; i < recipients.size(); i++) {
			String tableName = recipients.get(i).getTableName();
			int tableHash = hashStringToInt(tableName);
			if (uriType == tableHash) {
				// we have the tableName. Now we bulk insert
				sqlDB.beginTransaction();
				try {
					for (ContentValues cv : values) {
						boolean replaceFlag = false;
						if (cv.containsKey(SQL_INSERT_OR_REPLACE)) {
							replaceFlag = true;
							cv.remove(SQL_INSERT_OR_REPLACE);
						}
						long newID = 0;
						if (replaceFlag) {
							newID = sqlDB.replaceOrThrow(tableName, null, cv);
						} else {
							newID = sqlDB.insertOrThrow(tableName, null, cv);
						}

						if (newID <= 0) {
							throw new SQLException("Failed to insert row into "
									+ uri);
						}
					}
					sqlDB.setTransactionSuccessful();
					numInserted = values.length;
				} finally {
					sqlDB.endTransaction();
				}
				return numInserted;
				// success for table name
			
			}
		}
		throw new IllegalArgumentException("Unknown URI: " + uri);

	}

	public long insert(Uri uri, ContentValues values, SQLiteDatabase sqlDB) {
		int uriType = sURIMatcher.match(uri);
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		// we have the URIType. Now we have to conver it back into the value we
		// want
		long id = 0;
		Uri _uri = null;
		for (int i = 0; i < recipients.size(); i++) {
			String tableName = recipients.get(i).getTableName();
			int tableHash = hashStringToInt(tableName);
			if (uriType == tableHash) {

				// success for table name
				long _ID1 = 0;
				boolean replace = false;
				if (values.containsKey(SQL_INSERT_OR_REPLACE)) {
					replace = values.getAsBoolean(SQL_INSERT_OR_REPLACE);

					// Clone the values object, so we don't modify the original.
					// This is not strictly necessary, but depends on your needs
					values = new ContentValues(values);

					// Remove the key, so we don't pass that on to db.insert()
					// or db.replace()
					values.remove(SQL_INSERT_OR_REPLACE);
				}

				if (replace) {
					_ID1 = sqlDB.replace(tableName, "", values);
				} else {
					_ID1 = sqlDB.insert(tableName, "", values);
				}

				// ---if added successfully---
				if (_ID1 > 0) {
					_uri = ContentUris.withAppendedId(
							contentURIbyTableName(tableName), _ID1);
					context.getContentResolver().notifyChange(_uri, null);
				}
				id = _ID1;
				return id;
			}
		}
		throw new IllegalArgumentException("Unknown URI: " + uri);

	}

	private Uri contentURIbyTableName(String tableName) {
		return Uri.parse("content://" + authority + "/"
				+ tableName);
	}

	public int delete(Uri uri, String selection, String[] selectionArgs,
			SQLiteDatabase sqlDB) {
		int uriType = sURIMatcher.match(uri);

		int rowsDeleted = 0;

		for (int i = 0; i < recipients.size(); i++) {
			int tableHash = hashStringToInt(recipients.get(i).getTableName());
			if (uriType == tableHash) {
				// success for table name
				rowsDeleted = sqlDB.delete(recipients.get(i).getTableName(), selection,
						selectionArgs);
				return rowsDeleted;
			}

		}
		throw new IllegalArgumentException("Unknown URI: " + uri);
	}

	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs, SQLiteDatabase sqlDB) {
		int uriType = sURIMatcher.match(uri);

		int rowsUpdated = 0;

		for (int i = 0; i < recipients.size(); i++) {
			String tableName = recipients.get(i).getTableName();
			int tableHash = hashStringToInt(recipients.get(i).getTableName());
			if (uriType == tableHash) {
				// success for table name

				// we shoudl replace if the id has

				rowsUpdated = sqlDB.update(tableName, values, selection,
						selectionArgs);
				return rowsUpdated;
			}
		}
		throw new IllegalArgumentException("Unknown URI: " + uri);
	}

	public String[] checkColumns(Uri uri) {

		int uriType = sURIMatcher.match(uri);

		for (int i = 0; i < recipients.size(); i++) {
			int tableHash = hashStringToInt(recipients.get(i).getTableName());
			if (uriType == tableHash) {
				return recipients.get(i).getColumns();
			}
		}
		return new String[] {};
	}

	public static void applyCommandtoAllTables(SQLiteDatabase database,
			String infront, String tail) {
		for (int i = 0; i < recipients.size(); i++) {
			String tableName = recipients.get(i).getTableName();
			database.execSQL(infront + tableName + tail);
		}
	}

	public static void upgradeAll(SQLiteDatabase db) {
		for (int i = 0; i < recipients.size(); i++) {

			String TABLE_NAME = recipients.get(i).getTableName();
			try {

                List<String> columns = GetColumns(db, TABLE_NAME);                              
                db.execSQL("ALTER table " + TABLE_NAME + " RENAME TO 'temp_" + TABLE_NAME + "'");
                String createTableCommand = recipients.get(i)
    					.getCreateTableCommand();
    			createTableCommand = createTableCommand.replace("CREATE TABLE",
    					"CREATE TABLE IF NOT EXISTS");
             
                
                columns.retainAll(GetColumns(db, TABLE_NAME));
                String cols = join(columns, ",");
                db.execSQL(String.format( "INSERT INTO %s (%s) SELECT %s from temp_%s", TABLE_NAME, cols, cols, TABLE_NAME));
                db.execSQL("DROP table 'temp_" + TABLE_NAME + "'");
               
                return;
                
        } catch (SQLException e) {
                // if anything goes wrong, just start over
                e.printStackTrace();
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
               
                String createTableCommand = recipients.get(i)
    					.getCreateTableCommand();
    			createTableCommand = createTableCommand.replace("CREATE TABLE",
    					"CREATE TABLE IF NOT EXISTS");
    			db.execSQL(createTableCommand);
        }
			///
			/*
			String tableName = recipients.get(i).getTableName();
			String temp_tableName = "temp_"+tableName;
			db.execSQL("ALTER TABLE " + tableName + " RENAME TO " + temp_tableName+";");
			
			//want to get all the old table's columns
			String[] oldTableColumns= getColumns(db, "tableName");
		
			String createTableCommand = recipients.get(i)
					.getCreateTableCommand();
			createTableCommand = createTableCommand.replace("CREATE TABLE",
					"CREATE TABLE IF NOT EXISTS");
			db.execSQL(createTableCommand);
			
			 db.execSQL(String.format( "INSERT INTO %s (%s) SELECT %s from temp_%s", tableName, oldTableColumns, oldTableColumns, tableName));
			
			 
            db.execSQL("DROP table 'temp_" + tableName);*/
            
       
		}
	}
	public static List<String> GetColumns(SQLiteDatabase db, String tableName) {
	    List<String> ar = null;
	    Cursor c = null;
	    try {
	        c = db.rawQuery("select * from " + tableName + " limit 1", null);
	        if (c != null) {
	            ar = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
	        }
	    } catch (Exception e) {
	        Log.v(tableName, e.getMessage(), e);
	        e.printStackTrace();
	    } finally {
	        if (c != null)
	            c.close();
	    }
	    return ar;
	}

	public static String join(List<String> list, String delim) {
	    StringBuilder buf = new StringBuilder();
	    int num = list.size();
	    for (int i = 0; i < num; i++) {
	        if (i != 0)
	            buf.append(delim);
	        buf.append((String) list.get(i));
	    }
	    return buf.toString();
	}
}