package com.example.moneyminder

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MyDbHelper: SQLiteOpenHelper {
    companion object {
        const val DATABASE_NAME = "my_database"
        const val DATABASE_VERSION = 1

        public class Balance {
            companion object {
                const val TABLE_NAME = "balance"
                const val COLUMN_ID = "id"
                const val COLUMN_NAME = "name"
                const val COLUMN_PHONE = "phone"
            }
        }
        public class DetiledBalance {
            companion object {
                const val TABLE_NAME = "detailedbalances"
                const val COLUMN_ID = "id"
                const val BALANCE_ID = "balance_id"
                const val COLUMN_AMMOUNT = "ammount"
                const val COLUMN_DATE = "date"
                const val COLUMN_COMMENT = "comment"
                const val COLUMN_AMMOUNT_TYPE = "ammount_type"
            }
        }
        var CREATE_TABLE_BALANCE = "CREATE TABLE " + Balance.TABLE_NAME + "(" + Balance.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + Balance.COLUMN_NAME + " TEXT," + Balance.COLUMN_PHONE + " TEXT" + ")"
        var DROP_TABLE_BALANCE = "DROP TABLE IF EXISTS " + Balance.TABLE_NAME

        var CREATE_TABLE_DETAILEDBALANCE = "CREATE TABLE " + DetiledBalance.TABLE_NAME + "(" + DetiledBalance.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DetiledBalance.BALANCE_ID + " TEXT," + DetiledBalance.COLUMN_AMMOUNT + " TEXT," + DetiledBalance.COLUMN_DATE + " TEXT,"+ DetiledBalance.COLUMN_AMMOUNT_TYPE + " TEXT," + DetiledBalance.COLUMN_COMMENT + " TEXT" + ")"
        var DROP_TABLE_DETAILEDBALANCE = "DROP TABLE IF EXISTS " + DetiledBalance.TABLE_NAME
    }
    constructor(context: Context):super(context, DATABASE_NAME,null, DATABASE_VERSION)

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_BALANCE)
        db?.execSQL(CREATE_TABLE_DETAILEDBALANCE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(DROP_TABLE_BALANCE)
        db?.execSQL(DROP_TABLE_DETAILEDBALANCE)
        onCreate(db)
    }
    fun checknamenum(name:String,phone:String):Boolean{
        var db=this.readableDatabase
        var cursor=db.rawQuery("SELECT * FROM balance WHERE name=? AND phone=?", arrayOf(name,phone))
        if(cursor.count<=0){
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }
    fun insertbalance(name:String,phone:String):Boolean{
        var db=this.writableDatabase
        var values= ContentValues()
        values.put(Balance.COLUMN_NAME,name)
        values.put(Balance.COLUMN_PHONE,phone)
        var success=db.insert(Balance.TABLE_NAME,null,values)
        db.close()
        if(success>0){
            return true
        }
        return false
    }
    fun insertdetailedbalance(balance_id:String,ammount:String,date:String,comment:String,tpye:String): Long {
        var db=this.writableDatabase
        var values= ContentValues()
        values.put(DetiledBalance.BALANCE_ID,balance_id)
        values.put(DetiledBalance.COLUMN_AMMOUNT,ammount)
        values.put(DetiledBalance.COLUMN_DATE,date)
        values.put(DetiledBalance.COLUMN_COMMENT,comment)
        values.put(DetiledBalance.COLUMN_AMMOUNT_TYPE,tpye)
        var success=db.insert(DetiledBalance.TABLE_NAME,null,values)
        return success
    }

    fun deletebalance(id: String) {
        var db=this.writableDatabase
        db.delete(Balance.TABLE_NAME, Balance.COLUMN_ID + "=?", arrayOf(id))
        db.delete(DetiledBalance.TABLE_NAME, DetiledBalance.BALANCE_ID + "=?", arrayOf(id))
        db.close()

    }

    @SuppressLint("Range")
    fun selectDetailedBalance(id: String): ArrayList<DetailBalance> {
        var ls=ArrayList<DetailBalance>()
        var db=this.readableDatabase
        var cursor =db.query(DetiledBalance.TABLE_NAME,null, DetiledBalance.BALANCE_ID + "=?", arrayOf(id),null,null,null)
//        var cursor=db.rawQuery("SELECT * FROM detailedbalances WHERE balance_id=?", arrayOf(id))
        if(cursor.moveToFirst()){
            do{
                var id=cursor.getString(cursor.getColumnIndex(DetiledBalance.COLUMN_ID))
                var ammount=cursor.getString(cursor.getColumnIndex(DetiledBalance.COLUMN_AMMOUNT))
                var date=cursor.getString(cursor.getColumnIndex(DetiledBalance.COLUMN_DATE))
                var comment=cursor.getString(cursor.getColumnIndex(DetiledBalance.COLUMN_COMMENT))
                var type=cursor.getString(cursor.getColumnIndex(DetiledBalance.COLUMN_AMMOUNT_TYPE))
                Log.e("type in select data",type)
                ls.add(DetailBalance(id,ammount,date,type,comment))
                Log.e("ls",ls[0].ammount)
            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return ls
    }
    fun sumofbalanceType(id: String,type:String): Int {
        var db=this.readableDatabase
        var cursor=db.rawQuery("SELECT SUM(ammount) FROM detailedbalances WHERE balance_id=? AND ammount_type=?", arrayOf(id,type))
        if(cursor.moveToFirst()){
            var sum=cursor.getInt(0)
            cursor.close()
            db.close()
            return sum
        }
        cursor.close()
        db.close()
        return 0
    }
    fun sumofbalance(id: String): Int {
        return sumofbalanceType(id,"1")-sumofbalanceType(id,"0")
    }

    fun deleteDetailBalance(id: String) {
        var db=this.writableDatabase
        db.delete(DetiledBalance.TABLE_NAME, DetiledBalance.COLUMN_ID + "=?", arrayOf(id))
        db.close()
    }
}