package com.gavingassmann.chizel;

import static com.gavingassmann.chizel.DrawHelper.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Gassmann844 on 5/15/2015.
 */
public class MainMenu implements IDrawable, IUpdatable {
    public MainMenu() {

    }
    @Override
    public void draw() {
        glPushMatrix();
        color(190, 190, 220);
        scale(100);
        renderSquare();
        glPopMatrix();
        glPushMatrix();
        translate(3f, 7f);
        scale(3);
        text("Chizel");
        glPopMatrix();
        glPushMatrix();
        translate(6f, 4f);
        scale(1.1);
        text("[Enter]");
        glPopMatrix();
        glPushMatrix();
        translate(text("LClick + RClick        Bar to turn"), 0);
        glPopMatrix();
    }
    @Override
    public void update() {
    }
}
