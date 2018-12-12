import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlPanelDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;

    public ControlPanelDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
    }
    private static int randInt(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

    private void onOK() {
        System.out.println(Integer.toString(randInt(888888, 9999999), 35) + "@gmail.com");
        System.out.println(Integer.toString(randInt(0, 9999)) + (randInt(0, 9999)) + (randInt(0, 9999)) + randInt(0, 9999));
        //dispose();
    }

    public static void main(String[] args) {
        ControlPanelDialog dialog = new ControlPanelDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
