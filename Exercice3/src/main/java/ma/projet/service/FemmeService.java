package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FemmeService implements IDao<Femme> {

    private final SessionFactory sessionFactory;

    @Autowired
    public FemmeService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void create(Femme o) {
        getSession().persist(o);
    }

    @Override
    public Femme getById(int id) {
        return getSession().get(Femme.class, id);
    }

    @Override
    public List<Femme> getAll() {
        return getSession().createQuery("from Femme", Femme.class).list();
    }

    @Override
    public void update(Femme o) {
        getSession().merge(o);
    }

    @Override
    public void delete(Femme o) {
        getSession().remove(o);
    }

    // Nombre d'enfants d'une femme entre deux dates (Requête native nommée)
    public int countEnfantsBetweenDates(int femmeId, Date dateDebut, Date dateFin) {
        Object result = getSession().createNamedQuery("Femme.countEnfantsBetweenDates")
                .setParameter("femmeId", femmeId)
                .setParameter("dateDebut", dateDebut)
                .setParameter("dateFin", dateFin)
                .uniqueResult();

        return result != null ? ((Number) result).intValue() : 0;
    }

    // Femmes mariées au moins deux fois (Requête nommée)
    public List<Femme> findFemmesMarriedTwiceOrMore() {
        return getSession().createNamedQuery("Femme.findMarriedTwiceOrMore", Femme.class).list();
    }

    // Trouver la femme la plus âgée
    public Femme findOldestFemme() {
        List<Femme> femmes = getAll();
        return femmes.stream()
                .min(Comparator.comparing(Femme::getDateNaissance))
                .orElse(null);
    }

    // Afficher toutes les femmes
    public void afficherFemmes() {
        List<Femme> femmes = getAll();
        System.out.println("Liste des femmes:");
        for (Femme f : femmes) {
            System.out.println("  - " + f.getPrenom() + " " + f.getNom() +
                    " (Née le: " + f.getDateNaissance() + ")");
        }
    }
}