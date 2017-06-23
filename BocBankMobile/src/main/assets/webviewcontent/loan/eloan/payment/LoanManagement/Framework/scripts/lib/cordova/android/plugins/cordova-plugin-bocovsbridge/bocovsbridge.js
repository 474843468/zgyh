cordova.define("cordova-plugin-bocovsbridge.bocovsbridge", function(require, exports, module) {

    var cordova = require('cordova');
    var exec = require('cordova/exec');
    var argscheck = require('cordova/argscheck');
    var BocovsBridge = function () {
    };

    BocovsBridge.prototype.showEtokenPasswordPage = function (success, fail, params) {
        argscheck.checkArgs('fFO', 'bocovsbridge.showEtokenPasswordPage', arguments);
        return exec(function (arguments) {
            success(arguments);
        }, function (arguments) {
            fail(arguments);
        }, 'bocovsbridge', 'showEtokenPasswordPage', [params]);
    };

    BocovsBridge.prototype.showDatePicker = function (success, fail, params) {





        return exec(function (arguments) {
            success(arguments);
        }, function (arguments) {
            fail(arguments);
        }, 'bocovsbridge', 'showDatePicker', [params]);
    };

    BocovsBridge.prototype.showAccountListPage = function (success, fail, accountsData) {
        argscheck.checkArgs('fFO', 'bocovsbridge.showAccountListPage', arguments);
        return exec(function (arguments) {
            success(arguments);
        }, function (arguments) {
            fail(arguments);
        }, 'bocovsbridge', 'showAccountListPage', [accountsData]);
    };

    BocovsBridge.prototype.navigateToPage = function (success, fail, params) {
        return exec(function (args) {
            success(args);
        }, function (args) {
            fail(args);
        }, 'bocovsbridge', 'navigateToPage', [params]);
    };

    BocovsBridge.prototype.retrieveAccountsList = function (success, fail, params) {
        return exec(function (args) {
            success(args);
        }, function (args) {
            fail(args);
        }, 'bocovsbridge', 'retrieveAccountsList', []);
    };

    BocovsBridge.prototype.retrieveTransAcctInfo = function (success, fail, params) {
        return exec(function (args) {
            success(args);
        }, function (args) {
            fail(args);
        }, 'bocovsbridge', 'retrieveTransAcctInfo', []);
    };

    BocovsBridge.prototype.backToTransAcctInfoPage = function (success, fail, params) {
        return exec(function (args) {
            success(args);
        }, function (args) {
            fail(args);
        }, 'bocovsbridge', 'backToTransAcctInfoPage', [params]);
    };



    window.bocovsbridge = new BocovsBridge();

    // backwards compatibility
    window.plugins = window.plugins || {};
    window.plugins.bocovsbridge = window.bocovsbridge;
});