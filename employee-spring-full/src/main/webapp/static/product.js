
function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	var $form = $("#product-form");
	var json = toJson($form);
	var url = getProductUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {	
		getProductList();
		handleSuccess("Product added successfully");
	   },
	   error: (response) => {
		handleError(response);
	   }
	});

	return false;
}

function updateProduct(id,brand,category,name,mrp,barcode){
	var url = getProductUrl() + "/" + id;
	let productData = {barcode:barcode,brandName:brand,category:category,name:name,mrp:mrp}
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: JSON.stringify(productData),
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
			handleSuccess("Product updated successfully");
	   		getProductList();   
	   },
	   error: (response)=>{
		handleError(response);
	   }
	});

	return false;
}


function getProductList(){
	var url = getProductUrl()+"s";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);  
	   },
	   error: (response)=>{
		handleError(response);
	   }
	});
}


// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#employeeFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
		if(errorData.length>0){
			alert("There was some problem with some of your entries!");
		}
		else if(errorData.length==0){
			handleSuccess("All products uploaded successfully");
			$('#upload-product-modal').modal('toggle');
		}
		getProductList();
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getProductUrl();
	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		uploadRows();  
	   },
	   error: function(response){
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}

//UI DISPLAY METHODS

function roundToTwo(num) {    
	return +(Math.round(num + "e+2") + "e-2");
  }

function displayProductList(data){
	var $tbody = $('#dtBasicExample').find('tbody');
	$('#dtBasicExample').DataTable().destroy();
	$tbody.empty();
	let j=1;
	for(var i in data){
		var e = data[i];
		var role = $('.get-role').find('span').text();

		var buttonHtml = ` <button class="btn btn-link btn-sm btn-rounded" onclick="displayEditProduct(${e.id},'${e.brandName}','${e.category}')">edit</button>`
		var row;
		if(role=='supervisor'){
			row = '<tr>'
			+ '<td>' + j + '</td>'
			+ '<td>' + e.name + '</td>'
			+ '<td>'  + e.barcode + '</td>'
			+ '<td>'  + e.brandName + '</td>'
			+ '<td>'  + e.category + '</td>'
			+ '<td align="right">'  + roundToTwo(e.mrp).toFixed(2) + '</td>'
			+ '<td>' + buttonHtml + '</td>'
			+ '</tr>';
		}
		else{
			row = '<tr>'
			+ '<td>' + j + '</td>'
			+ '<td>' + e.name + '</td>'
			+ '<td>'  + e.barcode + '</td>'
			+ '<td>'  + e.brandName + '</td>'
			+ '<td>'  + e.category + '</td>'
			+ '<td align="right">'  + roundToTwo(e.mrp).toFixed(2) + '</td>'
			+ '</tr>';
		}
		$tbody.append(row);
		j++;
	}
	paginate();
}

function displayEditProduct(id,brandName,category){
	var url = getProductUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data,brandName,category);   
	   },
	   error: handleError
	});	
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#employeeFile');
	$file.val('');
	$('#employeeFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#employeeFile');
	var fileName = $file.val();
	$('#employeeFileName').html(fileName.split('\\')[fileName.split('\\').length-1]);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-product-modal').modal('toggle');
}

function displayProduct(data,brandName,category){
	$("#product-edit-form input[name=name]").val(data.name);	
	$("#product-edit-form input[name=brandName]").val(brandName);	
	$("#product-edit-form input[name=category]").val(category);
	$("#product-edit-form input[name=mrp]").val(data.mrp);
	$("#product-edit-form input[name=barcode]").val(data.barcode);	
	$("#product-edit-form input[name=id]").val(data.id);
	// $('#edit-product-modal').modal('toggle');
	Swal.fire({
		title: 'Edit Product',
		width: '70%',
		html:`<form class="form-inline" id="#product-edit-form">
		<div class="container">
		<div class="form-outline row">
		<label class="col" for="name">Product Name</label>
		<input placeholder="Product Name" value="${data.name}" id="name" type="text" name="name" class="swal2-input col" />
		</div>

		<div class="form-outline row">
		<label class="col" for="brand">Brand Name</label>
		<input placeholder="Brand Name" value="${brandName}" id="brand" type="text" name="brandName" class="swal2-input col" />
		</div>
		
		<div class="form-outline row">
		<label class="col" for="category">Category</label>
		<input placeholder="Category" value="${category}" id="category" type="text" name="category" class="swal2-input col" />
		</div>

		<div class="form-outline row">
		<label class="col" for="mrp">MRP</label>
		<input placeholder="MRP" pattern="^\\d*(\\.\\d{0,2})?$" value="${data.mrp}" id="mrp" type="number" name="mrp" class="swal2-input col" />
		</div>

		<div class="form-outline row">
		<label class="col" for="barcode">Barcode</label>
		<input placeholder="Barcode" value="${data.barcode}" id="barcode" type="text" name="barcode" class="swal2-input col" />
		</div>

		<input id="id" type="hidden" value=${data.id} name="id">
		</div>
		</form>`,
  		showCancelButton: true,
  		confirmButtonText: `Save`,
  		denyButtonText: `Don't save`,
		preConfirm: () => {
			let id = Swal.getPopup().querySelector('#id').value;
			let brand = Swal.getPopup().querySelector('#brand').value;
			let category = Swal.getPopup().querySelector('#category').value;
			let name = Swal.getPopup().querySelector('#name').value;
			let mrp = Swal.getPopup().querySelector('#mrp').value;
			let barcode = Swal.getPopup().querySelector('#barcode').value;
			return {id,brand,category,name,mrp,barcode}
		}
	}).then((result)=>{
		updateProduct(result.value.id,result.value.brand,result.value.category,result.value.name,result.value.mrp,result.value.barcode);
	})
}


//INITIALIZATION CODE
function init(){
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#employeeFile').on('change', updateFileName);
    $('#inputBrand').on('change', populateCategoryDropdown);
}

function populateCategoryDropdown(){
    var url = getBrandUrl() + "/get-categories/"+document.getElementById("inputBrand").value;
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            var $select = $('#inputCategory');
            $select.empty();
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

function populateBrandDropDown(){
    var url = getBrandUrl()+"s";
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data) {
            var $select = $('#inputBrand');
            $select.empty();
			$select.append('<option value="" disabled selected style="display: none;">' + 'Choose Brand' + '</option>');
			$select.attr('placeholder', 'Select Brand');
            for(var i in data){
                var e = data[i];
                var option = '<option value="' + e.brand + '">' + e.brand + '</option>';
                $select.append(option);
            }
        },
        error: handleError
    });
}

$(document).ready(init);
$(document).ready(getProductList);
$(document).ready(populateBrandDropDown);

$(document).on('keydown', 'input[pattern]', function(e){
	var input = $(this);
	var oldVal = input.val();
	var regex = new RegExp(input.attr('pattern'), 'g');
  
	setTimeout(function(){
	  var newVal = input.val();
	  if(!regex.test(newVal)){
		input.val(oldVal); 
	  }
	}, 1);
  });

function paginate() {
	$('#dtBasicExample').DataTable();
	$('.dataTables_length').addClass('bs-select');
}