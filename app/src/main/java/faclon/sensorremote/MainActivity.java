package faclon.sensorremote;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
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

public class MainActivity extends AppCompatActivity {


    protected EditText searchText;
    protected SQLiteDatabase db;
    protected Cursor cursor;
    protected ListAdapter adapter;
    protected ListView tankList;
    protected Context context;
    private Drawer result = null;
    private MiniDrawer miniResult = null;
    private Crossfader crossFader;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, remove_sensor.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setTitle("SensorRemote");


        db = (new DatabaseHelper(this)).getWritableDatabase();
        searchText = (EditText) findViewById(R.id.searchText);
        tankList = (ListView) findViewById(R.id.list);

        regenList();

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
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 2) {

                            Toast.makeText(getApplicationContext(), "Notifications under development", Toast.LENGTH_SHORT).show();
                        }
                        if (drawerItem.getIdentifier() == 3) {
                            Intent intent = new Intent(MainActivity.this, addsensor.class);
                            startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 4) {
                            Intent intent = new Intent(MainActivity.this, remove_sensor.class);
                            startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 6) {

                            Toast.makeText(getApplicationContext(), "Help under development", Toast.LENGTH_SHORT).show();
                        }

                        return false;
                    }
                })
                .withGenerateMiniDrawer(true)
                .withSavedInstance(savedInstanceState)
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

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        //   miniResult.withCrossFader(new CrossfadeWrapper(crossFader));

        //define a shadow (this is only for normal LTR layouts if you have a RTL app you need to define the other one
        crossFader.getCrossFadeSlidingPaneLayout().setShadowResourceLeft(R.drawable.material_drawer_shadow_left);


        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                regenList();

            }


            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });


        tankList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(MainActivity.this, scatter.class);
                Cursor cursor = (Cursor) adapter.getItem(position);
                intent.putExtra("sDid", cursor.getString(cursor.getColumnIndex("_id")));
                intent.putExtra("sUID", cursor.getString(cursor.getColumnIndex("UID")));
                intent.putExtra("tNAME", cursor.getString(cursor.getColumnIndex("TANK_NAME")));
                intent.putExtra("tSCALEM", cursor.getString(cursor.getColumnIndex("SCALE_M")));
                intent.putExtra("tSCALEC", cursor.getString(cursor.getColumnIndex("SCALE_C")));
                intent.putExtra("tUNIT", cursor.getString(cursor.getColumnIndex("UNIT")));
                intent.putExtra("tDP", cursor.getString(cursor.getColumnIndex("DP")));
                startActivity(intent);
            }
        });


    }

    @Override
    public void onRestart() {
        super.onRestart();
        regenList();

    }

    public void regenList() {
        cursor = db.rawQuery("SELECT * FROM wtrlvl WHERE TANK_NAME || ' ' || TANK_NAME LIKE ?",
                new String[]{"%" + searchText.getText().toString() + "%"});
        adapter = new SimpleCursorAdapter(
                MainActivity.this,
                R.layout.tank_list_item,
                cursor,
                new String[]{"TANK_NAME", "UID"},
                new int[]{R.id.Tankname});
        tankList.setAdapter(adapter);
    }


}
