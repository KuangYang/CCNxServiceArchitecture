package edu.bupt.sia.ccn;

import org.ccnx.ccn.protocol.ContentName;

/**
 * Created by fish on 16-4-20.
 */
public class ServiceNameParser {
    public static ServiceNameObject getServiceName(ContentName name){
        int count = name.count();
        ServiceNameObject serviceNameObject = new ServiceNameObject();
        for(int i = 0; i < count; ++i){
            String tmp = name.stringComponent(i);
            if(tmp.charAt(0) == '{' && tmp.charAt(tmp.length()-1) == '}'){
                String[] arr = tmp.split(",");
                serviceNameObject.setContentName(name.cut(i).toURIString());
                for (int n = 0; n < arr.length; ++n){
                    String[] t = arr[n].split(":");
                    switch (t[0]){
                        case "servicename":
                            serviceNameObject.setServiceName(t[1]);
                            break;
                        case "type":
                            serviceNameObject.setType(t[1]);
                            break;
                        case "version":
                            serviceNameObject.setVersion(t[1]);
                            break;
                        case "args":
                            String arg = t[1].substring(1,t[1].length()-1);
                            serviceNameObject.setArgs(arg.split(","));
                            break;
                    }
                }
            }
        }
        return serviceNameObject;
    }
}
