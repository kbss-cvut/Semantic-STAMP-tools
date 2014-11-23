angular.module('InbasApp', [
  'ngRoute',
  'ngTouch',
  'mobile-angular-ui',
  'InbasApp.controllers.Main',
  'InbasApp.controllers.Report'
])

.config(function($routeProvider) {
  $routeProvider.when('/', {templateUrl: 'home.html'});
});