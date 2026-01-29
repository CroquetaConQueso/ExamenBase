package persistence;
import model.Flor;
import org.hibernate.Session;
import java.util.List;

public class FlorDAO extends BaseDAO<Flor> {
    
    public void insertar(Flor f) { validar(f); ejecutar(s -> s.save(f)); }
    public void actualizar(Flor f) { validar(f); ejecutar(s -> s.update(f)); }
    
    // --- MODIFICADO: Borrado en Cascada Manual ---
    public void borrar(int id) { 
        ejecutar(s -> {
            // 1. Primero borramos los PEDIDOS asociados a esta flor
            // Esto evita el error "Foreign key constraint fails"
            s.createQuery("delete from Pedido p where p.flor.idFlor = :id")
             .setParameter("id", id)
             .executeUpdate();
             
            // 2. Ahora que está limpia, borramos la Flor
            Flor f = s.get(Flor.class, id);
            if(f != null) s.delete(f);
        });
    }

    public List<Flor> listar() {
        try(Session s = HibernateUtil.getSessionFactory().openSession()){
            return s.createQuery("from Flor", Flor.class).list();
        }
    }
    
    public List<Flor> buscar(String nombre) {
        try(Session s = HibernateUtil.getSessionFactory().openSession()){
            return s.createQuery("from Flor where upper(nombreFlor) like :n", Flor.class)
                    .setParameter("n", "%"+nombre.toUpperCase()+"%").list();
        }
    }
}