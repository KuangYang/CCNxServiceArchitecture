package edu.bupt.sia.osgi;

import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Created by fish on 16-4-11.
 */
public class OSGILauncher {
    private static Framework framework;

    private OSGILauncher(){
    }

    private static Framework frameworkInit() throws BundleException {
        // Obtain a framework factory.
        ServiceLoader<FrameworkFactory> loader = ServiceLoader.load(FrameworkFactory.class);
        FrameworkFactory factory = loader.iterator().next();
        Map<String, String> configProps = new HashMap<String, String>();
        // TODO: add some config properties
        configProps.put("org.osgi.framework.storage.clean", "onFirstInit");
        // And get a framework.
        framework = factory.newFramework(configProps);
        //初始化framework
//        framework.init();
        framework.start();
        return framework;
    }

    protected static Framework getFramework(){
        if(framework == null){
            try {
                framework = frameworkInit();
            } catch (BundleException e) {
                e.printStackTrace();
            }
        }
        return framework;
    }
}
