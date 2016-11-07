package main.java.dbh;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream nw = new PrintStream(out);
    PrintStream old = System.out;
    
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
            session.createQuery("DELETE FROM Employee");    //przedtestowe czyszczenie bazy
            session.createQuery("DELETE FROM Certificate");
            tx.commit();
        }catch(HibernateException e) {
            fail("Database communication error. Aborting test.");
        }
    }
    
    @After
    public void tearDown() {
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.createQuery("DELETE FROM Employee");    //potestowe czyszczenie bazy
            session.createQuery("DELETE FROM Certificate");
            tx.commit();
        }catch(HibernateException e) {
            fail("Database communication error. Aborting test.");
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
        ArrayList set1 = new ArrayList();
        set1.add(new Certificate("MCA"));
        ManageEmployee manager = new ManageEmployee(factory);
        
        //Creating new employees
        Session session = factory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.createSQLQuery("INSERT INTO EMPLOYEE(id, first_name, last_name, salary) "
                    +           "VALUES(default, 'Paweł', 'Jaruga', 666000000)").executeUpdate();
            session.createSQLQuery("INSERT INTO EMPLOYEE(id, first_name, last_name, salary) "
                    +           "VALUES(default, 'Maciej', 'Stepnowski', 1850)").executeUpdate();
            session.createSQLQuery("INSERT INTO CERTIFICATE(id, certificate_name) "
                    +           "VALUES(default, 'AXA')").executeUpdate();
            session.createSQLQuery("update CERTIFICATE set employee_id=1, idx=0 where id=1").executeUpdate();
            session.createSQLQuery("INSERT INTO CERTIFICATE(id, certificate_name) "
                    +           "VALUES(default, 'XAXA')").executeUpdate();
            session.createSQLQuery("update CERTIFICATE set employee_id=1, idx=1 where id=2").executeUpdate();
            session.createSQLQuery("INSERT INTO CERTIFICATE(id, certificate_name) "
                    +           "VALUES(default, 'NOOB')").executeUpdate();
            session.createSQLQuery("update CERTIFICATE set employee_id=2, idx=0 where id=3").executeUpdate();
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
        
        
        
        assertTrue(true);
        //TODO
    }
    
    @Test
    public void deleteEmployeeTest() {
        System.out.println("Structure Delete test: deleteEmployee");
        assertTrue(true);
        //TODO
    }
}
