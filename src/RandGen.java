class RandGen {
    static int randInt(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
    static long randLong(long start, long end) { return start + Math.round(Math.random() * (end - start)); }
    static boolean randBool(){return Math.random() > 0.5;}
}
