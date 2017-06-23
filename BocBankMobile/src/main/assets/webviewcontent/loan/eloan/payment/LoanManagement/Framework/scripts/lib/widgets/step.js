/**
 * Created by zjd3981 on 2015/7/30.
 */

define('StepWidget', ['zepto', 'vewController'], function($, vc){
    $.extendCls({
        name: '账单缴付',
        panes: '',
        nav: '',
        el: null,
    });
});

define('StepPane', ['zepto', 'viewController'], function($, vc){
    $.extendCls({
        hasNext: true,
        hasPrev: false,
        services: [],
        onNext: null,
        onPrev: null,
        onLoad: null,
        title: '账单信息查询',
        stepWidget: null,
        templeString: "",
        ejs: '',
        panename: '',
        view: null,
        setTitle: function(){

        },
        setContent: function(){

        },
        setTemplate: function(){

        },
        placeAt: function(){

        },
        load: function(){
            this.view = new $.extendCls(vc, {
                id: this.panename + Math.random()*0xfff,
            })({
                templateString: this.templateString,
                ejs: this.ejs,
                onPrev: this.onPrev,
                onNext: this.onNext,
                onLoadFinish: this.onLoad
            });
            this.view.load();
            this.active();
        },
        active: function(){

        }
    })
});