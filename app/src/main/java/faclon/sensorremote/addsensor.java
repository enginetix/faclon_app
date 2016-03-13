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
                        //  new PrimaryDrawerItem().withDescription("A more complex sample").withName(R.string.drawer_item_advanced_drawer).withIcon(GoogleMaterial.Icon.gmd_adb).withIdentifier(5),
                        new PrimaryDrawerItem().withName("Help").withIcon(GoogleMaterial.Icon.gmd_help).withIdentifier(6),
                        // new SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        //new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github),
                        new DividerDrawerItem(),
                        // new SwitchDrawerItem().withName("Switch").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                        new SwitchDrawerItem().withName("Alerts").withIcon(GoogleMaterial.Icon.gmd_notifications).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener)
                ) // add the items we want to use with our Drawer

                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {

                            Toast.makeText(addsensor.this, ((Nameable) drawerItem).getName().getText(addsensor.this), Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                })

                .withGenerateMiniDrawer(true)
                .withSavedInstance(savedInstanceState)
                        // build only the view of the Drawer (don't inflate it automatically in our layout which is done with .build())
                .buildView();

        //the MiniDrawer is managed by the Drawer and we just get it to hook it into the Crossfader
        miniResult = result.getMiniDrawer();

        //get the widths in px for the first and second panel
        int firstWidth = (int) UIUtils.convertDpToPixel(230, this);
        int secondWidth = (int) UIUtils.convertDpToPixel(70, this);

        //create and build our crossfader (see the MiniDrawer is also built in here, as the build method returns the view to be used in the crossfader)
        //the crossfader library can be found here: https://github.com/mikepenz/Crossfader
        crossFader = new Crossfader()
                .withContent(findViewById(R.id.crossfade_content))
                .withFirst(result.getSlider(), firstWidth)
                .withSecond(miniResult.build(this), secondWidth)
                .withSavedInstance(savedInstanceState)
                .build();

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        //   miniResult.withCrossFader(new CrossfadeWrapper(crossFader));

        //define a shadow (this is only for normal LTR layouts if you have a RTL app you need to define the other one
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
