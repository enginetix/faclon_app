package faclon.sensorremote;

/**
 * Created by Utkarsh on 25-Sep-15.
 */
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class SecondFragment extends Fragment {
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
        v = inflater.inflate(R.layout.second_frag, container, false);
        Bundle bundle = getArguments();
        if(bundle!=null)
        {
            sensorUID = getArguments().getString("senID");
            tankname = getArguments().getString("tNAME");
            SCALE_C = (getArguments().getString("tSCALEC"));
            SCALE_M = (getArguments().getString("tSCALEM"));
            DPs = (getArguments().getString("tDP"));
            UNITs = (getArguments().getString("tUNIT"));
            numVar = getArguments().getInt("numVar");

        }
        new MyAsyncTask().execute();
        return v;
    }

    public static SecondFragment newInstance(String text) {

        SecondFragment f = new SecondFragment();
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

            numVar=5;
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
                    // System.out.println(k);
                    //for (int l = 0; l < k; l++) {
                    //   System.out.println("time = " + ravi[l][0] + "  " + "value = " + ravi[l][1]);
                    //}
                }

                in.close();
                connection.disconnect();


            } catch (Exception e) {
                e.printStackTrace();
                //returnMsg = "" + e;
            }
            return returnMsg;
        }

        protected void onPostExecute(String result) {

            if (k > 4) {
                long unixSeconds = Long.parseLong(ravi[k - 1][0] + "");
                Date date = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a"); // the format of your date
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+05:30")); // give a timezone reference for formating (see comment at the bottom
                String formattedDate = sdf.format(date);
                System.out.println(formattedDate);
                //   tv.setText("Latest Reading: " + (Float.parseFloat(ravi[numVar - 1][1]) * m + c) + "");
                //tv.setText("Latest Reading: " + (formattedDate) + "");
                TextView ttv1 = (TextView) v.findViewById(R.id.lastV21);
                ttv1.setText((Float.parseFloat(ravi[0][1]) * m + c) + " " + UNITs);
                TextView ttv2 = (TextView) v.findViewById(R.id.lastV51);
                ttv2.setText((Float.parseFloat(ravi[1][1]) * m + c) + " " + UNITs);
                TextView ttv3 = (TextView) v.findViewById(R.id.lastV81);
                ttv3.setText((Float.parseFloat(ravi[2][1]) * m + c) + " " + UNITs);
                TextView ttv4 = (TextView) v.findViewById(R.id.lastV111);
                ttv4.setText((Float.parseFloat(ravi[3][1]) * m + c) + " " + UNITs);
                TextView ttv5 = (TextView) v.findViewById(R.id.lastV141);
                ttv5.setText((Float.parseFloat(ravi[4][1]) * m + c) + " " + UNITs);


                long unixSec = Long.parseLong(ravi[0][0] + "");
                Date date1 = new Date(unixSec * 1000L); // *1000 is to convert seconds to milliseconds
                SimpleDateFormat sdformat = new SimpleDateFormat("dd-MM-yyyy hh:mm a"); // the format of your date
                sdformat.setTimeZone(TimeZone.getTimeZone("GMT+05:30")); // give a timezone reference for formating (see comment at the bottom
                String datestr = sdformat.format(date1);
                TextView ttt1 = (TextView) v.findViewById(R.id.lastV31);
                ttt1.setText(datestr);
                TextView ttt2 = (TextView) v.findViewById(R.id.lastV61);
                unixSec = Long.parseLong(ravi[1][0] + "");
                date1 = new Date(unixSec * 1000L);
                datestr = sdformat.format(date1);
                ttt2.setText(datestr);
                TextView ttt3 = (TextView) v.findViewById(R.id.lastV91);
                unixSec = Long.parseLong(ravi[2][0] + "");
                date1 = new Date(unixSec * 1000L);
                datestr = sdformat.format(date1);
                ttt3.setText(datestr);
                TextView ttt4 = (TextView) v.findViewById(R.id.lastV121);
                unixSec = Long.parseLong(ravi[3][0] + "");
                date1 = new Date(unixSec * 1000L);
                datestr = sdformat.format(date1);
                ttt4.setText(datestr);
                TextView ttt5 = (TextView) v.findViewById(R.id.lastV151);
                unixSec = Long.parseLong(ravi[4][0] + "");
                date1 = new Date(unixSec * 1000L);
                datestr = sdformat.format(date1);
                ttt5.setText(datestr);
            } else {
                TextView ttv1 = (TextView) v.findViewById(R.id.lastV21);
                ttv1.setText("");
                TextView ttv2 = (TextView) v.findViewById(R.id.lastV51);
                ttv2.setText("");
                TextView ttv3 = (TextView) v.findViewById(R.id.lastV81);
                ttv3.setText("");
                TextView ttv4 = (TextView) v.findViewById(R.id.lastV111);
                ttv4.setText("");
                TextView ttv5 = (TextView) v.findViewById(R.id.lastV141);
                ttv5.setText("");

            }
        }
    }
}