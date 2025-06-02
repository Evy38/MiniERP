// OrderHistoryViewer.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

// Adapte ces imports si tes classes POJO/DAO sont dans des packages
// import com.yourproject.model.Customer; // Nécessaire si ComboBox contient des objets Customer
// import com.yourproject.dao.CustomerDAO;
// import com.yourproject.model.OrderDetail;
// import com.yourproject.dao.OrderDAO;


/**
 * 🖼️ CLASSE ORDERHISTORYVIEWER - Fenêtre pour afficher l'historique des commandes d'un client.
 * 
 * Elle permet de :
 *   - Sélectionner un client.
 *   - Afficher les détails des commandes de ce client dans un tableau.
 */
public class OrderHistoryViewer extends JFrame {

    private JComboBox<Integer> clientComboBox; // Gardé en Integer pour l'instant, comme ton code original
    private JButton showHistoryButton;
    private JTable ordersTable;
    private DefaultTableModel tableModel;

    public OrderHistoryViewer(int initialClientId) {
        super("Historique des commandes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500); // Un peu plus de hauteur pour le bouton retour
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10)); // Ajout d'un peu d'espace

        // --- Panel du Haut (pour la sélection du client) ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5)); // Petite marge
        clientComboBox = new JComboBox<>();
        
        // Remplissage de la ComboBox (gardé comme dans ton code original avec des IDs)
        // Idéalement, charger des objets Customer et utiliser un renderer
        clientComboBox.addItem(7);
        clientComboBox.addItem(8);
        clientComboBox.addItem(9);
        clientComboBox.addItem(10);

        // Essayer de présélectionner le client si un ID valide est fourni
        boolean clientFound = false;
        if (initialClientId > 0) {
            for (int i = 0; i < clientComboBox.getItemCount(); i++) {
                if (clientComboBox.getItemAt(i).equals(initialClientId)) {
                    clientComboBox.setSelectedIndex(i);
                    clientFound = true;
                    break;
                }
            }
        }
        // Si l'ID initial n'est pas trouvé ou si aucun ID n'est fourni, sélectionner le premier
        if (!clientFound && clientComboBox.getItemCount() > 0) {
            clientComboBox.setSelectedIndex(0);
        }


        showHistoryButton = new JButton("Afficher l'historique");
        topPanel.add(new JLabel("Client ID:"));
        topPanel.add(clientComboBox);
        topPanel.add(showHistoryButton);
        add(topPanel, BorderLayout.NORTH);

        // --- Configuration de la Table (Centre) ---
        String[] columnNames = {
            "ID Commande", "Date", "Montant HT", "TVA", "Total TTC", 
            "Produit", "Quantité", "Prix Unit."
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; } // Rendre non éditable
        };
        ordersTable = new JTable(tableModel);
        add(new JScrollPane(ordersTable), BorderLayout.CENTER);

        // --- ✨ Panel du BAS : Bouton Retour ✨ ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Aligner à droite
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5)); // Petite marge
        JButton backButton = new JButton("Retour au Menu Principal");
        backButton.addActionListener(_ -> this.dispose()); // Action : ferme cette fenêtre
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH); // Ajout du panel en bas

        // --- Action du bouton "Afficher l'historique" ---
        showHistoryButton.addActionListener(_ -> {
            Integer selectedClientId = (Integer) clientComboBox.getSelectedItem();
            if (selectedClientId != null) { //  && selectedClientId != -1 (si -1 était une valeur spéciale)
                loadOrderHistory(selectedClientId);
                // Mettre à jour le titre avec le nom du client serait mieux si on avait l'objet Customer
                setTitle("Historique des commandes du client ID: " + selectedClientId);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client.", "Sélection Client", JOptionPane.WARNING_MESSAGE);
                tableModel.setRowCount(0); // Vider la table
                setTitle("Historique des commandes");
            }
        });

        // Charger l'historique pour le client initialement sélectionné (s'il y en a un)
        Integer initiallySelectedId = (Integer) clientComboBox.getSelectedItem();
        if (initiallySelectedId != null) {
            loadOrderHistory(initiallySelectedId);
            setTitle("Historique des commandes du client ID: " + initiallySelectedId);
        }
    }

    private void loadOrderHistory(int clientId) {
        // System.out.println("Chargement de l'historique des commandes pour le client ID: " + clientId);
        // setTitle("Historique des commandes du client " + clientId); // Déjà fait dans l'appelant

        try {
            List<OrderDetail> ordersDetails = OrderDAO.getOrderDetailsByCustomer(clientId);
            tableModel.setRowCount(0); 

            if (ordersDetails.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucune commande trouvée pour ce client (ID: " + clientId + ").", "Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (OrderDetail od : ordersDetails) {
                    tableModel.addRow(new Object[]{
                        od.getOrderId(),
                        od.getOrderDate(),
                        String.format("%.2f", od.getNetAmount()),
                        String.format("%.2f", od.getTax()),
                        String.format("%.2f", od.getTotalAmount()),
                        od.getProductName(),
                        od.getQuantity(),
                        String.format("%.2f", od.getUnitPrice())
                    });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                                          "Erreur lors de la récupération de l'historique : \n" + ex.getMessage(), 
                                          "Erreur Base de Données", 
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    // Optionnel: Méthode main pour tester ce Viewer indépendamment
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OrderHistoryViewer viewer = new OrderHistoryViewer(7); // Test avec client ID 7 par défaut
            viewer.setVisible(true);
        });
    }
}