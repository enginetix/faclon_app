package faclon.sensorremote;

/**
 * Created by Utkarsh on 18-Feb-16.
 */

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class FirstFragment extends Fragment {
    View v;
    String[][] ravi = new String[1000][60];
    int numVar;
    protected String sensorUID;
    protected String tankname;
    protected String SCALE_M;
    protected String SCALE_C;
    protected String DPs;
    protected String UNITs;
    int k = 0;
    float m = 1;
    float c = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.first_frag, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            sensorUID = getArguments().getString("senID");
            numVar = getArguments().getInt("numVar");
        }
        new MyAsyncTask().execute();
        return v;
    }


    public static FirstFragment newInstance(String text) {

        FirstFragment f = new FirstFragment();
        return f;
    }


    private ArrayList<LineDataSet> getDataSet() {
        ArrayList<LineDataSet> dataSets = null;
        ArrayList<Entry> valueSet1 = new ArrayList<>();
        for (int l = 0; l < k; l++) {

            valueSet1.add(new Entry(Float.parseFloat(ravi[l][1]) * m + c, l));
        }

        LineDataSet lDataSet1 = new LineDataSet(valueSet1, "Readings");
        lDataSet1.setColor(Color.rgb(0, 0, 0));
        lDataSet1.setValueTextSize(8f);
        dataSets = new ArrayList<>();
        dataSets.add(lDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            long unixSeconds = Long.parseLong(ravi[i][0] + "");
            Date date = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a"); // the format of your date
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+05:30")); // give a timezone reference for formating (see comment at the bottom
            String formattedDate = sdf.format(date);
            xAxis.add(formattedDate);
        }
        return xAxis;
    }

    private class MyAsyncTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            return getData();
        }

        protected String getData() {
            String decodedString = "";
            String returnMsg = "";

            numVar = 200;
            String request = "http://api.carriots.com/devices/arduinotest@rishisharma920.rishisharma920/streams/?order=-1&_contains[]=" + sensorUID + "&max=" + numVar;
            URL url;
            HttpURLConnection connection = null;
            try {
                url = new URL(request);
                connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("carriots.apikey", "e1e841ddf488f8470f101c081d30231596db7ecd08521a10c428da85b8bf2b5d");
                connection.addRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((decodedString = in.readLine()) != null) {
                    returnMsg += decodedString;

                    String[] toppings = returnMsg.split("[,]");

                    int le = toppings.length;

                    for (int p = 5; p < le; p += 10) {
                        String[] bad = toppings[p].split("[:]");
                        ravi[k][0] = bad[1];
                        String[] newbad = toppings[p + 2].split("[\"]");
                        ravi[k][1] = newbad[5];
                        k++;
                    }
                }

                in.close();
                connection.disconnect();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnMsg;
        }

        protected void onPostExecute(String result) {
            LineChart chart = (LineChart) v.findViewById(R.id.chart1);
            chart.setTouchEnabled(true);
//            MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
          //  chart.setMarkerView(mv);
            chart.setDrawGridBackground(false);
            LineData data = new LineData(getXAxisValues(), getDataSet());
            chart.setData(data);
            chart.setDescription(sensorUID);
            chart.animateXY(2000, 2000);
            chart.invalidate();
        }

    }


}