
//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    return json;
}


function handleError(response){
    Swal.fire({
        title: "Error",
        text: JSON.parse(response.responseText).message,
        icon: "error",
      });
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

function handleSuccess(message){
    Swal.fire({
        title: "Success",
        text: message,
        icon: "success",
      });
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


$(document).ready(function() {
    var currentPage = window.location.pathname;
    $('.nav-item a[href="' + currentPage + '"]').parent().addClass('selected');
  
    $('.nav-item').click(function() {
      $('.nav-item').removeClass('selected');
      $(this).addClass('selected');
    });
  });
