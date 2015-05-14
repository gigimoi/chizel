package com.gavingassmann.chizel;

import static org.lwjgl.opengl.GL11.*;
import static com.gavingassmann.chizel.DrawHelper.*;

/**
 * Created by Gassmann844 on 5/12/2015.
 */
public class BlockGroup implements IDrawable {
    public Block[][] blocks = new Block[5][5];
    public float angle = 0f;
    @Override
    public void draw() {
        angle += 1;
        scale(0.9);
        translate(2.5, 2.5);
        glRotated(angle, 0, 0, 1);
        for(int i = 0; i < blocks.length; i++) {
            for(int j = 0; j < blocks[i].length; j++) {
                glPushMatrix();
                translate(i - 2.5, j - 2.5);
                if(blocks[i][j] == null) {
                    blocks[i][j] = new Block();
                }
                blocks[i][j].draw();
                glPopMatrix();
            }
        }
    }
}
