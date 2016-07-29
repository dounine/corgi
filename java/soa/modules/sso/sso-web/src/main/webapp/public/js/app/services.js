define(['angular'], function(angular) {
    "use strict";

    var app = angular.module(
        "app.services.hyaena",//定义的模块名称
        [
            "ui.router",//依赖的模块
            "ngCookies"
        ]);
    app.factory("hyaenaService", hyaenaService);
    app.factory("hyaenaCaptchaService", hyaenaCaptchaService);
    app.factory("hyaenaFileService", hyaenaFileService);
    app.factory("hyaenaCaptchaData", hyaenaCaptchaData);
    app.factory("hyaenaCheckService", hyaenaCheckService);
    app.factory("hyaenaSearchData", hyaenaSearchData);
    app.factory("captureService", captureService);

    app.constant("imgTypeIcos",
        [{suffixs: ["folder"]}
            , {suffixs: ["file"]}
            , {suffixs: ["png","jpg","gif","bmp","jpeg"]}
            , {suffixs: ["xls", "xlsx", "excel"]}
            , {suffixs: ["txt"]}]);

    function hyaenaService($http) {
        var service = {
            need_captcha: need_captcha
        };
        return service;

        function need_captcha(account) {
            var postDat = {"account": account};
            return $http.post("admin/clouddisk/captcha/needCaptcha", postDat);//captcha HttpRequest
        }
    }
    function captureService($http) {
        var service = {
            navs: navs,
            list: list
        };
        return service;

        function navs() {
            return $http.get("capture/navs")
        }
        function list(type) {
            return $http.get("capture/"+type+"/list")
        }
    }

    function hyaenaCheckService($http) {
        var service = {
            clear_request: clear_request
        };
        return service;

        function clear_request(account) {
            return $http.get("admin/clouddisk/loginCheck/clear");
        }
    }

    function hyaenaSearchData($http) {
        return {
            search: false
        }
    }

    function hyaenaCaptchaService($http) {
        var service = {
            captcha_request: captcha_request//验证
        };
        return service;


        function captcha_request(data) {
            return $http.post("admin/clouddisk/captcha/valid", data);
        }
    }

    function hyaenaCaptchaData() {
        return {
            url: null,
            path: function () {
                return "admin/clouddisk/captcha/read?account=403833139@qq.com&t=" + new Date().getTime()
            }
        }
    }

    function hyaenaFileService($http, imgTypeIcos, $rootScope) {
        var service = {
            list_request: list_request,
            suffix_ico: suffix_ico
        };
        return service;


        function list_request(search) {
            var postDat = {"path": $rootScope.$stateParams.path};
            if(search){
                return $http.post("admin/clouddisk/file/search", postDat);
            }
            return $http.post("admin/clouddisk/file/list", postDat);
        }

        function suffix_ico(suffix) {//后缀样式选择
            var isItem = null;
            for (var i in imgTypeIcos) {
                var item = imgTypeIcos[i];
                for (var j in item.suffixs) {
                    var value = item.suffixs[j];
                    if (value == suffix) {
                        isItem = item;
                        break;
                    }
                }
            }
            isItem = isItem || imgTypeIcos[0];
            return "ico-" + isItem.suffixs[0];
        }
    }
});

