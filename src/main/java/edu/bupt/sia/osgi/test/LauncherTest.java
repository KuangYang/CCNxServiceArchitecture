package edu.bupt.sia.osgi.test;

import edu.bupt.sia.osgi.OSGIContoller;
import org.osgi.framework.Bundle;

import java.io.*;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fish on 16-4-11.
 */
public class LauncherTest {
    public static void main(String[] args){
        try {
            OSGIContoller osgiContoller = new OSGIContoller();
            List<Bundle> installedBundles = new LinkedList<Bundle>();
            //安装bundle
//            installedBundles.add(osgiContoller.installBundle(
//                "file:/home/fish/IdeaProjects/OSGILaunchTest/out/production/helloworldbundle.jar"));
            Bundle bundle = osgiContoller.installBundle("file:/home/fish/IdeaProjects/ServiceFramework/out/production/ServiceFramework.jar");

            File bundlefile = new File("/home/fish/IdeaProjects/ccnservice-myfirstservice/out/production/ccnservice-myfirstservice.jar");
            InputStream inputStream = new BufferedInputStream(new FileInputStream(bundlefile));
            Bundle bundle2 = osgiContoller.installBundle("ccnservice-myfirstservice", inputStream);

            Dictionary<String,String> dictionary = bundle2.getHeaders();
            Enumeration<String> keys = dictionary.keys();
            while(keys.hasMoreElements()){
                String keyStr = keys.nextElement();
                System.out.println(keyStr+" , "+dictionary.get(keyStr));
            }
            installedBundles.add(bundle);
            installedBundles.add(bundle2);
//            for (Bundle bundle : installedBundles) {
//                bundle.start();
//            }
//        } catch (BundleException e) {
//            e.printStackTrace();
            long id = bundle2.getBundleId();
            System.out.println("installed bundle id:"+id);
            osgiContoller.executeServiceByID(id, new String[]{"hello", "world"});
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
