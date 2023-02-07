
function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

function getRole() {
	var role = $("meta[name=role]").attr("content")
	return role;
}

function updateInventory(event){
    event.preventDefault();
	var barcode = $("#inventory-edit-form input[name=barcode]").val();
	var url = getInventoryUrl();
	var $form = $("#inventory-edit-form");
	var quantity =  $("#inventory-edit-form input[name=quantity]").val();
	if(quantity<=0) {
	    $('.notifyjs-wrapper').trigger('notify-hide');
        $.notify.defaults({clickToHide:true,autoHide:false});
	    $.notify("Quantity can't be negative", "error");
	    return;
	}
	var json = toJson($form);
	makeAjaxCall(url,'PUT',json,(response)=> {
        getInventoryList();
        $('#edit-inventory-modal').modal('toggle');
        $.notify("Inventory Updated", "success");
    },handleAjaxError);
	return false;
}


function getInventoryList(){
	var url = getInventoryUrl();
	makeAjaxCall(url,'GET',{},(data)=> {
        displayInventoryList(data);
    },handleAjaxError);
}


// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#inventoryFile')[0].files[0];
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
    if(meta.fields[0]!="barcode" || meta.fields[1]!="quantity")
    {
        var row = {};
        row.error="Incorrect headers name.";
        errorData.push(row);
        $('#download-errors').show();
        return;
    }
    if (file.type != 'text/tab-separated-values') {
        handleErrorNotification("Wrong file type");
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
	    getInventoryList();
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
	var url = getInventoryUrl();

    makeAjaxCall(url,'PUT',json,(response)=>{
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


function displayInventoryList(data){
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	$('#results-found').text("There were " + data.length + " results found.")
	for(var i in data){
		var e = data[i];
		var buttonHtml = '';
		if(getRole() === "supervisor") {
		 buttonHtml = '<td>' + `<button type="button" onclick="displayEditInventory('${e.barcode}')" data-toggle="tooltip"
                                                      data-placement="bottom" style='background-color: transparent;border: 0;' title="Edit">
                                                <i class="fa fa-pencil-square-o fa-1x"></i>
                                              </button>` + '</td>';
        }
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.pname + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+  buttonHtml
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditInventory(barcode){
	var url = getInventoryUrl() + "/" + barcode;
	makeAjaxCall(url,'GET',{},(data)=> {
        displayInventory(data);
    },handleAjaxError);
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
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
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	$('#inventoryFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-inventory-modal').modal('toggle');
	$('#download-errors').hide();
}

function displayInventory(data){
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
	$("#inventory-edit-form input[name=quantity]").val(data.quantity);
	$('#edit-inventory-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
    $('#nav-inventory').addClass('active');
	$('#inventory-edit-form').submit(updateInventory);
	$('#refresh-data').click(getInventoryList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName);
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
$(document).ready(getInventoryList);

