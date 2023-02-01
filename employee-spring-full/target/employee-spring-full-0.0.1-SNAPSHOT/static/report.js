
function getReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report";
}

function getReportList(){
	var url = getReportUrl()+"/get-sales-report";
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

function filterByDate(){
    var $form = $('#report-form');
    var json = toJson($form);
    json=JSON.parse(json);
    console.log(json);
    var startDate = new Date(json['startDate']);
    var endDate = new Date(json['endDate']);
	if(startDate > endDate){
		Swal.fire({
			icon: 'error',
			title: 'Error',
			text: 'Start date cannot be greater than end date',
		});
	}
	else{
    var url = getReportUrl()+"/get-sales-report";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
            //filter the data between startDate and endDate
            let filteredData = []
            for(d of data){
                let obj = {}
                console.log(new Date(d.date.split('T')[0]), startDate, endDate);
                if(new Date(d.date.split('T')[0]) >= startDate && new Date(d.date.split('T')[0]) <= endDate){
                    obj['date'] = d.date
                    obj['invoiced_orders_count'] = d.invoiced_orders_count
                    obj['invoiced_items_count'] = d.invoiced_items_count
                    obj['total_revenue'] = d.total_revenue
                    filteredData.push(obj)
                }
            }
			jsontocsv(filteredData);
            displayReportList(filteredData);
	   },
	   error: (response)=>{
		handleError(response);
	   }
	});
	}
}



//INITIALIZATION CODE
function init(){
    $('#get-report').click(filterByDate);
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
$(document).ready(getReportList);
