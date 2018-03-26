/*
 * lightserver.cpp
 * Author: Einstrasse
 * Create date: 2018-3-24
 * Comment: Simple light server to manipulate mechanical main-light switch with servo motor.
 */

#include "iotivity_config.h"

#include <functional>
#ifdef HAVE_UNISTD_H
#include <unistd.h>
#endif
#ifdef HAVE_PTHREAD_H
#include <pthread.h>
#endif
#include <mutex>
#include <condition_variable>

#include "OCPlatform.h"
#include "OCApi.h"
#ifdef HAVE_WINDOWS_H
#include <windows.h>
#endif

#include "ocpayload.h"

using namespace OC;
using namespace std;
namespace PH = std::placeholders;

static const char* SVR_DB_FILE_NAME = "./oic_svr_db_server.dat";

// Set of strings for each of platform Info fields
std::string gPlatformId = "DEADBEEF-CAFE-EEEE-0000-000000000001";
std::string gManufacturerName = "OCF";
std::string gManufacturerLink = "http://eine.tistory.com";
std::string gModelNumber = "myModelNumber";
std::string gDateOfManufacture = "2018-03-24";
std::string gPlatformVersion = "myPlatformVersion";
std::string gOperatingSystemVersion = "myOS";
std::string gHardwareVersion = "myHardwareVersion";
std::string gFirmwareVersion = "1.0";
std::string gSupportLink = "http://eine.tistory.com";
std::string gSystemTime = "2018-03-24T11.01";

// Set of strings for each of device info fields
std::string  deviceName = "IoTivity Light Server";
std::string  deviceType = "oic.wk.d";
std::string  specVersion = "ocf.1.1.0";
std::vector<std::string> dataModelVersions = {"ocf.res.1.1.0", "ocf.sh.1.1.0"};
std::string  protocolIndependentID = "fa008167-3bbf-4c9d-8604-c9bcb96cb712";

// OCPlatformInfo Contains all the platform info to be stored
OCPlatformInfo platformInfo;

// Specifies secure or non-secure
// false: non-secure resource
// true: secure resource
bool isSecure = false;

class LightResource {
public:
    /// Access this property from a TB client
    std::string m_name;
    bool m_switch;
    int m_brightness;
    std::string m_lightUri;
    OCResourceHandle m_resourceHandle;
    OCRepresentation m_lightRep;
    // ObservationIds m_interestedObservers;

public:
    /// Constructor
    LightResource()
        :m_name("Main light"), m_switch(false), m_brightness(0), m_lightUri("/a/light"),
                m_resourceHandle(nullptr) {
        // Initialize representation
        m_lightRep.setUri(m_lightUri);

        m_lightRep.setValue("switch", m_switch);
        m_lightRep.setValue("brightness", m_brightness);
        m_lightRep.setValue("name", m_name);
    }

    /* Note that this does not need to be a member function: for classes you do not have
    access to, you can accomplish this with a free function: */

    /// This function internally calls registerResource API.
    OCStackResult createResource() {
        //URI of the resource
        std::string resourceURI = m_lightUri;
        //resource type name. In this case, it is light
        std::string resourceTypeName = "core.light";
        // resource interface.
        std::string resourceInterface = DEFAULT_INTERFACE;

        // OCResourceProperty is defined ocstack.h
        uint8_t resourceProperty;

        EntityHandler cb = std::bind(&LightResource::entityHandler, this,PH::_1);

        // This will internally create and register the resource.
        OCStackResult result = OCPlatform::registerResource(
                                    m_resourceHandle, resourceURI, resourceTypeName,
                                    resourceInterface, cb, OC_DISCOVERABLE | OC_OBSERVABLE);

        if (OC_STACK_OK != result)
        {
            cout << "Resource creation was failed\n";
        } else {
            cout << "Resource created successfully\n";
        }

        return result;
    }


    OCResourceHandle getHandle() {
        return m_resourceHandle;
    }

    // gets(retrieve) the updated representation.
    // Updates the representation with latest internal state before
    // sending out.
    OCRepresentation get() {
        m_lightRep.setValue("switch", m_switch);
        m_lightRep.setValue("brightness", m_brightness);

        return m_lightRep;
    }

    // Puts(update) all or some of representation.
    // Gets values from the representation and
    // updates the internal state
    void put(OCRepresentation& rep)
    {
        try {
            if (rep.getValue("switch", m_switch))
            {
                cout << "\t\t\t\t" << "switch: " << m_switch << endl;
            }
            else
            {
                cout << "\t\t\t\t" << "switch not found in the representation" << endl;
            }

            if (rep.getValue("brightness", m_brightness))
            {
                cout << "\t\t\t\t" << "brightness: " << m_brightness << endl;
            }
            else
            {
                cout << "\t\t\t\t" << "brightness not found in the representation" << endl;
            }
        }
        catch (exception& e)
        {
            cout << e.what() << endl;
        }
    }

    // Post(Create) representation.
    // Post can create new resource or simply act like put.
    // Gets values from the representation and
    // updates the internal state
    OCRepresentation post(OCRepresentation& rep)
    {
        // static int first = 1;

        // // Only the first post request make a new resource
        // if(first)
        // {
        //     first = 0;

        //     if(OC_STACK_OK == createResource())
        //     {
        //         OCRepresentation rep1;
        //         rep1.setValue("createduri", std::string("/a/light"));

        //         return rep1;
        //     }
        // }

        // Second request make it update value and return the value
        put(rep);
        return get();
    }


    

    // void addType(const std::string& type) const {
    //     OCStackResult result = OCPlatform::bindTypeToResource(m_resourceHandle, type);
    //     if (OC_STACK_OK != result)
    //     {
    //         cout << "Binding TypeName to Resource was unsuccessful\n";
    //     }
    // }

    void addInterface(const std::string& iface) const {
        OCStackResult result = OCPlatform::bindInterfaceToResource(m_resourceHandle, iface);
        if (OC_STACK_OK != result)
        {
            cout << "Binding TypeName to Resource was unsuccessful\n";
        }
    }

private:
    // This is just a sample implementation of entity handler.
    // Entity handler can be implemented in several ways by the manufacturer
    OCEntityHandlerResult entityHandler(std::shared_ptr<OCResourceRequest> request) {
        cout << "\tIn Server CPP entity handler:\n";
        OCEntityHandlerResult ehResult = OC_EH_ERROR;
        if(request) {
            // Get the request type and request flag
            std::string requestType = request->getRequestType();
            int requestFlag = request->getRequestHandlerFlag();

            if(requestFlag & RequestHandlerFlag::RequestFlag) {
                cout << "\t\trequestFlag : Request\n";
                auto pResponse = std::make_shared<OC::OCResourceResponse>();
                pResponse->setRequestHandle(request->getRequestHandle());
                pResponse->setResourceHandle(request->getResourceHandle());

                // Check for query params (if any)
                QueryParamsMap queries = request->getQueryParameters();

                if (!queries.empty()) {
                    std::cout << "\nQuery processing upto entityHandler" << std::endl;
                }
                for (auto it : queries) {
                    std::cout << "Query key: " << it.first << " value : " << it.second
                            << std:: endl;
                }

                // If the request type is GET
                if(requestType == "GET") {
                    cout << "\t\t\trequestType : GET\n";
                    pResponse->setResponseResult(OC_EH_OK);
                    pResponse->setResourceRepresentation(get());
                    if(OC_STACK_OK == OCPlatform::sendResponse(pResponse)) {
                        ehResult = OC_EH_OK;
                    }
                } else if(requestType == "PUT") {
                    cout << "\t\t\trequestType : PUT\n";
                    OCRepresentation rep = request->getResourceRepresentation();

                    // Do related operations related to PUT request
                    // Update the lightResource
                    put(rep);

                    pResponse->setResponseResult(OC_EH_OK);
                    pResponse->setResourceRepresentation(get());
                    if(OC_STACK_OK == OCPlatform::sendResponse(pResponse))
                    {
                        ehResult = OC_EH_OK;
                    }
                } else if(requestType == "POST") {
                    cout << "\t\t\trequestType : POST\n";

                    OCRepresentation rep = request->getResourceRepresentation();

                    // Do related operations related to POST request
                    OCRepresentation rep_post = post(rep);
                    pResponse->setResourceRepresentation(rep_post);

                    if(rep_post.hasAttribute("createduri"))
                    {
                        pResponse->setResponseResult(OC_EH_RESOURCE_CREATED);
                        pResponse->setNewResourceUri(rep_post.getValue<std::string>("createduri"));
                    }
                    else
                    {
                        pResponse->setResponseResult(OC_EH_OK);
                    }

                    if(OC_STACK_OK == OCPlatform::sendResponse(pResponse))
                    {
                        ehResult = OC_EH_OK;
                    }
                }
                else if(requestType == "DELETE")
                {
                    cout << "Delete request received" << endl;
                }
            }

            /*
            // Observer request handler

            if(requestFlag & RequestHandlerFlag::ObserverFlag) {
                ObservationInfo observationInfo = request->getObservationInfo();
                if(ObserveAction::ObserveRegister == observationInfo.action) {
                    m_interestedObservers.push_back(observationInfo.obsId);
                } else if(ObserveAction::ObserveUnregister == observationInfo.action)
                {
                    m_interestedObservers.erase(std::remove(
                                                                m_interestedObservers.begin(),
                                                                m_interestedObservers.end(),
                                                                observationInfo.obsId),
                                                                m_interestedObservers.end());
                }

                pthread_t threadId;

                cout << "\t\trequestFlag : Observer\n";
                gObservation = 1;
                static int startedThread = 0;

                // Observation happens on a different thread in ChangeLightRepresentation function.
                // If we have not created the thread already, we will create one here.
                if(!startedThread)
                {
                    pthread_create (&threadId, NULL, ChangeLightRepresentation, (void *)this);
                    startedThread = 1;
                }
                ehResult = OC_EH_OK;
            }
            */

        } else {
            std::cout << "Request invalid" << std::endl;
        }

        return ehResult;
    }
};

/*
// ChangeLightRepresentaion is an observation function,
// which notifies any changes to the resource to stack
// via notifyObservers
void * ChangeLightRepresentation (void *param)
{
    LightResource* lightPtr = (LightResource*) param;

    // This function continuously monitors for the changes
    while (1)
    {
        sleep (3);

        if (gObservation)
        {
            // If under observation if there are any changes to the light resource
            // we call notifyObservors
            //
            // For demostration we are changing the power value and notifying.
            lightPtr->m_power += 10;

            cout << "\nPower updated to : " << lightPtr->m_power << endl;
            cout << "Notifying observers with resource handle: " << lightPtr->getHandle() << endl;

            OCStackResult result = OC_STACK_OK;

            if(isListOfObservers)
            {
                std::shared_ptr<OCResourceResponse> resourceResponse =
                            {std::make_shared<OCResourceResponse>()};

                resourceResponse->setResourceRepresentation(lightPtr->get(), DEFAULT_INTERFACE);

                result = OCPlatform::notifyListOfObservers(  lightPtr->getHandle(),
                                                             lightPtr->m_interestedObservers,
                                                             resourceResponse);
            }
            else
            {
                result = OCPlatform::notifyAllObservers(lightPtr->getHandle());
            }

            if(OC_STACK_NO_OBSERVERS == result)
            {
                cout << "No More observers, stopping notifications" << endl;
                gObservation = 0;
            }
        }
    }

    return NULL;
}
*/

void DeletePlatformInfo()
{
    delete[] platformInfo.platformID;
    delete[] platformInfo.manufacturerName;
    delete[] platformInfo.manufacturerUrl;
    delete[] platformInfo.modelNumber;
    delete[] platformInfo.dateOfManufacture;
    delete[] platformInfo.platformVersion;
    delete[] platformInfo.operatingSystemVersion;
    delete[] platformInfo.hardwareVersion;
    delete[] platformInfo.firmwareVersion;
    delete[] platformInfo.supportUrl;
    delete[] platformInfo.systemTime;
}

void DuplicateString(char ** targetString, std::string sourceString) {
    *targetString = new char[sourceString.length() + 1];
    strncpy(*targetString, sourceString.c_str(), (sourceString.length() + 1));
}

OCStackResult SetPlatformInfo(std::string platformID, std::string manufacturerName,
        std::string manufacturerUrl, std::string modelNumber, std::string dateOfManufacture,
        std::string platformVersion, std::string operatingSystemVersion,
        std::string hardwareVersion, std::string firmwareVersion, std::string supportUrl,
        std::string systemTime) {
    DuplicateString(&platformInfo.platformID, platformID);
    DuplicateString(&platformInfo.manufacturerName, manufacturerName);
    DuplicateString(&platformInfo.manufacturerUrl, manufacturerUrl);
    DuplicateString(&platformInfo.modelNumber, modelNumber);
    DuplicateString(&platformInfo.dateOfManufacture, dateOfManufacture);
    DuplicateString(&platformInfo.platformVersion, platformVersion);
    DuplicateString(&platformInfo.operatingSystemVersion, operatingSystemVersion);
    DuplicateString(&platformInfo.hardwareVersion, hardwareVersion);
    DuplicateString(&platformInfo.firmwareVersion, firmwareVersion);
    DuplicateString(&platformInfo.supportUrl, supportUrl);
    DuplicateString(&platformInfo.systemTime, systemTime);

    return OC_STACK_OK;
}

OCStackResult SetDeviceInfo()
{
    OCStackResult result = OC_STACK_ERROR;

    OCResourceHandle handle = OCGetResourceHandleAtUri(OC_RSRVD_DEVICE_URI);
    if (handle == NULL)
    {
        cout << "Failed to find resource " << OC_RSRVD_DEVICE_URI << endl;
        return result;
    }

    result = OCBindResourceTypeToResource(handle, deviceType.c_str());
    if (result != OC_STACK_OK)
    {
        cout << "Failed to add device type" << endl;
        return result;
    }

    result = OCPlatform::setPropertyValue(PAYLOAD_TYPE_DEVICE, OC_RSRVD_DEVICE_NAME, deviceName);
    if (result != OC_STACK_OK)
    {
        cout << "Failed to set device name" << endl;
        return result;
    }

    result = OCPlatform::setPropertyValue(PAYLOAD_TYPE_DEVICE, OC_RSRVD_DATA_MODEL_VERSION,
                                          dataModelVersions);
    if (result != OC_STACK_OK)
    {
        cout << "Failed to set data model versions" << endl;
        return result;
    }

    result = OCPlatform::setPropertyValue(PAYLOAD_TYPE_DEVICE, OC_RSRVD_SPEC_VERSION, specVersion);
    if (result != OC_STACK_OK)
    {
        cout << "Failed to set spec version" << endl;
        return result;
    }

    result = OCPlatform::setPropertyValue(PAYLOAD_TYPE_DEVICE, OC_RSRVD_PROTOCOL_INDEPENDENT_ID,
                                          protocolIndependentID);
    if (result != OC_STACK_OK)
    {
        cout << "Failed to set piid" << endl;
        return result;
    }

    return OC_STACK_OK;
}

void PrintIntro() {
    std::cout << std::endl;
    std::cout << "------ IoTivity Light Server -----\n";
    std::cout << "Manipulate Light switch mechanically with servo motor\n";
    std::cout << "Supported Method: POST / PUT / GET\n";
    std::cout << "1. POST\t- Create light resource OR reinitialize light resource\n";
    std::cout << "2. PUT\t- Modify resource attribute(s)\n";
    std::cout << "3. GET\t- Fetch resouce attributes\n";
}

static FILE* client_open(const char* path, const char* mode)
{
    char const * filename = path;
    if (0 == strcmp(path, OC_SECURITY_DB_DAT_FILE_NAME))
    {
        filename = SVR_DB_FILE_NAME;
    }
    else if (0 == strcmp(path, OC_INTROSPECTION_FILE_NAME))
    {
        filename = "lightserver_introspection.dat";
    }
    return fopen(filename, mode);
}


int main() {
    PrintIntro();

    //Platform setting and start
    OCPersistentStorage ps {client_open, fread, fwrite, fclose, unlink };
    PlatformConfig cfg {
        OC::ServiceType::InProc,
        OC::ModeType::Server,
        &ps
    };
    cfg.transportType = static_cast<OCTransportAdapter>(OCTransportAdapter::OC_ADAPTER_IP | 
                                                        OCTransportAdapter::OC_ADAPTER_TCP);
    cfg.QoS = OC::QualityOfService::LowQos;
    OCPlatform::Configure(cfg);
    OC_VERIFY(OCPlatform::start() == OC_STACK_OK);
    std::cout << "Setting Platform Info finished. Server Starts....\n";


    OCStackResult result = SetPlatformInfo(gPlatformId, gManufacturerName, gManufacturerLink,
            gModelNumber, gDateOfManufacture, gPlatformVersion, gOperatingSystemVersion,
            gHardwareVersion, gFirmwareVersion, gSupportLink, gSystemTime);

    result = OCPlatform::registerPlatformInfo(platformInfo);





    if (result != OC_STACK_OK)
    {
        std::cout << "Platform Registration failed\n";
        return -1;
    } else {
        std::cout << "Platform Registration succeed\n";
    }

    result = SetDeviceInfo();

    if (result != OC_STACK_OK)
    {
        std::cout << "Device Registration failed\n";
        return -1;
    } else {
        std::cout << "Device Registration succeed\n";
    }




    try
    {
        // Create the instance of the resource class
        // (in this case instance of class 'LightResource').
        LightResource myLight;

        // Invoke createResource function of class light.
        myLight.createResource();
        std::cout << "Created resource." << std::endl;

        // myLight.addType(std::string("core.brightlight"));
        myLight.addInterface(std::string(LINK_INTERFACE));
        std::cout << "Added Interface and Type" << std::endl;

        DeletePlatformInfo();

        // A condition variable will free the mutex it is given, then do a non-
        // intensive block until 'notify' is called on it.  In this case, since we
        // don't ever call cv.notify, this should be a non-processor intensive version
        // of while(true);
        std::mutex blocker;
        std::condition_variable cv;
        std::unique_lock<std::mutex> lock(blocker);
        std::cout <<"Waiting" << std::endl;
        cv.wait(lock, []{return false;});
    }
    catch(OCException &e)
    {
        std::cout << "OCException in main : " << e.what() << endl;
    }

    OC_VERIFY(OCPlatform::stop() == OC_STACK_OK);

    return 0;
}