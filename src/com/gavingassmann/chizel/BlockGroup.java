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
        int depth = direction == -1 ? 4 : 0;
        Pair<Block, Integer> foundBlock = new Pair<Block, Integer>(rotatedBlocks[block][depth], depth);
        while (((direction == -1 && depth >= 0) || (direction == 1 && depth < 5)) && foundBlock.getKey().broken) {
            foundBlock = new Pair<Block, Integer>(rotatedBlocks[block][depth], depth);
            depth += direction;
        }
        if (foundBlock.getKey().broken) {
            foundBlock = new Pair<Block, Integer>(rotatedBlocks[block][depth - 1 * direction], depth + 2 * direction);
        }
        return foundBlock;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == BlockGroup.class) {
            BlockGroup other = (BlockGroup)obj;
            for(int i = 0; i < 5; i++) {
                for(int j = 0; j < 5; j++) {
                    Block b1 = null;
                    Block b2 = null;
                    if(other.rotatedBlocks[i][j] == null) {
                        b1 = other.blocks[i][j];
                    } else {
                        b1 = other.rotatedBlocks[i][j];
                    }
                    if(rotatedBlocks[i][j] == null) {
                        b2 = blocks[i][j];
                    } else {
                        b2 = rotatedBlocks[i][j];
                    }
                    if(b1 == null || b2 == null || b1.broken != b2.broken) {
                        return false;
                    }
                }
            }
            return true;
        }
        return super.equals(obj);
    }
}
