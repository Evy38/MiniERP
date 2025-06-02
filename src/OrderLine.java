/**
 * 🛒 CLASSE ORDERLINE - Représente une ligne dans une commande
 * 
 * C'est un POJO qui associe un Produit (Product) à une Quantité commandée.
 * Une commande (Order) sera composée d'une liste de ces OrderLine.
 * 
 * 🎯 Son rôle :
 *   - Stocker quel produit a été commandé.
 *   - Stocker combien d'exemplaires de ce produit ont été commandés.
 * 
 * Exemple : 2 x "T-shirt Bleu" (OrderLine 1) + 1 x "Casquette Rouge" (OrderLine 2)
 */
public class OrderLine {

    // --- 📋 ATTRIBUTS ---
    private Product product;    // Le produit concerné par cette ligne de commande
    private int quantity;       // La quantité de ce produit commandée

    /**
     * 🏗️ CONSTRUCTEUR - Crée une nouvelle ligne de commande
     */
    public OrderLine(Product product, int quantity) {
        // 💡 On vérifie souvent ici si product n'est pas null et si quantity > 0
        //    mais pour un TP, on peut simplifier.
        this.product = product;
        this.quantity = quantity;
    }

    // --- 🔍 GETTERS (pour lire les informations de la ligne) ---
    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    // 💡 REMARQUE : Pas de SETTERS !
    // C'est un bon choix ici. Une fois qu'une ligne de commande est créée
    // (par exemple, "3 stylos bleus"), on ne la modifie généralement pas.
    // Si l'utilisateur veut changer, on supprime cette ligne et on en crée une nouvelle.
    // Cela rend l'objet "immutable" (non modifiable après création), ce qui est bien !
}