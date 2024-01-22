package com.example.ball;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.util.AttributeSet;

public class CircleSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static int MAX_CIRCLES = 10;
    private static final int CIRCLE_RADIUS = 50;
    private int currentLevel;
    private static final int[] COLORS = {
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.YELLOW,
            Color.CYAN
    };
    private static final long[] LEVEL_TIMES = {60000, 50000, 40000, 30000, 20000}; // Время уровней в миллисекундах
    private static final int[] LEVEL_MAX_CIRCLES = {10, 12, 15, 18, 20}; // Максимальное количество шариков на уровнях
    private static final int[][] LEVEL_SCORES = {
            {1, 2, 3, 4, 5}, // Уровень 1: Ценность шариков
            {2, 3, 4, 5, 6}, // Уровень 2: Ценность шариков
            {3, 4, 5, 6, 7}, // Уровень 3: Ценность шариков
            {4, 5, 6, 7, 8}, // Уровень 4: Ценность шариков
            {5, 6, 7, 8, 9}  // Уровень 5: Ценность шариков
    };
    private static int[] SCORES = {1, 2, 3, 4, 5};

    private Paint paint;
    private Random random;
    private List<Circle> circles;
    private int score;
    private CountDownTimer timer;
    private long timeLeft;

    public CircleSurfaceView(Context context) {
        super(context);
        init();
    }



    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        random = new Random();
        circles = new ArrayList<>();
        score = 0;
        timeLeft = 60000; // Измените это значение на нужную продолжительность игры в миллисекундах
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Начать отрисовку когда surface создан
        if (timeLeft <= 0) {
            restartGame();
        } else {
            startGame();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Реагировать на изменения размеров surface
        drawCircles();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Остановить отрисовку когда surface разрушен
        stopGame();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float touchX = event.getX();
            float touchY = event.getY();

            Iterator<Circle> iterator = circles.iterator();
            while (iterator.hasNext()) {
                Circle circle = iterator.next();
                if (isCircleTouched(circle, touchX, touchY)) {
                    iterator.remove();
                    increaseScore(circle.getScore());
                }
            }

            // Создание нового шарика, если возможно
            if (circles.size() < MAX_CIRCLES) {
                float newX = random.nextFloat() * getWidth();
                float newY = random.nextFloat() * getHeight();
                int newColor = COLORS[random.nextInt(COLORS.length)];
                int newScore = SCORES[random.nextInt(SCORES.length)];
                Circle newCircle = new Circle(newX, newY, newColor, newScore);
                circles.add(newCircle);
            }

            drawCircles();
        }
        if (timeLeft <= 0) {
           restartGame(); // Начало новой игры
        }

        drawCircles();
        return true;
    }

    private boolean isCircleTouched(Circle circle, float touchX, float touchY) {
        float dx = circle.getX() - touchX;
        float dy = circle.getY() - touchY;
        float distanceSquared = dx * dx + dy * dy;
        return distanceSquared <= (CIRCLE_RADIUS * CIRCLE_RADIUS);
    }

    private void increaseScore(int circleScore) {
        score += circleScore;
        // Дополнительные действия, если требуется
    }

    private void createCircles() {

            if (circles.size() >= MAX_CIRCLES) {
                return;
            }

        circles.clear();
        for (int i = 0; i < MAX_CIRCLES; i++) {
            float x = random.nextFloat() * getWidth();
            float y = random.nextFloat() * getHeight();
            int color = COLORS[random.nextInt(COLORS.length)];
            int score = SCORES[random.nextInt(SCORES.length)];
            Circle circle = new Circle(x, y, color, score);
            circles.add(circle);
        }
    }

    private void drawCircles() {
        Canvas canvas = getHolder().lockCanvas();
        if (canvas != null) {
            canvas.drawColor(Color.WHITE); // Очищаем холст

            for (Circle circle : circles) {
                paint.setColor(circle.getColor());
                canvas.drawCircle(circle.getX(), circle.getY(), CIRCLE_RADIUS, paint);
            }
            // Вывод количества очков
            paint.setColor(Color.BLACK);
            paint.setTextSize(50);
            canvas.drawText("Score: " + score, 50, 50, paint);
            // Вывод оставшегося времени
            paint.setColor(Color.RED);
            paint.setTextSize(50);
            canvas.drawText("Time: " + (timeLeft / 1000) + "s", canvas.getWidth() - 300, 50, paint);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void startGame() {
        createCircles();

        timer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                drawCircles();
            }

            @Override
            public void onFinish() {
                currentLevel++;
                if (currentLevel < LEVEL_TIMES.length) {
                    setupLevel(currentLevel);
                    startGame(); // Начинаем следующий уровень
                } else {
                    restartGame(); // Перезапускаем игру после завершения всех уровней
                }
            }
        }.start();
    }

    private void stopGame() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void onPause() {
        stopGame();
    }

    public void onResume() {
        startGame();
    }
    private void restartGame() {
        currentLevel = 0;
        score = 0;
        timeLeft = 60000;
        setupLevel(currentLevel);
        createCircles();
        startGame();
    }
    private void setupLevel(int level) {
        if (level >= LEVEL_TIMES.length) {
            level = LEVEL_TIMES.length - 1;
        }
        timeLeft = LEVEL_TIMES[level];
        MAX_CIRCLES = LEVEL_MAX_CIRCLES[level];
        SCORES = LEVEL_SCORES[level];
    }

}