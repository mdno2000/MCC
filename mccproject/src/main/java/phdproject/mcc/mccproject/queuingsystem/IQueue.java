/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.queuingsystem;

/**
 *
 * @author Mohammad Alkhalaileh
 */
public interface IQueue {
    void callculateWaitingTime();
    void calculateUtlization();
    void processQueue();
    void printQueue();    
    
}