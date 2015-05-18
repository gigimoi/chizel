package com.gavingassmann.chizel;

import static com.gavingassmann.chizel.DrawHelper.scale;
import static com.gavingassmann.chizel.DrawHelper.translate;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Gavin Gassmann
 */
public class BlockGroup implements IDrawable, IUpdatable{
    public Block[][] blocks = new Block[5][5];
    public Block[][] rotatedBlocks = new Block[5][5];
    public float angle = 0f;
    public boolean isRotating = false;
    @Override
    public void draw() {
        scale(0.9);
        translate(2.5, 2.5);
        glRotated(-angle, 0, 0, 1);
        for(int i = 0; i < blocks.length; i++) {
            for(int j = 0; j < blocks[i].length; j++) {
                glPushMatrix();
                translate(i - 2.5, j - 2.5);
                if(blocks[i][j] == null) {
                    blocks[i][j] = new Block();
                    rotatedBlocks[i][j] = blocks[i][j];
                }
                blocks[i][j].draw();
                glPopMatrix();
            }
        }
    }

    Block[][] getRotated(Block[][] matrix) {
        Block[][] rotated = new Block[5][5];

        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                rotated[i][j] = matrix[5 - j - 1][i];
            }
        }
        return rotated;
    }

    @Override
    public void update() {
        if(isRotating) {
            angle += 90f / 45f;
            if(angle % 90 == 0) {
                isRotating = false;
                rotatedBlocks = getRotated(rotatedBlocks);
            }
        }
    }

    public Pair<Block, Integer> getBlock(int block, int direction) {
        if (block < 0) {
            return null;
        }
        if (block > 4) {
            return null;
        }
        if (direction == 1) {
            int depth = 0;
            Pair<Block, Integer> foundBlock = new Pair<Block, Integer>(rotatedBlocks[block][depth], depth);
            while (depth < 5 && foundBlock.getKey().broken) {
                foundBlock = new Pair<Block, Integer>(rotatedBlocks[block][depth], depth);
                depth++;
            }
            if (foundBlock.getKey().broken) {
                foundBlock = new Pair<Block, Integer>(rotatedBlocks[block][depth - 1], depth + 2);
            }
            return foundBlock;
        }
        return null;
    }
}
