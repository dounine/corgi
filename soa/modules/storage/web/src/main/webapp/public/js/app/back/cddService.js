define(['angular'], function(angular) {
    "use strict";

    var app = angular.module(
        "dnnApp.services.cdd",//定义的模块名称
        [
            "ui.router",//依赖的模块
            "ngCookies"
        ]);
    app.factory("cddService", cddService);
    app.factory("cddCaptchaService", cddCaptchaService);
    app.factory("cddFileService", cddFileService);
    app.factory("cddCaptchaData", cddCaptchaData);
    app.factory("cddCheckService", cddCheckService);
    app.factory("cddSearchData", cddSearchData);

    app.constant("imgTypeIcos",
        [{suffixs: ["folder"]}
            , {suffixs: ["file"]}
            , {suffixs: ["png","jpg","gif","bmp","jpeg"]}
            , {suffixs: ["xls", "xlsx", "excel"]}
            , {suffixs: ["txt"]}]);

    function cddService($http) {
        var service = {
            need_captcha: need_captcha
        };
        return service;

        function need_captcha(account) {
            var postDat = {"account": account};
            return $http.post("admin/clouddisk/captcha/needCaptcha", postDat);//用户是否需要验证码http请求
        }
    }

    function cddCheckService($http) {
        var service = {
            clear_request: clear_request
        };
        return service;

        function clear_request(account) {
            return $http.get("admin/clouddisk/loginCheck/clear");
        }
    }

    function cddSearchData($http) {
        return {
            search: false
        }
    }

    function cddCaptchaService($http) {
        var service = {
            captcha_request: captcha_request//验证
        };
        return service;


        function captcha_request(data) {
            return $http.post("admin/clouddisk/captcha/valid", data);
        }
    }

    function cddCaptchaData() {
        return {
            url: null,
            path: function () {
                return "admin/clouddisk/captcha/read?account=403833139@qq.com&t=" + new Date().getTime()
            }
        }
    }

    function cddFileService($http, imgTypeIcos, $rootScope) {
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

