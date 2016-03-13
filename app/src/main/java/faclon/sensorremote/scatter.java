package faclon.sensorremote;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class scatter extends AppCompatActivity {
    protected String sensorUID;
    protected String tankname;
    protected String SCALE_M;
    protected String SCALE_C;
    protected String DPs;
    protected String UNITs;
    String np;
    int k = 0;
    float m;
    float c;
    int numVar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scatter);
        sensorUID = getIntent().getStringExtra("sUID");
        tankname = getIntent().getStringExtra("tNAME");
        SCALE_C = (getIntent().getStringExtra("tSCALEC"));
        SCALE_M = (getIntent().getStringExtra("tSCALEM"));
        DPs = (getIntent().getStringExtra("tDP"));
        UNITs = (getIntent().getStringExtra("tUNIT"));
        numVar = Integer.parseInt(DPs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tankname);

        final ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Graph").setIcon(R.mipmap.ic_launcher));
        tabLayout.addTab(tabLayout.newTab().setText("LVT").setIcon(R.mipmap.ic_launcher));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.ic_launcher));


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
                System.out.print(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_scaling) {
            Intent intent = new Intent(scatter.this, scaling.class);
            intent.putExtra("sUID", sensorUID);
            intent.putExtra("sNAME", tankname);
            intent.putExtra("tSCALEC", SCALE_C);
            intent.putExtra("tSCALEM", SCALE_M);
            intent.putExtra("tUNIT", UNITs);
            intent.putExtra("tDP", DPs);
            startActivity(intent);
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(scatter.this, Settings.class);
            intent.putExtra("sUID", sensorUID);
            intent.putExtra("sNAME", tankname);
            intent.putExtra("tSCALEC", SCALE_C);
            intent.putExtra("tSCALEM", SCALE_M);
            intent.putExtra("tUNIT", UNITs);
            intent.putExtra("tDP", DPs);
            startActivity(intent);
        }

        if (id == R.id.action_refresh) {
            Intent intent = new Intent(scatter.this, scatter.class);
            intent.putExtra("sUID", sensorUID);
            intent.putExtra("tNAME", tankname);
            intent.putExtra("tSCALEC", SCALE_C);
            intent.putExtra("tSCALEM", SCALE_M);
            intent.putExtra("tUNIT", UNITs);
            intent.putExtra("tDP", DPs);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            Fragment fragment;
            Bundle args = new Bundle();
            args.putString("senID", sensorUID);
            args.putInt("numVar", numVar);

            switch (pos) {
                case 0:
                    fragment = FirstFragment.newInstance("FirstFragment, Instance 1");
                    break;
                case 1:
                    fragment = SecondFragment.newInstance("SecondFragment, Instance 1");
                    break;
                case 2:
                    fragment = ThirdFragment.newInstance("ThirdFragment, Instance 1");
                    break;
                default:
                    fragment = FirstFragment.newInstance("FirstFragment, Instance 1");
                    break;
            }
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public int getCount() {
            return 3;
        }
    }
}