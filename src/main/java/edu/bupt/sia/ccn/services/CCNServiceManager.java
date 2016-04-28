package edu.bupt.sia.ccn.services;

import edu.bupt.sia.ccn.CCNServiceHandler;
import edu.bupt.sia.osgi.OSGIContoller;
import org.ccnx.ccn.CCNHandle;
import org.ccnx.ccn.config.ConfigurationException;
import org.ccnx.ccn.impl.support.Log;
import org.ccnx.ccn.io.CCNFileInputStream;
import org.ccnx.ccn.io.CCNInputStream;
import org.ccnx.ccn.protocol.ContentName;
import org.ccnx.ccn.protocol.MalformedContentNameStringException;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yangkuang on 16-4-14.
 */

public class CCNServiceManager{
    CCNServiceTable<String, CCNServiceObject> _serviceTable = new CCNServiceTable<>(5);
    OSGIContoller _serviceController = new OSGIContoller();

    public CCNServiceManager()
            throws MalformedContentNameStringException, ConfigurationException,
            IOException {
        System.out.println("CCNServiceManager Start!");
    }

    public boolean service_existed(String serviceName) {//Check whether a specific service already existed in the serviceTable(or already be installed)
        boolean compare_result = false;
        if (_serviceTable.get(serviceName) != null) {
            return true;
        }
        return compare_result;
    }

    public boolean same_version(Version serviceVersion, String serviceName) {
        boolean compare_result = false;
        if (serviceVersion == _serviceTable.get(serviceName).serviceVersion()) {
            return true;
        }
        return compare_result;
    }

    //public boolean service_installed(String serviceName) { //serviceName == bundleSymbolicName
    //    boolean default_result = false;
    //    if (_serviceTable.get(serviceName) != null) {
    //        return true;
    //    }
    //    return default_result;
    //}

    public boolean serviceTable_withinSize() {
        boolean default_result = false;
        if (_serviceTable.usedSize() < 5) { //default max table size = 5
            return true;
        }
        return default_result;
    }

    public void removeService(String serviceName) {
        _serviceController.removeServiceBySymbolicName(serviceName);
        _serviceTable.delete(serviceName);
        Log.info("CCN service is removed!", serviceName);
    }

    public CCNFileInputStream fetchService(String serviceName) throws IOException, MalformedContentNameStringException, ConfigurationException {
        String ccnserviceName = "ccnx:/" + serviceName + ".jar";
        CCNServiceHandler ccnserviceHandler = new CCNServiceHandler(ccnserviceName);
        ContentName serviceContentName = ContentName.fromURI(ccnserviceName);
        CCNHandle service_ccnHandle = ccnserviceHandler.getCCNHandle();

        CCNFileInputStream serviceStream = new CCNFileInputStream(serviceContentName, service_ccnHandle);
        //here this function is needed to be completed

        return serviceStream;
    }

    public void installService(String serviceName, String servicePath) {
        Bundle bundle = _serviceController.installBundle(servicePath);
        long serviceID = bundle.getBundleId();
        Version serviceVersion = bundle.getVersion();

        String servicePopularity = "";
        //servicePopularity = serviceName.getPopularity(); //this function need to be completed in other field
        CCNServiceObject CCNService_Object = null;
        try {
            CCNService_Object = new CCNServiceObject(serviceID, serviceName, serviceVersion, servicePopularity);
        } catch (MalformedContentNameStringException e) {
            e.printStackTrace();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        _serviceTable.put(serviceName, CCNService_Object);

    }
    public void installService(String serviceName) {
        Bundle bundle = _serviceController.installBundleByCCNIOStream(serviceName);
        long serviceID = bundle.getBundleId();
        Version serviceVersion = bundle.getVersion();

        String servicePopularity = "";
        //servicePopularity = serviceName.getPopularity(); //this function need to be completed in other field
        CCNServiceObject CCNService_Object = null;
        try {
            CCNService_Object = new CCNServiceObject(serviceID, serviceName, serviceVersion, servicePopularity);
        } catch (MalformedContentNameStringException e) {
            e.printStackTrace();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        _serviceTable.put(serviceName, CCNService_Object);

    }

    public void startLocalService(String serviceName) {
        if (service_existed(serviceName)) {
            System.out.println("Service:"+serviceName+" is existed and executing..");
            _serviceController.executeServiceBySymbolicName(serviceName, null);
        }else {
            System.out.println("Service:"+serviceName+" is not existed and installing..");
            installService(serviceName);
            startLocalService(serviceName);
        }
    }

    public void startCCNService(String serviceName, CCNFileInputStream serviceStream) {
//        Bundle bundleBase = _serviceController.installBundle("file:/home/fish/IdeaProjects/ServiceFramework/out/production/ServiceFramework.jar");
//        Bundle bundle = _serviceController.installBundle(serviceName, serviceStream);
        _serviceController.executeServiceBySymbolicName(serviceName, null);
    }
}
