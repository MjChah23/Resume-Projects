public class Main {

    public static void main(String[] args) {

        Config config = new Config(args);

        Simulation s = new Simulation(config);

        s.performWarfare();

    }
}
