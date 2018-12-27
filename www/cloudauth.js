var exec = require('cordova/exec');

exports.start = function(success, error, arg0) {
    exec(success, error, "CloudAuth", "start", [arg0]);
};


