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
//        configProps.put("org.osgi.framework.bundle.parent", "app");
//        configProps.put("org.osgi.framework.bootdelegation", "javax.crypto.*,javax.crypto.interfaces.*");
//        configProps.put("org.osgi.framework.executionenvironment", "JavaSE-1.8,JavaSE-1.7");
        configProps.put("org.osgi.framework.storage.clean", "onFirstInit");
        // And get a framework.
        framework = factory.newFramework(configProps);
        //初始化framework
        framework.init();
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
