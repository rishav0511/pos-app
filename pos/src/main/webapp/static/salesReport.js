function getSalesReportUrl(){
   var baseUrl = $("meta[name=baseUrl]").attr("content")
   return baseUrl + "/api/reports/sales";
}

function filterSalesReport() {
    var $form = $("#sales-form");
    var json = toJson($form);
    var url = getSalesReportUrl();
    $.ajax({
       url: url,
       type: 'POST',
       data: json,
       headers: {
        'Content-Type': 'application/json'
       },
       success: function(response) {
            displaySalesReport(response);
       },
       error: handleAjaxError
    });
}

function displaySalesReport(data) {
    var $tbody = $('#sales-table').find('tbody');
    $tbody.empty();
    for(var i in data){
        var b = data[i];
        var row = '<tr>'
        + '<td>&nbsp;</td>'
        + '<td>' + b.brand + '</td>'
        + '<td>' + b.category + '</td>'
        + '<td>' + b.quantity + '</td>'
        + '<td style="text-align: right;">' + numberWithCommas(b.revenue.toFixed(2)) + '</td>'
        + '</tr>';
        $tbody.append(row);
    }
}

//INITIALIZATION CODE
function init(){
   $('#filter-sales-report').click(filterSalesReport);
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