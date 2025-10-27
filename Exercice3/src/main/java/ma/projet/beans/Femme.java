package ma.projet.beans;


import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@NamedNativeQuery(
        name = "Femme.countEnfantsBetweenDates",
        query = "SELECT SUM(m.nbrEnfant) FROM mariage m WHERE m.femme_id = :femmeId " +
                "AND m.dateDebut BETWEEN :dateDebut AND :dateFin"
)
@NamedQuery(
        name = "Femme.findMarriedTwiceOrMore",
        query = "SELECT DISTINCT f FROM Femme f LEFT JOIN FETCH f.mariages WHERE SIZE(f.mariages) >= 2"
)
public class Femme extends Personne {

    @OneToMany(mappedBy = "femme", fetch = FetchType.LAZY)
    private List<Mariage> mariages;

    public Femme() {}

    public Femme(String nom, String prenom, String telephone, String adresse, Date dateNaissance) {
        super(nom, prenom, telephone, adresse, dateNaissance);
    }

    public List<Mariage> getMariages() { return mariages; }
    public void setMariages(List<Mariage> mariages) { this.mariages = mariages; }
}