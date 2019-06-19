package hw2;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestPercolation {
    Percolation p = new Percolation(10);
    @Test
    public void testIndex() {
        for (int i = 0,index = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j, ++index) {
                assertEquals(i, p.indexTo(index)[0]);
                assertEquals(j, p.indexTo(index)[1]);
                assertEquals(index, p.indexOf(i, j));
            }
        }
    }
}
