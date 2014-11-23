angular.module('InbasApp.controllers.Report', [])

.controller('ReportController', function($scope){


	if($scope.saved == null){
		$scope.saved = [];
	}

	$scope.save = function(report){
		if((report != null)&& ($scope.report.id != null)){

			if($scope.saved == null){
				$scope.saved = angular.copy(report);
				return false;
			}

			$scope.saved = $scope.saved.concat([
				angular.copy(report)

				]);
			$scope.report.id += 1;
			
		}
	};



});