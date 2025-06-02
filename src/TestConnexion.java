import java.sql.Connection;     // 🎫 Pour avoir le "ticket d'accès" à la base
import java.sql.ResultSet;       // 📋 Pour récupérer les résultats de tes requêtes
import java.sql.Statement;       // 📝 Pour écrire et envoyer des requêtes SQL

/**
 * 🧪 Classe de TEST pour vérifier que ta connexion à la base fonctionne
 * 
 * C'est comme faire un "test de micro" avant un concert :
 * "1, 2, 3... est-ce que ça marche ?"
 */
public class TestConnexion {
    
    /**
     * 🚀 Méthode principale - c'est ici que ton programme démarre !
     * Comme le bouton "Play" de ton programme
     */
    public static void main(String[] args) {
        
        // 🔒 TRY-WITH-RESOURCES = la façon MAGIQUE de gérer les connexions !
        // Tout ce qui est entre parenthèses se ferme automatiquement à la fin
        try (Connection conn = DatabaseManager.getConnection();    // 🎫 Je prends mon ticket d'accès
             Statement stmt = conn.createStatement()) {            // 📝 Je prépare mon "stylo SQL"

            // 🗃️ J'envoie ma requête SQL à la base de données
            // "Donne-moi les 5 premiers clients de ta table customers"
            ResultSet rs = stmt.executeQuery("SELECT * FROM customers LIMIT 5");
            
            // 📋 ResultSet = comme un tableau Excel avec tes réponses
            // rs.next() = "passe à la ligne suivante"
            // Tant qu'il y a des lignes, continue à lire !
            while (rs.next()) {
                // 📖 Je lis chaque ligne et j'affiche les infos
                // rs.getInt("customer_id") = récupère le nombre dans la colonne "customer_id"
                // rs.getString("customer_name") = récupère le texte dans la colonne "customer_name"
                System.out.println("Client ID: " + rs.getInt("customer_id") +
                                   ", Nom: " + rs.getString("customer_name"));
            }
            
            // 🎉 Si on arrive ici, c'est que tout fonctionne !

        } catch (Exception e) {
            // 💥 Si quelque chose se passe mal, on arrive ici
            // e.printStackTrace() = affiche l'erreur complète pour débugger
            
            System.out.println("❌ Oups ! Problème de connexion :");
            e.printStackTrace();
            
            // Causes possibles d'erreur :
            // - PostgreSQL pas démarré
            // - Mauvais mot de passe dans DatabaseManager
            // - Base "store" n'existe pas
            // - Table "customers" n'existe pas
        }
        
        // ✨ Magie du try-with-resources :
        // Ici, Connection et Statement se ferment AUTOMATIQUEMENT !
        // Même s'il y a eu une erreur, tout est propretement fermé
    }
}

/**
 * 💡 CE QUE CE CODE FAIT EN RÉSUMÉ :
 * 
 * 1. 🔑 Demande l'accès à la base (DatabaseManager)
 * 2. 📝 Prépare un "crayon SQL" (Statement)  
 * 3. 🗣️ Demande : "Montre-moi 5 clients !" (executeQuery)
 * 4. 📋 Reçoit la réponse dans un "tableau" (ResultSet)
 * 5. 🔄 Lit ligne par ligne et affiche
 * 6. 🚪 Ferme tout proprement (automatique)
 * 
 * ✅ Points forts de ton code :
 * - Tu uses try-with-resources (BRAVO !)
 * - Tu gères les erreurs
 * - Code simple et efficace
 * 
 * 🎯 Résultat attendu si ça marche :
 * Client ID: 1, Nom: Jean Dupont
 * Client ID: 2, Nom: Marie Martin
 **/