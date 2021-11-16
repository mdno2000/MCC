/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.application;

import phdproject.mcc.mccproject.resource.RescourceAPI;
import phdproject.mcc.mccproject.resource.RescourceLocation;
import phdproject.mcc.mccproject.resource.Resource;

/**
 *
 * @author Mohammad Alkhalaileh
 */
public class ExecutionScenario {
    public static double [] randmizeDataSize(int numTasks)
    {
        double [] dsize =new double[numTasks];
        for(int i=0; i< dsize.length;i++)
        {
            dsize[i]=RescourceAPI.getRandDataSize();
        }
        return dsize;
    }
    public static RescourceLocation [] randmizeDataLocation(int numTasks)
    {
        RescourceLocation [] resLoc =new RescourceLocation[numTasks];
        for(int i=0; i< resLoc.length;i++)
        {
            int randomInteger = 1 + (int) (Math.random() * 5);
            if(randomInteger==3)
                randomInteger=2;
            
            resLoc[i]=Resource.getLocationFromID(randomInteger);
        }
        return resLoc;
    }
}
