/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.psooptimizer;

import java.util.ArrayList;
import java.util.List;
import phdproject.mcc.mccproject.application.ApplicationManager;
import phdproject.mcc.mccproject.application.TaskResult;
import phdproject.mcc.mccproject.bagOfTask.Task;
import phdproject.mcc.mccproject.resource.Cloudlet;
import phdproject.mcc.mccproject.resource.MobileDevice;
import phdproject.mcc.mccproject.resource.PublicCloud;
import phdproject.mcc.mccproject.resource.RescourceAPI;
import phdproject.mcc.mccproject.resource.RescourceLocation;
import static phdproject.mcc.mccproject.resource.RescourceLocation.CLOUDLET;
import static phdproject.mcc.mccproject.resource.RescourceLocation.MOBILE;
import static phdproject.mcc.mccproject.resource.RescourceLocation.PUBLICCLOUD;
import phdproject.mcc.mccproject.resource.Resource;
import phdproject.mcc.mccproject.resource.resourcecharacteristics.ResourceCharacteristic;

/**
 *
 * @author Mohammad Alkhalaileh
 */
public class PSOCostFunction {

    /**
     * calculate all optimization values
     *
     * @param targetParticle
     * @param userID
     * @param AppID
     * @return
     */
    public static ArrayList callculateTimes(PSOParticle targetParticle, int userID, int AppID, int model) {
        PublicCloud pubCloud = (PublicCloud) Resource.getResourcesList().stream().filter(res -> res.getResName().equals("PCLOUD")).findFirst().get();
        Cloudlet cloudlet = (Cloudlet) Resource.getResourcesList().stream().filter(res -> res.getResName().equals("CLOUDLET")).findFirst().get();
        MobileDevice mobile = ApplicationManager.getUserProfileByID(userID).getUserMobile();
        List<Task> appTasks = ApplicationManager.getUserProfileByID(userID).getApplicationRunByID(AppID).getUserAppTask();
        double computResBw = 0;
        double dataToSrcbw = 0;
        double Total_PT_Mobile = 0; // PT; processing time on mobile
        double Total_PT_PCloud = 0; // PT; processing time on public cloud
        double Total_PT_Cloudlet = 0; // PT; processing time on loudlet
        double Total_ET_Mobile = 0;  // ET: execution time = PT + TT (transfer time)
        double Total_TT_Mobile = 0;
        double Total_ET_PCloud = 0;
        double Total_ET_Cloudlet = 0;
        double Total_PT = 0;
        double TotalDataTMobile = 0;    // total data processed in mobile   
        double TPT = 0;
        double TT = 0;
        double TTT = 0;
        double PTC = 0; // PT cost
        double TTC = 0;
        double TC = 0; // Transfer Cost
        double PE = 0; // Processing Energy
        double TE = 0; // transfer Energy
        double TTE = 0;
        int validDeadline = 1;
        double PT = 0;

        for (int tIndex = 0; tIndex < ApplicationManager.getNUMTASK(); tIndex++) { // itrate all tasks                        
            Task task = ApplicationManager.getUserProfileByID(userID).getApplicationRunByID(AppID).getUserAppTask().get(tIndex);
            double preE = task.getTaskres().getEnergy();
            Resource inputFileRes = task.getInputFileResLocation();
            if (tIndex == 0) {
                //System.out.println("Res LOC task 1: " + inputFileRes.getResLoc().toString());
            }
            //System.out.println("Data location.." + inputFileRes.getResLoc().name() );
            if ((inputFileRes.getResLoc() == RescourceLocation.OTHER || inputFileRes.getResLoc() == RescourceLocation.PUBLICCLOUD)
                    && mobile.getConnectedToPCloud() == 0) {
                targetParticle.setValidSol(0);
            } else {
                targetParticle.setValidSol(1);
            }

            double dataFromSrcbw = (double) inputFileRes.getResourceCharacteristicEntryCurValue(ResourceCharacteristic.BANDWIDTH);;
            if (tIndex == 0) {
                //System.out.println("Res LOC task 1, Bandwidth: " + mobile.getAvailBandwidth());
            }
            if (inputFileRes.getResLoc() == RescourceLocation.MOBILE) {

            }
            Resource outFileRes = task.getOutputFileResLocation();
            RescourceLocation computLocation = targetParticle.getPos()[tIndex];
            double dsize = task.getTaskres().get_inputDataSize();
            PSOOptimizer.PRINT = 0;
            if (model == 1) {
                if (tIndex == 0) {
                    dsize = 407;
                }
                if (tIndex == 4) {
                    dsize = 1839;
                }
                if (tIndex == 7) {
                    dsize = 1725;                    
                }
                if (tIndex == 9) {
                    dsize = 407;                    
                }
                if (tIndex == 10) {
                    dsize = 407;                    
                }
                if (tIndex == 16) {
                    dsize = 1839;                    
                }
                if (tIndex == 22) {
                    dsize = 407;                    
                }
                if (tIndex == 27) {
                    dsize = 1725;                    
                }
                if (tIndex == 29) {
                    dsize = 407;                    
                }
            }
            if (model == 2) {
                if (tIndex == 0) {
                     dsize = 451; 
                }
                if (tIndex == 4) {                   
                    dsize = 193;
                }
                if (tIndex == 7) {                    
                    dsize = 451;                    
                }
                if (tIndex == 9) {                    
                    dsize = 522;                    
                }
                if (tIndex == 10) {                    
                    dsize = 217;                    
                }
                if (tIndex == 16) {                    
                    dsize = 522;                    
                }
                if (tIndex == 22) {                    
                    dsize = 451;                    
                }
                if (tIndex == 27) {                    
                    dsize = 522;                    
                }
                if (tIndex == 29) {
                     dsize = 522;
                 }
            }
            
            if (model == 3) {
                if (tIndex == 0) {
                     dsize = 152; 
                }
                if (tIndex == 4) {                   
                    dsize = 152;
                }
                if (tIndex == 7) {                    
                    dsize = 140;                    
                }
                if (tIndex == 9) {                    
                    dsize = 41;                    
                }
                if (tIndex == 10) {                    
                    dsize = 152;                    
                }
                if (tIndex == 16) {                    
                    dsize = 119;                    
                }
                if (tIndex == 22) {                    
                    dsize = 140;                    
                }
                if (tIndex == 27) {                    
                    dsize = 119;                    
                }
                if (tIndex == 29) {
                     dsize = 41;
                 }
            }
            // to do: add data model 4 
            if (model == 4)
            {
                
            }

            //System.out.println("Data Size..." + dsize + " Task " + task.getTaskID());
            switch (targetParticle.getPos()[tIndex]) {
                case MOBILE:
                    computResBw = (double) mobile.getAvailBandwidth();
                    PT = mobile.estimateProcessingTime(userID, task);
                    // System.out.println("Processing time on mobile.." + PT +" Task " + task.getTaskID());
                    Total_PT_Mobile = Total_PT_Mobile + PT;
                    //System.out.println("Total Processing time on mobile.."+ Total_PT_Mobile);
                    double dtCost = 0;  //updated 20/6/2018
                    if (inputFileRes.getResLoc() == RescourceLocation.MOBILE) {
                        TT = 0;
                        task.getTaskres().setDtCost(0);
                    } else {
                        TT = (dsize / mobile.getAvailBandwidth());
                        Total_TT_Mobile = Total_TT_Mobile + TT;
                        TotalDataTMobile = TotalDataTMobile + dsize;
                        // download from cloud cost 
                        dtCost = (dsize / 1024) * 0.02;
                    }

                    PTC = PTC + 0;
                    //System.out.println("Processing Cost Mobile.." + PTC + " Task "+ task.getTaskID());
                    //TTC = TTC + (TT * 0.009);
                    //System.out.println("Transfer Cost Mobile.." + TTC + " Task "+ task.getTaskID());
                    //task.getTaskres().setEnergy(mobile.getAvergePTEEstimationFromProfiler() * PT, RescourceLocation.MOBILE);
                    task.getTaskres().setEnergy(0.196 * PT, RescourceLocation.MOBILE);
                    // System.out.println("Mobile Processing Energy.." + task.getTaskres().getEnergy() + " Task "+task.getTaskID());
                    // System.out.println("Mobile Network .." + mobile.getAvailNetwork());
                    task.getTaskres().setDtEnergy(mobile.getAvailNetwork(), TT);
                    // System.out.println("Mobile DT Energy.." + task.getTaskres().getDtEnergy()+ " Task "+task.getTaskID());
                    task.getTaskres().setPcost(0);
                    if (inputFileRes.getResLoc() != RescourceLocation.MOBILE) {
                        task.getTaskres().setDtEnergy(mobile.getAvailNetwork(), TT);
                        // network cost 
                        if (mobile.getAvailNetwork() == 3) { // data transfer cost updated 21/6 $/GB
                            dtCost = dtCost + ((dsize / 1024) * 0.05);

                        } else if (mobile.getAvailNetwork() == 2) {
                            dtCost = dtCost + ((dsize / 1024) * 1);
                            //task.getTaskres().setDtCost((dsize * 0.001024));
                        } else {
                            dtCost = dtCost + ((dsize / 1024) * 1);
                            //task.getTaskres().setDtCost((dsize * 0.001024));
                        }
                    } else {
                        task.getTaskres().setDtCost(0);
                    }

                    task.getTaskres().setDtCost(dtCost);
                    TTC = TTC + (task.getTaskres().getDtCost());
                    if (PSOOptimizer.PRINT == 1) {
                        System.out.println("Task " + task.getTaskID() + " DTCOst on mobile = " + (task.getTaskres().getDtCost()));
                        System.out.println("dtCost .." + dtCost + " Task " + task.getTaskID());
                        System.out.println("TT .." + TT + " Task " + task.getTaskID());
                        System.out.println("PT .." + PT + " Task " + task.getTaskID());
                    }
                    double postE = task.getTaskres().getEnergy();
                    RescourceLocation tloc = targetParticle.getPos()[tIndex];
                    RescourceLocation rloc = inputFileRes.getResLoc();
                    Total_ET_Mobile = Total_ET_Mobile + PT + TT;
                    //System.out.println("***"+task.getTaskID() + ": "+"Energy: " + preE + "---" + postE + 
                    // rloc + "---" +tloc);
                    break;
                case PUBLICCLOUD:
                    computResBw = (double) pubCloud.getResourceCharacteristicEntryCurValue(ResourceCharacteristic.BANDWIDTH);
                    PT = pubCloud.estimateProcessingTime(userID, task);
                    // System.out.println("Processing time on public cloud.." + PT + " Task " + task.getTaskID());
                    Total_PT_PCloud = Total_PT_PCloud + PT;
                    //System.out.println("Total Processing time on public cloud.." + Total_PT_PCloud);
                    dtCost = 0;
                    if (inputFileRes.getResLoc() == RescourceLocation.PUBLICCLOUD) {
                        TT = 0;
                        dtCost = (dsize / 1024) * 0.02;
                        //task.getTaskres().setDtCost((dsize / 1024) * 0.02 );          //Transfer cost within same region
                    } else {
                        TT = (dsize / RescourceAPI.callBandwidth(PUBLICCLOUD, inputFileRes.getResName(), 0));
                        //  Math.min(dataFromSrcbw, computResBw));
                        if (TT == Double.POSITIVE_INFINITY) {
                            TT = (dsize / mobile.getAvailBandwidth());
                            Total_TT_Mobile = Total_TT_Mobile + TT;
                            TotalDataTMobile = TotalDataTMobile + dsize;
                        } else {
                            dtCost = (dsize / 1024) * 0.02;
                        }
                        //task.getTaskres().setDtCost((dsize / 1024) * 0.02);             // https://aws.amazon.com/s3/pricing/ 
                        //TT = (dsize / Math.min(dataFromSrcbw, computResBw) / 1000);
                    }
                    Total_ET_PCloud = Total_ET_PCloud + PT + TT;
                    //The Cost for Task Processing Time 
                    PTC = PTC + (PT / (3600.0) * 0.3712);
                    //  System.out.println("Transfer time public cloud .." + TT + " Task "+ task.getTaskID());
                    //  System.out.println("Total execution time public cloud.."+ Total_ET_PCloud);                                        
                    //  System.out.println("Processing Cost Public Cloud.." + PTC + " Task "+ task.getTaskID());

                    task.getTaskres().setPcost(PT / (3600.0) * 0.3712);
                    task.getTaskres().setEnergy(0, RescourceLocation.MOBILE);
                    if (inputFileRes.getResLoc() != RescourceLocation.MOBILE) {
                        task.getTaskres().setDtEnergy(mobile.getAvailNetwork(), TT);
                        if (mobile.getAvailNetwork() == 3) { // data transfer cost updated 21/6 $/GB
                            dtCost = dtCost + ((dsize / 1024) * 0.05);

                        } else if (mobile.getAvailNetwork() == 2) {
                            dtCost = dtCost + ((dsize / 1024) * 1);
                            //task.getTaskres().setDtCost((dsize * 0.001024));
                        } else {
                            dtCost = dtCost + ((dsize / 1024) * 1);
                            //task.getTaskres().setDtCost((dsize * 0.001024));
                        }
                    } else {
                        task.getTaskres().setDtCost(0);
                    }

                    task.getTaskres().setDtCost(dtCost);
                    TTC = TTC + (task.getTaskres().getDtCost());
                    if (PSOOptimizer.PRINT == 1) {
                        System.out.println("Task " + task.getTaskID() + " DTCOst on pcloud = " + (task.getTaskres().getDtCost()));
                        System.out.println("Public Cloud");
                        System.out.println("TT .." + TT + " Task " + task.getTaskID());
                        System.out.println("PT .." + PT + " Task " + task.getTaskID());
                    }
                    break;
                case CLOUDLET:
                    computResBw = (double) cloudlet.getResourceCharacteristicEntryCurValue(ResourceCharacteristic.BANDWIDTH);
                    PT = cloudlet.estimateProcessingTime(userID, task);
                    Total_PT_Cloudlet = Total_PT_Cloudlet + PT;
                    dtCost = 0;
                    if (inputFileRes.getResLoc() == RescourceLocation.CLOUDLET) {
                        TT = 0;
                        dtCost = (dsize / 1024) * 0;
                        //task.getTaskres().setDtCost((dsize * 0.001) / 1024);

                    } else {
                        TT = (dsize / RescourceAPI.callBandwidth(CLOUDLET, inputFileRes.getResName(), 0)); //  Math.min(dataFromSrcbw, computResBw));
                        if (TT == Double.POSITIVE_INFINITY) {
                            TT = (dsize / mobile.getAvailBandwidth());
                            Total_TT_Mobile = Total_TT_Mobile + TT;
                            TotalDataTMobile = TotalDataTMobile + dsize;
                        } else {
                            dtCost = (dsize / 1024) * 0.02;
                        }
                        //TT = (dsize / Math.min(dataFromSrcbw, computResBw) / 1000);
                    }
                    Total_ET_Cloudlet = Total_ET_Cloudlet + PT + TT;
                    task.getTaskres().setPcost((PT / (3600) * 0.0742)); // updated 20/6/2018
                    PTC = PTC + (PT / (3600) * 0.0742);

                    task.getTaskres().setEnergy(0, RescourceLocation.MOBILE);
                    if (inputFileRes.getResLoc() != RescourceLocation.MOBILE) {
                        task.getTaskres().setDtEnergy(mobile.getAvailNetwork(), TT);
                        if (mobile.getAvailNetwork() == 3) { // data transfer cost updated 21/6 $/GB
                            dtCost = dtCost + ((dsize / 1024) * 0.05);

                        } else if (mobile.getAvailNetwork() == 2) {
                            dtCost = dtCost + ((dsize / 1024) * 1);
                            //task.getTaskres().setDtCost((dsize * 0.001024));
                        } else {
                            dtCost = dtCost + ((dsize / 1024) * 1);
                            //task.getTaskres().setDtCost((dsize * 0.001024));
                        }
                    } else {
                        task.getTaskres().setDtCost(0);
                    }
                    task.getTaskres().setDtCost(dtCost);
                    TTC = TTC + (task.getTaskres().getDtCost());

                    if (PSOOptimizer.PRINT == 1) {
                        System.out.println("Task " + task.getTaskID() + " DTCOst on cloudlet = " + (task.getTaskres().getDtCost()));
                        System.out.println("Cloudlet");
                        System.out.println("TT .." + TT + " Task " + task.getTaskID());
                        System.out.println("PT .." + PT + " Task " + task.getTaskID());
                    }
                    break;
                default:
                    PT = 0;
                    task.getTaskres().setEnergy(0, RescourceLocation.PUBLICCLOUD);
                    task.getTaskres().setDtEnergy(0);
                    break;
            }
            if (PT + TT > task.getDeadline()) {          // check deadline constraint       
                validDeadline = -1;
            }

            TPT = TPT + PT;
            TTT = TTT + TT;
            task.getTaskres().setProcssingTime(PT);
            task.getTaskres().setDtTime(TT);
            if ((task.getTaskres().getEnergy() > 0 || task.getTaskres().getDtEnergy() > 0)) {
                if (targetParticle.getPos()[tIndex] != RescourceLocation.MOBILE) {
                    int x = 0;
                }
            }
            double postE = task.getTaskres().getEnergy();
            RescourceLocation tloc = targetParticle.getPos()[tIndex];
            RescourceLocation rloc = inputFileRes.getResLoc();
            if (tloc != RescourceLocation.MOBILE && rloc != RescourceLocation.MOBILE) {
                // System.out.println(task.getTaskID() + ": "+"Energy: " + preE + "---" + postE + 
                //       rloc + "---" +tloc);
            }

        }
        // save values in arraylist
        ArrayList timeValues = new ArrayList();
        timeValues.add(TPT);
        timeValues.add(Total_PT_Mobile);
        timeValues.add(Total_PT_PCloud);
        timeValues.add(Total_PT_Cloudlet);
        timeValues.add(TTT);
        timeValues.add(TotalDataTMobile);
        timeValues.add(Total_ET_Mobile);
        timeValues.add(Total_ET_PCloud);
        timeValues.add(Total_ET_Cloudlet);
        timeValues.add(Total_TT_Mobile);

        timeValues.add(PTC);
        timeValues.add(TTC);
        TC = PTC + TTC;
        timeValues.add(TC);
        PE = 0.196 * Total_PT_Mobile;
        if (mobile.getAvailNetwork() == 3) {
            TTE = 0.264 * Total_TT_Mobile;
        }
        if (mobile.getAvailNetwork() == 2) {
            TTE = 0.501 * Total_TT_Mobile;
        }

        if (mobile.getAvailNetwork() == 1) {
            TTE = 0.550 * Total_TT_Mobile;
        }
        double ET = Double.max(Total_ET_Mobile, Double.max(Total_ET_PCloud, Total_ET_Cloudlet));
        double WE = Math.max(0, (ET - Total_PT_Mobile)) * 0.05;

        timeValues.add(PE);
        timeValues.add(TTE);
        TE = PE + TTE + WE;
//        if (Math.max(0, (ET - Total_PT_Mobile)) == 0) {
//            for (RescourceLocation loc2 : targetParticle.getPos()) {
//                System.out.print(loc2.name() + " ");
//            }
        if (PSOOptimizer.PRINT == 3) {
            System.out.println("Total Time Cloud.." + Total_ET_PCloud);
            System.out.println("Total Cloudlet.." + Total_ET_Cloudlet);
            System.out.println("Total Mobile.." + Total_PT_Mobile);
            System.out.println("Total Cost.." + TC);
            System.out.println("Total Energy.." + TE);
            System.out.println("Total Time.." + Math.max(Total_PT_Mobile, (ET)));
        }
//        }

//        System.out.println("Total Cost.." + TC);
//        System.out.println("Total Energy.." + TE);
//        System.out.println("Total Time.." + Math.max(Total_PT_Mobile, (ET)));
        timeValues.add(TE);
        timeValues.add((TE / 1000) * (TC));
        timeValues.add(validDeadline);
        int validE = mobile.getEnergyStatus(TE);  // check mobile energy constraint 
//        System.out.println("Valid Deadline: " + validDeadline);
//        System.out.println("Valid E Cost: " + validE);
//        System.out.println("OPT: " + (TE / 1000) * (TC));
//        System.out.println("Total Energy.." + TE);
//        System.out.println("Total Time Cloud.." + Total_ET_PCloud);
//        System.out.println("Total Cloudlet.." + Total_ET_Cloudlet);
//        System.out.println("Total Mobile.." + Total_PT_Mobile);
        timeValues.add(validE);
        if (PE > 0 || TTE > 0) {
            int v = 0;
        }
        if (targetParticle.getValidSol() == 1) {
            return timeValues;
        } else {
            return null;
        }

    }

    // Calculate energy: processing time in mobile and idle time 
    private static double calculateEnergy(double MPT, double IdealTime) {
        double E = 0;
        //double PT_E=
        return E;
    }

}
