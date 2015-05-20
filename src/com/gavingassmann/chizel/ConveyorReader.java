package com.gavingassmann.chizel;

import static com.gavingassmann.chizel.DrawHelper.*;

/**
 * Created by Gavin
 */
public class ConveyorReader implements IDrawable {
    boolean check = false;
    boolean bad = false;
    int index;
    public ConveyorReader(int index, String status) {
        this.index = index;
        if(status.equals("check")) {
            check = true;
        }
        if(status.equals("bad")) {
            bad = true;
        }
    }
    @Override
    public void draw() {
        translate(0, index);
        color(200, 200, 200);
        if(check) {
            color(100, 255, 100);
        }
        if(bad) {
            color(240, 70, 70);
        }
        renderSquare();
    }
}
