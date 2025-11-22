public class General {
    private int numJedi, numSith, numSurvive, generalID;

    public General(int GeneralID){
        numJedi = 0;
        numSith = 0;
        numSurvive = 0;
        this.generalID = GeneralID;
    }

    // updates the number of deployment depending on if they are a sith or jedi
    public void addDeployment(Deployment d){
        // if sith
        if (d.isSith()) {
            numSith += d.quantity;
        }
        // if jedi
        else {
            numJedi += d.quantity;
        }
    }

    public void addSurvivors(int s){
        // updates the number of survivors
        numSurvive += s;
    }

    // prints the stats
    public void printStats() {
        System.out.println("General " + generalID + " deployed " + numJedi + " Jedi troops and " + numSith + " Sith troops, and "
        + numSurvive + "/" + (numJedi + numSith) + " troops survived.");
    }
}
