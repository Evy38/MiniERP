import java.sql.*;           // 🎫 Pour accéder à la base (connexions, requêtes, résultats)
import java.util.*;          // 📦 Pour utiliser List et ArrayList

/**
 * 📦 PRODUCTDAO - Ton "employé produits"
 * 
 * Il va chercher les produits dans la base PostgreSQL
 * Il transforme les lignes SQL en objets Product que tu peux utiliser
 * 
 * 🏪 Analogie :
 * - La base = Le stock
 * - ProductDAO = Le vendeur
 * - Product = La fiche produit
 */
public class ProductDAO {

    /**
     * 📋 getAllProducts - Récupère TOUS les produits
     * 
     * 🔁 Étapes :
     * - Connecte à la base
     * - Exécute : SELECT * FROM products
     * - Pour chaque ligne : crée un objet Product
     * - Renvoie la liste des produits
     */
    public static List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("product_id"),           // 🆔 id du produit
                    rs.getString("product_name"),      // 🏷️ nom du produit
                    rs.getDouble("price"),             // 💶 prix
                    rs.getString("category")           // 🗂️ catégorie
                ));
            }
        }

        // 📦 Renvoie la liste remplie (ou vide si aucun produit)
        return products;
    }

    /**
     * 🧾 getAllCategories - Récupère toutes les catégories distinctes
     * 
     * 📦 Exemple de retour : ["Électronique", "Livres", "Jeux"]
     * Idéal pour remplir un menu déroulant (JComboBox)
     */
    public static List<String> getAllCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT category FROM products";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("category"));  // ➕ Ajoute la catégorie
            }
        }

        return categories;
    }

    /**
     * 🔎 getProductsByCategory - Récupère les produits d'une catégorie donnée
     * 
     * @param category La catégorie choisie (ex : "Jeux")
     * @return une liste des produits correspondant
     */
    public static List<Product> getProductsByCategory(String category) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE category = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, category);  // 💡 On remplace le ? par la catégorie
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getString("category")
                ));
            }
        }

        return products;
    }

    public static List<Product> getLowStockProducts(int threshold) throws SQLException {
    List<Product> lowStockProducts = new ArrayList<>();
    // Pour afficher le stock, il faudrait que Product ait un champ 'stock' ou qu'on retourne une autre structure.
    // Cette requête récupère le stock, mais l'objet Product actuel ne le stocke pas.
    String sql = "SELECT p.product_id, p.product_name, p.price, p.category, i.quantity_in_stock " +
                 "FROM products p " +
                 "JOIN inventory i ON p.product_id = i.product_id " +
                 "WHERE i.quantity_in_stock < ?";

    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, threshold);
        try (ResultSet rs = stmt.executeQuery()) { // Mettre ResultSet dans try-with-resources
            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getString("category")
                    // Si tu avais un champ stock dans Product: , rs.getInt("quantity_in_stock")
                );
                // Pour débogage ou si tu adaptes l'affichage :
                System.out.println("Produit en stock bas trouvé: " + product.getProductName() + 
                                   " (Stock actuel: " + rs.getInt("quantity_in_stock") + ")");
                lowStockProducts.add(product);
            }
        }
    }
    return lowStockProducts;
}


}
