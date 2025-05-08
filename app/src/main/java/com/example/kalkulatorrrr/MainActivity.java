package com.example.kalkulatorrrr;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null && sensorEventListener != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    SensorManager sensorManager;
    Sensor accelerometerSensor, gyroscopeSensor;
    SensorEventListener sensorEventListener;

    private void bukaKamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        EditText angka1, angka2;
        Button tambah, kurang, kali, bagi;
        TextView hasil;

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        angka1 = (EditText) findViewById(R.id.angka1);
        angka2 = (EditText) findViewById(R.id.angka2);

        tambah = (Button)  findViewById(R.id.tambah);
        kurang = (Button)  findViewById(R.id.kurang);
        kali = (Button)  findViewById(R.id.kali);
        bagi = (Button)  findViewById(R.id.bagi);


        hasil = (TextView) findViewById(R.id.hasil);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((angka1.getText().length()>0) && (angka2.getText().length()>0)){
                    double angka_pertama = Double.parseDouble(angka1.getText().toString());
                    double angka_kedua = Double.parseDouble(angka2.getText().toString());
                    double result = angka_pertama + angka_kedua;
                    hasil.setText(Double.toString(result));
                    if (result == 8) {
                        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

                        sensorEventListener = new SensorEventListener() {
                            @Override
                            public void onSensorChanged(SensorEvent event) {
                                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                                    float ax = event.values[0];
                                    float ay = event.values[1];
                                    float az = event.values[2];
                                    Log.d("ACCELEROMETER", "x: " + ax + ", y: " + ay + ", z: " + az);
                                } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                                    float gx = event.values[0];
                                    float gy = event.values[1];
                                    float gz = event.values[2];
                                    Log.d("GYROSCOPE", "x: " + gx + ", y: " + gy + ", z: " + gz);
                                }
                            }

                            @Override
                            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
                        };

                        // Daftarkan sensor listener
                        if (accelerometerSensor != null)
                            sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

                        if (gyroscopeSensor != null)
                            sensorManager.registerListener(sensorEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);

                        Toast.makeText(MainActivity.this, "Sensor aktif", Toast.LENGTH_SHORT).show();
                    }
                    if (result == 1) {
                        // Buka Google Maps di lokasi tertentu (misalnya Monas, Jakarta)
                        Uri gmmIntentUri = Uri.parse("geo:-6.1754,106.8272?q=Monas");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        } else {
                            Toast.makeText(MainActivity.this, "Google Maps tidak tersedia", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (result == 7) {
                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageResource(R.drawable.gambar12); //
                    } else {
                        // Menyembunyikan gambar jika hasil tidak sama dengan 7
                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setVisibility(View.GONE);
                    }
                    if (result == 3) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.lagu);
                        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                            Toast.makeText(MainActivity.this, "Error memutar audio", Toast.LENGTH_SHORT).show();
                            return true;
                        });
                        mediaPlayer.start();
                    }
                    if (result == 0) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                        } else {
                            // Kamera sudah diizinkan, bisa langsung buka kamera
                            bukaKamera();
                        }
                    }

                }else{
                    Toast toast = Toast.makeText(MainActivity.this, "Mohon masukan angka pertama dan angka kedua", Toast.LENGTH_LONG);
                    toast.show();

                }
            }
        });

        kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((angka1.getText().length()>0) && (angka2.getText().length()>0)){
                    double angka_pertama = Double.parseDouble(angka1.getText().toString());
                    double angka_kedua = Double.parseDouble(angka2.getText().toString());
                    double result = angka_pertama - angka_kedua;
                    hasil.setText(Double.toString(result));
                    if (result == 8) {
                        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

                        sensorEventListener = new SensorEventListener() {
                            @Override
                            public void onSensorChanged(SensorEvent event) {
                                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                                    float ax = event.values[0];
                                    float ay = event.values[1];
                                    float az = event.values[2];
                                    Log.d("ACCELEROMETER", "x: " + ax + ", y: " + ay + ", z: " + az);
                                } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                                    float gx = event.values[0];
                                    float gy = event.values[1];
                                    float gz = event.values[2];
                                    Log.d("GYROSCOPE", "x: " + gx + ", y: " + gy + ", z: " + gz);
                                }
                            }

                            @Override
                            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
                        };

                        // Daftarkan sensor listener
                        if (accelerometerSensor != null)
                            sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

                        if (gyroscopeSensor != null)
                            sensorManager.registerListener(sensorEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);

                        Toast.makeText(MainActivity.this, "Sensor aktif", Toast.LENGTH_SHORT).show();
                    }
                    if (result == 1) {
                        // Buka Google Maps di lokasi tertentu (misalnya Monas, Jakarta)
                        Uri gmmIntentUri = Uri.parse("geo:-6.1754,106.8272?q=Monas");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        } else {
                            Toast.makeText(MainActivity.this, "Google Maps tidak tersedia", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (result == 7) {
                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageResource(R.drawable.gambar12); //
                    } else {
                        // Menyembunyikan gambar jika hasil tidak sama dengan 7
                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setVisibility(View.GONE);
                    }
                    if (result == 3) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.lagu);
                        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                            Toast.makeText(MainActivity.this, "Error memutar audio", Toast.LENGTH_SHORT).show();
                            return true;
                        });
                        mediaPlayer.start();
                    }
                    if (result == 0) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                        } else {
                            // Kamera sudah diizinkan, bisa langsung buka kamera
                            bukaKamera();
                        }
                    }

                }else{
                    Toast toast = Toast.makeText(MainActivity.this, "Mohon masukan angka pertama dan angka kedua", Toast.LENGTH_LONG);
                    toast.show();

                }
            }
        });

        kali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((angka1.getText().length()>0) && (angka2.getText().length()>0)){
                    double angka_pertama = Double.parseDouble(angka1.getText().toString());
                    double angka_kedua = Double.parseDouble(angka2.getText().toString());
                    double result = angka_pertama * angka_kedua;
                    hasil.setText(Double.toString(result));
                    if (result == 8) {
                        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

                        sensorEventListener = new SensorEventListener() {
                            @Override
                            public void onSensorChanged(SensorEvent event) {
                                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                                    float ax = event.values[0];
                                    float ay = event.values[1];
                                    float az = event.values[2];
                                    Log.d("ACCELEROMETER", "x: " + ax + ", y: " + ay + ", z: " + az);
                                } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                                    float gx = event.values[0];
                                    float gy = event.values[1];
                                    float gz = event.values[2];
                                    Log.d("GYROSCOPE", "x: " + gx + ", y: " + gy + ", z: " + gz);
                                }
                            }

                            @Override
                            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
                        };

                        // Daftarkan sensor listener
                        if (accelerometerSensor != null)
                            sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

                        if (gyroscopeSensor != null)
                            sensorManager.registerListener(sensorEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);

                        Toast.makeText(MainActivity.this, "Sensor aktif", Toast.LENGTH_SHORT).show();
                    }
                    if (result == 1) {
                        // Buka Google Maps di lokasi tertentu (misalnya Monas, Jakarta)
                        Uri gmmIntentUri = Uri.parse("geo:-6.1754,106.8272?q=Monas");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        } else {
                            Toast.makeText(MainActivity.this, "Google Maps tidak tersedia", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (result == 7) {
                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageResource(R.drawable.gambar12); //
                    } else {
                        // Menyembunyikan gambar jika hasil tidak sama dengan 7
                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setVisibility(View.GONE);
                    }
                    if (result == 3) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.lagu);
                        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                            Toast.makeText(MainActivity.this, "Error memutar audio", Toast.LENGTH_SHORT).show();
                            return true;
                        });
                        mediaPlayer.start();
                    }
                    if (result == 0) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                        } else {
                            // Kamera sudah diizinkan, bisa langsung buka kamera
                            bukaKamera();
                        }
                    }
                }else{
                    Toast toast = Toast.makeText(MainActivity.this, "Mohon masukan angka pertama dan angka kedua", Toast.LENGTH_LONG);
                    toast.show();

                }
            }
        });

        bagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((angka1.getText().length()>0) && (angka2.getText().length()>0)){
                    double angka_pertama = Double.parseDouble(angka1.getText().toString());
                    double angka_kedua = Double.parseDouble(angka2.getText().toString());
                    double result = angka_pertama / angka_kedua;
                    hasil.setText(Double.toString(result));
                    if (result == 8) {
                        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

                        sensorEventListener = new SensorEventListener() {
                            @Override
                            public void onSensorChanged(SensorEvent event) {
                                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                                    float ax = event.values[0];
                                    float ay = event.values[1];
                                    float az = event.values[2];
                                    Log.d("ACCELEROMETER", "x: " + ax + ", y: " + ay + ", z: " + az);
                                } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                                    float gx = event.values[0];
                                    float gy = event.values[1];
                                    float gz = event.values[2];
                                    Log.d("GYROSCOPE", "x: " + gx + ", y: " + gy + ", z: " + gz);
                                }
                            }

                            @Override
                            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
                        };

                        // Daftarkan sensor listener
                        if (accelerometerSensor != null)
                            sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

                        if (gyroscopeSensor != null)
                            sensorManager.registerListener(sensorEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);

                        Toast.makeText(MainActivity.this, "Sensor aktif", Toast.LENGTH_SHORT).show();
                    }
                    if (result == 1) {
                        // Buka Google Maps di lokasi tertentu (misalnya Monas, Jakarta)
                        Uri gmmIntentUri = Uri.parse("geo:-6.1754,106.8272?q=Monas");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        } else {
                            Toast.makeText(MainActivity.this, "Google Maps tidak tersedia", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (result == 7) {
                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageResource(R.drawable.gambar12); //
                    } else {
                        // Menyembunyikan gambar jika hasil tidak sama dengan 7
                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setVisibility(View.GONE);
                    }
                    if (result == 3) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.lagu);
                        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                            Toast.makeText(MainActivity.this, "Error memutar audio", Toast.LENGTH_SHORT).show();
                            return true;
                        });
                        mediaPlayer.start();
                    }
                    if (result == 0) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                        } else {
                            // Kamera sudah diizinkan, bisa langsung buka kamera
                            bukaKamera();
                        }
                    }
                }else{
                    Toast toast = Toast.makeText(MainActivity.this, "Mohon masukan angka pertama dan angka kedua", Toast.LENGTH_LONG);
                    toast.show();

                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}