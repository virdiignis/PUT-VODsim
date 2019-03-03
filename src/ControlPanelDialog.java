import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

class ControlPanelDialog extends JDialog implements Runnable {
    private JPanel contentPane;
    private JTabbedPane tabbedPane1;
    private JButton ButtonAddProvider;
    private JButton ButtonDelProvider;
    private JButton buttonOK;
    private JButton RunButton;
    private JButton PauseButton;
    private JLabel simTimeLabel;
    private JLabel SaldoLabel;
    private JButton ResetButton;
    private DefaultTableModel ProvidersTableModel;
    private DefaultTableModel ClientsTableModel;
    private JTable ProvidersTable;
    private JButton RemoveClientButton;
    private JLabel ProvidersCountLabel;
    private JTable ClientsTable;
    private JList ProductsList;
    private JLabel ProductImage;
    private JLabel titleLabel;
    private JLabel runtimeLabel;
    private JTextPane descLabel;
    private JLabel priceLabel;
    private JLabel gradeLabel;
    private JList actorsList;
    private JTree episodesTree;
    private JButton deleteProductButton;
    private JPanel produtionPanel;
    private JTextField searchText;
    private DefaultTreeModel treeModel;
    private Product currentShown;
    private Vector<Product> products;
    private Simulation s;

    public ControlPanelDialog() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setContentPane(contentPane);
        setModal(true);
        PauseButton.setEnabled(false);

        s = new Simulation();

        RunButton.addActionListener(actionEvent -> {
            s.pause();
            PauseButton.setEnabled(true);
            RunButton.setEnabled(false);
        });
        PauseButton.addActionListener(actionEvent -> {
            s.pause();
            PauseButton.setEnabled(false);
            RunButton.setEnabled(true);
        });
        ResetButton.addActionListener(actionEvent -> {
            s = new Simulation();
            s.start();
            s.pause();
            RunButton.setEnabled(true);
            PauseButton.setEnabled(false);
        });
        ButtonAddProvider.addActionListener(actionEvent -> {
            var p = s.addProvider();
            ProvidersTableModel.addRow(new Object[]{p.getUid(), p.getProducts().size()});
            ProvidersCountLabel.setText(Long.toString(s.getProviders().size()));
        });
        ProductsList.addListSelectionListener(listSelectionEvent -> show(ProductsList.getSelectedIndex()));
        deleteProductButton.addActionListener(actionEvent -> deleteProduct());
        RemoveClientButton.addActionListener(actionEvent -> {
            s.deleteUser(ClientsTable.getSelectedRow());
            ClientsTableModel.removeRow(ClientsTable.getSelectedRow());
        });
        ButtonDelProvider.addActionListener(actionEvent -> {
            int selectedRow = ProvidersTable.getSelectedRow();
            if (selectedRow < 0) return;
            Provider provider = s.getProviders().get(selectedRow);
            provider.stop();
            s.getProviders().remove(provider);
            ProvidersTableModel.removeRow(selectedRow);
        });
        searchText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyTyped(e);
                update();
                show(0);
            }
        });
    }

    private void deleteProduct() {
        s.getProductsLock().writeLock().lock();
        currentShown.getProvider().deleteProduct(currentShown);
        s.getProducts().remove(currentShown);
        s.getProductsLock().writeLock().unlock();
        s.getProductsLock().readLock().lock();
        int n = s.getProductsNo();
        s.getProductsLock().readLock().unlock();
        show(n - 1);
        update();
    }

    @Override
    public void run() {
        s.start();
        s.pause();
        setVisible(true);
    }

    private void show(int i) {
        if (i < 0 || i >= products.size()) {
            return;
        }
        currentShown = products.get(i);
        try {
            ProductImage.setIcon(new ImageIcon(currentShown.getImage()));
        } catch (Exception ignored) {
        }
        titleLabel.setText(currentShown.getName());
        descLabel.setText(currentShown.getDesc());
        try {
            runtimeLabel.setText(Integer.toString(currentShown.getRuntime()));
        } catch (Exception ignored) {
        }
        try {
            priceLabel.setText(Float.toString(currentShown.getBasePrice()));
        } catch (Exception ignored) {
        }
        try {
            gradeLabel.setText(Float.toString(currentShown.getGrade()));
        } catch (Exception ignored) {
        }

        actorsList.setListData(new Vector());
        if (currentShown instanceof Movie) {
            Movie movie = (Movie) currentShown;
            actorsList.setListData(movie.getActors().toArray());
        }
        episodesTree.setVisible(false);
        if (currentShown instanceof Series) {
            Series series = (Series) currentShown;
            actorsList.setListData(series.getActors().toArray());

            DefaultMutableTreeNode top = new DefaultMutableTreeNode();
            for (Series.Season season : series.getSeasons()) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(season.name);
                for (Series.Episode ep : season) node.add(new DefaultMutableTreeNode(ep));
                top.add(node);
            }
            treeModel.setRoot(top);
            treeModel.reload();
            episodesTree.setVisible(true);
        }


    }

    public void update() {
        simTimeLabel.setText(Long.toString(s.getSimTime()));
        SaldoLabel.setText(Float.toString(s.getAccount()));
        for (int i = 0; i < s.getProviders().size(); i++)
            ProvidersTableModel.setValueAt(s.getProviders().get(i).getProducts().size(), i, 1);

        for (int i = ClientsTableModel.getRowCount(); i < s.getUsers().size(); i++) {
            User user = s.getUsers().get(i);
            ClientsTableModel.insertRow(0, new Object[]{user.getUid(), user.getEmail(), user.getCreditCard()});
//            ClientsTableModel.setValueAt(user.getUid(), i, 0);
//            ClientsTableModel.setValueAt(user.getEmail(), i, 1);
//            ClientsTableModel.setValueAt(user.getCreditCard(), i, 2);
        }

        try {
            s.getProductsLock().readLock().lock();
            String text = searchText.getText();
            if (text.isEmpty()) {
                products = new Vector<>(s.getProducts());
            } else {
                products = new Vector<>();
                for (Product p : s.getProducts()) if (p.getName().startsWith(text)) products.add(p);
            }
            s.getProductsLock().readLock().unlock();
            ProductsList.setListData(products);
        } catch (IllegalMonitorStateException ignored) {
        }
    }

    private void createUIComponents() {
        String[] cols = {"UID", "Products"};
        ProvidersTableModel = new DefaultTableModel(cols, 0);
        ProvidersTable = new JTable(ProvidersTableModel) {
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
//        ProvidersTable.setCellSelectionEnabled(false);

        String[] cols2 = {"UID", "Email", "Credit Card"};
        ClientsTableModel = new DefaultTableModel(cols2, 0);
        ClientsTable = new JTable(ClientsTableModel) {
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        treeModel = new DefaultTreeModel(root);
        episodesTree = new JTree(treeModel);
        episodesTree.setRootVisible(false);
    }
}
