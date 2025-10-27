package ma.projet.test;

import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TestApplication {

    private static AnnotationConfigApplicationContext context;
    private static CategorieService categorieService;
    private static ProduitService produitService;
    private static CommandeService commandeService;
    private static LigneCommandeService ligneCommandeService;

    public static void main(String[] args) {
        try {
            // Initialisation du contexte Spring
            System.out.println("=== INITIALISATION DU CONTEXTE SPRING ===");
            context = new AnnotationConfigApplicationContext(HibernateUtil.class);

            // Diagnostic: Afficher tous les beans chargés
            System.out.println("\n=== BEANS DÉTECTÉS PAR SPRING ===");
            String[] beanNames = context.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(" - " + beanName + " : " + context.getBean(beanName).getClass().getName());
            }
            System.out.println("Total beans: " + beanNames.length);

            // Vérifier si les services existent
            System.out.println("\n=== VÉRIFICATION DES SERVICES ===");
            String[] services = {"categorieService", "produitService", "commandeService", "ligneCommandeService"};
            for (String serviceName : services) {
                if (context.containsBean(serviceName)) {
                    System.out.println("✓ " + serviceName + " trouvé");
                } else {
                    System.out.println("✗ " + serviceName + " NON TROUVÉ");
                }
            }

            // Récupération des services
            System.out.println("\n=== CHARGEMENT DES SERVICES ===");
            categorieService = context.getBean(CategorieService.class);
            System.out.println("✓ CategorieService chargé");

            produitService = context.getBean(ProduitService.class);
            System.out.println("✓ ProduitService chargé");

            commandeService = context.getBean(CommandeService.class);
            System.out.println("✓ CommandeService chargé");

            ligneCommandeService = context.getBean(LigneCommandeService.class);
            System.out.println("✓ LigneCommandeService chargé");

            System.out.println("\n=== DÉMARRAGE DES TESTS ===");

            // Test 1: Création des catégories
            testCategories();

            // Test 2: Création des produits
            testProduits();

            // Test 3: Création des commandes
            testCommandes();

            // Test 4: Affichage des produits par catégorie
            testProduitsParCategorie();

            // Test 5: Affichage des produits commandés entre deux dates
            testProduitsCommandesEntreDates();

            // Test 6: Affichage des produits d'une commande
            testProduitsDansCommande();

            // Test 7: Produits avec prix supérieur à 100 DH
            testProduitsPrixSuperieur100();

            System.out.println("\n=== TESTS TERMINÉS AVEC SUCCÈS ===");

        } catch (Exception e) {
            System.err.println("\n❌ Erreur durant l'exécution : " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (context != null) {
                context.close();
                System.out.println("\nContexte Spring fermé.");
            }
        }
    }

    private static void testCategories() {
        System.out.println("\n--- TEST 1: Création des catégories ---");

        try {
            Categorie informatique = new Categorie("INFO", "Informatique", null);
            Categorie electromenager = new Categorie("ELEC", "Électroménager", null);
            Categorie mobilier = new Categorie("MOB", "Mobilier", null);

            categorieService.create(informatique);
            categorieService.create(electromenager);
            categorieService.create(mobilier);

            categorieService.afficherToutesLesCategories();
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testProduits() {
        System.out.println("\n--- TEST 2: Création des produits ---");

        try {
            Categorie info = categorieService.findByCode("INFO");
            Categorie elec = categorieService.findByCode("ELEC");
            Categorie mob = categorieService.findByCode("MOB");

            if (info == null || elec == null || mob == null) {
                System.err.println("Erreur : Les catégories n'ont pas été créées correctement.");
                return;
            }

            Produit p1 = new Produit("ORDI001", 4500.0f, info);
            Produit p2 = new Produit("SOURIS02", 80.0f, info);
            Produit p3 = new Produit("CLAV03", 120.0f, info);
            Produit p4 = new Produit("FRIGO01", 3200.0f, elec);
            Produit p5 = new Produit("TVLED01", 2800.0f, elec);
            Produit p6 = new Produit("CHAISE01", 350.0f, mob);
            Produit p7 = new Produit("BUREAU01", 850.0f, mob);

            produitService.create(p1);
            produitService.create(p2);
            produitService.create(p3);
            produitService.create(p4);
            produitService.create(p5);
            produitService.create(p6);
            produitService.create(p7);

            System.out.println("✓ 7 produits créés avec succès.");
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testCommandes() {
        System.out.println("\n--- TEST 3: Création des commandes ---");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            Commande cmd1 = new Commande(sdf.parse("01/03/2013"));
            Commande cmd2 = new Commande(sdf.parse("14/03/2013"));
            Commande cmd3 = new Commande(sdf.parse("25/03/2013"));
            Commande cmd4 = new Commande(sdf.parse("05/04/2013"));

            commandeService.create(cmd1);
            commandeService.create(cmd2);
            commandeService.create(cmd3);
            commandeService.create(cmd4);

            Produit ordi = produitService.findById(1);
            Produit clav = produitService.findById(3);
            Produit tv = produitService.findById(5);

            if (ordi == null || clav == null || tv == null) {
                System.err.println("Erreur : Impossible de récupérer les produits.");
                return;
            }

            LigneCommandeProduit l1 = new LigneCommandeProduit(cmd2, ordi, 2);
            LigneCommandeProduit l2 = new LigneCommandeProduit(cmd2, clav, 7);
            LigneCommandeProduit l3 = new LigneCommandeProduit(cmd2, tv, 3);

            ligneCommandeService.create(l1);
            ligneCommandeService.create(l2);
            ligneCommandeService.create(l3);

            System.out.println("✓ Commandes et lignes de commande créées avec succès.");
            commandeService.afficherToutesLesCommandes();

        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testProduitsParCategorie() {
        System.out.println("\n--- TEST 4: Produits par catégorie ---");

        try {
            Categorie info = categorieService.findByCode("INFO");
            if (info != null) {
                produitService.afficherProduitsParCategorie(info.getId());
            } else {
                System.err.println("Catégorie INFO introuvable.");
            }
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testProduitsCommandesEntreDates() {
        System.out.println("\n--- TEST 5: Produits commandés entre deux dates ---");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateDebut = sdf.parse("01/03/2013");
            Date dateFin = sdf.parse("31/03/2013");

            List<Produit> produits = produitService.findProduitsCommandesEntreDates(dateDebut, dateFin);

            if (produits != null && !produits.isEmpty()) {
                System.out.println("\nProduits commandés entre " + sdf.format(dateDebut) +
                        " et " + sdf.format(dateFin) + " :");
                System.out.printf("%-20s %-15s%n", "Référence", "Prix");
                System.out.println("----------------------------------------");

                for (Produit p : produits) {
                    System.out.printf("%-20s %-15.2f DH%n", p.getReference(), p.getPrix());
                }
            } else {
                System.out.println("Aucun produit commandé dans cette période.");
            }

        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testProduitsDansCommande() {
        System.out.println("\n--- TEST 6: Produits dans une commande ---");

        try {
            commandeService.afficherProduitsCommande(2);
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testProduitsPrixSuperieur100() {
        System.out.println("\n--- TEST 7: Produits avec prix > 100 DH ---");

        try {
            List<Produit> produitsChers = produitService.findProduitsSuperieur100();

            if (produitsChers != null && !produitsChers.isEmpty()) {
                System.out.println("\nProduits avec prix supérieur à 100 DH :");
                System.out.printf("%-20s %-15s %-20s%n", "Référence", "Prix", "Catégorie");
                System.out.println("----------------------------------------------------");

                for (Produit p : produitsChers) {
                    System.out.printf("%-20s %-15.2f DH %-20s%n",
                            p.getReference(), p.getPrix(), p.getCategorie().getLibelle());
                }
            } else {
                System.out.println("Aucun produit avec prix supérieur à 100 DH.");
            }
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}