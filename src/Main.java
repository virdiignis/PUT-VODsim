public class Main {
    public static void main(String[] args) {
        ControlPanelDialog dialog = new ControlPanelDialog();
        dialog.pack();
        Thread t = new Thread(dialog);
        t.start();

        dialog.setVisible(true);

        System.exit(0);
    }
}
