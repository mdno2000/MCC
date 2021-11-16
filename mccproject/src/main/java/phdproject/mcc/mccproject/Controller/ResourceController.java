/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import phdproject.mcc.mccproject.Service.ResourceService;
import phdproject.mcc.mccproject.Service.UserProfileService;
import phdproject.mcc.mccproject.applicationuser.UserProfile;
import phdproject.mcc.mccproject.profiler.Profiler;

/**
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/mccproject/resource/")
public class ResourceController{
    
    @Autowired
    Profiler profiler;
    @Autowired
    UserProfileService userprofileservice;
    @Autowired
    ResourceService resourceService;  
     
    @ResponseBody
    @RequestMapping(value = "getmobileprofile/{id}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Profiler>  getMobileDeviceProfiles(@PathVariable Integer id) { 
        
        System.out.println("Mobile device id.. "+userprofileservice.getProfile(id).getUserMobile().getAvailEnergy());
        return userprofileservice.getProfile(id).getUserMobile().getProfilerList();
        //return resourceService.getMobileProfileList(usrID);
    }
    
}
