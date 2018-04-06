package edu.skku.einstrasse.iotivity_client;

import android.content.Context;
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
    private TaskState ConnectionState = TaskState.IDLE;

    // Resource Data
    private Map<OcResourceIdentifier, OcResource> mFoundResources = new HashMap<>();
    private OcResource mFoundLightResource = null;
    private Light mLight = new Light();

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
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(), msg, len).show();
            }
        });
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
                        mLightOnBtn.setEnabled(false);
                        mLightOffBtn.setEnabled(false);
                        ConnectionState = TaskState.IDLE;
                        break;
                    case PROCESSING:
                        mLightConnectBtn.setEnabled(false);
                        mLightConnectBtn.setText("CONNECTING...");
                        mLightOnBtn.setEnabled(false);
                        mLightOffBtn.setEnabled(false);
                        ConnectionState = TaskState.PROCESSING;
                        break;
                    case DONE:
                        mLightConnectBtn.setEnabled(false);
                        mLightConnectBtn.setText("Connected!");
                        mLightOnBtn.setEnabled(true);
                        mLightOffBtn.setEnabled(true);
                        ConnectionState = TaskState.DONE;
                        break;
                    default:
                        return;
                }
            }
        });
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

        showToast("Resource Found!", Toast.LENGTH_SHORT);
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

    }
    @Override
    public synchronized void onGetFailed(Throwable throwable) {

    }

    @Override
    public synchronized void onPutCompleted(List<OcHeaderOption> list, OcRepresentation ocRepresentation) {

    }
    @Override
    public synchronized void onPutFailed(Throwable throwable) {

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
                //TODO: Connect btn 눌렀을 시 이벤트 핸들러 구현
                Log.e(ETAG, "Connected Button Pressed");

                IoTivityInit();
                setConnectionBtnState(TaskState.PROCESSING);
                findLightResource();

            }
        });

        mLightOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: On btn 눌렀을 시 이벤트 핸들러 구현
            }
        });

        mLightOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Off btn 눌렀을 시 이벤트 핸들러 구현
            }
        });
        setConnectionBtnState(TaskState.IDLE);
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
