/**
 * Created by huanghuanlai on 16/4/10.
 */
define(['app'],function (app) {
    "use strict";

    app.config(function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/index");
        $stateProvider.state("index", {
            url : "/index",
            views : {
                "" : {
                    templateUrl : "admin/menu",
                    controller:'cddCheckController'
                }
            }
        }).state("clouddisk",{
            url : "/clouddisk",
            views : {
                "":{
                    templateUrl : "admin/clouddisk/content",
                    controller : "cddController"
                }
            }
        }).state("fileList", {
            url : "/fileList?{path}",
            views : {
                "" : {
                    templateUrl : "admin/clouddisk/file/content"
                },
                "list@fileList" : {
                    templateUrl : "admin/clouddisk/file/list/content",
                    controller : "cddFileController"
                },
                "captcha@fileList" : {
                    templateUrl :"admin/clouddisk/captcha/content",
                    controller : "cddCaptchaController"
                }
            }
        })
    })
});