// public member function
'use strict'
var HTML5_SIP_VERSION = '3.1.0.4';
CFCAKeyboard.prototype.showKeyboard = function() {
    this._show(true);
};

CFCAKeyboard.prototype.hideKeyboard = function() {
    this._show(false);
};

/**
 * sipHandle: 可以是输入框ID，也可以是输入框对应的DOM节点
 */
CFCAKeyboard.prototype.bindInputBox = function(sipHandle) {
    var inputID, element;
    if(sipHandle === undefined || sipHandle === null) {
        return CFCA_ERROR_INVALID_PARAMETER;
    } else if(typeof sipHandle === "string") {
        inputID = sipHandle;
        element = document.getElementById(inputID) || null;
    } else if(typeof sipHandle === "object") {
        inputID = sipHandle.id;
        element = sipHandle;
    }
    if(!element) {
        return CFCA_ERROR_INVALID_PARAMETER;
    }
    this.sipHandleID = inputID;
    if(CFCAKeyboard.sipMap[this.sipHandleID] == undefined) {
        initSIPHandle(this.sipHandleID);
        CFCAKeyboard.sipMap[this.sipHandleID] = element;
    }
};

CFCAKeyboard.prototype.setDoneCallback = function(callback) {
    if(typeof callback === "function" && callback.length === 1) {
        this._onDone = callback;
    }
};

CFCAKeyboard.prototype.setPublicKeyToEncrypt = function(publicKeyIndex, sipHandleID) {
    if(sipHandleID !== undefined) {
        return setPublicKeyIndex(sipHandleID, publicKeyIndex);
    }
    return setPublicKeyIndex(this.sipHandleID, publicKeyIndex);
};

CFCAKeyboard.prototype.setOutputType = function(outputType, sipHandleID) {
    if(sipHandleID !== undefined) {
        return setOutputType(sipHandleID, outputType);
    }
    return setOutputType(this.sipHandleID, outputType);
};

CFCAKeyboard.prototype.setMaxLength = function(maxLength, sipHandleID) {
    if(sipHandleID !== undefined) {
        return setMaxLength(sipHandleID, maxLength);
    }
    return setMaxLength(this.sipHandleID, maxLength);
};

CFCAKeyboard.prototype.getMaxLength = function(sipHandleID) {
    if(sipHandleID !== undefined) {
        return getMaxLength(sipHandleID);
    }
    return getMaxLength(this.sipHandleID);
};

CFCAKeyboard.prototype.setMinLength = function(minLength, sipHandleID) {
    if(sipHandleID !== undefined) {
        return setMinLength(sipHandleID, minLength);
    }
    return setMinLength(this.sipHandleID, minLength);
};

CFCAKeyboard.prototype.getMinLength = function(sipHandleID) {
    if(sipHandleID !== undefined) {
        return getMinLength(sipHandleID);
    }
    return getMinLength(this.sipHandleID);
};

CFCAKeyboard.prototype.setMatchRegex = function(matchRegex, sipHandleID) {
    if(sipHandleID !== undefined) {
        return setMatchRegex(sipHandleID, matchRegex);
    }
    return setMatchRegex(this.sipHandleID, matchRegex);
};

CFCAKeyboard.prototype.setServerRandom = function(base64ServerRandom, sipHandleID) {
    if(sipHandleID !== undefined) {
        return setServerRandom(sipHandleID, base64ServerRandom);
    }
    return setServerRandom(this.sipHandleID, base64ServerRandom);
};

CFCAKeyboard.prototype.getEncryptedInputValue = function(sipHandleID) {
    if(sipHandleID !== undefined) {
        return getEncryptedValue(sipHandleID);
    }
    return getEncryptedValue(this.sipHandleID);
};

CFCAKeyboard.prototype.getEncryptedClientRandom = function(sipHandleID) {
    if(sipHandleID !== undefined) {
        return getEncryptClientRandom(sipHandleID);
    }
    return getEncryptClientRandom(this.sipHandleID);
};

CFCAKeyboard.prototype.checkInputValueMatch = function(sipHandleID1, sipHandleID2) {
    return checkInputValueMatch(sipHandleID1, sipHandleID2);
};

CFCAKeyboard.prototype.clearInputValue = function(sipHandleID) {
    var theSipHandleID = sipHandleID || this.sipHandleID;
    if(theSipHandleID  === undefined || CFCAKeyboard.sipMap[theSipHandleID] === undefined) {
        return CFCA_ERROR_INVALID_SIP_HANDLE_ID;
    }
    if(theSipHandleID === this.sipHandleID) {
        this._stop_timer();
    }
    var element = CFCAKeyboard.sipMap[theSipHandleID];
    element.value = "";
    return clearAllCharacters(theSipHandleID);
};

CFCAKeyboard.prototype.getErrorCode = function(sipHandleID) {
    if(sipHandleID !== undefined) {
        return getErrorCode(sipHandleID);
    }
    return getErrorCode(this.sipHandleID);
};

function getCFCAKeyboardVersion() {
    return HTML5_SIP_VERSION;
}
