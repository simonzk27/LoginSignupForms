package com.example.loginsignupforms;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;

public class Register_user extends FragmentActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private TextView locationText;
    private TextView dateOfBirthText;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        // Inicialización de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationText = findViewById(R.id.location_text);

        Button locationButton = findViewById(R.id.location_button);
        locationButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(Register_user.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Register_user.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            } else {
                getLocation();
            }
        });

        // Inicialización de fecha de nacimiento
        dateOfBirthText = findViewById(R.id.date_of_birth_text);
        Button dateOfBirthButton = findViewById(R.id.date_of_birth_button);
        dateOfBirthButton.setOnClickListener(v -> showDatePicker());

        // Lógica para validar edad en el botón de registro
        Button registerButton = findViewById(R.id.button3);
        registerButton.setOnClickListener(v -> {
            if (isUserOldEnough()) {
                // Proceder con el registro
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Debes tener al menos 18 años para registrarte", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para obtener la ubicación
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    locationText.setText("Latitud: " + latitude + ", Longitud: " + longitude);
                } else {
                    Toast.makeText(Register_user.this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Método para mostrar el selector de fecha de nacimiento
    private void showDatePicker() {
        // Obtener la fecha actual
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            this.year = year;
            this.month = monthOfYear;
            this.day = dayOfMonth;

            // Establecer la fecha seleccionada en el TextView
            dateOfBirthText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

            // Verificar si el usuario es mayor de 18 años
            if (!isUserOldEnough()) {
                // Mostrar mensaje si el usuario es menor de 18 años
                Toast.makeText(Register_user.this, "Debe ser mayor de edad para registrarse", Toast.LENGTH_SHORT).show();
            }
        }, year, month, day);
        datePickerDialog.show();
    }


    // Método para verificar si el usuario es mayor de 18 años
    private boolean isUserOldEnough() {
        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();
        birthDate.set(year, month, day);

        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age >= 18;
    }
}
