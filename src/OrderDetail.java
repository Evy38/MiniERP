/**
 * 📜 CLASSE ORDERDETAIL - Représente une LIGNE DÉTAILLÉE d'un historique de commande
 * 
 * C'est un POJO "plat" souvent utilisé pour afficher les résultats
 * d'une requête SQL qui fait des jointures entre plusieurs tables (orders, orderlines, products).
 * Chaque objet OrderDetail correspondrait à UNE ligne dans une JTable d'historique.
 * 
 * 🎯 Son rôle :
 *   - Afficher de manière combinée des infos de la commande globale ET d'une ligne spécifique.
 *   - Faciliter le remplissage d'une JTable pour l'historique.
 * 
 * ⚠️ Important : Ce n'est PAS l'objet principal pour CRÉER une commande.
 * Pour ça, on utilise Order (qui contient des OrderLine).
 */
public class OrderDetail {

    // --- ℹ️ Infos de la COMMANDE GLOBALE ---
    private int orderId;            // 🆔 L'ID de la commande
    private String orderDate;       // 📅 La date de la commande
    private double netAmount;       // 💰 Montant HT global de la commande
    private double tax;             // ➕ TVA globale de la commande
    private double totalAmount;     // 💶 Total TTC global de la commande

    // --- 🛍️ Infos de la LIGNE DE PRODUIT SPÉCIFIQUE DANS CETTE COMMANDE ---
    private String productName;     // 🏷️ Nom du produit de CETTE ligne
    private int quantity;           // 🔢 Quantité de CE produit
    private double unitPrice;       // 💲 Prix unitaire de CE produit

    /**
     * 🏗️ CONSTRUCTEUR - Remplit toutes les infos d'une ligne d'historique
     * 
     * Typiquement, les valeurs viendront directement d'un ResultSet après une requête SQL avec jointures.
     */
    public OrderDetail(int orderId, String orderDate, double netAmount, double tax, double totalAmount,
                       String productName, int quantity, double unitPrice) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.netAmount = netAmount; // Attention: est-ce le netAmount de la ligne ou de la commande totale?
        this.tax = tax;             // Idem pour tax et totalAmount. Le nommage suggère la commande totale.
        this.totalAmount = totalAmount; 
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // --- 🔍 GETTERS (pour lire les informations) ---
    public int getOrderId() { return orderId; }
    public String getOrderDate() { return orderDate; }
    public double getNetAmount() { return netAmount; } // Total HT de la commande ?
    public double getTax() { return tax; }             // TVA totale de la commande ?
    public double getTotalAmount() { return totalAmount; } // Total TTC de la commande ?
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    
    // 💡 On pourrait ajouter une méthode pour calculer le sous-total de CETTE ligne :
    // public double getLineSubTotal() { return this.quantity * this.unitPrice; }
    // Mais cela dépend si netAmount/tax/totalAmount sont pour la commande ou la ligne.
    // D'après leur nom, ils semblent être pour la commande entière.
}