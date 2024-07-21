package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static DataBaseHelper database;
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE USER (EMAIL TEXT PRIMARY KEY, PHONE TEXT, FIRSTNAME TEXT, LASTNAME TEXT, GENDER BOOLEAN, PASSWORD TEXT, USERMODE BOOLEAN, PICTURE BLOB)");
        sqLiteDatabase.execSQL("CREATE TABLE PIZZA (NAME TEXT, SIZE TEXT, PRICE REAL, CATEGORY TEXT, PICTURE BLOB, PRIMARY KEY (NAME, SIZE))");
        sqLiteDatabase.execSQL("CREATE TABLE ORDERS (ID INTEGER PRIMARY KEY AUTOINCREMENT, EMAIL TEXT, PRICE REAL, ORDER_TIME DATETIME DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (EMAIL) REFERENCES USER(EMAIL))");
        sqLiteDatabase.execSQL("CREATE TABLE ORDERSEXTEND (ID INTEGER PRIMARY KEY AUTOINCREMENT, ORDERS_ID INTEGER, NAME TEXT, SIZE TEXT, QUANTITY INTEGER, FOREIGN KEY (ORDERS_ID) REFERENCES ORDERS(ID), FOREIGN KEY (NAME, SIZE) REFERENCES PIZZA(NAME, SIZE))");
        sqLiteDatabase.execSQL("CREATE TABLE SPECIAL (ID INTEGER PRIMARY KEY AUTOINCREMENT, TIME_PERIOD TEXT, DATE DATETIME DEFAULT CURRENT_TIMESTAMP, PRICE REAL, OLDPRICE REAL, PICTURE BLOB)");
        sqLiteDatabase.execSQL("CREATE TABLE SPECIALEXTEND (ID INTEGER PRIMARY KEY AUTOINCREMENT, SPECIAL_ID INTEGER, NAME TEXT, SIZE TEXT, QUANTITY INTEGER, FOREIGN KEY (SPECIAL_ID) REFERENCES SPECIAL(ID), FOREIGN KEY (NAME, SIZE) REFERENCES PIZZA(NAME, SIZE))");
        sqLiteDatabase.execSQL("CREATE TABLE FAVOURITE (EMAIL TEXT, NAME TEXT, PRIMARY KEY (EMAIL, NAME),FOREIGN KEY (EMAIL) REFERENCES USER(EMAIL),FOREIGN KEY (NAME) REFERENCES PIZZA(NAME))");

        sqLiteDatabase.execSQL("INSERT INTO USER (EMAIL, PHONE, FIRSTNAME, LASTNAME, GENDER, PASSWORD, USERMODE) VALUES ('luaihawi@gmail.com', '0599647713', 'luai', 'hawi', 1, '"+Hash.hashPassword("luai1234")+"', 1)");
        sqLiteDatabase.execSQL("INSERT INTO USER (EMAIL, PHONE, FIRSTNAME, LASTNAME, GENDER, PASSWORD, USERMODE) VALUES ('luai@gmail.com', '0599647713', 'luai', 'hawi', 1, '"+Hash.hashPassword("luai1234")+"', 0)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public long insertUser(User user) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EMAIL", user.getEmail());
        contentValues.put("PHONE", user.getPhone());
        contentValues.put("FIRSTNAME", user.getFirstname());
        contentValues.put("LASTNAME", user.getLastname());
        contentValues.put("GENDER", user.isGender());
        contentValues.put("PASSWORD", user.getPassword());
        contentValues.put("USERMODE", user.isUsermode());
        return sqLiteDatabase.insert("USER", null, contentValues);
    }

    public boolean updatePic(String type, byte[] pic) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("PICTURE", pic);

        db.update("PIZZA", values, "NAME = ?", new String[]{type});
        return true;

    }




    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (!(newEmailExists(user.getEmail()) && !user.getEmail().equals(CurrentUser.user))) {
            ContentValues values = new ContentValues();
            values.put("EMAIL", user.getEmail());
            values.put("PHONE", user.getPhone());
            values.put("FIRSTNAME", user.getFirstname());
            values.put("LASTNAME", user.getLastname());
            if(!user.getPassword().equals(""))
                values.put("PASSWORD", user.getPassword());
            values.put("GENDER", user.isGender());

            db.update("USER", values, "EMAIL = ?", new String[]{CurrentUser.user});
            return true;
        } else {
           return false;
        }
    }
    private boolean newEmailExists(String newEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USER WHERE EMAIL = ?", new String[]{newEmail});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM USER", null);
    }
    public Cursor getPizzaPrice(String name, String size) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM PIZZA WHERE NAME = '"+name+"' AND SIZE = '"+size+"'", null);
    }
    public Cursor getAllOrdersForOneType(String name) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM ORDERSEXTEND WHERE NAME = '"+name+"'", null);
    }
    public boolean changeProfilePicture(String email, byte[] picture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PICTURE", picture);
        try {
            db.update("USER", values, "EMAIL = ?", new String[]{email});
            db.close();
            return true;
        }catch (Exception e){
            db.close();
            return false;
        }

    }
    public Cursor getAllPizzas() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM PIZZA", null);
    }
    public Cursor getOffers() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM SPECIAL", null);
    }
    public Cursor getOffersExtend(int id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM SPECIALEXTEND WHERE SPECIAL_ID = "+id+"", null);
    }
    public Cursor getPizzaByName(String name) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM PIZZA WHERE NAME = '"+name+"'", null);
    }
    public Cursor getOrderById(int id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM ORDERS WHERE ID = "+id+"", null);
    }
    public Cursor getUserByName(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM USER WHERE EMAIL = '"+email+"'", null);
    }
    public Cursor getOrdersByEmail(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM ORDERS WHERE EMAIL = '"+email+"'", null);
    }
    public Cursor getAllOrders() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM ORDERS", null);
    }
    public Cursor getOrdersExtend(int id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM ORDERSEXTEND WHERE ORDERS_ID = "+id+"", null);
    }
    public Cursor getFavouritePizzas(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM FAVOURITE WHERE EMAIL = '"+email+"'", null);
    }
    public void undoFavourite(String email, String name) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM FAVOURITE WHERE EMAIL = '"+email+"' AND NAME = '"+name+"'");
    }
    public Cursor getAllPizzasFiltered(String category) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        if(!category.equals(""))
            return sqLiteDatabase.rawQuery("SELECT * FROM PIZZA WHERE CATEGORY = '"+category+"'", null);
        else return getAllPizzas();


    }
    public void insertPizza(Pizza pizza) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        try {
            ContentValues contentValues=new ContentValues();
            contentValues.put("NAME", pizza.name);
            contentValues.put("SIZE", pizza.size);
            contentValues.put("PRICE", pizza.price);
            contentValues.put("CATEGORY", pizza.category);
            contentValues.put("PICTURE", pizza.picture);
            sqLiteDatabase.insert("PIZZA", null, contentValues);
        }catch (Exception e){

        }
    }
    public void insertSpecialOrder(ArrayList<PizzaNS> pizzas, double oldPrice, double price, byte[] picture) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        SQLiteDatabase db = getWritableDatabase();
        int id = -1;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT MAX(ID) FROM SPECIAL", null);
        cursor.moveToFirst();
        id = cursor.getInt(0) + 1;
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", id);
        contentValues.put("PRICE", price);
        contentValues.put("OLDPRICE", oldPrice);
        contentValues.put("PICTURE", picture);
        db.insert("SPECIAL", null, contentValues);
        for (int counter = 0; counter < pizzas.size(); counter++) {
            db.execSQL("INSERT INTO SPECIALEXTEND (SPECIAL_ID, NAME, SIZE, QUANTITY) VALUES (" + id + ", '" + pizzas.get(counter).name + "', '" + pizzas.get(counter).size + "', " + pizzas.get(counter).quantity + ")");

        }
    }
    public boolean insertOrder(String email, String name,String size, int quantity, double price) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        SQLiteDatabase db=getReadableDatabase();
        try {
            Cursor c=db.rawQuery("SELECT MAX(ID) FROM ORDERS",null);
            int id=-1;
            if (c.moveToFirst()) {
                id = c.getInt(0)+1;
            }
            else id=1;
            sqLiteDatabase.execSQL("INSERT INTO ORDERS (ID, EMAIL, PRICE) VALUES ("+id+", '"+email+"', "+price+")");
            sqLiteDatabase.execSQL("INSERT INTO ORDERSEXTEND (ORDERS_ID, NAME, SIZE, QUANTITY) VALUES ("+id+", '"+name+"', '"+size+"', "+quantity+")");
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public boolean insertSpecialOrderToOrders(Orders orders) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        try {
            Cursor c=sqLiteDatabase.rawQuery("SELECT MAX(ID) FROM ORDERS",null);
            int id=1;
            if (c.moveToFirst()) {
                id = c.getInt(0)+1;
            }
            sqLiteDatabase.execSQL("INSERT INTO ORDERS (ID, EMAIL, PRICE) VALUES ("+id+", '"+orders.getEmail()+"', "+orders.getPrice()+")");
            for(int counter=0;counter<orders.getOrdersExtend().size();counter++){
                sqLiteDatabase.execSQL("INSERT INTO ORDERSEXTEND (ORDERS_ID, NAME, SIZE, QUANTITY) VALUES ("+id+", '"+orders.ordersExtend.get(counter).name+"', '"+orders.ordersExtend.get(counter).size+"', "+orders.ordersExtend.get(counter).quantity+")");
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public boolean insertFavourite(String email, String name) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        try {
            sqLiteDatabase.execSQL("INSERT INTO FAVOURITE(EMAIL, NAME) VALUES ('"+email+"', '"+name+"')");
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public int LogIn(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM USER WHERE email = ? AND password = ?";
            cursor = db.rawQuery(query, new String[]{email, password});

            if (cursor != null && cursor.moveToFirst()) {
                if(cursor.getInt(cursor.getColumnIndexOrThrow("USERMODE"))>0){
                    return 1;
                }
                return 0; // User exists and password matches
            } else {
                return -1; // User does not exist or password does not match
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }


}
