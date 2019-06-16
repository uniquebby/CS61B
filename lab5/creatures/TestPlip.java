package creatures;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.awt.Color;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;

/** Tests the plip class
 *  @authr yangbinbin
 */

public class TestPlip {

    @Test
    public void testBasics() {
        Plip p = new Plip(2);
        assertEquals(2, p.energy(), 0.01);
        assertEquals(new Color(99, 255, 76), p.color());
        p.move();
        assertEquals(1.85, p.energy(), 0.01);
        p.move();
        assertEquals(1.70, p.energy(), 0.01);
        p.stay();
        assertEquals(1.90, p.energy(), 0.01);
        p.stay();
        assertEquals(2.00, p.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Plip p = new Plip(1.8);
        Plip offspring = p.replicate();
        assertEquals(p.energy(), offspring.energy(), 0.01);
        assertEquals(p.energy() + offspring.energy(), 1.8, 0.01);
        assertNotEquals(p, offspring);
    }

    @Test
    public void testChoose() {

        // No empty adjacent spaces; stay.
        Plip p = new Plip(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = p.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);


        // Energy >= 1; replicate towards an empty space.
        p = new Plip(1.2);
        HashMap<Direction, Occupant> topEmpty = new HashMap<Direction, Occupant>();
        topEmpty.put(Direction.TOP, new Empty());
        topEmpty.put(Direction.BOTTOM, new Impassible());
        topEmpty.put(Direction.LEFT, new Impassible());
        topEmpty.put(Direction.RIGHT, new Impassible());

        actual = p.chooseAction(topEmpty);
        expected = new Action(Action.ActionType.REPLICATE, Direction.TOP);

        assertEquals(expected, actual);


        // Energy >= 1; replicate towards an empty space.
        p = new Plip(1.2);
        HashMap<Direction, Occupant> allEmpty = new HashMap<Direction, Occupant>();
        allEmpty.put(Direction.TOP, new Empty());
        allEmpty.put(Direction.BOTTOM, new Empty());
        allEmpty.put(Direction.LEFT, new Empty());
        allEmpty.put(Direction.RIGHT, new Empty());

        actual = p.chooseAction(allEmpty);
        Action unexpected = new Action(Action.ActionType.STAY);

        assertNotEquals(unexpected, actual);


        // Energy < 1; stay.
        p = new Plip(.99);

        actual = p.chooseAction(allEmpty);
        expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);


        // Energy < 1; stay.
        p = new Plip(.99);

        actual = p.chooseAction(topEmpty);
        expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);


        // We don't have Cloruses yet, so we can't test behavior for when they are nearby right now.
        // We have Cloruses now. continue test.

        //just replace Impassible at the RIGHT direction of topEmpty with Clorus.
        topEmpty.put(Direction.RIGHT, new Clorus());

        actual = p.chooseAction(topEmpty);
        // MOVE with 50% possible.
        Action expected2 = new Action(Action.ActionType.MOVE, Direction.RIGHT);
        assertTrue(actual.equals(expected) || actual.equals(expected2));
    }

    /**
     * test the randomEntry() in class Creature.
     */
    @Test
    public void testRandomEntry() {
        Deque<Direction> deque = new ArrayDeque<>();
        deque.addFirst(Direction.LEFT);
        assertEquals(Plip.randomEntry(deque), Direction.LEFT);

        //becuase of random, sometimes will pass, sometiems not pass.
        deque.removeFirst();
        deque.addFirst(Direction.RIGHT);
        deque.addFirst(Direction.LEFT);
        deque.addFirst(Direction.RIGHT);
        assertEquals(Plip.randomEntry(deque), Direction.RIGHT);

        deque.removeFirst();
        deque.addFirst(Direction.BOTTOM);
        deque.addFirst(Direction.BOTTOM);
        assertEquals(Plip.randomEntry(deque), Direction.BOTTOM);

        deque.removeFirst();
        deque.addFirst(Direction.TOP);
        deque.addFirst(Direction.TOP);
        assertEquals(Plip.randomEntry(deque), Direction.TOP);
    }
}
