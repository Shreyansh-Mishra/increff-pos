
function getReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report";
}

function getReportList(){
	let startDate = $('#startDate').val();
	let endDate = $('#endDate').val();
	var url = getReportUrl()+"/get-day-wise-report"+"/"+startDate+"/"+endDate;
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

let csv = '';

function jsontocsv(data){
	if(data.length==0){
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'No data found!',
        })
        return;
    }
    csv = '';
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
	hiddenElement.download = 'daywise_report.csv';
	hiddenElement.click();
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
		+ '<td>' + e.date.split('T')[0] + '</td>'
		+ '<td>' + e.invoiced_orders_count + '</td>'
		+ '<td>'  + e.invoiced_items_count + '</td>'
		+ '<td>' + Math.round((e.total_revenue + Number.EPSILON) * 100) / 100 + '</td>'
		+ '</tr>';
        $tbody.append(row);
		j++;	
	}
	paginate("#dtBasicExample");
}


//INITIALIZATION CODE
function init(){
    $('#get-report').click(getReportList);
	$("#endDate").prop("max", function(){
        return new Date().toJSON().split('T')[0];
    });
	$('#download-report').click(downloadCSV);
}



function paginate(id) {
	$(id).DataTable();
	$('.dataTables_length').addClass('bs-select');
}

$(document).ready(init);
// $(document).ready(getReportList);
