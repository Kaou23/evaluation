package ma.projet.service;

import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MariageService implements IDao<Mariage> {

    private final SessionFactory sessionFactory;

    @Autowired
    public MariageService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void create(Mariage o) {
        getSession().persist(o);
    }

    @Override
    public Mariage getById(int id) {
        return getSession().get(Mariage.class, id);
    }

    @Override
    public List<Mariage> getAll() {
        return getSession().createQuery("from Mariage", Mariage.class).list();
    }

    @Override
    public void update(Mariage o) {
        getSession().merge(o);
    }

    @Override
    public void delete(Mariage o) {
        getSession().remove(o);
    }
}