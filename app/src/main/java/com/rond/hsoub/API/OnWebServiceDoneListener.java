package com.rond.hsoub.API;

/**
 * Created by Nullsky on 12/23/2016.
 */

public interface OnWebServiceDoneListener {
    void onTaskDone(WebServiceResponse responseData);
    void onError(String trace);
}
