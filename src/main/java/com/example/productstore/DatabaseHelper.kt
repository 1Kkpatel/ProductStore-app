package com.example.productstore
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "app.db"
        private const val DATABASE_VERSION = 1

        // Users Table
        private const val TABLE_USERS = "users"
        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"

        // Items Table
        private const val TABLE_ITEMS = "items"
        private const val COLUMN_ITEM_ID = "id"
        private const val COLUMN_IMAGE_URI = "imageUri"
        private const val COLUMN_TEXT1 = "text1"
        private const val COLUMN_TEXT2 = "text2"
        private const val COLUMN_TEXT3 = "text3"

        private const val CREATE_TABLE_USERS = "CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_PASSWORD TEXT)"

        private const val CREATE_TABLE_ITEMS = "CREATE TABLE $TABLE_ITEMS (" +
                "$COLUMN_ITEM_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_IMAGE_URI TEXT, " +
                "$COLUMN_TEXT1 TEXT, " +
                "$COLUMN_TEXT2 TEXT, " +
                "$COLUMN_TEXT3 TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_USERS)
        db.execSQL(CREATE_TABLE_ITEMS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ITEMS")
        onCreate(db)
    }

    fun insertUser(username: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        val result = db.insert(TABLE_USERS, null, values)
        return result != -1L
    }

    fun authenticateUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_USER_ID), "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?", arrayOf(username, password), null, null, null)
        val count = cursor.count
        cursor.close()
        return count > 0
    }

    fun insertItem(item: Item): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IMAGE_URI, item.imageUri)
            put(COLUMN_TEXT1, item.text1)
            put(COLUMN_TEXT2, item.text2)
            put(COLUMN_TEXT3, item.text3)
        }
        return db.insert(TABLE_ITEMS, null, values)
    }

    fun getAllItems(): List<Item> {
        val items = mutableListOf<Item>()
        val db = readableDatabase
        val cursor = db.query(TABLE_ITEMS, null, null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ITEM_ID))
                val imageUri = getString(getColumnIndexOrThrow(COLUMN_IMAGE_URI))
                val text1 = getString(getColumnIndexOrThrow(COLUMN_TEXT1))
                val text2 = getString(getColumnIndexOrThrow(COLUMN_TEXT2))
                val text3 = getString(getColumnIndexOrThrow(COLUMN_TEXT3))
                items.add(Item(id, imageUri, text1, text2, text3))
            }
            close()
        }
        return items
    }

    fun updateItem(item: Item) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IMAGE_URI, item.imageUri)
            put(COLUMN_TEXT1, item.text1)
            put(COLUMN_TEXT2, item.text2)
            put(COLUMN_TEXT3, item.text3)
        }
        db.update(TABLE_ITEMS, values, "$COLUMN_ITEM_ID = ?", arrayOf(item.id.toString()))
    }
}
