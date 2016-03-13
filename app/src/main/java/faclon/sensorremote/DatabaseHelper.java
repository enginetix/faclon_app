package faclon.sensorremote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Utkarsh on 13-Feb-16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "wtrlvl";
    protected Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
}
    @Override
    public void onCreate(SQLiteDatabase db) {
        String s;
        try {

            InputStream in = context.getResources().openRawResource(R.raw.sql);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(in, null);
            NodeList statements = doc.getElementsByTagName("statement");
            for (int i=0; i<statements.getLength(); i++) {
                s = statements.item(i).getChildNodes().item(0).getNodeValue();
                db.execSQL(s);
            }
        } catch (Throwable t) {
            Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS wtrlvl");
        onCreate(db);
    }
}