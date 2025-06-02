// CustomerViewer.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Adapte les imports POJO/DAO/Viewer
// import com.yourproject.model.Customer;
// import com.yourproject.dao.CustomerDAO;
// import com.yourproject.view.OrderHistoryViewer;
// import com.yourproject.view.AddCustomerForm; // Si on ajoute un bouton pour ça

public class CustomerViewer extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    // Optionnel: Bouton pour ouvrir le formulaire d'ajout de client
    // private JButton addCustomerButton; 

    public CustomerViewer() {
        setTitle("Liste des Clients");
        setSize(700, 450); // Un peu plus de place pour les boutons
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Important pour ne pas quitter l'app
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10)); // Utiliser BorderLayout pour la JFrame

        model = new DefaultTableModel(new Object[] { "ID", "Nom", "Email", "Téléphone" }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // --- Panel du HAUT pour les actions sur la liste ---
        JPanel topActionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton loadButton = new JButton("Charger les clients");
        loadButton.addActionListener(_ -> loadCustomers());
        topActionsPanel.add(loadButton);

        // Optionnel: Bouton pour ajouter un nouveau client directement depuis cette vue
        // addCustomerButton = new JButton("Ajouter un Nouveau Client");
        // addCustomerButton.addActionListener(e -> {
        //     SwingUtilities.invokeLater(() -> new AddCustomerForm().setVisible(true));
        // });
        // topActionsPanel.add(addCustomerButton);

        add(topActionsPanel, BorderLayout.NORTH);

        // --- CENTRE : La table des clients ---
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Panel du BAS pour les actions sur un client sélectionné ET le retour ---
        JPanel bottomActionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Aligner à droite

        JButton btnAfficherCommandes = new JButton("Afficher l'historique du client sélectionné");
        btnAfficherCommandes.addActionListener(_ -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int modelRow = table.convertRowIndexToModel(selectedRow); // Si la table est triable
                Object customerIdObj = model.getValueAt(modelRow, 0);
                if (customerIdObj instanceof Integer) {
                    int customerId = (Integer) customerIdObj;
                    SwingUtilities.invokeLater(() -> new OrderHistoryViewer(customerId).setVisible(true));
                } else {
                    JOptionPane.showMessageDialog(this, "ID client invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez d'abord un client.");
            }
        });
        bottomActionsPanel.add(btnAfficherCommandes);
        
        // ✨ BOUTON RETOUR ✨
        JButton backButton = new JButton("Retour au Menu");
        backButton.addActionListener(_ -> this.dispose()); // Ferme cette fenêtre
        bottomActionsPanel.add(Box.createHorizontalStrut(10)); // Petit espace
        bottomActionsPanel.add(backButton);

        add(bottomActionsPanel, BorderLayout.SOUTH);

        loadCustomers(); // Charger les clients au démarrage de cette fenêtre
    }

    private void loadCustomers() {
        try {
            List<Customer> customers = CustomerDAO.getAllCustomers();
            model.setRowCount(0); 
            for (Customer c : customers) {
                model.addRow(new Object[] { c.getCustomerId(), c.getName(), c.getEmail(), c.getPhone() });
            }
        } catch (Exception e) { 
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur chargement clients : " + e.getMessage());
        }
    }

    // main de test (garder pour tester cette fenêtre seule)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerViewer().setVisible(true));
    }
}