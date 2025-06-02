import java.util.ArrayList; // 📋 Pour la liste des lignes de commande (qui peut grandir)
import java.util.List;      // 📦 L'interface pour tous les types de listes

/**
 * 🧾 CLASSE ORDER - Représente une commande passée par un client
 * 
 * 
 * 🎯 Son rôle :
 *   - Garder l'info du client qui a commandé.
 *   - Stocker la liste des produits commandés (les OrderLine).
 *   - Calculer automatiquement le total (HT, TVA, TTC).
 */
public class Order {
    
    // --- 📋 ATTRIBUTS ---
    private int customerId;                 // 🧑‍💼 L'ID du client qui passe la commande
    private List<OrderLine> lines;          // 🛒 La liste des "lignes" de produits dans cette commande
                                            //    Initialisée directement ici pour éviter un null ! Très bien !
    private double netAmount;               // 💰 Montant Hors Taxe (calculé)
    private double tax;                     // ➕ Montant de la TVA (calculé)
    private double totalAmount;             // 💶 Montant Total TTC (calculé)

    /**
     * 🏗️ CONSTRUCTEUR - Crée une nouvelle commande pour un client donné
     */
    public Order(int customerId) {
        this.customerId = customerId;
        this.lines = new ArrayList<>(); // Important : on crée une liste vide prête à l'emploi !
        // Les montants (netAmount, tax, totalAmount) sont à 0 par défaut, c'est bien.
    }

    /**
     * ➕ MÉTHODE addLine - Ajoute un produit (avec sa quantité) à la commande
     */
    public void addLine(OrderLine line) {
        this.lines.add(line);        // Ajoute la nouvelle ligne à notre liste
        recalculateTotals();         // À chaque ajout, on recalcule TOUT !
    }

    /**
     * 🔄 MÉTHODE recalculateTotals - Met à jour les montants (HT, TVA, TTC)
     * 
     * Cette méthode est privée : elle n'est utilisée qu'à l'intérieur de la classe Order.
     * C'est une bonne pratique pour garder le contrôle.
     */
    private void recalculateTotals() {
        this.netAmount = 0; // On repart de zéro pour le calcul
        
        // On parcourt chaque ligne de la commande
        for (OrderLine line : this.lines) {
            // Montant HT de la ligne = Prix du produit * Quantité
            this.netAmount += line.getProduct().getPrice() * line.getQuantity();
        }
        
        this.tax = this.netAmount * 0.20; // Calcul de la TVA (ici, 20% codé en dur)
        this.totalAmount = this.netAmount + this.tax; // Calcul du TTC
    }

    // --- 🔍 GETTERS (pour lire les informations de la commande) ---

    public int getCustomerId() {
        return customerId;
    }

    public List<OrderLine> getLines() {
        return lines; // Renvoie la liste complète des lignes
    }

    public double getNetAmount() {
        return netAmount;
    }

    public double getTax() {
        return tax;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
    

}