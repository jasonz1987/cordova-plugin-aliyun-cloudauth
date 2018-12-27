package com.jasonz.cordova.aliyun;

import org.apache.cordova.BuildConfig;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;

import android.util.Log;
import android.webkit.WebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;

import com.alibaba.security.rp.RPSDK;

public class CDVBugly extends CordovaPlugin {
    public static final String TAG = "Cordova.Plugin.Aliyun.CloudAuth";
    public static final String ERROR_INVALID_PARAMETERS = "参数格式错误";

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        RPSDK.initialize(this);
    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {

        if(action.equals("start")) {
            return this.start(args, callbackContext);
        } 

        return false;
    }

    private boolean start(CordovaArgs args, CallbackContext callbackContext) {
        final JSONObject params;

        try {
            params = args.getJSONObject(0);

             if(params.has("token")) {
                String token = params.get("token").toString();
                 RPSDK.start(token, this, 
                 new RPSDK.RPCompletedListener() {
                 @Override
                 public void onAuditResult(RPSDK.AUDIT audit) {
                     Toast.makeText(ParametersActivity.this, audit + "", Toast.LENGTH_SHORT).show();
                     if(audit == RPSDK.AUDIT.AUDIT_PASS) { //认证通过
                        callbackContext.success();
                     }
                     else if(audit == RPSDK.AUDIT.AUDIT_FAIL) { //认证不通过
                         callbackContext.error("AUDIT_FAIL");
                     }
                     else if(audit == RPSDK.AUDIT.AUDIT_IN_AUDIT) { //认证中，通常不会出现，只有在认证审核系统内部出现超时，未在限定时间内返回认证结果时出现。此时提示用户系统处理中，稍后查看认证结果即可。
                     }
                     else if(audit == RPSDK.AUDIT.AUDIT_NOT) { //未认证，用户取消
                        callbackContext.error("AUDIT_NOT");
                     }
                     else if(audit == RPSDK.AUDIT.AUDIT_EXCEPTION){ //系统异常
                        callbackContext.error("AUDIT_EXCEPTION");
                     }
                 }
             });
            }      
        } catch (JSONException e) {
            callbackContext.error(ERROR_INVALID_PARAMETERS);
            return true;
        }
    
        return true;
    }


}