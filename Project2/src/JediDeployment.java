public class JediDeployment extends Deployment{
    public JediDeployment(int id, long ts, int g, int p, int f, int q) {
        super(id, ts, g, p, f, q);
    }

    @Override
    public boolean isSith() {
        return false;
    }

    @Override
    public int compareTo(Deployment d) {
        // this
        // d
        // return a negative number if "this" is higher priority than d
        // return a positive number if "this" is lower priority than d

        if (this.force == d.force){
            // which came earlier?
            // sith deployment is the same here
            return this.id - d.id;
        }
        // if the forces are different
        return this.force - d.force;
    }
}
