import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author yangbinbin
 * 2019.06.18
 */
public class TestUnionFind {
    @Test
    public void testUnionFind() {
        UnionFind uf = new UnionFind(10000);
        assertFalse(uf.connected(0, 1));

        uf.union(0, 1);
        assertTrue(uf.connected(0, 1));
        assertEquals(uf.parent(1), 0);
        assertEquals(uf.sizeOf(0), 2);

        for (int i = 2; i < 9999; ++i) {
            uf.union(i - 1, i);
            uf.find(i);
            assertEquals(uf.parent(i), 0);
        }
    }
}
