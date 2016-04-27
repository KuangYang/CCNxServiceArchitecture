package edu.bupt.sia.osgi;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by fish on 16-4-11.
 */
public class OSGIContoller {
    private Framework framework;
    private BundleContext bundleContext;
    final static protected String CCNServiceTag = "CCNService";

    public OSGIContoller(){
        this.framework = OSGILauncher.getFramework();
        this.bundleContext = framework.getBundleContext();
        //add initial bundles
        try {
//            Bundle ccnbundle01 = bundleContext.installBundle("file:libs/org.apache.felix.gogo.runtime_0.10.0.v201209301036.jar");
//            ccnbundle01.start();
//            Bundle ccnbundle02 = bundleContext.installBundle("file:libs/org.apache.felix.gogo.shell_0.10.0.v201212101605.jar");
//            ccnbundle02.start();
//            Bundle ccnbundle03 = bundleContext.installBundle("file:libs/org.apache.felix.gogo.command_0.10.0.v201209301215.jar");
//            ccnbundle03.start();

            Bundle ccnbundle = bundleContext.installBundle("file:libs/ccn-bundle_1.0.0.jar");
            ccnbundle.start();
            Bundle ccniobundle = bundleContext.installBundle("file:libs/CCNIOService.jar");
            ccniobundle.start();
        } catch (BundleException e) {
            e.printStackTrace();
        }
    }

    public Bundle installBundle(String path){
        Bundle bundle = null;
        try {
            bundle = bundleContext.installBundle(path);
            bundle.start();
        } catch (BundleException e) {
            System.out.println("installBundle exception:"+path);
            e.printStackTrace();
        }
        return bundle;
    }

    public Bundle installBundle(String bundleName, InputStream inputStream){
        Bundle bundle = null;
        try {
            bundle = bundleContext.installBundle(bundleName, inputStream);
            bundle.start();
        } catch (BundleException e) {
            System.out.println("installBundle by InputStream exception:"+bundleName);
            e.printStackTrace();
        }
        return bundle;
    }

    public void executeServiceByID(long bundleID, String[] args){
        Bundle b = bundleContext.getBundle(bundleID);
        String ServiceEntryName = b.getHeaders().get(CCNServiceTag);
//        String ServiceEntryName = "";
        if(ServiceEntryName != null && ServiceEntryName.length() > 0) {
            ServiceReference sr = bundleContext.getServiceReference(ServiceEntryName);
            Object o = bundleContext.getService(sr);
            try {
                Method m = o.getClass().getMethod("execute", String[].class);
                m.invoke(o, new Object[]{args});
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                System.out.println("Can't find execute method!!");
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }else{
            System.err.println("executeServiceByID : Can't get Service entry class");
        }

    }

    public void executeServiceBySymbolicName(String bundleSymbolicName, String[] args){
        Bundle b = bundleContext.getBundle(bundleSymbolicName);
        String ServiceEntryName = b.getHeaders().get(CCNServiceTag);
//        String ServiceEntryName = "";
        if(ServiceEntryName != null && ServiceEntryName.length() > 0) {
            ServiceReference sr = bundleContext.getServiceReference(ServiceEntryName);
            Object o = bundleContext.getService(sr);
            try {
                Method m = o.getClass().getMethod("execute", String[].class);
                m.invoke(o, new Object[]{args});
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                System.out.println("Can't find execute method!!");
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }else{
            System.err.println("executeServiceBySymbolicName : Can't get Service entry class");
        }
    }

    public void updateServiceByID(long bundleID){
    }

    public void removeServiceByID(long bundleID){
        Bundle b = bundleContext.getBundle(bundleID);
        try {
            b.uninstall();
        } catch (BundleException e) {
            e.printStackTrace();
        }
    }

    public void removeServiceBySymbolicName(String bundleSymbolicName){
        Bundle b = bundleContext.getBundle(bundleSymbolicName);
        try {
            b.stop();
            b.uninstall();
        } catch (BundleException e) {
            e.printStackTrace();
        }
    }

    public BundleContext getBundleContext() {
        return bundleContext;
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }
}
