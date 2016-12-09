<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!-- <div class="container panel breadcrumb_sivale"> -->
<!-- 	<div class=" navbar"> -->
<!-- 		<ol class="breadcrumb breadcrumb-arrow"> -->
<!-- 			<li class="active"><span>Inicio</span></li> -->
<!-- 		</ol> -->
<!-- 	</div> -->
<!-- </div> -->
<body>
						
<div class="panel-body back-sivale">
	<div class=" navbar container">
		<ol class="breadcrumb breadcrumb-arrow">
			<li class="active"><span>Inicio</span></li>
		</ol>
	</div>

	<div class="container">
		<div class="panel panel-default table-top-sivale">
			<div class="panel-heading">
				<div class="container-fluid">
					<div class="navbar-header">Mis clasificaciones de Incentivo</div>
				</div>
			</div>				
							
			<div class="panel-body back-sivale">

				<div class="row" data-ng-controller="getClassifications" ng-cloak>
					<div class="col-sm-6 col-md-4 back-sivale"
						ng-repeat="class in classifications">
						<div class="thumbnail">
							<div class="thumbnail2 div-sivale portfolio-box">
								<div class="thumbnail2_wrapper">
									<a href="#"
										data-ng-click="updateClassification(class)"
										ui-sref="transactions"> <img
										ng-src="img/company_logo/{{class.catViews.logos}}/logo.png" class="img-responsive"
										alt="">
										<div class="portfolio-box-caption">
											<div class="portfolio-box-caption-content">
												<div class="project-name">{{class.description}}</div>
											</div>
										</div>
									</a>
								</div>
							</div>
							<div class="caption">
								<h4>{{class.className}}</h4>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</body>