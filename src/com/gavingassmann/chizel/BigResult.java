package com.gavingassmann.chizel;

import static com.gavingassmann.chizel.DrawHelper.*;

/**
 * Created by Gassmann844 on 5/20/2015.
 */
public class BigResult implements IDrawable {
    boolean bad;
    public BigResult(boolean bad) {
        this.bad = bad;
    }
    @Override
    public void draw() {
        translate(2.5, 0.5);
        scale(7, 8);
        if(bad) {
            color(255, 0, 0);
            line(0, 1, 0, 1, 15);
            line(0, 1, 1, 0, 15);
        } else {
            color(0, 255, 0);
            line(0.3f, 0.8f, 0f, 1f, 15);
            line(0.2f, 0.3f, 0.3f, 0f, 15);
        }
    }
}
