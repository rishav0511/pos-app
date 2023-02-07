function getSalesReportUrl(){
   var baseUrl = $("meta[name=baseUrl]").attr("content")
   return baseUrl + "/api/reports/sales";
}

function filterSalesReport() {
    var $form = $("#sales-form");
    var json = toJson($form);
    var data = JSON.parse(json);
    if(Date.parse(data["startDate"]) > Date.parse(data["endDate"])) {
        $('.notifyjs-wrapper').trigger('notify-hide');
        $.notify.defaults({clickToHide:true,autoHide:false});
        $.notify("Start Date is greater than End date", "error");
        return;
    }
    var url = getSalesReportUrl();
    makeAjaxCall(url,'POST',json,(response)=>{
            displaySalesReport(response);
    },handleAjaxError);
}

function displaySalesReport(data) {
    if(data.length===0)
    {
        $('.notifyjs-wrapper').trigger('notify-hide');
        $.notify.defaults({clickToHide:true,autoHide:false});
        $.notify("No Data Found", "error");
        return;
    }
    $('#sales-table').show();
    var $tbody = $('#sales-table').find('tbody');
    $tbody.empty();
    $('#results-found').text("There were " + data.length + " results found.")
    for(var i in data){
        var b = data[i];
        var row = '<tr>'
        + '<td>' + b.brand + '</td>'
        + '<td>' + b.category + '</td>'
        + '<td>' + b.quantity + '</td>'
        + '<td style="text-align: right;">' + '₹' + numberWithCommas(b.revenue.toFixed(2)) + '</td>'
        + '</tr>';
        $tbody.append(row);
    }
    $(location).attr('href','#filter-sales-report');
}
function startDateChanged(event){
    if( $('#inputEndDate').val()==="") {
        $('#inputEndDate').attr('min',event.target.value)
    }
}

function endDateChanged(event){
    if( $('#inputStartDate').val()==="") {
        $('#inputStartDate').attr('max',event.target.value);
    }
}
//INITIALIZATION CODE
function init(){
   $('#filter-sales-report').click(filterSalesReport);
   $('#sales-table').hide();
   $(function(){
       var dtToday = new Date();

       var month = dtToday.getMonth() + 1;
       var day = dtToday.getDate();
       var year = dtToday.getFullYear();

       if(month < 10)
           month = '0' + month.toString();
       if(day < 10)
           day = '0' + day.toString();

       var maxDate = year + '-' + month + '-' + day;
       $('#inputStartDate').attr('max', maxDate);
       $('#inputEndDate').attr('max', maxDate);
   });
   $('#inputStartDate').change(startDateChanged);
   $('#inputEndDate').change(endDateChanged);
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

$(document).ready(init);