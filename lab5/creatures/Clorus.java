package creatures;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.Occupant;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class Clorus extends Creature {

    /**
     * red color.
     */
    private int r;
    /**
     * green color.
     */
    private int g;
    /**
     * blue color.
     */
    private int b;

    /**
     * creates clorus with energy equal to e.
     */
    public Clorus(double e) {
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        energy = e;
    }

    /**
    * creates clorus with energy equal to 1.
    */
    public Clorus() {
        this(1);
    }

    /**
     *
     * @return color with red equals 34, green equals 0 and blue equals 231.
     */
    @Override
    public Color color() {
        return color(r, g, b);
    }

    /**
     * loss 0.03 energy each move action.
     */
    @Override
    public void move() {
        energy -= 0.03;
        if (energy < 0) {
            energy = 0;
        }
    }

    /**
     * loss 0.01 energy each stay action.
     */
    @Override
    public void stay() {
        energy -= 0.01;
        if (energy < 0) {
            energy = 0;
        }
    }

    /**
     * eating a Creatrue and get its energe.
     * @param c the Creature being attacked.
     */
    @Override
    public void attack(Creature c) {
        energy += c.energy();
    }

    /**
     * Clorus and their offspring each get 50% of the energy, with none
     * lost to the process. Now that's efficiency!
     * @return Returns a baby Clorus.
     */
    @Override
    public Clorus replicate() {
        Clorus offspring = new Clorus();
        offspring.energy = energy;
        energy /= 2.0;
        offspring.energy -= energy;
        return offspring;
    }

    /**
     * 1.If there are no empty squares, the Clorus will STAY (even if there are Plips nearby they could attack
     * since plip squares do not count as empty squares).
     * 2.Otherwise, if any Plips are seen, the Clorus will ATTACK one of them randomly.
     * 3.Otherwise, if the Clorus has energy greater than or equal to one, it will REPLICATE to a random empty square.
     * 4.Otherwise, the Clorus will MOVE to a random empty square.
     * @param neighbors
     * @return
     */
    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        // Rule 1
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        Deque<Direction> plipsNeighbors = new ArrayDeque<>();

        for (Direction key : neighbors.keySet()) {
            if (neighbors.get(key).name().equals("empty")) {
                emptyNeighbors.addFirst(key);
            } else if (neighbors.get(key).name().equals("plip")) {
                plipsNeighbors.addFirst(key);
            }
        }
        if (emptyNeighbors.isEmpty()) {
            return new Action(Action.ActionType.STAY);
        }

        //Rule 2
        if (!plipsNeighbors.isEmpty()) {
            return new Action(Action.ActionType.ATTACK, randomEntry(plipsNeighbors));
        }

        //Rule 3
        if (energy >= 1) {
            return new Action(Action.ActionType.REPLICATE, randomEntry(emptyNeighbors));
        }

        //Rule 4
        return new Action(Action.ActionType.MOVE, randomEntry(emptyNeighbors));
    }
}
