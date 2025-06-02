// OrderForm.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Adapte les imports pour Customer, Product, OrderLine, CustomerDAO, ProductDAO, OrderDAO
// import com.yourproject.model.Customer;
// import com.yourproject.dao.CustomerDAO;
// import com.yourproject.model.Product;
// import com.yourproject.dao.ProductDAO;
// import com.yourproject.model.Order;
// import com.yourproject.model.OrderLine;
// import com.yourproject.dao.OrderDAO;


/**
 * 🧾 ORDERFORM - Interface pour créer une nouvelle commande
 * Permet à l'utilisateur de :
 *   - Choisir un client
 *   - Choisir un produit + quantité
 *   - Ajouter plusieurs lignes à une commande
 *   - Valider la commande (DAO + DB)
 */
public class OrderForm extends JFrame {

    private JComboBox<Customer> customerBox;
    private JComboBox<Product> productBox;
    private JTextField quantityField;
    private JTextArea orderArea;
    private List<OrderLine> orderLines;

    public OrderForm() {
        setTitle("Créer une commande");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10)); // Ajout d'un peu d'espace

        orderLines = new ArrayList<>();

        // --- TOP PANEL : Sélection client + produit + quantité ---
        JPanel topPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Marge
        customerBox = new JComboBox<>();
        productBox = new JComboBox<>();
        quantityField = new JTextField("1", 5); // Valeur par défaut et taille

        try {
            // Charger les clients
            List<Customer> customers = CustomerDAO.getAllCustomers();
            for (Customer c : customers) {
                customerBox.addItem(c);
            }
            customerBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Customer) setText(((Customer) value).getName());
                    return this;
                }
            });

            // Charger les produits
            List<Product> products = ProductDAO.getAllProducts();
            for (Product p : products) {
                productBox.addItem(p);
            }
            productBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Product) setText(((Product) value).getProductName() + " (" + String.format("%.2f", ((Product) value).getPrice()) + "€)");
                    return this;
                }
            });
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement données :\n" + e.getMessage());
        }

        topPanel.add(new JLabel("Client :"));
        topPanel.add(customerBox);
        topPanel.add(new JLabel("Produit :"));
        topPanel.add(productBox);
        topPanel.add(new JLabel("Quantité :"));
        topPanel.add(quantityField);

        add(topPanel, BorderLayout.NORTH);

        // --- CENTRE : Affichage du contenu de la commande ---
        orderArea = new JTextArea();
        orderArea.setEditable(false);
        orderArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Police pour meilleur affichage
        add(new JScrollPane(orderArea), BorderLayout.CENTER);

        // --- BOTTOM : Boutons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Aligner les boutons à droite
        JButton addButton = new JButton("Ajouter produit");
        JButton validateButton = new JButton("Valider commande");
        
        // ✨ AJOUT DU BOUTON RETOUR ✨
        JButton backButton = new JButton("Retour au Menu");
        backButton.addActionListener(_ -> this.dispose()); // Action : ferme cette fenêtre

        addButton.addActionListener(this::handleAdd);
        validateButton.addActionListener(this::handleValidate);

        buttonPanel.add(addButton);
        buttonPanel.add(validateButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); // Petit espace avant le bouton retour
        buttonPanel.add(backButton); // Ajout du bouton au panel
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleAdd(ActionEvent e) {
        Product selectedProduct = (Product) productBox.getSelectedItem();
        // Vérifier si un client est sélectionné
        Customer selectedCustomer = (Customer) customerBox.getSelectedItem();
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord sélectionner un client.", "Client requis", JOptionPane.WARNING_MESSAGE);
            customerBox.requestFocusInWindow();
            return;
        }
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit.", "Produit requis", JOptionPane.WARNING_MESSAGE);
            productBox.requestFocusInWindow();
            return;
        }
        
        int qty;
        try {
            qty = Integer.parseInt(quantityField.getText().trim());
            if (qty <= 0) {
                 JOptionPane.showMessageDialog(this, "La quantité doit être un nombre positif.", "Quantité invalide", JOptionPane.ERROR_MESSAGE);
                 quantityField.requestFocusInWindow();
                 quantityField.selectAll();
                 return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantité invalide. Veuillez entrer un nombre.", "Quantité invalide", JOptionPane.ERROR_MESSAGE);
            quantityField.requestFocusInWindow();
            quantityField.selectAll();
            return;
        }

        orderLines.add(new OrderLine(selectedProduct, qty));
        // Formatage pour un meilleur affichage
        String lineText = String.format("%d x %-30s @ %6.2f € = %7.2f €\n",
                                        qty,
                                        selectedProduct.getProductName(),
                                        selectedProduct.getPrice(),
                                        selectedProduct.getPrice() * qty);
        orderArea.append(lineText);
        quantityField.setText("1"); // Réinitialiser après ajout
        productBox.requestFocusInWindow();
    }

    private void handleValidate(ActionEvent e) {
        if (orderLines.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La commande est vide ! Ajoutez des produits.", "Commande Vide", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer selectedCustomer = (Customer) customerBox.getSelectedItem();
        if (selectedCustomer == null) { // Devrait être déjà géré par handleAdd, mais une sécurité en plus
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client pour cette commande.", "Client Manquant", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Order order = new Order(selectedCustomer.getCustomerId());
        for (OrderLine line : orderLines) {
            order.addLine(line);
        }
        
        String summary = String.format(
            "Client: %s\nMontant HT: %.2f €\nTVA (20%%): %.2f €\nTotal TTC: %.2f €\n\nConfirmer ?",
            selectedCustomer.getName(),
            order.getNetAmount(),
            order.getTax(),
            order.getTotalAmount()
        );
        int confirmation = JOptionPane.showConfirmDialog(this, summary, "Récapitulatif Commande", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                OrderDAO.createOrder(order);
                JOptionPane.showMessageDialog(this, "Commande enregistrée avec succès !");
                // Réinitialiser pour une nouvelle commande
                orderLines.clear();
                orderArea.setText("");
                quantityField.setText("1");
                if (customerBox.getItemCount() > 0) customerBox.setSelectedIndex(0);
                if (productBox.getItemCount() > 0) productBox.setSelectedIndex(0);
                // this.dispose(); // Optionnel: fermer la fenêtre après succès
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur création commande :\n" + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    // ▶️ Test rapide de l'UI (à appeler depuis App.java si tu veux)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrderForm().setVisible(true));
    }
}