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

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_CatInstitucion);
        db.execSQL(Create_CatMunicipios);
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

    /************************************************** CatMunicipio ********************************************************************/
    public void insertCatMunicipios(int idEntidadFederativa,int idMunicipio,String municipio){
        SQLiteDatabase dbSqLiteDatabase = this.getWritableDatabase();
        dbSqLiteDatabase.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("IdEntidadFederativa",idEntidadFederativa);
            values.put("IdMunicipio",idMunicipio);
            values.put("Municipio",municipio);

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
    public ArrayList<String> getAllMunicipios(){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_CatMunicipios;
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
}
