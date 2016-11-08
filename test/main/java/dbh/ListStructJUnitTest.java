package main.java.dbh;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
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
public class ListStructJUnitTest {
    
    private static SessionFactory factory;
    
    public ListStructJUnitTest() {
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
    public void addEmployeeTest() {
        System.out.println("Structure Create test: addEmployee");

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
        System.out.println("Structure Read test: listEmployees");
        ManageEmployee manager = new ManageEmployee(factory);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream nw = new PrintStream(out);
        PrintStream old = System.out;
        
        //Creating new employees
        List c1 = new ArrayList();
        c1.add(new Certificate("AXA"));
        c1.add(new Certificate("XAXA"));
        Employee e1 = new Employee("Paweł", "Jaruga", 666000000);
        e1.setCertificates(c1);
        List c2 = new ArrayList();
        c2.add(new Certificate("NOOB"));
        Employee e2 = new Employee("Maciej", "Stepnowski", 1850);
        e2.setCertificates(c2);
        
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.save(e1);
            session.save(e2);
            tx.commit();
        }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
        }finally {
         session.close(); 
        }
        
        System.setOut(nw);
        manager.listEmployees();
        System.out.flush();
        System.setOut(old);
        
        //Formatting listEmployees() output
        String result = out.toString();
        String[] lines = result.split(System.getProperty("line.separator"));
        CharSequence rem = "Hibernate:";
        for(int i = 0; i < lines.length; i++)
        {
            if(lines[i].contains(rem))
                lines[i] = "";
        }
        StringBuilder res = new StringBuilder();
        for(String s : lines)
        {
            if(!s.equals(""))
                res.append(s).append(System.getProperty("line.separator"));
        }
        result = res.toString();
        
        //Building expected string
        StringBuilder exp = new StringBuilder();
        exp.append("First Name: Paweł  Last Name: Jaruga  Salary: 666000000").
            append(System.getProperty("line.separator")).
            append("Certificate: AXA").
            append(System.getProperty("line.separator")).
            append("Certificate: XAXA").
            append(System.getProperty("line.separator")).
            append("First Name: Maciej  Last Name: Stepnowski  Salary: 1850").
            append(System.getProperty("line.separator")).
            append("Certificate: NOOB").
            append(System.getProperty("line.separator"));
                
        String expected = exp.toString();
        
        assertEquals(result, expected);
        
        
//        (~~~~~~~~~~~~~~~)           (~~~~~~~~~~~~~~~)
//         \   \~~~~~~~/ /             \   \~~~~~~~/ /
//           \  \    / /                 \  \    / /
//             \  \/ /__===_____________==_\  \/ /
//            __ --  __----__          __-----__  --__
//         _-~     /'         ~\      /'         ~\    ~-_
//       /~       |____________|    |_____________|       ~\
//      |         |  O         |  /\| O           |         |
//      |          \ _       ./ /    \.          /          |
//      |             ~~~~~~ /        \~~~~~~~'           |
//       \                  /____________\                 /
//        ~--__         ___(              )___       ___--~
//             ~~~~--~~~    \            /    ~~~--~~____------
//     ------____/__   ~~     \        /    __---~~ ~\
//              |   ~~~~~       \    /        __----~|~~~~~~
//          -----\------------    \/      _________  /
//                \                |                /
//                  \   _______   / \     ~~----__/____
//    ____-----~~~~~~~\~        /     \         /      ~~~~~---
//                     ~-____-~        ~-____-~
//                         \              /
//                           \          /
//                            ~-______-~
    }
    
    @Test
    public void updateEmployeeTest() {
        System.out.println("Structure Update test: updateEmployee");
        
        Employee expected = new Employee("Andrzej", "Kowalski", 9001);
        List a = new ArrayList();
        a.add(new Certificate("MCA"));
        a.add(new Certificate("MBA"));
        expected.setCertificates(a);
        
        Session session = factory.openSession();
        Transaction tx = null;
        int id;
        try{
            tx = session.beginTransaction();
            id = (int) session.save(expected);
            tx.commit();
        }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace();
         return;
        }finally {
            session.close();
        }
        expected.setSalary(3000);
        
        ManageEmployee mp = new ManageEmployee(factory);
        mp.updateEmployee(id, 3000);
        
        session = factory.openSession();
        try{
            tx = session.beginTransaction();
            int i = ((BigInteger) session.createSQLQuery("SELECT COUNT(*)"
                    + "FROM Certificate").uniqueResult()).intValue();
            assertEquals(2, i);
            List res = session.createQuery("FROM Employee").list();
            assertEquals(1, res.size());
            assertEquals(expected.getFirstName(), ((Employee)res.get(0)).getFirstName());
            assertEquals(expected.getLastName(), ((Employee)res.get(0)).getLastName());
            assertEquals(expected.getSalary(), ((Employee)res.get(0)).getSalary());
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        }finally {
            session.close();
        }
    }
    
    @Test
    public void deleteEmployeeTest() {
        System.out.println("Structure Delete test: deleteEmployee");
        
        //Create 2 employees
        List c1 = new ArrayList();
        c1.add(new Certificate("WR"));
        Employee e1 = new Employee("Jan", "Kowalski", 3200);
        e1.setCertificates(c1);
        List c2 = new ArrayList();
        c2.add(new Certificate("EDR"));
        c2.add(new Certificate("TWR"));
        Employee e2 = new Employee("Mariusz", "Nowak", 4850);
        e2.setCertificates(c2);
        
        Session session = factory.openSession();
        Transaction tx = null;
        int id1, id2;
        id1=id2=0;
        try{
            tx = session.beginTransaction();
            id1 = (int) session.save(e1);
            id2 = (int) session.save(e2);
            tx.commit();
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        }finally {
            session.close(); 
        }
        
        //Delete first employee
        ManageEmployee mp = new ManageEmployee(factory);
        mp.deleteEmployee(id1);
        
        //Check struct
        session = factory.openSession();
        tx = null;
        try{
            int numberOfEmployees = ((BigInteger) session.createSQLQuery("SELECT COUNT(*)"
                    + "FROM Employee").uniqueResult()).intValue();
        
            int numberOfCertificates = ((BigInteger) session.createSQLQuery("SELECT COUNT(*)"
                    + "FROM Certificate").uniqueResult()).intValue();
        
            assertEquals( 1, numberOfEmployees);
            assertEquals( 2, numberOfCertificates);
 
            List l = session.createQuery("FROM Employee e WHERE e.id=" + id2 + "").list();
            Employee e = null;
            if(l.iterator().hasNext()) {
                e = (Employee) l.iterator().next();
            } else {
                fail("No employee returned from the table");
            }
            assertEquals( "Mariusz", e.getFirstName() );
            assertEquals( "Nowak", e.getLastName() );
            assertEquals( 4850, e.getSalary() );
            
            List lc = e.getCertificates();
            Iterator it = lc.iterator();
            assertEquals( "EDR", ((Certificate) it.next()).getName() );
            assertEquals( "TWR", ((Certificate) it.next()).getName() );
            
        }catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace(); 
        }finally {
            session.close(); 
        }
    }
}
