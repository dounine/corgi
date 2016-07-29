define(['app'], function(app) {
    "use strict";

    app.directive("sbLoad", function ($parse) {
        return {
            restrict: "A",
            link: function (scope, elem, attrs) {
                var fn = $parse(attrs.sbLoad);
                elem.on("load", function (event) {
                    scope.$apply(function () {
                        fn(scope, {$event: event});
                    });
                });
            }
        };
    });

});

