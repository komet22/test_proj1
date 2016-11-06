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
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
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