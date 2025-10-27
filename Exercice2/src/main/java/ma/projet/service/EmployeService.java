package ma.projet.service;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class EmployeService implements IDao<Employe> {

    private final SessionFactory sessionFactory;

    @Autowired
    public EmployeService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void create(Employe o) {
        getSession().persist(o);
    }

    @Override
    public Employe getById(int id) {
        return getSession().get(Employe.class, id);
    }

    @Override
    public List<Employe> getAll() {
        return getSession().createQuery("from Employe", Employe.class).list();
    }

    @Override
    public void update(Employe o) {
        getSession().merge(o);
    }

    @Override
    public void delete(Employe o) {
        getSession().remove(o);
    }

    public void afficherTachesRealisees(int employeId) {
        List<EmployeTache> ets = getSession()
                .createQuery("from EmployeTache et where et.employe.id = :id", EmployeTache.class)
                .setParameter("id", employeId)
                .list();

        Employe e = getById(employeId);
        System.out.println("Tâches réalisées par l'employé " +
                (e != null ? e.getNom() + " " + e.getPrenom() : employeId) + ":");
        for (EmployeTache et : ets) {
            System.out.println("  - " + et.getTache().getNom() +
                    " (du " + et.getDateDebutReelle() +
                    " au " + et.getDateFinReelle() + ")");
        }
    }

    public void afficherProjetsGeres(int employeId) {
        List<Projet> projets = getSession()
                .createQuery("from Projet p where p.chefProjet.id = :id", Projet.class)
                .setParameter("id", employeId)
                .list();

        Employe e = getById(employeId);
        System.out.println("Projets gérés par l'employé " +
                (e != null ? e.getNom() + " " + e.getPrenom() : employeId) + ":");
        for (Projet p : projets) {
            System.out.println("  - " + p.getNom() + " (début: " + p.getDateDebut() + ")");
        }
    }
}