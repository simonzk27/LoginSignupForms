package com.example.loginsignupforms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Botón de registrarse
        Button btnRegister = findViewById(R.id.button2);

        // Al hacer clic en el botón de registro, inicia la actividad Register_user
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register_user.class);
                startActivity(intent);
            }
        });
    }
}

