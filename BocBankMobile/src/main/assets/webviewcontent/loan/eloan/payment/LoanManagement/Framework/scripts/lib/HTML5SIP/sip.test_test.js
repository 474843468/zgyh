var goog = goog || {};
goog.global = this;
goog.exportPath_ = function (name, opt_object, opt_objectToExportTo) {
    var parts = name.split(".");
    var cur = opt_objectToExportTo || goog.global;
    if (!(parts[0]in cur) && cur.execScript)cur.execScript("var " + parts[0]);
    for (var part; parts.length && (part = parts.shift());)if (!parts.length && opt_object !== undefined)cur[part] = opt_object; else if (cur[part])cur = cur[part]; else cur = cur[part] = {}
};
goog.exportSymbol = function (publicPath, object, opt_objectToExportTo) {
    goog.exportPath_(publicPath, object, opt_objectToExportTo)
};
goog.exportProperty = function (object, publicName, symbol) {
    object[publicName] = symbol
};
var HTML5_SIP_VERSION = "3.2.0.8";
var DEFAULT_MIN_LENGTH = 6;
var DEFAULT_MAX_LENGTH = 8;
var KEYBOARD_TYPE_COMPLETE = 0;
var KEYBOARD_TYPE_DIGITAL = 1;
var OUTPUT_TYPE_HASH = 1;
var OUTPUT_TYPE_ORIGINAL = 2;
var PUBLIC_KEY_OPEN_PLATFORM = 0;
var PUBLIC_KEY_EBANK = 1;
var CIPHER_TYPE_SM2 = 0;
var CIPHER_TYPE_RSA = 1;
var CFCA_OK = 0;
var CFCA_ERROR_INVALID_PARAMETER = 4097;
var CFCA_ERROR_INVALID_SIP_HANDLE_ID = 4098;
var CFCA_ERROR_INPUT_LENGTH_OUT_OF_RANGE = 4099;
var CFCA_ERROR_INPUT_VALUE_IS_NULL = 4100;
var CFCA_ERROR_SERVER_RANDOM_INVALID = 4101;
var CFCA_ERROR_SERVER_RANDOM_IS_NULL = 4102;
var CFCA_ERROR_INPUT_VALUE_NOT_MATCH_REGEX = 4103;
var CFCA_ERROR_RSA_ENCRYPT_FAILED = 4104;
var l, ba = new function () {
    this.ta = function (a, b) {
        if ("8" != a.substring(b + 2, b + 3))return 1;
        var d = parseInt(a.substring(b + 3, b + 4));
        return 0 == d ? -1 : 0 < d && 10 > d ? d + 1 : -2
    };
    this.eb = function (a, b) {
        var d = this.ta(a, b);
        return 1 > d ? "" : a.substring(b + 2, b + 2 + 2 * d)
    };
    this.ea = function (a, b) {
        var d = this.eb(a, b);
        return"" == d ? -1 : aa(8 > parseInt(d.substring(0, 1)) ? new m(d, 16) : new m(d.substring(2), 16))
    };
    this.fa = function (a, b) {
        var d = this.ta(a, b);
        return 0 > d ? d : b + 2 * (d + 1)
    };
    this.da = function (a, b) {
        var d = this.fa(a, b), c = this.ea(a, b);
        return a.substring(d,
                d + 2 * c)
    };
    this.fb = function (a, b) {
        var d = this.fa(a, b), c = this.ea(a, b);
        return d + 2 * c
    };
    this.wa = function (a, b) {
        var d = [], c = this.fa(a, b);
        d.push(c);
        for (var e = this.ea(a, b), f = c, g = 0; ;) {
            f = this.fb(a, f);
            if (null == f || f - c >= 2 * e)break;
            if (200 <= g)break;
            d.push(f);
            g++
        }
        return d
    };
    this.ua = function (a, b, d) {
        if (0 == d.length)return b;
        var c = d.shift();
        b = this.wa(a, b);
        return this.ua(a, b[c], d)
    }
};
ba.mb = function (a, b, d, c) {
    b = this.ua(a, b, d);
    if (void 0 === b)throw"can't find nthList object";
    if (void 0 !== c && a.substr(b, 2) != c)throw"checking tag doesn't match: " + a.substr(b, 2) + "!=" + c;
    return this.da(a, b)
};
ba.nb = function (a) {
    function b(a) {
        return 7 <= a.length ? a : Array(7 - a.length + 1).join("0") + a
    }

    var d = [], c = parseInt(a.substr(0, 2), 16);
    d[0] = new String(Math.floor(c / 40));
    d[1] = new String(c % 40);
    var e = a.substr(2);
    a = [];
    for (c = 0; c < e.length / 2; c++)a.push(parseInt(e.substr(2 * c, 2), 16));
    for (var e = [], f = "", c = 0; c < a.length; c++)a[c] & 128 ? f += b((a[c] & 127).toString(2)) : (f += b((a[c] & 127).toString(2)), e.push(new String(parseInt(f, 2))), f = "");
    d = d.join(".");
    0 < e.length && (d = d + "." + e.join("."));
    return d
};
if ("undefined" === typeof r)var r = {};
function ca(a) {
    return"undefined" === typeof r[a] ? -1 : r[a].h.length
}
function da(a) {
    if ("undefined" === typeof r[a])return!1;
    if (0 < r[a].Y.length) {
        var b = ea(B(r[a].I, r[a].h, 0, r[a].J, 0)), d = !1;
        if ((a = b.match(r[a].Y)) && 0 < a.length)for (var c = 0; c < a.length; c++)if (a[c] == b) {
            d = !0;
            break
        }
        return d
    }
    return!0
}
function fa(a, b) {
    if ("undefined" === typeof r[a])return CFCA_ERROR_INVALID_SIP_HANDLE_ID;
    var d = ha(b), c, e = [];
    for (c = 0; 2 * c < d.length; ++c)e[c] = parseInt(d.substring(2 * c, 2 * c + 2), 16);
    if (16 != e.length)return r[a].P = null, CFCA_ERROR_SERVER_RANDOM_INVALID;
    r[a].P = e;
    return CFCA_OK
}
function ia(a, b) {
    if ("undefined" === typeof r[a])return CFCA_ERROR_INVALID_SIP_HANDLE_ID;
    if (0 > b)return CFCA_ERROR_INVALID_PARAMETER;
    r[a].maxLength = b;
    return CFCA_OK
}
function ja(a, b) {
    if ("undefined" === typeof r[a])return CFCA_ERROR_INVALID_SIP_HANDLE_ID;
    if (0 > b)return CFCA_ERROR_INVALID_PARAMETER;
    r[a].xa = b;
    return CFCA_OK
}
function ka(a, b) {
    if ("undefined" === typeof r[a])return CFCA_ERROR_INVALID_SIP_HANDLE_ID;
    if (b != PUBLIC_KEY_OPEN_PLATFORM && b != PUBLIC_KEY_EBANK)return CFCA_ERROR_INVALID_PARAMETER;
    r[a].ha = b;
    return CFCA_OK
}
function la(a, b) {
    if ("undefined" === typeof r[a])return CFCA_ERROR_INVALID_SIP_HANDLE_ID;
    if (b != OUTPUT_TYPE_HASH && b != OUTPUT_TYPE_ORIGINAL)return CFCA_ERROR_INVALID_PARAMETER;
    r[a].Ba = b;
    return CFCA_OK
}
function ma(a, b) {
    if ("undefined" === typeof r[a])return CFCA_ERROR_INVALID_SIP_HANDLE_ID;
    if (b != CIPHER_TYPE_SM2 && b != CIPHER_TYPE_RSA)return CFCA_ERROR_INVALID_PARAMETER;
    r[a].R = b;
    return CFCA_OK
}
function na(a, b) {
    if ("undefined" === typeof r[a])return CFCA_ERROR_INVALID_SIP_HANDLE_ID;
    r[a].Y = b;
    return CFCA_OK
}
function oa(a) {
    var b, d;
    if ("undefined" === typeof r[a])return r[a].errorCode = CFCA_ERROR_INVALID_SIP_HANDLE_ID, "";
    if (null == r[a].P)return r[a].errorCode = CFCA_ERROR_SERVER_RANDOM_IS_NULL, "";
    if ("" == r[a].h)return r[a].errorCode = CFCA_ERROR_INPUT_VALUE_IS_NULL, "";
    if (r[a].R == CIPHER_TYPE_RSA) {
        b = Array(24);
        d = Array(8);
        for (var c = 0; 16 > c; c++)12 > c ? (b[c] = r[a].P[c], b[c + 12] = r[a].S[c]) : (d[c - 12] = r[a].P[c], d[c - 12 + 4] = r[a].S[c])
    } else for (b = Array(16), d = Array(16), c = 0; 16 > c; c++)8 > c ? (b[c] = r[a].P[c], b[c + 8] = r[a].S[c]) : (d[c - 8] = r[a].P[c],
        d[c] = r[a].S[c]);
    var e = ea(B(r[a].I, r[a].h, 0, r[a].J, 0));
    if (e.length < r[a].xa || e.length > r[a].maxLength)return r[a].errorCode = CFCA_ERROR_INPUT_LENGTH_OUT_OF_RANGE, "";
    if (0 < r[a].Y.length) {
        var f = !1, g = e.match(r[a].Y);
        if (g && 0 < g.length)for (c = 0; c < g.length; c++)if (g[c] == e) {
            f = !0;
            break
        }
        if (!f)return r[a].errorCode = CFCA_ERROR_INPUT_VALUE_NOT_MATCH_REGEX, ""
    }
    if (r[a].Ba === OUTPUT_TYPE_HASH) {
        for (var c = "", f = -1, h; ++f < e.length;)g = e.charCodeAt(f), h = f + 1 < e.length ? e.charCodeAt(f + 1) : 0, 55296 <= g && 56319 >= g && 56320 <= h && 57343 >= h && (g =
            65536 + ((g & 1023) << 10) + (h & 1023), f++), 127 >= g ? c += String.fromCharCode(g) : 2047 >= g ? c += String.fromCharCode(192 | g >>> 6 & 31, 128 | g & 63) : 65535 >= g ? c += String.fromCharCode(224 | g >>> 12 & 15, 128 | g >>> 6 & 63, 128 | g & 63) : 2097151 >= g && (c += String.fromCharCode(240 | g >>> 18 & 7, 128 | g >>> 12 & 63, 128 | g >>> 6 & 63, 128 | g & 63));
        e = Array(c.length >> 2);
        for (f = 0; f < e.length; f++)e[f] = 0;
        for (f = 0; f < 8 * c.length; f += 8)e[f >> 5] |= (c.charCodeAt(f / 8) & 255) << 24 - f % 32;
        c = 8 * c.length;
        e[c >> 5] |= 128 << 24 - c % 32;
        e[(c + 64 >> 9 << 4) + 15] = c;
        c = Array(80);
        f = 1732584193;
        g = -271733879;
        h = -1732584194;
        for (var k = 271733878, n = -1009589776, w = 0; w < e.length; w += 16) {
            for (var t = f, v = g, x = h, u = k, C = n, y = 0; 80 > y; y++) {
                var z;
                16 > y ? z = e[w + y] : (z = c[y - 3] ^ c[y - 8] ^ c[y - 14] ^ c[y - 16], z = z << 1 | z >>> 31);
                c[y] = z;
                z = D(D(f << 5 | f >>> 27, 20 > y ? g & h | ~g & k : 40 > y ? g ^ h ^ k : 60 > y ? g & h | g & k | h & k : g ^ h ^ k), D(D(n, c[y]), 20 > y ? 1518500249 : 40 > y ? 1859775393 : 60 > y ? -1894007588 : -899497514));
                n = k;
                k = h;
                h = g << 30 | g >>> 2;
                g = f;
                f = z
            }
            f = D(f, t);
            g = D(g, v);
            h = D(h, x);
            k = D(k, u);
            n = D(n, C)
        }
        e = [f, g, h, k, n];
        c = "";
        for (f = 0; f < 32 * e.length; f += 8)c += String.fromCharCode(e[f >> 5] >>> 24 - f % 32 & 255);
        e = c;
        try {
            pa
        } catch (M) {
            pa =
                0
        }
        c = pa ? "0123456789ABCDEF" : "0123456789abcdef";
        f = "";
        for (h = 0; h < e.length; h++)g = e.charCodeAt(h), f += c.charAt(g >>> 4 & 15) + c.charAt(g & 15);
        e = qa(f)
    }
    if (r[a].R == CIPHER_TYPE_RSA) {
        b = B(ra(b), e, 1, ra(d), 1);
        d = "";
        e = "0123456789abcdef".split("");
        for (c = 0; c < b.length; c++)d += e[b.charCodeAt(c) >> 4] + e[b.charCodeAt(c) & 15];
        b = qa(d)
    } else {
        c = [];
        for (f = 0; f < e.length; f++)k = e.charCodeAt(f), g = [], 0 < k >>> 31 || (0 <= k && 127 >= k ? g.push(k) : 128 <= k && 2047 >= k ? (h = k & 255, k = 192 ^ k >>> 8 << 2 ^ h >>> 6, t = 128 ^ h << 2 >>> 2, g.push(k), g.push(t)) : 2048 <= k && 65535 >= k ? (x = 224, n = t =
            128, w = k >>> 8, h = k & 255, k = x ^ w >>> 4, t = t ^ (w & 15) << 2 ^ h >>> 6, n ^= h & 63, g.push(k), g.push(t), g.push(n)) : 65536 <= k && 1114111 >= k && (x = 240, n = t = 128, v = k >>> 16, w = k >>> 8 & 255, h = k & 255, k = x ^ v >>> 2, t = t ^ (v & 3) << 4 ^ w >>> 4, n = n ^ (w & 15) << 2 ^ h >>> 6, h = 128 ^ h & 63, g.push(k), g.push(t), g.push(n), g.push(h))), c = c.concat(g);
        e = c;
        c = d;
        d = 0;
        if (0 == e.length || 16 != b.length || 16 != c.length)g = {result: null, errorCode: -1}; else {
            k = sa(b);
            h = 0;
            b = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
            h = [2746333894, 1453994832, 1736282519, 2993693404];
            f = [462357, 472066609,
                943670861, 1415275113, 1886879365, 2358483617, 2830087869, 3301692121, 3773296373, 4228057617, 404694573, 876298825, 1347903077, 1819507329, 2291111581, 2762715833, 3234320085, 3705924337, 4177462797, 337322537, 808926789, 1280531041, 1752135293, 2223739545, 2695343797, 3166948049, 3638552301, 4110090761, 269950501, 741554753, 1213159005, 1684763257];
            g = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
            g[0] = k[0] ^ h[0];
            g[1] = k[1] ^ h[1];
            g[2] = k[2] ^ h[2];
            g[3] = k[3] ^ h[3];
            for (h = 0; 32 > h; h++)k = k = k = 0, k = ta(g[h + 1] ^ g[h +
                2] ^ g[h + 3] ^ f[h]), n = 0, k = n = k ^ E(k, 13) ^ E(k, 23), b[h] = g[h] ^ k, g[h + 4] = b[h];
            f = sa(c);
            for (c = []; d < e.length; d += 16)g = va(sa(e.slice(d, d + 16)), b, f), c = c.concat(g.A);
            0 == (e.length & 15) && (b = va([269488144, 269488144, 269488144, 269488144], b, f), c = c.concat(b.A));
            b = c;
            if (0 == b.length)b = null; else {
                d = [];
                for (e = 0; e < b.length; e++)d.push(b[e] >>> 24 & 255), d.push(b[e] >>> 16 & 255), d.push(b[e] >>> 8 & 255), d.push(b[e] & 255);
                b = d
            }
            g = {result: b, errorCode: 0}
        }
        b = wa(g.result)
    }
    r[a].errorCode = CFCA_OK;
    return b
}
function xa(a) {
    var b;
    if ("undefined" === typeof r[a])return r[a].errorCode = CFCA_ERROR_INVALID_SIP_HANDLE_ID, "";
    if (r[a].R == CIPHER_TYPE_RSA) {
        b = ha(r[a].ha === PUBLIC_KEY_OPEN_PLATFORM ? BOC_RSA_PUBLICKEY_OPEN_PLATFORM : BOC_RSA_PUBLICKEY_EBANK);
        var d = r[a].S, c = ba.wa(b, 0);
        if (2 != c.length)b = null; else {
            var e = ba.da(b, c[0]), c = ba.da(b, c[1]);
            b = new ya;
            if (null != e && null != c && 0 < e.length && 0 < c.length)e = new m(e, 16), b.n = e, b.e = parseInt(c, 16); else throw Error("Invalid RSA public key");
            e = za(b.n) + 7 >> 3;
            if (e < d.length + 11)throw Error("Message too long for RSA");
            for (var c = [], f = d.length - 1; 0 <= f && 0 < e;)c[--e] = d[f--];
            c[--e] = 0;
            d = new Aa;
            for (f = []; 2 < e;) {
                for (f[0] = 0; 0 == f[0];)d.Aa(f);
                c[--e] = f[0]
            }
            c[--e] = 2;
            c[--e] = 0;
            d = new m(c);
            b = Ba(d, b.e, b.n);
            null == b ? b = null : (b = b.toString(16), b = 0 == (b.length & 1) ? b : "0" + b);
            b = qa(b)
        }
    } else {
        c = ha(r[a].ha === PUBLIC_KEY_OPEN_PLATFORM ? BOC_SM2_PUBLICKEY_OPEN_PLATFORM : BOC_SM2_PUBLICKEY_EBANK);
        b = r[a].S;
        a:{
            var d = b.length, e = c.slice(0, 64), f = c.slice(64), g = [], h, k, c = !1;
            h = new m(e, 16);
            var n = new m(f, 16), c = new m("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF",
                16);
            k = new m("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC", 16);
            var g = new m("28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93", 16), w = new m("2", 16), t = new m("3", 16), n = Ca(n, w, c);
            n.toString(16);
            t = Ca(h, t, c);
            h = Da(k.multiply(h), c);
            h = Da(t.add(h), c);
            h = Da(h.add(g), c);
            h.toString(16);
            c = n.l(h);
            if (0 == c)b = void 0; else {
                for (; ;)if (c = new m("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123", 16), g = c.m(F), h = new Aa, g = G(new m(za(c), h), g).add(F), g.toString(16), c = h = new Ea(new m("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF",
                    16), new m("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC", 16), new m("28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93", 16)), k = (new H(c, I(c, new m("32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16)), I(c, new m("BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16)))).multiply(g), c = Fa(k), c = Ga(c.x.toString(16)), k = Ha(k), k = Ga(k.x.toString(16)), !(32 > c.length || 32 > k.length)) {
                    c = Ia(c);
                    k = Ia(k);
                    c = c.concat(k);
                    h = new H(h, I(h, new m(e, 16)), I(h, new m(f,
                        16)));
                    if (J(h)) {
                        b = void 0;
                        break a
                    }
                    h = h.multiply(g);
                    g = Fa(h);
                    g = Ga(g.x.toString(16));
                    h = Ha(h);
                    h = Ga(h.x.toString(16));
                    if (!(32 > g.length || 32 > h.length)) {
                        g = Ia(g);
                        h = Ia(h);
                        k = g.concat(h);
                        n = !0;
                        for (t = 0; t < k.length; t++)if (0 != k[t]) {
                            n = !1;
                            break
                        }
                        if (!n)break
                    }
                }
                f = k;
                t = k.length;
                w = d << 3;
                e = [];
                if (!(0 < w >>> 30)) {
                    var v = 1;
                    k = w + 256 - 1 >>> 8;
                    for (var x = 0, u = 0, C = 0, y = [0, 0, 0, 0], C = v, n = [], x = 1; x <= k; x++) {
                        for (var u = Ja(), z = Ka(u.s, u.B, u.A, u.C, f, t), C = v, u = 3; 0 <= u; u--)y[u] = C & 255, C >>>= 8;
                        z = Ka(z.s, z.B, z.A, z.C, y, 4);
                        u = La(z.s, z.B, z.A, z.C, null);
                        for (C = 0; C < u.length; C++)n.push(u[C]);
                        v++
                    }
                    if (0 == (w & 255))for (u = 0; u < k << 5; u++)e.push(n[u]); else {
                        f = (w & 255) >>> 3;
                        for (u = 0; u < k - 1 << 5; u++)e.push(n[u]);
                        for (C = 0; C < f; C++)e.push(n[u + C])
                    }
                }
                if (e.length != d)b = void 0; else {
                    f = [];
                    for (k = 0; k < d; k++)f[k] = b[k] ^ e[k];
                    d = [];
                    d = g.concat(b);
                    b = d = d.concat(h);
                    e = Ja();
                    d = Ka(e.s, e.B, e.A, e.C, b, d.length);
                    b = La(d.s, d.B, d.A, d.C, b);
                    g = c.concat(b);
                    g = g.concat(f);
                    for (b = 0; b < c.length; b++);
                    b = g
                }
            }
        }
        b = wa(b)
    }
    if (null == b)return r[a].errorCode = CFCA_ERROR_RSA_ENCRYPT_FAILED, "";
    r[a].errorCode = CFCA_OK;
    return b
}
function Ma(a, b) {
    if ("undefined" === typeof r[a] || "undefined" === typeof r[b])return!1;
    if ("" == r[a].h && "" == r[b].h)return!0;
    if ("" == r[a].h || "" == r[b].h)return!1;
    var d = ea(B(r[a].I, r[a].h, 0, r[a].J, 0)), c = ea(B(r[b].I, r[b].h, 0, r[b].J, 0));
    return d == c ? !0 : !1
}
function Na(a) {
    return"undefined" === typeof r[a] ? CFCA_ERROR_INVALID_SIP_HANDLE_ID : r[a].errorCode
}
function Oa(a) {
    var b = a.toLowerCase(), d = window.document, c = d.documentElement;
    if (void 0 === window["inner" + a])a = c["client" + a]; else if (window["inner" + a] != c["client" + a]) {
        var e = d.createElement("body");
        e.id = "vpw-test-b";
        e.style.cssText = "overflow:scroll";
        var f = d.createElement("div");
        f.id = "vpw-test-d";
        f.style.cssText = "position:absolute;top:-1000px";
        f.innerHTML = "<style>@media(" + b + ":" + c["client" + a] + "px){body#vpw-test-b div#vpw-test-d{" + b + ":7px!important}}</style>";
        e.appendChild(f);
        c.insertBefore(e, d.head);
        a = 7 ==
            f["offset" + a] ? window["inner" + a] : c["client" + a];
        c.removeChild(e)
    } else a = window["inner" + a];
    return a
}
if (document.all && !window.setTimeout.b) {
    var Pa = window.setTimeout;
    window.setTimeout = function (a, b) {
        var d = Array.prototype.slice.call(arguments, 2);
        return Pa(a instanceof Function ? function () {
            a.apply(null, d)
        } : a, b)
    };
    window.setTimeout.b = !0
}
if (document.all && !window.setInterval.b) {
    var Qa = window.setInterval;
    window.setInterval = function (a, b) {
        var d = Array.prototype.slice.call(arguments, 2);
        return Qa(a instanceof Function ? function () {
            a.apply(null, d)
        } : a, b)
    };
    window.setInterval.b = !0
}
function K(a, b) {
    return Ra(this, a, b)
}
goog.exportSymbol("CFCAKeyboard", K);
var Sa = {}, Ta = Oa("Height"), Ua = Oa("Width"), Va = window.navigator.userAgent.toLowerCase(), Wa = [], Xa = "touchmove" + (window.navigator.msPointerEnabled ? " MSPointerMove" : ""), Ya = "touchstart" + (window.navigator.msPointerEnabled ? " MSPointerDown" : ""), Za = "touchend" + (window.navigator.msPointerEnabled ? " MSPointerUp" : "");
function $a(a) {
    a.preventDefault && a.preventDefault()
}
function ab(a, b, d) {
    b = b.split(" ");
    for (var c = 0; c < b.length; c++)a.addEventListener(b[c], d, !1)
}
function bb(a, b, d) {
    b = b.split(" ");
    for (var c = 0; c < b.length; c++)a.removeEventListener(b[c], d, !1)
}
var cb = [];
function db(a) {
    this.b = a;
    ab(a, Ya, this);
    a.addEventListener("click", this, !1)
}
function eb(a) {
    Wa.pop();
    a = a.b;
    var b = a.id.indexOf("___"), d = Sa[a.id.substring(0, b)], b = a.id.substring(b + 3);
    switch (b) {
        case "caps":
            d.H || (a.className = "cfca-btn cfca-mod");
            break;
        case "del":
        case "sp":
        case "done":
            a.className = "cfca-btn cfca-mod";
            break;
        default:
            if (!isNaN(parseInt(b))) {
                d = a.value;
                if (void 0 === d || 0 === d.length)break;
                a.className = "cfca-btn cfca-default";
                "object" === typeof K.b && (fb(), delete K.b)
            }
    }
}
db.prototype.handleEvent = function (a) {
    switch (a.type) {
        case "touchstart":
        case "pointerdown":
            a.preventDefault && a.preventDefault();
            a.stopPropagation && a.stopPropagation();
            for (var b = 0; b < Wa.length; b++)Wa[b].reset(), eb(Wa[b]);
            Wa.push(this);
            var d = this.b, b = d.id.indexOf("___"), c = d.id.substring(b + 3), e = parseInt(c), b = Sa[d.id.substring(0, b)];
            switch (c) {
                case "caps":
                    b.H || (d.className = "cfca-btn cfca-mod-click");
                    break;
                case "del":
                case "sp":
                case "done":
                    d.className = "cfca-btn cfca-mod-click";
                    break;
                default:
                    if (!isNaN(e)) {
                        c =
                            b.G ? d.value.toUpperCase() : d.value.toLowerCase();
                        if (void 0 === c || 0 === c.length)break;
                        d.className = "cfca-btn cfca-click";
                        var d = gb(d), e = Oa("Height"), f = Oa("Width");
                        null != d && e > f && b.T == KEYBOARD_TYPE_COMPLETE && (K.b = new hb(d.x - d.ja / 2, d.y - d.L, 2 * d.ja, d.L, c), b = K.b, (d = document.getElementById("CFCABubble")) ? (d.style.top = b.j + "px", d.style.left = b.i + "px", d.style.height = b.height + "px", d.style.width = b.width + "px", d.firstChild.value = b.b) : (d = document.createElement("DIV"), d.id = "CFCABubble", document.body.appendChild(d), c = d.style,
                            c.position = "fixed", c.b = "99999999", c.top = b.j + "px", c.left = b.i + "px", c.height = b.height + "px", c.width = b.width + "px", c = document.createElement("INPUT"), d.appendChild(c), c.className = "cfca-btn cfca-bubble", c.setAttribute("type", "button"), c.value = b.b, c.style.fontSize = Math.floor(14 * b.height / 20) + "px", c.style.height = "100%", c.style.fontWeight = "bold"))
                    }
            }
            ab(this.b, Za, this);
            ab(document.body, Xa, this);
            this.i = a.clientX || a.changedTouches[0].clientX;
            this.j = a.clientY || a.changedTouches[0].clientY;
            break;
        case "touchmove":
        case "pointermove":
            a:if (!window.navigator.msPointerEnabled ||
                a.isPrimary) {
                if (window.navigator.msPointerEnabled) {
                    if (10 >= Math.abs(a.clientX - this.i) && 10 >= Math.abs(a.clientY - this.j))break a
                } else for (b = 0; b < a.touches.length; b++)if (10 >= Math.abs(a.touches[b].clientX - this.i) && 10 >= Math.abs(a.touches[b].clientY - this.j))break a;
                this.reset();
                eb(this)
            }
            break;
        case "touchend":
        case "pointerup":
            ib(this, a);
            break;
        case "click":
            ib(this, a)
    }
};
function ib(a, b) {
    b.stopPropagation && b.stopPropagation();
    a.reset();
    Wa.pop();
    var d = a.b, c = d.id.indexOf("___"), e = d.id.substring(c + 3), c = Sa[d.id.substring(0, c)];
    switch (e) {
        case "caps":
            c.H || (c.G = !c.G, d.className = "cfca-btn cfca-mod", jb(c));
            break;
        case "del":
            d.className = "cfca-btn cfca-mod";
            void 0 != c.f && (e = document.getElementById(c.f), void 0 != e && void 0 !== e.value && (0 >= ca(c.f) ? e.value = "" : (e.value = e.value.substring(0, e.value.length - 1), c = c.f, "undefined" !== typeof r[c] && (e = "", 0 < r[c].h.length && (e = ea(B(r[c].I, r[c].h,
                0, r[c].J, 0)), 1 < e.length ? (e = e.substr(0, e.length - 1), r[c].h = B(r[c].I, e, 1, r[c].J, 1)) : r[c].h = "")))));
            break;
        case "sp":
            d.className = "cfca-btn cfca-mod";
            c.H = !c.H;
            jb(c);
            break;
        case "done":
            d.className = "cfca-btn cfca-mod";
            kb(c, !1);
            void 0 != c.qa && c.qa(c.f);
            break;
        default:
            if (!isNaN(parseInt(e))) {
                e = c.G ? d.value.toUpperCase() : d.value.toLowerCase();
                if (void 0 === e || 0 === e.length)break;
                d.className = "cfca-btn cfca-default";
                void 0 !== c.f && (d = document.getElementById(c.f), void 0 != d && void 0 !== d.value && (0 == ca(c.f) && (d.value = ""),
                    d.value.length >= c.va() || e && 0 == e.length || (lb(c), 0 < d.value.length && "*" != d.value.charAt(d.value.length - 1) && (d.value = d.value.substring(0, d.value.length - 1).concat("*")), mb(c, d), d.value += e, c = c.f, "undefined" !== typeof r[c] && (d = "", 0 < r[c].h.length && (d = ea(B(r[c].I, r[c].h, 0, r[c].J, 0))), r[c].h = B(r[c].I, d + e, 1, r[c].J, 1)))));
                "object" === typeof K.b && (fb(), delete K.b)
            }
    }
    if ("touchend" == b.type || "pointerup" == b.type)cb.push(a.i, a.j), window.setTimeout(nb, 2500)
}
db.prototype.reset = function () {
    bb(this.b, Za, this);
    bb(document.body, Xa, this)
};
function nb() {
    cb.splice(0, 2)
}
document.addEventListener("click", function (a) {
    for (var b = 0; b < cb.length; b += 2) {
        var d = cb[b + 1];
        25 > Math.abs(a.clientX - cb[b]) && 25 > Math.abs(a.clientY - d) && (a.stopPropagation && a.stopPropagation(), a.preventDefault && a.preventDefault())
    }
}, !0);
var ob = "1234567890qwertyuiopasdfghjklzxcvbnm".split(""), pb = "1234567890QWERTYUIOPASDFGHJKLZXCVBNM".split(""), qb = "! @ # $ % ^ & * ( ) - _ + { } [ ] < > : ; \" ' , . ? = / \\   | ~ `  ".split(" "), rb = "1234567890".split("");
function gb(a) {
    if (void 0 == a)return null;
    for (var b = {x: 0, y: 0, ja: 0, L: 0}, d = a, c = 0, e = 0; d;)c += d.offsetLeft, e += d.offsetTop, d = d.offsetParent;
    b.x = c;
    b.y = e;
    b.ja = a.offsetWidth;
    b.L = a.offsetHeight;
    return b
}
function sb(a, b, d) {
    a.K = void 0 != a.j;
    var c = a.K ? a.j : document.getElementById(b);
    if (!a.K) {
        var e = -1 !== Va.indexOf("ipad") || -1 === Va.indexOf("windows") && -1 !== Va.indexOf("android") && -1 === Va.indexOf("mobile") ? .0625 : .125;
        a.G = !1;
        a.H = !1;
        a.ka = !1;
        a.ia = [];
        a.i = [];
        a.b = {};
        a.T = void 0 === d || d === KEYBOARD_TYPE_COMPLETE ? KEYBOARD_TYPE_COMPLETE : KEYBOARD_TYPE_DIGITAL;
        a.j = c;
        a.j.className = "cfca-keyboard";
        a.Z = Math.floor(e * Math.min(Ua, Ta));
        a.aa = Math.floor(a.Z);
        a.Ka = Math.floor(14 * a.Z / 25);
        a.La = Math.floor(14 * a.aa / 25);
        a.f = void 0;
        Sa[b] =
            a
    }
}
function tb(a) {
    var b = a.K ? a.j.childNodes[1] : document.createElement("DIV");
    a.K || (a.j.appendChild(b), b.style.position = "relative", b.style.top = "0px", b.style.left = "0px");
    return b
}
function ub(a, b, d) {
    a = a.j.id + d;
    d = document.createElement("SPAN");
    b.appendChild(d);
    b = document.createElement("INPUT");
    d.appendChild(b);
    b.id = a;
    new db(b);
    return b
}
function vb(a, b, d) {
    b = ub(a, b, d);
    b.parentNode.className = "col";
    b.parentNode.style.height = a.Z + "px";
    b.className = "cfca-btn cfca-default";
    b.setAttribute("type", "button");
    b.style.height = a.Z + "px";
    b.style.fontSize = a.Ka + "px";
    return b
}
function wb(a, b, d, c) {
    var e = document.createElement("DIV");
    e.className = "cfca-row";
    b.appendChild(e);
    for (b = d; b < c; b++)a.i[b] = vb(a, e, "___" + String(b))
}
function xb(a, b, d, c, e) {
    a = vb(a, b, d);
    a.parentNode.style.width = c;
    a.value = e;
    return a
}
function yb(a, b) {
    var d = a.K ? b.childNodes[0] : document.createElement("DIV");
    if (!a.K) {
        b.appendChild(d);
        wb(a, d, 0, 10);
        wb(a, d, 10, 20);
        wb(a, d, 20, 29);
        a.i[20].parentNode.style.marginLeft = "5.625%";
        var c = document.createElement("DIV");
        d.appendChild(c);
        c.className = "cfca-row";
        a.b.caps = xb(a, c, "___caps", "13.75%", "\u21e7");
        a.b.caps.className = "cfca-btn cfca-mod";
        for (var e = 29; 36 > e; e++)a.i[e] = vb(a, c, "___" + String(e));
        a.b.del = xb(a, c, "___del", "13.75%", "\u2190");
        a.b.del.className = "cfca-btn cfca-mod";
        c = document.createElement("DIV");
        d.appendChild(c);
        c.className = "cfca-row";
        a.b.sp = xb(a, c, "___sp", "18.75%", "#+=");
        a.b.sp.className = "cfca-btn cfca-mod";
        a.b.sp.parentNode.style.marginBottom = "1%";
        a.b.space = xb(a, c, "___space", "58.75%", "");
        a.b.space.className = "cfca-btn cfca-space";
        a.b.space.parentNode.style.marginBottom = "1%";
        a.b.done = xb(a, c, "___done", "18.75%", "\u5b8c\u6210");
        a.b.done.className = "cfca-btn cfca-mod";
        a.b.done.parentNode.style.marginBottom = "1%"
    }
}
function zb(a, b, d) {
    b = ub(a, b, d);
    b.parentNode.className = "col-pad";
    b.parentNode.style.height = a.aa + "px";
    b.className = "cfca-btn cfca-default";
    b.setAttribute("type", "button");
    b.style.height = a.aa + "px";
    b.style.fontSize = a.La + "px";
    return b
}
function Ab(a, b, d, c) {
    var e = document.createElement("DIV");
    e.className = "cfca-row";
    b.appendChild(e);
    for (b = d; b < c; b++)a.ia[b - 100] = zb(a, e, "___" + String(b))
}
function Bb(a, b) {
    var d = a.T == KEYBOARD_TYPE_COMPLETE ? 1 : 0, d = a.K ? b.childNodes[d] : document.createElement("DIV");
    if (!a.K) {
        b.appendChild(d);
        Ab(a, d, 100, 103);
        Ab(a, d, 103, 106);
        Ab(a, d, 106, 109);
        var c = document.createElement("DIV");
        c.className = "cfca-row";
        d.appendChild(c);
        a.b.del = zb(a, c, "___del");
        a.b.del.value = "\u2190";
        a.b.del.className = "cfca-btn cfca-mod";
        a.ia[9] = zb(a, c, "___109");
        a.b.done = zb(a, c, "___done");
        a.b.done.value = "\u5b8c\u6210";
        a.b.done.className = "cfca-btn cfca-mod"
    }
}
function Ra(a, b, d) {
    sb(a, b, d);
    b = tb(a);
    ab(b, "selectstart", function () {
        return!1
    });
    ab(b, Xa, $a);
    ab(b, Ya, $a);
    a.T == KEYBOARD_TYPE_DIGITAL ? Bb(a, b) : yb(a, b)
}
function Cb(a, b, d) {
    a:{
        var c = void 0, c = 0;
        if (58 > b)c = Math.floor(b / 2), c = a.i[c], c.parentNode.style.width = a.la + "%"; else if (58 == b || 59 == b)c = a.b.caps, c.parentNode.style.width = a.na + "%"; else if (59 < b && 74 > b)c = Math.floor(b / 2) - 1, c = a.i[c], c.parentNode.style.width = a.la + "%"; else if (74 == b || 75 == b)c = a.b.del, c.parentNode.style.width = a.na + "%"; else if (76 == b || 77 == b)c = a.b.sp, c.parentNode.style.width = a.ma + "%"; else if (78 == b || 79 == b)c = a.b.space, c.parentNode.style.width = a.Va + "%"; else if (80 == b || 81 == b)c = a.b.done, c.parentNode.style.width =
            a.ma + "%"; else {
            a = void 0;
            break a
        }
        a = c
    }
    void 0 != a && (0 == b % 2 ? (40 == b && (d += 5), a.parentNode.style.marginLeft = d + "%") : (57 == b && (d += 5), a.parentNode.style.marginRight = d + "%"))
}
function Db(a, b, d, c) {
    for (var e = 0; b < d; b++)e += c, 1 < e ? (--e, Cb(a, b, a.Ua)) : Cb(a, b, a.Ta)
}
function jb(a) {
    if (a.T === KEYBOARD_TYPE_COMPLETE) {
        a.H ? (a.G = !1, a.b.sp.value = "abc") : a.b.sp.value = "#+=";
        for (var b = a.H ? qb : a.G ? pb : ob, d = b.length - 1; 0 <= d; d--)a.i[d].value = b[d], a.i[d].className = 0 == b[d].length ? "cfca-btn cfca-disable" : "cfca-btn cfca-default"
    } else for (b = rb.length - 1; 0 <= b; b--)a.ia[b].value = rb[b];
    b = Oa("Width");
    d = Oa("Height");
    if (a.T == KEYBOARD_TYPE_COMPLETE && (!a.ka || Ua != b)) {
        Ua = b;
        Ta = d;
        a.ka = !0;
        var c = .0875 * b, e = Math.floor(c), c = c - e, f = .00625 * b, g = Math.floor(f), f = f - g, h = .1375 * b, k = Math.floor(h), h = h - k, n = .1875 *
            b, w = Math.floor(n), n = n - w, t = .5875 * b, v = Math.floor(t), t = t - v;
        if (-1E-4 > f || 1E-4 < f)a.Ta = 100 * (g + .001) / b, a.Ua = 100 * (g + 1.001) / b, a.la = 100 * (e + .001) / b, a.na = 100 * (k + .001) / b, a.ma = 100 * (w + .001) / b, a.Va = 100 * (v + .001) / b, Db(a, 0, 20, f + .5 * c), Db(a, 20, 40, f + .5 * c), Db(a, 40, 58, f + .5 * c), Db(a, 58, 76, f + (7 * c + 2 * h) / 18), Db(a, 76, 82, f + (2 * n + t) / 6); else {
            for (e = 0; e < a.i.length; e++)a.i[e].parentNode.style.width = "8.75%", a.i[e].parentNode.style.marginLeft = 20 == e ? "5.625%" : "0.625%", a.i[e].parentNode.style.marginRight = "0.625%";
            c = ["caps", "del", "sp", "space",
                "done"];
            for (e = 0; e < c.length; e++)a.b[c[e]].parentNode.style.marginLeft = "0.625%", a.b[c[e]].parentNode.style.marginRight = "0.625%";
            a.b.caps.parentNode.style.width = "13.75%";
            a.b.del.parentNode.style.width = "13.75%";
            a.b.sp.parentNode.style.width = "18.75%";
            a.b.space.parentNode.style.width = "58.75%";
            a.b.done.parentNode.style.width = "18.75%"
        }
    }
    Ta != d && (Ua = b, Ta = d)
}
function kb(a, b) {
    a.j.style.display = void 0 === b || !0 === b ? "block" : "none";
    var d = document.getElementById("_INNER_BLANK_");
    if (void 0 === b || !0 === b) {
        a.H = !1;
        a.G = !1;
        jb(a);
        var c = document.getElementById(a.f);
        if (c) {
            var e = gb(c);
            if (null != e) {
                var f = a.T == KEYBOARD_TYPE_COMPLETE ? Math.floor(5 * a.Z + .06 * Ua) : Math.floor(4 * a.aa + .08 * Ua), c = document.body, g = document.documentElement, h = Math.max(c.scrollHeight, c.offsetHeight, g.clientHeight, g.scrollHeight, g.offsetHeight), c = f - (h - (e.y + e.L)), g = f - (Ta - (e.y + e.L));
                e.y + e.L + f >= h && (c += h - e.y - e.L);
                0 == c && (c = g - window.pageYOffset);
                0 <= c && (d || (d = document.createElement("DIV"), d.id = "_INNER_BLANK_", e = d, f = a.j, h = f.parentNode, h.lastChild == f ? h.appendChild(e) : h.insertBefore(e, f.nextSibling)), d.style.height = c + "px");
                g >= window.pageYOffset ? window.scrollTo(0, g) : window.scrollTo(0, window.pageYOffset)
            }
        }
    } else d && d.parentNode && d.parentNode.removeChild(d), a.G = !1
}
function lb(a) {
    "number" == typeof a.ba && (window.clearTimeout(a.ba), delete a.ba)
}
function mb(a, b) {
    a.ba = window.setTimeout(function (a) {
        a.value = a.value.substring(0, a.value.length - 1).concat("*")
    }, 1E3, b)
}
function hb(a, b, d, c, e) {
    this.i = Math.floor(a);
    this.j = Math.floor(b);
    this.width = Math.floor(d);
    this.height = Math.floor(c);
    this.b = e
}
function fb() {
    var a = document.getElementById("CFCABubble") || null;
    a && a.parentNode.removeChild(a)
}
K.prototype.cb = function () {
    kb(this, !0)
};
goog.exportProperty(K.prototype, "showKeyboard", K.prototype.cb);
K.prototype.Ra = function () {
    kb(this, !1)
};
goog.exportProperty(K.prototype, "hideKeyboard", K.prototype.Ra);
K.prototype.Sa = function () {
    return void 0 !== this.j && "block" === this.j.style.display
};
goog.exportProperty(K.prototype, "isShowing", K.prototype.Sa);
K.prototype.Ga = function (a) {
    var b, d;
    if (void 0 === a || null === a)return CFCA_ERROR_INVALID_PARAMETER;
    "string" === typeof a ? (b = a, d = document.getElementById(b) || null) : "object" === typeof a && (b = a.id, d = a);
    if (!d)return CFCA_ERROR_INVALID_PARAMETER;
    this.f = b;
    if (void 0 == r[a]) {
        a = this.f;
        b = Array(16);
        d = Array(24);
        var c = Array(8);
        Eb(b);
        Eb(d);
        Eb(c);
        r[a] = {S: b, I: ra(d), J: ra(c), P: null, Y: "", ha: PUBLIC_KEY_EBANK, maxLength: DEFAULT_MAX_LENGTH, xa: DEFAULT_MIN_LENGTH, Ba: OUTPUT_TYPE_HASH, R: CIPHER_TYPE_SM2, errorCode: CFCA_OK, h: ""}
    }
    return CFCA_OK
};
goog.exportProperty(K.prototype, "bindInputBox", K.prototype.Ga);
K.prototype.ab = function (a, b) {
    return void 0 !== b ? ka(b, a) : ka(this.f, a)
};
goog.exportProperty(K.prototype, "setPublicKeyToEncrypt", K.prototype.ab);
K.prototype.Xa = function (a) {
    "function" === typeof a && 1 === a.length && (this.qa = a)
};
goog.exportProperty(K.prototype, "setDoneCallback", K.prototype.Xa);
K.prototype.$a = function (a, b) {
    return void 0 !== b ? la(b, a) : la(this.f, a)
};
goog.exportProperty(K.prototype, "setOutputType", K.prototype.$a);
K.prototype.Wa = function (a, b) {
    return void 0 !== b ? ma(b, a) : ma(this.f, a)
};
goog.exportProperty(K.prototype, "setCipherType", K.prototype.Wa);
K.prototype.Ma = function (a) {
    return void 0 !== a ? r[a].R : r[this.f].R
};
goog.exportProperty(K.prototype, "getCipherType", K.prototype.Ma);
K.prototype.Ya = function (a, b) {
    return void 0 !== b ? ia(b, a) : ia(this.f, a)
};
goog.exportProperty(K.prototype, "setMaxLength", K.prototype.Ya);
K.prototype.va = function (a) {
    return void 0 !== a ? r[a].maxLength : r[this.f].maxLength
};
goog.exportProperty(K.prototype, "getMaxLength", K.prototype.va);
K.prototype.Za = function (a, b) {
    return void 0 !== b ? ja(b, a) : ja(this.f, a)
};
goog.exportProperty(K.prototype, "setMinLength", K.prototype.Za);
K.prototype.Qa = function (a) {
    return void 0 !== a ? getMinLength(a) : getMinLength(this.f)
};
goog.exportProperty(K.prototype, "getMinLength", K.prototype.Qa);
K.prototype.Ea = function (a, b) {
    return void 0 !== b ? na(b, a) : na(this.f, a)
};
goog.exportProperty(K.prototype, "setMatchRegex", K.prototype.Ea);
K.prototype.bb = function (a, b) {
    return void 0 !== b ? fa(b, a) : fa(this.f, a)
};
goog.exportProperty(K.prototype, "setServerRandom", K.prototype.bb);
K.prototype.Ia = function (a) {
    return void 0 !== a ? da(a) : da(this.f)
};
goog.exportProperty(K.prototype, "checkMatchRegex", K.prototype.Ia);
K.prototype.Oa = function (a) {
    return void 0 !== a ? oa(a) : oa(this.f)
};
goog.exportProperty(K.prototype, "getEncryptedInputValue", K.prototype.Oa);
K.prototype.Na = function (a) {
    return void 0 !== a ? xa(a) : xa(this.f)
};
goog.exportProperty(K.prototype, "getEncryptedClientRandom", K.prototype.Na);
K.prototype.Ha = function (a, b) {
    return Ma(a, b)
};
goog.exportProperty(K.prototype, "checkInputValueMatch", K.prototype.Ha);
K.prototype.Ja = function (a) {
    a = a || this.f;
    if (void 0 === a)return CFCA_ERROR_INVALID_SIP_HANDLE_ID;
    a === this.f && lb(this);
    var b = document.getElementById(a);
    if (void 0 == b || void 0 == b.value)return CFCA_ERROR_INVALID_SIP_HANDLE_ID;
    b.value = "";
    "undefined" === typeof r[a] ? a = CFCA_ERROR_INVALID_SIP_HANDLE_ID : (r[a].h = "", a = CFCA_OK);
    return a
};
goog.exportProperty(K.prototype, "clearInputValue", K.prototype.Ja);
K.prototype.Pa = function (a) {
    return void 0 !== a ? Na(a) : Na(this.f)
};
goog.exportProperty(K.prototype, "getErrorCode", K.prototype.Pa);
goog.exportSymbol("getCFCAKeyboardVersion", function () {
    var a;
    return a = "20810" + HTML5_SIP_VERSION.split(".").join("")
});
var L = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", Fb = "=";
function qa(a) {
    var b, d, c = "";
    for (b = 0; b + 3 <= a.length; b += 3)d = parseInt(a.substring(b, b + 3), 16), c += L.charAt(d >> 6) + L.charAt(d & 63);
    b + 1 == a.length ? (d = parseInt(a.substring(b, b + 1), 16), c += L.charAt(d << 2)) : b + 2 == a.length && (d = parseInt(a.substring(b, b + 2), 16), c += L.charAt(d >> 2) + L.charAt((d & 3) << 4));
    for (; 0 < (c.length & 3);)c += Fb;
    return c
}
function ha(a) {
    var b = "", d, c = 0, e, f;
    for (d = 0; d < a.length && a.charAt(d) != Fb; ++d)f = L.indexOf(a.charAt(d)), 0 > f || (0 == c ? (b += N.charAt(f >> 2), e = f & 3, c = 1) : 1 == c ? (b += N.charAt(e << 2 | f >> 4), e = f & 15, c = 2) : 2 == c ? (b += N.charAt(e), b += N.charAt(f >> 2), e = f & 3, c = 3) : (b += N.charAt(e << 2 | f >> 4), b += N.charAt(f & 15), c = 0));
    1 == c && (b += N.charAt(e << 2));
    return b
}
function wa(a) {
    var b, d = 0, c = "";
    for (b = 0; b + 3 <= a.length; b += 3)d = (a[b] & 255) << 16 ^ (a[b + 1] & 255) << 8 ^ a[b + 2] & 255, c += L.charAt(d >>> 18) + L.charAt((d & 258048) >>> 12) + L.charAt((d & 4032) >>> 6) + L.charAt(d & 63);
    b == a.length - 1 ? (d = a[b] & 255, c += L.charAt((d & 252) >>> 2) + L.charAt((d & 3) << 4)) : b + 1 == a.length - 1 && (d = (a[b] & 255) << 8 ^ a[b + 1] & 255, c += L.charAt((d & 64512) >>> 10) + L.charAt((d & 1008) >>> 4) + L.charAt((d & 15) << 2));
    for (; 0 < (c.length & 3);)c += Fb;
    return c
}
function B(a, b, d, c, e) {
    var f = [16843776, 0, 65536, 16843780, 16842756, 66564, 4, 65536, 1024, 16843776, 16843780, 1024, 16778244, 16842756, 16777216, 4, 1028, 16778240, 16778240, 66560, 66560, 16842752, 16842752, 16778244, 65540, 16777220, 16777220, 65540, 0, 1028, 66564, 16777216, 65536, 16843780, 4, 16842752, 16843776, 16777216, 16777216, 1024, 16842756, 65536, 66560, 16777220, 1024, 4, 16778244, 66564, 16843780, 65540, 16842752, 16778244, 16777220, 1028, 66564, 16843776, 1028, 16778240, 16778240, 0, 65540, 66560, 0, 16842756], g = [-2146402272, -2147450880, 32768,
        1081376, 1048576, 32, -2146435040, -2147450848, -2147483616, -2146402272, -2146402304, -2147483648, -2147450880, 1048576, 32, -2146435040, 1081344, 1048608, -2147450848, 0, -2147483648, 32768, 1081376, -2146435072, 1048608, -2147483616, 0, 1081344, 32800, -2146402304, -2146435072, 32800, 0, 1081376, -2146435040, 1048576, -2147450848, -2146435072, -2146402304, 32768, -2146435072, -2147450880, 32, -2146402272, 1081376, 32, 32768, -2147483648, 32800, -2146402304, 1048576, -2147483616, 1048608, -2147450848, -2147483616, 1048608, 1081344, 0, -2147450880, 32800,
        -2147483648, -2146435040, -2146402272, 1081344], h = [520, 134349312, 0, 134348808, 134218240, 0, 131592, 134218240, 131080, 134217736, 134217736, 131072, 134349320, 131080, 134348800, 520, 134217728, 8, 134349312, 512, 131584, 134348800, 134348808, 131592, 134218248, 131584, 131072, 134218248, 8, 134349320, 512, 134217728, 134349312, 134217728, 131080, 520, 131072, 134349312, 134218240, 0, 512, 131080, 134349320, 134218240, 134217736, 512, 0, 134348808, 134218248, 131072, 134217728, 134349320, 8, 131592, 131584, 134217736, 134348800, 134218248, 520, 134348800,
        131592, 8, 134348808, 131584], k = [8396801, 8321, 8321, 128, 8396928, 8388737, 8388609, 8193, 0, 8396800, 8396800, 8396929, 129, 0, 8388736, 8388609, 1, 8192, 8388608, 8396801, 128, 8388608, 8193, 8320, 8388737, 1, 8320, 8388736, 8192, 8396928, 8396929, 129, 8388736, 8388609, 8396800, 8396929, 129, 0, 0, 8396800, 8320, 8388736, 8388737, 1, 8396801, 8321, 8321, 128, 8396929, 129, 1, 8192, 8388609, 8193, 8396928, 8388737, 8193, 8320, 8388608, 8396801, 128, 8388608, 8192, 8396928], n = [256, 34078976, 34078720, 1107296512, 524288, 256, 1073741824, 34078720, 1074266368, 524288, 33554688,
        1074266368, 1107296512, 1107820544, 524544, 1073741824, 33554432, 1074266112, 1074266112, 0, 1073742080, 1107820800, 1107820800, 33554688, 1107820544, 1073742080, 0, 1107296256, 34078976, 33554432, 1107296256, 524544, 524288, 1107296512, 256, 33554432, 1073741824, 34078720, 1107296512, 1074266368, 33554688, 1073741824, 1107820544, 34078976, 1074266368, 256, 33554432, 1107820544, 1107820800, 524544, 1107296256, 1107820800, 34078720, 0, 1074266112, 1107296256, 524544, 33554688, 1073742080, 524288, 0, 1074266112, 34078976, 1073742080], w = [536870928, 541065216,
        16384, 541081616, 541065216, 16, 541081616, 4194304, 536887296, 4210704, 4194304, 536870928, 4194320, 536887296, 536870912, 16400, 0, 4194320, 536887312, 16384, 4210688, 536887312, 16, 541065232, 541065232, 0, 4210704, 541081600, 16400, 4210688, 541081600, 536870912, 536887296, 16, 541065232, 4210688, 541081616, 4194304, 16400, 536870928, 4194304, 536887296, 536870912, 16400, 536870928, 541081616, 4210688, 541065216, 4210704, 541081600, 0, 541065232, 16, 16384, 541065216, 4210704, 16384, 4194320, 536887312, 0, 541081600, 536870912, 4194320, 536887312], t = [2097152,
        69206018, 67110914, 0, 2048, 67110914, 2099202, 69208064, 69208066, 2097152, 0, 67108866, 2, 67108864, 69206018, 2050, 67110912, 2099202, 2097154, 67110912, 67108866, 69206016, 69208064, 2097154, 69206016, 2048, 2050, 69208066, 2099200, 2, 67108864, 2099200, 67108864, 2099200, 2097152, 67110914, 67110914, 69206018, 69206018, 2, 2097154, 67108864, 67110912, 2097152, 69208064, 2050, 2099202, 69208064, 2050, 67108866, 69208066, 69206016, 2099200, 0, 2, 69208066, 0, 2099202, 69206016, 2048, 67108866, 67110912, 2048, 2097154], v = [268439616, 4096, 262144, 268701760, 268435456,
        268439616, 64, 268435456, 262208, 268697600, 268701760, 266240, 268701696, 266304, 4096, 64, 268697600, 268435520, 268439552, 4160, 266240, 262208, 268697664, 268701696, 4160, 0, 0, 268697664, 268435520, 268439552, 266304, 262144, 266304, 262144, 268701696, 4096, 64, 268697664, 4096, 266304, 268439552, 64, 268435520, 268697600, 268697664, 268435456, 262144, 268439616, 0, 268701760, 262208, 268435520, 268697600, 268439552, 268439616, 0, 268701760, 266240, 266240, 4160, 4160, 262208, 268435456, 268701696];
    pc2bytes0 = [0, 4, 536870912, 536870916, 65536, 65540, 536936448,
        536936452, 512, 516, 536871424, 536871428, 66048, 66052, 536936960, 536936964];
    pc2bytes1 = [0, 1, 1048576, 1048577, 67108864, 67108865, 68157440, 68157441, 256, 257, 1048832, 1048833, 67109120, 67109121, 68157696, 68157697];
    pc2bytes2 = [0, 8, 2048, 2056, 16777216, 16777224, 16779264, 16779272, 0, 8, 2048, 2056, 16777216, 16777224, 16779264, 16779272];
    pc2bytes3 = [0, 2097152, 134217728, 136314880, 8192, 2105344, 134225920, 136323072, 131072, 2228224, 134348800, 136445952, 139264, 2236416, 134356992, 136454144];
    pc2bytes4 = [0, 262144, 16, 262160, 0, 262144, 16, 262160,
        4096, 266240, 4112, 266256, 4096, 266240, 4112, 266256];
    pc2bytes5 = [0, 1024, 32, 1056, 0, 1024, 32, 1056, 33554432, 33555456, 33554464, 33555488, 33554432, 33555456, 33554464, 33555488];
    pc2bytes6 = [0, 268435456, 524288, 268959744, 2, 268435458, 524290, 268959746, 0, 268435456, 524288, 268959744, 2, 268435458, 524290, 268959746];
    pc2bytes7 = [0, 65536, 2048, 67584, 536870912, 536936448, 536872960, 536938496, 131072, 196608, 133120, 198656, 537001984, 537067520, 537004032, 537069568];
    pc2bytes8 = [0, 262144, 0, 262144, 2, 262146, 2, 262146, 33554432, 33816576, 33554432,
        33816576, 33554434, 33816578, 33554434, 33816578];
    pc2bytes9 = [0, 268435456, 8, 268435464, 0, 268435456, 8, 268435464, 1024, 268436480, 1032, 268436488, 1024, 268436480, 1032, 268436488];
    pc2bytes10 = [0, 32, 0, 32, 1048576, 1048608, 1048576, 1048608, 8192, 8224, 8192, 8224, 1056768, 1056800, 1056768, 1056800];
    pc2bytes11 = [0, 16777216, 512, 16777728, 2097152, 18874368, 2097664, 18874880, 67108864, 83886080, 67109376, 83886592, 69206016, 85983232, 69206528, 85983744];
    pc2bytes12 = [0, 4096, 134217728, 134221824, 524288, 528384, 134742016, 134746112, 16, 4112, 134217744,
        134221840, 524304, 528400, 134742032, 134746128];
    pc2bytes13 = [0, 4, 256, 260, 0, 4, 256, 260, 1, 5, 257, 261, 1, 5, 257, 261];
    for (var x = 8 < a.length ? 3 : 1, u = Array(32 * x), C = [0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0], y, z, M = 0, Z = 0, A, ga = 0; ga < x; ga++) {
        left = a.charCodeAt(M++) << 24 | a.charCodeAt(M++) << 16 | a.charCodeAt(M++) << 8 | a.charCodeAt(M++);
        right = a.charCodeAt(M++) << 24 | a.charCodeAt(M++) << 16 | a.charCodeAt(M++) << 8 | a.charCodeAt(M++);
        A = (left >>> 4 ^ right) & 252645135;
        right ^= A;
        left ^= A << 4;
        A = (right >>> -16 ^ left) & 65535;
        left ^= A;
        right ^= A << -16;
        A = (left >>> 2 ^ right) &
            858993459;
        right ^= A;
        left ^= A << 2;
        A = (right >>> -16 ^ left) & 65535;
        left ^= A;
        right ^= A << -16;
        A = (left >>> 1 ^ right) & 1431655765;
        right ^= A;
        left ^= A << 1;
        A = (right >>> 8 ^ left) & 16711935;
        left ^= A;
        right ^= A << 8;
        A = (left >>> 1 ^ right) & 1431655765;
        right ^= A;
        left ^= A << 1;
        A = left << 8 | right >>> 20 & 240;
        left = right << 24 | right << 8 & 16711680 | right >>> 8 & 65280 | right >>> 24 & 240;
        right = A;
        for (var p = 0; p < C.length; p++)C[p] ? (left = left << 2 | left >>> 26, right = right << 2 | right >>> 26) : (left = left << 1 | left >>> 27, right = right << 1 | right >>> 27), left &= -15, right &= -15, y = pc2bytes0[left >>> 28] |
            pc2bytes1[left >>> 24 & 15] | pc2bytes2[left >>> 20 & 15] | pc2bytes3[left >>> 16 & 15] | pc2bytes4[left >>> 12 & 15] | pc2bytes5[left >>> 8 & 15] | pc2bytes6[left >>> 4 & 15], z = pc2bytes7[right >>> 28] | pc2bytes8[right >>> 24 & 15] | pc2bytes9[right >>> 20 & 15] | pc2bytes10[right >>> 16 & 15] | pc2bytes11[right >>> 12 & 15] | pc2bytes12[right >>> 8 & 15] | pc2bytes13[right >>> 4 & 15], A = (z >>> 16 ^ y) & 65535, u[Z++] = y ^ A, u[Z++] = z ^ A << 16
    }
    a = 0;
    var q, Ub, ua, Vb, Wb, Xb, C = b.length;
    y = 0;
    z = 32 == u.length ? 3 : 9;
    x = 3 == z ? d ? [0, 32, 2] : [30, -2, -2] : d ? [0, 32, 2, 62, 30, -2, 64, 96, 2] : [94, 62, -2, 32, 64, 2, 30, -2,
        -2];
    2 == e ? b += "        " : 1 == e ? (e = 8 - C % 8, b += String.fromCharCode(e, e, e, e, e, e, e, e), 8 == e && (C += 8)) : e || (b += "\x00\x00\x00\x00\x00\x00\x00\x00");
    tempresult = result = "";
    M = c.charCodeAt(a++) << 24 | c.charCodeAt(a++) << 16 | c.charCodeAt(a++) << 8 | c.charCodeAt(a++);
    ua = c.charCodeAt(a++) << 24 | c.charCodeAt(a++) << 16 | c.charCodeAt(a++) << 8 | c.charCodeAt(a++);
    for (a = 0; a < C;) {
        p = b.charCodeAt(a++) << 24 | b.charCodeAt(a++) << 16 | b.charCodeAt(a++) << 8 | b.charCodeAt(a++);
        q = b.charCodeAt(a++) << 24 | b.charCodeAt(a++) << 16 | b.charCodeAt(a++) << 8 | b.charCodeAt(a++);
        d ? (p ^= M, q ^= ua) : (Ub = M, Vb = ua, M = p, ua = q);
        e = (p >>> 4 ^ q) & 252645135;
        q ^= e;
        p ^= e << 4;
        e = (p >>> 16 ^ q) & 65535;
        q ^= e;
        p ^= e << 16;
        e = (q >>> 2 ^ p) & 858993459;
        p ^= e;
        q ^= e << 2;
        e = (q >>> 8 ^ p) & 16711935;
        p ^= e;
        q ^= e << 8;
        e = (p >>> 1 ^ q) & 1431655765;
        q ^= e;
        p ^= e << 1;
        p = p << 1 | p >>> 31;
        q = q << 1 | q >>> 31;
        for (Z = 0; Z < z; Z += 3) {
            Wb = x[Z + 1];
            Xb = x[Z + 2];
            for (c = x[Z]; c != Wb; c += Xb)A = q ^ u[c], ga = (q >>> 4 | q << 28) ^ u[c + 1], e = p, p = q, q = e ^ (g[A >>> 24 & 63] | k[A >>> 16 & 63] | w[A >>> 8 & 63] | v[A & 63] | f[ga >>> 24 & 63] | h[ga >>> 16 & 63] | n[ga >>> 8 & 63] | t[ga & 63]);
            e = p;
            p = q;
            q = e
        }
        p = p >>> 1 | p << 31;
        q = q >>> 1 | q << 31;
        e = (p >>> 1 ^ q) & 1431655765;
        q ^= e;
        p ^= e << 1;
        e = (q >>> 8 ^ p) & 16711935;
        p ^= e;
        q ^= e << 8;
        e = (q >>> 2 ^ p) & 858993459;
        p ^= e;
        q ^= e << 2;
        e = (p >>> 16 ^ q) & 65535;
        q ^= e;
        p ^= e << 16;
        e = (p >>> 4 ^ q) & 252645135;
        q ^= e;
        p ^= e << 4;
        d ? (M = p, ua = q) : (p ^= Ub, q ^= Vb);
        tempresult += String.fromCharCode(p >>> 24, p >>> 16 & 255, p >>> 8 & 255, p & 255, q >>> 24, q >>> 16 & 255, q >>> 8 & 255, q & 255);
        y += 8;
        512 == y && (result += tempresult, tempresult = "", y = 0)
    }
    return result + tempresult
}
function ea(a) {
    return a.substr(0, a.length - a.charCodeAt(a.length - 1))
}
function ra(a) {
    for (var b = "", d = 0; d < a.length; d++)b += String.fromCharCode(a[d]);
    return b
}
function Gb(a, b) {
    this.x = b;
    this.q = a
}
l = Gb.prototype;
l.l = function (a) {
    return a == this ? !0 : this.q.l(a.q) && this.x.l(a.x)
};
l.N = function () {
    return new Gb(this.q, G(this.x.N(), this.q))
};
l.add = function (a) {
    return new Gb(this.q, G(this.x.add(a.x), this.q))
};
l.m = function (a) {
    return new Gb(this.q, G(this.x.m(a.x), this.q))
};
l.multiply = function (a) {
    return new Gb(this.q, G(this.x.multiply(a.x), this.q))
};
l.F = function () {
    return new Gb(this.q, G(this.x.F(), this.q))
};
l.sa = function (a) {
    return new Gb(this.q, G(this.x.multiply(Hb(a.x, this.q)), this.q))
};
function H(a, b, d, c) {
    this.curve = a;
    this.x = b;
    this.y = d;
    this.z = null == c ? F : c;
    this.V = null
}
function Fa(a) {
    null == a.V && (a.V = Hb(a.z, a.curve.q));
    var b = a.x.x.multiply(a.V);
    a.curve.reduce(b);
    return I(a.curve, b)
}
function Ha(a) {
    null == a.V && (a.V = Hb(a.z, a.curve.q));
    var b = a.y.x.multiply(a.V);
    a.curve.reduce(b);
    return I(a.curve, b)
}
H.prototype.l = function (a) {
    return a == this ? !0 : J(this) ? J(a) : J(a) ? J(this) : G(a.y.x.multiply(this.z).m(this.y.x.multiply(a.z)), this.curve.q).l(O) ? G(a.x.x.multiply(this.z).m(this.x.x.multiply(a.z)), this.curve.q).l(O) : !1
};
function J(a) {
    return null == a.x && null == a.y ? !0 : a.z.l(O) && !a.y.x.l(O)
}
H.prototype.N = function () {
    return new H(this.curve, this.x, this.y.N(), this.z)
};
H.prototype.add = function (a) {
    if (J(this))return a;
    if (J(a))return this;
    var b = G(a.y.x.multiply(this.z).m(this.y.x.multiply(a.z)), this.curve.q), d = G(a.x.x.multiply(this.z).m(this.x.x.multiply(a.z)), this.curve.q);
    if (O.l(d))return O.l(b) ? Ib(this) : this.curve.ga;
    var c = new m("3"), e = this.x.x, f = this.y.x, g = d.F(), h = g.multiply(d), e = e.multiply(g), g = b.F().multiply(this.z), d = G(g.m(e.shiftLeft(1)).multiply(a.z).m(h).multiply(d), this.curve.q), b = G(e.multiply(c).multiply(b).m(f.multiply(h)).m(g.multiply(b)).multiply(a.z).add(b.multiply(h)),
        this.curve.q);
    a = G(h.multiply(this.z).multiply(a.z), this.curve.q);
    return new H(this.curve, I(this.curve, d), I(this.curve, b), a)
};
function Ib(a) {
    if (J(a))return a;
    if (0 == Jb(a.y.x))return a.curve.ga;
    var b = new m("3"), d = a.x.x, c = a.y.x, e = c.multiply(a.z), f = G(e.multiply(c), a.curve.q), c = a.curve.ca.x, g = d.F().multiply(b);
    O.l(c) || (g = g.add(a.z.F().multiply(c)));
    g = G(g, a.curve.q);
    c = G(g.F().m(d.shiftLeft(3).multiply(f)).shiftLeft(1).multiply(e), a.curve.q);
    b = G(g.multiply(b).multiply(d).m(f.shiftLeft(1)).shiftLeft(2).multiply(f).m(g.F().multiply(g)), a.curve.q);
    e = G(e.F().multiply(e).shiftLeft(3), a.curve.q);
    return new H(a.curve, I(a.curve, c), I(a.curve,
        b), e)
}
H.prototype.multiply = function (a) {
    if (J(this))return this;
    if (0 == Jb(a))return this.curve.ga;
    var b = a.multiply(new m("3")), d = this.N(), c = this, e;
    for (e = za(b) - 2; 0 < e; --e) {
        var c = Ib(c), f = Kb(b, e), g = Kb(a, e);
        f != g && (c = c.add(f ? this : d))
    }
    return c
};
function Ea(a, b, d) {
    this.q = a;
    this.ca = I(this, b);
    this.ra = I(this, d);
    this.ga = new H(this, null, null);
    this.jb = new Lb(this.q)
}
Ea.prototype.l = function (a) {
    return a == this ? !0 : this.q.l(a.q) && this.ca.l(a.ca) && this.ra.l(a.ra)
};
function I(a, b) {
    return new Gb(a.q, b)
}
Ea.prototype.reduce = function (a) {
    this.jb.reduce(a)
};
var Mb;
function m(a, b, d) {
    null != a && ("number" == typeof a ? Nb(this, a, b, d) : null == b && "string" != typeof a ? Ob(this, a, 256) : Ob(this, a, b))
}
function P() {
    return new m(null)
}
function Pb(a, b, d, c, e, f) {
    for (; 0 <= --f;) {
        var g = b * this[a++] + d[c] + e;
        e = Math.floor(g / 67108864);
        d[c++] = g & 67108863
    }
    return e
}
function Qb(a, b, d, c, e, f) {
    var g = b & 32767;
    for (b = b >> 15; 0 <= --f;) {
        var h = this[a] & 32767, k = this[a++] >> 15, n = b * h + k * g, h = g * h + ((n & 32767) << 15) + d[c] + (e & 1073741823);
        e = (h >>> 30) + (n >>> 15) + b * k + (e >>> 30);
        d[c++] = h & 1073741823
    }
    return e
}
function Rb(a, b, d, c, e, f) {
    var g = b & 16383;
    for (b = b >> 14; 0 <= --f;) {
        var h = this[a] & 16383, k = this[a++] >> 14, n = b * h + k * g, h = g * h + ((n & 16383) << 14) + d[c] + e;
        e = (h >> 28) + (n >> 14) + b * k;
        d[c++] = h & 268435455
    }
    return e
}
"Microsoft Internet Explorer" == navigator.appName ? (m.prototype.w = Qb, Mb = 30) : "Netscape" != navigator.appName ? (m.prototype.w = Pb, Mb = 26) : (m.prototype.w = Rb, Mb = 28);
l = m.prototype;
l.c = Mb;
l.u = (1 << Mb) - 1;
l.o = 1 << Mb;
l.Fa = Math.pow(2, 52);
l.oa = 52 - Mb;
l.pa = 2 * Mb - 52;
var N = "0123456789abcdefghijklmnopqrstuvwxyz", Sb = [], Tb, Q;
Tb = 48;
for (Q = 0; 9 >= Q; ++Q)Sb[Tb++] = Q;
Tb = 97;
for (Q = 10; 36 > Q; ++Q)Sb[Tb++] = Q;
Tb = 65;
for (Q = 10; 36 > Q; ++Q)Sb[Tb++] = Q;
function Yb(a, b) {
    var d = Sb[a.charCodeAt(b)];
    return null == d ? -1 : d
}
function Zb(a) {
    var b = P();
    $b(b, a);
    return b
}
function ac(a) {
    var b = 1, d;
    0 != (d = a >>> 16) && (a = d, b += 16);
    0 != (d = a >> 8) && (a = d, b += 8);
    0 != (d = a >> 4) && (a = d, b += 4);
    0 != (d = a >> 2) && (a = d, b += 2);
    0 != a >> 1 && (b += 1);
    return b
}
function bc(a) {
    this.g = a
}
l = bc.prototype;
l.W = function (a) {
    return 0 > a.a || 0 <= R(a, this.g) ? G(a, this.g) : a
};
l.$ = function (a) {
    return a
};
l.reduce = function (a) {
    cc(a, this.g, null, a)
};
l.U = function (a, b, d) {
    dc(a, b, d);
    this.reduce(d)
};
l.D = function (a, b) {
    ec(a, b);
    this.reduce(b)
};
function fc(a) {
    this.g = a;
    var b;
    if (1 > a.t)b = 0; else if (b = a[0], 0 == (b & 1))b = 0; else {
        var d = b & 3, d = d * (2 - (b & 15) * d) & 15, d = d * (2 - (b & 255) * d) & 255, d = d * (2 - ((b & 65535) * d & 65535)) & 65535, d = d * (2 - b * d % a.o) % a.o;
        b = 0 < d ? a.o - d : -d
    }
    this.ya = b;
    this.za = this.ya & 32767;
    this.gb = this.ya >> 15;
    this.lb = (1 << a.c - 15) - 1;
    this.hb = 2 * a.t
}
l = fc.prototype;
l.W = function (a) {
    var b = P();
    gc(a.abs(), this.g.t, b);
    cc(b, this.g, null, b);
    0 > a.a && 0 < R(b, O) && S(this.g, b, b);
    return b
};
l.$ = function (a) {
    var b = P();
    a.copyTo(b);
    this.reduce(b);
    return b
};
l.reduce = function (a) {
    for (; a.t <= this.hb;)a[a.t++] = 0;
    for (var b = 0; b < this.g.t; ++b) {
        var d = a[b] & 32767, c = d * this.za + ((d * this.gb + (a[b] >> 15) * this.za & this.lb) << 15) & a.u, d = b + this.g.t;
        for (a[d] += this.g.w(0, c, a, b, 0, this.g.t); a[d] >= a.o;)a[d] -= a.o, a[++d]++
    }
    T(a);
    hc(a, this.g.t, a);
    0 <= R(a, this.g) && S(a, this.g, a)
};
l.U = function (a, b, d) {
    dc(a, b, d);
    this.reduce(d)
};
l.D = function (a, b) {
    ec(a, b);
    this.reduce(b)
};
l = m.prototype;
l.copyTo = function (a) {
    for (var b = this.t - 1; 0 <= b; --b)a[b] = this[b];
    a.t = this.t;
    a.a = this.a
};
function $b(a, b) {
    a.t = 1;
    a.a = 0 > b ? -1 : 0;
    0 < b ? a[0] = b : -1 > b ? a[0] = b + a.o : a.t = 0
}
function Ob(a, b, d) {
    if (16 == d)d = 4; else if (8 == d)d = 3; else if (256 == d)d = 8; else if (2 == d)d = 1; else if (32 == d)d = 5; else if (4 == d)d = 2; else {
        $b(a, 0);
        null == d && (d = 10);
        for (var c = Math.floor(Math.LN2 * a.c / Math.log(d)), e = Math.pow(d, c), f = !1, g = 0, h = 0, k = 0; k < b.length; ++k) {
            var n = Yb(b, k);
            0 > n ? "-" == b.charAt(k) && 0 == Jb(a) && (f = !0) : (h = d * h + n, ++g >= c && (ic(a, e), jc(a, h, 0), h = g = 0))
        }
        0 < g && (ic(a, Math.pow(d, g)), jc(a, h, 0));
        f && S(O, a, a);
        return
    }
    a.t = 0;
    a.a = 0;
    c = b.length;
    e = !1;
    for (f = 0; 0 <= --c;)g = 8 == d ? b[c] & 255 : Yb(b, c), 0 > g ? "-" == b.charAt(c) && (e = !0) : (e = !1,
            0 == f ? a[a.t++] = g : f + d > a.c ? (a[a.t - 1] |= (g & (1 << a.c - f) - 1) << f, a[a.t++] = g >> a.c - f) : a[a.t - 1] |= g << f, f += d, f >= a.c && (f -= a.c));
    8 == d && 0 != (b[0] & 128) && (a.a = -1, 0 < f && (a[a.t - 1] |= (1 << a.c - f) - 1 << f));
    T(a);
    e && S(O, a, a)
}
function T(a) {
    for (var b = a.a & a.u; 0 < a.t && a[a.t - 1] == b;)--a.t
}
function gc(a, b, d) {
    var c;
    for (c = a.t - 1; 0 <= c; --c)d[c + b] = a[c];
    for (c = b - 1; 0 <= c; --c)d[c] = 0;
    d.t = a.t + b;
    d.a = a.a
}
function hc(a, b, d) {
    for (var c = b; c < a.t; ++c)d[c - b] = a[c];
    d.t = Math.max(a.t - b, 0);
    d.a = a.a
}
function kc(a, b, d) {
    var c = b % a.c, e = a.c - c, f = (1 << e) - 1;
    b = Math.floor(b / a.c);
    var g = a.a << c & a.u, h;
    for (h = a.t - 1; 0 <= h; --h)d[h + b + 1] = a[h] >> e | g, g = (a[h] & f) << c;
    for (h = b - 1; 0 <= h; --h)d[h] = 0;
    d[b] = g;
    d.t = a.t + b + 1;
    d.a = a.a;
    T(d)
}
function lc(a, b, d) {
    d.a = a.a;
    var c = Math.floor(b / a.c);
    if (c >= a.t)d.t = 0; else {
        b = b % a.c;
        var e = a.c - b, f = (1 << b) - 1;
        d[0] = a[c] >> b;
        for (var g = c + 1; g < a.t; ++g)d[g - c - 1] |= (a[g] & f) << e, d[g - c] = a[g] >> b;
        0 < b && (d[a.t - c - 1] |= (a.a & f) << e);
        d.t = a.t - c;
        T(d)
    }
}
function S(a, b, d) {
    for (var c = 0, e = 0, f = Math.min(b.t, a.t); c < f;)e += a[c] - b[c], d[c++] = e & a.u, e >>= a.c;
    if (b.t < a.t) {
        for (e -= b.a; c < a.t;)e += a[c], d[c++] = e & a.u, e >>= a.c;
        e += a.a
    } else {
        for (e += a.a; c < b.t;)e -= b[c], d[c++] = e & a.u, e >>= a.c;
        e -= b.a
    }
    d.a = 0 > e ? -1 : 0;
    -1 > e ? d[c++] = a.o + e : 0 < e && (d[c++] = e);
    d.t = c;
    T(d)
}
function dc(a, b, d) {
    var c = a.abs(), e = b.abs(), f = c.t;
    for (d.t = f + e.t; 0 <= --f;)d[f] = 0;
    for (f = 0; f < e.t; ++f)d[f + c.t] = c.w(0, e[f], d, f, 0, c.t);
    d.a = 0;
    T(d);
    a.a != b.a && S(O, d, d)
}
function ec(a, b) {
    for (var d = a.abs(), c = b.t = 2 * d.t; 0 <= --c;)b[c] = 0;
    for (c = 0; c < d.t - 1; ++c) {
        var e = d.w(c, d[c], b, 2 * c, 0, 1);
        (b[c + d.t] += d.w(c + 1, 2 * d[c], b, 2 * c + 1, e, d.t - c - 1)) >= d.o && (b[c + d.t] -= d.o, b[c + d.t + 1] = 1)
    }
    0 < b.t && (b[b.t - 1] += d.w(c, d[c], b, 2 * c, 0, 1));
    b.a = 0;
    T(b)
}
function cc(a, b, d, c) {
    var e = b.abs();
    if (!(0 >= e.t)) {
        var f = a.abs();
        if (f.t < e.t)null != d && $b(d, 0), null != c && a.copyTo(c); else {
            null == c && (c = P());
            var g = P(), h = a.a;
            b = b.a;
            var k = a.c - ac(e[e.t - 1]);
            0 < k ? (kc(e, k, g), kc(f, k, c)) : (e.copyTo(g), f.copyTo(c));
            e = g.t;
            f = g[e - 1];
            if (0 != f) {
                var n = f * (1 << a.oa) + (1 < e ? g[e - 2] >> a.pa : 0), w = a.Fa / n, n = (1 << a.oa) / n, t = 1 << a.pa, v = c.t, x = v - e, u = null == d ? P() : d;
                gc(g, x, u);
                0 <= R(c, u) && (c[c.t++] = 1, S(c, u, c));
                gc(F, e, u);
                for (S(u, g, g); g.t < e;)g[g.t++] = 0;
                for (; 0 <= --x;) {
                    var C = c[--v] == f ? a.u : Math.floor(c[v] * w + (c[v - 1] + t) *
                        n);
                    if ((c[v] += g.w(0, C, c, x, 0, e)) < C)for (gc(g, x, u), S(c, u, c); c[v] < --C;)S(c, u, c)
                }
                null != d && (hc(c, e, d), h != b && S(O, d, d));
                c.t = e;
                T(c);
                0 < k && lc(c, k, c);
                0 > h && S(O, c, c)
            }
        }
    }
}
function U(a) {
    return 0 == (0 < a.t ? a[0] & 1 : a.a)
}
l.exp = function (a, b) {
    if (4294967295 < a || 1 > a)return F;
    var d = P(), c = P(), e = b.W(this), f = ac(a) - 1;
    for (e.copyTo(d); 0 <= --f;)if (b.D(d, c), 0 < (a & 1 << f))b.U(c, e, d); else var g = d, d = c, c = g;
    return b.$(d)
};
l.toString = function (a) {
    if (0 > this.a)return"-" + this.N().toString(a);
    if (16 == a)a = 4; else if (8 == a)a = 3; else if (2 == a)a = 1; else if (32 == a)a = 5; else if (4 == a)a = 2; else {
        var b;
        b = a;
        null == b && (b = 10);
        if (0 == Jb(this) || 2 > b || 36 < b)b = "0"; else {
            a = Math.floor(Math.LN2 * this.c / Math.log(b));
            a = Math.pow(b, a);
            var d = Zb(a), c = P(), e = P(), f = "";
            for (cc(this, d, c, e); 0 < Jb(c);)f = (a + aa(e)).toString(b).substr(1) + f, cc(c, d, c, e);
            b = aa(e).toString(b) + f
        }
        return b
    }
    var d = (1 << a) - 1, c = !1, e = "", f = this.t, g = this.c - f * this.c % a;
    if (0 < f--)for (g < this.c && 0 < (b = this[f] >>
        g) && (c = !0, e = N.charAt(b)); 0 <= f;)g < a ? (b = (this[f] & (1 << g) - 1) << a - g, b |= this[--f] >> (g += this.c - a)) : (b = this[f] >> (g -= a) & d, 0 >= g && (g += this.c, --f)), 0 < b && (c = !0), c && (e += N.charAt(b));
    return c ? e : "0"
};
l.N = function () {
    var a = P();
    S(O, this, a);
    return a
};
l.abs = function () {
    return 0 > this.a ? this.N() : this
};
function R(a, b) {
    var d = a.a - b.a;
    if (0 != d)return d;
    var c = a.t, d = c - b.t;
    if (0 != d)return 0 > a.a ? -d : d;
    for (; 0 <= --c;)if (0 != (d = a[c] - b[c]))return d;
    return 0
}
function za(a) {
    return 0 >= a.t ? 0 : a.c * (a.t - 1) + ac(a[a.t - 1] ^ a.a & a.u)
}
function G(a, b) {
    var d = P();
    cc(a.abs(), b, null, d);
    0 > a.a && 0 < R(d, O) && S(b, d, d);
    return d
}
function Ba(a, b, d) {
    d = 256 > b || U(d) ? new bc(d) : new fc(d);
    return a.exp(b, d)
}
var O = Zb(0), F = Zb(1);
function mc() {
}
function nc(a) {
    return a
}
mc.prototype.W = nc;
mc.prototype.$ = nc;
mc.prototype.U = function (a, b, d) {
    dc(a, b, d)
};
mc.prototype.D = function (a, b) {
    ec(a, b)
};
function Lb(a) {
    this.O = P();
    this.Ca = P();
    gc(F, 2 * a.t, this.O);
    this.ib = this.O.sa(a);
    this.g = a
}
l = Lb.prototype;
l.W = function (a) {
    if (0 > a.a || a.t > 2 * this.g.t)return G(a, this.g);
    if (0 > R(a, this.g))return a;
    var b = P();
    a.copyTo(b);
    this.reduce(b);
    return b
};
l.$ = function (a) {
    return a
};
l.reduce = function (a) {
    hc(a, this.g.t - 1, this.O);
    a.t > this.g.t + 1 && (a.t = this.g.t + 1, T(a));
    var b = this.ib, d = this.O, c = this.g.t + 1, e = this.Ca;
    --c;
    var f = e.t = b.t + d.t - c;
    for (e.a = 0; 0 <= --f;)e[f] = 0;
    for (f = Math.max(c - b.t, 0); f < d.t; ++f)e[b.t + f - c] = b.w(c - f, d[f], e, 0, 0, b.t + f - c);
    T(e);
    hc(e, 1, e);
    b = this.g;
    d = this.Ca;
    c = this.g.t + 1;
    e = this.O;
    f = Math.min(b.t + d.t, c);
    e.a = 0;
    for (e.t = f; 0 < f;)e[--f] = 0;
    var g;
    for (g = e.t - b.t; f < g; ++f)e[f + b.t] = b.w(0, d[f], e, f, 0, b.t);
    for (g = Math.min(d.t, c); f < g; ++f)b.w(0, d[f], e, f, 0, c - f);
    for (T(e); 0 > R(a, this.O);)jc(a, 1,
            this.g.t + 1);
    for (S(a, this.O, a); 0 <= R(a, this.g);)S(a, this.g, a)
};
l.U = function (a, b, d) {
    dc(a, b, d);
    this.reduce(d)
};
l.D = function (a, b) {
    ec(a, b);
    this.reduce(b)
};
var V = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739,
    743, 751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997], oc = 67108864 / V[V.length - 1];
function Nb(a, b, d, c) {
    if ("number" == typeof d)if (2 > b)$b(a, 1); else {
        Nb(a, b, c);
        if (!Kb(a, b - 1)) {
            c = F.shiftLeft(b - 1);
            var e, f, g = Math.min(c.t, a.t);
            for (e = 0; e < g; ++e)a[e] |= c[e];
            if (c.t < a.t) {
                f = c.a & a.u;
                for (e = g; e < a.t; ++e)a[e] |= f;
                a.t = a.t
            } else {
                f = a.a & a.u;
                for (e = g; e < c.t; ++e)a[e] = f | c[e];
                a.t = c.t
            }
            a.a |= c.a;
            T(a)
        }
        for (U(a) && jc(a, 1, 0); !pc(a, d);)jc(a, 2, 0), za(a) > b && S(a, F.shiftLeft(b - 1), a)
    } else c = [], e = b & 7, c.length = (b >> 3) + 1, d.Aa(c), c[0] = 0 < e ? c[0] & (1 << e) - 1 : 0, Ob(a, c, 256)
}
function qc(a, b, d) {
    for (var c = 0, e = 0, f = Math.min(b.t, a.t); c < f;)e += a[c] + b[c], d[c++] = e & a.u, e >>= a.c;
    if (b.t < a.t) {
        for (e += b.a; c < a.t;)e += a[c], d[c++] = e & a.u, e >>= a.c;
        e += a.a
    } else {
        for (e += a.a; c < b.t;)e += b[c], d[c++] = e & a.u, e >>= a.c;
        e += b.a
    }
    d.a = 0 > e ? -1 : 0;
    0 < e ? d[c++] = e : -1 > e && (d[c++] = a.o + e);
    d.t = c;
    T(d)
}
function ic(a, b) {
    a[a.t] = a.w(0, b - 1, a, 0, 0, a.t);
    ++a.t;
    T(a)
}
function jc(a, b, d) {
    if (0 != b) {
        for (; a.t <= d;)a[a.t++] = 0;
        for (a[d] += b; a[d] >= a.o;)a[d] -= a.o, ++d >= a.t && (a[a.t++] = 0), ++a[d]
    }
}
function rc(a, b) {
    var d = a.m(F), c;
    a:{
        for (c = 0; c < d.t; ++c)if (0 != d[c]) {
            var e;
            e = d[c];
            if (0 == e)e = -1; else {
                var f = 0;
                0 == (e & 65535) && (e >>= 16, f += 16);
                0 == (e & 255) && (e >>= 8, f += 8);
                0 == (e & 15) && (e >>= 4, f += 4);
                0 == (e & 3) && (e >>= 2, f += 2);
                0 == (e & 1) && ++f;
                e = f
            }
            c = c * d.c + e;
            break a
        }
        c = 0 > d.a ? d.t * d.c : -1
    }
    if (0 >= c)return!1;
    f = c;
    e = P();
    0 > f ? kc(d, -f, e) : lc(d, f, e);
    b = b + 1 >> 1;
    b > V.length && (b = V.length);
    for (var f = P(), g = 0; g < b; ++g) {
        $b(f, V[Math.floor(Math.random() * V.length)]);
        var h = Ca(f, e, a);
        if (0 != R(h, F) && 0 != R(h, d)) {
            for (var k = 1; k++ < c && 0 != R(h, d);)if (h = Ba(h, 2, a),
                0 == R(h, F))return!1;
            if (0 != R(h, d))return!1
        }
    }
    return!0
}
l = m.prototype;
l.clone = function () {
    var a = P();
    this.copyTo(a);
    return a
};
function aa(a) {
    if (0 > a.a) {
        if (1 == a.t)return a[0] - a.o;
        if (0 == a.t)return-1
    } else {
        if (1 == a.t)return a[0];
        if (0 == a.t)return 0
    }
    return(a[1] & (1 << 32 - a.c) - 1) << a.c | a[0]
}
function Jb(a) {
    return 0 > a.a ? -1 : 0 >= a.t || 1 == a.t && 0 >= a[0] ? 0 : 1
}
l.l = function (a) {
    return 0 == R(this, a)
};
l.min = function (a) {
    return 0 > R(this, a) ? this : a
};
l.max = function (a) {
    return 0 < R(this, a) ? this : a
};
l.shiftLeft = function (a) {
    var b = P();
    0 > a ? lc(this, -a, b) : kc(this, a, b);
    return b
};
function Kb(a, b) {
    var d = Math.floor(b / a.c);
    return d >= a.t ? 0 != a.a : 0 != (a[d] & 1 << b % a.c)
}
l.add = function (a) {
    var b = P();
    qc(this, a, b);
    return b
};
l.m = function (a) {
    var b = P();
    S(this, a, b);
    return b
};
l.multiply = function (a) {
    var b = P();
    dc(this, a, b);
    return b
};
l.sa = function (a) {
    var b = P();
    cc(this, a, b, null);
    return b
};
function Da(a, b) {
    var d = P();
    cc(a, b, null, d);
    return d
}
function Ca(a, b, d) {
    var c = za(b), e, f = Zb(1);
    if (0 >= c)return f;
    e = 18 > c ? 1 : 48 > c ? 3 : 144 > c ? 4 : 768 > c ? 5 : 6;
    d = 8 > c ? new bc(d) : U(d) ? new Lb(d) : new fc(d);
    var g = [], h = 3, k = e - 1, n = (1 << e) - 1;
    g[1] = d.W(a);
    if (1 < e)for (c = P(), d.D(g[1], c); h <= n;)g[h] = P(), d.U(c, g[h - 2], g[h]), h += 2;
    for (var w = b.t - 1, t, v = !0, x = P(), c = ac(b[w]) - 1; 0 <= w;) {
        c >= k ? t = b[w] >> c - k & n : (t = (b[w] & (1 << c + 1) - 1) << k - c, 0 < w && (t |= b[w - 1] >> a.c + c - k));
        for (h = e; 0 == (t & 1);)t >>= 1, --h;
        0 > (c -= h) && (c += a.c, --w);
        if (v)g[t].copyTo(f), v = !1; else {
            for (; 1 < h;)d.D(f, x), d.D(x, f), h -= 2;
            0 < h ? d.D(f, x) : (h = f, f = x, x =
                h);
            d.U(x, g[t], f)
        }
        for (; 0 <= w && 0 == (b[w] & 1 << c);)d.D(f, x), h = f, f = x, x = h, 0 > --c && (c = a.c - 1, --w)
    }
    return d.$(f)
}
function Hb(a, b) {
    var d = U(b);
    if (U(a) && d || 0 == Jb(b))return O;
    for (var c = b.clone(), e = a.clone(), f = Zb(1), g = Zb(0), h = Zb(0), k = Zb(1); 0 != Jb(c);) {
        for (; U(c);)lc(c, 1, c), d ? (U(f) && U(g) || (qc(f, a, f), S(g, b, g)), lc(f, 1, f)) : U(g) || S(g, b, g), lc(g, 1, g);
        for (; U(e);)lc(e, 1, e), d ? (U(h) && U(k) || (qc(h, a, h), S(k, b, k)), lc(h, 1, h)) : U(k) || S(k, b, k), lc(k, 1, k);
        0 <= R(c, e) ? (S(c, e, c), d && S(f, h, f), S(g, k, g)) : (S(e, c, e), d && S(h, f, h), S(k, g, k))
    }
    if (0 != R(e, F))return O;
    if (0 <= R(k, b))return k.m(b);
    if (0 > Jb(k))qc(k, b, k); else return k;
    return 0 > Jb(k) ? k.add(b) :
        k
}
l.pow = function (a) {
    return this.exp(a, new mc)
};
function pc(a, b) {
    var d, c = a.abs();
    if (1 == c.t && c[0] <= V[V.length - 1]) {
        for (d = 0; d < V.length; ++d)if (c[0] == V[d])return!0;
        return!1
    }
    if (U(c))return!1;
    for (d = 1; d < V.length;) {
        for (var e = V[d], f = d + 1; f < V.length && e < oc;)e *= V[f++];
        if (0 >= e)e = 0; else {
            var g = c.o % e, h = 0 > c.a ? e - 1 : 0;
            if (0 < c.t)if (0 == g)h = c[0] % e; else for (var k = c.t - 1; 0 <= k; --k)h = (g * h + c[k]) % e;
            e = h
        }
        for (; d < f;)if (0 == e % V[d++])return!1
    }
    return rc(c, b)
}
l.F = function () {
    var a = P();
    ec(this, a);
    return a
};
function sc() {
    this.X = this.M = 0;
    this.v = []
}
sc.prototype.next = function () {
    var a;
    this.M = this.M + 1 & 255;
    this.X = this.X + this.v[this.M] & 255;
    a = this.v[this.M];
    this.v[this.M] = this.v[this.X];
    this.v[this.X] = a;
    return this.v[a + this.v[this.M] & 255]
};
var tc = 256;
BOC_RSA_PUBLICKEY_EBANK = "MIGJAoGBAK1w5T8PYBoo9IwK/i18iAOoYUgVgyAYbHThMYKh6iAbxTYAjoic/511LwfRm9Meds0x4NG2PIS5ptPuZEAeRZn7giKa9tW8mirhBFXtdf4WPiAxuNUdKN1qJRXs5klf3vKfk68gAxQbvajDxWzi9XCsKN+al6qBttKCyTztknJFAgMBAAE=";
BOC_SM2_PUBLICKEY_EBANK = "V4MrggVgB0ZlMFTSEOXw9LCSdgtz1aWfaiyxSPhcRnNtWW08bg9CRNBSpSl02a7vNP6xV6FR5KQao4I4PqZaoA==";
BOC_RSA_PUBLICKEY_OPEN_PLATFORM = "MIGJAoGBALRegp5sozLBgWtW4VPHRMApW8pWC3i6cAfGdJ7XMzhMCsTk9o+E4vGM1m4X9OnLmCTD/YqA+DzN7qduLxfm9kLX5HIdsn2Q0a7vlSF3DRBqPjaI/qgdTzq9rIsSQxtkqVzW36rvpaN5iBs3IqREEOdfGFAvDAi2Cm4jeI6GrmCjAgMBAAE=";
BOC_SM2_PUBLICKEY_OPEN_PLATFORM = "iXf33eR4QwurB2hYrMsT8/+p2cDrtje8TNIjNOJojXamJ5f9h1YFzQ46lNPvujE5lFyU5Y5zyeGpAIWqRoRqYg==";
var uc, W, X;
function vc() {
    var a = (new Date).getTime();
    W[X++] ^= a & 255;
    W[X++] ^= a >> 8 & 255;
    W[X++] ^= a >> 16 & 255;
    W[X++] ^= a >> 24 & 255;
    X >= tc && (X -= tc)
}
if (null == W) {
    W = [];
    X = 0;
    var Y;
    if (window.crypto && window.crypto.getRandomValues) {
        var wc = new Uint8Array(32);
        window.crypto.getRandomValues(wc);
        for (Y = 0; 32 > Y; ++Y)W[X++] = wc[Y]
    }
    if ("Netscape" == navigator.appName && "5" > navigator.appVersion && window.crypto) {
        var xc = window.crypto.random(32);
        for (Y = 0; Y < xc.length; ++Y)W[X++] = xc.charCodeAt(Y) & 255
    }
    for (; X < tc;)Y = Math.floor(65536 * Math.random()), W[X++] = Y >>> 8, W[X++] = Y & 255;
    X = 0;
    vc()
}
function Eb(a) {
    var b;
    for (b = 0; b < a.length; ++b) {
        var d = b, c;
        if (null == uc) {
            vc();
            c = uc = new sc;
            for (var e = W, f = void 0, g = void 0, h = void 0, f = 0; 256 > f; ++f)c.v[f] = f;
            for (f = g = 0; 256 > f; ++f)g = g + c.v[f] + e[f % e.length] & 255, h = c.v[f], c.v[f] = c.v[g], c.v[g] = h;
            c.M = 0;
            for (X = c.X = 0; X < W.length; ++X)W[X] = 0;
            X = 0
        }
        c = uc.next();
        a[d] = c
    }
}
function Aa() {
}
Aa.prototype.Aa = Eb;
function ya() {
    this.n = null;
    this.e = 0;
    this.q = this.p = this.d = null
}
var pa = 0;
function D(a, b) {
    var d = (a & 65535) + (b & 65535);
    return(a >> 16) + (b >> 16) + (d >> 16) << 16 | d & 65535
}
function Ia(a) {
    var b = [];
    if (33 == a.length)for (var d = 1; 33 > d; d++)b.push(a[d]); else if (32 == a.length)for (d = 0; 32 > d; d++)b.push(a[d]);
    return b
}
function E(a, b) {
    b = b & 31;
    return a << b | a >>> 32 - b
}
function yc(a, b) {
    var d = (a & 65535) + (b & 65535), c = ((a & 4294901760) >>> 16) + ((b & 4294901760) >>> 16) + (d >>> 16) & 65535;
    return c << 16 ^ d & 65535
}
function zc(a, b, d) {
    for (var c = [], e = 0; 16 > e; e++) {
        for (var f = 0, g = 0; 4 > g; g++)f = f << 8 ^ d[(e << 2) + g];
        c[e] = f
    }
    d = [0, 0, 0, 0, 0, 0, 0, 0];
    e = c;
    f = f = 0;
    c = [0];
    d = [0];
    for (f = 0; 16 > f; f++)c[f] = e[f];
    for (f = 16; 68 > f; f++)e = c[f - 16] ^ c[f - 9] ^ E(c[f - 3], 15), c[f] = e ^ E(e, 15) ^ E(e, 23) ^ E(c[f - 13], 7) ^ c[f - 6];
    for (f = 0; 64 > f; f++)d[f] = c[f] ^ c[f + 4];
    var h, k, n, w, t, v = 0, x = [0, 0, 0, 0, 0, 0, 0, 0], e = a[0], f = a[1], g = a[2];
    h = a[3];
    k = a[4];
    n = a[5];
    w = a[6];
    t = a[7];
    for (v = 0; 64 > v; v++) {
        var u = 0, u = 0 <= v && 15 >= v ? 2043430169 : 16 <= v && 63 >= v ? 2055708042 : 0, u = E(yc(yc(E(e, 12), k), E(u, v)), 7), C = u ^ E(e,
            12), y = 0, z = y = 0, z = 0;
        a:if (y = 0, 0 <= v && 15 >= v)y = e ^ f ^ g; else if (16 <= v && 63 >= v)y = e & f | e & g | f & g; else {
            y = {kb: -1};
            break a
        }
        y = yc(yc(yc(y, h), C), d[v]);
        a:{
            h = 0;
            if (0 <= v && 15 >= v)h = k ^ n ^ w; else if (16 <= v && 63 >= v)h = k & n | ~k & w; else {
                z = {kb: -1};
                break a
            }
            z = h
        }
        z = yc(yc(yc(z, t), u), c[v]);
        h = g;
        g = E(f, 9);
        f = e;
        e = y;
        t = w;
        w = E(n, 19);
        n = k;
        k = z ^ E(z, 9) ^ E(z, 17)
    }
    x[0] = e ^ a[0];
    x[1] = f ^ a[1];
    x[2] = g ^ a[2];
    x[3] = h ^ a[3];
    x[4] = k ^ a[4];
    x[5] = n ^ a[5];
    x[6] = w ^ a[6];
    x[7] = t ^ a[7];
    d = x;
    a = [0, 0, 0, 0, 0, 0, 0, 0];
    for (c = 0; 8 > c; c++)a[c] = d[c];
    return{s: a, Da: b + 64}
}
function Ja() {
    var a = [0, 0, 0, 0, 0, 0, 0, 0];
    a[0] = 1937774191;
    a[1] = 1226093241;
    a[2] = 388252375;
    a[3] = -628488704;
    a[4] = -1452330820;
    a[5] = 372324522;
    a[6] = -477237683;
    a[7] = -1325724082;
    for (var b = [], d = 0; 64 > d; d++)b[d] = 0;
    return{s: a, B: 0, A: b, C: 0}
}
function Ka(a, b, d, c, e, f) {
    var g = 0, h = g = 0, k = 0, n = k = 0, h = 0;
    if (64 >= c + f) {
        for (g = c; g < c + f; g++)d[g] = e[g - c];
        return{s: a, B: b, A: d, C: c + f}
    }
    for (g = c; 64 > g; g++)d[g] = e[g - c];
    g = 64 - c;
    a = zc(a, b, d);
    if (0 < f - g) {
        h = f - g >>> 6;
        for (k = 0; k < h; k++) {
            b = [];
            for (c = 0; 64 > c; c++)b.push(e[g + 64 * k + c]);
            a = zc(a.s, a.Da, b)
        }
        n = f - g & 63;
        for (k = 0; k < n; k++)d[k] = e[g + 64 * h + k];
        h = n
    }
    return{s: a.s, B: a.Da, A: d, C: h}
}
function La(a, b, d, c, e) {
    retUpdate = Ka(a, b, d, c, e, 0);
    b = retUpdate.A;
    d = retUpdate.C;
    c = retUpdate.B + retUpdate.C;
    var f = e = 0;
    a = [];
    if (56 > d) {
        for (e = 0; 64 > e; e++)a[e] = 0;
        for (e = 0; e < d; e++)a[e] = b[e];
        a[e] = 128;
        e = c << 3;
        for (f = 7; 0 <= f; f--)a[56 + f] = e & 255, e >>>= 8
    } else if (64 >= d) {
        for (e = 0; 128 > e; e++)a[e] = 0;
        for (e = 0; e < d; e++)a[e] = b[e];
        a[e] = 128;
        e = c << 3;
        for (f = 7; 0 <= f; f--)a[120 + f] = e & 255, e >>>= 8
    }
    b = zc(retUpdate.s, retUpdate.B, a);
    if (64 < a.length) {
        c = [];
        for (d = 0; 64 > d; d++)c[d] = a[64 + d];
        b = zc(b.s, b.B, c)
    }
    a = [0, 0, 0, 0, 0, 0, 0, 0];
    for (d = 0; 8 > d; d++)a[d] = b.s[d];
    d =
        b = 0;
    c = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    for (b = 0; 8 > b; b++)for (e = a[b], d = 3; 0 <= d; d--)c[4 * b + d] = e & 255, e = e >>> 8;
    return c
}
function Ac(a) {
    var b;
    switch (a) {
        case "0":
            b = 0;
            break;
        case "1":
            b = 1;
            break;
        case "2":
            b = 2;
            break;
        case "3":
            b = 3;
            break;
        case "4":
            b = 4;
            break;
        case "5":
            b = 5;
            break;
        case "6":
            b = 6;
            break;
        case "7":
            b = 7;
            break;
        case "8":
            b = 8;
            break;
        case "9":
            b = 9;
            break;
        case "a":
        case "A":
            b = 10;
            break;
        case "b":
        case "B":
            b = 11;
            break;
        case "c":
        case "C":
            b = 12;
            break;
        case "d":
        case "D":
            b = 13;
            break;
        case "e":
        case "E":
            b = 14;
            break;
        case "f":
        case "F":
            b = 15
    }
    return b
}
function Ga(a) {
    var b = [];
    0 != (a.length & 1) && (a = "0" + a);
    for (var d = 0; d < a.length; d += 2)b.push(Ac(a.charAt(d)) << 4 ^ Ac(a.charAt(d + 1)));
    return b
}
var Bc = [214, 144, 233, 254, 204, 225, 61, 183, 22, 182, 20, 194, 40, 251, 44, 5, 43, 103, 154, 118, 42, 190, 4, 195, 170, 68, 19, 38, 73, 134, 6, 153, 156, 66, 80, 244, 145, 239, 152, 122, 51, 84, 11, 67, 237, 207, 172, 98, 228, 179, 28, 169, 201, 8, 232, 149, 128, 223, 148, 250, 117, 143, 63, 166, 71, 7, 167, 252, 243, 115, 23, 186, 131, 89, 60, 25, 230, 133, 79, 168, 104, 107, 129, 178, 113, 100, 218, 139, 248, 235, 15, 75, 112, 86, 157, 53, 30, 36, 14, 94, 99, 88, 209, 162, 37, 34, 124, 59, 1, 33, 120, 135, 212, 0, 70, 87, 159, 211, 39, 82, 76, 54, 2, 231, 160, 196, 200, 158, 234, 191, 138, 210, 64, 199, 56, 181, 163, 247, 242, 206,
    249, 97, 21, 161, 224, 174, 93, 164, 155, 52, 26, 85, 173, 147, 50, 48, 245, 140, 177, 227, 29, 246, 226, 46, 130, 102, 202, 96, 192, 41, 35, 171, 13, 83, 78, 111, 213, 219, 55, 69, 222, 253, 142, 47, 3, 255, 106, 114, 109, 108, 91, 81, 141, 27, 175, 146, 187, 221, 188, 127, 17, 217, 92, 65, 31, 16, 90, 216, 10, 193, 49, 136, 165, 205, 123, 189, 45, 116, 208, 18, 184, 229, 180, 176, 137, 105, 151, 74, 12, 150, 119, 126, 101, 185, 241, 9, 197, 110, 198, 132, 24, 240, 125, 236, 58, 220, 77, 32, 121, 238, 95, 62, 215, 203, 57, 72];
function ta(a) {
    for (var b = 0, d = 0, d = 0, c = [0, 0, 0, 0], e = [0, 0, 0, 0], d = 3; 0 <= d; d--)c[d] = a & 255, a = a >>> 8;
    e[0] = Bc[c[0]];
    e[1] = Bc[c[1]];
    e[2] = Bc[c[2]];
    e[3] = Bc[c[3]];
    for (d = 0; 4 > d; d++)b = (b << 8) + e[d];
    return b
}
function E(a, b) {
    b = b & 31;
    return a << b | a >>> 32 - b
}
function va(a, b, d) {
    for (var c = 0, e = [0, 0, 0, 0], f = [0, 0, 0, 0], c = 0; 4 > c; c++)f[c] = a[c] ^ d[c];
    a = [0, 0, 0, 0];
    c = 0;
    e = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    e[0] = f[0];
    e[1] = f[1];
    e[2] = f[2];
    e[3] = f[3];
    for (c = 0; 32 > c; c++) {
        f = [0, 0, 0, 0];
        f[0] = e[c];
        f[1] = e[c + 1];
        f[2] = e[c + 2];
        f[3] = e[c + 3];
        var g = 0, g = g = g = 0, g = ta(f[1] ^ f[2] ^ f[3] ^ b[c]), h = 0, g = h = g ^ E(g, 2) ^ E(g, 10) ^ E(g, 18) ^ E(g, 24), g = f[0] ^ g;
        e[c + 4] = g
    }
    a[0] = e[35];
    a[1] = e[34];
    a[2] = e[33];
    a[3] = e[32];
    e = a;
    for (c = 0; 4 > c; c++)d[c] = e[c];
    return{s: d, A: e}
}
function sa(a) {
    var b = [0, 0, 0, 0];
    if (0 == a.length)return null;
    if (16 > a.length)for (var d = 16 - a.length, c = 0; c < d; c++)a.push(d);
    for (c = 0; 4 > c; c++)b[c] = (a[4 * c + 0] << 24 | a[4 * c + 1] << 16 | a[4 * c + 2] << 8 | a[4 * c + 3]) >>> 0;
    return b
};
