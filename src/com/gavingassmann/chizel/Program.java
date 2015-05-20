package com.gavingassmann.chizel;

import javafx.util.Pair;
import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import java.nio.ByteBuffer;
import java.util.Random;

import static com.gavingassmann.chizel.DrawHelper.scale;
import static com.gavingassmann.chizel.DrawHelper.translate;
import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by Gavin Gassmann
 */
public class Program {

    public static final int WINDOW_WIDTH = 420*2;
    public static final int WINDOW_HEIGHT = 315*2;
    public static final String WINDOW_TITLE = "Chizel";

    public static int MouseX;
    public static int MouseY;

    private static Random _r = new Random();

    public BlockGroup blocks = new BlockGroup();
    public BlockGroup targetBlocks;

    int LaserX = 0;
    float laserTicker = 0;

    boolean shootingLaser = false;
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback cursorPosCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private long window;

    public static void main(String[] args) {
        //</skynet>
        //Safety first
        new Program().run();
    }

    public void run() {
        System.out.println(Sys.getVersion());

        try {
            init();
            loop();
            glfwDestroyWindow(window);
            keyCallback.release();
            cursorPosCallback.release();
            mouseButtonCallback.release();
        } finally {
            glfwTerminate();
            errorCallback.release();
        }
    }

    private void init() {
        Patterns.init();
        targetBlocks = Patterns.getGroup(0);

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
                    laserTicker = 10f;
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

    private void loop() {
        while ( glfwWindowShouldClose(window) == GL_FALSE ) {
            update();
            draw();
            glfwPollEvents();
        }
    }
    float offset = 0;
    public void update() {
        if (laserTicker > 0) {
            laserTicker -= 1f;
        } else {
            laserTicker = 0;
            shootingLaser = false;
        }
        if(!shootingLaser) {
            LaserX = Math.min(800, Math.max(MouseX, 120));
            offset -= 0.006f;
        }
        blocks.update();
    }

    void shootLaser() {
        shootingLaser = true;
    }

    public void draw() {
        clear();
        drawBorders();
        glPushMatrix();
        translate(7, 2.25);
        translate(offset, 0);
        blocks.draw();
        glPopMatrix();
        glPushMatrix();
        scale(0.2);
        translate(0.3, 0.3);
        targetBlocks.draw();
        glPopMatrix();
        glPushMatrix();
        translate(0, 2.25);
        scale(0.9);
        translate(0, 0.5);
        for(int i = 0; i < 5; i++) {
            glPushMatrix();
            new ConveyorReader(i, "check").draw();
            glPopMatrix();
        }
        glPopMatrix();
        glPushMatrix();
        new Laser(LaserX, 0).draw();
        glPopMatrix();
        glPushMatrix();
        if(shootingLaser) {
            int block = Math.round((LaserX - 488f - 32 - (offset * 420 * 2 / 12)) / 64f); //This line works through pixie magic, do not tough
            Pair<Block, Integer> hitBlock = blocks.getBlock(block, 1);
            if (hitBlock != null) {
                if (laserTicker == 1) {
                    hitBlock.getKey().broken = true;
                }
                new LaserShot(LaserX / (float) WINDOW_WIDTH * 12f, 1, hitBlock.getValue() + 1).draw();
            }
        }
        glPopMatrix();
        glfwSwapBuffers(window);
    }

    public void drawBorders() {
        glPushMatrix();
        DrawHelper.color(60, 60, 60);
        scale(100, 2.1);
        DrawHelper.renderSquare();
        glPopMatrix();
        glPushMatrix();
        DrawHelper.color(60, 60, 60);
        translate(0, 9);
        scale(100, 2.1);
        DrawHelper.renderSquare();
        glPopMatrix();
    }

    private void clear() {
        glPushMatrix();
        DrawHelper.color(0, 0, 0);
        scale(1000);
        DrawHelper.renderSquare();
        glPopMatrix();
    }
}
