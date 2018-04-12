package edu.skku.einstrasse.iotivity_client.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.iotivity.base.ModeType;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements
        OcPlatform.OnResourceFoundListener,
        OcResource.OnGetListener,
        OcResource.OnPutListener,
        OcResource.OnPostListener {

    // Constants
    private final static String TAG = HomeFragment.class.getSimpleName();
    private final static String ETAG = "Einstrasse@@@";

    //UI Components
    private Button mLightConnectBtn;
    private Button mLightOnBtn;
    private Button mLightOffBtn;

    // Internal data
    static private TaskState ConnectionState = TaskState.IDLE;

    // Resource Data
    static private Map<OcResourceIdentifier, OcResource> mFoundResources = new HashMap<>();
    static private OcResource mFoundLightResource = null;
    static private Light mLight = new Light();

    private static enum TaskState {
        IDLE, PROCESSING, DONE
    }

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /*
     * Show up the toast message from worker thread
     * It provides UI thread communication interface
     */
    private void showToast(final String msg, final int len){
        final Activity activity = getActivity();
        if (null != activity) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Context context = activity.getApplicationContext();
                    if (null != context) {
                        Toast.makeText(context, msg, len).show();
                    } else {
                        lg("getApplicationContext() function returns null!!");
                    }
                }
            });
        }
    };

    /*
     * Handles UI Component State. In this case, target is Connection Button
     */
    private void setConnectionBtnState(final TaskState state) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch(state) {
                    case IDLE:
                        mLightConnectBtn.setEnabled(true);
                        mLightConnectBtn.setText("Connect");
                        mLightConnectBtn.setTextColor(Color.rgb(0,0,0));
                        mLightOnBtn.setEnabled(false);
                        mLightOnBtn.setTextColor(Color.rgb(215,215,215));
                        mLightOffBtn.setEnabled(false);
                        mLightOffBtn.setTextColor(Color.rgb(215,215,215));
                        ConnectionState = TaskState.IDLE;
                        break;
                    case PROCESSING:
                        mLightConnectBtn.setEnabled(false);
                        mLightConnectBtn.setText("CONNECTING...");
                        mLightOnBtn.setEnabled(false);
                        mLightOnBtn.setTextColor(Color.rgb(215,215,215));
                        mLightOffBtn.setEnabled(false);
                        mLightOffBtn.setTextColor(Color.rgb(215,215,215));
                        ConnectionState = TaskState.PROCESSING;
                        break;
                    case DONE:
                        mLightConnectBtn.setEnabled(false);
                        mLightConnectBtn.setText("Connected!");
                        mLightConnectBtn.setTextColor(Color.BLUE);
                        mLightOnBtn.setEnabled(true);
                        mLightOnBtn.setTextColor(Color.rgb(0,0,0));
                        mLightOffBtn.setEnabled(true);
                        mLightOffBtn.setTextColor(Color.rgb(0,0,0));
                        ConnectionState = TaskState.DONE;
                        break;
                    default:
                        return;
                }
            }
        });
    }

    /*
     * Send GET Method for Light Resource
     */
    private void getLightResourceRepresentation() {
        lg("Request GET method for Light Resource");
        Map<String, String> queryParams = new HashMap<>();
        try {
            mFoundLightResource.get(queryParams, this);
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            lg("Error occured while invoking GET Method for Light Resource");
        }
    }
    private void putLightRepresentation(boolean switchMode) {
        mLight.setSwitch(switchMode);
        //mLight.setDefaultAngle(90);
        mLight.setOnAngle(88);
        mLight.setOffAngle(105);
        // TODO : SharedPreference에서 Angle 값 가져오기 필요
        OcRepresentation rep = null;
        try {
            rep = mLight.getOcRepresentation();
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            lg("Failed to get OcRepresentation from Light object");
        }
        Map<String, String> queryParams = new HashMap<>();

        try {
            mFoundLightResource.put(rep, queryParams, this);
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            lg("Error occured while invoking PUT Method Request for Light resource");
        }
    }

    private void IoTivityInit() {
        Context context = getActivity().getApplicationContext();
        PlatformConfig platformConfig = new PlatformConfig(
                getActivity(),
                context,
                ServiceType.IN_PROC,
                ModeType.CLIENT,
                "0.0.0.0",
                0,
                QualityOfService.LOW
        );
        Log.e(ETAG, "Configuring IoTivity Platform");
        OcPlatform.Configure(platformConfig);
    }

    private void findLightResource() {
        String lightRequestUri = OcPlatform.WELL_KNOWN_QUERY + "?rt=core.light";

        try {
            OcPlatform.findResource("",
                    lightRequestUri,
                    EnumSet.of(OcConnectivityType.CT_DEFAULT),
                    this
            );
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            Log.e(ETAG, "Find resource failed");
        }

    }

    @Override
    public synchronized void onResourceFound(OcResource ocResource) {
        lg("resource found!!");
        if (null == ocResource) {
            lg("Found resource is invalid!");
            return;
        }

        if (mFoundResources.containsKey(ocResource.getUniqueIdentifier())) {
            lg("Found a previous discovered resource again T.T");
        } else {
            lg("Found resource for the first time on server ID: " + ocResource.getServerId());
            mFoundResources.put(ocResource.getUniqueIdentifier(), ocResource);
        }

        String resourceUri = ocResource.getUri();
        String hostAddr = ocResource.getHost();
        lg("URI of the resource :" + resourceUri);
        lg("Host addr of the resource :" + hostAddr);

        lg("---- List of resource types ----");
        for (String resType : ocResource.getResourceTypes()) {
            lg("\n" + resType);
        }
        lg("---- List of resource interface ----");
        for (String resIf : ocResource.getResourceInterfaces()) {
            lg("\n" + resIf);
        }
        lg("---- List of resource connectivity type ----");
        for (OcConnectivityType connType : ocResource.getConnectivityTypeSet()) {
            lg("\n" + connType);
        }

        if (resourceUri.equals("/eine/light")) {
            mFoundLightResource = ocResource;
            getLightResourceRepresentation();
            showToast("Light Resource Found!", Toast.LENGTH_SHORT);
        }

        // TODO : 리소스 개수가 늘어날 경우를 대비해서, 해당 콜백 로직 변경 필요
        // TODO : 리소스 탐색 부분을 유저가 신경쓰지 않도록 추상화 필요

        setConnectionBtnState(TaskState.DONE);
    }

    @Override
    public synchronized void onFindResourceFailed(Throwable throwable, String uri) {
        Log.e(ETAG, "resource found Failed T.T!!");
        Log.e(ETAG, "Requested URI: " + uri);
        if (ConnectionState == TaskState.PROCESSING) {
            showToast("Find Resource Failed", Toast.LENGTH_SHORT);
            setConnectionBtnState(TaskState.IDLE);
        }
    }

    @Override
    public synchronized void onGetCompleted(List<OcHeaderOption> list, OcRepresentation ocRepresentation) {
        lg("GET request successfully finished");
        lg("Resource URI: " + ocRepresentation.getUri());

        try {
            mLight.setOcRepresentation(ocRepresentation);
        } catch (OcException e) {
            lg(e.toString());
            lg("Failed to read the attributes of a light resource");
        }
        lg("Light attributes:\n");
        lg(mLight.toString());
    }
    @Override
    public synchronized void onGetFailed(Throwable throwable) {

    }

    @Override
    public synchronized void onPutCompleted(List<OcHeaderOption> list, OcRepresentation ocRepresentation) {
        lg("Put Request Successfully Done");
    }
    @Override
    public synchronized void onPutFailed(Throwable throwable) {
        Log.e(TAG, "PUT Request Failed... : " + throwable.toString());
        lg("Put request Failed");
        showToast("Light Switch Control request failed T.T", Toast.LENGTH_SHORT);
    }

    @Override
    public synchronized void onPostCompleted(List<OcHeaderOption> list, OcRepresentation ocRepresentation) {

    }
    @Override
    public synchronized  void onPostFailed(Throwable throwable) {

    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflated = inflater.inflate(R.layout.fragment_home, container, false);
        mLightConnectBtn = (Button) inflated.findViewById(R.id.home_fragment_connect_btn);
        mLightOnBtn = (Button) inflated.findViewById(R.id.home_fragment_light_on_btn);
        mLightOffBtn = (Button) inflated.findViewById(R.id.home_fragment_light_off_btn);
        mLightConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Connect btn 을 궂이 누르지 않도록 버튼 추상화 필요
                Log.e(ETAG, "Connected Button Pressed");

                IoTivityInit();
                setConnectionBtnState(TaskState.PROCESSING);
                findLightResource();

            }
        });

        mLightOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putLightRepresentation(true);
            }
        });

        mLightOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putLightRepresentation(false);
            }
        });
        setConnectionBtnState(ConnectionState);
        return inflated;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void lg(final String txt) {
        Log.e(ETAG, txt);
    }
}
