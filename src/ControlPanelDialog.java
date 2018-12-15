import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ControlPanelDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JPanel imagexd;
    private JTextPane textPane1;
    private JTextPane textPane2;

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
        imagexd.removeAll();
        try {
//            Product a = new Product(new Provider(), 3);
//            textArea1.setText(a.name);

            Movie m = new Movie(new Provider(), 3);
            textPane1.setText(m.name);
            textPane2.setText(m.desc);
            ImageIcon icon=new ImageIcon(m.image);
            //JFrame frame=new JFrame();
            imagexd.setLayout(new FlowLayout());
            imagexd.setSize(600,900);
            JLabel lbl=new JLabel();
            lbl.setIcon(icon);
            imagexd.add(lbl);
            imagexd.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //dispose();
    }

    public static void main(String[] args) {
        ControlPanelDialog dialog = new ControlPanelDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
