/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.dbh;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author PM, JPII, SK
 */
public class CRUDJUnitTest {
    
    private static SessionFactory factory;
    
    public CRUDJUnitTest() {
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

    @Test
    public void saveTest() {
        System.out.println("CRUD Create test: session.save()");
        
        String[] fname = {"Paweł", "Maciej"};
        String[] lname = {"Jaruga", "Stepnowski"};
        int[] salary = {666000, 1850};
        
        List c1 = new ArrayList();
        c1.add(new Certificate("AXA"));
        c1.add(new Certificate("XAXA"));
        List c2 = new ArrayList();
        c2.add(new Certificate("NOOB"));
        List[] cert = {c1, c2};
        Employee[] employee = new Employee[fname.length];
        
        for(int i = 0 ; i < fname.length ; i++)
        {
            employee[i] = new Employee(fname[i], lname[i], salary[i]);
            employee[i].setCertificates(cert[i]);
        }
        
        
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            for(Employee em : employee)
                session.save(em);
            int i = 0;
            List result = session.createSQLQuery("SELECT * FROM Employee").addEntity(Employee.class).list();
            Iterator<Employee> it = result.iterator();
            if(result.size() != fname.length)
            {
                fail("Wrong number of Employees");
                return;
            }
            else
            {
                while(it.hasNext())
                {
                        Employee e = it.next();
                        assertEquals(e.getFirstName(), fname[i]);
                        assertEquals(e.getLastName(), lname[i]);
                        assertEquals(e.getSalary(), salary[i]);

                        List certificates = e.getCertificates();
                        Iterator<Certificate> ct = certificates.iterator();
                        if(certificates.size() != cert[i].size())
                        {
                            fail("Wrong number of Certificates");
                            return;
                        }
                        else
                            {
                            int j = 0;
                            while(ct.hasNext())
                            {
                                Certificate c = (Certificate) cert[i].get(j);
                                assertEquals(ct.next().getName(), c.getName());
                                j++;
                            }
                        }
                        i++;
                }
            }
            tx.commit();
        }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
        }finally {
         session.close(); 
        }
    }
    
    @Test
    public void createQueryTest() {
        System.out.println("CRUD Read test: session.createQuery() - for reading purposes");
        
        String[] fname = {"Paweł", "Maciej"};
        String[] lname = {"Jaruga", "Stepnowski"};
        int[] salary = {666000, 1850};
        
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            
            int cid = 1;
            for(int i = 0 ; i < fname.length ; i++)
            {
                session.createSQLQuery("INSERT INTO EMPLOYEE(id, first_name, last_name, salary) "
                    +           "VALUES(default, '"+ fname[i] +"', '"+ lname[i] +"', "+ salary[i] +")").executeUpdate();
            }
            
            int i = 0;
            List result = session.createQuery("FROM Employee").list();
            Iterator<Employee> it = result.iterator();
            if(result.size() != fname.length)
            {
                fail("Wrong number of Employees");
                return;
            }
            else
            {
                while(it.hasNext())
                {
                        Employee e = it.next();
                        assertEquals(e.getFirstName(), fname[i]);
                        assertEquals(e.getLastName(), lname[i]);
                        assertEquals(e.getSalary(), salary[i]);
                        i++;
                }
            }
            
            tx.commit();
        }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
        }finally {
         session.close(); 
        }
        
        assertTrue(true);
        //TODO
    }
    
    @Test
    public void updateTest() {
        System.out.println("CRUD Update test: session.update()");
        assertTrue(true);
        //TODO
    }
    
    @Test
    public void deleteTest() {
        System.out.println("CRUD Delete test: session.delete()");
        assertTrue(true);
        //TODO
    }
}