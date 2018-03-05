package org.iotivity.service;

import org.iotivity.service.client.RcsAddress;
import org.iotivity.service.client.RcsDiscoveryManager;
import org.iotivity.service.client.RcsDiscoveryManager.OnResourceDiscoveredListener;
import org.iotivity.service.client.RcsRemoteResourceObject;
import org.iotivity.service.server.RcsResourceObject;

import android.test.InstrumentationTestCase;
import android.util.Log;

public abstract class TestBase extends InstrumentationTestCase {
    protected static final String RESOURCEURI       = "/a/TemperatureSensor";
    protected static final String RESOURCETYPE      = "oic.r.type";
    protected static final String RESOURCEINTERFACE = "oic.if.baseline";

    protected static final String   KEY       = "key";
    protected static final RcsValue VALUE     = new RcsValue(100);
    protected static final int      RAW_VALUE = 100;

    private final Object mCond = new Object();

    protected RcsResourceObject       mServer;
    protected RcsRemoteResourceObject mClient;

    private final String TAG = TestBase.class.getName();

    private OnResourceDiscoveredListener mOnResourceDiscoveredListener = new OnResourceDiscoveredListener() {
        @Override
        public void onResourceDiscovered(
                RcsRemoteResourceObject remoteObject) {
            if (mClient != null) {
                return;
            }

            mClient = remoteObject;
            synchronized (mCond) {
                mCond.notify();
            }
        }
    };

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mServer = new RcsResourceObject.Builder(RESOURCEURI, RESOURCETYPE,
                RESOURCEINTERFACE).build();
        mServer.setAttribute(KEY, VALUE);

        WaitUntilDiscovered();

        assertNotNull(mClient);
    }

    private void WaitUntilDiscovered() throws RcsException {
        while (mClient == null) {
            try {
                RcsDiscoveryManager.DiscoveryTask discoveryTask = RcsDiscoveryManager
                        .getInstance().discoverResourceByType(RcsAddress.multicast(),
                                RESOURCETYPE, mOnResourceDiscoveredListener);

                synchronized (mCond) {
                    mCond.wait(100);
                }

                discoveryTask.cancel();
            } catch (InterruptedException e) {
                Log.e(TAG, "caught exception: " + e.toString());
            }
        }
    }

    protected void setServerAttrbutes(RcsResourceAttributes attrs)
            throws RcsException {
        RcsResourceObject.AttributesLock lock = mServer.getAttributesLock();

        try {
            lock.lock().putAll(attrs);
            lock.apply();
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        mServer.destroy();
        mClient.destroy();
    }
}
