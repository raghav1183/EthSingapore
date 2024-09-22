package com.raghav.ethglobalsingapore2024.javaapp;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class MainActivity extends AppCompatActivity {


    private BleAdvertiser bleAdvertiser;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private EditText inputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        inputEditText = findViewById(R.id.inputEditText);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter == null){
                Toast.makeText(this, "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
            }
            else if(!bluetoothAdapter.isEnabled()){
                Toast.makeText(this, "Please enable Bluetooth", Toast.LENGTH_SHORT).show();
            }
            else {
                // Device supports Bluetooth
                bleAdvertiser = new BleAdvertiser(this,bluetoothAdapter);
                Toast.makeText(this, bleAdvertiser.toString(), Toast.LENGTH_SHORT).show();
            }
            return insets;
        });
    }

    public void onBroadcastClick(View v){
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");  // Use Elliptic Curve (EC) cryptography
        keyGen.initialize(256);  // Key size
        KeyPair keyPair = keyGen.generateKeyPair();

// Get the public and private keys
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();

        MessageSigner messageSigner = new MessageSigner( privateKey);
        String signedMessage = new String(messageSigner.signMessage("Hello World!"));
        String message = "message:"+inputEditText.getText().toString().concat("sign:"+signedMessage+";publicKey:").concat(String.valueOf(publicKey).toString()); //json object with message and signature
        bleAdvertiser.startAdvertising(message);

    }


}