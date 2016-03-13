package faclon.sensorremote;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class Settings extends AppCompatActivity {

    protected ListAdapter adapter;

    protected SQLiteDatabase db;
    protected EditText UNIT_ET;
    protected EditText N_DP_ET;
    protected Context context;
    private Toolbar mToolbar;
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
        setContentView(R.layout.activity_settings);
        db = (new DatabaseHelper(this)).getWritableDatabase();


        TextView crnt = (TextView) findViewById(R.id.current);
        crnt.append("Unit = " + UNIT + " & Data points = " + DP);
    }


    public void add_dpU(View view) {
        UNIT_ET = (EditText) findViewById(R.id.unit);
        N_DP_ET = (EditText) findViewById(R.id.dpoints);
        String UNIT = UNIT_ET.getText().toString();
        String DP = N_DP_ET.getText().toString();
        if ((UNIT + "1") == "1" || (DP + "1") == "1") {
            System.out.println("1" + UNIT + "1            dd       1" + DP + "1");
            Toast.makeText(Settings.this,
                    "Invalid Entry", Toast.LENGTH_LONG).show();
        } else {
            String s = "UPDATE wtrlvl SET UNIT='" + UNIT + "',DP='" + DP + "' WHERE UID='" + UID + "' AND TANK_NAME='" + NAME + "'";
            db.execSQL(s);

            Toast.makeText(Settings.this,
                    "New settings saved: unit " + UNIT + " Data points= " + DP, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(Settings.this, scatter.class);
            intent.putExtra("sUID", UID);
            intent.putExtra("tNAME", NAME);
            intent.putExtra("tSCALEM", ScaleM);
            intent.putExtra("tSCALEC", ScaleC);
            intent.putExtra("tUNIT", UNIT);
            intent.putExtra("tDP", DP);
            startActivity(intent);
        }
    }



    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
