package opencsp.uacontroller.asterisk;


import opencsp.Log;
import opencsp.asterisk.Asterisk;
import opencsp.csta.types.Connection;
import opencsp.devices.SIPPhone;
import opencsp.uacontroller.UAController;
import opencsp.util.ConfigurationProvider;
import org.asteriskjava.manager.action.HangupAction;
import org.asteriskjava.manager.action.OriginateAction;

public class AMIController implements UAController {
    public static final String TAG = "AMIController";
    public static final String TYPE = "ami";

    private ConfigurationProvider config;
    private SIPPhone sipPhone;
    private Asterisk asterisk;

    public AMIController(Asterisk asterisk, SIPPhone phone, ConfigurationProvider config) {
        Log.d(TAG, "Creating new AMIController for phone " + phone.toString());
        this.config = config;
        this.sipPhone = phone;
        this.asterisk = asterisk;
    }

    @Override
    public void makeCall(String calledDirectoryNumber) {
        OriginateAction action = new OriginateAction();
        action.setChannel(sipPhone.getTechnology() + "/" + sipPhone.getDeviceId().toString());
        action.setContext(config.getConfigurationValue("cti_outbound_context"));
        action.setExten(calledDirectoryNumber);
        action.setPriority(1);
        Log.d(TAG, action.toString());
        asterisk.trySendAction(action);
    }

    @Override
    public void answerCall() {
        Log.d(TAG, "answerCall() is not implemented for AMI-controlled devices.");
    }

    @Override
    public void clearConnection(Connection connectionToBeCleared) {
        HangupAction action = new HangupAction();
        action.setChannel(connectionToBeCleared.getChannel());
        Log.d(TAG, action.toString());
        asterisk.trySendAction(action);
    }

    @Override
    public void holdCall() {
        Log.d(TAG, "holdCall() is not implemented for AMI-controlled devices.");
    }

    @Override
    public void retrieveCall() {
        Log.d(TAG, "retrieveCall() is not implemented for AMI-controlled devices.");
    }

    /**
     * Consultation calls will be "Call Waiting" calls on the originating device.
     * @param consultedDevice the device to consult
     */
    @Override
    public void consultationCall(String consultedDevice) {
        OriginateAction action = new OriginateAction();
        action.setChannel(sipPhone.getTechnology() + "/" + sipPhone.getDeviceId().toString());
        action.setContext(config.getConfigurationValue("cti_outbound_context"));
        action.setExten(consultedDevice);
        action.setPriority(1);
        Log.d(TAG, action.toString());
        asterisk.trySendAction(action);
    }
}
