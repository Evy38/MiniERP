// MainMenu.java

import javax.swing.*;
import java.awt.*;
// Pas besoin d'importer ActionEvent/ActionListener si on utilise des lambdas pour les actions

// Adapte ces imports si tes classes Viewer/Form sont dans des packages différents
// import com.yourproject.view.CustomerViewer;
// import com.yourproject.view.ProductViewer;
// import com.yourproject.view.OrderForm;
// import com.yourproject.view.OrderHistoryViewer;

/**
 * 🏠 CLASSE MAINMENU - Fenêtre principale de l'application Mini ERP.
 * 🎯 Rôle : Point d'accès central aux fonctionnalités.
 */
public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Mini ERP - Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Quitte l'app quand on ferme cette fenêtre
        
        // Panel pour les boutons, organisé en grille verticale
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 15, 15)); // 4 lignes, 1 colonne, espacements
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60)); // Marges extérieures

        // --- Création et ajout des Boutons ---
        JButton manageCustomersBtn = new JButton("🧑 Gérer les Clients");
        JButton viewProductsBtn = new JButton("📦 Voir les Produits");
        JButton createOrderBtn = new JButton("🛒 Créer une Commande");
        JButton viewHistoryBtn = new JButton("📜 Historique des Commandes");

        // Style commun (optionnel, pour une meilleure lisibilité)
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        manageCustomersBtn.setFont(buttonFont);
        viewProductsBtn.setFont(buttonFont);
        createOrderBtn.setFont(buttonFont);
        viewHistoryBtn.setFont(buttonFont);

        buttonPanel.add(manageCustomersBtn);
        buttonPanel.add(viewProductsBtn);
        buttonPanel.add(createOrderBtn);
        buttonPanel.add(viewHistoryBtn);

        add(buttonPanel, BorderLayout.CENTER); // Ajoute le panel au centre de la JFrame

        // --- Actions des Boutons (Lambdas pour la concision) ---
        manageCustomersBtn.addActionListener(_ -> openWindow(CustomerViewer.class));
        viewProductsBtn.addActionListener(_ -> openWindow(ProductViewer.class));
        createOrderBtn.addActionListener(_ -> openWindow(OrderForm.class)); // Utilise le nom de TA classe OrderForm
        
        // Pour l'historique, on ouvre CustomerViewer car c'est là qu'on sélectionne un client pour voir son historique
        // Si OrderHistoryViewer peut être lancé seul et permet de choisir un client, on peut l'appeler directement.
        // Pour l'instant, on suit la logique où l'historique est lié à un client sélectionné dans CustomerViewer.
        viewHistoryBtn.addActionListener(_ -> {
            // Si OrderHistoryViewer prend un ID client initial (ex: 0 pour aucun, ou le dernier utilisé)
            // new OrderHistoryViewer(0).setVisible(true); 
            // OU, pour forcer la sélection via CustomerViewer :
            openWindow(CustomerViewer.class);
            JOptionPane.showMessageDialog(this, 
                "Sélectionnez un client dans la liste, puis cliquez sur 'Afficher l'historique'.",
                "Info Historique", JOptionPane.INFORMATION_MESSAGE);
        });

        pack(); // Ajuste la taille de la fenêtre au contenu
        setLocationRelativeTo(null); // Centrer
    }

    /**
     * 💡 Méthode générique pour ouvrir une nouvelle fenêtre JFrame.
     * S'assure que l'ouverture se fait sur l'Event Dispatch Thread.
     * @param frameClass La classe du JFrame à instancier et afficher.
     */
    private <T extends JFrame> void openWindow(Class<T> frameClass) {
        SwingUtilities.invokeLater(() -> {
            try {
                T frame = frameClass.getDeclaredConstructor().newInstance();
                frame.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Erreur à l'ouverture de la fenêtre : " + frameClass.getSimpleName() + "\n" + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        System.out.println("Ouverture demandée pour : " + frameClass.getSimpleName());
    }

    // La méthode main de l'application doit être dans App.java et lancer ce MainMenu.
}