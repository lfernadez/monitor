package py.fpuna.tesis.qoetest.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import py.fpuna.tesis.qoetest.database.DatabaseContract.LocalizacionColumns;


/**
 * Created by User on 01/09/2014.
 */
public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_PATH = "/data/data/py" +
            ".fpuna.tesis.qoetest/databases/";
    private static final String DATABASE_NAME = "monitor.db";
    private static final int DATABASE_VERSION = 1;

    public static String getDBName() {
        return DATABASE_NAME;
    }


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    /**
     * Returns the database name and path.
     *
     * @return
     */
    public static String getDatabasePath() {
        return DATABASE_PATH + DATABASE_NAME;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.LOCALIZACION);
        db.execSQL("CREATE TABLE " + Tables.LOCALIZACION
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LocalizacionColumns.FECHA + " TEXT, "
                + LocalizacionColumns.HORA + " TEXT, "
                + LocalizacionColumns.LATITUD + " TEXT, "
                + LocalizacionColumns.LONGITUD + " TEXT, "
                + LocalizacionColumns.IMEI + " TEXT, "
                + LocalizacionColumns.PRECISION + " TEXT, "
                + LocalizacionColumns.TIPO_MUESTRA + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    public void createDataBase(Context context) throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            // do nothing - database already exist
        } else {
            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
            this.getReadableDatabase();
            /*try {
                copyDataBase(context);
            } catch (IOException e) {
                throw new Error("Error copying database");
            }*/
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // database does't exist yet.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     */
    private void copyDataBase(Context context) throws IOException {
        // Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = Environment.getDataDirectory()
                + DATABASE_PATH + DATABASE_NAME;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;

        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public interface Tables {
        String USUARIO = "usuario";
        String LOCALIZACION = "localizacion";
        String PEDIDOS = "pedido";
    }
}
