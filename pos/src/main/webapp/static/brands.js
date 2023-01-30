
function getBrandsUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}

function getRole() {
	var role = $("meta[name=role]").attr("content")
	return role;
}

function addBrand(event){
    event.preventDefault();
	var $form = $("#brand-form");
	var json = toJson($form);
	var url = getBrandsUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getBrandList();
	   		$("#brand-form input[name=brand]").val('');
	   		$("#brand-form input[name=category]").val('');
	   		$.notify("Brand and Category Added", "success");
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateBrand(event){
    event.preventDefault();
	var id = $("#brand-edit-form input[name=id]").val();
	var url = getBrandsUrl() + "/" + id;
	var $form = $("#brand-edit-form");
	var json = toJson($form);
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getBrandList();
	   		$('#edit-brand-modal').modal('toggle');
	   		$.notify("Brand and Category Updated", "success");
	   },
	   error: handleAjaxError
	});

	return false;
}


function getBrandList(){
	var url = getBrandsUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandList(data);  
	   },
	   error: handleAjaxError
	});
}


// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#brandFile')[0].files[0];
	processCount=0;
	errorData=[];
	fileData=[];
	updateUploadDialog();
	if(!file)
	{
	    $.notify("No file detected!", "error");
	    return;
	}
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	var meta = results.meta;
	if(meta.fields.length!=2 ) {
	    var row = {};
	    row.error="Number of headers don't match.";
	    errorData.push(row);
	    $('#download-errors').show();
	    return;
	}
	if(meta.fields[0]!="brand" || meta.fields[1]!="category")
	{
	    var row = {};
        row.error="Incorrect headers name.";
        errorData.push(row);
        $('#download-errors').show();
        return;
	}
    if(fileData.length >= 5000) {
        $.notify("Row Count greater than 5000!", "error");
        return;
    }
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
	    getBrandList();
	    if(errorData.length===0) {
            return;
        } else {
            $('#download-errors').show();
            return;
        }
	}

	//Process next row
	var row = fileData[processCount];
	processCount++;

	if(row.__parsed_extra != null) {
        row.Sno=processCount;
        row.error="Extra Fields exist.";
        errorData.push(row);
        uploadRows();
        return;
    }
	
	var json = JSON.stringify(row);
	var url = getBrandsUrl();

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		uploadRows();  
	   },
	   error: function(response){
		   	row.Sno=processCount;
		   	var data = JSON.parse(response.responseText);
	   		row.error=data["message"];
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}


function displayBrandList(data){
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '';
		if(getRole() === "supervisor") {
		    buttonHtml = '<td>' + `<button type="button" onclick="displayEditBrand('${e.id}')" data-toggle="tooltip"
                                        data-placement="bottom" style='background-color: transparent;border: 0;' title="Edit" >
                                  <i class="fa fa-pencil-square-o fa-1x"></i>
                                </button>` + '</td>';
        }
		var row = '<tr>'
		+ '<td>&nbsp;</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+  buttonHtml
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditBrand(id){
	var url = getBrandsUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrand(data);
	   },
	   error: handleAjaxError
	});	
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#brandFile');
	var fileName = $file.val();
	$('#brandFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
    $('#download-errors').hide();
	$('#upload-brand-modal').modal('toggle');
}

function displayBrand(data){
	$("#brand-edit-form input[name=brand]").val(data.brand);
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
}



function init(){
    $('#nav-brands').addClass('active');
	$('#add-brand').submit(addBrand);
	$('#brand-edit-form').submit(updateBrand);
	$('#refresh-data').click(getBrandList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName);
    $('#download-errors').hide();
}


let mybutton = document.getElementById("btn-back-to-top");


window.onscroll = function () {
  scrollFunction();
};

function scrollFunction() {
  if (
    document.body.scrollTop > 20 ||
    document.documentElement.scrollTop > 20
  ) {
    mybutton.style.display = "block";
  } else {
    mybutton.style.display = "none";
  }
}

mybutton.addEventListener("click", backToTop);

function backToTop() {
  document.body.scrollTop = 0;
  document.documentElement.scrollTop = 0;
}

$(document).ready(init);
$(document).ready(getBrandList);

