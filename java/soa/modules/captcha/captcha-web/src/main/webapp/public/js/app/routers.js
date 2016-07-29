define(["app"], function(app) {
    "use strict";

    app.run([
            '$rootScope',
            '$state',
            '$stateParams',
            function ($rootScope, $state, $stateParams) {
                $rootScope.$state = $state;
                $rootScope.$stateParams = $stateParams
            }
        ]);
    app.config(function ($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/index");
        $stateProvider.state("index", {
            url : "/index",
            views : {
                "" : {
                    templateUrl : "core/content",
                    controller: "captureController"
                },
                "database@index":{
                    templateUrl:"core/tmp",
                    controller:"databaseController"
                },
                "service@index":{
                    templateUrl:"core/tmp",
                    controller:"serviceController"
                },
                "controller@index":{
                    templateUrl:"core/tmp",
                    controller:"controllerController"
                },
                "view@index":{
                    templateUrl:"core/tmp",
                    controller:"viewController"
                }
            }
        })
    })
});