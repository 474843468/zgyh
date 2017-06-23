define("bocejs", ["zeptoExt", "ejs"], function($, EJS) {
    function scanWiget(tpl){
        tpl = tpl.replace(/<\!--.*?-->/g, '');
        var wdgtpls = tpl.match(/<([a-zA-Z]+)[\s]+[^<>]*widget_type[^<>]*>([^<>]*<[^<>]*>[^<>]*<\/[a-zA-Z]+>[^<>]*)*<\/\1>/g);
        wdgtpls && $.each(wdgtpls, function(i, wdgtpl){
            var wdgname = wdgtpl.match(/widget_type="[^>"]+"/)[0].replace(/widget_type="/, "").replace(/"/, "");
            var rds,wdghtm = (rds=$._widget_renders, rds && rds[wdgname] && rds[wdgname].apply(null, [wdgtpl]));
            tpl = tpl.replace(wdgtpl, wdghtm);
        });
        return tpl;
    }
    return $.funAop(EJS, {
        "render" : {
            after : function(res, args) {
                return scanWiget(res);
            }
        }
    }, true);
}); 