package com.gavingassmann.chizel;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Gavin Gassmann
 */
public class DrawHelper {
    public static void translate(double x, double y) {
        glTranslated(x, y, 0);
    }
    public static void scale(double factor) {
        scale(factor, factor);
    }
    public static void scale(double xFactor, double yFactor) {
        glScaled(xFactor, yFactor, 1);
    }
    public static void color(int r, int g, int b) {
        color(r, g, b, 255);
    }
    public static void color(int r, int g, int b, int a) {
        glColor4f(r / 255f, g / 255f, b / 255f, a / 255f);
    }
    public static void line(float x1, float x2, float y1, float y2, float width) {
        GL11.glLineWidth(width);
        glBegin(GL_LINES);
        glVertex2f(x1, y1);
        glVertex2f(x2, y2);
        glEnd();
    }

    /*
    public static void renderStar() {
        glBegin(GL_TRIANGLES);
        glVertex2f(-0.2f, 0);
        glVertex2f(0, 0.5f);
        glVertex2f(0.2f, 0);
        glEnd();
        glBegin(GL_TRIANGLES);
        glVertex2f(-0.2f, 0);
        glVertex2f(0, -0.5f);
        glVertex2f(0.2f, 0);
        glEnd();
        glBegin(GL_TRIANGLES);
        glVertex2f(0f, -0.2f);
        glVertex2f(-0.5f, 0f);
        glVertex2f(0f, 0.2f);
        glEnd();
        glBegin(GL_TRIANGLES);
        glVertex2f(0f, -0.2f);
        glVertex2f(0.5f, 0f);
        glVertex2f(0f, 0.2f);
        glEnd();
    }
    */
    public static void renderSquare() {
        glBegin(GL_QUADS);
        glVertex2f(-0.5f, -0.5f);
        glVertex2f(0.5f, -0.5f);
        glVertex2f(0.5f, 0.5f);
        glVertex2f(-0.5f, 0.5f);
        glEnd();
    }
    public static void text(String text) {
        glPushMatrix();
        scale(0.25);
        for(int i = 0; i < text.length(); i++) {
            translate(text(text.toLowerCase().charAt(i)) + 0.3, 0);
        }
        glPopMatrix();
    }
    private static float text(char text) {
        color(0, 0, 0, 255);
        if(text == 'a') {
            line(0, 0.5f, 0, 1, 2);
            line(0.5f, 1f, 1, 0, 2);
            line(0.25f, 0.75f, 0.4f, 0.4f, 2);
            return 1f;
        }
        if(text == 'b') {
            line(0, 0, 0, 1, 2);
            line(0, 0.8f, 1, 0.75f, 2);
            line(0, 0.8f, 0.5f, 0.75f, 2);
            line(0, 0.8f, 0.5f, 0.25f, 2);
            line(0, 0.8f, 0f, 0.25f, 2);
            return 0.8f;
        }
        if(text == 'c') {
            glPushMatrix();
            scale(0.7f, 1f);
            line(1, 0.25f, 1, 0.8f, 2);
            line(0.25f, 0f, 0.8f, 0.5f, 2);
            line(0.25f, 0f, 0.2f, 0.5f, 2);
            line(1, 0.25f, 0, 0.2f, 2);
            glPopMatrix();
            return 0.7f;
        }
        if(text == 'd') {
            glPushMatrix();
            scale(0.9f, 1f);
            line(0, 0, 0, 1, 2);
            line(0, 0.75f, 1, 0.9f, 2);
            line(0, 0.75f, 0, 0.1f, 2);
            line(0.75f, 1f, 0.1f, 0.2f, 2);
            line(0.75f, 1f, 0.9f, 0.8f, 2);
            line(0.75f, 1f, 0.9f, 0.8f, 2);
            line(1f, 1f, 0.8f, 0.2f, 2);
            glPopMatrix();
            return 0.9f;
        }
        if(text == 'e') {
            line(0, 0, 1, 0, 2);
            line(0, 0.8f, 1, 1, 2);
            line(0, 0.8f, 0.5f, 0.5f, 2);
            line(0, 0.8f, 0, 0, 2);
            return 0.8f;
        }
        if(text == 'f') {
            line(0, 0, 1, 0, 2);
            line(0, 0.8f, 1, 1, 2);
            line(0, 0.8f, 0.5f, 0.5f, 2);
            return 0.8f;
        }
        if(text == 'g') {
            line(0.6f, 0.7f, 1f, 0.8f, 2);
            line(0.1f, 0.6f, 0.9f, 1f, 2);
            line(0, 0.1f, 0.6f, 0.9f, 2);
            line(0, 0, 0.6f, 0.4f, 2);
            line(0, 0.1f, 0.4f, 0.1f, 2);
            line(0.1f, 0.5f, 0.1f, 0f, 2);
            line(0.5f, 0.6f, 0f, 0.1f, 2);
            line(0.6f, 0.7f, 0.1f, 0.3f, 2);
            line(0.5f, 0.7f, 0.3f, 0.3f, 2);
            return 0.7f;
        }
        if(text == 'h') {
            line(0, 0, 0, 1, 2);
            line(0.8f, 0.8f, 0, 1, 2);
            line(0, 0.8f, 0.5f, 0.5f, 2);
            return 0.8f;
        }
        if(text == 'i') {
            glPushMatrix();
            scale(1f, 1.1f);
            glRotated(90, 0, 0, 1);
            translate(0.1f, -1f);
            line(0, 0, 0, 1, 2);
            line(0.8f, 0.8f, 0, 1, 2);
            line(0, 0.8f, 0.5f, 0.5f, 2);
            glPopMatrix();
            return 1f;
        }
        if(text == 'k') {
            line(0, 0, 0, 1, 2);
            line(0, 0.8f, 0.5f, 1f, 2);
            line(0, 0.8f, 0.5f, 0f, 2);
            return 0.8f;
        }
        if(text == 'l') {
            line(0, 0, 0, 1, 2);
            line(0, 0.5f, 0, 0, 2);
            return 0.5f;
        }
        if(text == 'm') {
            line(0, 0, 0, 1, 2);
            line(0, 0.5f, 1, 0, 2);
            line(0.5f, 1f, 0, 1, 2);
            line(1, 1, 0, 1, 2);
            return 1.1f;
        }
        if(text == 'n') {
            line(0, 0, 0, 1, 2);
            line(0, 0.8f, 1, 0, 2);
            line(0.8f, 0.8f, 0, 1, 2);
            return 0.8f;
        }
        if(text == 'p') {
            line(0.6f, 0.65f, 0.55f, 0.6f, 2);
            line(0, 0.6f, 0.5f, 0.55f, 2);
            line(0, 0, 1, 0, 2);
            line(0, 0.6f, 1, 0.95f, 2);
            line(0.6f, 0.65f, 0.95f, 0.8f, 2);
            line(0.65f, 0.65f, 0.8f, 0.6f, 2);
            return 0.7f;
        }
        if(text == 'q') {
            glPushMatrix();
            scale(-1f, 1f);
            translate(-0.65, -0.2f);
            line(0.6f, 0.65f, 0.55f, 0.6f, 2);
            line(0, 0.6f, 0.5f, 0.55f, 2);
            line(0, 0, 1, 0, 2);
            line(0, 0.6f, 1, 0.95f, 2);
            line(0.6f, 0.65f, 0.95f, 0.8f, 2);
            line(0.65f, 0.65f, 0.8f, 0.6f, 2);
            glPopMatrix();
            return 0.7f;
        }
        if(text == 'r') {
            line(0.6f, 0.65f, 0.55f, 0.6f, 2);
            line(0, 0.6f, 0.5f, 0.55f, 2);
            line(0, 0, 1, 0, 2);
            line(0, 0.6f, 1, 0.95f, 2);
            line(0.6f, 0.65f, 0.95f, 0.8f, 2);
            line(0.65f, 0.65f, 0.8f, 0.6f, 2);
            line(0, 0.65f, 0.5f, 0f, 2);
            return 0.7f;
        }
        if(text == 't') {
            line(0, 0.8f, 1, 1, 2);
            line(0.4f, 0.4f, 1, 0, 2);
            return 0.8f;
        }
        if(text == 'u') {
            line(0, 0, 1, 0.1f, 2);
            line(0.8f, 0.8f, 1, 0.1f, 2);
            line(0.8f, 0.7f, 0.1f, 0f, 2);
            line(0f, 0.1f, 0.1f, 0f, 2);
            return 0.8f;
        }
        return 1f;
        /*
        glPushMatrix();
        translate(0.5f, 0.5f);
        renderSquare();
        glPopMatrix();
        return 1f;
        */
    }
}
