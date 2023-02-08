
function getSalesReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report/sales-report";
}

function getSalesReportList(){
    $form = $('#report-form');
    var json = toJson($form);
    json=JSON.parse(json);
    console.log(json);
    //check if brand and category exists as keys in json
    if(!('brand' in json)){
        json['brand']='placeholder';
    }
    if(!('category' in json)){
        json['category']='placeholder';
    }
    if(json['startDate']=='' || json['endDate']=='' || json['brand']=='placeholder' || json['category']=='placeholder'){
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Please fill all the fields!',
        })
        return;
    }
    var startDate = new Date(json['startDate']);
    var endDate = new Date(json['endDate']);
    if(startDate > endDate){
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Start date cannot be greater than end date',
        });
        return;
    }
	var url = getSalesReportUrl()+"/?brandName="+json['brand']+"&category="+json['category']+"&startDate="+json['startDate']+"&endDate="+json['endDate'];
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {

        displaySalesReportList(data);
	   },
	   error: (response)=>{
		handleError(response);
	   }
	});
}


function roundToTwo(num) {    
	return +(Math.round(num + "e+2") + "e-2");
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
		+ '<td>' + roundToTwo(e.revenue) + '</td>'
		+ '</tr>';
        $tbody.append(row);
		j++;	
	}
	paginate("#dtBasicExample");
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
    let headers = "S.no,Brand,Category,Quantity,Revenue\n";
    csv += headers;
    data.map( row => csv += row.join( ',' ) + '\n' )
}

function downloadCSV(){
	const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement("a");

    link.style.display = 'none';
    link.href = URL.createObjectURL(blob);
    link.download = 'sales_report.csv';

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
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
            text: 'Start date cannot be greater than end date!',
        })
    }
    else{
    var url = getSalesReportUrl()+"/sales-report";
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
}

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}

function populateBrandDropDown(){
    var url = getBrandUrl();
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            var brandSet = new Set();
			for(var i in data){
				var e = data[i];
				brandSet.add(e.brand);
			}
            data = Array.from(brandSet);
            var $select = $('#inputBrand');
			$select.append('<option value="all">' + 'All' + '</option>');
            for(var i in data){
                var e = data[i];
                var option = '<option value="' + e + '">' + e + '</option>';
                $select.append(option);
            }
        },
        error: handleError
    });
}

function populateCategoryDropdown(){
    var url = getBrandUrl();
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            var categorySet = new Set();
            for(var i in data){
                var e = data[i];
                categorySet.add(e.category);
            }
            data = Array.from(categorySet);
            var $select = $('#inputCategory');
			$select.append('<option value="all">' + 'All' + '</option>');
            $select.removeAttr('disabled');
            for(var i in data){
                var e = data[i];
                var option = '<option value="' + e + '">' + e + '</option>';
                $select.append(option);
            }
        },
        error: handleError
    });
}



//INITIALIZATION CODE
function init(){
    $('#get-sales-report').click(getSalesReportList);
    $("#endDate").prop("max", function(){
        return new Date().toJSON().split('T')[0];
    });
    $("#download-report").click(downloadCSV);
    $('#inputCategory').on('click', (e)=>{
        if(e.target.value == "placeholder")
            populateCategoryDropdown();
    });
    $('#inputBrand').on('click', (e)=>{
        if(e.target.value == "placeholder")
            populateBrandDropDown();
    });
}



function paginate(id) {
	$(id).DataTable();
	$('.dataTables_length').addClass('bs-select');
    let table = $(id).DataTable();
    let data = table.rows().data();
    jsontocsv(data);
}


// $(document).ready(populateBrandDropDown);

$(document).ready(init);
$(document).ready(()=>{
    //set startDate as 1 january 2023 and endDate as today
    document.getElementById("startDate").valueAsDate = new Date("2023-01-01");
    document.getElementById("endDate").valueAsDate = new Date();
})