
function getReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report";
}

function getReportList(){
	let brand = $('#inputBrand').val();
	let category = $('#inputCategory').val();
	var url = getReportUrl()+"/inventory-report"+"/"+brand+"/"+category;
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
    let headers = "S.no,Brand,Category,Quantity\n"
    csv += headers;
    data.map( row => csv += row.join( ',' ) + '\n' )
}

function downloadCSV(){
	var hiddenElement = document.createElement('a');
	hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURI(csv);
	hiddenElement.target = '_blank';
	hiddenElement.download = 'inventory_report.csv';
	hiddenElement.click();
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
    $('#download-report').click(downloadCSV);
	$('#get-inventory-report').click(getReportList);
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

$(document).ready(init);
