package edu.bupt.sia.ccn.services;

import edu.bupt.sia.osgi.OSGIContoller;
import org.ccnx.ccn.config.ConfigurationException;
import org.ccnx.ccn.io.CCNInputStream;
import org.ccnx.ccn.protocol.MalformedContentNameStringException;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by on 16-4-14.
 */

public class CCNServiceManager{
    CCNServiceTable<String, CCNServiceObject> _serviceTable = new CCNServiceTable<>(5);
    OSGIContoller _serviceController = new OSGIContoller();
    List<Bundle> _installedBundles = new LinkedList<Bundle>();

    public CCNServiceManager()
            throws MalformedContentNameStringException, ConfigurationException,
            IOException {
        System.out.println("CCNServiceManager Start!");
    }

    public boolean service_existed(String serviceName) {
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

    public boolean service_installed(String serviceName) { //serviceName == bundleSymbolicName
        boolean default_result = false;
        if (_serviceController.getBundleContext().getBundle(serviceName) != null) {
            return true;
        }
        return default_result;
    }

    public boolean serviceTable_withinSize() {
        boolean default_result = false;
        if (_serviceTable.usedSize() < 5) {
            return true;
        }
        return default_result;
    }

    public void removeService(String serviceName) {
        _serviceController.removeServiceBySymbolicName(serviceName);
        _serviceTable.delete(serviceName);
    }

    public void fetchService(String serviceName) {

    }

    public void addService(String serviceName) {
        Bundle bundle = _serviceController.getBundleContext().getBundle(serviceName);
        long serviceID = bundle.getBundleId();
        Version serviceVersion = bundle.getVersion();

        String servicePopularity = "";
        //servicePopularity = serviceName.getPopularity(); //this function need to be completed in other field= serviceName.getPopularity(); //this function need to be completed in other field

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
        String default_path = "/home/fish/IdeaProjects/default_name/out/production/default_name.jar";
        Bundle bundleBase = _serviceController.installBundle("file:/home/fish/IdeaProjects/ServiceFramework/out/production/ServiceFramework.jar");
        Bundle bundle = _serviceController.installBundle(default_path.replaceAll("default_name", serviceName));

        _installedBundles.add(bundleBase);
        _installedBundles.add(bundle);

        _serviceController.executeServiceBySymbolicName(serviceName, null);
    }

    public void startCCNService(String serviceName, CCNInputStream serviceStream) {
        Bundle bundleBase = _serviceController.installBundle("file:/home/fish/IdeaProjects/ServiceFramework/out/production/ServiceFramework.jar");
        Bundle bundle = _serviceController.installBundle(serviceName, serviceStream);

        _installedBundles.add(bundleBase);
        _installedBundles.add(bundle);

        _serviceController.executeServiceBySymbolicName(serviceName, null);
    }

}
