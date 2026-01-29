package persistence;
import model.Pedido;
import org.hibernate.Session;
import java.util.List;

public class PedidoDAO extends BaseDAO<Pedido> {
    public void insertar(Pedido p) { validar(p); ejecutar(s -> s.save(p)); }
    public void actualizar(Pedido p) { validar(p); ejecutar(s -> s.update(p)); }
    public void borrar(int id) { 
        ejecutar(s -> {
            Pedido p = s.get(Pedido.class, id);
            if(p != null) s.delete(p);
        });
    }
    public List<Pedido> listar() {
        try(Session s = HibernateUtil.getSessionFactory().openSession()){
            return s.createQuery("from Pedido", Pedido.class).list();
        }
    }
    public List<Pedido> buscar(String flor) { // Busca pedidos por nombre de flor
        try(Session s = HibernateUtil.getSessionFactory().openSession()){
            return s.createQuery("from Pedido p where upper(p.flor.nombreFlor) like :f", Pedido.class)
                    .setParameter("f", "%"+flor.toUpperCase()+"%").list();
        }
    }
}