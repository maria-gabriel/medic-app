
function limpiarPan(){
	$("#divInicio").hide();
	$("#divPacientes").hide();
	$("#divMedicos").hide();
	$("#divAcerca").hide();
	$("#liInicio").attr("class","list-group-item");
	$("#liPaciente").attr("class","list-group-item");
	$("#liMedico").attr("class","list-group-item");
	$("#liAcerca").attr("class","list-group-item");
}

limpiarPan();
$("#divInicio").show();
$("#liInicio").attr("class","list-group-item active");


$("#liInicio").click(function(){
	limpiarPan();
	$("#divInicio").show();
	$("#liInicio").attr("class","list-group-item active");
});

$("#liPaciente").click(function(){
	limpiarPan();
	$("#divPacientes").show();
	$("#liPaciente").attr("class","list-group-item active");
});

$("#liMedico").click(function(){
	limpiarPan();
	$("#divMedicos").show();
	$("#liMedico").attr("class","list-group-item active");
});

$("#liAcerca").click(function(){
	limpiarPan();
	$("#divAcerca").show();
	$("#liAcerca").attr("class","list-group-item active");
});





