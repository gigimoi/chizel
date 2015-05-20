package com.gavingassmann.chizel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gavin
 */
public class Patterns {
    static List<boolean[][]> patterns = new ArrayList<boolean[][]>();
    public static void init() {
        patterns.add(
                getPattern("xxxxx" +
                           "xxxxx" +
                           "xxoxx" +
                           "xxxxx" +
                           "xxxxx")
        );
    }
    public static BlockGroup getGroup(int index) {
        BlockGroup b = new BlockGroup();
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                b.blocks[i][j] = new Block();
                b.blocks[i][j].broken = patterns.get(index)[i][j];
            }
        }
        return b;
    }
    static boolean[][] getPattern(String in) {
        boolean[][] bools = new boolean[5][5];
        in = in.replace("\n", "");
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                bools[j][i] = in.charAt(i + j * 5) == 'x';
            }
        }
        return bools;
    }
}
