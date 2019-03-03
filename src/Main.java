class Main {
    public static void main(String[] args) {
        ControlPanelDialog dialog = new ControlPanelDialog();
        dialog.pack();
        Thread t = new Thread(dialog);
        t.start();
        while (true) {
            dialog.update();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        System.exit(0);
    }
}
