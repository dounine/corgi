/*jshint unused: vars */
require.config({
    baseUrl: 'public/js',
    paths: {
        'app': 'app',
        'routers':'app/routers',
        'module':'app/module',
        'dataservices':'app/dataservices',
        'directives':'app/directives',
        'filters':'app/filters',
        'services':'app/services',
        'jquery': 'lib/jquery/jquery',
        'bootstrap':'lib/bootstrap/bootstrap',
        'angular': 'lib/angular/angular',
        'css':'lib/require-css/css',
        'tether':'app/require-wrapper/tetherWrapper',
        'normalize':'lib/require-css/normalize',
        'angular-route': 'lib/angular-ui-router/angular-ui-router',
        'icheck': 'lib/iCheck/icheck.min',
        'flat-ui': 'lib/flat-ui/flat-ui',
        'angular-animate':'lib/angular-animate/angular-animate',
        'angular-file-upload':'lib/angular-file-upload/angular-file-upload.min',
        'angular-loading-bar':'lib/angular-loading-bar/loading-bar',
        'angular-cookies':'lib/angular-cookies/angular-cookies'
    },
    shim: {
        'jquery':{
            'exports':'$'
        },
        'angular' : {
            'deps':['jquery'],
            'exports' : 'angular'
        },
        'angular-animate':{
            'deps':['angular']
        },
        'icheck':{
            'deps':['jquery']
        },
        'bootstrap':{
            'deps':[
                'tether',
                'jquery'
            ]
        },
        'angular-route': {
            'deps':['angular']
        },
        'angular-loading-bar':{
            'deps':[
                'angular'
            ]
        },
        'angular-file-upload':['angular'],
        'angular-cookies': ['angular']
    },
    priority: [
        'angular'
    ],
    packages: [

    ],
    urlArgs: "______t=" + new Date().getTime()  //防止读取缓存，调试用
});

//http://code.angularjs.org/1.2.1/docs/guide/bootstrap#overview_deferred-bootstrap
window.name = 'NG_DEFER_BOOTSTRAP!';

require([
    'angular',
    'app',
    'icheck',
    'dataservices',
    'directives',
    'filters',
    'routers',
    'bootstrap',
    'angular-route',
    'angular-cookies',
    'angular-animate',
    'angular-loading-bar',
    'angular-file-upload'
], function(angular, app) {
    'use strict';
    /* jshint ignore:start */
    var $html = angular.element(document.getElementsByTagName('html')[0]);
    /* jshint ignore:end */

    angular.element().ready(function() {
        angular.resumeBootstrap([app.name]);
    });

});
