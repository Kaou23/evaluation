package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class ProjetService implements IDao<Projet> {

    private final SessionFactory sessionFactory;

    @Autowired
    public ProjetService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void create(Projet o) {
        getSession().persist(o);
    }

    @Override
    public Projet getById(int id) {
        return getSession().get(Projet.class, id);
    }

    @Override
    public List<Projet> getAll() {
        return getSession().createQuery("from Projet", Projet.class).list();
    }

    @Override
    public void update(Projet o) {
        getSession().merge(o);
    }

    @Override
    public void delete(Projet o) {
        getSession().remove(o);
    }

    public void afficherTachesProjet(int projetId) {
        Projet p = getById(projetId);
        if (p == null) {
            System.out.println("Projet introuvable!");
            return;
        }

        DateTimeFormatter headerFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.println("Projet : " + p.getId() +
                "\tNom : " + p.getNom() +
                "\tDate début : " + p.getDateDebut().format(headerFormatter));

        System.out.println("Liste des tâches:");
        System.out.println("Num\tNom\t\t\tDate Début Planifiée\tDate Fin Planifiée");

        List<Tache> taches = getSession()
                .createQuery("from Tache t where t.projet.id = :pid order by t.id", Tache.class)
                .setParameter("pid", projetId)
                .list();

        for (Tache t : taches) {
            System.out.printf("%d\t%-15s\t%s\t\t%s%n",
                    t.getId(),
                    t.getNom(),
                    t.getDateDebut().format(dateFormatter),
                    t.getDateFin().format(dateFormatter));
        }
    }

    public void afficherTachesRealisees(int projetId) {
        Projet p = getById(projetId);
        if (p == null) {
            System.out.println("Projet introuvable!");
            return;
        }

        DateTimeFormatter headerFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.println("Projet : " + p.getId() +
                "\tNom : " + p.getNom() +
                "\tDate début : " + p.getDateDebut().format(headerFormatter));

        System.out.println("Liste des tâches:");
        System.out.println("Num\tNom\t\t\tDate Début Réelle\tDate Fin Réelle");

        List<EmployeTache> employeTaches = getSession()
                .createQuery("from EmployeTache et where et.tache.projet.id = :pid order by et.tache.id",
                        EmployeTache.class)
                .setParameter("pid", projetId)
                .list();

        for (EmployeTache et : employeTaches) {
            Tache t = et.getTache();
            String dateDebut = et.getDateDebutReelle() != null ?
                    et.getDateDebutReelle().format(dateFormatter) : "N/A";
            String dateFin = et.getDateFinReelle() != null ?
                    et.getDateFinReelle().format(dateFormatter) : "N/A";

            System.out.printf("%d\t%-15s\t%s\t\t%s%n",
                    t.getId(),
                    t.getNom(),
                    dateDebut,
                    dateFin);
        }
    }
}