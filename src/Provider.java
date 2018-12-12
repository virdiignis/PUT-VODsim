public class Provider{
    /** Provider id */
    long uid;
    /**
     * Pay per view
     * false for monthly payment, true for charge per view
     */
    boolean ppv;
    float price;

    public Provider() {
        uid = RandGen.randLong(0, Long.MAX_VALUE);
        ppv = RandGen.randBool();

    }
}
