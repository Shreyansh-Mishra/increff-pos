
function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}


//BUTTON ACTIONS
function addInventory(event){
	//Set the values to update
	var $form = $("#inventory-form");
	var json = toJson($form);
	var url = getInventoryUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getInventoryList();  
	   },
	   error: (response)=>{
			handleError(response);
		}
	});

	return false;
}

function updateInventory(id,quantity,barcode){
	//Get the ID
	var url = getInventoryUrl() + "/"+id;

	//Set the values to update
	let jsoni = {quantity,barcode};
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: JSON.stringify(jsoni),
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
			handleSuccess('Inventory Updated!');
	   		getInventoryList();   
	   },
	   error: (response)=>{
		handleError(response);
		}
	});

	return false;
}


function getInventoryList(){
	//empty all the error present in form
	$("#inventory-form").find(".alert").remove();
	var url = getInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryList(data);  
	   },
	   error: (response)=>{
		handleError(response);
		}
	});
}

function deleteInventory(id){
	var url = getInventoryUrl() + "/delete/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getInventoryList();  
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
	var file = $('#inventoryFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	if(fileData.length>5000){
		Swal.fire({title: "Error",text: "You can upload maximum 5000 rows at a time",icon: "error",});
		return;
	}
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
		$('#upload-inventory-modal').modal('toggle');
			handleSuccess("Inventory updated successfully");
		}
			getInventoryList();
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getInventoryUrl();
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

var barcode;

function displayInventoryList(data){
	var $tbody = $('#dtBasicExample').find('tbody');
	$('#dtBasicExample').DataTable().destroy();
	$tbody.empty();
	let j=1;
	console.log(data);
	for(var i in data){
		var e = data[i];
		console.log(e);
		var role = $('.get-role').find('span').text();
		var buttonHtml = ` <button class="btn btn-link btn-sm btn-rounded" onclick="displayEditInventory(${e.id},'${e.barcode}')">edit</button>`
		var row;
		if(role == "supervisor"){
			row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
		}
		else{
			row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '</tr>';
		}
        $tbody.append(row);
		j++;	
	}
	paginate();
}

function displayEditInventory(id,barcode){
	console.log(barcode);
	var url = getInventoryUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventory(data,barcode);   
	   },
	   error: (response)=>{
		handleError(response);
		}
	});	
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
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
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	$('#inventoryFileName').html(fileName.split('\\')[fileName.split('\\').length-1]);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-inventory-modal').modal('toggle');
}

function displayInventory(data,barcode){
	console.log(barcode);
	Swal.fire({
		title: 'Edit Inventory',
		width: "40%",
		html:`<form class="form-inline" id="#inventory-edit-form">
		<div class="container">
		<div class="form-outline row">
		<label class="col" for="quantity">Enter New Quantity</label>
		<input placeholder="Quantity" value="${data.quantity}" id="quantity" type="number" name="quantity" class="swal2-input col" />
		</div>
		<input type="hidden" id="id" value="${data.id}"/>
		<input type="hidden" id="barcode" value="${barcode}"/>
		</div>
		</form>`,
  		showCancelButton: true,
  		confirmButtonText: `Save`,
		preConfirm: () => {
			let id = Swal.getPopup().querySelector('#id').value;
			let quantity = Swal.getPopup().querySelector('#quantity').value;
			let barcode = Swal.getPopup().querySelector('#barcode').value;
			return {id,quantity,barcode}
		}
	}).then((result)=>{
		updateInventory(result.value.id,result.value.quantity,result.value.barcode);
	})
}


//INITIALIZATION CODE
function init(){
	$('#add-inventory').click(addInventory);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName)
}

function paginate() {
	$('#dtBasicExample').DataTable();
	$('.dataTables_length').addClass('bs-select');
}

$(document).ready(init);
$(document).ready(getInventoryList);

