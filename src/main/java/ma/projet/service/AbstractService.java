package ma.projet.service;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractService<T> implements IDao<T> {
    private final Class<T> type;
    protected AbstractService(Class<T> type) { this.type = type; }
    protected <R> R lire(Function<Session,R> action) {
        return transaction(action);
    }
    private <R> R transaction(Function<Session,R> action) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            try { R result = action.apply(s); tx.commit(); return result; }
            catch (RuntimeException e) { if (tx.isActive()) tx.rollback(); throw e; }
        }
    }
    public T create(T e) { return transaction(s -> { s.persist(e); return e; }); }
    public T update(T e) { return transaction(s -> s.merge(e)); }
    public void delete(T e) { transaction(s -> { s.remove(s.merge(e)); return null; }); }
    public T findById(int id) { return lire(s -> s.get(type, id)); }
    public List<T> findAll() { return lire(s -> s.createQuery("from " + type.getSimpleName() + " e order by e.id", type).list()); }
    protected static void verifierDates(LocalDate debut, LocalDate fin) {
        if (debut == null || fin == null || debut.isAfter(fin))
            throw new IllegalArgumentException("Intervalle de dates invalide");
    }
}
