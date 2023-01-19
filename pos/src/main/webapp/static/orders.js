
function getOrdersUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orders";
}

//BUTTON ACTIONS
function addOrder(event){
	//Set the values to update
//	var $form = $("#order-form");
	const data = orderItems.map((it) => {
        return {
          barcode: it.barcode,
          quantity: it.quantity,
          sellingPrice: it.sellingPrice,
        };
      });
//	var json = toJson($form);
    const json = JSON.stringify(data);
//	console.log(json);

	var url = getOrdersUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		addOrderModal();
	   		getOrderList();
	   		$.notify("Order Created", "success");
	   },
	   error: handleAjaxError
	});

	return false;
}

function updateOrder(event){
	const data = orderItems.map((it) => {
            return {
              barcode: it.barcode,
              quantity: it.quantity,
              sellingPrice: it.sellingPrice,
            };
          });
    //	var json = toJson($form);
    const json = JSON.stringify(data);
//    console.log(json);
    var id = $("#edit-order-modal input[name=id]").val();
    var url = getOrdersUrl() + "/" + id;
//    console.log(url);
    $.ajax({
       url: url,
       type: 'PUT',
       data: json,
       headers: {
        'Content-Type': 'application/json'
       },
       success: function(response) {
            $('#edit-order-modal').modal('toggle');
            getOrderList();
            $.notify("Order Updated", "success");
       },
       error: handleAjaxError
    });
}


function getOrderList(){
	var url = getOrdersUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderList(data);
	   },
	   error: handleAjaxError
	});
}


// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#employeeFile')[0].files[0];
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
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function displayOrderList(data){
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
//		console.log(e);
//		var buttonHtml = ' <button onclick="displayEditOrder(' + e.orderId +')">edit</button>'
//        		        + ' <button onclick="displayOrderDetails(' + e.orderId +')">details</button>'
//        		        + ' <button onclick="downloadInvoice(' + e.orderId +')">Invoice</button>'
		var buttonHtml = `<button type="button" onclick="displayEditOrder('${e.orderId}')" data-toggle="tooltip"
                                                                  data-placement="bottom" title="Edit">
                                                                  <i class="fa fa-pencil-square-o fa-1x"></i>
                                                              </button>` +
                         `<button type="button" class="mx-2" onclick="displayOrderDetails('${e.orderId}')" data-toggle="tooltip"
                                                                  data-placement="bottom" title="Order Details">
                                                                  <i class="fa fa-info-circle fa-1x"></i>
                                                              </button>` +
                         `<button class = "downloadInvoiceBtn" type="button" disabled onclick="downloadInvoice('${e.orderId}')" data-toggle="tooltip"
                                                                 data-placement="bottom" title="Download Invoice">
                                                                 <i class="fa fa-file-pdf-o fa-1x"></i>
                                                              </button>`;
		var row = '<tr>'
		+ '<td>&nbsp;</td>'
		+ '<td>' + convertTimeStampToDateTime(e.createdAt) + '</td>'
		+ '<td>'  + e.billAmount + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	setTimeout(() => {
        const box = document.getElementsByClassName('downloadInvoiceBtn');
        for(let i=0;i<box.length;++i){
              box[i].disabled = false
        }
      }, 0);
}

function downloadInvoice(id) {
    var req = new XMLHttpRequest();
//    req.open("GET", `/pos/download/invoice/${id}`, true);
    req.open("GET", `/pos/api/orders/download/${id}`, true);
    req.responseType = "blob";

    req.onload = function (event) {
      var blob = req.response;
      console.log(blob.size);
      var link=document.createElement('a');
      link.href=window.URL.createObjectURL(blob);
      link.download=`invoice${id}.pdf`;
      link.click();
    };
    req.send();
}

function displayEditOrder(id) {

    $('#edit-orderId').text(id);
    var url = getOrdersUrl() + "/"+id+"/invoice";
    $.ajax({
       url: url,
       type: 'GET',
       success: function(data) {
//            console.log(data);
            orderItems = data.orderItemDataList;
            $('#edit-order-modal').modal('toggle');
            $("#edit-order-modal input[name=id]").val(data.orderData.orderId);
            $('#edit-orderId').text(data.orderData.orderId);
            var dateTime = data.orderData.createdAt;
            $('#edit-orderDateTime').text(convertTimeStampToDateTime(dateTime));
            displayEditOrderItems(data.orderItemDataList);
       },
       error: handleAjaxError
    });
}

function displayEditOrderItems(orderItems) {
    var $tbody = $('#create-edit-order-table').find('tbody');
    $tbody.empty();
    for(var i in orderItems) {
               var e = orderItems[i];
    //           var buttonHtml = `<button onclick="deleteOrderItem('${e.barcode}')">Delete</button>`
    //           var buttonHtml = `<button type="button" onclick="deleteOrderItem('${e.barcode}')" data-toggle="tooltip"
    //                                                                     data-placement="bottom" title="Delete">
    //                                                                     <i class="fa fa-trash-o fa-1x"></i>
    //                                                                 </button>`
    //           var row = '<tr>'
    //           + '<td>&nbsp;</td>'
    //           + '<td>' + e.barcode + '</td>'
    //           + '<td>'  + e.sellingPrice + '</td>'
    //           + '<td>'  + <input
    //                       id="order-item-${item.barcode}"
    //                       type="number"
    //                       class="form-controll
    //                       quantityData"
    //                       value="${item.quantity}"
    //                       onchange="onQuantityChanged('${item.barcode}')"
    //                       style="width:70%" min="1"> + '</td>'
    //           + '<td>' + buttonHtml + '</td>'
    //           + '</tr>';
               const row = `
                     <tr>
                       <td>&nbsp;</td>
                       <td class="barcodeData">${e.barcode}</td>
                       <td>${e.sellingPrice}</td>
                       <td>
                         <input
                           id="order-item-${e.barcode}"
                           type="number"
                           class="form-control quantityData"
                           value="${e.quantity}"
                           onchange="onQuantityChanged('${e.barcode}')"
                           style="width:70%" min="1">
                       </td>
                       <td>
                         <button type="button" onclick="deleteEditOrderItem('${e.barcode}')" data-toggle="tooltip"
                                                  data-placement="bottom" title="Delete">
                                                  <i class="fa fa-trash-o fa-1x"></i>
                                              </button>
                       </td>
                     </tr>
                   `;

               $tbody.append(row);
         }
}


function displayOrderDetails(id) {
    var url = getOrdersUrl() + "/"+id+"/invoice";
    $.ajax({
       url: url,
       type: 'GET',
       success: function(data) {
//            console.log(data);
            displayDetailsModal(data);
       },
       error: handleAjaxError
    });
}

function displayDetailsModal(data) {

    $('#details-orderId').text(data.orderData.orderId);
    var dateTime = data.orderData.createdAt
    $('#details-orderDateTime').text(convertTimeStampToDateTime(dateTime));
    $('#details-order-modal').modal('toggle');
    var $tbody = $('#details-order-table').find('tbody');
    $tbody.empty();
    var total=0;
    for(var i in data.orderItemDataList) {
       var e = data.orderItemDataList[i];
       var row = '<tr>'
       + '<td>&nbsp;</td>'
       + '<td>' + e.barcode + '</td>'
       + '<td>' + e.product + '</td>'
       + '<td>'  + e.sellingPrice + '</td>'
       + '<td>'  + e.quantity + '</td>'
       + '<td>' + e.sellingPrice * e.quantity + '</td>'
       + '</tr>';
       total=total+(e.sellingPrice * e.quantity);
       $tbody.append(row);
    }
    $('#grand-total').text(total.toFixed(2))
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#employeeFile');
	$file.val('');
	$('#employeeFileName').html("Choose File");
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
	var $file = $('#employeeFile');
	var fileName = $file.val();
	$('#employeeFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog();
	$('#upload-employee-modal').modal('toggle');
}

function displayOrder(data){
	$("#order-edit-form input[name=brand]").val(data.brand);
	$("#order-edit-form input[name=category]").val(data.category);
	$("#order-edit-form input[name=id]").val(data.id);
	$('#edit-order-modal').modal('toggle');
}

function convertTimeStampToDateTime(timestamp) {
    var date = new Date(timestamp);
    return (
      date.getDate() +
      "/" +
      (date.getMonth() + 1) +
      "/" +
      date.getFullYear() +
      " " +
      date.getHours() +
      ":" +
      date.getMinutes() +
      ":" +
      date.getSeconds()
    );
  }

function addOrderModal() {
    $('#add-order-modal').modal('toggle');
    var $tbody = $('#create-order-table').find('tbody');
    $tbody.empty();
    orderItems.length = 0
}
let orderItems = [];



function displayCreateOrderItems(orderItems) {
    var $tbody = $('#create-order-table').find('tbody');
     $tbody.empty();
     for(var i in orderItems) {
           var e = orderItems[i];
//           var buttonHtml = `<button onclick="deleteOrderItem('${e.barcode}')">Delete</button>`
//           var buttonHtml = `<button type="button" onclick="deleteOrderItem('${e.barcode}')" data-toggle="tooltip"
//                                                                     data-placement="bottom" title="Delete">
//                                                                     <i class="fa fa-trash-o fa-1x"></i>
//                                                                 </button>`
//           var row = '<tr>'
//           + '<td>&nbsp;</td>'
//           + '<td>' + e.barcode + '</td>'
//           + '<td>'  + e.sellingPrice + '</td>'
//           + '<td>'  + <input
//                       id="order-item-${item.barcode}"
//                       type="number"
//                       class="form-controll
//                       quantityData"
//                       value="${item.quantity}"
//                       onchange="onQuantityChanged('${item.barcode}')"
//                       style="width:70%" min="1"> + '</td>'
//           + '<td>' + buttonHtml + '</td>'
//           + '</tr>';
           const row = `
                 <tr>
                   <td>&nbsp;</td>
                   <td class="barcodeData">${e.barcode}</td>
                   <td>${e.sellingPrice}</td>
                   <td>
                     <input
                       id="order-item-${e.barcode}"
                       type="number"
                       class="form-control quantityData"
                       value="${e.quantity}"
                       onchange="onQuantityChanged('${e.barcode}')"
                       style="width:70%" min="1">
                   </td>
                   <td>
                     <button type="button" onclick="deleteOrderItem('${e.barcode}')" data-toggle="tooltip"
                                              data-placement="bottom" title="Delete">
                                              <i class="fa fa-trash-o fa-1x"></i>
                                          </button>
                   </td>
                 </tr>
               `;

           $tbody.append(row);
     }
}

function onQuantityChanged(barcode) {
  const index = orderItems.findIndex((it) => it.barcode === barcode);
  if (index == -1) return;
  const newQuantity = $(`#order-item-${barcode}`).val();
  orderItems[index].quantity = Number.parseInt(newQuantity);
}

function getCurrentOrderItem() {
  return {
    barcode: $('#inputBarcode').val(),
    quantity: Number.parseInt($('#inputQuantity').val()),
    sellingPrice: Number.parseFloat($('#inputSellingPrice').val()),
  };
}

function getCurrentEditOrderItem() {
  return {
    barcode: $('#inputEditBarcode').val(),
    quantity: Number.parseInt($('#inputEditQuantity').val()),
    sellingPrice: Number.parseFloat($('#inputEditSellingPrice').val()),
  };
}

function resetAddItemForm() {
  $('#inputBarcode').val('');
  $('#inputQuantity').val('');
  $('#inputSellingPrice').val('');
}

function resetEditAddItemForm() {
  $('#inputEditBarcode').val('');
  $('#inputEditQuantity').val('');
  $('#inputEditSellingPrice').val('');
}

function addItem(item) {
  const index = orderItems.findIndex((it) => it.barcode === item.barcode.toString());
  if (index == -1) {
    orderItems.push(item);
  } else {
    orderItems[index].quantity += item.quantity;
  }
}

function fetchOrderDetails(id) {
  var url = getOrderUrl() + id;
  $.ajax({
    url: url,
    type: 'GET',
    success: function (data) {
      displayOrderDetails(data);
    },
    error: handleAjaxError,
  });
}


function addOrderItem(event) {
  event.preventDefault();
  const item = getCurrentOrderItem();
  addItem(item);
  displayCreateOrderItems(orderItems);
  resetAddItemForm();
}
function deleteOrderItem(barcode) {
  const index = orderItems.findIndex((it) => it.barcode.toString() === barcode.toString());
  if (index == -1) return;
  orderItems.splice(index, 1);
  displayCreateOrderItems(orderItems);
}

function addEditOrderItem(event) {
event.preventDefault();
  const item = getCurrentEditOrderItem();
  addItem(item);
  displayEditOrderItems(orderItems);
  resetEditAddItemForm();
}

function deleteEditOrderItem(barcode) {
  const index = orderItems.findIndex((it) => it.barcode.toString() === barcode.toString());
  if (index == -1) return;
  orderItems.splice(index, 1);
  displayEditOrderItems(orderItems);
}

//INITIALIZATION CODE
function init(){
    $('#nav-orders').addClass('active');
    $('#add-order-button').click(addOrderModal);
	$('#place-order').click(addOrder);
	$('#edit-order').click(updateOrder);
	$('#add-item-form').submit(addOrderItem);
	$('#add-edit-item-form').submit(addEditOrderItem);
	$('#refresh-data').click(getOrderList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#employeeFile').on('change', updateFileName)
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
$(document).ready(getOrderList);
