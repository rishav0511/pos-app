function getDailySalesReportUrl(){
   var baseUrl = $("meta[name=baseUrl]").attr("content")
   return baseUrl + "/api/reports/daily-sales";
}

function filterSalesReport() {
    var url = getDailySalesReportUrl();
    console.log(url);
    $.ajax({
       url: url,
       type: 'GET',
       success: function(response) {
            console.log(response);
            displaySalesReport(response);
       },
       error: handleAjaxError
    });
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
        + '<td style="text-align: right;">' + numberWithCommas(b.totalRevenue.toFixed(2)) + '</td>'
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
      " " +
      date.getHours() +
      ":" +
      date.getMinutes() +
      ":" +
      date.getSeconds()
    );
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
$(document).ready(filterSalesReport);