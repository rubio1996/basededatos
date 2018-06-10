/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basededatos;

import java.sql.DriverManager;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author 1daw
 */
public class BaseDeDatos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("baseDeDatosPU");
        EntityManager em = emf.createEntityManager();
        Carrera CarreraVelocidad = new Carrera(0, "Velocimetro");
        Carrera CarreraResistencia = new Carrera();
        CarreraResistencia.setNombre("Resistencia");
        em.getTransaction().begin();
        em.persist(CarreraVelocidad);
        em.persist(CarreraResistencia);
        em.getTransaction().commit();
        em.close(); 
        emf.close(); 
        try { 
            DriverManager.getConnection("jdbc:derby:BDAgendaContactos;create=true"); 
        } catch (SQLException ex) { 
        }
    }
    
}
