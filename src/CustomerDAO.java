import java.sql.*;           // 🎫 Pour gérer les connexions et requêtes SQL
import java.util.ArrayList;   // 📋 Pour créer une liste qui grandit automatiquement
import java.util.List;        // 📋 Interface pour tous les types de listes

/**
 * 🏪 CLASSE CUSTOMERDAO - Ton "employé spécialisé clients"
 * 
 * DAO = Data Access Object = Objet d'Accès aux Données
 * 
 * 🎯 Son rôle : Faire le lien entre ta base PostgreSQL et tes objets Customer Java
 * 
 * Analogie 🏪 :
 * - Ta base de données = Le stock du magasin
 * - CustomerDAO = L'employé qui va chercher les infos clients dans le stock
 * - Customer = La fiche client qu'il te rapporte
 */
public class CustomerDAO {

    /**
     * 📋 MÉTHODE getAllCustomers() - Récupère TOUS les clients
     * 
     * 🔄 Processus :
     * 1. Va dans la base de données
     * 2. Demande "SELECT * FROM customers" (= "Donne-moi tous les clients")
     * 3. Pour chaque ligne trouvée, crée un objet Customer
     * 4. Met tous ces objets dans une liste
     * 5. Te renvoie la liste complète
     */
    public static List<Customer> getAllCustomers() {
        
        // 📦 Création d'une liste vide pour stocker nos clients
        // ArrayList = Liste qui peut grandir automatiquement
        List<Customer> customers = new ArrayList<>();
        
        // 📝 Requête SQL : "Donne-moi toutes les colonnes de la table customers"
        String sql = "SELECT * FROM customers";

        // 🎫 Try-with-resources : Ouvre connexion + statement + resultset
        // Et les ferme automatiquement à la fin (même si erreur !)
        try (Connection conn = DatabaseManager.getConnection();      // 🔑 Clé d'accès à la base
             Statement stmt = conn.createStatement();                // 📝 Préparateur de requêtes
             ResultSet rs = stmt.executeQuery(sql)) {                // 📋 Résultats de la requête

            // 🔄 Boucle : Tant qu'il y a une ligne suivante dans les résultats
            while (rs.next()) {
                
                // 🏗️ Construction d'un objet Customer avec les données de cette ligne
                Customer c = new Customer(
                    rs.getInt("customer_id"),         // 🔢 Récupère l'ID (nombre entier)
                    rs.getString("customer_name"),   // 📝 Récupère le nom (texte)
                    rs.getString("email"),           // 📧 Récupère l'email (texte)
                    rs.getString("phone")            // 📞 Récupère le téléphone (texte)
                );
                
                // ➕ Ajoute ce client à notre liste
                customers.add(c);
            }

        } catch (SQLException e) {
            // 🚨 Si problème SQL : affiche l'erreur complète
            // (connexion fermée, table inexistante, etc.)
            e.printStackTrace();
        }

        // 📦 Renvoie la liste complète (peut être vide si aucun client)
        return customers;
    }
}

/* 
🎓 POINTS CLÉS À RETENIR :

✅ Pattern DAO : Sépare la logique base de données du reste
✅ Try-with-resources : Gestion automatique des ressources
✅ ResultSet.next() : Avance ligne par ligne dans les résultats
✅ rs.getString("colonne") : Récupère la valeur d'une colonne
✅ Méthode statique : Peut être appelée sans créer d'objet CustomerDAO

🤔 QUESTIONS POUR COMPRENDRE :
- Pourquoi on utilise ArrayList plutôt qu'un tableau ?
- Que se passe-t-il si la table customers est vide ?
- Pourquoi la méthode est-elle static ?

💡 AMÉLIORATIONS POSSIBLES :
- Ajouter un paramètre limit pour limiter le nombre de résultats
- Ajouter une méthode getCustomerById(int id)
- Meilleure gestion d'erreurs avec des exceptions personnalisées
*/