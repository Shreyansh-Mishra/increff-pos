
function getReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report";
}

function getReportList(){
	let startDate = $('#startDate').val();
	let endDate = $('#endDate').val();
	var url = getReportUrl()+"/day-wise-report"+"?startDate="+startDate+"&endDate="+endDate;
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
	if(data.length==0){
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'No data found!',
        })
        return;
    }
    csv='';
    console.log(data);
    let headers = "Date,Invoiced Orders,Invoiced Items,Total Revenue\n";
	csv += headers;
    data.map( row => csv += row.join( ',' ) + '\n' )
}

function downloadCSV(){
	var hiddenElement = document.createElement('a');
	hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURI(csv);
	hiddenElement.target = '_blank';
	hiddenElement.download = 'daywise_report.csv';
	hiddenElement.click();
}

function roundToTwo(num) {    
	return +(Math.round(num + "e+2") + "e-2");
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
		+ '<td>' + roundToTwo(e.total_revenue) + '</td>'
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
	let table = $(id).DataTable();
    let data = table.rows().data();
    jsontocsv(data);
}

$(document).ready(init);
// $(document).ready(getReportList);
$(document).ready(()=>{
    //set startDate as 1 january 2023 and endDate as today
    document.getElementById("startDate").valueAsDate = new Date("2023-01-01");
    document.getElementById("endDate").valueAsDate = new Date();
})