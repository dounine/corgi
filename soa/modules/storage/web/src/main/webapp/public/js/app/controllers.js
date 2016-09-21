define(["angular","app/services"], function(angular) {
    "use strict";

    var app = angular.module(
        "app.controllers.hyaena",//定义的模块名称
        [
            "app.services.hyaena"//依赖的模块
        ]);
    app.controller("captureController", captureController);
    app.controller("databaseController", databaseController);
    app.controller("serviceController", serviceController);
    app.controller("controllerController", controllerController);
    app.controller("viewController", viewController);

    function captureController($scope,$rootScope,captureService) {
        captureService.navs().success(function (data) {
            $scope.navs = data.data;
        });
    }
    function databaseController($scope,$rootScope,captureService) {
        $scope.name = "database";
        captureService.list($scope.name).success(function (response) {
            $scope.list = response.data;
        });
    }
    function serviceController($scope,$rootScope,captureService) {
        $scope.name = "service";
        captureService.list($scope.name).success(function (response) {
            $scope.list = response.data;
        });
    }
    function controllerController($scope,$rootScope,captureService) {
        $scope.name = "controller";
        captureService.list($scope.name).success(function (response) {
            $scope.list = response.data;
        });
    }
    function viewController($scope,$rootScope,captureService) {
        $scope.name = "view";
        captureService.list($scope.name).success(function (response) {
            $scope.list = response.data;
        });
    }
    function hyaenaController($scope, $rootScope) {
        var vm = $scope;
        $rootScope.$state.go("fileList", {path: "/"});
    }

    function icheckInit() {
        $(document).ready(function(){//use for icheck
            $('input').iCheck({
                checkboxClass: 'icheckbox_flat-red',
                radioClass: 'iradio_flat'
            });
        });
    }

    function hyaenaFileSearchController($scope,$rootScope,hyaenaSearchData,hyaenaCaptchaData,hyaenaFileService,$cookies) {
        var vm = $scope;
        vm.file_img_init = file_img_init;
        vm.suffix_ico = suffix_ico;
        vm.hyaenaSearchData = hyaenaSearchData;
        inits();
        function inits() {
            hyaenaSearchData.search = true;
            vm.navs = [{
                path: "/",
                oriName: "所有文件"
            }];
            vm.hyaenaCaptchaData = hyaenaCaptchaData;
            vm.navs.push({
                "path": $rootScope.$stateParams.path,
                "oriName": '搜索: '+$rootScope.$stateParams.path
            });

            vm.emptyFolder = false;
            hyaenaFileService.list_request('search').success(function (data, status, headers, config) {
                if (data.hyaenamsg == "请验证帐户验证码成功后再操作.") {
                    //need_captcha();
                } else {
                    vm.files = [];
                    if(data.data){
                        angular.forEach(data.data.node_list, function (value) {
                            vm.files.push({
                                'oriName':value.file_name,
                                'fileType':value.is_dir?'folder':'file',
                                'isDir':value.is_dir,
                                'path':value.file_location
                            });
                        });
                        $scope.emptyFolder = ($scope.files.length == 0);//空文件夹
                    }else{
                        $scope.emptyFolder = true;
                    }
                }
            });
        }

        function suffix_ico(suffix) {
            return hyaenaFileService.suffix_ico(suffix);
        }

        function file_img_init(item) {
            if (item.file.hasThumb) {//是否包含所要读取的图片流的格式
                item.file.imgSrc = "admin/clouddisk/file/readImgStream?imgUrl=" + item.file.thumb;
            }
            return "";
        }
    }

    function hyaenaCaptchaController($scope, $rootScope, hyaenaCaptchaService, hyaenaCaptchaData, $cookies, $timeout, $log) {
        var vm = $scope;
        vm.change_captcha = change_captcha;//更换验证码
        vm.captcha_load = captcha_load;//加载验证码
        vm.valid_captcha = valid_captcha;//验证验证码

        inits();

        function inits() {
            vm.disSubmit = true;
            vm.hyaenaCaptchaData = hyaenaCaptchaData;

            vm.$watch("captchaValue", function (newValue, oldValue, scope) {
                if (newValue)
                    vm.disSubmit = false;
                else
                    vm.disSubmit = true;
            });
        }

        function change_captcha() {
            hyaenaCaptchaData.url = hyaenaCaptchaData.path();
        }

        function captcha_load(event) {
            hyaenaCaptchaData.url = null;
        }

        function valid_captcha() {
            var postData = {
                "account": $cookies.clouddisk_account,
                "captchaValue": vm.captchaValue
            };
            vm.disSubmit = true;
            hyaenaCaptchaService.captcha_request(postData).success(function (data, status) {
                if (data.success) {
                    $("#exampleModal").modal("hide");//隐藏输入验证码对话框
                    $timeout(function () {
                        vm.disSubmit = false;
                        $rootScope.$state.go("fileList",{}, {
                            reload: true
                        });
                    },200);//不清楚为什么隐藏不了,需要定时
                } else {
                    change_captcha();
                    vm.has_error = "has-error";
                    vm.valid_captcha_msg = data.msg;
                    $timeout(function () {
                        vm.valid_captcha_msg = null;
                        vm.captchaValue = null;
                        vm.has_error = null;
                        vm.disSubmit = false;
                    }, 3000);
                }
            });
        }
    }

    function hyaenaFileController($scope, $rootScope, $http,hyaenaSearchData, hyaenaFileService, hyaenaService, hyaenaCaptchaService, hyaenaCaptchaData, $cookies) {
        var vm = $scope;
        vm.need_captcha = need_captcha;//检测是否需要验证码
        vm.list_inits = list_inits;//文件列表初始化
        vm.file_img_init = file_img_init;//文件图片初始化
        vm.item_dblclick = item_dblclick;//双击文件
        vm.nav_click = nav_click;//文件导航
        vm.suffix_ico = suffix_ico;//文件图标格式选择
        vm.folder_create = folder_create;//文件夹创建
        vm.hyaenaSearchData = hyaenaSearchData;

        inits();

        function inits() {
            if(hyaenaSearchData.search)return;

            vm.navs = [{
                path: "/",
                oriName: "所有文件"
            }];
            vm.hyaenaCaptchaData = hyaenaCaptchaData;
            var folders = $rootScope.$stateParams.path.split("/");
            var folderArr = [];
            angular.forEach(folders, function (value) {
                if (value) {
                    folderArr.push(value);
                    vm.navs.push({
                        "path": "/" + folderArr.join("/") + "/",
                        "oriName": value
                    });
                }
            });
            list_inits();
        }

        function need_captcha() {
            hyaenaService.need_captcha($cookies.clouddisk_account).success(function (data, status) {
                if (data) {//需要验证码
                    $("#exampleModal").modal("show");//显示输入验证码对话框
                    hyaenaCaptchaData.url = hyaenaCaptchaData.path();
                }
            });
        }

        function list_inits() {
            vm.files = [];
            vm.emptyFolder = false;
            hyaenaFileService.list_request().success(function (data, status, headers, config) {
                if (data.hyaenamsg == "请验证帐户验证码成功后再操作.") {
                    need_captcha();
                } else {
                    angular.forEach(data.data, function (value) {
                        if (value.oriName) {
                            vm.files.push(value);
                        }
                    });
                    $scope.emptyFolder = ($scope.files.length == 0);//空文件夹
                }
            });
        }

        function file_img_init(item) {
            if (item.file.hasThumb) {//是否包含所要读取的图片流的格式
                item.file.imgSrc = "admin/clouddisk/file/readImgStream?imgUrl=" + item.file.thumb;
            }
            return "";
        }

        function item_dblclick(item) {
            hyaenaSearchData.search = false;
            if (item.file.isDir) {//文件夹打开
                $rootScope.$state.go("fileList", {path: item.file.path});
            }
        }

        function nav_click(nav) {
            hyaenaSearchData.search = false;
            $rootScope.$state.go("fileList", {path: nav.path});
        }

        function suffix_ico(suffix) {
            return hyaenaFileService.suffix_ico(suffix);
        }

        function folder_create() {

        }
    }
});

