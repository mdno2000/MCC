/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.Service;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phdproject.mcc.mccproject.application.ApplicationManager;
import phdproject.mcc.mccproject.applicationuser.UserProfile;
import phdproject.mcc.mccproject.profiler.Profiler;
import phdproject.mcc.mccproject.resource.Resource;

/**
 *
 * @author Administrator
 */
@Service
public class UserProfileService {

    @Autowired
    ResourceService resourceservice;

    public void addUserProfile(String userName, String device) throws IOException {
        UserProfile profile = new UserProfile(userName, device);
    }

    public UserProfile addUserProfile(UserProfile profile) throws IOException {
        int _userID = ApplicationManager.requestUserPofileID();
        profile.setUserID(profile.getUserID());
        //profile.getUserMobile().setResID(Resource.getRESOURCEID());
        Resource.setRESOURCEID(Resource.getRESOURCEID() + 1);
        Resource.getResourcesList().add(profile.getUserMobile());
        ApplicationManager.getUserProfileList().add(profile);

        return profile;
        //System.out.println("#apps ID "+profile.getUserApplications().size());
    }

    public List<UserProfile> getUserProfiles() {
        return ApplicationManager.getUserProfileList();
    }

    public UserProfile getProfile(int id) {
        return ApplicationManager.getUserProfileByID(id);
    }

//    public List<Profiler> getProfilers() {
//        return resourceservice.getPublicCloud().get
//    }
    }
