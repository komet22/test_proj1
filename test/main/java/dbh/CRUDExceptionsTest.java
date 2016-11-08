/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.dbh;

import java.util.Iterator;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author PM, JPII, SK
 */

public class CRUDExceptionsTest {

    private static SessionFactory factory;
    
    public CRUDExceptionsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            fail("Failed to create sessionFactory object. Aborting test.");
	}
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        Session session = factory.openSession();
        Transaction tx = null;
         try{
            tx = session.beginTransaction();
            List all = session.createQuery("FROM Employee").list();
            for (Iterator it = all.iterator(); it.hasNext();) {
                session.delete((it.next()));
            }
            all = session.createQuery("FROM Certificate").list();
            for (Iterator it = all.iterator(); it.hasNext();) {
                session.delete((it.next()));
            }
            tx.commit();
        }catch(HibernateException e) {
            fail("Database communication error. Aborting test.");
        } finally {
            session.close();
        }
    }
    
    @After
    public void tearDown() {
        Session session = factory.openSession();
        Transaction tx = null;
         try{
            tx = session.beginTransaction();
            List all = session.createQuery("FROM Employee").list();
            for (Iterator it = all.iterator(); it.hasNext();) {
                session.delete((it.next()));
            }
            all = session.createQuery("FROM Certificate").list();
            for (Iterator it = all.iterator(); it.hasNext();) {
                session.delete((it.next()));
            }
            tx.commit();
        }catch(HibernateException e) {
            fail("Database communication error. Aborting test.");
        }finally{
            session.close();
        }
    }
    
    
    
    @Test (expected=org.hibernate.MappingException.class)
    public void saveExceptionTest1() {
        System.out.println("CRUD Save Exception test 1");
        Session session = factory.openSession();
        Transaction tx = null;
        tx = session.beginTransaction();
        String s = "444";
        session.save(s);
        tx.commit();
        session.close();
    }
    
    @Test (expected=IllegalArgumentException.class)
    public void saveExceptionTest2() {
        System.out.println("CRUD Save Exception test 2");
        Session session = factory.openSession();
        Transaction tx = null;
        tx = session.beginTransaction();
        session.save(null);
        tx.commit();
        session.close();
    }
    
    
    @Test (expected=org.hibernate.MappingException.class)
    public void updateExceptionTest() {
        System.out.println("CRUD Update Exception test");
        Session session = factory.openSession();
        Transaction tx = null;
        tx = session.beginTransaction();       
        Integer i = 5;
        session.update(i);      
        tx.commit();
        session.close();
    }
  
    @Test(expected = org.hibernate.MappingException.class)
    public void getExceptionTest() {
        System.out.println("CRUD Get Exception test");
        Session session = factory.openSession();
        Transaction tx = null;
        tx = session.beginTransaction();
        int id = -1;
        Employee e = (Employee) session.get(Double.class, id);
        tx.commit();
        session.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void deleteExceptionTest1() {
        System.out.println("CRUD Delete Exception test 1");
        Session session = factory.openSession();
        Transaction tx = null;
        tx = session.beginTransaction();
        Employee e = null;
        session.delete(e); 
        tx.commit();
        session.close();
    }
    
    @Test(expected = org.hibernate.MappingException.class)
    public void deleteExceptionTest2() {
        System.out.println("CRUD Delete Exception test 2");
        Session session = factory.openSession();
        Transaction tx = null;
        tx = session.beginTransaction();
        char c = '4';
        session.delete(c); 
        tx.commit();
        session.close();
    }
    
    @Test(expected = org.hibernate.SessionException.class)
    public void connectionExceptionTest() {
        System.out.println("Test session exception");
        Session session = factory.openSession();
        Transaction tx = null;
        tx = session.beginTransaction();
        session.close();
        Employee e = new Employee();
        session.save(e); 
        tx.commit();
    }
    
}
