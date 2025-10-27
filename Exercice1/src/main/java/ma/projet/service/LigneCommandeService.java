package ma.projet.service;

import ma.projet.classes.LigneCommandeProduit;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional
public class LigneCommandeService {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Créer une ligne de commande
     */
    public boolean create(LigneCommandeProduit ligne) {
        try {
            getSession().save(ligne);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Trouver toutes les lignes de commande d'une commande
     */
    @SuppressWarnings("unchecked")
    public List<LigneCommandeProduit> findByCommande(int commandeId) {
        try {
            return getSession()
                    .createQuery("FROM LigneCommandeProduit lcp WHERE lcp.commande.id = :cmdId")
                    .setParameter("cmdId", commandeId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Trouver toutes les lignes de commande d'un produit
     */
    @SuppressWarnings("unchecked")
    public List<LigneCommandeProduit> findByProduit(int produitId) {
        try {
            return getSession()
                    .createQuery("FROM LigneCommandeProduit lcp WHERE lcp.produit.id = :prodId")
                    .setParameter("prodId", produitId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Supprimer une ligne de commande
     */
    public boolean delete(LigneCommandeProduit ligne) {
        try {
            getSession().delete(ligne);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mettre à jour la quantité d'une ligne de commande
     */
    public boolean updateQuantite(int commandeId, int produitId, int nouvelleQuantite) {
        try {
            LigneCommandeProduit ligne = (LigneCommandeProduit) getSession()
                    .createQuery("FROM LigneCommandeProduit lcp WHERE lcp.commande.id = :cmdId AND lcp.produit.id = :prodId")
                    .setParameter("cmdId", commandeId)
                    .setParameter("prodId", produitId)
                    .uniqueResult();

            if (ligne != null) {
                ligne.setQuantite(nouvelleQuantite);
                getSession().update(ligne);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Calculer le montant total d'une commande
     */
    public double calculerMontantCommande(int commandeId) {
        try {
            List<LigneCommandeProduit> lignes = findByCommande(commandeId);

            if (lignes == null || lignes.isEmpty()) {
                return 0;
            }

            double total = 0;
            for (LigneCommandeProduit ligne : lignes) {
                total += ligne.getProduit().getPrix() * ligne.getQuantite();
            }

            return total;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Afficher les détails d'une ligne de commande
     */
    public void afficherLignesCommande(int commandeId) {
        List<LigneCommandeProduit> lignes = findByCommande(commandeId);

        if (lignes == null || lignes.isEmpty()) {
            System.out.println("Aucune ligne de commande trouvée.");
            return;
        }

        System.out.println("\n=== Lignes de commande " + commandeId + " ===");
        System.out.printf("%-20s %-15s %-10s %-15s%n", "Produit", "Prix Unit.", "Quantité", "Sous-total");
        System.out.println("---------------------------------------------------------------");

        double total = 0;
        for (LigneCommandeProduit ligne : lignes) {
            double sousTotal = ligne.getProduit().getPrix() * ligne.getQuantite();
            System.out.printf("%-20s %-15.2f %-10d %-15.2f DH%n",
                    ligne.getProduit().getReference(),
                    ligne.getProduit().getPrix(),
                    ligne.getQuantite(),
                    sousTotal);
            total += sousTotal;
        }

        System.out.println("---------------------------------------------------------------");
        System.out.printf("Total : %.2f DH%n", total);
    }
}