/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.dbh;

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
 * @author PM, PJ, SK
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
            session.createQuery("DELETE FROM Employee");    //przedtestowe czyszczenie bazy
            tx.commit();
        }catch(HibernateException e) {
            fail("Database communication error. Aborting test.");
        }
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void addEmployeeTest() {
        System.out.println("CRUD Create test: addEmployee");

        String firstname = "Andrzej";
        String lastname = "Kowalski";
        int salary = 3000;
        ArrayList cert = new ArrayList();
        cert.add(new Certificate("MCA"));
        cert.add(new Certificate("MBA"));
        cert.add(new Certificate("PMP"));
        
        ManageEmployee ME = new ManageEmployee(factory);
        int id = ME.addEmployee(firstname, lastname, salary, cert);
        
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            List result = session.createQuery("FROM Employee e WHERE e.id=" + id + "").list();
            Employee employee;
            Certificate c;
            if(result.iterator().hasNext()) employee = (Employee) result.iterator().next();
            else {
                fail("No employee saved or wrong employee ID saved into database.");
                return;
            }
            assertEquals(firstname, employee.getFirstName());
            assertEquals(lastname, employee.getLastName());
            result = employee.getCertificates();
            Iterator it = result.iterator();
            for (int i = 0; i < 3; i++) {
                if(it.hasNext()) c = (Certificate) it.next();
                else {
                    fail("Wrong number of certificates saved: too few certificates.");
                    return;
                }
                assertEquals(((Certificate)cert.get(i)).getName(), c.getName());
            }
            if (it.hasNext()) {
                fail("Wrong number of certificates saved: too many certificates.");
                return;
            }
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            session.close();
            fail("Database communication error. Aborting test.");
        }finally {
            session.close(); 
        }
    }
    
    @Test
    public void listEmployeesTest() {
        System.out.println("CRUD Read test: listEmployees");
        assertTrue(true);
        //TODO
    }
    
    @Test
    public void updateEmployeeTest() {
        System.out.println("CRUD Update test: updateEmployee");
        assertTrue(true);
        //TODO
    }
    
    @Test
    public void deleteEmployeeTest() {
        System.out.println("CRUD Delete test: deleteEmployee");
        assertTrue(true);
        //TODO
    }
}