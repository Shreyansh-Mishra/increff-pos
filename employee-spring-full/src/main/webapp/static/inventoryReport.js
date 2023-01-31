
function getReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report";
}

function getReportList(){
	var url = getReportUrl()+"/get-inventory-report";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
			jsontocsv(data);
	   		displayReportList(data);  
	   },
	   error: (response)=>{
		handleError(response);
	   }
	});
}


function displayReportList(data){
	var $tbody = $('#dtBasicExample').find('tbody');
	$('#dtBasicExample').DataTable().destroy();
	$tbody.empty();
	let j=1;
    console.log(data);
	for(var i in data){
		var e = data[i];
        var buttonHtml = ' <button class="btn btn-link btn-sm btn-rounded" onclick="displayWholeReport('+e.id+')">view</button>'
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '</tr>';
        $tbody.append(row);
		j++;	
	}
	paginate("#dtBasicExample");
}

function filterByDate(){
    var url = getReportUrl()+"/get-sales-report";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
            displayReportList(data);
	   },
	   error: (response)=>{
		handleError(response);
	   }
	});
}


let csv = '';

function jsontocsv(data){
	const keys = Object.keys(data[0]);
	csv += keys.join(',') + '\n';
	data.forEach(item=>{
		csv += Object.values(item).join(',') + '\n';
	})
}

function downloadCSV(){
	var hiddenElement = document.createElement('a');
	hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURI(csv);
	hiddenElement.target = '_blank';
	hiddenElement.download = 'inventory_report.csv';
	hiddenElement.click();
}


//INITIALIZATION CODE
function init(){
    $('#get-report').click(filterByDate);
	$('#download-report').click(downloadCSV);
}



function paginate(id) {
	$(id).DataTable();
	$('.dataTables_length').addClass('bs-select');
}

$(document).ready(init);
$(document).ready(getReportList)
