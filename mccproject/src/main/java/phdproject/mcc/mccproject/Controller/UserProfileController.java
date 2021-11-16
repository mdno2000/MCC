/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.Controller;

import java.awt.PageAttributes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.websocket.server.PathParam;
import javax.ws.rs.DefaultValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping(value = "/mccproject/profile/")
public class UserProfileController {

    @Autowired
    UserProfileService profileservice;
    Profiler profiler;
    ResourceService resourceService; 

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserProfile> getProfiles() throws IOException {
        profileservice.addUserProfile("user1", "NEXUS");
        profileservice.addUserProfile("user2", "SAMSUNGTAB");
        List<String> profiles = new ArrayList<>();
        return profileservice.getUserProfiles();
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public UserProfile addProfile(@RequestBody UserProfile profile) throws IOException {

        UserProfile toadd = profileservice.addUserProfile(profile);
        //toadd.getUserMobile().allocateProfilers();
         return toadd;//String.valueOf(profile.getUserID());
    }

    /**
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getprofile/{id}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public UserProfile addProfile(@PathVariable String id) {

        int profileID = Integer.parseInt(id);
        return profileservice.getProfile(profileID);
    }

    @ResponseBody
    @RequestMapping(value = "updatemobile/{id}/{e}/{n}/{b}/{c}", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public UserProfile updateenergy(@PathVariable String id, @PathVariable Double e, @PathVariable Integer n, @PathVariable Double b, @PathVariable Integer c) {

        int profileID = Integer.parseInt(id);
        profileservice.getProfile(profileID).getUserMobile().setAvailEnergy(e);
        profileservice.getProfile(profileID).getUserMobile().setAvailNetwork(n);
        profileservice.getProfile(profileID).getUserMobile().setAvailBandwidth(b);
        profileservice.getProfile(profileID).getUserMobile().setConnectedToCloudlet(c);
        return profileservice.getProfile(profileID);
    }

}
