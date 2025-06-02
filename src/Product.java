/**
 * 📦 CLASSE PRODUCT - Représente un produit dans ton application ERP
 * 
 * 👉 C'est un POJO : un objet Java tout simple, comme une fiche produit
 * 🎯 Son rôle : stocker les données d’un article (nom, prix, catégorie, etc.)
 */
public class Product {

    // 🔐 ATTRIBUTS PRIVÉS = infos qu'on ne peut lire qu'avec les "fenêtres" (getters)
    private int productId;         // 🆔 L’identifiant du produit
    private String productName;    // 🏷️ Le nom du produit
    private double price;          // 💶 Le prix
    private String category;       // 🗂️ La catégorie (ex: "Électronique", "Vêtements")

    /**
     * 🏗️ CONSTRUCTEUR - Sert à "fabriquer" un nouveau produit
     * Quand tu fais new Product(...), ce code est exécuté
     */
    public Product(int productId, String productName, double price, String category) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.category = category;
    }

    // 🔍 LES FENÊTRES (getters) pour lire les données privées

    public int getProductId() { return productId; }         
    public String getProductName() { return productName; }   
    public double getPrice() { return price; }
    public String getCategory() { return category; }
}
