package ma.projet.service;

import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TacheService implements IDao<Tache> {

    private final SessionFactory sessionFactory;

    @Autowired
    public TacheService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void create(Tache o) {
        getSession().persist(o);
    }

    @Override
    public Tache getById(int id) {
        return getSession().get(Tache.class, id);
    }

    @Override
    public List<Tache> getAll() {
        return getSession().createQuery("from Tache", Tache.class).list();
    }

    @Override
    public void update(Tache o) {
        getSession().merge(o);
    }

    @Override
    public void delete(Tache o) {
        getSession().remove(o);
    }

    public void afficherTachesSup1000() {
        List<Tache> taches = getSession()
                .createNamedQuery("Tache.findByPrixSup1000", Tache.class)
                .list();

        System.out.println("Tâches dont le prix > 1000 DH:");
        for (Tache t : taches) {
            System.out.println("  - " + t.getNom() + " (prix: " + t.getPrix() + " DH)");
        }
    }

    public void afficherTachesEntreDates(LocalDate d1, LocalDate d2) {
        List<Tache> taches = getSession()
                .createNamedQuery("Tache.findBetweenDates", Tache.class)
                .setParameter("d1", d1)
                .setParameter("d2", d2)
                .list();

        System.out.println("Tâches entre " + d1 + " et " + d2 + ":");
        for (Tache t : taches) {
            System.out.println("  - " + t.getNom() + " (début: " + t.getDateDebut() + ")");
        }
    }
}