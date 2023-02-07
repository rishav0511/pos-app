function getbrandReportUrl() {
  var baseUrl = $("meta[name=baseUrl]").attr("content");
  return baseUrl + "/api/reports/brand";
}

function getbrandReport() {
  var url = getbrandReportUrl();
  makeAjaxCall(url,'GET',{},(data)=> {
          displaybrandReportList(data);
  },handleAjaxError);
}

//UI DISPLAY METHODS
function displaybrandReportList(data) {
  var $tbody = $("#brandCategory-table").find("tbody");
  $tbody.empty();
  for (var i in data) {
    var e = data[i];
    var row =
      "<tr>" +
      "<td>" +
      e.brand +
      "</td>" +
      "<td>" +
      e.category +
      "</td>" +
      "</tr>";
    $tbody.append(row);
  }
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

//INITIALIZATION CODE
function init() {
  $("#refresh-data").click(getbrandReport);
}

$(document).ready(init);
$(document).ready(getbrandReport);