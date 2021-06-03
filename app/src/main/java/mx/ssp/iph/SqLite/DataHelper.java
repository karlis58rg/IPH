package mx.ssp.iph.SqLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataHelper extends SQLiteOpenHelper {
    public static final String DataBase_Name = "IPH";
    public static final int Database_Version = 12 ;

    public static final String Table_CatAutoridadAdmin = "CatAutoridadAdmin";
    public static final String Create_CatAutoridadAdmin = "CREATE TABLE IF NOT EXISTS " + Table_CatAutoridadAdmin +"(IdAutoridadAdmin INTEGER PRIMARY KEY, AutoridadAdmin TEXT NOT NULL UNIQUE)";

    public static final String Table_CatCargo = "CatCargo";
    public static final String Create_CatCargo = "CREATE TABLE IF NOT EXISTS " + Table_CatCargo +"(IdCargo INTEGER PRIMARY KEY, Cargo TEXT NOT NULL UNIQUE)";

    public static final String Table_CatConocimientoInfraccion = "CatConocimientoInfraccion";
    public static final String Create_CatConocimientoInfraccion = "CREATE TABLE IF NOT EXISTS " + Table_CatConocimientoInfraccion +"(IdConocimiento INTEGER PRIMARY KEY, Conocimiento TEXT NOT NULL UNIQUE)";

    public static final String Table_CatFiscaliaAutoridad = "CatFiscaliaAutoridad";
    public static final String Create_CatFiscaliaAutoridad = "CREATE TABLE IF NOT EXISTS " + Table_CatFiscaliaAutoridad +"(IdFiscaliaAutoridad INTEGER PRIMARY KEY, FiscaliaAutoridad TEXT NOT NULL UNIQUE)";

    public static final String Table_CatInstitucion = "CatInstitucion";
    public static final String Create_CatInstitucion = "CREATE TABLE IF NOT EXISTS " + Table_CatInstitucion +"(IdInstitucion INTEGER PRIMARY KEY, Institucion TEXT NOT NULL UNIQUE)";

    public static final String Table_CatLugarTraslado = "CatLugarTraslado";
    public static final String Create_CatLugarTraslado = "CREATE TABLE IF NOT EXISTS " + Table_CatLugarTraslado +"(IdLugarTraslado INTEGER PRIMARY KEY, LugarTraslado TEXT NOT NULL UNIQUE, Descripcion TEXT NOT NULL UNIQUE)";

    public static final String Table_CatMunicipios = "CatMunicipios";
    public static final String Create_CatMunicipios = "CREATE TABLE IF NOT EXISTS " + Table_CatMunicipios +"(IdEntidadFederativa INTEGER, IdMunicipio INTEGER PRIMARY KEY, Municipio TEXT NOT NULL UNIQUE)";

    public static final String Table_CatNacionalidad = "CatNacionalidad";
    public static final String Create_CatNacionalidad = "CREATE TABLE IF NOT EXISTS " + Table_CatNacionalidad +"(IdNacionalida INTEGER PRIMARY KEY, Nacionalida TEXT NOT NULL UNIQUE, DesNacionalidad TEXT NOT NULL UNIQUE)";

    public static final String Table_CatSexo = "CatSexo";
    public static final String Create_CatSexo = "CREATE TABLE IF NOT EXISTS " + Table_CatSexo +"(IdSexo INTEGER PRIMARY KEY, Sexo TEXT NOT NULL UNIQUE)";

    public static final String Table_CatUnidad = "CatUnidad";
    public static final String Create_CatUnidad = "CREATE TABLE IF NOT EXISTS " + Table_CatUnidad +"(IdUnidad TEXT PRIMARY KEY, Unidad TEXT NOT NULL, IdMarca TEXT NOT NULL, IdSubMarca INTEGER NOT NULL, Modelo INTEGER NOT NULL, Descripcion TEXT NOT NULL, IdInstitucion INTEGER NOT NULL)";

    public static final String Table_CatMarcaVehiculos = "CatMarcaVehiculos";
    public static final String Create_CatMarcaVehiculos = "CREATE TABLE IF NOT EXISTS " + Table_CatMarcaVehiculos +"(IdMarca TEXT PRIMARY KEY, Marca TEXT NOT NULL UNIQUE)";

    public static final String Table_CatSubMarcaVehiculos = "CatSubMarcaVehiculos";
    public static final String Create_CatSubMarcaVehiculos = "CREATE TABLE IF NOT EXISTS " + Table_CatSubMarcaVehiculos +"(IdMarca TEXT NOT NULL, IdSubMarca INTEGER NOT NULL ,SubMarca TEXT NOT NULL, PRIMARY KEY (IdMarca, IdSubMarca))";


    //public static final String Delete_CatSubMarcaVehiculos = "DROP TABLE IF EXISTS "+ Table_CatSubMarcaVehiculos;
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_CatAutoridadAdmin);
        db.execSQL(Create_CatCargo);
        db.execSQL(Create_CatConocimientoInfraccion);
        db.execSQL(Create_CatFiscaliaAutoridad);
        db.execSQL(Create_CatInstitucion);
        db.execSQL(Create_CatLugarTraslado);
        db.execSQL(Create_CatMunicipios);
        db.execSQL(Create_CatNacionalidad);
        db.execSQL(Create_CatSexo);
        db.execSQL(Create_CatUnidad);
        db.execSQL(Create_CatMarcaVehiculos);
        db.execSQL(Create_CatSubMarcaVehiculos);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(Delete_CatSubMarcaVehiculos);
        onCreate(db);
    }

    public DataHelper(Context context){
        super(context,DataBase_Name,null,Database_Version);
    }

    /************************************************** CatAutoridadAdmin ********************************************************************/
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
                    String autoridadAdmin = cursor.getString(cursor.getColumnIndex("AutoridadAdmin"));
                    list.add(autoridadAdmin);
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

            String selectQuery = "SELECT IdAutoridadAdmin FROM " + Table_CatAutoridadAdmin +" WHERE AutoridadAdmin = '"+descAutoridadAdmin+"'";
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
    public int getIdCargo(String descCargo ){
        int idCargo = 0;
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatCargo;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);

            String selectQuery = "SELECT IdCargo FROM " + Table_CatCargo +" WHERE Cargo = '"+descCargo+"'";
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

    /************************************************** CatConocimientoInfraccion ********************************************************************/
    public void insertCatConocimientoInfraccion(int idConocimiento,String conocimiento){
        SQLiteDatabase dbSqLiteDatabase = this.getWritableDatabase();
        dbSqLiteDatabase.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("IdConocimiento",idConocimiento);
            values.put("Conocimiento",conocimiento);
            dbSqLiteDatabase.insert(Table_CatConocimientoInfraccion,null,values);
            dbSqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            dbSqLiteDatabase.endTransaction();
            dbSqLiteDatabase.close();
        }
    }
    public ArrayList<String> getAllConocimientoInfraccion(){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_CatConocimientoInfraccion;
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatConocimientoInfraccion;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String conocimiento = cursor.getString(cursor.getColumnIndex("Conocimiento"));
                    list.add(conocimiento);
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
    public int getIdConocimientoInfraccion(String descConocimiento){
        int idConocimiento = 0;
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatConocimientoInfraccion;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);

            String selectQuery = "SELECT IdConocimiento FROM " + Table_CatConocimientoInfraccion +" WHERE Conocimiento = '"+descConocimiento+"'";
            Cursor mCount2= dbDatabase.rawQuery(selectQuery, null);
            mCount2.moveToFirst();
            int count2= mCount2.getInt(0);
            idConocimiento = count2;
            System.out.println(count2);
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return idConocimiento;
    }

    /************************************************** CatFiscaliaAutoridad********************************************************************/
    public void insertCatFiscaliaAutoridad(int idFiscaliaAutoridad, String fiscaliaAutoridad){
        SQLiteDatabase dbSqLiteDatabase = this.getWritableDatabase();
        dbSqLiteDatabase.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("IdFiscaliaAutoridad",idFiscaliaAutoridad);
            values.put("FiscaliaAutoridad", fiscaliaAutoridad);
            dbSqLiteDatabase.insert(Table_CatFiscaliaAutoridad,null,values);
            dbSqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            dbSqLiteDatabase.endTransaction();
            dbSqLiteDatabase.close();
        }
    }
    public ArrayList<String> getAllFiscaliaAutoridad(){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_CatFiscaliaAutoridad;
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatFiscaliaAutoridad;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String fiscaliaAutoridad = cursor.getString(cursor.getColumnIndex("FiscaliaAutoridad"));
                    list.add(fiscaliaAutoridad);
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
    public int getIdFiscaliaAutoridad(String descFiscaliaAutoridad ){
        int idFiscaliaAutoridad = 0;
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatFiscaliaAutoridad;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);

            String selectQuery = "SELECT IdFiscaliaAutoridad FROM " + Table_CatFiscaliaAutoridad +" WHERE FiscaliaAutoridad = '"+descFiscaliaAutoridad+"'";
            Cursor mCount2= dbDatabase.rawQuery(selectQuery, null);
            mCount2.moveToFirst();
            int count2= mCount2.getInt(0);
            idFiscaliaAutoridad = count2;
            System.out.println(count2);
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return idFiscaliaAutoridad;
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

            String selectQuery = "SELECT IdInstitucion FROM " + Table_CatInstitucion +" WHERE Institucion = '"+descInstitucion+"'";
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

    /************************************************** CatLugarTraslado ********************************************************************/
    public void insertCatLugarTraslado(int idLugarTraslado, String lugarTraslado, String descripcion){
        SQLiteDatabase dbSqLiteDatabase = this.getWritableDatabase();
        dbSqLiteDatabase.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("IdLugarTraslado",idLugarTraslado);
            values.put("LugarTraslado",lugarTraslado);
            values.put("Descripcion", descripcion);
            dbSqLiteDatabase.insert(Table_CatLugarTraslado,null,values);
            dbSqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            dbSqLiteDatabase.endTransaction();
            dbSqLiteDatabase.close();
        }
    }
    public ArrayList<String> getAllLugarTraslado(){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_CatLugarTraslado;
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatLugarTraslado;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String descripcion = cursor.getString(cursor.getColumnIndex("Descripcion"));
                    list.add(descripcion);
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
    public int getIdLugarTraslado(String descDescripcion ){
        int idLugarTraslado = 0;
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatLugarTraslado;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);

            String selectQuery = "SELECT IdLugarTraslado FROM " + Table_CatLugarTraslado +" WHERE Descripcion = '"+descDescripcion+"'";
            Cursor mCount2= dbDatabase.rawQuery(selectQuery, null);
            mCount2.moveToFirst();
            int count2= mCount2.getInt(0);
            idLugarTraslado = count2;
            System.out.println(count2);
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return idLugarTraslado;
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

    /************************************************** CatNacionalidad ********************************************************************/
    public void insertCatNacionalidad(int idNacionalida, String nacionalida, String desNacionalidad){
        SQLiteDatabase dbSqLiteDatabase = this.getWritableDatabase();
        dbSqLiteDatabase.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("IdNacionalida",idNacionalida);
            values.put("Nacionalida",nacionalida);
            values.put("DesNacionalidad", desNacionalidad);
            dbSqLiteDatabase.insert(Table_CatNacionalidad,null,values);
            dbSqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            dbSqLiteDatabase.endTransaction();
            dbSqLiteDatabase.close();
        }
    }
    public ArrayList<String> getAllNacionalidad(){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_CatNacionalidad;
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatNacionalidad;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String desNacionalidad = cursor.getString(cursor.getColumnIndex("DesNacionalidad"));
                    list.add(desNacionalidad);
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
    public int getIdNacionalidad(String desNacionalidad ){
        int idNacionalida = 0;
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatNacionalidad;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);

            String selectQuery = "SELECT IdNacionalida FROM " + Table_CatNacionalidad +" WHERE DesNacionalidad = '"+desNacionalidad+"'";
            Cursor mCount2= dbDatabase.rawQuery(selectQuery, null);
            mCount2.moveToFirst();
            int count2= mCount2.getInt(0);
            idNacionalida = count2;
            System.out.println(count2);
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return idNacionalida;
    }

    /************************************************** CatSexo ********************************************************************/
    public void insertCatSexo(int idSexo,String sexo){
        SQLiteDatabase dbSqLiteDatabase = this.getWritableDatabase();
        dbSqLiteDatabase.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("IdSexo",idSexo);
            values.put("Sexo",sexo);
            dbSqLiteDatabase.insert(Table_CatSexo,null,values);
            dbSqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            dbSqLiteDatabase.endTransaction();
            dbSqLiteDatabase.close();
        }
    }
    public ArrayList<String> getAllSexo(){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_CatSexo;
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatSexo;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String sexo = cursor.getString(cursor.getColumnIndex("Sexo"));
                    list.add(sexo);
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
    public int getIdSexo(String descSexo){
        int idSexo = 0;
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatSexo;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);

            String selectQuery = "SELECT IdSexo FROM " + Table_CatSexo +" WHERE Sexo = '"+descSexo+"'";
            Cursor mCount2= dbDatabase.rawQuery(selectQuery, null);
            mCount2.moveToFirst();
            int count2= mCount2.getInt(0);
            idSexo = count2;
            System.out.println(count2);
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return idSexo;
    }

    /************************************************** CatUnidad ********************************************************************/
    public void insertCatUnidad(String idUnidad,String unidad,String idMarca,int idSubMarca,int modelo,String descripcionU,int idInstitucionU){
        SQLiteDatabase dbSqLiteDatabase = this.getWritableDatabase();
        dbSqLiteDatabase.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("IdUnidad",idUnidad);
            values.put("Unidad",unidad);
            values.put("IdMarca",idMarca);
            values.put("IdSubMarca",idSubMarca);
            values.put("Modelo",modelo);
            values.put("Descripcion",descripcionU);
            values.put("IdInstitucion",idInstitucionU);
            dbSqLiteDatabase.insert(Table_CatUnidad,null,values);
            dbSqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            dbSqLiteDatabase.endTransaction();
            dbSqLiteDatabase.close();
        }
    }
    public ArrayList<String> getAllUnidad(){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_CatUnidad;
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatUnidad;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String descripcion = cursor.getString(cursor.getColumnIndex("Descripcion"));
                    list.add(descripcion);
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
    public String getIdUnidad(String descDescripcion){
        String idUnidad = "";
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatUnidad;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);

            String selectQuery = "SELECT IdUnidad FROM " + Table_CatUnidad +" WHERE Descripcion = '"+descDescripcion+"'";
            Cursor mCount2= dbDatabase.rawQuery(selectQuery, null);
            mCount2.moveToFirst();
            String count2= mCount2.getString(0);
            idUnidad = count2;
            System.out.println(count2);
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return idUnidad;
    }

    /************************************************** CatMarcaVehiculos ********************************************************************/
    public void insertCatVehiculos(String idMarca, String marca){
        SQLiteDatabase dbSqLiteDatabase = this.getWritableDatabase();
        dbSqLiteDatabase.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("IdMarca",idMarca);
            values.put("Marca",marca);
            dbSqLiteDatabase.insert(Table_CatMarcaVehiculos,null,values);
            dbSqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            dbSqLiteDatabase.endTransaction();
            dbSqLiteDatabase.close();
        }
    }
    public ArrayList<String> getAllMarcaVehiculos(){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_CatMarcaVehiculos;
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatMarcaVehiculos;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String marca = cursor.getString(cursor.getColumnIndex("Marca"));
                    list.add(marca);
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
    public String getIdMarcaVehiculo(String marca){
        String idMarcaVehiculos = "";
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatMarcaVehiculos;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);

            String selectQuery = "SELECT IdMarca FROM " + Table_CatMarcaVehiculos +" WHERE Marca = '"+marca+"'";
            Cursor mCount2= dbDatabase.rawQuery(selectQuery, null);
            mCount2.moveToFirst();
            String count2= mCount2.getString(0);
            idMarcaVehiculos = count2;
            System.out.println(count2);
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return idMarcaVehiculos;
    }

    /************************************************** CatSubMarcaVehiculos ********************************************************************/
    public void insertCatSubMarcaVehiculos(String idMarca, int idSubMarca, String subMarca){
        SQLiteDatabase dbSqLiteDatabase = this.getWritableDatabase();
        dbSqLiteDatabase.beginTransaction();
        ContentValues values;
        try {
            values = new ContentValues();
            values.put("IdMarca",idMarca);
            values.put("IdSubMarca",idSubMarca);
            values.put("SubMarca",subMarca);
            dbSqLiteDatabase.insert(Table_CatSubMarcaVehiculos,null,values);
            dbSqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            dbSqLiteDatabase.endTransaction();
            dbSqLiteDatabase.close();
        }
    }
    public ArrayList<String> getAllSubMarcaVehiculos(){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT * FROM " + Table_CatSubMarcaVehiculos;
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatSubMarcaVehiculos;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String subMarca = cursor.getString(cursor.getColumnIndex("SubMarca"));
                    list.add(subMarca);
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
    public String getIdSubMarcaVehiculos(String subMarca){
        String idSubMarca = "";
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery2 = "SELECT COUNT(*) FROM " + Table_CatSubMarcaVehiculos;
            Cursor mCount= dbDatabase.rawQuery(selectQuery2, null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            System.out.println(count);

            String selectQuery = "SELECT IdSubMarca FROM " + Table_CatSubMarcaVehiculos +" WHERE SubMarca = '"+subMarca+"'";
            Cursor mCount2= dbDatabase.rawQuery(selectQuery, null);
            mCount2.moveToFirst();
            String count2= mCount2.getString(0);
            idSubMarca = count2;
            System.out.println(count2);
            dbDatabase.setTransactionSuccessful();
        }catch (Exception e) {e.printStackTrace();}
        finally {
            {
                dbDatabase.endTransaction();
                dbDatabase.close();
            }
        }
        return idSubMarca;
    }

    public ArrayList<String> getValueByIdMarca(String idMarcaVehiculos){
        ArrayList<String> list = new ArrayList<String>();
        SQLiteDatabase dbDatabase = this.getReadableDatabase();
        dbDatabase.beginTransaction();
        try{
            String selectQuery = "SELECT SubMarca FROM " + Table_CatSubMarcaVehiculos +" WHERE IdMarca = '"+idMarcaVehiculos+"'";
            Cursor cursor = dbDatabase.rawQuery(selectQuery, null);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    String subMarca = cursor.getString(cursor.getColumnIndex("SubMarca"));
                    list.add(subMarca);
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
