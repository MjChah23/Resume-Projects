public class SithDeployment extends Deployment {
    public SithDeployment(int id, long ts, int g, int p, int f, int q) {
        super(id, ts, g, p, f, q);
    }

    @Override
    public boolean isSith() {
        return true;
    }

    @Override
    public int compareTo(Deployment d) {
        if (this.force == d.force) {
            return this.id - d.id;
        }
        return d.force - this.force;
    }
}