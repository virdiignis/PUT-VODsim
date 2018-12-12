public class Abonament {
    public enum Version {
        BASIC(10, 1, 1920, 1080),
        FAMILY(30, 3, 1920, 1080),
        PREMIUM(50, 3, 4096, 2160);


        final float price;
        final int devices;
        final int resX;
        final int resY;

        Version(float price, int devices, int resX, int resY) {
            this.price = price;
            this.devices = devices;
            this.resX = resX;
            this.resY = resY;
        }
    }
}
