define("zepto", ["./lib/zepto"], function () {
    return $;
});
define("appconfig", [], function(){
    return {
        baseUrl: "scripts",
        waitSeconds: 30,
        paths: {
            zeptoExt: "lib/zepto.bocextend",
            domReady: "lib/domReady",
            ejs: "lib/ejs",
            bocejs: "lib/bocejs",
            model: "public/model",
            common: "public/common",
            viewcontroller: "public/viewcontroller",
            routesUtil: "public/routesUtil",
            checkForm: "public/checkForm",
            constant: "public/constant",
            errMessage: "public/errMessage",
            LoanManagementRouterPath: "../../LoanManagement/routes/routerPath",
            stackCtrl: "public/stackCtrl",
            nls_public_en_us: "nls/public/en_us",
            nls_public_zh_cn: "nls/public/zh_cn"
        },
        onAppInitBefore: function(){
            console.log("customer info: before app init")
        },
        onAppInitAfter: function($, Common, Model,RoutesUtil, nls){
            console.log("customer info: after app init")
            $("#localebtn").on("click", function(){
                var txt = $("#localebtn").text();
                Common.changeLocalizeResource(txt == "中文" ? "zh" : "en");
                $("#localebtn").text(txt == "中文" ? "English" : "中文")
            });
        },
        onRouteBefore: function(){
            console.log("customer info: before enter to route")
        },
        onRouteAfter: function(){
            console.log("customer info: after route entered")
        }
    }
});