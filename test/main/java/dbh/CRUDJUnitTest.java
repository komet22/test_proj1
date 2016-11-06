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
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PrintStream nw = new PrintStream(out);
    PrintStream old = System.out;
    
    public CRUDJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void saveTest() {
        System.out.println("CRUD Create test: session.save()");
        assertTrue(true);
        //TODO
    }
    
    @Test
    public void createQueryTest() {
        System.out.println("CRUD Read test: session.createQuery() - for reading purposes");
        ArrayList set1 = new ArrayList();
        set1.add(new Certificate("MCA"));
        
        ManageEmployee manager = new ManageEmployee(factory);
        
        manager.addEmployee("Paweł", "Jaruga", 666000000, set1);
        
        System.setOut(nw);
        manager.listEmployees();
        System.out.flush();
        System.setOut(old);
        
        //Formatting listEmployees()
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
            append("Certificate: MCA").
            append(System.getProperty("line.separator"));
        String expected = exp.toString();
        
        assertEquals(result, expected);
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