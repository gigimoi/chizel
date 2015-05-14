package com.gavingassmann.chizel;

import java.util.Random;

import static com.gavingassmann.chizel.DrawHelper.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScaled;

/**
 * Created by Gassmann844 on 5/12/2015.
 */
public class Block implements IDrawable {
    public boolean broken = false;
    Random _r;
    long seed;
    public Block() {
        _r = new Random();
        seed = _r.nextLong();
    }
    @Override
    public void draw() {
        if(!broken) {
            translate(0.5, 0.5);
            color(255, 20, 20);
            renderSquare();
            scale(0.9);
            color(255, 100, 100);
            renderSquare();
        } else {
            _r.setSeed(seed);
            for(int i = 0; i < _r.nextInt(15) + 10; i++) {
                glPushMatrix();
                translate(0.5, 0.5);
                translate(_r.nextFloat() - 0.5, _r.nextFloat() - 0.5);
                scale(0.05 + _r.nextDouble() / 7f);
                color(255, 20, 20);
                renderSquare();
                scale(0.9);
                color(255, 100, 100);
                renderSquare();
                glPopMatrix();
            }
        }
    }
}
