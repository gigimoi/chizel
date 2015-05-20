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
        translate(10.7, 0.4);
        scale(2, 0.9);
        renderSquare();
        glPopMatrix();
        glPushMatrix();
        translate(10, -0.01);
        String scoreText = "";
        for(int i = 0; i < score; i++) {
            scoreText += "l";
            if(scoreText.endsWith("llll")) {
                scoreText = scoreText.substring(0, scoreText.length() - 4) + "v";
            }
        }
        text(scoreText);
        glPopMatrix();
    }
}
