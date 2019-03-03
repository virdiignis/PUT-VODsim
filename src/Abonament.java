class Abonament {
    enum Version {
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

    private final Version version;

    Abonament(int ver) {
        if(ver == 0){
            version = Version.BASIC;
        } else if (ver == 1){
            version = Version.FAMILY;
        } else {
            version = Version.PREMIUM;
        }
    }

    Version getVersion() {
        return version;
    }
}
