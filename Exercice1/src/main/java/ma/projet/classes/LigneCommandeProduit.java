package ma.projet.classes;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class LigneCommandeProduit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "commande_id" ,nullable = false)
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "produit_id" ,nullable = false)
    private Produit produit;

    private int quantite;

    public LigneCommandeProduit() {
    }

    public LigneCommandeProduit(Commande commande, Produit produit, int quantite) {
        this.commande = commande;
        this.produit = produit;
        this.quantite = quantite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    @Override
    public String toString() {
        return "LigneCommandeProduit{" +
                "id=" + id +
                ", commande=" + commande +
                ", produit=" + produit +
                ", quantite=" + quantite +
                '}';
    }
}