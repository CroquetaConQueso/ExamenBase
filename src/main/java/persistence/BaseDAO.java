package persistence;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.validation.*;
import java.util.Set;
import java.util.List;
import java.util.function.Consumer;

public abstract class BaseDAO<T> {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    protected void validar(T obj) {
        Set<ConstraintViolation<T>> errors = validator.validate(obj);
        if (!errors.isEmpty()) {
            StringBuilder sb = new StringBuilder("Errores:\n");
            for (ConstraintViolation<T> v : errors) sb.append("- ").append(v.getMessage()).append("\n");
            throw new RuntimeException(sb.toString());
        }
    }

    protected void ejecutar(Consumer<Session> accion) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            accion.accept(s);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
}