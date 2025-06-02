// ProductViewer.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

// Adapte les imports POJO/DAO
// import com.yourproject.model.Product;
// import com.yourproject.dao.ProductDAO;

public class ProductViewer extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> categoryFilter;
    private JButton lowStockButton;

    public ProductViewer() {
        setTitle("Liste des Produits");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Panel du HAUT : Filtre catégorie et alerte stock ---
        JPanel topSectionPanel = new JPanel(new BorderLayout(5,5));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filtrer par Catégorie :"));
        categoryFilter = new JComboBox<>();
        categoryFilter.addItem("Tous");
        filterPanel.add(categoryFilter);
        
        lowStockButton = new JButton("Afficher Stocks Bas (seuil < 5)"); // Seuil à adapter
        lowStockButton.addActionListener(_ -> showLowStockProducts());
        filterPanel.add(lowStockButton); // Ajouté au même panel que le filtre pour simplicité

        topSectionPanel.add(filterPanel, BorderLayout.NORTH);
        add(topSectionPanel, BorderLayout.NORTH);

        // --- CENTRE : Table des produits ---
        String[] columnNames = {"ID", "Nom du Produit", "Prix (€)", "Catégorie"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Panel du BAS : Bouton Retour ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("Retour au Menu Principal"); // ✨ BOUTON RETOUR ✨
        backButton.addActionListener(_ -> this.dispose());
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Listeners et chargement initial
        categoryFilter.addActionListener(_ -> loadProducts());
        loadCategories();
        loadProducts();
    }

    private void loadCategories() {
        try {
            List<String> categories = ProductDAO.getAllCategories();
            for (String cat : categories) categoryFilter.addItem(cat);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement catégories : " + e.getMessage());
        }
    }

    private void loadProducts() {
        if (categoryFilter == null || categoryFilter.getSelectedItem() == null) return;
        try {
            List<Product> products;
            String selectedCategory = (String) categoryFilter.getSelectedItem();
            if ("Tous".equals(selectedCategory)) {
                products = ProductDAO.getAllProducts();
            } else {
                products = ProductDAO.getProductsByCategory(selectedCategory);
            }
            tableModel.setRowCount(0);
            for (Product p : products) {
                tableModel.addRow(new Object[]{
                    p.getProductId(), p.getProductName(),
                    String.format("%.2f", p.getPrice()), p.getCategory()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement produits : " + e.getMessage());
        }
    }
    
    private void showLowStockProducts() {
        // ... (code de la méthode showLowStockProducts comme précédemment) ...
        // S'assurer qu'il affiche un JOptionPane ou un JDialog
        try {
            int threshold = 5; 
            List<Product> lowStockItems = ProductDAO.getLowStockProducts(threshold);
            if (lowStockItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucun produit avec un stock bas (inférieur à " + threshold + ") trouvé.", "Stock OK", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            StringBuilder message = new StringBuilder("Produits avec stock bas (seuil < " + threshold + "):\n\n");
            for (Product p : lowStockItems) message.append("- ").append(p.getProductName()).append("\n");
            
            JTextArea textArea = new JTextArea(message.toString());
            textArea.setEditable(false); textArea.setWrapStyleWord(true); textArea.setLineWrap(true);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 250));
            JOptionPane.showMessageDialog(this, scrollPane, "Alerte Stocks Bas", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur récupération stocks bas:\n" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductViewer().setVisible(true));
    }
}