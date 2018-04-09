$.fn.aesEncrypt = function(e) {
    var a = CryptoJS.MD5("login.189.cn");
    var c = CryptoJS.enc.Utf8.parse(a);
    var b = CryptoJS.enc.Utf8.parse("1234567812345678");
    var d = CryptoJS.AES.encrypt(e, c, {
        iv: b
    });
    return d + ""
}
;
$.fn.aesDecrypt = function(e) {
    var b = CryptoJS.MD5("login.189.cn");
    var d = CryptoJS.enc.Utf8.parse(b);
    var c = CryptoJS.enc.Utf8.parse("1234567812345678");
    var a = CryptoJS.AES.decrypt(e, d, {
        iv: c
    }).toString(CryptoJS.enc.Utf8);
    return a
}
;
$.fn.valAesEncrypt = function() {
    return this.aesEncrypt(this.val())
}
;
$.fn.valAesDecrypt = function() {
    return this.aesDecrypt(this.val())
}
;
$.fn.valAesEncryptSet = function() {
    var d = this.val();
    var a, c;
    try {
        a = this.aesDecrypt(d);
        if (a != "") {
            c = this.aesEncrypt(a);
            if (c != d) {
                a = ""
            }
        }
    } catch (b) {
        a = ""
    }
    if (a == "") {
        c = this.aesEncrypt(d)
    }
    this.val(c);
    return this.val()
}
;
