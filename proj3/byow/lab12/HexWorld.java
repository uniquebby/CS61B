package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    public Object[][] addHexagon(int size, Object o) {
        int x = size << 1, y = 3 * size - 2;
        Object[][] hexagon = new Object[x][y];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < y; ++j) {
                if (j > size - 2 - i && j < 2 * size - 1 + i) {
                    hexagon[i][j] = o;
                } else {
                    hexagon[i][j] = ' ';
                }
            }
        }
        for (int k = size; k < x; ++k) {
            hexagon[k] = hexagon[x - k - 1];
        }
        return hexagon;
    }

    public void printHex(Object[][] hex) {
        for (int i = 0; i < hex.length; ++i) {
            for (int j = 0; j < hex[i].length; ++j) {
                System.out.print(hex[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        HexWorld hex6 = new HexWorld();
        Object[][] a6 = hex6.addHexagon(6, 'a');
        hex6.printHex(a6);
    }

}


