import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

public class Config {
    boolean verbose;
    boolean median;
    boolean general;
    public Config(String[] args){
        LongOpt[] longOptions = {
                new LongOpt("verbose", LongOpt.NO_ARGUMENT, null, 'v'),
                new LongOpt("median", LongOpt.NO_ARGUMENT, null, 'm'),
                new LongOpt("general", LongOpt.NO_ARGUMENT, null, 'g')
        };

        Getopt g = new Getopt("Project2", args, "vmgw", longOptions);
        int choice;
        while((choice = g.getopt()) != -1){
            switch (choice) {
                case 'v':
                    verbose = true;
                    break;
                case 'm':
                    median = true;
                    break;
                case 'g':
                    general = true;
                    break;
                default:
                    System.err.println("You have chosen an invalid option");
                    System.exit(1);
                    break;
            }
        }
    }
}
