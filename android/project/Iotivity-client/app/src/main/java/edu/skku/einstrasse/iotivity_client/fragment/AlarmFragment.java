package edu.skku.einstrasse.iotivity_client.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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

import edu.skku.einstrasse.iotivity_client.IoTivity;
import edu.skku.einstrasse.iotivity_client.R;
import edu.skku.einstrasse.iotivity_client.activity.WeeklyAlarmAddActivity;
import edu.skku.einstrasse.iotivity_client.adapter.AlarmListAdapter;
import edu.skku.einstrasse.iotivity_client.oic.res.AlarmJSONData;
import edu.skku.einstrasse.iotivity_client.oic.res.WeeklyAlarmHandler;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlarmFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * factory method to
 * create an instance of this fragment.
 */
public class AlarmFragment extends Fragment implements
        OcPlatform.OnResourceFoundListener,
        OcResource.OnGetListener {

    // Constant
    private final static String TAG = HomeFragment.class.getSimpleName();
    private final static String ETAG = "Einstrasse@@@";
    private final static int REQUEST_CODE_ALARM_CREATE = 1;

    private OcResource.OnPutListener onPutListener = null;

    //UI Components
    private Button btn_get_data = null;
    private RecyclerView alarm_recycler_view = null;
    private AlarmListAdapter adapter = null;
    private RecyclerView.LayoutManager layout_manager = null;
    private FloatingActionButton fab_add_alarm = null;

    // Internal data
//    static private TaskState ConnectionState = TaskState.IDLE;

    // Resource Data
    static private Map<OcResourceIdentifier, OcResource> mFoundResources = new HashMap<>();
    // TODO: singletone으로 변경
//    static private OcResource mFoundWeeklyAlarmHandler = null;
//    static private WeeklyAlarmHandler mWeeklyAlarmHandler = null;

//    private static enum TaskState {
//        IDLE, PROCESSING, DONE
//    }

    public AlarmFragment() {
        // Required empty public constructor
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

    private void findAlarmResource() {
        String lightRequestUri = OcPlatform.WELL_KNOWN_QUERY + "?rt=core.alarm";

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

        if (resourceUri.equals("/eine/alarm/weekly")) {
//            mFoundWeeklyAlarmHandler = ocResource;
            IoTivity.setWeeklyAlarmHandlerResource(ocResource);
            getWeeklyAlarmHandlerResourceRepresentation();
//            mFoundLightResource = ocResource;
//            getLightResourceRepresentation();
            showToast("Weekly Alarm Resource Found!", Toast.LENGTH_SHORT);
        }

        // TODO : 리소스 개수가 늘어날 경우를 대비해서, 해당 콜백 로직 변경 필요
        // TODO : 리소스 탐색 부분을 유저가 신경쓰지 않도록 추상화 필요
    }

    /*
     * Send GET Method for WeeklyAlarmHandler Resource
     */
    private void getWeeklyAlarmHandlerResourceRepresentation() {
        lg("Request GET method for Weekly Alarm Handler Resource");
        Map<String, String> queryParams = new HashMap<>();
        try {
//            mFoundWeeklyAlarmHandler.get(queryParams, this);
            IoTivity.getWeeklyAlarmHandlerResource().get(queryParams, this);
        } catch (OcException e) {
            Log.e(TAG, e.toString());
            lg("Error occured while invoking GET Method for weekly alarm Resource");
        }
    }


    @Override
    public synchronized void onFindResourceFailed(Throwable throwable, String uri) {
        Log.e(ETAG, "resource found Failed T.T!!");
        Log.e(ETAG, "Requested URI: " + uri);
//        showToast("Find Resource Failed", Toast.LENGTH_SHORT);
    }

    @Override
    public synchronized void onGetCompleted(List<OcHeaderOption> list, OcRepresentation ocRepresentation) {
        lg("GET Request successfully finished");
        lg("Resource URI: " + ocRepresentation.getUri());
        updateWeeklyAlarmList(ocRepresentation);

    }

    private void updateWeeklyAlarmList(OcRepresentation ocRepresentation) {
        //TODO: 이 디버그용 코드를 삭제하고 WeeklyAlarmHandler Class만들어서 거기로 빼버리기
        try {
//            lg((String)ocRepresentation.getValue("name"));
//            mWeeklyAlarmHandler.setOcRepresentation(ocRepresentation);
            IoTivity.getWeeklyAlarmHandler().setOcRepresentation(ocRepresentation);
//            String serializedData = ocRepresentation.getValue("serializedData");
//            int alarmCount = ocRepresentation.getValue("alarmCount");
//            lg("Serialized Data:" + serializedData);
//            lg("alarmCount:" + alarmCount);
        } catch (OcException e) {
            lg(e.toString());
        }

//        adapter.setData(mWeeklyAlarmHandler.getWeeklyAlarmList());
        adapter.setData(IoTivity.getWeeklyAlarmHandler().getWeeklyAlarmList());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public synchronized void onGetFailed(Throwable throwable) {
        lg("Get request Failed T.T");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        lg("## AlarmFragment onCreate");
        super.onCreate(savedInstanceState);
        if (IoTivity.getWeeklyAlarmHandler() == null) {
            IoTivity.setWeeklyAlarmHandler(new WeeklyAlarmHandler());
        }
        if (onPutListener == null) {
            onPutListener = new OcResource.OnPutListener() {
                @Override
                public void onPutCompleted(List<OcHeaderOption> list, OcRepresentation ocRepresentation) {
                    updateWeeklyAlarmList(ocRepresentation);
                }

                @Override
                public void onPutFailed(Throwable throwable) {
                    lg("Put Method failed T.T");
                }
            };
        }
//        if (null == mWeeklyAlarmHandler) {
//            mWeeklyAlarmHandler = new WeeklyAlarmHandler();
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        lg("## AlarmFragment onCreateView");
        View inflated = inflater.inflate(R.layout.fragment_alarm, container, false);
//        btn_get_data = (Button) inflated.findViewById(R.id.btn_get_data);
        alarm_recycler_view = (RecyclerView) inflated.findViewById(R.id.alarm_recycler_view);
        fab_add_alarm = (FloatingActionButton) inflated.findViewById(R.id.fab_add_alarm);
        layout_manager = new LinearLayoutManager(getActivity());

        fab_add_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (null == getActivity()) {
                    lg("Cannot load activity!");
                    return;
                }
                Intent intent = new Intent(activity, WeeklyAlarmAddActivity.class);
//                startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_ALARM_CREATE);

            }
        });
        alarm_recycler_view.setLayoutManager(layout_manager);
        adapter = new AlarmListAdapter(onPutListener);
        alarm_recycler_view.setAdapter(adapter);
        IoTivityInit();
        findAlarmResource();

        return inflated;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ALARM_CREATE) {
            if (resultCode == RESULT_OK) {
                lg("result OKOKOKKOKOK");
                String alarm_name = data.getStringExtra("name");
                int hour = data.getIntExtra("hour", 0);
                int min = data.getIntExtra("min", 0);
                int day = data.getIntExtra("day", 0);
                Map<String, String> queryParams = new HashMap<>();

                queryParams.put("name", alarm_name);
                queryParams.put("hour", String.valueOf(hour));
                queryParams.put("min", String.valueOf(min));
                queryParams.put("day", String.valueOf(day));
                OcRepresentation rep = null;
                try {
                    rep = IoTivity.getWeeklyAlarmHandler().getOcRepresentation();
//                    rep = mWeeklyAlarmHandler.getOcRepresentation();
                } catch (OcException e) {
                    Log.e(TAG, e.toString());
                    lg("Error occured while getOcRepresentation of weekly alarm handler");
                    return;
                }

                try {
//                    mFoundWeeklyAlarmHandler.post(rep, queryParams, new OcResource.OnPostListener() {
                    IoTivity.getWeeklyAlarmHandlerResource().post(rep, queryParams, new OcResource.OnPostListener() {
                        @Override
                        public void onPostCompleted(List<OcHeaderOption> list, OcRepresentation ocRepresentation) {
                            lg("Post success!");
                            updateWeeklyAlarmList(ocRepresentation);
                        }

                        @Override
                        public void onPostFailed(Throwable throwable) {
                            lg("Post failed T.T");
                        }
                    });
                } catch (OcException e) {
                    Log.e(TAG, e.toString());
                    lg("Error occured while invoking GET Method for Light Resource");
                    return;
                }
            }
        }
    }



    @Override
    public void onAttach(Context context) {
        lg("## AlarmFragment onAttach");
        super.onAttach(context);


//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    /*
    @Override
    public void onDetach() {
        lg("## AlarmFragment onDetach");
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onDestroy() {
        lg("## AlarmFragment onDestroy");
        super.onDestroy();
    }

    @Override
    public void onStop() {
        lg("## AlarmFragment onStop");
        super.onStop();
    }

    @Override
    public void onPause() {
        lg("## AlarmFragment onPause");
        super.onPause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        lg("## AlarmFragment onActivityCreated");
        super.onActivityCreated(savedInstance);
    }

    @Override
    public void onStart() {
        lg("## AlarmFragment onStart");
        super.onStart();
    }
    @Override
    public void onResume() {
        lg("## AlarmFragment onResume");
        super.onResume();
    }
    */

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
