
function getProductUrl(){
   var baseUrl = $("meta[name=baseUrl]").attr("content")
   //console.log(baseUrl);
   return baseUrl + "/api/product";
}

function getRole() {
    var role = $("meta[name=role]").attr("content")
	return role;
}

function getBrandCategoryUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/brands";
}

//BUTTON ACTIONS
function addProduct(event){
   //Set the values to update
   var $form = $("#product-add-form");

   var brandCategory = $('#inputBrandCategory').val();
   var brandCategoryJson = extractNameAndCategory(brandCategory);

   $form.append('<input type="hidden" name="bname" value="' + brandCategoryJson.brandName + '" /> ');
   $form.append('<input type="hidden" name="bcategory" value="' + brandCategoryJson.brandCategory + '" /> ');

   var json = toJson($form);
   var url = getProductUrl();

   console.log(json);

   $.ajax({
      url: url,
      type: 'POST',
      data: json,
      headers: {
           'Content-Type': 'application/json'
       },
      success: function(response) {
         getProductList();
         $("#product-add-form input[name=barcode]").val('');
         $("#product-add-form input[name=product]").val('');
         $("#product-add-form input[name=bname]").val('');
         $("#product-add-form input[name=bcategory]").val('');
         $("#product-add-form input[name=mrp]").val('');
         $.notify("Product Added", "success");
      },
      error: handleAjaxError
   });

   return false;
}

function updateProduct(event){
   $('#edit-product-modal').modal('toggle');
   //Get the ID
   var id = $("#product-edit-form input[name=id]").val();
   var url = getProductUrl() + "/" + id;
   //Set the values to update
   var $form = $("#product-edit-form");

   var brandCategory = $('#inputEditBrandCategory').val();
   var brandCategoryJson = extractNameAndCategory(brandCategory);

   $form.append('<input type="hidden" name="bname" value="' + brandCategoryJson.brandName + '" /> ');
   $form.append('<input type="hidden" name="bcategory" value="' + brandCategoryJson.brandCategory + '" /> ');


   var json = toJson($form);
   $.ajax({
      url: url,
      type: 'PUT',
      data: json,
      headers: {
           'Content-Type': 'application/json'
       },
      success: function(response) {
             getProductList();
             $.notify("Product Updated", "success");
      },
      error: handleAjaxError
   });

   return false;
}


function getProductList(){
   var url = getProductUrl();
   $.ajax({
      url: url,
      type: 'GET',
      success: function(data) {
             displayProductList(data);
      },
      error: handleAjaxError
   });
}

function deleteProduct(id){
   var url = getProductUrl() + "/" + id;

   $.ajax({
      url: url,
      type: 'DELETE',
      success: function(data) {
             getProductList();
      },
      error: handleAjaxError
   });
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
   var file = $('#productFile')[0].files[0];
   readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
   fileData = results.data;
   uploadRows();
}

function uploadRows(){
   //Update progress
   updateUploadDialog();
   //If everything processed then return
   if(processCount==fileData.length){
      return;
   }

   //Process next row
   var row = fileData[processCount];
   processCount++;

   var json = JSON.stringify(row);
   var url = getProductUrl();

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
             row.error=response.responseText
             errorData.push(row);
             uploadRows();
      }
   });

}

function downloadErrors(){
   writeFileData(errorData);
}

function addDataToBrandCategoryDropdown(data, formId) {
	var $brandCategory = $(`${formId} select[name=brandCategory]`);
	$brandCategory.empty();

	var brandDefaultOption = '<option value="">Select brand and category</option>';
    $brandCategory.append(brandDefaultOption);

	for (var i in data) {
	  var e = data[i];
	  var option =
		'<option value="' +
		e.brand + '~' + e.category +
		'">' +
		e.brand + '-' + e.category +
		"</option>";
	  $brandCategory.append(option);
    }
}


function populateBrandCategoryDropDown(formType) {
	var url = getBrandCategoryUrl();
	$.ajax({
	  url: url,
	  type: "GET",
	  success: function (data) {
	    if(formType === "add-form") {
		    addDataToBrandCategoryDropdown(data, "#product-add-form");
		} else if(formType === "edit-form") {
		    addDataToBrandCategoryDropdown(data, "#product-edit-form");
        }
	  },
	  error: handleAjaxError,
	});
}


//UI DISPLAY METHODS

function displayProductList(data){
   var $tbody = $('#product-table').find('tbody');
   $tbody.empty();
   for(var i in data){
      var e = data[i];
      var buttonHtml = '';
      if(getRole() === "supervisor") {
         buttonHtml = '<td>' + `<button type="button" onclick="displayEditProduct('${e.id}')" data-toggle="tooltip"
                                              data-placement="bottom" title="Edit">
                                        <i class="fa fa-pencil-square-o fa-1x"></i>
                                      </button>` + '</td>';
      }
      var row = '<tr>'
      + '<td>&nbsp;</td>'
      + '<td>' + e.barcode + '</td>'
      + '<td>' + e.product + '</td>'
      + '<td>' + e.bname + '</td>'
      + '<td>'  + e.bcategory + '</td>'
      + '<td>' + e.mrp + '</td>'
      +  buttonHtml
      + '</tr>';
        $tbody.append(row);
   }
}

function displayEditProduct(id){
   var url = getProductUrl() + "/" + id;
   populateBrandCategoryDropDown("edit-form");
   $.ajax({
      url: url,
      type: 'GET',
      success: function(data) {
             displayProduct(data);
      },
      error: handleAjaxError
   });
}

function resetUploadDialog(){
   //Reset file name
   var $file = $('#productFile');
   $file.val('');
   $('#productFileName').html("Choose File");
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
   var $file = $('#productFile');
   var fileName = $file.val();
   $('#productFileName').html(fileName);
}

function displayUploadData(){
   resetUploadDialog();
   $('#upload-product-modal').modal('toggle');
}

function displayProduct(data){
   var brandCategory = data.bname + "-" + data.bcategory;
   $("#product-edit-form input[name=barcode]").val(data.barcode);
   $("#product-edit-form input[name=product]").val(data.product);
//   $("#product-edit-form input[name=bname]").val(data.bname);
   $("#product-edit-form select[name=brandCategory] option:contains(" + brandCategory + ")").attr("selected", true);
//   $("#product-edit-form input[name=bcategory]").val(data.bcategory);
   $("#product-edit-form input[name=mrp]").val(data.mrp);
   $("#product-edit-form input[name=id]").val(data.id);
   $('#edit-product-modal').modal('toggle');
}


function addProductModal() {
    $('#add-product-modal').modal('toggle');
    populateBrandCategoryDropDown("add-form");
}
//INITIALIZATION CODE
function init(){
    $('#nav-products').addClass('active');
   $('#add-product-button').click(addProductModal);
   $('#add-product').click(addProduct);
   $('#update-product').click(updateProduct);
   $('#refresh-data').click(getProductList);
   $('#upload-data').click(displayUploadData);
   $('#process-data').click(processData);
   $('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName)
}
//Get the button
let mybutton = document.getElementById("btn-back-to-top");

// When the user scrolls down 20px from the top of the document, show the button
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
// When the user clicks on the button, scroll to the top of the document
mybutton.addEventListener("click", backToTop);

function backToTop() {
  document.body.scrollTop = 0;
  document.documentElement.scrollTop = 0;
}

$(document).ready(init);
$(document).ready(getProductList);