import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 🏪 Cette classe sert de "portier" pour accéder à ta base de données
 * 
 * Imagine que ta base de données est un magasin fermé à clé.
 * Cette classe garde les clés et te les donne quand tu en as besoin !
 */
public class DatabaseManager {
    
    // 📍 L'ADRESSE de ta base de données (comme une adresse postale)
    // "jdbc:postgresql://" = dit à Java "je veux parler à PostgreSQL"
    // "localhost:5432" = sur mon ordinateur, port 5432 (porte d'entrée standard de PostgreSQL)
    // "store" = le nom de ma base de données
    private static final String URL = "jdbc:postgresql://localhost:5432/store";
    
    // 🔑 Les identifiants pour se connecter (comme login/mot de passe)
    private static final String USER = "postgres"; 
    private static final String PASSWORD = "root";

    /**
     * 🚪 Cette méthode ouvre la porte de ta base de données
     * 
     * Pourquoi "static" ? = Tu peux l'utiliser directement sans créer d'objet
     * C'est comme avoir une clé universelle !
     * 
     * Comment l'utiliser : DatabaseManager.getConnection()
     * 
     * Elle te rend un "ticket d'accès" (Connection) pour faire tes requêtes SQL
     */
    public static Connection getConnection() throws SQLException {
        // 🔌 Cette ligne fait la "poignée de main" avec PostgreSQL
        // Elle dit : "Salut PostgreSQL ! C'est moi, voici mes identifiants"
        // Si tout va bien, PostgreSQL répond : "OK, voici ton accès !"
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    /*
     * 💡 POUR BIEN COMPRENDRE :
     * 
     * Cette classe = le gardien qui a les clés de la base
     * Connection = le ticket d'entrée qu'il te donne
     * 
     * ⚠️ RÈGLE D'OR : Toujours fermer la Connection après usage !
     * Sinon c'est comme laisser la porte du magasin ouverte 🚪
     * 
     * ✅ La bonne façon de faire :
     * try (Connection conn = DatabaseManager.getConnection()) {
     *     // Ici tu fais tes requêtes SQL
     *     // SELECT, INSERT, UPDATE, etc.
     * } // ← La connexion se ferme toute seule ici !
     * 
     * ❌ Ne JAMAIS faire :
     * Connection conn = DatabaseManager.getConnection();
     * // faire ses trucs...
     * // et oublier de fermer ! = FUITE MÉMOIRE !
     */
}