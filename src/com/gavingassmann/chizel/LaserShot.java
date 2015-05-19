package com.gavingassmann.chizel;

import java.util.Random;

import static com.gavingassmann.chizel.DrawHelper.*;

/**
 * Created by Gavin Gassmann
 */
public class LaserShot implements IDrawable {
    public float x;
    public float y;
    public float distance;
    Random _r = new Random();
    public LaserShot(float x, float y, float distance) {
        this.x = x;
        this.y = y;
        this.distance = distance;
    }
    @Override
    public void draw() {
        translate(x, y);
        float displace = 0.5f;
        for(int i = 0; i < _r.nextFloat() * 20 + 20; i++) {
            displace *= 0.95f;
            int brightness = (int)(_r.nextFloat() * 100f) + 120;
            color(brightness, brightness, 255, (int)(_r.nextFloat() * 155f + 100f));
            drawLightning(
                    _r.nextFloat() / 4f - 0.125f,
                    _r.nextFloat() / 2f,
                    _r.nextFloat() / 4f - 0.125f,
                    distance - 0.15f + _r.nextFloat() / 4f - 0.125f,
                    distance / 5f + displace + _r.nextFloat() / 6f
            );
        }
    }
    void drawLightning(float x1, float y1, float x2, float y2, float displace)
    {
        if (displace < 0.05f) {
            line(x1, x2, y1, y2, (float)Math.random() + 2);
        }
        else {
            float mid_x = (x2 + x1) / 2;
            float mid_y = (y2 + y1) / 2;
            mid_x += (Math.random() - .5) * displace;
            mid_y += (Math.random() - .5) * displace;
            drawLightning(x1, y1, mid_x, mid_y, displace / 2);
            drawLightning(x2, y2, mid_x, mid_y, displace / 2);
        }
    }
}
