$(document).ready(init);
$(document).ready(getBrandList);

function init(){
    $('#nav-brands').addClass('active');
    $('#add-brand').click(addBrandModal)
	$('#brand-form').submit(addBrand);
	$('#brand-edit-form').submit(updateBrand);
	$('#brand-add-form').submit(addBrand);
	$('#refresh-data').click(getBrandList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName);
    $('#download-errors').hide();
}

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
	var $form = $("#brand-add-form");
	var json = toJson($form);
	var url = getBrandsUrl();
	makeAjaxCall(url,'POST',json,(response)=> {
        getBrandList();
        $("#brand-form input[name=brand]").val('');
        $("#brand-form input[name=category]").val('');
        $.notify("Brand and Category Added", "success");
    },handleAjaxError);
	return false;
}

function updateBrand(event){
    event.preventDefault();
	var id = $("#brand-edit-form input[name=id]").val();
	var url = getBrandsUrl() + "/" + id;
	var $form = $("#brand-edit-form");
	var json = toJson($form);
	makeAjaxCall(url,'PUT',json,(response)=>{
        getBrandList();
        $('#edit-brand-modal').modal('toggle');
        $.notify("Brand and Category Updated", "success");
    },handleAjaxError);
	return false;
}


function getBrandList(){
	var url = getBrandsUrl();
	makeAjaxCall(url,'GET',{},(data)=>{
        displayBrandList(data);
    },handleAjaxError);
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
	    $('.notifyjs-wrapper').trigger('notify-hide');
        $.notify.defaults({clickToHide:true,autoHide:false});
	    $.notify("No file detected!", "error");
	    return;
	}
	if (file.type != 'text/tab-separated-values') {
        handleErrorNotification("Wrong file type");
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
        $('.notifyjs-wrapper').trigger('notify-hide');
        $.notify.defaults({clickToHide:true,autoHide:false});
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

    makeAjaxCall(url,'POST',json,(response)=>{
        uploadRows();
    },(response)=>{
        row.Sno=processCount;
        var data = JSON.parse(response.responseText);
        row.error=data["message"];
        errorData.push(row);
        uploadRows();
    });
}

function downloadErrors(){
	writeFileData(errorData);
}


function displayBrandList(data){
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	 $('#results-found').text("There were " + data.length + " results found.")
	for(var i in data){
		var e = data[i];
		var buttonHtml = '';
		if(getRole() === "supervisor") {
		    buttonHtml = '<td>' + `<button type="button" onclick="displayEditBrand('${e.id}')" data-toggle="tooltip"
                                        data-placement="bottom" style='background-color: transparent;border: 0;' title="Update" >
                                  <i class="fa fa-pencil-square-o fa-1x"></i>
                                </button>` + '</td>';
        }
		var row = '<tr>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+  buttonHtml
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditBrand(id){
	var url = getBrandsUrl() + "/" + id;
	makeAjaxCall(url,'GET',{},(data)=>{
        displayBrand(data);
    },handleAjaxError);
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

function addBrandModal() {
    $('#add-brand-modal').modal('toggle');
}

let mybutton = document.getElementsByClassName("btn-back-to-top")[0];


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

