package com.gavingassmann.chizel;

import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Random;

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
    public float[] bars = new float[16];
    public int redSquareX;
    public int redSquareY;
    public BlockGroup blocks = new BlockGroup();

    float laserBottomX = 0;
    float laserTopX = 0;
    float laserBottomTicker = 0;
    float laserTopTicker = 0;
    boolean shootingBottomLaser = false;
    boolean shootingTopLaser = false;

    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback cursorPosCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private long window;

    static MainMenu menu = new MainMenu();
    public static void main(String[] args) {
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
        long monitor = glfwGetPrimaryMonitor();
        if(false) {
            window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, WINDOW_TITLE, NULL, NULL);
        } else {
            IntBuffer wid = IntBuffer.allocate(100);
            IntBuffer hei = IntBuffer.allocate(100);
            glfwGetMonitorPhysicalSize(monitor, wid, hei);
            System.out.println(wid.get());
            window = glfwCreateWindow(wid.get(), hei.get(), WINDOW_TITLE, monitor, NULL);
        }

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
                if(key == GLFW_KEY_ENTER && action == GLFW_RELEASE) {
                    menu = null;
                }
            }
        });
        glfwSetMouseButtonCallback(window, mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if(laserBottomTicker == 0 && button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                    shootBottomLaser();
                    laserBottomTicker = 10f;
                }
                if(laserTopTicker == 0 && button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS) {
                    shootTopLaser();
                    laserTopTicker = 10f;
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

    public void update() {
        if(menu != null) {
            menu.update();
            return;
        }
        if (laserBottomTicker > 0) {
            laserBottomTicker -= 1f;
        } else {
            laserBottomTicker = 0;
            shootingBottomLaser = false;
        }
        if(!shootingBottomLaser) {
            laserBottomX = Math.min(800, Math.max(MouseX, 120));
        }

        if (laserTopTicker > 0) {
            laserTopTicker -= 1f;
        } else {
            laserTopTicker = 0;
            shootingTopLaser = false;
        }
        if(!shootingTopLaser) {
            laserTopX = Math.min(800, Math.max(MouseX, 120));
        }
        blocks.update();
    }

    void shootBottomLaser() {
        shootingBottomLaser = true;
    }
    void shootTopLaser() {
        shootingTopLaser = true;
    }

    public void draw() {
        clear();
        if(menu != null) {
            glPushMatrix();
            menu.draw();
            glPopMatrix();
            glfwSwapBuffers(window);
            return;
        }
        drawBorders();
        glPushMatrix();
        translate(7, 2.25);
        blocks.draw();
        glPopMatrix();
        glPushMatrix();
        DrawHelper.scale(0.2);
        float oldAngle = blocks.angle;
        blocks.angle = 0;
        translate(0.4, 0.4);
        blocks.draw();
        blocks.angle = oldAngle;
        glPopMatrix();
        glPushMatrix();
        new Laser((int)laserBottomX, 0).draw();
        glPopMatrix();
        glPushMatrix();
        new Laser((int)laserTopX, 9).draw();
        glPopMatrix();
        glPushMatrix();
        if(shootingBottomLaser) {
            int block = Math.round((laserBottomX - 488f - 32) / 64f);
            Pair<Block, Integer> hitBlock = blocks.getBlock(block, 1);
            if (hitBlock != null) {
                if (laserBottomTicker == 1) {
                    hitBlock.getKey().broken = true;
                }
                glPushMatrix();
                new LaserShot(laserBottomX / (float) WINDOW_WIDTH * 12f, 1, hitBlock.getValue() + 1).draw();
                glPopMatrix();
            }
        }
        if(shootingTopLaser) {
            int block = Math.round((laserTopX - 488f - 32) / 64f);
            Pair<Block, Integer> hitBlock = blocks.getBlock(block, -1);
            if (hitBlock != null) {
                if (laserTopTicker == 1) {
                    hitBlock.getKey().broken = true;
                }
                glPushMatrix();
                translate(0, hitBlock.getValue() + 2);
                new LaserShot(laserBottomX / (float) WINDOW_WIDTH * 12f, 1, 5f - hitBlock.getValue()).draw();
                glPopMatrix();
            }
        }
        glPopMatrix();
        glfwSwapBuffers(window);
    }

    public void drawBorders() {
        glPushMatrix();
        new PanelsTopAndBottom().draw();
        glPopMatrix();
    }

    private void clear() {
        glPushMatrix();
        DrawHelper.color(0, 0, 0);
        DrawHelper.scale(1000);
        DrawHelper.renderSquare();
        glPopMatrix();
    }
}
