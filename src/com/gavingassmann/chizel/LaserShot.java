package com.gavingassmann.chizel;

import org.lwjgl.opengl.GL11;

import java.util.Random;

import static com.gavingassmann.chizel.DrawHelper.*;

/**
 * Created by Gassmann844 on 5/14/2015.
 */
public class LaserShot implements IDrawable {
    Random _r = new Random();
    public float x;
    public float y;
    public float distance;
    public LaserShot(float x, float y, float distance) {
        this.x = x;
        this.y = y;
        this.distance = distance;
    }
    @Override
    public void draw() {
        translate(x, y);
        for(int i = 0; i < _r.nextFloat() * 10 + 14; i++) {
            int brightness = (int)(_r.nextFloat() * 100f) + 100;
            color(brightness, brightness, 255, (int)(_r.nextFloat() * 155f + 100f));
            drawLightning(
                    _r.nextFloat() / 4f - 0.125f,
                    _r.nextFloat() / 2f,
                    _r.nextFloat() / 2f - 0.25f,
                    distance - 0.15f + _r.nextFloat() / 4f - 0.125f,
                    1.5f + _r.nextFloat() / 4f);
        }
    }
    void drawLightning(float x1, float y1, float x2, float y2, float displace)
    {
        if (displace < 0.05f) {
            line(x1, x2, y1, y2, (float)Math.random() + 2);
        }
        else {
            float mid_x = (x2+x1)/2;
            float mid_y = (y2+y1)/2;
            mid_x += (Math.random()-.5)*displace;
            mid_y += (Math.random()-.5)*displace;
            drawLightning(x1,y1,mid_x,mid_y,displace/2);
            drawLightning(x2,y2,mid_x,mid_y,displace/2);
        }
    }
}
