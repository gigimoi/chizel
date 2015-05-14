package com.gavingassmann.chizel;

import org.lwjgl.opengl.GL11;

import static com.gavingassmann.chizel.DrawHelper.*;

/**
 * Created by Gassmann844 on 5/14/2015.
 */
public class LaserShot implements IDrawable {
    @Override
    public void draw() {
        color(255, 255, 0);
        drawLightning(0, 0, 5, 5, 3f);
        drawLightning(0, 0, 5, 5, 3f);
        drawLightning(0, 0, 5, 5, 3f);
        drawLightning(0, 0, 5, 5, 3f);
        drawLightning(0, 0, 5, 5, 3f);
    }
    void drawLightning(float x1, float y1, float x2, float y2, float displace)
    {
        if (displace < 0.1f) {
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
