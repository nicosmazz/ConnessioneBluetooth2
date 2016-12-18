package com.example.nicomac.appdiprova;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button scanBluetooth = (Button)findViewById(R.id.button1);
        scanBluetooth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                // definisco l'intenzione
                Intent openPageListBluetooth = new Intent(MainActivity.this,listBluetooth.class);
                // passo all'attivazione dell'activity Pagina.java
                startActivity(openPageListBluetooth);
            }
        });

    }
}
