function getOrdersUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orders";
}

function getProductUrl(){
    var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/products";
}

function getProductByBarcode(barcode, onSuccess) {
  const url = getProductUrl() + "/b/" + barcode;
  makeAjaxCall(url,'GET',{},(data)=>{
          onSuccess(data);
  },handleAjaxError);
}

function addOrder(event){
    var ok = true;
    const data = orderItems.map((it) => {
        if(isNaN(it.quantity)) {
           $('.notifyjs-wrapper').trigger('notify-hide');
           $.notify.defaults({clickToHide:true,autoHide:false});
           $.notify("Quantity cannot be empty", "error");
           ok = false;
        }
        return {
            barcode: it.barcode,
            quantity: it.quantity,
            sellingPrice: it.sellingPrice,
        };
    });
    if(!ok)
        return;
    if(data.length===0) {
        $('.notifyjs-wrapper').trigger('notify-hide');
        $.notify.defaults({clickToHide:true,autoHide:false});
        $.notify("Crate is Empty", "error");
        return;
    }
    const json = JSON.stringify(data);

	var url = getOrdersUrl();
	makeAjaxCall(url,'POST',json,(response)=>{
        addOrderModal();
        getOrderList();
        $.notify("Order Created", "success");
    },handleAjaxError);
	return false;
}

function updateOrder(event){
    var ok = true;
	const data = orderItems.map((it) => {
        if(isNaN(it.quantity)) {
           $('.notifyjs-wrapper').trigger('notify-hide');
           $.notify.defaults({clickToHide:true,autoHide:false});
           $.notify("Quantity cannot be empty", "error");
           ok = false;
        }
        return {
            barcode: it.barcode,
            quantity: it.quantity,
            sellingPrice: it.sellingPrice,
        };
    });
    if(!ok)
        return;
    if(data.length===0) {
        $('.notifyjs-wrapper').trigger('notify-hide');
        $.notify.defaults({clickToHide:true,autoHide:false});
        $.notify("Crate is Empty", "error");
        return;
    }
    const json = JSON.stringify(data);
    var id = $("#edit-order-modal input[name=id]").val();
    var url = getOrdersUrl() + "/" + id;
    makeAjaxCall(url,'PUT',json,(response)=>{
        $('#edit-order-modal').modal('toggle');
        getOrderList();
        $.notify("Order Updated", "success");
    },handleAjaxError);
}


function getOrderList(){
	var url = getOrdersUrl();
	makeAjaxCall(url,'GET',{},(data)=>{
        displayOrderList(data);
    },handleAjaxError);
}


function displayOrderList(data){
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = `<button type="button" onclick="displayEditOrder('${e.orderId}')" data-toggle="tooltip"
                        style='background-color: transparent;border: 0;' data-placement="bottom" title="Update">
                                                                  <i class="fa fa-pencil-square-o fa-1x"></i>
                                                              </button>` +
                         `<button type="button" class="mx-2" onclick="displayOrderDetails('${e.orderId}')" data-toggle="tooltip"
                         style='background-color: transparent;border: 0;' data-placement="bottom" title="View Order">
                                                                  <i class="fa fa-info-circle fa-1x"></i>
                                                              </button>` +
                         `<button class = "downloadInvoiceBtn" type="button" onclick="downloadInvoice('${e.orderId}')" data-toggle="tooltip"
                         style='background-color: transparent;border: 0;' data-placement="bottom" title="Download Invoice">
                                                                 <i class="fa fa-file-pdf-o fa-1x"></i>
                                                              </button>`;
		var row = '<tr>'
		+ '<td>'+ e.orderId + '</td>'
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
    makeAjaxCall(url,'GET',{},(data)=>{
        orderItems = data.orderItemDataList;
        $('#edit-order-modal').modal('toggle');
        $("#edit-order-modal input[name=id]").val(data.orderData.orderId);
        $('#edit-orderId').text(data.orderData.orderId);
        var dateTime = data.orderData.createdAt;
        $('#edit-orderDateTime').text(convertTimeStampToDateTime(dateTime));
        displayEditOrderItems(data.orderItemDataList);
        resetEditAddItemForm();
    },handleAjaxError);
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
                       <td class="d-flex justify-content-center">
                            <input
                              id="order-item-sp-${e.barcode}"
                              type="number"
                              class="form-control sellingPriceData numberInput"
                              value="${e.sellingPrice}"
                              onchange="onSellingPriceChanged('${e.barcode}')"
                              style="width:70%" min="0" step="0.01">
                       </td>
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
         verifyNumberInput();
}


function displayOrderDetails(id) {
    var url = getOrdersUrl() + "/"+id+"/invoice";
    makeAjaxCall(url,'GET',{},(data)=>{
        displayDetailsModal(data);
    },handleAjaxError);
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
       + '<td class="text-right">' + "₹"  + numberWithCommas((e.sellingPrice * e.quantity).toFixed(2)) + '</td>'
       + '</tr>';
       total=total+(e.sellingPrice * e.quantity);
       $tbody.append(row);
    }
    $('#grand-total').text("₹"+total.toFixed(2))
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
                   <td class="d-flex justify-content-center">
                       <input
                         id="order-item-sp-${e.barcode}"
                         type="number"
                         class="form-control sellingPriceData numberInput"
                         value="${e.sellingPrice}"
                         onchange="onSellingPriceChanged('${e.barcode}')"
                         style="width:70%" min="0" step="0.01">
                  </td>
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
     verifyNumberInput();
}

function onQuantityChanged(barcode) {
  const index = orderItems.findIndex((it) => it.barcode === barcode);
  if (index == -1) return;
  const newQuantity = $(`#order-item-${barcode}`).val();
  orderItems[index].quantity = Number.parseInt(newQuantity);
}
function onSellingPriceChanged(barcode) {
  const index = orderItems.findIndex((it) => it.barcode === barcode);
  if (index == -1) return;
  const newSellingPrice = $(`#order-item-sp-${barcode}`).val();
  orderItems[index].sellingPrice = Number.parseFloat(newSellingPrice);
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
  makeAjaxCall(url,'GET',{},(data)=>{
      displayOrderDetails(data);
  },handleAjaxError);
}


function addOrderItem(event) {
  event.preventDefault();
  const item = getCurrentOrderItem();
  getProductByBarcode(item.barcode, (product) => {
      if(product.mrp<item.sellingPrice){
        showError("Selling Price should not be greater than MRP: "+product.mrp);
        return;
      }
      addItem(item);
      displayCreateOrderItems(orderItems);
      resetAddItemForm();
  })
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
  getProductByBarcode(item.barcode, (product) => {
        if(product.mrp<item.sellingPrice){
          showError("Selling Price should not be greater than MRP: "+product.mrp);
          return;
        }
        addItem(item);
        displayEditOrderItems(orderItems);
        resetEditAddItemForm();
    })
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

