
#import <Cordova/CDVViewController.h>
#import <RPSDK/RPSDK.h>
#import "CDVCloudAuth.h"

@implementation CDVCloudAuth

- (void)pluginInitialize
{
    [RPSDK initialize:RPSDKEnvOnline];
}

- (void)start:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;

    NSDictionary *params = [command.arguments objectAtIndex:0];
    
    if (!params)
    {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"参数格式错误"];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        return ;
    }
        
    if([params objectForKey:@"debug"]) {
        NSString *token = [params objectForKey:@"token"];

      


        [RPSDK start:verifyToken rpCompleted:^(AUDIT auditState) {
             NSLog(@"verifyResult = %ld",(unsigned long)auditState);
             if(auditState == AUDIT_PASS) { //认证通过
                pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@""];
    
             }
             else if(auditState == AUDIT_FAIL) { //认证不通过
                 pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"AUDIT_FAIL"];
                 }
             else if(auditState == AUDIT_IN_AUDIT) { //认证中，通常不会出现，只有在认证审核系统内部出现超时，未在限定时间内返回认证结果时出现。此时提示用户系统处理中，稍后查看认证结果即可。
             }
             else if(auditState == AUDIT_NOT) { //未认证，用户取消
                 pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"AUDIT_NOT"];
             }
             else if(auditState == AUDIT_EXCEPTION) { //系统异常
                pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"AUDIT_EXCEPTION"];
             }
                             [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];

         }withVC:self.navigationController];
    }
   
    
 
}

@end
