angular.module('InbasApp', [
	'ngRoute',
	'ngTouch',
	'mobile-angular-ui',
	'InbasApp.recordsScreen',
	'InbasApp.controllers.Main',
	'InbasApp.controllers.Report',
	'InbasApp.camera'
	])

.config(function($routeProvider) {
	$routeProvider
	.when('/', {templateUrl: 'home.html'})
	.when('/records', {
		templateUrl: 'records.html',
		controller: 'RecordsController'
	});
});

angular.element(document).ready(function() {
	document.addEventListener("deviceready", function() {
		angular.bootstrap(document, ['InbasApp']);
	},false);
});