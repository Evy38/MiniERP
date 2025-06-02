import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 🛒 CLASSE ORDERDAO - Gère la sauvegarde et la lecture des commandes en base
 * 
 * DAO = Data Access Object
 * 
 * 🎯 Son rôle :
 * - Insérer une nouvelle commande (avec ses lignes) dans la base (tables
 * `orders` et `orderlines`).
 * Cela doit se faire en "transaction" : soit tout réussit, soit tout est
 * annulé.
 * - Récupérer l'historique des commandes d'un client avec les détails des
 * produits.
 */
public class OrderDAO {

    // 💡 Pas de constructeur ni d'attribut "conn" ici.
    // Chaque méthode va gérer sa propre connexion, comme dans CustomerDAO et
    // ProductDAO.
    // Cela simplifie l'utilisation du DAO.

    /**
     * ➕ MÉTHODE createOrder - Enregistre une nouvelle commande en base
     * 
     * Gère une TRANSACTION :
     * 1. Insère dans `orders` et récupère l'ID de la commande créée.
     * 2. Insère toutes les `orderlines` liées à cet ID de commande.
     * 3. Si TOUT se passe bien -> COMMIT (valide les changements).
     * 4. Si la MOINDRE erreur -> ROLLBACK (annule tout).
     * 
     * @throws SQLException Si un problème SQL survient.
     */
    public static void createOrder(Order order) throws SQLException {
        // 📜 Requête pour insérer dans la table 'orders' et récupérer l'ID auto-généré
        // RETURNING order_id est spécifique à PostgreSQL (pour d'autres BDD, c'est
        // différent)
        String orderSql = "INSERT INTO orders (customer_id, order_date, net_amount, tax, total_amount) " +
                "VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?) RETURNING order_id";
        // 💡 Note : J'ai ajouté order_date et initialisé avec CURRENT_TIMESTAMP (date
        // actuelle de la BDD)
        // Si ton Order POJO avait une date, tu la passerais en paramètre.

        String lineSql = "INSERT INTO orderlines (order_id, product_id, quantity, unit_price) " +
                "VALUES (?, ?, ?, ?)";

        Connection conn = null; // Déclarer la connexion à l'extérieur du try pour le finally et le rollback
        try {
            conn = DatabaseManager.getConnection(); // 🔑 Obtenir la connexion
            conn.setAutoCommit(false); // 🏁 DÉBUT DE LA TRANSACTION

            int orderId; // Pour stocker l'ID de la commande créée

            // --- 1. Insérer la commande dans la table 'orders' ---
            try (PreparedStatement orderStmt = conn.prepareStatement(orderSql)) {
                orderStmt.setInt(1, order.getCustomerId());
                // order_date est gérée par CURRENT_TIMESTAMP dans la requête
                orderStmt.setDouble(2, order.getNetAmount());
                orderStmt.setDouble(3, order.getTax());
                orderStmt.setDouble(4, order.getTotalAmount());

                ResultSet rs = orderStmt.executeQuery(); // Exécute et attend le RETURNING
                if (rs.next()) {
                    orderId = rs.getInt("order_id"); // Récupère l'ID généré
                    // Tu pourrais vouloir mettre à jour ton objet Order avec cet ID et la date
                    // order.setOrderId(orderId);
                    // order.setOrderDate(rs.getTimestamp("order_date_if_returned")); // Si tu
                    // retournais la date
                } else {
                    throw new SQLException("Échec de la création de la commande, aucun ID retourné.");
                }
            }

            // --- 2. Insérer les lignes de commande dans 'orderlines' (en mode batch) ---
            try (PreparedStatement lineStmt = conn.prepareStatement(lineSql)) {
                for (OrderLine line : order.getLines()) {
                    lineStmt.setInt(1, orderId); // Utilise l'ID de la commande fraîchement créée
                    lineStmt.setInt(2, line.getProduct().getProductId()); // ✅ CORRIGÉ: getProductId()
                    lineStmt.setInt(3, line.getQuantity());
                    lineStmt.setDouble(4, line.getProduct().getPrice()); // Prix unitaire du produit au moment de la
                                                                         // commande

                    lineStmt.addBatch(); // Ajoute la requête au lot
                }
                lineStmt.executeBatch(); // Exécute toutes les requêtes du lot d'un coup
            }

            //--- 3. Mettre à jour le stock dans 'inventory' --- ✨
            String updateStockSql = "UPDATE inventory SET quantity_in_stock = quantity_in_stock - ? " +
                    "WHERE product_id = ?";
            try (PreparedStatement stockStmt = conn.prepareStatement(updateStockSql)) {
                for (OrderLine line : order.getLines()) {
                    stockStmt.setInt(1, line.getQuantity()); // Quantité à décrémenter
                    stockStmt.setInt(2, line.getProduct().getProductId()); // ID du produit

                    // Exécuter la mise à jour pour chaque ligne.
                    // Si tu veux faire un batch, c'est aussi possible :
                    // stockStmt.addBatch();
                    int rowsAffected = stockStmt.executeUpdate(); // Exécute l'update
                    if (rowsAffected == 0) {
                        // Ce cas signifie que le produit n'a pas été trouvé dans l'inventaire,
                        // ou que le stock était déjà à 0 et que la BDD a une contrainte pour ne pas
                        // aller en négatif
                        // si la soustraction n'a pas eu lieu.
                        // Tu pourrais lever une exception ici si c'est un problème critique.
                        System.err.println("Avertissement: Stock non mis à jour pour product_id " +
                                line.getProduct().getProductId() +
                                ". Le produit n'existe peut-être pas dans l'inventaire ou le stock est déjà à 0.");
                        // Pour un TP, un message d'erreur peut suffire, mais dans une vraie app,
                        // cela pourrait invalider la transaction si le stock doit absolument être mis à
                        // jour.
                        // throw new SQLException("Impossible de mettre à jour le stock pour le produit
                        // ID: " + line.getProduct().getProductId());
                    }
                }
                // Si tu utilisais addBatch():
                // stockStmt.executeBatch();
            }

            conn.commit(); // ✅ FIN DE LA TRANSACTION : Tout s'est bien passé, on valide !
            System.out.println("Commande créée avec succès. ID: " + orderId);

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    System.err.println("Transaction en cours d'annulation pour la commande.");
                    conn.rollback(); // ❌ ANNULATION DE LA TRANSACTION : Quelque chose a mal tourné
                } catch (SQLException excepRollback) {
                    System.err.println("Erreur lors du rollback de la transaction : " + excepRollback.getMessage());
                }
            }
            // Relance l'exception originale pour que l'appelant soit informé
            throw new SQLException("Erreur lors de la création de la commande: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Remettre autoCommit à true (important pour les autres usages de la
                                              // connexion)
                    conn.close(); // Fermer la connexion
                } catch (SQLException e) {
                    System.err.println(
                            "Erreur lors de la réinitialisation de autoCommit ou de la fermeture de la connexion: "
                                    + e.getMessage());
                }
            }
        }
    }

    /**
     * 📜 MÉTHODE getOrderDetailsByCustomer - Récupère l'historique détaillé des
     * commandes d'un client
     * 
     * Utilise la classe OrderDetail pour stocker chaque ligne de résultat de la
     * jointure.
     * 
     * @param customerId L'ID du client.
     * @return Une List<OrderDetail> pour affichage.
     * @throws SQLException Si un problème SQL survient.
     */
    public static List<OrderDetail> getOrderDetailsByCustomer(int customerId) throws SQLException {
        List<OrderDetail> orderDetails = new ArrayList<>();

        // Requête SQL avec jointures pour récupérer toutes les infos nécessaires
        String sql = "SELECT " +
                "o.order_id, o.order_date, o.net_amount, o.tax, o.total_amount, " + // Infos de la commande
                "p.product_name, ol.quantity, ol.unit_price " + // Infos de la ligne de produit
                "FROM orders o " +
                "JOIN orderlines ol ON o.order_id = ol.order_id " + // Jointure entre orders et orderlines
                "JOIN products p ON ol.product_id = p.product_id " + // Jointure entre orderlines et products
                "WHERE o.customer_id = ? " + // Filtre sur le client
                "ORDER BY o.order_date DESC, o.order_id, p.product_name"; // Tri pour un affichage logique

        // Utilisation du try-with-resources pour la connexion et le statement
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId); // Remplace le '?' par l'ID du client

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Construction d'un objet OrderDetail pour chaque ligne de résultat
                    OrderDetail detail = new OrderDetail(
                            rs.getInt("order_id"),
                            rs.getString("order_date"), // Si order_date est de type DATE ou TIMESTAMP en BDD, préférer
                                                        // rs.getDate() ou rs.getTimestamp()
                            rs.getDouble("net_amount"), // ✅ CORRIGÉ
                            rs.getDouble("tax"), // ✅ CORRIGÉ
                            rs.getDouble("total_amount"), // ✅ CORRIGÉ
                            rs.getString("product_name"),
                            rs.getInt("quantity"),
                            rs.getDouble("unit_price") // ✅ CORRIGÉ
                    );
                    orderDetails.add(detail);
                }
            }
        }
        return orderDetails;
    }
}