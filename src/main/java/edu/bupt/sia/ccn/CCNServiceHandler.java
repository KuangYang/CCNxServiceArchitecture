package edu.bupt.sia.ccn;

import edu.bupt.sia.ccn.services.CCNServiceManager;
import org.ccnx.ccn.CCNHandle;
import org.ccnx.ccn.CCNInterestHandler;
import org.ccnx.ccn.config.ConfigurationException;
import org.ccnx.ccn.impl.support.Log;
import org.ccnx.ccn.profiles.SegmentationProfile;
import org.ccnx.ccn.profiles.metadata.MetadataProfile;
import org.ccnx.ccn.protocol.ContentName;
import org.ccnx.ccn.protocol.Interest;
import org.ccnx.ccn.protocol.MalformedContentNameStringException;

import java.io.IOException;

/**
 * Created by fish on 16-4-21.
 */
public class CCNServiceHandler implements CCNInterestHandler {
    static String DEFAULT_URI = "ccnx:/";

    protected CCNHandle ccnHandle;
    protected ContentName _prefix;
    protected CCNServiceManager manager;

    public CCNServiceHandler(String ccnxURI) throws MalformedContentNameStringException, IOException, ConfigurationException {
        _prefix = ContentName.fromURI(ccnxURI);
        ccnHandle = CCNHandle.open();
        ccnHandle.registerFilter(_prefix, this);
        manager = new CCNServiceManager();
    }

    @Override
    public boolean handleInterest(Interest interest) {
        System.out.println(interest.name().count()+","+interest.name().toURIString());
        if (!_prefix.isPrefixOf(interest.name())) {
            Log.info("Unexpected interest, {0}", _prefix);
            return false;
        }
        if (SegmentationProfile.isSegment(interest.name())
                && !SegmentationProfile.isFirstSegment(interest.name())) {
            Log.info("Not first segment, ignoring {0}.", interest.name());
            return false;
        }else if (MetadataProfile.isHeader(interest.name())) {
            Log.info("interest for the first segment of the header, ignoring {0}.", interest.name());
            return false;
        }
        ServiceNameObject serviceNameObject = ServiceNameParser.getServiceName(interest.name());
        if(serviceNameObject != null) {
            if (serviceNameObject.getServiceName() != null
                    && serviceNameObject.getServiceName().length() > 0) {
                manager.startLocalService(serviceNameObject.getServiceName(), serviceNameObject.getVersion());
            } else {
                Log.warning("Service Name parse error!", serviceNameObject);
            }
        }else{
            Log.warning("ServiceNameObject is null!");
        }
        return false;
    }

    protected boolean serviceInit(ServiceNameObject serviceNameObject){
        String serviceName = serviceNameObject.getServiceName();
        if(!manager.service_existed(serviceName)){
//            manager.fetchService();
        }
        return true;
    }

    public static void main(String[] args){
        if (args.length < 1) {
            System.err.println("usage: CCNServiceHandler [<ccn prefix URI> default: ccn:/]");
            return;
        }
        String ccnURI = (args.length > 0) ? args[0] : DEFAULT_URI;
        try {
            CCNServiceHandler ccnServiceHandler = new CCNServiceHandler(ccnURI);

        } catch (MalformedContentNameStringException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public CCNHandle getCCNHandle() {
        return ccnHandle;
    }
}
