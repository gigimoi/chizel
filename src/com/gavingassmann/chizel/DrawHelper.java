package com.gavingassmann.chizel;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Gassmann844 on 5/12/2015.
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
    public static void renderSquare() {
        glBegin(GL_QUADS);
        glVertex2f(-0.5f, -0.5f);
        glVertex2f(0.5f, -0.5f);
        glVertex2f(0.5f, 0.5f);
        glVertex2f(-0.5f, 0.5f);
        glEnd();
    }
    public static void text(String text) {
        for(int i = 0; i < text.length(); i++) {

        }
    }
}
