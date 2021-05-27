package mx.ssp.iph.SqLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataHelper extends SQLiteOpenHelper {
    public static final String DataBase_Name = "IPH";
    public static final int Database_Version = 2 ;
    public static final String Table_CatInstitucion = "CatInstitucion";
    public static final String Create_CatInstitucion = "CREATE TABLE IF NOT EXISTS " + Table_CatInstitucion +"(IdInstitucion INTEGER PRIMARY KEY, Institucion TEXT NOT NULL UNIQUE)";
    public static final String Table_CatMunicipios = "CatMunicipios";
    public static final String Create_CatMunicipios = "CREATE TABLE IF NOT EXISTS " + Table_CatMunicipios +"(IdEntidadFederativa INTEGER, IdMunicipio INTEGER PRIMARY KEY, Municipio TEXT NOT NULL UNIQUE)";
    public static final String Table_CatCargo = "CatCargo";
    public static final String Create_CatCargo = "CREATE TABLE IF NOT EXISTS " + Table_CatCargo +"(IdCargo INTEGER PRIMARY KEY, Cargo TEXT NOT NULL UNIQUE)";
    public static final String Table_CatAutoridadAdmin = "CatAutoridadAdmin";
    public static final String Create_CatAutoridadAdmin = "CREATE TABLE IF NOT EXISTS " + Table_CatAutoridadAdmin +"(IdAutoridadAdmin INTEGER PRIMARY KEY, AutoridadAdmin TEXT NOT NULL UNIQUE)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_CatInstitucion);
        db.execSQL(Create_CatMunicipios);
        db.execSQL(Create_CatCargo);
        db.execSQL(Create_CatAutoridadAdmin);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public DataHelper(Context context){
        super(context,DataBase_Name,null,Database_Version);
    }

    /************************************************** CatInstitucion ********************************************************************/
    public void insertCatInstitucion(int idInstitucion,String institucion){
        SQLiteDatabase dbSqLiteDatabase = this.getWritableDatabase();
        dbSqLiteDatabase.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("IdInstitucion",idInstitucion);
            values.put("Institucion",institucion);
            dbSqLiteDatabase.insert(Table_CatInstitucion,null,values);
            dbSqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            dbSqLiteDatabase.endTransaction();
            dbSqLiteDatabase.close();
        }
    }
    public ArrayList<String> getAllInstitucion(){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_CatInstitucion;
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatInstitucion;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String institucion = cursor.getString(cursor.getColumnIndex("Institucion"));
                    list.add(institucion);
                }
            }
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return  list;
    }

    public int getIdInstitucion(String descInstitucion ){
        int idInstitucion = 0;
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatInstitucion;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);

            String selectQuery = "SELECT IdMunicipio FROM " + Table_CatInstitucion +" WHERE Municipio = '"+descInstitucion+"'";
            Cursor mCount2= dbDatabase.rawQuery(selectQuery, null);
            mCount2.moveToFirst();
            int count2= mCount2.getInt(0);
            idInstitucion = count2;
            System.out.println(count2);
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return idInstitucion;
    }

    /************************************************** CatMunicipio ********************************************************************/
    public void insertCatMunicipios(int idEntidadFederativa, int idMunicipio, String municipio){
        SQLiteDatabase dbSqLiteDatabase = this.getWritableDatabase();
        dbSqLiteDatabase.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("IdEntidadFederativa",idEntidadFederativa);
            values.put("IdMunicipio",idMunicipio);
            values.put("Municipio", municipio);
            dbSqLiteDatabase.insert(Table_CatMunicipios,null,values);
            dbSqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            dbSqLiteDatabase.endTransaction();
            dbSqLiteDatabase.close();
        }
    }
    public ArrayList<String> getAllMunicipios(){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_CatMunicipios;
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatMunicipios;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String municipio = cursor.getString(cursor.getColumnIndex("Municipio"));
                    list.add(municipio);
                }
            }
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return  list;
    }

    public int getIdMunicipio(String descMunicipio ){
        int idMunicipio = 0;
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatMunicipios;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);

            String selectQuery = "SELECT IdMunicipio FROM " + Table_CatMunicipios +" WHERE Municipio = '"+descMunicipio+"'";
            Cursor mCount2= dbDatabase.rawQuery(selectQuery, null);
            mCount2.moveToFirst();
            int count2= mCount2.getInt(0);
            idMunicipio = count2;
            System.out.println(count2);
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return idMunicipio;
    }
    /************************************************** CatCargo********************************************************************/
    public void insertCatCargo(int idCargo, String cargo){
        SQLiteDatabase dbSqLiteDatabase = this.getWritableDatabase();
        dbSqLiteDatabase.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("IdCargo",idCargo);
            values.put("Cargo", cargo);
            dbSqLiteDatabase.insert(Table_CatCargo,null,values);
            dbSqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            dbSqLiteDatabase.endTransaction();
            dbSqLiteDatabase.close();
        }
    }
    public ArrayList<String> getAllCargos(){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_CatCargo;
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatCargo;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String cargo = cursor.getString(cursor.getColumnIndex("Cargo"));
                    list.add(cargo);
                }
            }
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return  list;
    }

    public int getIdCargo(String descCArgo ){
        int idCargo = 0;
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatCargo;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);

            String selectQuery = "SELECT IdCargo FROM " + Table_CatCargo +" WHERE Municipio = '"+descCArgo+"'";
            Cursor mCount2= dbDatabase.rawQuery(selectQuery, null);
            mCount2.moveToFirst();
            int count2= mCount2.getInt(0);
            idCargo = count2;
            System.out.println(count2);
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return idCargo;
    }

    /************************************************** CatInstitucion ********************************************************************/
    public void insertCatAutoridadAdmin(int idAutoridadAdmin,String autoridadAdmin){
        SQLiteDatabase dbSqLiteDatabase = this.getWritableDatabase();
        dbSqLiteDatabase.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("IdAutoridadAdmin",idAutoridadAdmin);
            values.put("AutoridadAdmin",autoridadAdmin);
            dbSqLiteDatabase.insert(Table_CatAutoridadAdmin,null,values);
            dbSqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            dbSqLiteDatabase.endTransaction();
            dbSqLiteDatabase.close();
        }
    }
    public ArrayList<String> getAllAutoridadAdmin(){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_CatAutoridadAdmin;
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatAutoridadAdmin;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String institucion = cursor.getString(cursor.getColumnIndex("AutoridadAdmin"));
                    list.add(institucion);
                }
            }
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return  list;
    }

    public int getIdAutoridadAdmin(String descAutoridadAdmin){
        int idAutoridadAdmin = 0;
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatAutoridadAdmin;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);

            String selectQuery = "SELECT IdMunicipio FROM " + Table_CatAutoridadAdmin +" WHERE Municipio = '"+descAutoridadAdmin+"'";
            Cursor mCount2= dbDatabase.rawQuery(selectQuery, null);
            mCount2.moveToFirst();
            int count2= mCount2.getInt(0);
            idAutoridadAdmin = count2;
            System.out.println(count2);
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return idAutoridadAdmin;
    }
}
