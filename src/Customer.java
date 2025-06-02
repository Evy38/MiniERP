/**
 * 🧑‍💼 CLASSE CUSTOMER - Représente un client dans ton application
 * 
 * C'est ce qu'on appelle un POJO (Plain Old Java Object)
 * = Un objet Java simple qui contient juste des données
 * 
 * 🎯 Son rôle : Être le "moule" pour créer des objets clients
 * Comme un formulaire vierge qu'on remplit avec les infos d'un client !
 */
public class Customer {
    
    // 📋 LES ATTRIBUTS (les "cases" de ton formulaire client)
    // private = personne d'autre ne peut les modifier directement !
    // C'est comme avoir des tiroirs fermés à clé 🔐
    
    private int customerId;     // 🆔 L'identifiant unique du client (clé primaire en BDD)
    private String name;        // 👤 Le nom du client  
    private String email;       // 📧 Son adresse email
    private String phone;       // 📱 Son numéro de téléphone

    /**
     * 🏗️ CONSTRUCTEUR - La "machine à fabriquer des clients"
     * 
     * Quand tu fais : new Customer(1, "Jean", "jean@mail.com", "0123456789")
     * Ce constructeur se déclenche et remplit tous les attributs !
     */
    public Customer(int customerId, String name, String email, String phone) {
        // this.customerId = le customerId de CET objet = le paramètre customerId
        this.customerId = customerId;   // 🏷️ "this" = cet objet précis qu'on est en train de créer
        this.name = name;
        this.email = email;
        this.phone = phone;
        
        // 💡 Après ces lignes, ton objet Customer est prêt à être utilisé !
    }

    // 🔍 LES GETTERS - Les "fenêtres" pour LIRE les données privées
    // Comme regarder dans les tiroirs fermés à travers une vitre !
    
    /**
     * 📖 Récupère l'ID du client
     * Usage : int id = monClient.getCustomerId();
     */
    public int getCustomerId() { 
        return customerId; 
    }
    
    /**
     * 📖 Récupère le nom du client  
     * Usage : String nom = monClient.getName();
     */
    public String getName() { 
        return name; 
    }
    
    /**
     * 📖 Récupère l'email du client
     * Usage : String mail = monClient.getEmail();
     */
    public String getEmail() { 
        return email; 
    }
    
    /**
     * 📖 Récupère le téléphone du client
     * Usage : String tel = monClient.getPhone();
     */
    public String getPhone() { 
        return phone; 
    }
    
    // 💡 REMARQUE : Tu n'as pas de SETTERS !
    // C'est un choix de design = tes clients sont "immutables" (ne changent pas après création)
    // Une fois créé, un Customer ne peut plus être modifié !
    // C'est souvent une bonne pratique pour éviter les bugs ! ✅
}

/*
 * 🎯 COMMENT UTILISER CETTE CLASSE :
 * 
 * // 1. 🏗️ CRÉER un client
 * Customer client1 = new Customer(1, "Marie Dupont", "marie@email.com", "0612345678");
 * 
 * // 2. 📖 LIRE ses informations
 * System.out.println("Client: " + client1.getName());           // "Marie Dupont"
 * System.out.println("Email: " + client1.getEmail());           // "marie@email.com"
 * System.out.println("ID: " + client1.getCustomerId());         // 1
 * 
 * // 3. 📦 STOCKER dans une liste
 * List<Customer> mesClients = new ArrayList<>();
 * mesClients.add(client1);
 * 
 * // 4. 🔄 PARCOURIR la liste
 * for (Customer c : mesClients) {
 *     System.out.println(c.getName() + " - " + c.getEmail());
 * }
 */

/*
 * 🏆 POINTS FORTS DE TON CODE :
 * 
 * ✅ Attributs private (encapsulation respectée)
 * ✅ Constructeur complet (tous les champs obligatoires)
 * ✅ Getters simples et clairs
 * ✅ Pas de setters = objet immutable (bonne pratique)
 * ✅ Noms d'attributs cohérents avec la base de données
 * 
 * 🎯 ÉVENTUELLES AMÉLIORATIONS (pas obligatoires pour le TP) :
 * 
 * - Ajouter une méthode toString() pour l'affichage
 * - Ajouter equals() et hashCode() si tu veux comparer des clients
 * - Validation des paramètres (email valide, etc.)
 * 
 * Mais pour ton niveau et ton TP, c'est PARFAIT ! 🎉
 */