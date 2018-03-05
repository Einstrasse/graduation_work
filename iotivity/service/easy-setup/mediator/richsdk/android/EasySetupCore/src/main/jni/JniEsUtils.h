/******************************************************************
 *
 * Copyright 2016 Samsung Electronics All Rights Reserved.
 *
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************/

/**
  * @file   JniESUtils.h
  *
  * @brief  This file contains the utility functions for JNI layer
  */

#ifndef __JNI_ES_UTILS_H_
#define __JNI_ES_UTILS_H_

#include <jni.h>
#include <string>

#include "ESRichCommon.h"
#include "octypes.h"

#include "JniJvm.h"

using namespace OIC::Service;

/**
 * @brief Throw the Exception to the java layer
 */
void throwESException(JNIEnv *env, std::string reason);

#endif //__JNI_ES_UTILS_H_
