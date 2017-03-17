// ------ themes ----------
var CSS_DEFAULT = 'sivale.css';
var LOGO_DEFAULT = '';
var STRING_DEFAULT = '';


// ------- Classification Campaings ----------
var COMPANY_SELECT		= 0;
var PROGRAM_SELECT		= 1;
var SUBPROGRAM_SELECT	= 2;
var BUSINESSUNIT_SELECT	= 3;
var LEVEL4_SELECT		= 4;
var LEVEL5_SELECT		= 5;

var SUCCESS_CODE = "001";
var ERROR_CODE 	= "002";

var appres = angular.module('app', [ 'ngMessages', 'daterangepicker',
		'ngTable', 'ui.router', 'ng-toggle.btn', 'cgNotify' ])
		
appres.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

appres.run(function($rootScope) {
	$rootScope.search = {
		campaignName : STRING_DEFAULT,
		classification : STRING_DEFAULT
	};

	$rootScope.classif = {
		company : STRING_DEFAULT
	};

	$rootScope.files = {
		html : '',
		excel : ''
	};
	
})

appres.controller('campaignAdminController', ['$scope', 'upload', '$filter', '$rootScope', '$http', 'NgTableParams', '$state', 'notify',
		function($scope, upload, $filter, $rootScope, $http, NgTableParams, $state, notify) {
				    
					$scope.date = {
						startDate : moment(),
						endDate : moment()
					};

					$scope.buttonCampaig = '';
					
					$scope.rows = [];
					  
					$scope.counter = 0;
					
					$scope.rows2 = [];
					  
					$scope.counter2 = 0;
					
					$scope.attachedFileTemp = '';
					
					$scope.selectPublicationView = {
							availableOptions : [{ id : '1', name : 'Tarjetahabiente' },
			                                    { id : '2', name : 'Etiquetas' } ],
							selectedOption : { id : '2', name : 'Etiquetas' },
							cardNumber : ''
					};
					
					$scope.updateAttachedFile = function(attachedfile, index) {
						$scope.attachedFileTemp = attachedfile;
						$scope.attachedFileTemp.index = index;
					}
					
					$scope.updateStatusPublication = function(publication){
						
						publication.isEnable = !publication.isEnable;
//						publication.imagePath = null; 
						
						var data = angular.toJson(publication);
						
						$http({
							method : 'POST',
							url : 'updateStatusPublicationAction',
							data : 'publication=' + data,
							headers : { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' }
						  }).success(function(data, status, headers, config) {
							  $scope.showNotify(data);
						  }).error(function(data, status, headers, config) {
						  });
						
					}
					
					$scope.changeAttachedFileStatus = function(attachedfile, index) {
					  
						$scope.updateAttachedFile(attachedfile, index);
						
						if($scope.attachedFileTemp.isPublic)
							$scope.attachedFileTemp.isPublic = false;
						else
							$scope.attachedFileTemp.isPublic = true;
						
					    $('#modalChangeAttachedFile').modal({
					        show: true
					    });
					    
					 }
					  
					$scope.addRow = function() {
					    
						$scope.rows.push( {index : $scope.counter + 2, value : false} );
						$scope.counter++;
					}
					
					$scope.removeRow = function(index){
					    $scope.rows.splice(index, 1);
					}
					
					$scope.addRow2 = function() {
					    
						$scope.rows2.push( {index : $scope.counter2 + 2, value : false} );
						$scope.counter2++;
					}
					
					$scope.removeRow2 = function(index){
					    $scope.rows2.splice(index, 1);
					}
					
					$scope.removeAttachedFile = function(index){
					    $scope.updateAttachedFiles.splice(index, 1);
					}
					
					$scope.deleteAttacherFile = function() {
						
						var data = angular.toJson($scope.attachedFileTemp);
						
						$http({
							method : 'POST',
							url : 'deleteAttachedFileAction',
							data : 'attachedFile=' + data,
							headers : { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' }
						  }).success(function(data, status, headers, config) {
							  if(data.code = SUCCESS_CODE){
								  $scope.removeAttachedFile($scope.attachedFileTemp.index);
							  }
							  $scope.showNotify(data);
						  }).error(function(data, status, headers, config) {
							  
						  });
						
					};
					
					$scope.updateAttacherFile = function() {
						
						var data = angular.toJson($scope.attachedFileTemp);
						
						$http({
							method : 'POST',
							url : 'updateAttachedFileAction',
							data : 'attachedFile=' + data,
							headers : { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' }
						  }).success(function(data, status, headers, config) {
							  $scope.showNotify(data);
						  }).error(function(data, status, headers, config) {
							  
						  });
						
					};
					
					$scope.uploadFile = function()
					{
						var file = $rootScope.files.html;
						console.log(file);
						
						
						var data = angular.toJson($rootScope.files.html);
						
						$http({
							method : 'POST',
							url : 'uploadFileAction',
							data : 'file=' + data,
							headers : { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' }
						  }).success(function(data, status, headers, config) {
							  
						  }).error(function(data, status, headers, config) {
							  
						  });
						
					};
					
					
					
					$scope.getCampaignsAdmin = function() {

						$http.get('getCampaignsAdminAction').success(
								function(data, status, headers, config) {
									$scope.campaigns = data;
									
									var index2;
									for (index2 = 0; index2 < $scope.campaigns.length; index2++) {
										$scope.campaigns[index2].indexImage2 = $scope.getIndex(); 
									}

								}).error(
								function(data, status, headers, config) {
								});
					};

					$scope.getCampaign = function() {

						var data = angular.toJson($scope.campaign);

						$http({
									method : 'POST',
									url : 'getPublicationsAdminAction',
									data : 'campaign=' + data,
									headers : { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' }
								})
								.success(
										function(data, status, headers, config) {
											$scope.publications = data;
											console.log(JSON.stringify(data));

											$scope.tablePublications = new NgTableParams({
												page : 1,
												count : 10,
												filter : $scope.filters,
											}, {
												total : $scope.publications.length,
												counts : [],
												getData : function($defer, params) {
													var filteredData = params.filter() ? $filter(
															'filter')($scope.publications,
															params.filter().myfilter)
															: $scope.publications;

													var orderedData = params.sorting() ? $filter(
															'orderBy')(filteredData, params.orderBy())
															: $scope.publications;

													$defer.resolve(orderedData.slice(
															(params.page() - 1) * params.count(),
															params.page() * params.count()));
												}
											});

										}).error(function(data, status, headers, config) {

								});
					};

					$scope.triggerModal = function(){
						$("#modalViewPublication").modal(
								{backdrop: 'static', keyboard: false});
					}
					
					$scope.viewPublication = function(){
						
						if($scope.selectPublicationView.selectedOption.id == 1){
							
							if($scope.selectPublicationView.cardNumber != null ){
								$scope.showPublicationAsTH($scope.selectPublicationView.cardNumber);
							}
						}
						else{
							$scope.showPublication();
						}
					}
					
					$scope.showPublicationAsTH = function(cardNumber) {
						$scope.publication.acoundNumber = cardNumber;
						
						var data = angular.toJson($scope.publication);
						$scope.loadingPublicationImage = true;
						$scope.files = false;

						$http({
							method : 'POST',
							url : 'showPublicationAsTHAction',
							data : 'publication=' + data,
							headers : {
								'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
							}
						})
						.success(
								function(data, status, headers, config) {
									$scope.attachedFiles = data.listFiles;
									
									$scope.imageOfPublication = data.image;
									
									
									if(data.image != null ){
										
										$('body').addClass('image-th-p');
										$('.image-th-p').css({ backgroundImage: "url("+data.image+")" });
										$("#modalViewPublication").modal('hide');
										
									}
									else{
										
									}
									
									$scope.tableAttachedFiles = new NgTableParams(
											{
												count : 10
											},
											{
												counts : [],
												dataset : $scope.attachedFiles
											});
									
									$scope.loadingPublicationImage = false;
									$scope.files = true;
									

									$(".publication-html").html(data.html);
									//$state.go('publication');
								}).error(function(data, status, headers, config) {
						});
					};
					
					$scope.showPublication = function() {

						var data = angular.toJson($scope.publication);
						$scope.loadingPublicationImage = true;
						$scope.files = false;

						$http({
							method : 'POST',
							url : 'showPublicationAction',
							data : 'publication=' + data,
							headers : {
								'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
							}
						})
						.success(
								function(data, status, headers, config) {
									$scope.attachedFiles = data.listFiles;
									
									$scope.imageOfPublication = data.image;
									
									
									if(data.image != null ){
										
										$('body').addClass('image-th-p');
										$('.image-th-p').css({ backgroundImage: "url("+data.image+")" });
										$("#modalViewPublication").modal('hide');
										
									}
									else{
										
									}
									
									$scope.tableAttachedFiles = new NgTableParams(
											{
												count : 10
											},
											{
												counts : [],
												dataset : $scope.attachedFiles
											});
									
									$scope.loadingPublicationImage = false;
									$scope.files = true;


									$(".publication-html").html(data.html);
//									$state.go('publication');
								}).error(function(data, status, headers, config) {
						});
					};
					
					$scope.getAttachedFilesByPublication = function() {

						var data = angular.toJson($scope.publication);

						$http({
							method : 'POST',
							url : 'showPublicationAction',
							data : 'publication=' + data,
							headers : {
								'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
							}
						}).success(
								function(data, status, headers, config) {
									
									$scope.updateAttachedFiles = data.listFiles;
									
									var options =  [{ id : '1', name : 'Privado' },
			                                    	{ id : '2', name : 'Público' } ];
									
									for (i = 0 ; i < $scope.updateAttachedFiles.length; i++){
										$scope.updateAttachedFiles[i].options = options;
										if ($scope.updateAttachedFiles[i].isPublic)
											$scope.updateAttachedFiles[i].selected = { id : '2', name : 'Público' };
										else
											$scope.updateAttachedFiles[i].selected = { id : '1', name : 'Privado' };
									}

								}).error(function(data, status, headers, config) {
						});
					};
					
					$scope.updateSession = function() {
						$http.get('updateSessionAction').success(
								function(data, status, headers, config) {

									$scope.user = data.user;
									$scope.campaign = data.campaign;
									$scope.publication = data.publication;

								}).error(
								function(data, status, headers, config) {
								});
					};

					$scope.updateCampaign = function(campaign) {
						$scope.campaign = campaign;
					};

					$scope.formatDate = function(date) {
						var d = new Date(date || Date.now()), month = ''
								+ (d.getMonth() + 1), day = '' + d.getDate(), year = d
								.getFullYear();

						if (month.length < 2)
							month = '0' + month;
						if (day.length < 2)
							day = '0' + day;

						return [ month, day, year ].join(' / ');
					};

					$scope.convertToPositive = function(amount) {
						if (amount == undefined)
							return 0;
						if (amount < 0)
							return amount * -1;
						return amount;
					};
					
					$scope.getFile = function(file) {
						$http.get('getFileAction?fileName='+file.fileName+'.'+file.fileExtension).success(
								function(data, status, headers, config) {

									$scope.downloadFile(file.fileName+'.'+file.fileExtension, data)
								
								}).error(function(data, status, headers, config) {
									
							});
					};
					
					$scope.updatePublication = function(publication) {
						$scope.publication = publication;
					};

					
					$scope.downloadFile = function(name,file) {

				        var octetStreamMime = 'application/octet-stream';
				        var success = false;         
				        var filename = name;
				        var contentType =  octetStreamMime;              
				        
				       var fileDownload = new Uint8Array(file);
				        try
				        {
				            // Try using msSaveBlob if supported
				            console.log("Trying saveBlob method ...");
				            var blob = new Blob([fileDownload], { type: contentType });
				            if(navigator.msSaveBlob)
				                navigator.msSaveBlob(blob, filename);
				            else {
				                // Try using other saveBlob implementations, if available
				                var saveBlob = navigator.webkitSaveBlob || navigator.mozSaveBlob || navigator.saveBlob;
				                if(saveBlob === undefined) throw "Not supported";
				                saveBlob(blob, filename);
				            }
				            console.log("saveBlob succeeded");
				            success = true;
				        } catch(ex)
				        {
				            console.log("saveBlob method failed with the following exception:");
				            console.log(ex);
				        }

				        if(!success)
				        {
				            // Get the blob url creator
				            var urlCreator = window.URL || window.webkitURL || window.mozURL || window.msURL;
				            if(urlCreator)
				            {
				                // Try to use a download link
				                var link = document.createElement('a');
				                if('download' in link)
				                {
				                    // Try to simulate a click
				                    try
				                    {
				                        // Prepare a blob URL
				                        console.log("Trying download link method with simulated click ...");
				                        var blob = new Blob([fileDownload], { type: contentType });
				                                                
				                        
				                        var url = urlCreator.createObjectURL(blob);
				                        link.setAttribute('href', url);

				                        // Set the download attribute (Supported in Chrome 14+ / Firefox 20+)
				                        link.setAttribute("download", filename);

				                        // Simulate clicking the download link
				                        var event = document.createEvent('MouseEvents');
				                        event.initMouseEvent('click', true, true, window, 1, 0, 0, 0, 0, false, false, false, false, 0, null);
				                        link.dispatchEvent(event);
				                        console.log("Download link method with simulated click succeeded");
				                        success = true;

				                    } catch(ex) {
				                        console.log("Download link method with simulated click failed with the following exception:");
				                        console.log(ex);
				                    }
				                }

				                if(!success)
				                {
				                    // Fallback to window.location method
				                    try
				                    {
				                        // Prepare a blob URL
				                        // Use application/octet-stream when using window.location to force download
				                        console.log("Trying download link method with window.location ...");
				                        var blob = new Blob([fileDownload], { type: octetStreamMime });
				                        var url = urlCreator.createObjectURL(blob);
				                        window.location = url;
				                        console.log("Download link method with window.location succeeded");
				                        success = true;
				                    } catch(ex) {
				                        console.log("Download link method with window.location failed with the following exception:");
				                        console.log(ex);
				                    }
				                }
				            }
				        }

				        if(!success)
				        {
				            // Fallback to window.open method
				            console.log("No methods worked for saving the arraybuffer, using last resort window.open");
				            window.open(httpPath, '_blank', '');
				        }    
				};

				$scope.searchCampaigns = function(date) {
				    
					var searchCampaign = {
							campaignName : $rootScope.search.campaignName,
							classificationName1 : $rootScope.search.classification,
							company : $rootScope.classif.company,
							startDate : date.startDate,
							endDate : date.endDate
					};
					
					$rootScope.search.campaignName = STRING_DEFAULT;
					$rootScope.search.classification = STRING_DEFAULT;
					$rootScope.classif.company = STRING_DEFAULT;
					
					var data = angular.toJson(searchCampaign);
					console.log(JSON.stringify(searchCampaign));

					$http({
						method : 'POST',
						url : 'searchCampaignsAdminAction',
						data : 'searchCampaign=' + data,
						headers : {
							'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
						}
					}).success(
							function(data, status, headers, config) {
								
								console.log('data: '+JSON.stringify(data));
								
								$scope.campaigns = data;
								$scope.tableCampaigns = new NgTableParams({
									count : 10
								}, {
									counts : [],
								    dataset: data
								});
								
							}).error(function(data, status, headers, config) {

					});
				};

/************************************************************************
================================ New Campaign Start =====================
************************************************************************/
					
					$scope.select = 0;
					$scope.form = {
							campaignName : ''
					};
					
					$scope.getPublicationTypes = function() {
						
						$http.get('getPublicationTypesAction').success(
								function(data, status, headers, config) {

									if(data.length > 0){
										$scope.selectPublicationType.availableOptions = data;
										$scope.selectPublicationType.selectedOption = data[0];
									}
								}).error(
								function(data, status, headers, config) {
									
								});
					};
					
					$scope.getPublicationTypesUpdate = function() {
						
						$http.get('getPublicationTypesAction').success(
								function(data, status, headers, config) {

									$scope.selectPublicationType.availableOptions = data;
									var index = 0;
									
									for(i = 0; index < $scope.selectPublicationType.availableOptions.length; i++){
										if($scope.selectPublicationType.availableOptions[i].name == $scope.publication.catPublicationType.name){
											index = i;
											break;
										}
									}
									
									$scope.selectPublicationType.selectedOption = $scope.selectPublicationType.availableOptions[index];
						
								}).error(
								function(data, status, headers, config) {
									
								});
					};
					
					$scope.getSelectClassList = function() {
						if($scope.select == COMPANY_SELECT)
							var action = 'getClassificationLevelAction?classificationId=-1';
						else
							var action = 'getClassificationLevelAction?classificationId='+$scope.selectCampaign.items[$scope.select-1].selectedOption.id;
						
						$http.get(action).success(
								function(data, status, headers, config) {

									switch ($scope.select) {
								    	case COMPANY_SELECT:
								    		$scope.selectCampaign.items[0].availableOptions = data;
								    		break;
								    	case PROGRAM_SELECT:
								    		$scope.selectCampaign.items[1].availableOptions = data;
								    		break;
								    	case SUBPROGRAM_SELECT:
								    		$scope.selectCampaign.items[2].availableOptions = data;
								    		break;
								    	case BUSINESSUNIT_SELECT:
								    		$scope.selectCampaign.items[3].availableOptions = data;
								    		break;
								    	case LEVEL4_SELECT:
								    		$scope.selectCampaign.items[4].availableOptions = data;
								    		break;
								    	case LEVEL5_SELECT:
								    		$scope.selectCampaign.items[5].availableOptions = data;
								    		break;
									}
						
								}).error(
								function(data, status, headers, config) {
									
								});
					};
					
					$scope.getClassCompanyList = function() {
						
						$scope.formCampaingInit();
						$scope.select = COMPANY_SELECT;
						$scope.getSelectClassList();
					};
					
					$scope.getClassProgramList = function() {
						
						$scope.select = PROGRAM_SELECT;
						$scope.getSelectClassList();
					};
					
					$scope.getClassSubProgramList = function() {
						
						$scope.select = SUBPROGRAM_SELECT;
						$scope.getSelectClassList();
					};
					
					$scope.getClassBusinessUnitList = function() {
						
						$scope.select = BUSINESSUNIT_SELECT;
						$scope.getSelectClassList();
					};
					
					$scope.getClassLevel4List = function() {
						
						$scope.select = LEVEL4_SELECT;
						$scope.getSelectClassList();
					};
					
					$scope.getClassLevel5List = function() {
						
						$scope.select = LEVEL5_SELECT;
						$scope.getSelectClassList();
					};
					
					$scope.getSelectNone = function() {
						var data = { id : '-1', name : 'Ninguno' };
						return data;
					};
					
					$scope.getBasicSelect = function() {
						var data =  [	{ id : '-1', name : 'Ninguno' }
										,{ id : '-2', name : 'Añadir nuevo' } 
										
										];
						return data;
					};
					
					
					$scope.changeCompany = function() {
						$scope.getClassProgramList();
						
						$scope.selectCampaign.items[1].selectedOption =	$scope.getSelectNone();
						$scope.select == COMPANY_SELECT
						$scope.selectCampaign.items[2].selectedOption =	$scope.getSelectNone();
						$scope.selectCampaign.items[3].selectedOption =	$scope.getSelectNone();
						$scope.selectCampaign.items[4].selectedOption =	$scope.getSelectNone();
						$scope.selectCampaign.items[5].selectedOption =	$scope.getSelectNone();

					};

					
					$scope.changeProgram = function() {
						$scope.selectCampaign.items[1].className = '';
						$scope.selectCampaign.items[2].className = '';
						$scope.selectCampaign.items[3].className = '';
						$scope.selectCampaign.items[4].className = '';
						$scope.selectCampaign.items[5].className = '';

						if ($scope.selectCampaign.items[1].selectedOption.name == 'Añadir nuevo') {

							$scope.selectCampaign.items[1].className = '';

							$scope.selectCampaign.items[2].availableOptions = $scope.getBasicSelect();
							$scope.selectCampaign.items[2].selectedOption =	$scope.getSelectNone();

							$scope.selectCampaign.items[3].availableOptions = $scope.getBasicSelect();
							$scope.selectCampaign.items[3].selectedOption =	$scope.getSelectNone();

							$scope.selectCampaign.items[4].availableOptions = $scope.getBasicSelect();
							$scope.selectCampaign.items[4].selectedOption =	$scope.getSelectNone();

							$scope.selectCampaign.items[5].availableOptions = $scope.getBasicSelect();
							$scope.selectCampaign.items[5].selectedOption =	$scope.getSelectNone();
							
						} else {
							
							$scope.getClassSubProgramList();
							
							$scope.selectCampaign.items[2].selectedOption =	$scope.getSelectNone();
							$scope.selectCampaign.items[3].selectedOption =	$scope.getSelectNone();
							$scope.selectCampaign.items[4].selectedOption =	$scope.getSelectNone();
							$scope.selectCampaign.items[5].selectedOption =	$scope.getSelectNone();
							
						}

					};

					$scope.changeSubProgram = function() {
						$scope.selectCampaign.items[2].className = '';
						$scope.selectCampaign.items[3].className = '';
						$scope.selectCampaign.items[4].className = '';
						$scope.selectCampaign.items[5].className = '';

						if ($scope.selectCampaign.items[2].selectedOption.name == 'Añadir nuevo') {

							$scope.selectCampaign.items[2].className = '';

							$scope.selectCampaign.items[3].availableOptions = $scope.getBasicSelect();
							$scope.selectCampaign.items[3].selectedOption =	$scope.getSelectNone();

							$scope.selectCampaign.items[4].availableOptions = $scope.getBasicSelect();
							$scope.selectCampaign.items[4].selectedOption =	$scope.getSelectNone();

							$scope.selectCampaign.items[5].availableOptions = $scope.getBasicSelect();
							$scope.selectCampaign.items[5].selectedOption = 	$scope.getSelectNone();
						
						} else {
							
							$scope.getClassBusinessUnitList();
							
							$scope.selectCampaign.items[3].selectedOption =	$scope.getSelectNone();
							$scope.selectCampaign.items[4].selectedOption =	$scope.getSelectNone();
							$scope.selectCampaign.items[5].selectedOption =	$scope.getSelectNone();
						
						}
					};

					$scope.changeBusinessUnit = function() {
						$scope.selectCampaign.items[3].className = '';
						$scope.selectCampaign.items[4].className = '';
						$scope.selectCampaign.items[5].className = '';

						if ($scope.selectCampaign.items[3].selectedOption.name == 'Añadir nuevo') {

							$scope.selectCampaign.items[3].className = '';

							$scope.selectCampaign.items[4].availableOptions = $scope.getBasicSelect();
							$scope.selectCampaign.items[4].selectedOption =	$scope.getSelectNone();

							$scope.selectCampaign.items[5].availableOptions = $scope.getBasicSelect();
							$scope.selectCampaign.items[5].selectedOption =	$scope.getSelectNone();
						} else {
							$scope.getClassLevel4List();

							$scope.selectCampaign.items[4].selectedOption =	$scope.getSelectNone();
							$scope.selectCampaign.items[5].selectedOption =	$scope.getSelectNone();
						}
					};

					$scope.changeLevel4 = function() {
						$scope.selectCampaign.items[4].className = '';
						$scope.selectCampaign.items[5].className = '';

						if ($scope.selectCampaign.items[4].selectedOption.name == 'Añadir nuevo') {

							$scope.selectCampaign.items[4].className = '';

							$scope.selectCampaign.items[5].availableOptions = $scope.getBasicSelect();
							$scope.selectCampaign.items[5].selectedOption =	$scope.getSelectNone();
							
						} else {
							
							$scope.getClassLevel5List();

							$scope.selectCampaign.items[5].selectedOption =	$scope.getSelectNone();
						}
					};

					$scope.changeLevel5 = function() {
						$scope.selectCampaign.items[5].className = '';
					};


					$scope.formCampaingInit = function(){
						
						for (i = 0; i < $scope.selectCampaign.length; i++) {
							$scope.selectCampaign.items[i].selectedOption = '';
							$scope.selectCampaign.items[i].availableOptions = '';
						}
					};
					
					$scope.selectPublicationType = {
							availableOptions : '',
							selectedOption : ''
					};
					
					$scope.selectCampaign = { items : [{availableOptions : '', selectedOption : '', className : ''},
					                                   {availableOptions : '', selectedOption : '', className : ''},
					                                   {availableOptions : '', selectedOption : '', className : ''},
					                                   {availableOptions : '', selectedOption : '', className : ''},
					                                   {availableOptions : '', selectedOption : '', className : ''},
					                                   {availableOptions : '', selectedOption : '', className : ''}],
					                          isEnabled : 	function(item) {

					                        	  				if (item.selectedOption != '') {
					                        	  					if ((item.selectedOption.name == 'Añadir nuevo' || item.selectedOption.name == 'Ninguno')
					                        	  							&& item.className == '')
					                        	  						return false;
					                        	  					return true;
					                        	  				}
					                        	  				return false;
					                          				},
					                          isNewClass : function(item) {
					                        	  				if (item.selectedOption.name == 'Añadir nuevo')
					                        	  					return true;
					                        	  				return false;
					                          				}
					                        };

					$scope.cleanSelect = function(){
						$scope.form.campaignName = '';
						$scope.date.startDate = moment();
						$scope.date.endDate = moment();
						
						$scope.selectCampaign.items[0].selectedOption = '';
						for(i = 1 ; i < $scope.selectCampaign.items.length; i++){
							$scope.selectCampaign.items[i].availableOptions =	$scope.getBasicSelect();
							$scope.selectCampaign.items[i].selectedOption =	$scope.getSelectNone();
						}
					};
					
					$scope.getCampaignForm = function(){
						
						var data = angular.toJson($scope.campaign);

						$http({
								method : 'POST',
								url : 'getCampaignUpdateAction',
								data : 'campaignDetail=' + data,
								headers : { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' }
							  }).success(function(data, status, headers, config) {
								  console.log(JSON.stringify(data));
								  
								  $scope.form.campaignName = $scope.campaign.campaignName;
								  $scope.date.startDate = $scope.campaign.startDate;
								  $scope.date.endDate = $scope.campaign.endDate;
								  
								  for (i=0 ; i<$scope.selectCampaign.items.length; i++){
									  if(i < data.length){
										  $scope.selectCampaign.items[i].availableOptions	= data[i].availableOptions;
										  $scope.selectCampaign.items[i].selectedOption		= data[i].selectedOption;
									  }
									  else{
										  $scope.selectCampaign.items[i].availableOptions = $scope.getBasicSelect();
										  $scope.selectCampaign.items[i].selectedOption =	$scope.getSelectNone();
									  }
								  }
								  								  
							  }).error(function(data, status, headers, config) {
								  
							  });
												
					};
					
					function UInt8ArrayToString(uInt8Array)
					{
					    var s= "";
					    for(var i= number = 0; i < uInt8Array.byteLength; i++)
					    {
					        if( i > 0 )
					            s += ",";
					        s += uInt8Array[i];
					    }    
					    return s;
					}
					
					$scope.newCampaignForm = function(date){
												
				        var file = $scope.form.myFile, r = new FileReader();
				        console.log('file is ' );
				        console.dir(file);
				        
				        r.onloadend = function(e) {
				    		var fileBytes = new Uint8Array(e.target.result);
				    		var fileBytesStr = UInt8ArrayToString(fileBytes);
				    		
				    		if(file.size >= 204800){
				    			alert("El tamaño del archivo sobrepasa el limite permitido: 200KB");
				    			return; 
				    		}
				    		
				    		var campaignForm = {
				    				nameFile : file.name, 
				    				imageFile : fileBytesStr, 
									campaignName : $scope.form.campaignName,
									startDate : date.startDate,
									endDate : date.endDate,
									classificationList : []
							};
				    		
				    		$scope.form.myFile = null; 
							
							campaignForm.classificationList.push($scope.selectCampaign.items[0].selectedOption);		
					
							for (i = 1; i < $scope.selectCampaign.items.length; i++) { 
								if($scope.selectCampaign.items[i].selectedOption.id != '-2')
									campaignForm.classificationList.push($scope.selectCampaign.items[i].selectedOption);
								else {
									campaignForm.classificationList.push({ id : '-2', name : $scope.selectCampaign.items[i].className });
								}
							}
							
							var data =angular.toJson(campaignForm);
							
							$http({
								method : 'POST',
								url : 'addCampaignAction',
								data : 'formNewCampaign=' + data,
								headers : {
									'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
								}
							}).success(
									function(data, status, headers, config) {
										$scope.showNotify(data);
										$state.go('home');
									}).error(function(data, status, headers, config) {
							});
		
						}
						
						r.readAsArrayBuffer(file);
						
					};
					
						
					$scope.updateCampaignForm = function(date){
						
						var campaignForm = {
								campaignId : $scope.campaign.campaignId,
								campaignName : $scope.form.campaignName,
								startDate : date.startDate,
								endDate : date.endDate,
								classificationList : []
						};
						
						campaignForm.classificationList.push($scope.selectCampaign.items[0].selectedOption);		
				
						for (i = 1; i < $scope.selectCampaign.items.length; i++) { 
							if($scope.selectCampaign.items[i].selectedOption.id != '-2')
								campaignForm.classificationList.push($scope.selectCampaign.items[i].selectedOption);
							else {
								campaignForm.classificationList.push({ id : '-2', name : $scope.selectCampaign.items[i].className });
							}
						}
						
						var data = angular.toJson(campaignForm);
						console.log(JSON.stringify(data));
						
						$http({
							method : 'POST',
							url : 'updateCampaignAction',
							data : 'formNewCampaign=' + data,
							headers : {
								'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
							}
						}).success(
								function(data, status, headers, config) {
									$scope.showNotify(data);
									$state.go('home');
								}).error(function(data, status, headers, config) {
									
						});
						
					};
					
					$scope.deleteCampaign = function() {
						$http.get('deleteCampaignAction?campaignId='+$scope.campaign.campaignId).success(
								function(data, status, headers, config) {
									$scope.showNotify(data);
									$state.go('home');
								}).error(function(data, status, headers, config) {
							});
					};
					
					$scope.deletePublication = function() {
						$http.get('deletePublicationAction?publicationId='+$scope.publication.publicationId).success(
								function(data, status, headers, config) {
									$scope.showNotify(data);
									$state.go('campaign');
								}).error(function(data, status, headers, config) {
							});
					};
					
					$scope.showNotify = function(data){
						
						if (data.code == SUCCESS_CODE){
							var messageTemplate = '<span>' + data.message +'</span>';
							notify({
					            messageTemplate: messageTemplate,
					            classes: "alert-danger",
					            position: 'center',
					            duration: 10000
					        });
						}
						else if(data.code == ERROR_CODE){
							var messageTemplate = '<span>' + data.message +'</span>';
							notify({
					            messageTemplate: messageTemplate,
					            classes: "alert-danger",
					            position: 'center',
					            duration: 10000
					        });
						}
					};
					
					
					var imageCampaignArray = [1,2,3,4,5];
					
					//funcion para colocar imagen en estado de campañas
					
					$scope.getIndex = function(){
						
						var indexImage = imageCampaignArray.pop();
						
						if(indexImage == null){
							imageCampaignArray.push(1,2,3,4,5);
							indexImage = imageCampaignArray.pop();
						}
						
						return indexImage; 
					}
/*********************************************************************************
=============================== New Campaign End =================================
*********************************************************************************/
					
					$scope.getRM = function() {

						var data = angular.toJson($scope.campaign);
						
						console.log("FUNCION RM");
						
						

						$http({
									method : 'POST',
									url : 'getListRMAction',
									data : 'campaign=' + data,
									headers : { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' }
								})
								.success(
										function(data, status, headers, config) {
											
											$scope.rm = data;
											
											$scope.ganado 	 = data[0].ganado;
											$scope.pagado 	 = data[0].pagado;
											$scope.pendiente = data[0].pendiente;
											
											console.log($scope.ganado);
											console.log($scope.pagado);
											console.log($scope.pendiente);
											
											console.log(JSON.stringify(data));
											
											$scope.tableRM = new NgTableParams({
												page : 1,
												count : 15,
												filter : $scope.filters,
											}, {
												total : $scope.rm.length,
												counts : [],
												getData : function($defer, params) {
													var filteredData = params.filter() ? $filter(
															'filter')($scope.rm,
															params.filter().myfilter)
															: $scope.rm;

													var orderedData = params.sorting() ? $filter(
															'orderBy')(filteredData, params.orderBy())
															: $scope.rm;

													$defer.resolve(orderedData.slice(
															(params.page() - 1) * params.count(),
															params.page() * params.count()));
												}
											});

										}).error(function(data, status, headers, config) {

								});
						
						
					};
					
					$scope.getRMPending = function() {
						
						var data = angular.toJson($scope.campaign);
						console.log("getRMPending");	

						$http({
									method : 'POST',
									url : 'getListRMPendingAction',
									data : 'campaign=' + data,
									headers : { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' }
								})
								.success(
										function(data, status, headers, config) {
											
											$scope.rm = data;
											
											$scope.ganado 	 = data[0].ganado;
											$scope.pagado 	 = data[0].pagado;
											$scope.pendiente = data[0].pendiente;
											
											console.log($scope.ganado);
											console.log($scope.pagado);
											console.log($scope.pendiente);
											
											console.log(JSON.stringify(data));
											
											$scope.tableRM = new NgTableParams({
												page : 1,
												count : 15,
												filter : $scope.filters,
											}, {
												total : $scope.rm.length,
												counts : [],
												getData : function($defer, params) {
													var filteredData = params.filter() ? $filter(
															'filter')($scope.rm,
															params.filter().myfilter)
															: $scope.rm;

													var orderedData = params.sorting() ? $filter(
															'orderBy')(filteredData, params.orderBy())
															: $scope.rm;

													$defer.resolve(orderedData.slice(
															(params.page() - 1) * params.count(),
															params.page() * params.count()));
												}
											});

										}).error(function(data, status, headers, config) {

								});
						
						
					};
					
					$scope.getRMNoPending = function() {
						
						var data = angular.toJson($scope.campaign);
						console.log("getRMPending");	

						$http({
									method : 'POST',
									url : 'getListRMNoPendingAction',
									data : 'campaign=' + data,
									headers : { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' }
								})
								.success(
										function(data, status, headers, config) {
											
											$scope.rm = data;
											
											$scope.ganado 	 = data[0].ganado;
											$scope.pagado 	 = data[0].pagado;
											$scope.pendiente = data[0].pendiente;
											
											console.log($scope.ganado);
											console.log($scope.pagado);
											console.log($scope.pendiente);
											
											console.log(JSON.stringify(data));
											
											$scope.tableRM = new NgTableParams({
												page : 1,
												count : 15,
												filter : $scope.filters,
											}, {
												total : $scope.rm.length,
												counts : [],
												getData : function($defer, params) {
													var filteredData = params.filter() ? $filter(
															'filter')($scope.rm,
															params.filter().myfilter)
															: $scope.rm;

													var orderedData = params.sorting() ? $filter(
															'orderBy')(filteredData, params.orderBy())
															: $scope.rm;

													$defer.resolve(orderedData.slice(
															(params.page() - 1) * params.count(),
															params.page() * params.count()));
												}
											});

										}).error(function(data, status, headers, config) {

								});
						
						
					};
					
					$scope.changeDate = function(value){
						console.log(value);
						
						if(value){
							
							$("#daterange1").prop('disabled', false);
						}
						else{
							$("#daterange1").prop('disabled', true);
						}
					}
					
					$scope.searchAccountStatus = function(date, stateDate) {
						
						console.log(stateDate);
						
						if(!stateDate){
							var searchAccountStatusvar = {
									campaign 			: $rootScope.search.campaing,
									//unidadDeNegocio 	: $rootScope.search.unity,
									participanteIdStars : $rootScope.search.thIdStars,
									movimiento 			: $rootScope.search.movement,
									observaciones 		: $rootScope.search.observation, 
									startDate 			: null,
									endDate 			: null
							};
						}
						else{
							var searchAccountStatusvar = {
									campaign 			: $rootScope.search.campaing,
									//unidadDeNegocio 	: $rootScope.search.unity,
									participanteIdStars : $rootScope.search.thIdStars,
									movimiento 			: $rootScope.search.movement,
									observaciones 		: $rootScope.search.observation, 
									startDate 			: date.startDate,
									endDate 			: date.endDate
							};
						}
					    
						
						
						var data = angular.toJson(searchAccountStatusvar);
						console.log(JSON.stringify(searchAccountStatusvar));
						
						$http({
						method : 'POST',
						url : 'searchAccountStatusAdminAction',
						data : 'searchAccountStatusvar=' + data,
						headers : {
							'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
						}
						}).success(
							function(data, status, headers, config) {
								
								$scope.rm = data;
								
								
								if(data.length != 0){
									
									$scope.ganado 	 = data[0].ganado;
									$scope.pagado 	 = data[0].pagado;
									$scope.pendiente = data[0].pendiente;
									
									console.log($scope.ganado);
									console.log($scope.pagado);
									console.log($scope.pendiente);
								}
								else{
									$scope.tableRM.reload();
								}
								
								
								
								console.log(JSON.stringify(data));
								
								$scope.tableRM = new NgTableParams({
									page : 1,
									count : 15,
									filter : $scope.filters,
								}, {
									total : $scope.rm.length,
									counts : [],
									getData : function($defer, params) {
										var filteredData = params.filter() ? $filter(
												'filter')($scope.rm,
												params.filter().myfilter)
												: $scope.rm;

										var orderedData = params.sorting() ? $filter(
												'orderBy')(filteredData, params.orderBy())
												: $scope.rm;

										$defer.resolve(orderedData.slice(
												(params.page() - 1) * params.count(),
												params.page() * params.count()));
									}
								});
								
							}).error(function(data, status, headers, config) {

							});
						
					};
					
					$scope.data = {
							singleSelect: '1',
						    multipleSelect: []
						   };
					
					$scope.downloadRMPending = function (){
						
						
						$http({
							method : 'POST',
							url : 'getRMXLSPendingAction',
							data : 'reportSelectedCon=' + $scope.data.singleSelect,
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded'
							}
						})
							.success(function(data, status, headers, config) {
								var resultCode = data.resultCode;				
								if(resultCode == '100'){
									
									console.log("descargando");
									
									downloadReportFile(data.fileName+ '.xls', data.valueCode);
									
								}else{
									
									
								}
							})
							.error(function(data, status, headers, config) {
								
								
							});
						
					}
					
					
					downloadReportFile = function(name,file) {

				        var octetStreamMime = 'application/octet-stream';
				        var success = false;         
				        var filename = name;
				        var contentType =  octetStreamMime;              
				        
				       var fileDownload = new Uint8Array(file);
				        try
				        {
				            // Try using msSaveBlob if supported
				            console.log("Trying saveBlob method ...");
				            var blob = new Blob([fileDownload], { type: contentType });
				            if(navigator.msSaveBlob)
				                navigator.msSaveBlob(blob, filename);
				            else {
				                // Try using other saveBlob implementations, if available
				                var saveBlob = navigator.webkitSaveBlob || navigator.mozSaveBlob || navigator.saveBlob;
				                if(saveBlob === undefined) throw "Not supported";
				                saveBlob(blob, filename);
				            }
				            console.log("saveBlob succeeded");
				            success = true;
				        } catch(ex)
				        {
				            console.log("saveBlob method failed with the following exception:");
				            console.log(ex);
				        }

				        if(!success)
				        {
				            // Get the blob url creator
				            var urlCreator = window.URL || window.webkitURL || window.mozURL || window.msURL;
				            if(urlCreator)
				            {
				                // Try to use a download link
				                var link = document.createElement('a');
				                if('download' in link)
				                {
				                    // Try to simulate a click
				                    try
				                    {
				                        // Prepare a blob URL
				                        console.log("Trying download link method with simulated click ...");
				                        var blob = new Blob([fileDownload], { type: contentType });
				                                                
				                        
				                        var url = urlCreator.createObjectURL(blob);
				                        link.setAttribute('href', url);

				                        // Set the download attribute (Supported in Chrome 14+ / Firefox 20+)
				                        link.setAttribute("download", filename);

				                        // Simulate clicking the download link
				                        var event = document.createEvent('MouseEvents');
				                        event.initMouseEvent('click', true, true, window, 1, 0, 0, 0, 0, false, false, false, false, 0, null);
				                        link.dispatchEvent(event);
				                        console.log("Download link method with simulated click succeeded");
				                        success = true;

				                    } catch(ex) {
				                        console.log("Download link method with simulated click failed with the following exception:");
				                        console.log(ex);
				                    }
				                }

				                if(!success)
				                {
				                    // Fallback to window.location method
				                    try
				                    {
				                        // Prepare a blob URL
				                        // Use application/octet-stream when using window.location to force download
				                        console.log("Trying download link method with window.location ...");
				                        var blob = new Blob([fileDownload], { type: octetStreamMime });
				                        var url = urlCreator.createObjectURL(blob);
				                        window.location = url;
				                        console.log("Download link method with window.location succeeded");
				                        success = true;
				                    } catch(ex) {
				                        console.log("Download link method with window.location failed with the following exception:");
				                        console.log(ex);
				                    }
				                }
				            }
				        }

				        if(!success)
				        {
				            // Fallback to window.open method
				            console.log("No methods worked for saving the arraybuffer, using last resort window.open");
				            window.open(httpPath, '_blank', '');
				        }    
				};
						
					

				}])


appres.directive('uploaderModel', ['$parse', function ($parse) {
	return {
		restrict: 'A',
		link: function (scope, iElement, iAttrs) 
		{
			iElement.on('change', function(e)
			{
				$parse(iAttrs.uploaderModel).assign(scope, iElement[0].files[0]);
			});
		}
	};
}])

appres.service('upload', ['$http', '$q', function ($http, $q) 
{
	this.uploadFile = function(file)
	{
		//var deferred = $q.defer();
		//var formData = new FormData();
		//formData.append('file', file);
		
		var data = angular.toJson(file);
		
		$http({
			method : 'POST',
			url : 'UploadFileAction',
			data : 'file=' + data,
			headers : { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8' }
		  }).success(function(data, status, headers, config) {
			  
		  }).error(function(data, status, headers, config) {
			  
		  });
		
	}	
}])

appres.config(function($stateProvider, $urlRouterProvider) {

	$urlRouterProvider.otherwise('/home');
	$stateProvider

	.state('home', {
		url : '/home',
		templateUrl : 'templates/campaigns_admin.jsp',
		controller:	
 			function($scope) {
			
			$('html, body').animate({
				scrollTop : $("#init").offset().top
			});
			
			//$('.navbar-default .navbar-nav>li>a').css( "color", "#FFF" );
			
			$('body').removeClass('image-th-p');
			
			//$('body').addClass('image-Admin');
			
			$('body').css({ "background-image": "" });
			
			$('#li-campaign').hide();
			
			$('#li-separator').hide();
			
		}
	})

	.state('campaign', {
		url : '/incentivo',
		templateUrl : 'templates/campaignDetail_admin.jsp',
		controller:	
 			function($scope) {
			
			$('html, body').animate({
				scrollTop : $("#init").offset().top
			});
			
			//$('.navbar-default .navbar-nav>li>a').css( "color", "#FFF" );
			
			$('#init').removeClass('image-th-p');
			
			//$('#init').addClass('image-Admin');
			
			$('body').css({ "background-image": "" });
			
			$('#li-campaign').show();
			
			$('#li-separator').show();
		}
	})

	.state('newCampaign', {
		url : '/nuevo_incentivo',
		templateUrl : 'templates/newCampaign_admin.jsp',
		controller:	
 			function($scope) {
			
			$('html, body').animate({
				scrollTop : $("#init").offset().top
			});
			
			//$('.navbar-default .navbar-nav>li>a').css( "color", "#FFF" );
			
			$('#init').removeClass('image-th-p');
			
			//$('#init').addClass('image-Admin');
			
			$('body').css({ "background-image": "" });
			
			$('#li-campaign').hide();
			
			$('#li-separator').hide();
		}
	})
	
	.state('addCampaign', {
		url : '/agregar_incentivo',
		templateUrl : 'templates/admin/upload_files_campaign.jsp',
		controller:	
 			function($scope) {
			
			$('html, body').animate({
				scrollTop : $("#init").offset().top
			});
			
			//$('.navbar-default .navbar-nav>li>a').css( "color", "#FFF" );
			
			$('#init').removeClass('image-th-p');
			
			//$('#init').addClass('image-Admin');
			
			$('body').css({ "background-image": "" });
			
			$('#li-campaign').hide();
			
			$('#li-separator').hide();
		}
	})
	
	.state('updateCampaign', {
		url : '/actualizar_incentivo',
		templateUrl : 'templates/updateCampaign_admin.jsp',
		controller:	
 			function($scope) {
			
			$('html, body').animate({
				scrollTop : $("#init").offset().top
			});
			
			//$('.navbar-default .navbar-nav>li>a').css( "color", "#FFF" );
			
			$('#init').removeClass('image-th-p');
			
			//$('#init').addClass('image-Admin');
			
			$('body').css({ "background-image": "" });
			
			$('#li-campaign').hide();
			
			$('#li-separator').hide();
		}
	})
	
	.state('newPublication', {
		url : '/nueva_publicacion',
		templateUrl : 'templates/UploadFile.jsp',
		controller:	
 			function($scope) {
			
			$('html, body').animate({
				scrollTop : $("#init").offset().top
			});
			
			//$('.navbar-default .navbar-nav>li>a').css( "color", "#FFF" );
			
			$('#init').removeClass('image-th-p');
			
			//$('#init').addClass('image-Admin');
			
			$('body').css({ "background-image": "" });
			
			$('#li-campaign').show();
			
			$('#li-separator').show();
		}
	})
	
	.state('publication', {
		url : '/publicacion',
		templateUrl : 'templates/publication_admin.jsp',
		controller:	
 			function($scope) {
			
			$('html, body').animate({
				scrollTop : $("#init").offset().top
			});
			
			//$('body').addClass('image-Admin');
			
			//$('.navbar-default .navbar-nav>li>a').css( "color", "#FFF" );
			
			$('#li-campaign').show();
			
			$('#li-separator').show();
			
		}
	})
	
	.state('updatePublication', {
		url : '/editar_publicacion',
		templateUrl : 'templates/updatePublication.jsp',
		controller:	
 			function($scope) {
			
			$('html, body').animate({
				scrollTop : $("#init").offset().top
			});
			
			//$('.navbar-default .navbar-nav>li>a').css( "color", "#FFF" );
			
			$('#init').removeClass('image-th-p');
			
			//$('#init').addClass('image-Admin');
			
			$('body').css({ "background-image": "" });
			
			$('#li-campaign').show();
			
			$('#li-separator').show();
			
		}
	})
	
	.state('uploadRMFile', {
		url : '/cargar_reporte_de_movimientos',
		templateUrl : 'templates/admin/UploadRMFile.jsp',
		controller:	
 			function($scope) {
			
			$('html, body').animate({
				scrollTop : $("#init").offset().top
			});
			
			$('#init').removeClass('image-th-p');
			
			//$('#init').addClass('image-Admin');
			
			$('body').css({ "background-image": "" });
			
			$('#li-campaign').show();
			
			$('#li-separator').show();
		}
	})
	
	.state('account_status', {
		url : '/estado_de_cuenta',
		templateUrl : 'templates/admin/account_status.jsp',
		controller:	
 			function($scope) {
			
			$('html, body').animate({
				scrollTop : $("#init").offset().top
			});
			
			$('#init').removeClass('image-th-p');
			
			//$('#init').addClass('image-Admin');
			
			$('body').css({ "background-image": "" });
			
			$('#li-campaign').show();
			
			$('#li-separator').show();
		}
	})
	
	.state('load_user', {
		url : '/carga_de_usuarios',
		templateUrl : 'templates/admin/load_user.jsp',
		controller:	
 			function($scope) {
			
			$('html, body').animate({
				scrollTop : $("#init").offset().top
			});
			
			$('#init').removeClass('image-th-p');
			
			//$('#init').addClass('image-Admin');
			
			$('body').css({ "background-image": "" });
			
			$('#li-campaign').show();
			
			$('#li-separator').show();
		}
	})
	
})

