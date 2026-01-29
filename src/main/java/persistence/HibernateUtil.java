package persistence;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.io.File;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;
    static {
        try {
            Configuration cfg = new Configuration();
            cfg.configure(new File("src/main/java/hibernate.cfg.xml"));
            sessionFactory = cfg.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Error config: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    public static SessionFactory getSessionFactory() { return sessionFactory; }
}