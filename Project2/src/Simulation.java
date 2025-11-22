import java.sql.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Simulation {
    private ArrayList<General> generals;
    private ArrayList<Planet> planets;

    private Config config;

    private Scanner in;

    private int currId = 0;

    static long currentTime = 0;

    public Simulation(Config c){
        config = c;

        in = new Scanner(System.in);

        // skip first line... it's a comment
        in.nextLine();

        // read the mode
        // skip the header
        in.next();
        String mode = in.next();

        // read the number of generals
        in.next();
        int numGenerals = in.nextInt();

        // read the number of planets
        in.next();
        int numPlanets = in.nextInt();

        generals = new ArrayList<>(numGenerals);
        for(int i = 0; i < numGenerals; i++){
            generals.add(new General(i));
        }

        planets = new ArrayList<>(numPlanets);
        for (int i = 0; i < numPlanets; i++){
            planets.add(new Planet(i));
        }

        // if PR mode, finalize our setup
        if (mode.equals("PR")){
            // random seed
            in.next();
            int seed = in.nextInt();

            // num deployments
            in.next();
            int numDeployments = in.nextInt();

            // arrival rate
            in.next();
            int arrivalRate = in.nextInt();

            // create our random deployment generator
            in = P2Random.PRInit(seed, numGenerals, numPlanets, numDeployments, arrivalRate);
        }

        // in scanner
        // DL: System.in
        // PR: P2Random
    }



    // where we read deployments and perform battles
    public void performWarfare(){
        // print out the simulation starting message
        System.out.println("Deploying troops...");

        while (in.hasNextInt()){
            Deployment d = getNextDeployment();

            // check the time
            if (currentTime < d.timestamp){

                if (config.median){
                    // median mode is on
                    for (Planet p : planets){
                        p.printMedian();
                    }
                }
                currentTime = d.timestamp;
            }

            // checks if the time stamps are non-decreasing
            if (currentTime > d.timestamp){
                System.err.println("Timestamps are non-decreasing");
                System.exit(1);
            }
            // track the deployment for the general eval mode
            if (config.general) {
                generals.get(d.general).addDeployment(d);
            }

            Planet p = planets.get(d.planet);
            p.addDeployment(d);
            p.performBattles(config);

        }

        // print out our running medians
        if (config.median){
            // median mode is on
            for (Planet p : planets){
                p.printMedian();
            }
        }

        // print out summary
        System.out.print("---End of Day---\n");


        // count up the battles
        int totalBattles = 0;
        for (Planet p : planets){
            totalBattles += p.getNumBattles();
        }
        System.out.print("Battles: " + totalBattles + "\n");

        // general eval output
        if (config.general){
            for (Planet p : planets){

                // count up the survivors
                // add them to the appropriate general
             p.countSurvivors(generals);
            }

            System.out.println("---General Evaluation---");
            for (General g : generals){
                g.printStats();
            }
        }
    }

    private Deployment getNextDeployment() {
        // read all of this and construct a deployment object to represent the deployment
        long timestamp = in.nextLong();
        String type = in.next();

        // Integer.parseInt()
        int generalId = Integer.parseInt(in.next().substring(1));
        int planetId = Integer.parseInt(in.next().substring(1));
        int force = Integer.parseInt(in.next().substring(1));
        int numTroops = Integer.parseInt(in.next().substring(1));

        // checks if the timestamp is greater than 0
        if (timestamp < 0){
            System.err.println("Timestamp is less than 0");
            System.exit(1);
        }

        // checks if the number of troops is positive
        if (numTroops < 1){
            System.err.println("The number of troops can not be less than 1");
            System.exit(1);
        }

        // checks if the force sensitivity is less than 1
        if (force < 1){
            System.err.println("Force can not be less than 1");
            System.exit(1);
        }

        // checks if the General ID integers are in range
        if (generals.size() <= generalId){
            System.err.println("GeneralId is not in range");
            System.exit(1);
        }
        // checks if the Planet ID integers are in range
        if (planets.size() <= planetId){
            System.err.println("PlanetId is not in range");
            System.exit(1);
        }

        if (type.equals("SITH")) {
            return new SithDeployment(currId++, timestamp, generalId, planetId, force, numTroops);
        } else {
            return new JediDeployment(currId++, timestamp, generalId, planetId, force, numTroops);
        }
    }
}
