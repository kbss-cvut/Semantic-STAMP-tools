angular.module('InbasApp.controllers.Report', ['InbasApp.storage','InbasApp.camera'])
.factory('storage', [function () {
	var storage = new Storage();
	return storage;
}])

.controller('ReportController', function($scope,storage,camera){
	if($scope.saved == null){
		$scope.saved = [];
	}

	$scope.save = function(report){
		if((report != null)&& ($scope.report.id != null)){

			if($scope.saved == null){
				$scope.saved = angular.copy(report);
				return false;
			}

			$scope.saved = $scope.saved.concat([angular.copy(report)]);
			
			var report = JSON.stringify($scope.report);
			console.log("Report:" + report);	

			storage.writeRecord($scope.report.id, report, function(response) {
				if (response.error) {
					alert("Při ukládání došlo k chybě");
				}
				callback();
			});

			$scope.report.id += 1;

		}
	};

			
			
	$scope.capturePicture = function() {
		console.log("Trying to capture Picture");
		camera.capturePhoto();
		/*navigator.camera.getPicture(function(filename) {
			$scope.$apply(function() {
				media.push(filename);
			});*/
		
	};




});