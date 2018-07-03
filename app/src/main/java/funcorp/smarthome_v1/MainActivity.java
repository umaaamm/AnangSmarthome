package funcorp.smarthome_v1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;


public class MainActivity extends AppCompatActivity {
    //koneksi mqtt
    MqttAndroidClient client;
    String clientId;
    ImageView lampu_ruang_tamu,lampu_kamar_mandi,lampu_kamar_tidur,lampu_dapur,lampu_teras;
    String Slampu_kamar_mandi="mati",Slampu_kamar_tidur="mati",Slampu_ruang_tamu="mati",Slampu_dapur="mati",Slampu_teras="mati";
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lampu_ruang_tamu = (ImageView) findViewById(R.id.lampu_ruang_tamu);
        lampu_kamar_mandi = (ImageView) findViewById(R.id.lampu_kamar_mandi);
        lampu_kamar_tidur = (ImageView) findViewById(R.id.lampu_kamartidur);
        lampu_dapur = (ImageView) findViewById(R.id.lampu_dapur);
        lampu_teras = (ImageView) findViewById(R.id.lampu_teras);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        cek();
//                        cek();
//                        cek();
                        finish();
                        startActivity(getIntent());

                        swipeRefreshLayout.setRefreshing(false);

                    }
                },2000);
            }
        });

        sambung();


        lampu_kamar_mandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payload = "the payload";
                try {
                    if (Slampu_kamar_mandi.equals("hidup")) {
                        payload = "off";
                        lampu_kamar_mandi.setImageResource(R.drawable.lamp_off);
                        //Toast.makeText(this, "Status : " + payload, Toast.LENGTH_LONG).show();
                    }
                    if (Slampu_kamar_mandi.equals("mati")) {
                        lampu_kamar_mandi.setImageResource(R.drawable.lamp_on);
                        payload = "on";
                    }

                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
                }
                String topic = "L5KM";
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        lampu_teras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payload = "the payload";
                try {
                    if (Slampu_teras.equals("hidup")) {
                        payload = "off";
                        lampu_teras.setImageResource(R.drawable.lamp_luar_off);
                        //Toast.makeText(this, "Status : " + payload, Toast.LENGTH_LONG).show();
                    }
                    if (Slampu_teras.equals("mati")) {
                        lampu_teras.setImageResource(R.drawable.lamp_luar_on);
                        payload = "on";
                    }

                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
                }
                String topic = "L1T";
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        lampu_dapur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payload = "the payload";
                try {
                    if (Slampu_dapur.equals("hidup")) {
                        payload = "off";
                        lampu_dapur.setImageResource(R.drawable.lamp_luar_off);
                        //Toast.makeText(this, "Status : " + payload, Toast.LENGTH_LONG).show();
                    }
                    if (Slampu_dapur.equals("mati")) {
                        lampu_dapur.setImageResource(R.drawable.lamp_luar_on);
                        payload = "on";
                    }

                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
                }
                String topic = "L2D";
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        lampu_ruang_tamu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payload = "the payload";
                try {
                    if (Slampu_ruang_tamu.equals("hidup")) {
                        payload = "off";
                        lampu_ruang_tamu.setImageResource(R.drawable.lamp_luar_off);
                        //Toast.makeText(this, "Status : " + payload, Toast.LENGTH_LONG).show();
                    }
                    if (Slampu_ruang_tamu.equals("mati")) {
                        lampu_ruang_tamu.setImageResource(R.drawable.lamp_luar_on);
                        payload = "on";
                    }

                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
                }
                String topic = "L4RT";
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        lampu_kamar_tidur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payload = "the payload";
                try {
                    if (Slampu_kamar_tidur.equals("hidup")) {
                        payload = "off";
                        lampu_kamar_tidur.setImageResource(R.drawable.lamp_luar_off);
                        //Toast.makeText(this, "Status : " + payload, Toast.LENGTH_LONG).show();
                    }
                    if (Slampu_kamar_tidur.equals("mati")) {
                        lampu_kamar_tidur.setImageResource(R.drawable.lamp_luar_on);
                        payload = "on";
                    }

                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
                }
                String topic = "L3KT";
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }
            }
        });

    }


//    //ruang kamar mandi
//    public void Fruang_kamar_mandi(View view) {
//
//        String payload = "the payload";
//        try {
//            if (Slampu_kamar_mandi.equals("hidup")) {
//                payload = "off";
//                lampu_kamar_mandi.setImageResource(R.drawable.lamp_off);
//                //Toast.makeText(this, "Status : " + payload, Toast.LENGTH_LONG).show();
//            }
//            if (Slampu_kamar_mandi.equals("mati")) {
//                lampu_kamar_mandi.setImageResource(R.drawable.lamp_on);
//                payload = "on";
//            }
//
//        }catch (Exception e){
//            Toast.makeText(this, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
//        }
//        String topic = "lampu1";
//        byte[] encodedPayload = new byte[0];
//        try {
//            encodedPayload = payload.getBytes("UTF-8");
//            MqttMessage message = new MqttMessage(encodedPayload);
//            client.publish(topic, message);
//        } catch (UnsupportedEncodingException | MqttException e) {
//            e.printStackTrace();
//        }
//    }

//    //ruang kamar tidur
//    public void Fruang_kamar_tidur(View view) {
//
//        String payload = "the payload";
//        try {
//            if (Slampu_kamar_tidur.equals("hidup")) {
//                payload = "off";
//                lampu_kamar_tidur.setImageResource(R.drawable.lamp_off);
//                //Toast.makeText(this, "Status : " + payload, Toast.LENGTH_LONG).show();
//            }
//            if (Slampu_kamar_tidur.equals("mati")) {
//                lampu_kamar_tidur.setImageResource(R.drawable.lamp_on);
//                payload = "on";
//            }
//
//        }catch (Exception e){
//            Toast.makeText(this, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
//        }
//        String topic = "lampu1";
//        byte[] encodedPayload = new byte[0];
//        try {
//            encodedPayload = payload.getBytes("UTF-8");
//            MqttMessage message = new MqttMessage(encodedPayload);
//            client.publish(topic, message);
//        } catch (UnsupportedEncodingException | MqttException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //ruang tamu
//    public void Fruang_lampu(View view) {
//
//        String payload = "the payload";
//        try {
//            if (Slampu_ruang_tamu.equals("hidup")) {
//                payload = "off";
//                lampu_ruang_tamu.setImageResource(R.drawable.lamp_off);
//                //Toast.makeText(this, "Status : " + payload, Toast.LENGTH_LONG).show();
//            }
//            if (Slampu_ruang_tamu.equals("mati")) {
//                lampu_ruang_tamu.setImageResource(R.drawable.lamp_on);
//                payload = "on";
//            }
//
//        }catch (Exception e){
//            Toast.makeText(this, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
//        }
//        String topic = "lampu1";
//        byte[] encodedPayload = new byte[0];
//        try {
//            encodedPayload = payload.getBytes("UTF-8");
//            MqttMessage message = new MqttMessage(encodedPayload);
//            client.publish(topic, message);
//        } catch (UnsupportedEncodingException | MqttException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //ruang dapur
//    public void Flampu_dapur(View view) {
//
//        String payload = "the payload";
//        try {
//            if (Slampu_dapur.equals("hidup")) {
//                payload = "off";
//                lampu_dapur.setImageResource(R.drawable.lamp_off);
//                //Toast.makeText(this, "Status : " + payload, Toast.LENGTH_LONG).show();
//            }
//            if (Slampu_dapur.equals("mati")) {
//                lampu_dapur.setImageResource(R.drawable.lamp_on);
//                payload = "on";
//            }
//
//        }catch (Exception e){
//            Toast.makeText(this, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
//        }
//        String topic = "lampu1";
//        byte[] encodedPayload = new byte[0];
//        try {
//            encodedPayload = payload.getBytes("UTF-8");
//            MqttMessage message = new MqttMessage(encodedPayload);
//            client.publish(topic, message);
//        } catch (UnsupportedEncodingException | MqttException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //ruang teras
//    public void Fruang_teras(View view) {
//
//        String payload = "the payload";
//        try {
//            if (Slampu_teras.equals("hidup")) {
//                payload = "off";
//                lampu_teras.setImageResource(R.drawable.lamp_off);
//                //Toast.makeText(this, "Status : " + payload, Toast.LENGTH_LONG).show();
//            }
//            if (Slampu_teras.equals("mati")) {
//                lampu_teras.setImageResource(R.drawable.lamp_on);
//                payload = "on";
//            }
//
//        }catch (Exception e){
//            Toast.makeText(this, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
//        }
//        String topic = "lampu1";
//        byte[] encodedPayload = new byte[0];
//        try {
//            encodedPayload = payload.getBytes("UTF-8");
//            MqttMessage message = new MqttMessage(encodedPayload);
//            client.publish(topic, message);
//        } catch (UnsupportedEncodingException | MqttException e) {
//            e.printStackTrace();
//        }
//    }



    public  void sambung(){
        //"tcp://192.168.43.39:1883"
        clientId = MqttClient.generateClientId();
        client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://test.mosquitto.org:1883",
                        clientId);
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);
//            options.setUserName("fujimiya");
//            options.setPassword("123".toCharArray());
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    //Log.d(TAG, "onSuccess");
                    Toast.makeText(MainActivity.this,"Terhubung",Toast.LENGTH_SHORT).show();
                    cek();
                    cek();
                    String topic = "test";
                    int qos = 1;
                    try {
                        IMqttToken subToken = client.subscribe(topic, qos);
                        subToken.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                // The message was published
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken,
                                                  Throwable exception) {
                                // The subscription could not be performed, maybe the user was not
                                // authorized to subscribe on the specified topic e.g. using wildcards

                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    try {
                        client.subscribe("L5KM",qos);
                        client.subscribe("L1T",qos);
                        client.subscribe("L2D",qos);
                        client.subscribe("L3KT",qos);
                        client.subscribe("L4RT",qos);

                        client.setCallback(new MqttCallback() {
                            @Override
                            public void connectionLost(Throwable cause) {

                            }

                            @Override
                            public void messageArrived(String topic, MqttMessage message) throws Exception {
                                //Toast.makeText(MainActivity.this,"topic : "+topic+" message : "+message.toString(),Toast.LENGTH_SHORT).show();
                                //setMessageNotification(topic,message.toString());

                                if(topic.equals("status_L5KM")){
                                    Slampu_kamar_mandi = ""+message;
                                    if(Slampu_kamar_mandi.equals("hidup")){
                                        lampu_kamar_mandi.setImageResource(R.drawable.lamp_luar_on);
                                    }
                                    if(Slampu_kamar_mandi.equals("mati")){
                                        lampu_kamar_mandi.setImageResource(R.drawable.lamp_luar_off);
                                    }
                                }
                                if(topic.equals("status_L3KT")){
                                    Slampu_kamar_tidur = ""+message;
                                    if(Slampu_kamar_tidur.equals("hidup")){
                                        lampu_kamar_tidur.setImageResource(R.drawable.lamp_luar_on);
                                    }
                                    if(Slampu_kamar_tidur.equals("mati")){
                                        lampu_kamar_tidur.setImageResource(R.drawable.lamp_luar_off);
                                    }

                                }
                                if(topic.equals("status_L4RT")){
                                    Slampu_ruang_tamu = ""+message;
                                    if(Slampu_ruang_tamu.equals("hidup")){
                                        lampu_ruang_tamu.setImageResource(R.drawable.lamp_luar_on);
                                    }
                                    if(Slampu_ruang_tamu.equals("mati")){
                                        lampu_ruang_tamu.setImageResource(R.drawable.lamp_luar_off);
                                    }

                                }
                                if(topic.equals("status_L2D")){
                                    Slampu_dapur = ""+message;
                                    if(Slampu_dapur.equals("hidup")){
                                        lampu_dapur.setImageResource(R.drawable.lamp_luar_on);
                                    }
                                    if(Slampu_dapur.equals("mati")){
                                        lampu_dapur.setImageResource(R.drawable.lamp_luar_off);
                                    }
                                }
                                if(topic.equals("status_L1T")){
                                    Slampu_teras = ""+message;
                                    if(Slampu_teras.equals("hidup")){
                                        lampu_teras.setImageResource(R.drawable.lamp_luar_on);
                                    }
                                    if(Slampu_teras.equals("mati")){
                                        lampu_teras.setImageResource(R.drawable.lamp_luar_off);
                                    }
                                }
                            }

                            @Override
                            public void deliveryComplete(IMqttDeliveryToken token) {

                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    //Log.d(TAG, "onFailure");
                    Toast.makeText(MainActivity.this,"Tidak Terhubung",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }
    public void cek(){
        String topic = "cek";
        String payload = "Periksa Kondisi";
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
