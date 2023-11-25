package org.egov.fsm.util;

import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.util.encoders.Hex;
import org.egov.fsm.fsmProducer.FSMProducer;
import org.egov.fsm.web.model.FSMEvent;
import org.egov.fsm.web.model.user.User;
//import org.egov.hash.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FSMEventUtil {
	
	

	    @Autowired
	    private HMac hMac;

	   @Autowired
		private FSMProducer producer;
	    
	    @Autowired
//	    private HashService hashService;

	    @Value("${fsm.event.index.kafka.topic}")
	    private String fsmEventIndexKafkaTopic;


		public void processFSMEvent(FSMEvent fsmEvent){
//	        checkAndEnrichOwnerFromProperty(fsmEvent);
//	        hashMobileNumbers(fsmEvent);

	        producer.push(fsmEventIndexKafkaTopic, fsmEvent);
	        
            log.info("Send event on topic - " +fsmEventIndexKafkaTopic +" Request body for topic "+ fsmEvent);

	    }

	
	    /**
	     * Hashes mobileNumbers inside OwnerInfos
	     * @param wsEvent Incoming WSEvent containing the connection related data
	     */
	    private void hashMobileNumbers(FSMEvent fsmEvent){
	        User userInfos =fsmEvent.getFsmRequest().getFsm().getCitizen() ;
//	        userInfos.setMobileNumber(hashService.getHashValue(userInfos.getMobileNumber()));
	    }
	    private String getHashValue(String mobileNumber){
	        // Calculate hash value of mobileNumber
	       
	        byte[] inputDataBytesArray = mobileNumber.getBytes();
	        hMac.update(inputDataBytesArray, 0, inputDataBytesArray.length);
	        byte[] hmacOut = new byte[hMac.getMacSize()];

	        hMac.doFinal(hmacOut, 0);
	        return new String(Hex.encode(hmacOut));
	    }

}
