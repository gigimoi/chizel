package com.gavingassmann.chizel;

import static com.gavingassmann.chizel.DrawHelper.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

/**
 * Created by Gassmann844 on 5/20/2015.
 */
public class ScoreUI implements IDrawable {
    int score;
    public ScoreUI(int score) {
        this.score = score;
    }
    @Override
    public void draw() {
        glPushMatrix();
        color(230, 230, 230);
        translate(0.72, 8.55);
        scale(0.7);
        scale(3.5, 1);
        renderSquare();
        glPopMatrix();
        glPushMatrix();
        translate(0.1, 8.5);
        scale(0.7);
        text("Vim: " + new RomanNumeral().RomanNumerals(score));
        glPopMatrix();
        glPopMatrix();
    }
}
