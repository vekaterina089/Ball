package com.example.ball;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


        private CircleSurfaceView circleSurfaceView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Создаем экземпляр CircleSurfaceView и устанавливаем его в качестве представления активности
            circleSurfaceView = new CircleSurfaceView(this);
            setContentView(circleSurfaceView);
        }

        @Override
        protected void onResume() {
            super.onResume();
            // Регистрируем onResume активности
            circleSurfaceView.onResume();
        }

        @Override
        protected void onPause() {
            super.onPause();
            // Регистрируем onPause активности
            circleSurfaceView.onPause();
        }
    }
