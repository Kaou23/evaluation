package ma.projet.service;

import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Join;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class HommeService implements IDao<Homme> {

    private final SessionFactory sessionFactory;

    @Autowired
    public HommeService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void create(Homme o) {
        getSession().persist(o);
        System.out.println("Homme créé : " + o.getNom() + " " + o.getPrenom());
    }

    @Override
    public Homme getById(int id) {
        return getSession().get(Homme.class, id);
    }

    @Override
    public List<Homme> getAll() {
        return getSession().createQuery("from Homme", Homme.class).list();
    }

    @Override
    public void update(Homme o) {
        getSession().merge(o);
    }

    @Override
    public void delete(Homme o) {
        getSession().remove(o);
    }

    public void afficherEpousesEntreDates(int hommeId, Date dateDebut, Date dateFin) {
        String hql = "SELECT m.femme FROM Mariage m WHERE m.homme.id = :hommeId " +
                "AND m.dateDebut BETWEEN :dateDebut AND :dateFin";
        List<?> femmes = getSession().createQuery(hql)
                .setParameter("hommeId", hommeId)
                .setParameter("dateDebut", dateDebut)
                .setParameter("dateFin", dateFin)
                .list();
        Homme h = getById(hommeId);
        System.out.println("Épouses de " + h.getNom() + " " + h.getPrenom() +
                " entre " + new SimpleDateFormat("dd/MM/yyyy").format(dateDebut) + " et " +
                new SimpleDateFormat("dd/MM/yyyy").format(dateFin) + ":");
        femmes.forEach(f -> System.out.println("  - " + ((ma.projet.beans.Femme) f).getNom().toUpperCase() + " " +
                ((ma.projet.beans.Femme) f).getPrenom()));
    }

    public void afficherHommesMarieQuatreFemmes(Date dateDebut, Date dateFin) {
        CriteriaBuilder cb = getSession().getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Homme> hommeRoot = cq.from(Homme.class);
        Join<Homme, Mariage> mariageJoin = hommeRoot.join("mariages");

        cq.multiselect(
                hommeRoot.get("id"),
                hommeRoot.get("nom"),
                hommeRoot.get("prenom"),
                cb.count(mariageJoin)
        );
        cq.where(cb.between(mariageJoin.get("dateDebut"), dateDebut, dateFin));
        cq.groupBy(hommeRoot.get("id"), hommeRoot.get("nom"), hommeRoot.get("prenom"));
        cq.having(cb.equal(cb.count(mariageJoin), 4));

        List<Object[]> results = getSession().createQuery(cq).getResultList();
        System.out.println("Hommes mariés à 4 femmes entre " + new SimpleDateFormat("dd/MM/yyyy").format(dateDebut) +
                " et " + new SimpleDateFormat("dd/MM/yyyy").format(dateFin) + ":");
        for (Object[] result : results) {
            System.out.println("  - " + result[1] + " " + result[2] + " (ID: " + result[0] +
                    ", Nombre de mariages: " + result[3] + ")");
        }
    }

    public void afficherMariagesHomme(int hommeId) {
        Homme h = getById(hommeId);
        if (h == null) {
            System.out.println("Homme introuvable avec ID : " + hommeId);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("Nom : " + h.getNom().toUpperCase() + " " + h.getPrenom());

        // Mariages en cours avec JOIN FETCH pour éviter lazy loading
        String hqlEnCours = "FROM Mariage m JOIN FETCH m.femme WHERE m.homme.id = :hommeId AND m.dateFin IS NULL ORDER BY m.dateDebut";
        List<Mariage> mariagesEnCours = getSession().createQuery(hqlEnCours, Mariage.class)
                .setParameter("hommeId", hommeId)
                .list();

        if (!mariagesEnCours.isEmpty()) {
            System.out.println("Mariages En Cours :");
            int i = 1;
            for (Mariage m : mariagesEnCours) {
                System.out.printf("%d. Femme : %s %s   Date Début : %s   Nbr Enfants : %d%n",
                        i++,
                        m.getFemme().getNom().toUpperCase(),
                        m.getFemme().getPrenom(),
                        sdf.format(m.getDateDebut()),
                        m.getNbEnfants());
            }
        } else {
            System.out.println("Mariages En Cours : Aucun");
        }

        // Mariages échoués avec JOIN FETCH
        String hqlEchoues = "FROM Mariage m JOIN FETCH m.femme WHERE m.homme.id = :hommeId AND m.dateFin IS NOT NULL ORDER BY m.dateDebut";
        List<Mariage> mariagesEchoues = getSession().createQuery(hqlEchoues, Mariage.class)
                .setParameter("hommeId", hommeId)
                .list();

        if (!mariagesEchoues.isEmpty()) {
            System.out.println("\nMariages échoués :");
            int i = 1;
            for (Mariage m : mariagesEchoues) {
                System.out.printf("%d. Femme : %s %s   Date Début : %s   Date Fin : %s   Nbr Enfants : %d%n",
                        i++,
                        m.getFemme().getNom().toUpperCase(),
                        m.getFemme().getPrenom(),
                        sdf.format(m.getDateDebut()),
                        sdf.format(m.getDateFin()),
                        m.getNbEnfants());
            }
        } else {
            System.out.println("Mariages échoués : Aucun");
        }
    }
}