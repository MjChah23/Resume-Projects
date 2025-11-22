public abstract class Deployment implements Comparable<Deployment> {
    int id;
    long timestamp;
    int general;
    int planet;
    int force;
    int quantity;

    public Deployment(int id, long ts, int g, int p, int f, int q) {
        this.id = id;
        timestamp = ts;
        general = g;
        planet = p;
        force = f;
        quantity = q;
    }

    public abstract boolean isSith();


    @Override
    public abstract int compareTo(Deployment o);
}
