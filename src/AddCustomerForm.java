// AddCustomerForm.java
import javax.swing.*;    // 🪟 Pour créer l'interface graphique (Swing)
import java.awt.*;       // 🎨 Pour gérer la disposition (GridLayout, BorderLayout, FlowLayout)
import java.sql.*;       // 🛢️ Pour se connecter à PostgreSQL

// Adapte import DatabaseManager
// import com.yourproject.util.DatabaseManager;

/**
 * ➕ FORMULAIRE D'AJOUT DE CLIENT
 * 
 * Interface Swing pour saisir un nouveau client
 * 
 * 🎯 Quand tu cliques sur "Ajouter", ça appelle une fonction SQL côté serveur :
 * CALL new_customer(nom, email, téléphone)
 */
public class AddCustomerForm extends JFrame {

    // 🧾 Champs de saisie
    private JTextField nameField, emailField, phoneField;

    // 🏗️ Constructeur = crée l'interface
    public AddCustomerForm() {
        setTitle("Ajouter un Client");                // 🪧 Titre de la fenêtre
        setSize(350, 200);                            // 📐 Taille un peu ajustée
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // ❌ Ferme juste cette fenêtre
        setLocationRelativeTo(null); // Centrer
        setLayout(new BorderLayout(10, 10));          // 🗺️ Utiliser BorderLayout pour la structure globale

        // --- Panel pour les champs de saisie (au centre) ---
        JPanel formFieldsPanel = new JPanel(new GridLayout(3, 2, 5, 5)); // 3 lignes pour les champs
        formFieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Marge interne

        // 📦 Ligne 1 : champ nom
        formFieldsPanel.add(new JLabel("Nom :"));
        nameField = new JTextField();
        formFieldsPanel.add(nameField);

        // 📧 Ligne 2 : champ email
        formFieldsPanel.add(new JLabel("Email :"));
        emailField = new JTextField();
        formFieldsPanel.add(emailField);

        // 📱 Ligne 3 : champ téléphone
        formFieldsPanel.add(new JLabel("Téléphone :"));
        phoneField = new JTextField();
        formFieldsPanel.add(phoneField);
        
        add(formFieldsPanel, BorderLayout.CENTER); // Ajoute le panel des champs au centre

        // --- Panel pour les boutons (en bas) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Aligner les boutons à droite
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Marge externe

        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(_ -> addCustomer());  // 🎯 Action quand on clique

        // ✨ AJOUT DU BOUTON RETOUR/ANNULER ✨
        JButton cancelButton = new JButton("Annuler / Retour");
        cancelButton.addActionListener(_ -> this.dispose()); // Action : ferme cette fenêtre

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH); // Ajoute le panel des boutons en bas
    }

    /**
     * 💾 Ajoute un client en base via la fonction SQL new_customer(...)
     */
    private void addCustomer() {
        // 🧹 On récupère ce que l’utilisateur a saisi
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        // ❌ Si un champ est vide → on bloque (Email et Nom sont souvent les plus importants)
        if (name.isEmpty() || email.isEmpty()) { // Rendre le téléphone optionnel par exemple
            JOptionPane.showMessageDialog(this, "Le nom et l'email sont obligatoires.", "Champs Requis", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 🧠 On appelle la fonction stockée PostgreSQL
        try (Connection conn = DatabaseManager.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL new_customer(?, ?, ?)}")) { // Syntaxe standard JDBC

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone); // Si phone est vide, il sera envoyé comme chaîne vide
            stmt.execute();  // 📞 Envoie la requête à PostgreSQL

            // ✅ Affiche un message de confirmation
            JOptionPane.showMessageDialog(this, "Client '" + name + "' ajouté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            
            // Vider les champs et remettre le focus
            nameField.setText("");
            emailField.setText("");
            phoneField.setText("");
            nameField.requestFocusInWindow();

        } catch (SQLException ex) {
            // ⚠️ Si erreur SQL (fonction manquante, doublon, etc.)
            ex.printStackTrace(); // Pour le débogage
            String errorMessage = "Erreur lors de l'ajout du client : \n" + ex.getMessage();
            if (ex.getSQLState() != null && ex.getSQLState().equals("23505")) { 
                errorMessage = "Erreur : Un client avec cet email ou ces informations existe déjà.";
            }
            JOptionPane.showMessageDialog(this, errorMessage, "Erreur Base de Données", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 🚀 Pour tester indépendamment l'interface
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AddCustomerForm().setVisible(true);
        });
    }
}