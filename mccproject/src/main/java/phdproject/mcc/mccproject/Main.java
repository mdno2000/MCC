/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author Mohammad Alkhalaileh
 */
@SpringBootApplication
public class Main {
    public static void main(String [] args)
    {
//         int cores = Runtime.getRuntime().availableProcessors();
//        System.out.println("#Cores " + cores);
        SpringApplication.run(Main.class, args);
        
    }    
}
