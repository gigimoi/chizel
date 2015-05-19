package com.gavingassmann.chizel;


import static com.gavingassmann.chizel.DrawHelper.*;
/**
 * Created by Gavin Gassmann
 */
public class Laser implements IDrawable {
    public int X;
    public int Y;
    public Laser(int x, int y) {
        this.X = x;
        this.Y = y;
    }
    @Override
    public void draw() {
        translate((float)X / (float)Program.WINDOW_WIDTH * 12f, Y);
        color(150, 150, 150);
        renderSquare();
        scale(0.3, 2);
        color(170, 170, 170);
        renderSquare();
    }
}
