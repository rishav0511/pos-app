function getOrdersUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orders";
}

function addOrder(event){
	const data = orderItems.map((it) => {
        return {
          barcode: it.barcode,
          quantity: it.quantity,
          sellingPrice: it.sellingPrice,
        };
      });
      if(data.length===0) {
        $.notify("Crate is Empty", "error");
        return;
      }
    const json = JSON.stringify(data);

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
    const json = JSON.stringify(data);
    var id = $("#edit-order-modal input[name=id]").val();
    var url = getOrdersUrl() + "/" + id;
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


function displayOrderList(data){
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = `<button type="button" onclick="displayEditOrder('${e.orderId}')" data-toggle="tooltip"
                        style='background-color: transparent;border: 0;' data-placement="bottom" title="Edit">
                                                                  <i class="fa fa-pencil-square-o fa-1x"></i>
                                                              </button>` +
                         `<button type="button" class="mx-2" onclick="displayOrderDetails('${e.orderId}')" data-toggle="tooltip"
                         style='background-color: transparent;border: 0;' data-placement="bottom" title="Order Details">
                                                                  <i class="fa fa-info-circle fa-1x"></i>
                                                              </button>` +
                         `<button class = "downloadInvoiceBtn" type="button" onclick="downloadInvoice('${e.orderId}')" data-toggle="tooltip"
                         style='background-color: transparent;border: 0;' data-placement="bottom" title="Download Invoice">
                                                                 <i class="fa fa-file-pdf-o fa-1x"></i>
                                                              </button>`;
		var row = '<tr>'
		+ '<td>&nbsp;</td>'
		+ '<td>' + convertTimeStampToDateTime(e.createdAt) + '</td>'
		+ '<td style="text-align: right;">' + '₹' + numberWithCommas(e.billAmount.toFixed(2)) + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function downloadInvoice(id) {
    var req = new XMLHttpRequest();
    req.open("GET", `/pos/api/orders/download/${id}`, true);
    req.responseType = "blob";

    req.onload = function (event) {
      var blob = req.response;
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
               const row = `
                     <tr>
                       <td>&nbsp;</td>
                       <td class="barcodeData">${e.barcode}</td>
                       <td class="text-right">₹${numberWithCommas(e.sellingPrice.toFixed(2))}</td>
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
                            style='background-color: transparent;border: 0;' data-placement="bottom" title="Delete">
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
       + '<td class="text-right">' + "₹" + numberWithCommas(e.sellingPrice.toFixed(2)) + '</td>'
       + '<td>'  + e.quantity + '</td>'
       + '<td class="text-right">' + numberWithCommas((e.sellingPrice * e.quantity).toFixed(2)) + '</td>'
       + '</tr>';
       total=total+(e.sellingPrice * e.quantity);
       $tbody.append(row);
    }
    $('#grand-total').text(total.toFixed(2))
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
           const row = `
                 <tr>
                   <td>&nbsp;</td>
                   <td class="barcodeData">${e.barcode}</td>
                   <td class="text-right"> ₹${numberWithCommas(e.sellingPrice.toFixed(2))}</td>
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
                        style='background-color: transparent;border: 0;' data-placement="bottom" title="Delete">
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
$(document).ready(getOrderList);


