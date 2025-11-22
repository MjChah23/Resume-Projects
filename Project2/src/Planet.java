import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class Planet {

    private PriorityQueue<Deployment> jediDeployments;
    private PriorityQueue<Deployment> sithDeployments;

    // 2 priority queues for the median
    private PriorityQueue<Integer> lowerMod;
    private PriorityQueue<Integer> upperMod;

    private int id;
    private int numBattles;

    public Planet(int id) {
        this.id = id;
        numBattles = 0;

        jediDeployments = new PriorityQueue<>();
        sithDeployments = new PriorityQueue<>();

        lowerMod = new PriorityQueue<>(Collections.reverseOrder());
        upperMod = new PriorityQueue<>();
    }

    // add deployment
    public void addDeployment(Deployment d) {

        if (d.isSith()) {
            sithDeployments.add(d);
        } else {
            jediDeployments.add(d);
        }
    }

    public boolean canBattle() {
        // there is a valid battle if there is a jedi and a sith
        // we need both a jedi and a sith
        if (jediDeployments.isEmpty() || sithDeployments.isEmpty()) {
            return false;
        }
        return jediDeployments.peek().force <= sithDeployments.peek().force;
    }

    public void performBattles(Config c) {
        // checks to see if battle can be performed
        while (canBattle()) {
            // a battle can occur
            // subtract the same number of troops from both sith and jedi deployments
            int troopsLost = Math.min(jediDeployments.peek().quantity,
                    sithDeployments.peek().quantity);

            // subtract these from both deployments
            jediDeployments.peek().quantity -= troopsLost;
            sithDeployments.peek().quantity -= troopsLost;

            // print output before removing from the PQs
            if (c.verbose) {
                System.out.print("General " + sithDeployments.peek().general + "'s battalion " +
                        "attacked General " + jediDeployments.peek().general + "'s battalion on " +
                        "planet " + id + ". " + (troopsLost * 2) + " troops were lost.\n");
            }

            // one of these deployments will have lost all the troops
            // remove that deployment from the PQ
            if (jediDeployments.peek().quantity == 0) {
                jediDeployments.remove();
            }

            if (sithDeployments.peek().quantity == 0) {
                sithDeployments.remove();
            }

            // update the count of our battles
            numBattles++;

            // update the troops lost if in median mode
            if (c.median) {
                updateMedian(troopsLost * 2);
            }
        }
    }

    private void updateMedian(int troopsLost) {
        // add to the PQs
        // make sure they're balanced...
        if (lowerMod.isEmpty() || troopsLost <= lowerMod.peek()) {
            lowerMod.add(troopsLost);
        } else {
            upperMod.add(troopsLost);
        }

        if (upperMod.size() - lowerMod.size() == 2) {
            lowerMod.add(upperMod.remove());
        } else if (lowerMod.size() - upperMod.size() == 2) {
            upperMod.add(lowerMod.remove());
        }
    }

    public int getNumBattles() {
        return numBattles;
    }

    // prints out the current median value
    public void printMedian() {
        if (getNumBattles() == 0) {
            return;
        }
        // initializes a variable for median
        int median;

        if (lowerMod.size() == upperMod.size()) {
            median = (lowerMod.peek() + upperMod.peek()) / 2;
        } else if (upperMod.size() > lowerMod.size()) {
            median = upperMod.peek();
        } else {
            median = lowerMod.peek();
        }
        // prints out the median
        System.out.println("Median troops lost on planet " + id + " at time " + Simulation.currentTime + " is " + median + ".");
    }


    // counts the survivors
    public void countSurvivors(ArrayList<General> generals) {

        while (!sithDeployments.isEmpty()) {
            // Process all of the sith deployments until empty
            Deployment sithd = sithDeployments.poll();
            // updates the number of survivors
            generals.get(sithd.general).addSurvivors(sithd.quantity);
        }
        // Process all of the jedi deployments until empty
        while (!jediDeployments.isEmpty()){
            Deployment jedid = jediDeployments.poll();
            // updates the number of survivors
            generals.get(jedid.general).addSurvivors(jedid.quantity);
        }
    }
}