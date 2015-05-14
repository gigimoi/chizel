package com.gavingassmann.chizel;

import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;
import java.util.Random;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Created by Gassmann844 on 2/19/2015.
 */
public class Program {

    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback cursorPosCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;

    private static Random _r = new Random();
    public static final int WINDOW_WIDTH = 420*2;
    public static final int WINDOW_HEIGHT = 315*2;
    public static final String WINDOW_TITLE = "Chizel";
    public float[] bars = new float[16];

    public static int MouseX;
    public static int MouseY;
    public int redSquareX;
    public int redSquareY;
    public static float ticker = 0;

    private long window;

    public void run() {
        System.out.println(Sys.getVersion());

        try {
            init();
            loop();
            glfwDestroyWindow(window);
            keyCallback.release();
            cursorPosCallback.release();
        } finally {
            glfwTerminate();
            errorCallback.release();
        }
    }

    private void init() {
        for(int i = 0; i < bars.length; i++) {
            bars[i] = _r.nextFloat() * - 20f;
        }
        redSquareX = _r.nextInt(8);
        redSquareY = _r.nextInt(6);
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
        if ( glfwInit() != GL11.GL_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, GL_TRUE);
                if(key == GLFW_KEY_SPACE && action == GLFW_PRESS && !blocks.isRotating) {
                    blocks.isRotating = true;
                }
            }
        });
        glfwSetMouseButtonCallback(window, mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if(button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                    shootLaser();
                }
            }
        });
        glfwSetCursorPosCallback(window, cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double x, double y) {
                MouseY = (int) y;
                MouseX = (int) x;
            }
        });
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
                window,
                (GLFWvidmode.width(vidmode) - WINDOW_WIDTH) / 2,
                (GLFWvidmode.height(vidmode) - WINDOW_HEIGHT) / 2
        );
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        GLContext.createFromCurrent();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glOrtho(0, 12, 0, 9, 0, 1);
    }
    private int score = 0;
    public BlockGroup blocks = new BlockGroup();
    private void loop() {
        while ( glfwWindowShouldClose(window) == GL_FALSE ) {
            update();
            draw();
            glfwPollEvents();
        }
    }
    int LaserX = 0;
    public void update() {
        if(!shootingLaser) {
            LaserX = Math.min(800, Math.max(MouseX, 120));
        }
        blocks.update();
    }
    boolean shootingLaser = false;
    void shootLaser() {
        shootingLaser = true;
    }
    public void draw() {
        clear();
        drawBorders();
        glPushMatrix();
        DrawHelper.translate(7, 2.25);
        blocks.draw();
        glPopMatrix();
        glPushMatrix();
        DrawHelper.scale(0.2);
        float oldAngle = blocks.angle;
        blocks.angle = 0;
        blocks.draw();
        blocks.angle = oldAngle;
        glPopMatrix();
        glPushMatrix();
        new Laser(LaserX, 0).draw();
        glPopMatrix();
        glPushMatrix();
        new LaserShot().draw();
        glPopMatrix();
        glfwSwapBuffers(window);
    }
    public void drawBorders() {
        glPushMatrix();
        DrawHelper.color(60, 60, 60);
        DrawHelper.scale(100, 2.1);
        DrawHelper.renderSquare();
        glPopMatrix();
        glPushMatrix();
        DrawHelper.color(60, 60, 60);
        DrawHelper.translate(0, 9);
        DrawHelper.scale(100, 2.1);
        DrawHelper.renderSquare();
        glPopMatrix();
    }
    public static void main(String[] args) {
        new Program().run();
    }
    private void clear() {
        glPushMatrix();
        DrawHelper.color(0, 0, 0);
        DrawHelper.scale(1000);
        DrawHelper.renderSquare();
        glPopMatrix();
    }
}
