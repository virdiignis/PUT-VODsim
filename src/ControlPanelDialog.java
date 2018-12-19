import javax.swing.*;

public class ControlPanelDialog extends JDialog implements Runnable {
    private JPanel contentPane;
    private JButton buttonOK;
    private JPanel imagexd;
    private JTextPane textPane1;
    private JList<Object> list1;
    private JScrollBar scrollBar1;
    Simulation s;

    public ControlPanelDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


//
//        list1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//        list1.setLayoutOrientation(JList.HORIZONTAL_WRAP);
//        contentPane.add(list1);
////        list1.setVisibleRowCount(-1);
//
//        scrollBar1.add(list1);
//        listScroller.setPreferredSize(new Dimension(250, 80));

        buttonOK.addActionListener(e -> onOK());
        s = new Simulation();

    }

    private void onOK() {
//        imagexd.removeAll();
//        try {
////            Product a = new Product(new Provider(), 3);
////            textArea1.setText(a.name);
//
//            Product m = new Live(new Provider(), 3);
//            textPane1.setText(m.name);
//            textPane2.setText(m.desc);
//            ImageIcon icon=new ImageIcon(m.image);
//            //JFrame frame=new JFrame();
//            imagexd.setLayout(new FlowLayout());
//            imagexd.setSize(600,900);
//            JLabel lbl=new JLabel();
//            lbl.setIcon(icon);
//            imagexd.add(lbl);
//            imagexd.setVisible(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //dispose();
        s.addProvider();


    }

    @Override
    public void run() {
        s.start();

        while (true) {
            textPane1.setText(String.format("%d", s.getUsersNo()));
            list1.setListData(s.getProducts().toArray());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
