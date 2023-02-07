function getDailySalesReportUrl(){
   var baseUrl = $("meta[name=baseUrl]").attr("content")
   return baseUrl + "/api/reports/daily-sales";
}
const dayDate= ["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"];

function filterSalesReport() {
    var url = getDailySalesReportUrl();
    makeAjaxCall(url,'GET',{},(response)=> {
        displaySalesReport(response);
    },handleAjaxError);
}

function displaySalesReport(data) {
    var $tbody = $('#daily-sales-table').find('tbody');
    $tbody.empty();
    for(var i in data){
        var b = data[i];
        var row = '<tr>'
        + '<td>&nbsp;</td>'
        + '<td>' + convertTimeStampToDateTime(b.date) + '</td>'
        + '<td>' + b.orderCount + '</td>'
        + '<td>' + b.itemCount + '</td>'
        + '<td style="text-align: right;">' + '₹' + numberWithCommas(b.totalRevenue.toFixed(2)) + '</td>'
        + '</tr>';
        $tbody.append(row);
    }
}

//INITIALIZATION CODE
function init(){
   $('#get-daily-sales-report').click(filterSalesReport);
}

function convertTimeStampToDateTime(timestamp) {
    var date = new Date(timestamp);
    return (
      date.getDate() +
      "/" +
      (date.getMonth() + 1) +
      "/" +
      date.getFullYear() +
      " (" +
      dayDate[date.getDay()] + ")"
    );
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
$(document).ready(filterSalesReport);