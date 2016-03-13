package faclon.sensorremote;

/**
 * Created by Utkarsh on 18-Feb-16.
 */

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class scaling extends AppCompatActivity {

    protected ListAdapter adapter;

    protected SQLiteDatabase db;
    protected EditText SCALE_M;
    protected EditText SCALE_C;
    protected Context context;

    String UID;
    String NAME;
    String ScaleC;
    String ScaleM;
    String UNIT;
    String DP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        if (null != intent) {
            UID = intent.getStringExtra("sUID");
            NAME = intent.getStringExtra("sNAME");
            ScaleC = intent.getStringExtra("tSCALEC");
            ScaleM = intent.getStringExtra("tSCALEM");
            UNIT = intent.getStringExtra("tUNIT");
            DP = intent.getStringExtra("tDP");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaling);
        db = (new DatabaseHelper(this)).getWritableDatabase();
        TextView crnt = (TextView) findViewById(R.id.current);
        crnt.append("Slope = " + ScaleM + " & Y-Intercept = " + ScaleC);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    public void addScale(View view) {
        SCALE_M = (EditText) findViewById(R.id.Tanksensor);
        SCALE_C = (EditText) findViewById(R.id.SensorCode);
        String s1 = SCALE_M.getText().toString();
        String s2 = SCALE_C.getText().toString();
        if ((s1 + "1") == "1" || (s2 + "1") == "1") {
            System.out.println("1" + s1 + "1            dd       1" + s2 + "1");
            Toast.makeText(scaling.this,
                    "Invalid Entry", Toast.LENGTH_LONG).show();
        } else {
            String s = "UPDATE wtrlvl SET SCALE_M='" + s1 + "',SCALE_C='" + s2 + "' WHERE UID='" + UID + "'";
            db.execSQL(s);

            Toast.makeText(scaling.this,
                    "New Scale Added: m=" + s1 + " c=" + s2, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(scaling.this, scatter.class);
            intent.putExtra("sUID", UID);
            intent.putExtra("tNAME", NAME);
            intent.putExtra("tSCALEM", s1);
            intent.putExtra("tSCALEC", s2);
            intent.putExtra("tDP", DP);
            intent.putExtra("tUNIT", UNIT);
            startActivity(intent);

        }
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scaling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
