package com.gavingassmann.chizel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gavin
 */
public class Patterns {
    static List<boolean[][]> patterns = new ArrayList<boolean[][]>();
    static public BlockGroup swastika;
    public static void init() {
        swastika = getGroup(getPattern("oxooo" +
                "oxoxx" +
                "ooooo" +
                "xxoxo" +
                "oooxo")
        );
        patterns.add(
                getPattern("xxxxx" +
                           "xxoxx" +
                           "xooox" +
                           "xxoxx" +
                           "xxxxx")
        );
        patterns.add(
                getPattern("oxxxo" +
                           "xxoxx" +
                           "oooox" +
                           "xxoxx" +
                           "oxxxo")
        );
        patterns.add(
                getPattern("xoxox" +
                           "ooxoo" +
                           "xxoxx" +
                           "ooxoo" +
                           "xoxox")
        );
        patterns.add(
                getPattern("xoxox" +
                           "ooxoo" +
                           "ooooo" +
                           "xxxxx" +
                           "xoxox")
        );
        patterns.add(
                getPattern("ooxoo" +
                           "ooxoo" +
                           "xxxxx" +
                           "ooxoo" +
                           "xoxox")
        );
        patterns.add(
                getPattern("oxoxo" +
                           "oxoxo" +
                           "xxoxx" +
                           "oxoxo" +
                           "oxoxo")
        );
        patterns.add(
                getPattern("xxoox" +
                           "oooox" +
                           "xxoxx" +
                           "xoooo" +
                           "ooxoo")
        );
    }
    public static BlockGroup getGroup(int index) {
        return getGroup(patterns.get(index));
    }
    public static BlockGroup getGroup(boolean[][] pattern) {
        BlockGroup b = new BlockGroup();
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                b.blocks[i][j] = new Block();
                b.blocks[i][j].broken = pattern[i][j];
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
