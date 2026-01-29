package persistence;

import model.Distribuidor;
import org.hibernate.Session;
import java.util.List;

public class DistribuidorDAO extends BaseDAO<Distribuidor> {

    public void insertar(Distribuidor d) { validar(d); ejecutar(s -> s.save(d)); }
    public void actualizar(Distribuidor d) { validar(d); ejecutar(s -> s.update(d)); }

    public void borrar(int id) {
        ejecutar(s -> {
            // 1. Borrar relaciones en la tabla intermedia (SQL nativo para asegurar limpieza)
            s.createSQLQuery("DELETE FROM FLOR_DISTRIBUIDOR WHERE ID_DISTRIBUIDOR = :id")
             .setParameter("id", id).executeUpdate();
             
            // 2. Borrar entidad
            Distribuidor d = s.get(Distribuidor.class, id);
            if(d != null) s.delete(d);
        });
    }

    public List<Distribuidor> listar() {
        try(Session s = HibernateUtil.getSessionFactory().openSession()){
            return s.createQuery("from Distribuidor", Distribuidor.class).list();
        }
    }

    public List<Distribuidor> buscar(String nombre) {
        try(Session s = HibernateUtil.getSessionFactory().openSession()){
            return s.createQuery("from Distribuidor where upper(nombre) like :n", Distribuidor.class)
                    .setParameter("n", "%"+nombre.toUpperCase()+"%").list();
        }
    }
}