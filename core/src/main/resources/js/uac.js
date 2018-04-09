/*需要的起始cookie参数有4个 _n3fa_cid,_n3fa_ext, _n3fa_lpvt_*,_n3fa_lvt_*, 均需要js的拼装 */


/*起始cookie "_n3fa_cid" */
function uuid() {
    var d = new Date().getTime();
    var uuid = 'xxxxxxxxxxxx4xxxyxxxxxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c == 'x' ? r : (r & 0x7 | 0x8)).toString(16);
    });
    return uuid;
}


/*起始cookie "_n3fa_ext" */
function buildExt() {
    var _ext = {};
    _ext.ft = nowTimeOnSecond();
    extToServerParamNames = "ft".split(" ");
    return generateStringWithObjProperty(_ext, extToServerParamNames, "^");
}

function nowTimeOnSecond() {
    return Math.round(new Date().getTime() / 1E3);
}

function generateStringWithObjProperty(obj, propertys, join) {
    for (var valuesToSend = [], i = 0, length = propertys.length; i < length; i++) {
        var propertyName = propertys[i], propertyValue = obj[propertyName];
        "undefined" != typeof propertyValue && "" !== propertyValue
        && valuesToSend.push(propertyName + "=" + encodeURIComponent(propertyValue))
    }
    return valuesToSend.join(join)
}

