import java.sql.*;
import java.text.SimpleDateFormat; // Pour formater la date
import java.util.ArrayList;
import java.util.List;

/**
 * 📜 CLASSE ORDERHISTORYDAO - Spécialiste pour récupérer l'historique des commandes
 *                             sous forme de tableau de chaînes de caractères.
 * 
 * DAO = Data Access Object
 * 
 * 🎯 Son rôle : Fournir les données de l'historique des commandes d'un client,
 * prêtes à être affichées (par exemple dans une JTable).
 * Chaque "ligne" de l'historique est un tableau de String.
 */
public class OrderHistoryDAO {

    /**
     * 📋 MÉTHODE getOrderHistoryByCustomerId - Récupère l'historique sous forme List<String[]>
     * 
     * @param customerId L'ID du client dont on veut l'historique.
     * @return Une liste de tableaux de String. Chaque tableau représente une ligne de produit
     *         dans une commande (order_id, order_date, product_name, quantity, unit_price).
     * @throws SQLException Si un problème SQL survient.
     */
    public static List<String[]> getOrderHistoryByCustomerId(int customerId) throws SQLException {
        List<String[]> history = new ArrayList<>();

        // Requête SQL utilisant un "text block" pour une meilleure lisibilité
        String sql = """
            SELECT o.order_id, o.order_date, p.product_name, ol.quantity, ol.unit_price
            FROM orders o
            JOIN orderlines ol ON o.order_id = ol.order_id
            JOIN products p ON ol.product_id = p.product_id
            WHERE o.customer_id = ?
            ORDER BY o.order_date DESC, o.order_id, p.product_name 
        """;
        // Le tri est important pour un affichage cohérent de l'historique.

        // try-with-resources pour gérer automatiquement la fermeture des ressources
        try (Connection conn = DatabaseManager.getConnection();      // 🔑 Connexion à la base
             PreparedStatement stmt = conn.prepareStatement(sql)) { // 👨‍🍳 Préparation de la requête

            stmt.setInt(1, customerId); // Remplacer le '?' par l'ID du client

            try (ResultSet rs = stmt.executeQuery()) { // 🧺 Exécution et récupération des résultats (ResultSet aussi dans le try)
                
                // Optionnel : pour formater la date proprement si elle vient d'un type DATE/TIMESTAMP
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); // Ou "dd/MM/yyyy" si pas d'heure

                while (rs.next()) { // 🚶‍♂️ Parcourir chaque ligne de résultat
                    String[] row = new String[5]; // Crée un tableau pour stocker les 5 colonnes

                    // ✍️ Remplir le tableau avec les données de la ligne actuelle du ResultSet
                    //    Il est préférable de récupérer avec le bon type puis convertir en String.
                    row[0] = String.valueOf(rs.getInt("order_id"));
                    
                    // Gestion de la date (exemple si c'est un Timestamp en BDD)
                    Timestamp orderTimestamp = rs.getTimestamp("order_date");
                    row[1] = (orderTimestamp != null) ? dateFormat.format(orderTimestamp) : "N/A";
                    // Si order_date est un VARCHAR en BDD, rs.getString("order_date") suffit.
                    // Si c'est un type DATE, utiliser rs.getDate("order_date").

                    row[2] = rs.getString("product_name");
                    row[3] = String.valueOf(rs.getInt("quantity"));
                    row[4] = String.format("%.2f", rs.getDouble("unit_price")); // Formate le prix avec 2 décimales

                    history.add(row); // ➕ Ajouter la ligne formatée à la liste d'historique
                }
            }
        }
        return history;
    }

    // --- Pour tester rapidement cette méthode (optionnel) ---
    /*
    public static void main(String[] args) {
        try {
            // Remplacez 1 par un customer_id qui existe dans votre base et qui a des commandes
            List<String[]> customerHistory = getOrderHistoryByCustomerId(1); 
            if (customerHistory.isEmpty()) {
                System.out.println("Aucun historique de commande trouvé pour ce client.");
            } else {
                System.out.println("Historique des commandes pour le client ID 1:");
                System.out.println("ID Commande | Date Commande      | Produit         | Qté | Prix Unit.");
                System.out.println("----------------------------------------------------------------------");
                for (String[] row : customerHistory) {
                    System.out.printf("%-11s | %-18s | %-15s | %-3s | %s%n", 
                                      row[0], row[1], row[2], row[3], row[4]);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'historique des commandes:");
            e.printStackTrace();
        }
    }
    */
}