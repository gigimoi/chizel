package com.gavingassmann.chizel;

import java.awt.*;

import static com.gavingassmann.chizel.DrawHelper.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslated;

/**
 * Created by Gassmann844 on 5/15/2015.
 */
public class PanelsTopAndBottom implements IDrawable {
    public boolean red;
    public PanelsTopAndBottom(boolean red) {
        this.red = red;
    }
    @Override
    public void draw() {
        glPushMatrix();
        color(60, 60, 60);
        translate(0, 9);
        scale(100, 2.1);
        renderSquare();
        glPopMatrix();
        glPushMatrix();
        color(60, 60, 60);
        scale(100, 2.1);
        renderSquare();
        glPopMatrix();
        glPushMatrix();
        color(90, 90, 90);
        glPushMatrix();
        for(int i = 0; i < 2; i++) {
            glPushMatrix();
            for(int j = 0; j < 20; j++) {
                line(0, 1, 0, 1, 5f);
                line(0, 1, 1, 0, 5f);
                translate(1, 0);
            }
            glPopMatrix();
            line(0, 100, 1, 1, 5f);
            line(0, 100, 0, 0, 5f);
            translate(0, 8);
        }
        glPopMatrix();
        if(red) {
            glPushMatrix();
            scale(1000);
            color(255, 10, 10, 100);
            renderSquare();
            glPopMatrix();
        }
    }
}
