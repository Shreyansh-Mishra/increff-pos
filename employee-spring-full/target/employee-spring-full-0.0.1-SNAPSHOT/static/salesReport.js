
function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report";
}

function getSalesReportList(){
    $form = $('#report-form');
    var json = toJson($form);
    json=JSON.parse(json);
	var url = getSalesReportUrl()+"/get-sales-report"+"/"+json['startDate']+"/"+json['endDate'];
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   	if(json['brand']!='all' && json['category']!='all'){
            let filteredData = []
            for(d of data){
                if(d.brand==json['brand'] && d.category==json['category']){
                    filteredData.push(d);
                }
            }
            data = filteredData;
        }
        else if(json['brand']!='all'){
            let filteredData = []
            for(d of data){
                if(d.brand==json['brand']){
                    filteredData.push(d);
                }
            }
            data = filteredData;
        }	

        else if(json['category']!='all'){
            let filteredData = []
            for(d of data){
                if(d.category==json['category']){
                    filteredData.push(d);
                }
            }
            data = filteredData;
        }
        
        displaySalesReportList(data);
              
	   },
	   error: (response)=>{
		handleError(response);
	   }
	});
}


function displaySalesReportList(data){
	var $tbody = $('#dtBasicExample').find('tbody');
	$('#dtBasicExample').DataTable().destroy();
	$tbody.empty();
	let j=1;
    console.log(data);
	for(var i in data){
		var e = data[i];
        var buttonHtml = ' <button class="btn btn-link btn-sm btn-rounded" onclick="displayWholeSalesReport('+e.id+')">view</button>'
		var row = '<tr>'
        + '<td>' + j + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + Math.round((e.revenue + Number.EPSILON) * 100) / 100 + '</td>'
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
    var url = getSalesReportUrl()+"/get-sales-report";
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
            displaySalesReportList(filteredData);
	   },
	   error: (response)=>{
		handleError(response);
	   }
	});
}



//INITIALIZATION CODE
function init(){
    $('#get-sales-report').click(getSalesReportList);
    $("#endDate").prop("max", function(){
        return new Date().toJSON().split('T')[0];
    });
}



function paginate(id) {
	$(id).DataTable();
	$('.dataTables_length').addClass('bs-select');
}

$(document).ready(init);
