package com.gavingassmann.chizel;

import java.nio.IntBuffer;
import java.util.Random;

import static com.gavingassmann.chizel.DrawHelper.*;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

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

    float laserBottomX = 0;
    float laserTopX = 0;
    float laserBottomTicker = 0;
    float laserTopTicker = 0;
    boolean shootingBottomLaser = false;
    boolean shootingTopLaser = false;

    int score = 0;

    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback cursorPosCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;

    private long window;

    static MainMenu menu = new MainMenu();
    public static void main(String[] args) {
        //</skynet>
        //Safety first
        System.setProperty("java.library.path", "native");
        new Program().run();
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        try {
            init();
            loop();

            // Release window and window callbacks
            glfwDestroyWindow(window);
            keyCallback.release();
        } finally {
            // Terminate GLFW and release the GLFWErrorCallback
            glfwTerminate();
            errorCallback.release();
        }
    }

    private void init() {
        Patterns.init();
        resetGameboard();

        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        if ( glfwInit() != GL11.GL_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
        long monitor = glfwGetPrimaryMonitor();
        if(true) {
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
                if(!isReading() && key == GLFW_KEY_SPACE && action == GLFW_PRESS && !blocks.isRotating) {
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
                if(!isReading()) {
                    if(laserBottomTicker == 0 && button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                        shootBottomLaser();
                        laserBottomTicker = 10f;
                    }
                    if(laserTopTicker == 0 && button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS) {
                        shootTopLaser();
                        laserTopTicker = 10f;
                    }
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
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                window,
                (vidmode.width() - WINDOW_WIDTH) / 2,
                (vidmode.height() - WINDOW_HEIGHT) / 2
        );
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glOrtho(0, 12, 0, 9, 0, 1);
        System.out.println("Initialized in " + Math.round(glfwGetTime() * 1000) + " milliseconds");
    }

    private void loop() {

        float frameTotal = 0f;
        int frameTicker = 0;
        while ( glfwWindowShouldClose(window) == GL_FALSE ) {
            glfwSetTime(0);
            update();
            draw();
            glfwPollEvents();
            frameTicker++;
            frameTotal += glfwGetTime();
            if(frameTicker == 60) {
                System.out.println("Average frame time in milliseconds: " + Math.round(frameTotal / 60f * 1000f));
                frameTicker = 0;
                frameTotal = 0;
            }
        }
    }
    boolean foundBad = false;
    boolean allGood = false;
    int badTicker = 0;
    int goodTicker = 0;
    float offset = 0;
    boolean done = false;
    public void update() {
        if(menu != null) {
            menu.update();
            return;
        }
        if(blocks.equals(Patterns.swastika)) {
            score += 10;
            resetGameboard();
        }
        if(!foundBad) {
            offset -= 0.007f;
            if(blocks.equals(targetBlocks)) {
                done = true;
                offset -= 0.023f;
            }
        } else {
            badTicker++;
            if(badTicker > 60) {
                resetGameboard();
            }
        }
        if (laserTopTicker > 0) {
            laserTopTicker -= 1f;
        } else {
            laserTopTicker = 0;
            shootingTopLaser = false;
        }
        if (laserBottomTicker > 0) {
            laserBottomTicker -= 1f;
        } else {
            laserBottomTicker = 0;
            shootingBottomLaser = false;
        }
        if(!isReading()) {
            if(!shootingBottomLaser) {
                laserBottomX = Math.min(800, Math.max(MouseX, 120));
            }
            if(!shootingTopLaser) {
                laserTopX = Math.min(800, Math.max(MouseX, 120));
            }
        } else {
            if(doneReading()) {
                if(!allGood) {
                    allGood = true;
                } else {
                    if(goodTicker < 60) {
                        goodTicker++;
                        if(goodTicker == 1) {
                            score += 1;
                        }
                    } else {
                        resetGameboard();
                    }
                }
            }
        }
        blocks.update();
    }

    private void resetGameboard() {
        offset = 0;
        foundBad = false;
        allGood = false;
        goodTicker = 0;
        badTicker = 0;
        blocks = new BlockGroup();
        done = false;
        targetBlocks = Patterns.getGroup(_r.nextInt(Patterns.patterns.size()));
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
            int index = -1;
            for(int j = 0; j < 5; j++) {
                if(offset < - 6.55 - (j * 0.9)) {
                    index = j;
                }
            }
            String state = "none";
            if(index >= 0) {
                if(targetBlocks.blocks[index][i].broken == blocks.rotatedBlocks[index][i].broken) {
                    state = "check";
                } else {
                    state = "bad";
                    foundBad = true;
                }
            }
            new ConveyorReader(i, state).draw();
            glPopMatrix();
        }
        glPopMatrix();
        glPushMatrix();
        new Laser((int)laserBottomX, 0).draw();
        glPopMatrix();
        glPushMatrix();
        new Laser((int)laserTopX, 9).draw();
        glPopMatrix();
        glPushMatrix();
        if(shootingBottomLaser) {
            int block = Math.round((laserBottomX - 488f - 32 - (offset * 420 * 2 / 12)) / 64f);
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
            int block = Math.round((laserTopX - 488f - 32 - (offset * 420 * 2 / 12)) / 64f);
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
        glPushMatrix();
        new ScoreUI(score).draw();
        glPopMatrix();
        if(foundBad) {
            glPushMatrix();
            new BigResult(true).draw();
            glPopMatrix();
        }
        if(allGood) {
            glPushMatrix();
            new BigResult(false).draw();
            glPopMatrix();
        }
        glPopMatrix();
        glPushMatrix();
        color(255, 255, 255);
        float x = MouseX / (float)WINDOW_WIDTH * 12f;
        float y = (WINDOW_HEIGHT - MouseY) / (float)WINDOW_HEIGHT * 9f;
        line(x - 0.25f, x + 0.25f, y, y, 6);
        line(x, x, y - 0.25f, y + 0.25f, 6);
        glPopMatrix();
        glfwSwapBuffers(window);
    }
    public boolean isReading() {
        return offset < -6 || done;
    }
    public boolean doneReading() {
        return offset < -6 - 5.5;
    }
    public void drawBorders() {
        glPushMatrix();
        new PanelsTopAndBottom(isReading()).draw();
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
