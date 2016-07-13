module.exports = {
    TAGS: function (data, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "TAGS", "read", []);
    }
};