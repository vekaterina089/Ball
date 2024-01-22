package com.example.ball;

public class Circle {
    private float x;
    private float y;
    private int color;
    private int score;
    public Circle(float x, float y, int color, int score) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.score = score;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public int getColor() {
        return color;
    }
        public int getScore() {
        return score;
    }
}
