cordova.define("cordova-plugin-bocovsbridge.bocovsbridge", function(require, exports, module) { /*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
*/

var argscheck = require('cordova/argscheck'),
               exec = require('cordova/exec');

var bocovsbridge = {};

//parameter settings example: {"cipher":true,"cipherType":"0","randomeKey_S":"value","outputValueType":"2","passwordRegularExpression":"value","passwordMinLength":"1","passwordMaxLength":"10"}
bocovsbridge.showEtokenPasswordPage = function(successCallback, errorCallback, settings) {
    argscheck.checkArgs('fFO', 'bocovsbridge.showEtokenPasswordPage', arguments);
    exec(successCallback, errorCallback, "Bocovsbridge", "showEtokenPasswordPage", [settings]);
};
//parameter settings example: {"select":{"options":[{"name":"value"},{"name":"value"},{"name":"value"}],"showPicker":true},"picker":{"single":{"min":"123456","max":"123456","def":"123456"},"scope":{"min":"123456","max":"123456","mindef":"123456","maxdef":"123456"},"format":"YYMMDD"}}
bocovsbridge.showDatePicker = function(successCallback, errorCallback, settings) {
    argscheck.checkArgs('fFO', 'bocovsbridge.showDatePicker', arguments);
    exec(successCallback, errorCallback, "Bocovsbridge", "showDatePicker", [settings]);
};
//parameter accountsData example: same to receive data type from native
bocovsbridge.showAccountListPage = function(successCallback, errorCallback, accountsData) {
    argscheck.checkArgs('fFO', 'bocovsbridge.showAccountListPage', arguments);
    exec(successCallback, errorCallback, "Bocovsbridge", "showAccountListPage", [accountsData]);
};

bocovsbridge.navigateToPage = function(successCallback, errorCallback, pageId) {
    argscheck.checkArgs('fFs', 'bocovsbridge.navigateToPage', arguments);
    exec(successCallback, errorCallback, "Bocovsbridge", "navigateToPage", [pageId]);
};
//according 'successCallback' to receiving 'accountsList'
bocovsbridge.retrieveAccountsList = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'bocovsbridge.retrieveAccountsList', arguments);
    exec(successCallback, errorCallback, "Bocovsbridge", "retrieveAccountsList", []);
};

bocovsbridge.retrieveTransAcctInfo = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'bocovsbridge.retrieveTransAcctInfo', arguments);
    exec(successCallback, errorCallback, "Bocovsbridge", "retrieveTransAcctInfo", []);
};
            
bocovsbridge.backToTransAcctInfoPage = function(successCallback, errorCallback, payeeInfo) {
    argscheck.checkArgs('fFO', 'bocovsbridge.backToTransAcctInfoPage', arguments);
    exec(successCallback, errorCallback, "Bocovsbridge", "backToTransAcctInfoPage", [payeeInfo]);
};

module.exports = bocovsbridge;

});
