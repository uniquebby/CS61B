import static org.junit.Assert.*;

import org.junit.Test;

public class ArrayDequeTest {
   @Test
   public void testAdd() {
      ArrayDeque<Integer> que = new ArrayDeque<>();
      for (int i = 1; i < 100; ++i) {
         que.addFirst(i);
         que.addLast(i);
      }
      assertEquals(198, que.size());
      assertEquals(Integer.valueOf(99), que.get(0));

      for (int i = 0; i < 195; ++i) {
         que.removlFirst();
      }
      assertEquals(8, que.maxSize());
   }
}
