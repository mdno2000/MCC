/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.Service;

import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import phdproject.mcc.mccproject.application.ApplicationManager;
import phdproject.mcc.mccproject.profiler.Profiler;
import phdproject.mcc.mccproject.resource.MobileDevice;
import phdproject.mcc.mccproject.resource.PublicCloud;
import phdproject.mcc.mccproject.resource.Resource;

/**
 *
 * @author Administrator
 */
@Service
public class ResourceService {
    public PublicCloud getPublicCloud()
    {
         return (PublicCloud) Resource.getResourcesList().stream().filter(res -> res.getResName().equals("PCLOUD")).findFirst().get();

    }
    public Resource getResource(int id)
    {
        return Resource.getRescourceByID(id);
    }
    public List<Profiler> getMobileProfileList(int userID){
        
        List<Profiler> profList=null;
        MobileDevice mobile = ApplicationManager.getUserProfileByID(userID).getUserMobile(); 
        profList = mobile.getProfilerList();        
        return profList;
        
    }
}
