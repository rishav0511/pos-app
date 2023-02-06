
//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
//    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}

function showError(message) {
    $('.notifyjs-wrapper').trigger('notify-hide');
    $.notify.defaults({clickToHide:true,autoHide:false});
    $.notify(message, "error");
}

function handleAjaxError(response){
	var response = JSON.parse(response.responseText);
	showError(response.message);
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}	
	}
	Papa.parse(file, config);
}


function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};
	
	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.tsv');
    tempLink.click(); 
}


function extractNameAndCategory(brandCategory) {
    var index = brandCategory.indexOf('~');
    var name = brandCategory.substr(0, index);
    var category = brandCategory.substr(index + 1);
    return {
        "brandName": name,
        "brandCategory": category
    };
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
}


//prevent 'e' press in number field
var invalidChars = [
    "-",
    "+",
    "e",
];
const verifyNumberInput = () =>  document.querySelectorAll('input[type="number"]').forEach( input => input.addEventListener("keydown", function(e) {
    if (invalidChars.includes(e.key)) {
      e.preventDefault();
    }
  }));
verifyNumberInput();
// 0 for null values
// 8 for backspace
// 48-57 for 0-9 numbers
