/* *****************************************************************
 *
 * Copyright 2015 Samsung Electronics All Rights Reserved.
 *
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * *****************************************************************/

#include "ocstack.h"
#include "experimental/ocrandom.h"
#include "srmutility.h"
#include "OCProvisioningManager.hpp"

namespace OC
{
    /**
     * Prevent multiple registrations of the input pin callback since the 
     * underlying IoTivity stack can only handle having one callback set
     * at a time.
     */
    bool g_inputPinCallbackRegistered = false;

    /**
     * Prevent multiple registrations of the display pin callback since the
     * underlying IoTivity stack can only handle having one callback set
     * at a time.
     */
    bool g_displayPinCallbackRegistered = false;

    static void callbackWrapperImpl(void* ctx, size_t nOfRes, OCProvisionResult_t *arr, bool hasError)
    {
        PMResultList_t *results = nullptr;
        ProvisionContext* context = static_cast<ProvisionContext*>(ctx);

        try
        {
            results = new PMResultList_t;
        }
        catch (std::bad_alloc& e)
        {
            OC_UNUSED(e);
            oclog() <<"Bad alloc exception";
            return;
        }

        for (size_t i = 0; i < nOfRes; i++)
        {
            results->push_back(arr[i]);
        }

        std::thread exec(context->callback, results, hasError);
        exec.detach();

        delete context;
    }

    OCStackResult OCSecure::provisionInit(const std::string& dbPath)
    {
        OCStackResult result;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCInitPM(dbPath.c_str());
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }

        return result;
    }

    OCStackResult OCSecure::provisionClose()
    {
        OCStackResult result;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCClosePM();
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }

        return result;
    }

    OCStackResult OCSecure::discoverUnownedDevices(unsigned short timeout,
            DeviceList_t &list)
    {
        OCStackResult result;
        OCProvisionDev_t *pDevList = nullptr, *pCurDev = nullptr, *tmp = nullptr;
        auto csdkLock = OCPlatform_impl::Instance().csdkLock();
        auto cLock = csdkLock.lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCDiscoverUnownedDevices(timeout, &pDevList);
            if (result == OC_STACK_OK)
            {
                // Create DeviceList of OCSecureResource's
                pCurDev = pDevList;
                while (pCurDev)
                {
                    tmp = pCurDev;
                    list.push_back(std::shared_ptr<OCSecureResource>(
                                new OCSecureResource(csdkLock, pCurDev)));
                    pCurDev = pCurDev->next;
                    tmp->next = nullptr;
                }
            }
            else
            {
                oclog() <<"Unowned device discovery failed!";
            }
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }

        return result;
    }

    OCStackResult OCSecure::discoverOwnedDevices(unsigned short timeout,
            DeviceList_t &list)
    {
        OCStackResult result;
        OCProvisionDev_t *pDevList = nullptr, *pCurDev = nullptr, *tmp = nullptr;
        auto csdkLock = OCPlatform_impl::Instance().csdkLock();
        auto cLock = csdkLock.lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCDiscoverOwnedDevices(timeout, &pDevList);
            if (result == OC_STACK_OK)
            {
                pCurDev = pDevList;
                while (pCurDev)
                {
                    tmp = pCurDev;
                    list.push_back(std::shared_ptr<OCSecureResource>(
                                new OCSecureResource(csdkLock, pCurDev)));
                    pCurDev = pCurDev->next;
                    tmp->next = nullptr;
                }
            }
            else
            {
                oclog() <<"Owned device discovery failed!";
            }
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }

        return result;
    }

    OCStackResult OCSecure::discoverSingleDevice(unsigned short timeout,
            const OicUuid_t* deviceID,
            std::shared_ptr<OCSecureResource> &foundDevice)
    {
        OCStackResult result;
        OCProvisionDev_t *pDev = nullptr;
        auto csdkLock = OCPlatform_impl::Instance().csdkLock();
        auto cLock = csdkLock.lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCDiscoverSingleDevice(timeout, deviceID, &pDev);
            if (result == OC_STACK_OK)
            {
                if (pDev)
                {
                    foundDevice.reset(new OCSecureResource(csdkLock, pDev));
                }
                else
                {
                    oclog() <<"Not found Secure resource!";
                    foundDevice.reset();
                }
            }
            else
            {
                oclog() <<"Secure resource discovery failed!";
            }
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }

        return result;
    }

    OCStackResult OCSecure::setOwnerTransferCallbackData(OicSecOxm_t oxm,
            OTMCallbackData_t* callbackData, InputPinCallback inputPin)
    {
        if (NULL == callbackData || oxm >= OIC_OXM_COUNT)
        {
            oclog() <<"Invalid callbackData or OXM type";
            return OC_STACK_INVALID_PARAM;
        }

        if ((OIC_RANDOM_DEVICE_PIN == oxm) && !inputPin)
        {
            oclog() <<"for OXM type DEVICE_PIN, inputPin callback can't be null";
            return OC_STACK_INVALID_PARAM;
        }

        OCStackResult result;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCSetOwnerTransferCallbackData(oxm, callbackData);
            if (result == OC_STACK_OK && (OIC_RANDOM_DEVICE_PIN == oxm))
            {
                SetInputPinCB(inputPin);
            }
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OCSecure::discoverSingleDeviceInUnicast(unsigned short timeout,
            const OicUuid_t* deviceID,
            const std::string& hostAddress,
            OCConnectivityType connType,
            std::shared_ptr<OCSecureResource> &foundDevice)
    {
        OCStackResult result = OC_STACK_ERROR;
        OCProvisionDev_t *pDev = nullptr;
        auto csdkLock = OCPlatform_impl::Instance().csdkLock();
        auto cLock = csdkLock.lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCDiscoverSingleDeviceInUnicast(timeout, deviceID, hostAddress.c_str(),
                            connType, &pDev);

            if (result == OC_STACK_OK)
            {
                if (pDev)
                {
                    foundDevice.reset(new OCSecureResource(csdkLock, pDev));
                }
                else
                {
                    oclog() <<"Not found Secure resource!";
                    foundDevice.reset();
                }
            }
            else
            {
                oclog() <<"Secure resource discovery failed!";
            }
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }

        return result;
    }

#ifdef MULTIPLE_OWNER
    OCStackResult OCSecure::discoverMultipleOwnerEnabledDevices(unsigned short timeout,
            DeviceList_t &list)
    {
        OCStackResult result;
        OCProvisionDev_t *pDevList = nullptr, *pCurDev = nullptr, *tmp = nullptr;
        auto csdkLock = OCPlatform_impl::Instance().csdkLock();
        auto cLock = csdkLock.lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCDiscoverMultipleOwnerEnabledDevices(timeout, &pDevList);
            if (result == OC_STACK_OK)
            {
                pCurDev = pDevList;
                while (pCurDev)
                {
                    tmp = pCurDev;
                    list.push_back(std::shared_ptr<OCSecureResource>(
                                new OCSecureResource(csdkLock, pCurDev)));
                    pCurDev = pCurDev->next;
                    tmp->next = nullptr;
                }
            }
            else
            {
                oclog() <<"MultipleOwner Enabled device discovery failed!";
            }
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }

        return result;
    }

    OCStackResult OCSecure::discoverMultipleOwnedDevices(unsigned short timeout,
            DeviceList_t &list)
    {
        OCStackResult result;
        OCProvisionDev_t *pDevList = nullptr, *pCurDev = nullptr, *tmp = nullptr;
        auto csdkLock = OCPlatform_impl::Instance().csdkLock();
        auto cLock = csdkLock.lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCDiscoverMultipleOwnedDevices(timeout, &pDevList);
            if (result == OC_STACK_OK)
            {
                pCurDev = pDevList;
                while (pCurDev)
                {
                    tmp = pCurDev;
                    list.push_back(std::shared_ptr<OCSecureResource>(
                                new OCSecureResource(csdkLock, pCurDev)));
                    pCurDev = pCurDev->next;
                    tmp->next = nullptr;
                }
            }
            else
            {
                oclog() <<"Multiple Owned device discovery failed!";
            }
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }

        return result;
    }

    OCStackResult OCSecure::discoverMultipleOwnerEnabledDevice(unsigned short timeout, const OicUuid_t* deviceID, std::shared_ptr<OCSecureResource> &foundDevice)
    {
        OCStackResult result;
        OCProvisionDev_t *pDev = nullptr;
        auto csdkLock = OCPlatform_impl::Instance().csdkLock();
        auto cLock = csdkLock.lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCDiscoverMultipleOwnerEnabledSingleDevice(timeout, deviceID, &pDev);
            if (result == OC_STACK_OK)
            {
                if (pDev)
                {
                    foundDevice.reset(new OCSecureResource(csdkLock, pDev));
                }
                else
                {
                    oclog() << "Not found Secure resource!";
                    foundDevice.reset();
                }
            }
            else
            {
                oclog() << "Secure resource discovery failed!";
            }
        }
        else
        {
            oclog() << "Mutex not found";
            result = OC_STACK_ERROR;
        }

        return result;
    }

#endif
    OCStackResult OCSecure::setInputPinCallback(InputPinCallback inputPin)
    {
        if (!inputPin)
        {
            oclog() << "inputPin can't be null";
            return OC_STACK_INVALID_PARAM;
        }
        else if (g_inputPinCallbackRegistered)
        {
            oclog() << "Callback for pin input already registered.";
            return OC_STACK_DUPLICATE_REQUEST;
        }

        OCStackResult result = OC_STACK_OK;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            SetInputPinCB(inputPin);
            g_inputPinCallbackRegistered = true;
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }

        return result;
    }

    OCStackResult OCSecure::unsetInputPinCallback()
    {
        OCStackResult result = OC_STACK_OK;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            UnsetInputPinCB();
            g_inputPinCallbackRegistered = false;
        }
        else
        {
            oclog() << "Mutex not found";
            result = OC_STACK_ERROR;
        }

        return result;
    }

    static void OC_CALL inputPinCallbackWrapper(OicUuid_t deviceId, char* pinBuffer, size_t pinBufferSize, void* context)
    {
        (static_cast<InputPinContext*>(context))->callback(deviceId, pinBuffer, pinBufferSize);
    }

    OCStackResult OCSecure::registerInputPinCallback(InputPinCB inputPinCB, InputPinCallbackHandle* inputPinCallbackHandle)
    {
        OCStackResult result = OC_STACK_ERROR;

        if (!inputPinCB)
        {
            oclog() << "Failed to register callback for pin input.";
            return OC_STACK_INVALID_CALLBACK;
        }
        else if (!inputPinCallbackHandle)
        {
            return OC_STACK_INVALID_PARAM;
        }
        else if (g_inputPinCallbackRegistered)
        {
            oclog() << "Callback for pin input already registered.";
            return OC_STACK_DUPLICATE_REQUEST;
        }

        *inputPinCallbackHandle = nullptr;

        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            InputPinContext* inputPinContext = new InputPinContext(inputPinCB);
            if (nullptr != inputPinContext)
            {
                std::lock_guard<std::recursive_mutex> lock(*cLock);
                result = SetInputPinWithContextCB(&inputPinCallbackWrapper, static_cast<void*>(inputPinContext));
                if (OC_STACK_OK == result)
                {
                    g_inputPinCallbackRegistered = true;
                    *inputPinCallbackHandle = inputPinContext;
                }
                else
                {
                    if (nullptr != inputPinContext)
                    {
                        delete inputPinContext;
                    }
                }
            }
            else
            {
                result = OC_STACK_NO_MEMORY;
            }
        }
        else
        {
            oclog() << "Mutex not found";
        }

        return result;
    }

    OCStackResult OCSecure::deregisterInputPinCallback(InputPinCallbackHandle inputPinCallbackHandle)
    {
        OCStackResult result = OC_STACK_OK;

        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            UnsetInputPinWithContextCB();
            if (nullptr != inputPinCallbackHandle)
            {
                delete inputPinCallbackHandle;
            }
            g_inputPinCallbackRegistered = false;
        }
        else
        {
            oclog() << "Mutex not found";
            result = OC_STACK_ERROR;
        }

        return result;
    }

    OCStackResult OCSecure::setRandomPinPolicy(size_t pinSize, OicSecPinType_t pinType)
    {
        OCStackResult result;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = SetRandomPinPolicy(pinSize, pinType);
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OCSecure::getDevInfoFromNetwork(unsigned short timeout,
            DeviceList_t &ownedDevList,
            DeviceList_t &unownedDevList)
    {
        OCStackResult result = OC_STACK_OK;
        OCProvisionDev_t *owned = nullptr, *unowned = nullptr, *tmp = nullptr, *dev = nullptr;
        auto csdkLock = OCPlatform_impl::Instance().csdkLock();
        auto cLock = csdkLock.lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);

            result = OCGetDevInfoFromNetwork(timeout, &owned, &unowned);

            if (result == OC_STACK_OK)
            {
                dev = owned;
                while (dev)
                {
                    tmp = dev;
                    ownedDevList.push_back(std::shared_ptr<OCSecureResource>(
                                new OCSecureResource(csdkLock, dev)));
                    dev = dev->next;
                    tmp->next = nullptr;
                }

                dev = unowned;
                while (dev)
                {
                    tmp = dev;
                    unownedDevList.push_back(std::shared_ptr<OCSecureResource>(
                                new OCSecureResource(csdkLock,  dev)));
                    dev = dev->next;
                    tmp->next = nullptr;
                }
            }
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }

        return result;
    }

    OCStackResult OCSecure::setDisplayPinCB(GeneratePinCallback displayPin)
    {
        if (!displayPin)
        {
            oclog() <<"displayPin can't be null";
            return OC_STACK_INVALID_PARAM;
        }
        else if (g_displayPinCallbackRegistered)
        {
            oclog() << "Callback for pin display already registered.";
            return OC_STACK_DUPLICATE_REQUEST;
        }

        OCStackResult result = OC_STACK_OK;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            SetGeneratePinCB(displayPin);
            g_displayPinCallbackRegistered = true;
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }

        return result;
    }

    OCStackResult OCSecure::unsetDisplayPinCB()
    {
        OCStackResult result = OC_STACK_OK;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            UnsetGeneratePinCB();
            g_displayPinCallbackRegistered = false;
        }
        else
        {
            oclog() << "Mutex not found";
            result = OC_STACK_ERROR;
        }

        return result;
    }

    static void OC_CALL displayPinCallbackWrapper(char* pinData, size_t pinDataSize, void* context)
    {
        (static_cast<DisplayPinContext*>(context))->callback(pinData, pinDataSize);
    }

    OCStackResult OCSecure::registerDisplayPinCallback(DisplayPinCB displayPinCB, DisplayPinCallbackHandle* displayPinCallbackHandle)
    {
        OCStackResult result = OC_STACK_ERROR;

        if (!displayPinCB)
        {
            oclog() << "Failed to register callback for pin display.";
            return OC_STACK_INVALID_CALLBACK;
        }
        else if (!displayPinCallbackHandle)
        {
            return OC_STACK_INVALID_PARAM;
        }
        else if (g_displayPinCallbackRegistered)
        {
            oclog() << "Callback for pin display already registered.";
            return OC_STACK_DUPLICATE_REQUEST;
        }

        *displayPinCallbackHandle = nullptr;

        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            DisplayPinContext* displayPinContext = new DisplayPinContext(displayPinCB);
            if (nullptr != displayPinContext)
            {
                std::lock_guard<std::recursive_mutex> lock(*cLock);
                result = SetDisplayPinWithContextCB(&displayPinCallbackWrapper, static_cast<void*>(displayPinContext));
                if (OC_STACK_OK == result)
                {
                    *displayPinCallbackHandle = displayPinContext;
                    g_displayPinCallbackRegistered = true;
                }
                else
                {
                    if (nullptr != displayPinContext)
                    {
                        delete displayPinContext;
                    }
                }
            }
            else
            {
                result = OC_STACK_NO_MEMORY;
            }
        }
        else
        {
            oclog() << "Mutex not found";
        }

        return result;
    }

    OCStackResult OCSecure::deregisterDisplayPinCallback(DisplayPinCallbackHandle displayPinCallbackHandle)
    {
        OCStackResult result = OC_STACK_OK;

        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();
        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            UnsetDisplayPinWithContextCB();
            if (nullptr != displayPinCallbackHandle)
            {
                delete displayPinCallbackHandle;
            }
            g_displayPinCallbackRegistered = false;
        }
        else
        {
            oclog() << "Mutex not found";
            result = OC_STACK_ERROR;
        }

        return result;
    }

    OCStackResult OCSecure::removeDeviceWithUuid(unsigned short waitTimeForOwnedDeviceDiscovery,
            std::string uuid,
            ResultCallBack resultCallback)
    {
        if (!resultCallback)
        {
            oclog() << "Result calback can't be null";
            return OC_STACK_INVALID_CALLBACK;
        }

        OCStackResult result;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            ProvisionContext* context = new ProvisionContext(resultCallback);

            std::lock_guard<std::recursive_mutex> lock(*cLock);

            OicUuid_t targetDev;
            result = ConvertStrToUuid(uuid.c_str(), &targetDev);
            if(OC_STACK_OK == result)
            {
                result = OCRemoveDeviceWithUuid(static_cast<void*>(context), waitTimeForOwnedDeviceDiscovery,
                        &targetDev, &OCSecure::callbackWrapper);
            }
            else
            {
                oclog() <<"Can not convert struuid to uuid";
            }
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OCSecure::saveACL(const OicSecAcl_t* acl)
    {
        if (!acl)
        {
            oclog() <<"ACL can't be null";
            return OC_STACK_INVALID_PARAM;
        }

        OCStackResult result;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCSaveACL(const_cast<OicSecAcl_t*>(acl));
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OC_CALL OCSecure::displayNumCallbackWrapper(void* ctx,
            uint8_t verifNum[MUTUAL_VERIF_NUM_LEN])
    {
        uint8_t *number = NULL;

        DisplayNumContext* context = static_cast<DisplayNumContext*>(ctx);
        if (!context)
        {
            oclog() << "Invalid context";
            return OC_STACK_INVALID_PARAM;
        }

        if (NULL != verifNum) {
            number = new uint8_t[MUTUAL_VERIF_NUM_LEN];
            memcpy(number, verifNum, MUTUAL_VERIF_NUM_LEN);
        }

        OCStackResult res = context->callback(number);
        delete context;
        return res;
    }

    OCStackResult OCSecure::registerDisplayNumCallback(void *ctx)
    {
        if(!ctx)
        {
            oclog() << "Failed set context";
            return OC_STACK_INVALID_CALLBACK;
        }

        OCStackResult result = OCSecure::deregisterDisplayNumCallback();
        if (OC_STACK_OK != result)
        {
            oclog() << "Failed to de-register callback for display."<<std::endl;
            return result;
        }

        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();
        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            SetDisplayNumCB(ctx, &OCSecure::displayNumCallbackWrapper);
            result = OC_STACK_OK;
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OCSecure::deregisterDisplayNumCallback()
    {
        OCStackResult result;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            DisplayNumContext* context = static_cast<DisplayNumContext*>(UnsetDisplayNumCB());
            if (context)
            {
                oclog() << "Delete registered display num context"<<std::endl;
                delete context;
            }
            result = OC_STACK_OK;
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OC_CALL OCSecure::confirmUserCallbackWrapper(void* ctx)
    {
        UserConfirmNumContext* context = static_cast<UserConfirmNumContext*>(ctx);
        if (!context)
        {
            oclog() << "Invalid context";
            return OC_STACK_INVALID_PARAM;
        }

        OCStackResult res = context->callback();
        delete context;
        return res;
    }

    OCStackResult OCSecure::registerUserConfirmCallback(void* ctx)
    {
        if(!ctx)
        {
            oclog() << "Failed to set context";
            return OC_STACK_INVALID_CALLBACK;
        }

        OCStackResult result = OCSecure::deregisterUserConfirmCallback();
        if (OC_STACK_OK != result)
        {
            oclog() << "Failed to de-register callback for comfirm."<<std::endl;
            return result;
        }

        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();
        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            SetUserConfirmCB(ctx, &OCSecure::confirmUserCallbackWrapper);
            result = OC_STACK_OK;
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OCSecure::deregisterUserConfirmCallback()
    {
        OCStackResult result;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            UserConfirmNumContext* context = static_cast<UserConfirmNumContext*>(UnsetUserConfirmCB());
            if (context)
            {
                oclog() << "Delete registered user confirm context"<<std::endl;
                delete context;
            }
            result = OC_STACK_OK;
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OCSecure::setVerifyOptionMask(VerifyOptionBitmask_t optionMask)
    {
        OCStackResult result;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();
        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            SetVerifyOption(optionMask);
            result = OC_STACK_OK;
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OCSecure::configSelfOwnership()
    {
        OCStackResult result;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCConfigSelfOwnership();
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OCSecure::pdmCleanupForTimeout()
    {
        OCStackResult result;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            result = OCPDMCleanupForTimeout();
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }

        return result;
    }

#if defined(__WITH_DTLS__) || defined(__WITH_TLS__)
    OCStackResult OCSecure::saveTrustCertChain(uint8_t *trustCertChain, size_t chainSize,
                                        OicEncodingType_t encodingType, uint16_t *credId)
    {
        if (!trustCertChain)
        {
            oclog() <<"trustCertChain can't be null";
            return OC_STACK_INVALID_PARAM;
        }
        if (!credId)
        {
            oclog() <<"cred ID can not be null";
            return OC_STACK_INVALID_PARAM;
        }

        OCStackResult result;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCSaveTrustCertChain(trustCertChain, chainSize, encodingType, credId );
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OCSecure::readTrustCertChain(uint16_t credId, uint8_t **trustCertChain,
            size_t *chainSize)
    {
        OCStackResult result;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCReadTrustCertChain(credId, trustCertChain, chainSize);
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    void OCSecure::certCallbackWrapper(void* ctx, uint16_t credId, uint8_t *trustCertChain,
            size_t chainSize)
    {
        TrustCertChainContext* context = static_cast<TrustCertChainContext*>(ctx);
        uint8_t *certChain = new uint8_t[chainSize];
        memcpy(certChain, trustCertChain, chainSize);
        std::thread exec(context->callback, credId, certChain, chainSize);
        exec.detach();
        delete context;
    }

    OCStackResult OCSecure::registerTrustCertChangeNotifier(CertChainCallBack callback)
    {
        if (!callback)
        {
            oclog() <<"callback can not be null";
            return OC_STACK_INVALID_CALLBACK;
        }

        OCStackResult result;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            TrustCertChainContext* context = new TrustCertChainContext(callback);
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCRegisterTrustCertChainNotifier(static_cast<void*>(context),
                    &OCSecure::certCallbackWrapper);
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }


    OCStackResult OCSecure::removeTrustCertChangeNotifier()
    {
        OCStackResult result;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            OCRemoveTrustCertChainNotifier();
            result = OC_STACK_OK;
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OCSecure::setDeviceIdSeed(const uint8_t* seed, size_t seedSize)
    {
        if (!seed)
        {
            oclog() <<"seed can not be null";
            return OC_STACK_INVALID_PARAM;
        }

        OCStackResult result;
        auto cLock = OCPlatform_impl::Instance().csdkLock().lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = SetDeviceIdSeed(seed, seedSize);
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }
#endif // __WITH_DTLS__ || __WITH_TLS__

    void OCSecure::callbackWrapper(void* ctx, size_t nOfRes, OCProvisionResult_t *arr, bool hasError)
    {
        callbackWrapperImpl(ctx, nOfRes, arr, hasError);
    }

    void OCSecureResource::callbackWrapper(void* ctx, size_t nOfRes, OCProvisionResult_t *arr, bool hasError)
    {
        callbackWrapperImpl(ctx, nOfRes, arr, hasError);
    }

    OCSecureResource::OCSecureResource(): m_csdkLock(std::weak_ptr<std::recursive_mutex>()),
                                        devPtr(nullptr)
    {
    }

    OCSecureResource::OCSecureResource(std::weak_ptr<std::recursive_mutex> csdkLock,
            OCProvisionDev_t *dPtr)
        :m_csdkLock(csdkLock), devPtr(dPtr)
    {
    }

    OCSecureResource::~OCSecureResource()
    {
        if (devPtr)
        {
            OCDeleteDiscoveredDevices(devPtr);
        }
    }

    OCStackResult OCSecureResource::doOwnershipTransfer(ResultCallBack resultCallback)
    {
        if (!resultCallback)
        {
            oclog() <<"Result callback can't be null";
            return OC_STACK_INVALID_CALLBACK;
        }

        OCStackResult result;
        auto cLock = m_csdkLock.lock();

        if (cLock)
        {
            ProvisionContext* context = new ProvisionContext(resultCallback);

            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCDoOwnershipTransfer(static_cast<void*>(context),
                    devPtr, &OCSecureResource::callbackWrapper);
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

#ifdef MULTIPLE_OWNER
    OCStackResult OCSecureResource::doMultipleOwnershipTransfer(ResultCallBack resultCallback)
    {
        if (!resultCallback)
        {
            oclog() <<"Result callback can't be null";
            return OC_STACK_INVALID_CALLBACK;
        }

        OCStackResult result;
        auto cLock = m_csdkLock.lock();

        if (cLock)
        {
            ProvisionContext* context = new ProvisionContext(resultCallback);

            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCDoMultipleOwnershipTransfer(static_cast<void*>(context),
                    devPtr, &OCSecureResource::callbackWrapper);
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OCSecureResource::isSubownerOfDevice(bool* subowner)
    {
        if (!subowner)
        {
            oclog() << "Subowner cannot be null";
            return OC_STACK_INVALID_CALLBACK;
        }

        OCStackResult result;
        auto cLock = m_csdkLock.lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCIsSubownerOfDevice(devPtr, subowner);
        }
        else
        {
            oclog() << "Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

#endif
    OCStackResult OCSecureResource::provisionACL( const OicSecAcl_t* acl,
            ResultCallBack resultCallback)
    {
        if (!acl)
        {
            oclog() <<"ACL can't be null";
            return OC_STACK_INVALID_PARAM;
        }
        if (!resultCallback)
        {
            oclog() <<"result callback can not be null";
            return OC_STACK_INVALID_CALLBACK;
        }

        OCStackResult result;
        auto cLock = m_csdkLock.lock();

        if (cLock)
        {
            ProvisionContext* context = new ProvisionContext(resultCallback);

            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCProvisionACL(static_cast<void*>(context),
                    devPtr, const_cast<OicSecAcl_t*>(acl),
                    &OCSecureResource::callbackWrapper);
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OCSecureResource::provisionCredentials(const Credential &cred,
            const OCSecureResource &device2, ResultCallBack resultCallback)
    {
        if (!resultCallback)
        {
            oclog() << "Result calback can't be null";
            return OC_STACK_INVALID_CALLBACK;
        }

        OCStackResult result;
        auto cLock = m_csdkLock.lock();

        if (cLock)
        {
            ProvisionContext* context = new ProvisionContext(resultCallback);

            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCProvisionCredentials(static_cast<void*>(context),
                    cred.getCredentialType(),
                    cred.getCredentialKeySize(),
                    devPtr, device2.getDevPtr(),
                    &OCSecureResource::callbackWrapper);
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OCSecureResource::provisionPairwiseDevices(const Credential &cred,
            const OicSecAcl_t* acl1, const OCSecureResource &device2, const OicSecAcl_t* acl2,
            ResultCallBack resultCallback)
    {
        if (!resultCallback)
        {
            oclog() << "Result callback can not be null";
            return OC_STACK_INVALID_CALLBACK;
        }

        OCStackResult result;
        auto cLock = m_csdkLock.lock();

        if (cLock)
        {
            ProvisionContext* context = new ProvisionContext(resultCallback);

            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCProvisionPairwiseDevices(static_cast<void*>(context),
                    cred.getCredentialType(),
                    cred.getCredentialKeySize(),
                    devPtr, const_cast<OicSecAcl_t*>(acl1),
                    device2.getDevPtr(), const_cast<OicSecAcl_t*>(acl2),
                    &OCSecureResource::callbackWrapper);
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OCSecureResource::unlinkDevices(const OCSecureResource &device2,
            ResultCallBack resultCallback)
    {
        if (!resultCallback)
        {
            oclog() << "Result calback can't be null";
            return OC_STACK_INVALID_CALLBACK;
        }

        OCStackResult result;
        auto cLock = m_csdkLock.lock();

        if (cLock)
        {
            ProvisionContext* context = new ProvisionContext(resultCallback);

            std::lock_guard<std::recursive_mutex> lock(*cLock);

            result = OCUnlinkDevices(static_cast<void*>(context),
                    devPtr, device2.getDevPtr(), &OCSecureResource::callbackWrapper);
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OCSecureResource::removeDevice(unsigned short waitTimeForOwnedDeviceDiscovery,
            ResultCallBack resultCallback)
    {
        if (!resultCallback)
        {
            oclog() << "Result calback can't be null";
            return OC_STACK_INVALID_CALLBACK;
        }

        OCStackResult result;
        auto cLock = m_csdkLock.lock();

        if (cLock)
        {
            ProvisionContext* context = new ProvisionContext(resultCallback);

            std::lock_guard<std::recursive_mutex> lock(*cLock);

            result = OCRemoveDevice(static_cast<void*>(context), waitTimeForOwnedDeviceDiscovery,
                    devPtr, &OCSecureResource::callbackWrapper);
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OCSecureResource::getLinkedDevices(UuidList_t &uuidList)
    {
        OCStackResult result;
        auto devUuid = devPtr->doxm->deviceID;
        auto cLock = m_csdkLock.lock();

        if (cLock)
        {
            size_t numOfDevices;
            std::lock_guard<std::recursive_mutex> lock(*cLock);

            OCUuidList_t* linkedDevs = nullptr, *tmp = nullptr;
            result = OCGetLinkedStatus(&devUuid, &linkedDevs, &numOfDevices);
            if (result == OC_STACK_OK)
            {
                for (tmp = linkedDevs; tmp; tmp = tmp->next)
                {
                    uuidList.push_back(tmp->dev);
                }
                OCDeleteUuidList(linkedDevs);
            }
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

#if defined(__WITH_DTLS__) || defined(__WITH_TLS__)
    OCStackResult OCSecureResource::provisionTrustCertChain(OicSecCredType_t type, uint16_t credId,
                    ResultCallBack resultCallback)
    {
        if (SIGNED_ASYMMETRIC_KEY != type)
        {
            oclog() <<"Invalid key type";
            return OC_STACK_INVALID_PARAM;
        }
        if (!resultCallback)
        {
            oclog() <<"result callback can not be null";
            return OC_STACK_INVALID_CALLBACK;
        }

        OCStackResult result;
        auto cLock = m_csdkLock.lock();

        if (cLock)
        {
            ProvisionContext* context = new ProvisionContext(resultCallback);

            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCProvisionTrustCertChain(static_cast<void*>(context),
                    type, credId, devPtr,
                    &OCSecureResource::callbackWrapper);
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }
#endif // __WITH_DTLS__ or __WITH_TLS__

    std::string OCSecureResource::getDeviceID()
    {
        std::ostringstream deviceId("");
        char devID[UUID_STRING_SIZE];

        validateSecureResource();

        if (OCConvertUuidToString(devPtr->doxm->deviceID.id, devID))
        {
            deviceId << devID;
        }
        else
        {
            oclog() <<"Can not convert uuid to struuid";
        }

        return deviceId.str();
    }

    OCProvisionDev_t* OCSecureResource::getDevPtr() const
    {
        return devPtr;
    }

    std::string OCSecureResource::getDevAddr()
    {
        validateSecureResource();
        std::string ipAddr(devPtr->endpoint.addr);
        return ipAddr;
    }

    int OCSecureResource::getDeviceStatus()
    {
        validateSecureResource();
        return (int)devPtr->devStatus;
    }

    bool OCSecureResource::getOwnedStatus()
    {
        validateSecureResource();
        return devPtr->doxm->owned;
    }

    OicSecOxm_t OCSecureResource::getSelectedOwnershipTransferMethod()
    {
        validateSecureResource();
        return devPtr->doxm->oxmSel;
    }

    void OCSecureResource::validateSecureResource()
    {
        if (!devPtr)
        {
            throw OCException("Incomplete secure resource", OC_STACK_RESOURCE_ERROR);
        }
    }

    OCStackResult OCSecureResource::getOTMethod(OicSecOxm_t* oxm)
    {
        if(!oxm)
        {
            oclog() << "Null param";
            return OC_STACK_INVALID_PARAM;
        }

        OCStackResult result = OC_STACK_ERROR;
        auto cLock = m_csdkLock.lock();
        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            if(devPtr && devPtr->doxm)
            {
                result = OCSelectOwnershipTransferMethod(devPtr->doxm->oxm, devPtr->doxm->oxmLen,
                                                  oxm, SUPER_OWNER);
            }
        }
        else
        {
            oclog() <<"Mutex not found";
        }
        return result;
    }


#ifdef MULTIPLE_OWNER
    OCStackResult OCSecureResource::getMOTMethod( OicSecOxm_t* oxm)
    {
        if (!oxm)
        {
            oclog() << "Null param";
            return OC_STACK_INVALID_PARAM;
        }

        OCStackResult result = OC_STACK_ERROR;
        auto cLock = m_csdkLock.lock();
        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            if (devPtr && devPtr->doxm)
            {
                result = OCSelectOwnershipTransferMethod(devPtr->doxm->oxm, devPtr->doxm->oxmLen,
                                                  oxm, SUB_OWNER);
            }
        }
        else
        {
            oclog() <<"Mutex not found";
        }
        return result;
    }

    bool OCSecureResource::isMOTSupported()
    {
        if (devPtr && devPtr->doxm)
        {
            return (devPtr->doxm->mom ? true : false);
        }
        return false;
    }

    bool OCSecureResource::isMOTEnabled()
    {
        if (devPtr && devPtr->doxm && devPtr->doxm->mom)
        {
            if (OIC_MULTIPLE_OWNER_DISABLE != devPtr->doxm->mom->mode)
            {
                return true;
            }
        }
        return false;
    }

    OCStackResult OCSecureResource::selectMOTMethod( const OicSecOxm_t oxmSelVal,
            ResultCallBack resultCallback)
    {
        if (!resultCallback)
        {
            oclog() <<"result callback can not be null";
            return OC_STACK_INVALID_CALLBACK;
        }

        OCStackResult result;
        auto cLock = m_csdkLock.lock();

        if (cLock)
        {
            ProvisionContext* context = new ProvisionContext(resultCallback);

            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCSelectMOTMethod(static_cast<void*>(context),
                    devPtr, oxmSelVal,
                    &OCSecureResource::callbackWrapper);
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OCSecureResource::changeMOTMode( const OicSecMomType_t momType,
            ResultCallBack resultCallback)
    {
        if (!resultCallback)
        {
            oclog() <<"result callback can not be null";
            return OC_STACK_INVALID_CALLBACK;
        }

        OCStackResult result;
        auto cLock = m_csdkLock.lock();

        if (cLock)
        {
            ProvisionContext* context = new ProvisionContext(resultCallback);

            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCChangeMOTMode(static_cast<void*>(context),
                    devPtr, momType,
                    &OCSecureResource::callbackWrapper);
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }


    OCStackResult OCSecureResource::addPreconfigPIN(const char* preconfPIN,
            size_t preconfPINLength)
    {
        if (!preconfPIN)
        {
            oclog() <<"pre config pin can not be null";
            return OC_STACK_INVALID_PARAM;
        }
        if (preconfPINLength <= 0)
        {
            oclog() <<"pre config pin length can not be zero or less";
            return OC_STACK_INVALID_PARAM;
        }
        OCStackResult result;
        auto cLock = m_csdkLock.lock();

        if (cLock)
        {
            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCAddPreconfigPin(devPtr, preconfPIN,
                    preconfPINLength);
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }

    OCStackResult OCSecureResource::provisionPreconfPin(const char * preconfPin,
            size_t preconfPinLength, ResultCallBack resultCallback)
    {
        if (!resultCallback)
        {
            oclog() <<"result callback can not be null";
            return OC_STACK_INVALID_CALLBACK;
        }
        if (!preconfPin)
        {
            oclog() <<"pre config pin can not be null";
            return OC_STACK_INVALID_PARAM;
        }
        if (preconfPinLength <= 0)
        {
            oclog() <<"pre config pin length can not be zero or less";
            return OC_STACK_INVALID_PARAM;
        }

        OCStackResult result;
        auto cLock = m_csdkLock.lock();

        if (cLock)
        {
            ProvisionContext* context = new ProvisionContext(resultCallback);

            std::lock_guard<std::recursive_mutex> lock(*cLock);
            result = OCProvisionPreconfigPin(static_cast<void*>(context),
                    devPtr, preconfPin, preconfPinLength,
                    &OCSecureResource::callbackWrapper);
        }
        else
        {
            oclog() <<"Mutex not found";
            result = OC_STACK_ERROR;
        }
        return result;
    }
#endif // MULTIPLE_OWNER
}
