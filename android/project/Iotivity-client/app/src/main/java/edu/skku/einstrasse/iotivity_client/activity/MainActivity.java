package edu.skku.einstrasse.iotivity_client.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import org.iotivity.base.ErrorCode;
import org.iotivity.base.ModeType;
import org.iotivity.base.ObserveType;
import org.iotivity.base.OcConnectivityType;
import org.iotivity.base.OcException;
import org.iotivity.base.OcHeaderOption;
import org.iotivity.base.OcPlatform;
import org.iotivity.base.OcRepresentation;
import org.iotivity.base.OcResource;
import org.iotivity.base.OcResourceIdentifier;
import org.iotivity.base.PlatformConfig;
import org.iotivity.base.QualityOfService;
import org.iotivity.base.ServiceType;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.skku.einstrasse.iotivity_client.R;
import edu.skku.einstrasse.iotivity_client.oic.res.Light;


/**
 * SimpleClient
 * <p/>
 * SimpleClient is a sample client app which should be started after the simpleServer is started.
 * It finds resources advertised by the server and calls different operations on it (GET, PUT,
 * POST, DELETE and OBSERVE).
 */
public class MainActivity extends Activity implements
        OcPlatform.OnResourceFoundListener,
        OcResource.OnGetListener,
        OcResource.OnPutListener,
        OcResource.OnPostListener,
        OcResource.OnObserveListener {

    private Map<OcResourceIdentifier, OcResource> mFoundResources = new HashMap<>();
    private OcResource mFoundLightResource = null;
    //local representation of a server's light resource
    private Light mLight = new Light();

    private OcConnectivityType adapterFlag = OcConnectivityType.CT_ADAPTER_IP;
    //flags related TCP transport test
    private boolean isRequestFlag = false;
    private boolean isTCPContained = false;

    /**
     * A local method to configure and initialize platform, and then search for the light resources.
     */
    private void startSimpleClient(OcConnectivityType type) {
        Context context = this;
        adapterFlag = type;

        PlatformConfig platformConfig = new PlatformConfig(
                this,
                context,
                ServiceType.IN_PROC,
                ModeType.CLIENT,
                "0.0.0.0", // By setting to "0.0.0.0", it binds to all available interfaces
                0,         // Uses randomly available port
                QualityOfService.LOW
        );
        msg("Configuring platform.");
        OcPlatform.Configure(platformConfig);

        try {
            msg("Finding all resources of type \"core.light\".");
            String requestUri = OcPlatform.WELL_KNOWN_QUERY + "?rt=core.light";
            OcPlatform.findResource("",
                    requestUri,
                    EnumSet.of(OcConnectivityType.CT_DEFAULT),
                    this
            );
            sleep(1);

            /*Find resource is done twice so that we discover the original resources a second time.
            These resources will have the same uniqueidentifier (yet be different objects),
            so that we can verify/show the duplicate-checking code in foundResource(above);
             */
            msg("Finding all resources of type \"core.light\" for the second time");
            OcPlatform.findResource("",
                    requestUri,
                    EnumSet.of(OcConnectivityType.CT_DEFAULT),
                    this
            );

        } catch (OcException e) {
            Log.e(TAG, e.toString());
            msg("Failed to invoke find resource API");
        }

        printLine();
    }

    /**
     * An event handler to be executed whenever a "findResource" request completes successfully
     *
     * @param ocResource found resource
     */
    @Override
    public synchronized void onResourceFound(OcResource ocResource) {
        if (null == ocResource) {
            msg("Found resource is invalid");
            return;
        }

        if (mFoundResources.containsKey(ocResource.getUniqueIdentifier())) {
            msg("Found a previously seen resource again!");
        } else {
            msg("Found resource for the first time on server with ID: " + ocResource.getServerId());
            mFoundResources.put(ocResource.getUniqueIdentifier(), ocResource);
        }

        if (null != mFoundLightResource) {
            if (ocResource.getUri().equals("/a/light")) {
                if (ocResource.getConnectivityTypeSet().contains(OcConnectivityType.CT_ADAPTER_TCP)) {
                    msg("Found resource which has TCP transport");
                    if (isTCPContained == false) {
                        isTCPContained = true;
                        return;
                    }
                }
            }
            msg("Found another resource, ignoring");
            return;

        }
        // Get the resource URI
        String resourceUri = ocResource.getUri();
        // Get the resource host address
        String hostAddress = ocResource.getHost();
        msg("\tURI of the resource: " + resourceUri);
        msg("\tHost address of the resource: " + hostAddress);
        // Get the resource types
        msg("\tList of resource types: ");
        for (String resourceType : ocResource.getResourceTypes()) {
            msg("\t\t" + resourceType);
        }
        msg("\tList of resource interfaces:");
        for (String resourceInterface : ocResource.getResourceInterfaces()) {
            msg("\t\t" + resourceInterface);
        }
        msg("\tList of resource connectivity types:");
        for (OcConnectivityType connectivityType : ocResource.getConnectivityTypeSet()) {
            msg("\t\t" + connectivityType);
        }
        printLine();

        //In this example we are only interested in the light resources
        if (resourceUri.equals("/a/light")) {
            //Assign resource reference to a global variable to keep it from being
            //destroyed by the GC when it is out of scope.
            if (OcConnectivityType.CT_ADAPTER_TCP == adapterFlag) {
                if (ocResource.getConnectivityTypeSet().contains(OcConnectivityType.CT_ADAPTER_TCP)) {
                    msg("set mFoundLightResource which has TCP transport");
                    mFoundLightResource = ocResource;
                    // Call a local method which will internally invoke "get" API
                    getLightResourceRepresentation();
                    return;
                }
            } else {
                msg("set mFoundLightResource which has UDP transport");
                mFoundLightResource = ocResource;
                // Call a local method which will internally invoke "get" API on the foundLightResource
                getLightResourceRepresentation();
            }
        }
    }

    @Override
    public synchronized void onFindResourceFailed(Throwable throwable, String uri) {
        msg("findResource request has failed");
        Log.e(TAG, throwable.toString());
    }

    /**
     * Local method to get representation of a found light resource
     */
    private void getLightResourceRepresentation() {
        msg("Getting Light Representation...");

        Map<String, String> queryParams = new HashMap<>();
        try {
            // Invoke resource's "get" API with a OcResource.OnGetListener event
            // listener implementation
            sleep(1);
            mFoundLightResource.get(queryParams, this);
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            msg("Error occurred while invoking \"get\" API");
        }
    }

    /**
     * An event handler to be executed whenever a "get" request completes successfully
     *
     * @param list             list of the header options
     * @param ocRepresentation representation of a resource
     */
    @Override
    public synchronized void onGetCompleted(List<OcHeaderOption> list,
                                            OcRepresentation ocRepresentation) {
        msg("GET request was successful");
        msg("Resource URI: " + ocRepresentation.getUri());

        try {
            //Read attribute values into local representation of a light
            mLight.setOcRepresentation(ocRepresentation);
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            msg("Failed to read the attributes of a light resource");
        }
        msg("Light attributes: ");
        msg(mLight.toString());
        printLine();

        //Call a local method which will internally invoke put API on the foundLightResource
        putLightRepresentation();
    }

    /**
     * An event handler to be executed whenever a "get" request fails
     *
     * @param throwable exception
     */
    @Override
    public synchronized void onGetFailed(Throwable throwable) {
        if (throwable instanceof OcException) {
            OcException ocEx = (OcException) throwable;
            Log.e(TAG, ocEx.toString());
            ErrorCode errCode = ocEx.getErrorCode();
            //do something based on errorCode
            msg("Error code: " + errCode);
        }
        msg("Failed to get representation of a found light resource");
    }

    /**
     * Local method to put a different state for this light resource
     */
    private void putLightRepresentation() {
        //set new values
        mLight.setSwitch(true);
        mLight.setBrightness(15);

        msg("Putting light representation...");
        OcRepresentation representation = null;
        try {
            representation = mLight.getOcRepresentation();
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            msg("Failed to get OcRepresentation from a light");
        }

        Map<String, String> queryParams = new HashMap<>();

        try {
            sleep(1);
            // Invoke resource's "put" API with a new representation, query parameters and
            // OcResource.OnPutListener event listener implementation
            mFoundLightResource.put(representation, queryParams, this);
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            msg("Error occurred while invoking \"put\" API");
        }
    }

    /**
     * An event handler to be executed whenever a "put" request completes successfully
     *
     * @param list             list of the header options
     * @param ocRepresentation representation of a resource
     */
    @Override
    public synchronized void onPutCompleted(List<OcHeaderOption> list, OcRepresentation ocRepresentation) {
        msg("PUT request was successful");
        try {
            mLight.setOcRepresentation(ocRepresentation);
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            msg("Failed to create Light representation");
        }
        msg("Light attributes: ");
        msg(mLight.toString());
        printLine();

        //Call a local method which will internally invoke post API on the foundLightResource
        postLightRepresentation();
    }

    /**
     * An event handler to be executed whenever a "put" request fails
     *
     * @param throwable exception
     */
    @Override
    public synchronized void onPutFailed(Throwable throwable) {
        if (throwable instanceof OcException) {
            OcException ocEx = (OcException) throwable;
            Log.e(TAG, ocEx.toString());
            ErrorCode errCode = ocEx.getErrorCode();
            //do something based on errorCode
            msg("Error code: " + errCode);
        }
        msg("Failed to \"put\" a new representation");
    }

    /**
     * Local method to post a different state for this light resource
     */
    private void postLightRepresentation() {
        //set new values
        mLight.setSwitch(false);
        mLight.setBrightness(105);

        msg("Posting light representation...");
        OcRepresentation representation = null;
        try {
            representation = mLight.getOcRepresentation();
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            msg("Failed to get OcRepresentation from a light");
        }

        Map<String, String> queryParams = new HashMap<>();
        try {
            sleep(1);
            // Invoke resource's "post" API with a new representation, query parameters and
            // OcResource.OnPostListener event listener implementation
            mFoundLightResource.post(representation, queryParams, this);
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            msg("Error occurred while invoking \"post\" API");
        }
    }

    /**
     * An event handler to be executed whenever a "post" request completes successfully
     *
     * @param list             list of the header options
     * @param ocRepresentation representation of a resource
     */
    @Override
    public synchronized void onPostCompleted(List<OcHeaderOption> list,
                                             OcRepresentation ocRepresentation) {
        msg("POST request was successful");
        try {
            if (ocRepresentation.hasAttribute(OcResource.CREATED_URI_KEY)) {
                msg("\tUri of the created resource: " +
                        ocRepresentation.getValue(OcResource.CREATED_URI_KEY));
            } else {
                mLight.setOcRepresentation(ocRepresentation);
                msg(mLight.toString());
            }
        } catch (OcException e) {
            Log.e(TAG, e.toString());
        }

        //setting new values
        mLight.setSwitch(true);
        mLight.setBrightness(55);
        msg("Posting again light representation...");
        OcRepresentation representation2 = null;
        try {
            representation2 = mLight.getOcRepresentation();
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            msg("Failed to get OcRepresentation from a light");
        }

        Map<String, String> queryParams = new HashMap<>();
        try {
            // Invoke resource's "post" API with a new representation, query parameters and
            // OcResource.OnPostListener event listener implementation
            mFoundLightResource.post(representation2, queryParams, onPostListener2);
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            msg("Error occurred while invoking \"post\" API");
        }
    }

    /**
     * An event handler to be executed whenever a "post" request fails
     *
     * @param throwable exception
     */
    @Override
    public synchronized void onPostFailed(Throwable throwable) {
        if (throwable instanceof OcException) {
            OcException ocEx = (OcException) throwable;
            Log.e(TAG, ocEx.toString());
            ErrorCode errCode = ocEx.getErrorCode();
            //do something based on errorCode
            msg("Error code: " + errCode);
        }
        msg("Failed to \"post\" a new representation");
    }

    /**
     * Declare and implement a second OcResource.OnPostListener
     */
    OcResource.OnPostListener onPostListener2 = new OcResource.OnPostListener() {
        /**
         * An event handler to be executed whenever a "post" request completes successfully
         * @param list             list of the header options
         * @param ocRepresentation representation of a resource
         */
        @Override
        public synchronized void onPostCompleted(List<OcHeaderOption> list,
                                                 OcRepresentation ocRepresentation) {
            msg("Second POST request was successful");
            try {
                if (ocRepresentation.hasAttribute(OcResource.CREATED_URI_KEY)) {
                    msg("\tUri of the created resource: " +
                            ocRepresentation.getValue(OcResource.CREATED_URI_KEY));
                } else {
                    mLight.setOcRepresentation(ocRepresentation);
                    msg(mLight.toString());
                }
            } catch (OcException e) {
                Log.e(TAG, e.toString());
            }

            //Call a local method which will internally invoke observe API on the foundLightResource
            observeFoundLightResource();
        }

        /**
         * An event handler to be executed whenever a "post" request fails
         *
         * @param throwable exception
         */
        @Override
        public synchronized void onPostFailed(Throwable throwable) {
            if (throwable instanceof OcException) {
                OcException ocEx = (OcException) throwable;
                Log.e(TAG, ocEx.toString());
                ErrorCode errCode = ocEx.getErrorCode();
                //do something based on errorCode
                msg("Error code: " + errCode);
            }
            msg("Failed to \"post\" a new representation");
        }
    };

    /**
     * Local method to start observing this light resource
     */
    private void observeFoundLightResource() {
        try {
            sleep(1);
            // Invoke resource's "observe" API with a observe type, query parameters and
            // OcResource.OnObserveListener event listener implementation
            mFoundLightResource.observe(ObserveType.OBSERVE, new HashMap<String, String>(), this);
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            msg("Error occurred while invoking \"observe\" API");
        }
    }

    // holds current number of observations
    private static int mObserveCount = 0;

    /**
     * An event handler to be executed whenever a "post" request completes successfully
     *
     * @param list             list of the header options
     * @param ocRepresentation representation of a resource
     * @param sequenceNumber   sequence number
     */
    @Override
    public synchronized void onObserveCompleted(List<OcHeaderOption> list,
                                                OcRepresentation ocRepresentation,
                                                int sequenceNumber) {

        if (sequenceNumber != OcResource.OnObserveListener.MAX_SEQUENCE_NUMBER + 1) {
            msg("OBSERVE Result:");
            msg("\tSequenceNumber:" + sequenceNumber);
            try {
                mLight.setOcRepresentation(ocRepresentation);
            } catch (OcException e) {
                Log.e(TAG, e.toString());
                msg("Failed to get the attribute values");
            }
            msg(mLight.toString());

            if ((++mObserveCount) == 11) {
                msg("Cancelling Observe...");
                try {
                    mFoundLightResource.cancelObserve(QualityOfService.HIGH);
                } catch (OcException e) {
                    Log.e(TAG, e.toString());
                    msg("Error occurred while invoking \"cancelObserve\" API");
                }

                sleep(10);
                resetGlobals();
                if (true == isTCPContained && false == isRequestFlag) {
                    msg("Start TCP test...");
                    startSimpleClient(OcConnectivityType.CT_ADAPTER_TCP);
                    isRequestFlag = true;
                    return;
                } else if (true == isRequestFlag) {
                    msg("End TCP test...");
                    isRequestFlag = false;
                }

                msg("DONE");
                //prepare for the next restart of the SimpleClient
                enableStartButton();
            }
        }
    }

    /**
     * An event handler to be executed whenever a "observe" request fails
     *
     * @param throwable exception
     */
    @Override
    public synchronized void onObserveFailed(Throwable throwable) {
        if (throwable instanceof OcException) {
            OcException ocEx = (OcException) throwable;
            Log.e(TAG, ocEx.toString());
            ErrorCode errCode = ocEx.getErrorCode();
            //do something based on errorCode
            msg("Error code: " + errCode);
        }
        msg("Observation of the found light resource has failed");
    }

    //******************************************************************************
    // End of the OIC specific code
    //******************************************************************************

    private final static String TAG = MainActivity.class.getSimpleName();
    private TextView mConsoleTextView;
    private ScrollView mScrollView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConsoleTextView = (TextView) findViewById(R.id.consoleTextView);
        mConsoleTextView.setMovementMethod(new ScrollingMovementMethod());
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mScrollView.fullScroll(View.FOCUS_DOWN);
        final Button button = (Button) findViewById(R.id.button);
        final Button setting_button = (Button) findViewById(R.id.setting_button);

        if (null == savedInstanceState) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button.setText("Re-start");
                    button.setEnabled(false);
                    new Thread(new Runnable() {
                        public void run() {
                            isTCPContained = false;
                            startSimpleClient(OcConnectivityType.CT_ADAPTER_IP);
                        }
                    }).start();
                }
            });

            setting_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent setting_intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(setting_intent);
                }
            });
        } else {
            String consoleOutput = savedInstanceState.getString("consoleOutputString");
            mConsoleTextView.setText(consoleOutput);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("consoleOutputString", mConsoleTextView.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String consoleOutput = savedInstanceState.getString("consoleOutputString");
        mConsoleTextView.setText(consoleOutput);
    }

    private void enableStartButton() {
        runOnUiThread(new Runnable() {
            public void run() {
                Button button = (Button) findViewById(R.id.button);
                button.setEnabled(true);
            }
        });
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }

    private void msg(final String text) {
        runOnUiThread(new Runnable() {
            public void run() {
                mConsoleTextView.append("\n");
                mConsoleTextView.append(text);
                mScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
        Log.i(TAG, text);
    }

    private void printLine() {
        msg("------------------------------------------------------------------------");
    }

    private synchronized void resetGlobals() {
        mFoundLightResource = null;
        mFoundResources.clear();
        mLight = new Light();
        mObserveCount = 0;
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent with changes sending broadcast IN ");

        Intent i = new Intent();
        i.setAction(intent.getAction());
        i.putExtra(NfcAdapter.EXTRA_NDEF_MESSAGES,
                intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES));
        sendBroadcast(i);
        Log.d(TAG, "Initialize Context again resetting");
    }

}
