/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.application;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import phdproject.mcc.mccproject.application.tasks.*;
import phdproject.mcc.mccproject.applicationuser.UserProfile;
import phdproject.mcc.mccproject.bagOfTask.Task;
import phdproject.mcc.mccproject.profiler.ProfilerEntry;
import phdproject.mcc.mccproject.resource.RescourceLocation;
import phdproject.mcc.mccproject.resource.Resource;
import phdproject.mcc.mccproject.resource.resourcecharacteristics.ResourceCharacteristic;

/**
 *
 * @author Mohammad Alkhalaileh
 */
public class RunnableTask {

    /**
     * Run a Task Logic
     *
     * @return
     */
    Task task;
    UserProfile userProfile;

    public RunnableTask(Task task, UserProfile userProfile) {
        this.task = task;
        this.userProfile = userProfile;
    }
   public RunnableTask()
   {
       
   }
    public static TaskResult runTaskCode(Task task, Resource res) {
        TaskResult taskRes = null;
        System.out.println("******");
        switch (task.getTaskID()) {
            case 1:
                return (TaskResult) ApplicationTask1.runTaskCode(task, res);
            case 2:
                taskRes = (TaskResult) ApplicationTask2.runTaskCode(task, res);
                break;
            case 3:
                taskRes = (TaskResult) ApplicationTask3.runTaskCode(task, res);
                break;
            case 4:
                taskRes = (TaskResult) ApplicationTask4.runTaskCode(task, res);
                break;
            case 5:
                taskRes = (TaskResult) ApplicationTask5.runTaskCode(task, res);
                break;

        }
        return taskRes;
    }

    public static List<ProfilerEntry> generatePTProfileEntries(int taskID, Resource res) {
        List<ProfilerEntry> entries = new ArrayList<>();
        double cpu = (double) res.getResourceCharacteristicEntryCurValue(ResourceCharacteristic.CPU);
        double ram = (double) res.getResourceCharacteristicEntryCurValue(ResourceCharacteristic.RAM); 
        UniformRealDistribution dist = null;
        double val = 0;
        switch (taskID) {
            case 0:
                dist = new UniformRealDistribution(1.5, 4.9);
                val = dist.getNumericalMean();
                break;
            case 1:
                dist = new UniformRealDistribution(9.8, 18.5);
                val = dist.getNumericalMean();
                break;
            case 2:
                dist = new UniformRealDistribution(20.1, 30.6);
                val = dist.getNumericalMean();
                break;
            case 3:
                dist = new UniformRealDistribution(45, 50.2);
                val = dist.getNumericalMean();
                break;
            case 4:
                dist = new UniformRealDistribution(30.7, 40.5);
                val = dist.getNumericalMean();
                break;
            case 5:
                dist = new UniformRealDistribution(1.5, 4.9);
                val = dist.getNumericalMean();
                break;
            case 6:
                dist = new UniformRealDistribution(10.9, 16.9);
                val = dist.getNumericalMean();
                break;
            case 7:
                dist = new UniformRealDistribution(40.8, 55.2);
                val = dist.getNumericalMean();
                break;
            case 8:
                dist = new UniformRealDistribution(22.1, 25.8);
                val = dist.getNumericalMean();
                break;
            case 9:
                dist = new UniformRealDistribution(3.7, 4.5);
                val = dist.getNumericalMean();
                break;
         
                
        }
        for (int i = 0; i < ApplicationManager.getNUMTASK(); i++) {
            int randomInteger = (int) (Math.random() * 1000);
            double random = 0.5+ Math.random() / cpu;
            while (randomInteger < 500) {
                randomInteger = (int) (Math.random() * 1000);
            } 
            ProfilerEntry entry=new ProfilerEntry(randomInteger, ((val+ (val*random)) / (cpu)));             
            entries.add(new ProfilerEntry(randomInteger, ((val+ (val*random)) / (cpu))));
         }
        return entries;
    }

    public static List<ProfilerEntry> generateEPTProfileEntries(int taskID) {
        List<ProfilerEntry> entries = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            double minRnd = 0.1 + (double) (Math.random() * 2);
            double maxRnd = 1.5 + (double) (Math.random() * 2);
            while (minRnd > maxRnd) {
                minRnd = 0.1 + (double) (Math.random() * 2);
                maxRnd = 1.5 + (double) (Math.random() * 2);
            }
            UniformRealDistribution dist = new UniformRealDistribution(minRnd, maxRnd);
            entries.add(new ProfilerEntry(1, dist.getNumericalMean() * 0.7));
        }
        return entries;
    }

    public static List<ProfilerEntry> generateEIdleProfileEntries(int taskID) {
        List<ProfilerEntry> entries = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            double minRnd = 0.1 + (double) (Math.random() * 2);
            double maxRnd = 1.5 + (double) (Math.random() * 2);
            while (minRnd > maxRnd) {
                minRnd = 0.1 + (double) (Math.random() * 2);
                maxRnd = 1.5 + (double) (Math.random() * 2);
            }
            UniformRealDistribution dist = new UniformRealDistribution(minRnd, maxRnd);
            entries.add(new ProfilerEntry(1, dist.getNumericalMean() * 0.09));
        }
        return entries;
    }
}
