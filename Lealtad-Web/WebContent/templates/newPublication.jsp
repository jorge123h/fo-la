<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<div class="panel-body back-sivale">
	<div class="navbar container">
		<ol class="breadcrumb breadcrumb-arrow">
			<li><a ui-sref="home">Incentivos Recientes</a></li>
			<li><a ui-sref="campaign">Publicaciones</a></li>
			<li class="active"><span>Alta de Publicaciones</span></li>
		</ol>
	</div>
	<div class="container">
		<div class="panel panel-default table-top-sivale">
			<div class="panel-heading">
				<div class="container-fluid">
					<div class="navbar-header">Datos de Publicación</div>
				</div>
			</div>
			<div class="panel-body back-sivale">

				<div class="text-right">

					<div class="row margin-top10">
						<div class="col-md-4">Nombre de Publiccion</div>
						<div class="col-md-8">
							<input type="text" class="form-control">
						</div>
					</div>

					<div class="row margin-top10">
						<div class="col-md-4">Tipo de Publicación</div>
						<div class="col-md-8">
							<select class="form-control">
								<option>Teaser</option>
								<option>Avance</option>
								<option>Cierre</option>
								<option>Premiación</option>
							</select>
						</div>
					</div>

					<div class="row margin-top10">
						<div class="col-md-4">Documento html</div>
						<div class="col-md-8">
							<input id="file-html" type="file"
								class="file" name="file-html[]" data-show-preview="false"
								data-show-upload="false" data-show-remove="false" uploader-model="files.html">
						</div>
					</div>

					<div class="row margin-top10">
						<div class="col-md-4">Documento excel</div>
						<div class="col-md-8">
							<input id="file-xlsx" type="file"
								class="file" name="file-xlsx[]" data-show-preview="false"
								data-show-upload="false" data-show-remove="false" uploader-model="files.excel">
						</div>
					</div>

					<div class="row margin-top10">
						<div class="col-md-4">Descripción</div>
						<div class="col-md-8">
							<textarea class="form-control" rows="3"></textarea>
						</div>
					</div>

					<div class="row margin-top10">
						<div class="col-md-4">Documentación anexa</div>
						<div class="col-md-8">
							<input type="file" class="file" data-show-preview="false"
								data-show-upload="false" data-show-remove="false">
							<!-- <div class="col-md-2">
						<label class="checkbox span4"> <input type="checkbox"
							value="all" id="allstates" name="all" />Archivo PÃºblico
						</label>
					</div> -->
						</div>
					</div>
				</div>


				<div class="row margin-top10">
					<div class="col-md-4"></div>

					<div class="col-md-8 text-left">

						<button type="button" class="btn btn-success"
							ng-click="uploadFile()">Publicar</button>
						<button type="button" class="btn btn-danger left-35">
							Cancelar</button>

					</div>
				</div>

				<script>
					$('#file-xlsx').fileinput({
						language : 'es',
						uploadUrl : '#',
						allowedFileExtensions : [ 'xlsx' ],
					});
					$('#file-html').fileinput({
						language : 'es',
						uploadUrl : '#',
						allowedFileExtensions : [ 'html' ],
					});
				</script>

			</div>
		</div>
	</div>
</div>