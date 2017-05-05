package destekinfo.com.commandrunner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import destekinfo.com.commandrunner.host.App;
import destekinfo.com.commandrunner.host.AppList;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "hosts.sqlite";
    private static final String DATABASE_TABLE = "host";

    private static final String KEY_ID = "id";
    private static final String APP_NAME = "appName";
    private static final String HOST_NAME = "hostName";
    private static final String COMMAND = "command";
    private static final String TYPE = "type";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String USERS = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + APP_NAME + " TEXT,"+ HOST_NAME +" TEXT,"+ COMMAND +" TEXT,"+ TYPE +" INTEGER)";
        db.execSQL(USERS);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + DATABASE_TABLE);
    }

    public void addApp(App app) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(APP_NAME, app.getAppName());
        values.put(HOST_NAME, app.getHostName());
        values.put(COMMAND, app.getCommand());
        values.put(TYPE, app.getType());
        db.insert(DATABASE_TABLE, null, values);
        db.close();
    }

    public void deleteApp(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, KEY_ID + "=" + id, null);
    }

    public AppList getAppList(int typ) {
        SQLiteDatabase db = this.getReadableDatabase();
        AppList list = new AppList();
        Cursor cursor = db.rawQuery("select * from "+DATABASE_TABLE +" where "+TYPE+" = "+typ,null);

        if (cursor .moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String appName = cursor.getString(cursor.getColumnIndex(APP_NAME));
                String hostName = cursor.getString(cursor.getColumnIndex(HOST_NAME));
                String command = cursor.getString(cursor.getColumnIndex(COMMAND));
                int type = cursor.getInt(cursor.getColumnIndex(TYPE));

                App app = new App();
                app.setId(id);
                app.setAppName(appName);
                app.setHostName(hostName);
                app.setCommand(command);
                app.setType(type);

                cursor.moveToNext();
                list.add(app);
            }
        }
        return list;
    }
}