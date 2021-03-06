package faclon.sensorremote;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.mikepenz.crossfader.Crossfader;
import com.mikepenz.crossfader.util.UIUtils;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

public class addsensor extends AppCompatActivity {

    protected SQLiteDatabase db;
    protected EditText Tanksensor;
    protected EditText SensorCode;
    protected Context context;
    private Drawer result = null;
    private MiniDrawer miniResult = null;
    private Crossfader crossFader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsensor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        db = (new DatabaseHelper(this)).getWritableDatabase();


        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withTranslucentStatusBar(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                        new PrimaryDrawerItem().withName("Alerts").withIcon(GoogleMaterial.Icon.gmd_notifications_active).withBadge("" + (int) Math.floor(Math.random() * 30)).withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName("Add Device").withIcon(GoogleMaterial.Icon.gmd_plus_circle).withIdentifier(3),
                        new PrimaryDrawerItem().withName("Remove Device").withIcon(GoogleMaterial.Icon.gmd_minus_circle).withIdentifier(4),
                        new PrimaryDrawerItem().withName("Help").withIcon(GoogleMaterial.Icon.gmd_help).withIdentifier(6),
                        new DividerDrawerItem(),
                        new SwitchDrawerItem().withName("Alerts").withIcon(GoogleMaterial.Icon.gmd_notifications).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener)
                ) // add the items we want to use with our Drawer

                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 1) {
                            Intent intent = new Intent(addsensor.this, MainActivity.class);
                            startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 2) {
                            Intent intent = new Intent(addsensor.this, remove_sensor.class);
                            startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 3) {
                            Intent intent = new Intent(addsensor.this, addsensor.class);
                            startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 4) {
                            Intent intent = new Intent(addsensor.this, remove_sensor.class);
                            startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 6) {
                            Intent intent = new Intent(addsensor.this, remove_sensor.class);
                            startActivity(intent);
                        }
                        return false;
                    }
                })

                .withGenerateMiniDrawer(true)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(3)
                .buildView();

        miniResult = result.getMiniDrawer();

        int firstWidth = (int) UIUtils.convertDpToPixel(230, this);
        int secondWidth = (int) UIUtils.convertDpToPixel(70, this);


        crossFader = new Crossfader()
                .withContent(findViewById(R.id.crossfade_content))
                .withFirst(result.getSlider(), firstWidth)
                .withSecond(miniResult.build(this), secondWidth)
                .withSavedInstance(savedInstanceState)
                .build();


        crossFader.getCrossFadeSlidingPaneLayout().setShadowResourceLeft(R.drawable.material_drawer_shadow_left);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void addSnsr(View view) {
        Tanksensor = (EditText) findViewById(R.id.Tanksensor);
        SensorCode = (EditText) findViewById(R.id.SensorCode);
        String s1 = Tanksensor.getText().toString();
        String s2 = SensorCode.getText().toString();
        if (s1.matches(""))
            Toast.makeText(addsensor.this,
                    "Please enter valid name and code", Toast.LENGTH_LONG).show();
        else {


            if ((s1 + "1") == "1" || (s2 + "1") == "1") {
                System.out.println("1" + s1 + "1            dd       1" + s2 + "1");
                Toast.makeText(addsensor.this,
                        "Invalid Entry", Toast.LENGTH_LONG).show();
            } else {
                String s = "INSERT INTO wtrlvl (UID, TANK_NAME, SCALE_M, SCALE_C, DP, UNIT) VALUES('" + s2 + "','" + s1 + "','1','0','20','MM')";
                db.execSQL(s);
                Toast.makeText(addsensor.this,
                        "Entry added", Toast.LENGTH_LONG).show();
                Tanksensor.setText("");
                SensorCode.setText("");
            }


        }
    }


    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };

}
