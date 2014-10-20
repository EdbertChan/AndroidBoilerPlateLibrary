package com.edbert.library.database;

public interface DatabaseHelperInterface{
	public  String getTableName();
	public  String getColumnID();
	public String getCreateTableCommand();
	public String[] getColumns();
}