package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class EmployeTacheService implements IDao<EmployeTache> {

    private final SessionFactory sessionFactory;

    @Autowired
    public EmployeTacheService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void create(EmployeTache o) {
        getSession().persist(o);
    }

    @Override
    public EmployeTache getById(int id) {
        return getSession().get(EmployeTache.class, id);
    }

    @Override
    public List<EmployeTache> getAll() {
        return getSession().createQuery("from EmployeTache", EmployeTache.class).list();
    }

    @Override
    public void update(EmployeTache o) {
        getSession().merge(o);
    }

    @Override
    public void delete(EmployeTache o) {
        getSession().remove(o);
    }
}