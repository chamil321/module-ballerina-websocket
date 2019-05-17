/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.ballerinalang.net.http.actions.websocketconnector;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.CallableUnitCallback;
import org.ballerinalang.jvm.Strand;
import org.ballerinalang.jvm.values.ObjectValue;
import org.ballerinalang.jvm.values.connector.TempCallableUnitCallback;
import org.ballerinalang.model.NativeCallableUnit;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BBoolean;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinalang.net.http.HttpUtil;
import org.ballerinalang.net.http.WebSocketConstants;
import org.ballerinalang.net.http.WebSocketOpenConnectionInfo;
import org.ballerinalang.net.http.WebSocketUtil;

/**
 * {@code Get} is the GET action implementation of the HTTP Connector.
 */
@BallerinaFunction(
        orgName = "ballerina", packageName = "http",
        functionName = "ready",
        receiver = @Receiver(type = TypeKind.OBJECT, structType = WebSocketConstants.WEBSOCKET_CONNECTOR,
                             structPackage = "ballerina/http")
)
public class Ready implements NativeCallableUnit {

    @Override
    public void execute(Context context, CallableUnitCallback callback) {
//        try {
//            BMap<String, BValue> webSocketConnector = (BMap<String, BValue>) context.getRefArgument(0);
//            WebSocketOpenConnectionInfo connectionInfo = (WebSocketOpenConnectionInfo) webSocketConnector
//                    .getNativeData(WebSocketConstants.NATIVE_DATA_WEBSOCKET_CONNECTION_INFO);
//            boolean isReady = ((BBoolean) webSocketConnector.get(WebSocketConstants.CONNECTOR_IS_READY_FIELD))
//                    .booleanValue();
//            if (!isReady) {
//                WebSocketUtil.readFirstFrame(connectionInfo.getWebSocketConnection(), webSocketConnector);
//                context.setReturnValues();
//            } else {
//                context.setReturnValues(HttpUtil.getError(context, "Already started reading frames"));
//            }
//            callback.notifySuccess();
//        } catch (Exception e) {
//            context.setReturnValues(HttpUtil.getError(context, e));
//            callback.notifySuccess();
//        }
    }

    public static void ready(Strand strand, ObjectValue wsConnection) {
        //TODO : TempCallableUnitCallback is temporary fix to handle non blocking call
        TempCallableUnitCallback callback = new TempCallableUnitCallback(strand);
        try {
            WebSocketOpenConnectionInfo connectionInfo = (WebSocketOpenConnectionInfo) wsConnection
                    .getNativeData(WebSocketConstants.NATIVE_DATA_WEBSOCKET_CONNECTION_INFO);
            boolean isReady = ((BBoolean) wsConnection.get(WebSocketConstants.CONNECTOR_IS_READY_FIELD))
                    .booleanValue();
            if (!isReady) {
                WebSocketUtil.readFirstFrame(connectionInfo.getWebSocketConnection(), wsConnection);
                //TODO remove this call back
                callback.setReturnValues(null);
            } else {
                //TODO remove this call back
                callback.setReturnValues(HttpUtil.getError("Already started reading frames"));
            }
            callback.notifySuccess();
        } catch (Exception e) {
            //TODO remove this call back
            callback.setReturnValues(HttpUtil.getError(e.getMessage()));
            callback.notifySuccess();
        }
    }

    @Override
    public boolean isBlocking() {
        return false;
    }
}
