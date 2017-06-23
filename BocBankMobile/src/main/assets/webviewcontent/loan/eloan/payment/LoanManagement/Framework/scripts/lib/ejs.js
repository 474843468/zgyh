define("ejs", function () {
    var rsplit = function (string, regex) {
        for (var first_idx, last_idx, first_bit, result = regex.exec(string), retArr = new Array; null != result;)first_idx = result.index, last_idx = regex.lastIndex, 0 != first_idx && (first_bit = string.substring(0, first_idx), retArr.push(string.substring(0, first_idx)), string = string.slice(first_idx)), retArr.push(result[0]), string = string.slice(result[0].length), result = regex.exec(string);
        return"" == !string && retArr.push(string), retArr
    }, chop = function (string) {
        return string.substr(0, string.length - 1)
    }, extend = function (d, s) {
        for (var n in s)s.hasOwnProperty(n) && (d[n] = s[n])
    };
    return EJS = function (options) {
        if (options = "string" == typeof options ? {view: options} : options, this.set_options(options), options.precompiled)return this.template = {}, this.template.process = options.precompiled, void EJS.update(this.name, this);
        if (options.element) {
            if ("string" == typeof options.element) {
                var name = options.element;
                if (options.element = document.getElementById(options.element), null == options.element)throw name + "does not exist!"
            }
            this.text = options.element.value ? options.element.value : options.element.innerHTML, this.name = options.element.id, this.type = "["
        } else if (options.url) {
            options.url = EJS.endExt(options.url, this.extMatch), this.name = this.name ? this.name : options.url;
            var url = options.url, template = EJS.get(this.name, this.cache);
            if (template)return template;
            if (template == EJS.INVALID_PATH)return null;
            try {
                this.text = EJS.request(url + (this.cache ? "" : "?" + Math.random()))
            } catch (e) {
            }
            if (null == this.text)throw{type: "EJS", message: "There is no template at " + url}
        }
        var template = new EJS.Compiler(this.text, this.type);
        template.compile(options, this.name), EJS.update(this.name, this), this.template = template
    }, EJS.prototype = {render: function (object, extra_helpers) {
        object = object || {}, this._extra_helpers = extra_helpers;
        var v = new EJS.Helpers(object, extra_helpers || {});
        return this.template.process.call(object, object, v)
    }, update: function (element, options) {
        return"string" == typeof element && (element = document.getElementById(element)), null == options ? (_template = this, function (object) {
            EJS.prototype.update.call(_template, element, object)
        }) : void("string" == typeof options ? (params = {}, params.url = options, _template = this, params.onComplete = function (request) {
            var object = eval(request.responseText);
            EJS.prototype.update.call(_template, element, object)
        }, EJS.ajax_request(params)) : element.innerHTML = this.render(options))
    }, out: function () {
        return this.template.out
    }, set_options: function (options) {
        this.type = options.type || EJS.type, this.cache = null != options.cache ? options.cache : EJS.cache, this.text = options.text || null, this.name = options.name || null, this.ext = options.ext || EJS.ext, this.extMatch = new RegExp(this.ext.replace(/\./, "."))
    }}, EJS.endExt = function (path, match) {
        return path ? (match.lastIndex = 0, path + (match.test(path) ? "" : this.ext)) : null
    }, EJS.Scanner = function (source, left, right) {
        extend(this, {left_delimiter: left + "%", right_delimiter: "%" + right, double_left: left + "%%", double_right: "%%" + right, left_equal: left + "%=", left_comment: left + "%#"}), this.SplitRegexp = "[" == left ? /(\[%%)|(%%\])|(\[%=)|(\[%#)|(\[%)|(%\]\n)|(%\])|(\n)/ : new RegExp("(" + this.double_left + ")|(%%" + this.double_right + ")|(" + this.left_equal + ")|(" + this.left_comment + ")|(" + this.left_delimiter + ")|(" + this.right_delimiter + "\n)|(" + this.right_delimiter + ")|(\n)"), this.source = source, this.stag = null, this.lines = 0
    }, EJS.Scanner.to_text = function (input) {
        return null == input || void 0 === input ? "" : input instanceof Date ? input.toDateString() : input.toString ? input.toString() : ""
    }, EJS.Scanner.prototype = {scan: function (block) {
        if (scanline = this.scanline, regex = this.SplitRegexp, "" == !this.source)for (var source_split = rsplit(this.source, /\n/), i = 0; i < source_split.length; i++) {
            var item = source_split[i];
            this.scanline(item, regex, block)
        }
    }, scanline: function (line, regex, block) {
        this.lines++;
        for (var line_split = rsplit(line, regex), i = 0; i < line_split.length; i++) {
            var token = line_split[i];
            if (null != token)try {
                block(token, this)
            } catch (e) {
                throw{type: "EJS.Scanner", line: this.lines}
            }
        }
    }}, EJS.Buffer = function (pre_cmd, post_cmd) {
        this.line = new Array, this.script = "", this.pre_cmd = pre_cmd, this.post_cmd = post_cmd;
        for (var i = 0; i < this.pre_cmd.length; i++)this.push(pre_cmd[i])
    }, EJS.Buffer.prototype = {push: function (cmd) {
        this.line.push(cmd)
    }, cr: function () {
        this.script = this.script + this.line.join("; "), this.line = new Array, this.script = this.script + "\n"
    }, close: function () {
        if (this.line.length > 0) {
            for (var i = 0; i < this.post_cmd.length; i++)this.push(pre_cmd[i]);
            this.script = this.script + this.line.join("; "), line = null
        }
    }}, EJS.Compiler = function (source, left) {
        this.pre_cmd = ["var ___ViewO = [];"], this.post_cmd = new Array, this.source = " ", null != source && ("string" == typeof source ? (source = source.replace(/\r\n/g, "\n"), source = source.replace(/\r/g, "\n"), this.source = source) : source.innerHTML && (this.source = source.innerHTML), "string" != typeof this.source && (this.source = "")), left = left || "<";
        var right = ">";
        switch (left) {
            case"[":
                right = "]";
                break;
            case"<":
                break;
            default:
                throw left + " is not a supported deliminator"
        }
        this.scanner = new EJS.Scanner(this.source, left, right), this.out = ""
    }, EJS.Compiler.prototype = {compile: function (options, name) {
        options = options || {}, this.out = "";
        var put_cmd = "___ViewO.push(", insert_cmd = put_cmd, buff = new EJS.Buffer(this.pre_cmd, this.post_cmd), content = "", clean = function (content) {
            return content = content.replace(/\\/g, "\\\\"), content = content.replace(/\n/g, "\\n"), content = content.replace(/"/g, '\\"')
        };
        this.scanner.scan(function (token, scanner) {
            if (null == scanner.stag)switch (token) {
                case"\n":
                    content += "\n", buff.push(put_cmd + '"' + clean(content) + '");'), buff.cr(), content = "";
                    break;
                case scanner.left_delimiter:
                case scanner.left_equal:
                case scanner.left_comment:
                    scanner.stag = token, content.length > 0 && buff.push(put_cmd + '"' + clean(content) + '")'), content = "";
                    break;
                case scanner.double_left:
                    content += scanner.left_delimiter;
                    break;
                default:
                    content += token
            } else switch (token) {
                case scanner.right_delimiter:
                    switch (scanner.stag) {
                        case scanner.left_delimiter:
                            "\n" == content[content.length - 1] ? (content = chop(content), buff.push(content), buff.cr()) : buff.push(content);
                            break;
                        case scanner.left_equal:
                            buff.push(insert_cmd + "(EJS.Scanner.to_text(" + content + ")))")
                    }
                    scanner.stag = null, content = "";
                    break;
                case scanner.double_right:
                    content += scanner.right_delimiter;
                    break;
                default:
                    content += token
            }
        }), content.length > 0 && buff.push(put_cmd + '"' + clean(content) + '")'), buff.close(), this.out = buff.script + ";";
        var to_be_evaled = "/*" + name + "*/this.process = function(_CONTEXT,_VIEW) { try { with(_VIEW) { with (_CONTEXT) {" + this.out + " return ___ViewO.join('');}}}catch(e){e.lineNumber=null;throw e;}};";
        try {
            eval(to_be_evaled)
        } catch (e) {
            if ("undefined" == typeof JSLINT)throw e;
            JSLINT(this.out);
            for (var i = 0; i < JSLINT.errors.length; i++) {
                var error = JSLINT.errors[i];
                if ("Unnecessary semicolon." != error.reason) {
                    error.line++;
                    var e = new Error;
                    throw e.lineNumber = error.line, e.message = error.reason, options.view && (e.fileName = options.view), e
                }
            }
        }
    }}, EJS.config = function (options) {
        EJS.cache = null != options.cache ? options.cache : EJS.cache, EJS.type = null != options.type ? options.type : EJS.type, EJS.ext = null != options.ext ? options.ext : EJS.ext;
        var templates_directory = EJS.templates_directory || {};
        EJS.templates_directory = templates_directory, EJS.get = function (path, cache) {
            return 0 == cache ? null : templates_directory[path] ? templates_directory[path] : null
        }, EJS.update = function (path, template) {
            null != path && (templates_directory[path] = template)
        }, EJS.INVALID_PATH = -1
    }, EJS.config({cache: !0, type: "<", ext: ".ejs"}), EJS.Helpers = function (data, extras) {
        this._data = data, this._extras = extras, extend(this, extras)
    }, EJS.Helpers.prototype = {view: function (options, data, helpers) {
        return helpers || (helpers = this._extras), data || (data = this._data), new EJS(options).render(data, helpers)
    }, to_text: function (input, null_text) {
        return null == input || void 0 === input ? null_text || "" : input instanceof Date ? input.toDateString() : input.toString ? input.toString().replace(/\n/g, "<br />").replace(/''/g, "'") : ""
    }}, EJS.newRequest = function () {
        for (var factories = [function () {
            return new ActiveXObject("Msxml2.XMLHTTP")
        }, function () {
            return new XMLHttpRequest
        }, function () {
            return new ActiveXObject("Microsoft.XMLHTTP")
        }], i = 0; i < factories.length; i++)try {
            var request = factories[i]();
            if (null != request)return request
        } catch (e) {
            continue
        }
    }, EJS.request = function (path) {
        var request = new EJS.newRequest;
        request.open("GET", path, !1);
        try {
            request.send(null)
        } catch (e) {
            return null
        }
        return 404 == request.status || 2 == request.status || 0 == request.status && "" == request.responseText ? null : request.responseText
    }, EJS.ajax_request = function (params) {
        params.method = params.method ? params.method : "GET";
        var request = new EJS.newRequest;
        request.onreadystatechange = function () {
            4 == request.readyState && params.onComplete(200 == request.status ? request : request)
        }, request.open(params.method, params.url), request.send(null)
    }, EJS
});