package edu.bupt.sia.ccn.services;

import org.ccnx.ccn.config.ConfigurationException;
import org.ccnx.ccn.protocol.MalformedContentNameStringException;
import org.osgi.framework.Version;

import java.io.IOException;

/**
 * Created by yangkuang on 16-4-14.
 */

public class CCNServiceObject {
    long _serviceID;
    String _serviceName;
    String _serviceVersion;
    int _servicePopularity;

    public CCNServiceObject(long serviceID, String serviceName, String serviceVersion, int servicePopularity)
            throws MalformedContentNameStringException, ConfigurationException,
            IOException {
        _serviceID = serviceID;
        _serviceName = serviceName;
        _serviceVersion = serviceVersion;
        _servicePopularity = servicePopularity;
    }

    public long serviceID() {

        return _serviceID;
    }

    public String serviceName() {

        return _serviceName;
    }

    public String serviceVersion() {

        return _serviceVersion;
    }

    public int servicePopularity() {

        return _servicePopularity;
    }
}
