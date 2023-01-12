
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function convertToArrayOfObject(data){
	var serialized = data.serializeArray();
	let arr = []
	//Convert to array of object
	for(let i=0;i<serialized.length;i+=3){
		let obj = {};
		obj['barcode'] = serialized[i].value;
		obj['mrp'] = serialized[i+1].value;
		obj['quantity'] = serialized[i+2].value;
		arr.push(obj);
	}
	return JSON.stringify(arr);
}

//BUTTON ACTIONS
function addOrder(event){
	$('#create-order-modal').modal('toggle');
	var url = getOrderUrl()+"/add-order";
	let data = []
	for(let i=0;i<orderArr.length;i++){
		if(deleteArr.includes(i)){
			continue;
		}
		data.push(orderArr[i]);
	}
	console.log(data);
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: JSON.stringify(data),
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		handleSuccess("Order Created");
			getOrderList();
			orderArr = [];
			deleteArr = [];
			barcodeMap = {};
			i=1;
			clearOrderItemsTable();
	   },
	   error: (response)=>{
		handleError(response);
	   }
	});

	return false;
}

function clearOrderItemsTable(){
	console.log($("#dtBasicExample-order-create > tbody").html)
	$("#dtBasicExample-order-create").DataTable().clear().draw();
}

function updateOrder(event){
	$('#edit-order-modal').modal('toggle');
	//Get the ID
	var id = $("#order-edit-form input[name=id]").val();	
	var url = getOrderUrl() + id;

	//Set the values to update
	var $form = $("#order-edit-form");
	let jsoni = toJson($form);
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: jsoni,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getOrderList();   
	   },
	   error: (response)=>{
		handleError(response);
	   }
	});

	return false;
}


function getOrderList(){
	//empty all the error present in form
	$("#order-form").find(".alert").remove();
	var url = getOrderUrl()+"/get-orders";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderList(data);  
	   },
	   error: (response)=>{
		handleError(response);
	   }
	});
}

function deleteOrder(id){
	var url = getOrderUrl() + "/delete/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getOrderList();  
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
	var file = $('#orderFile')[0].files[0];
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
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getOrderUrl()+"/add-order";
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

function displayOrderList(data){
	var $tbody = $('#dtBasicExample').find('tbody');
	$('#dtBasicExample').DataTable().destroy();
	$tbody.empty();
	let j=1;
	for(var i in data){
		var e = data[i];
        var buttonHtml = ' <button onclick="displayWholeOrder('+e.id+')">view</button>'
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.id + '</td>'
		+ '<td>'  + e.time.toString() + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
		j++;	
	}
	paginate("#dtBasicExample");
}

function displayWholeOrder(id){
    var url = getOrderUrl() + "/" + id;
    $.ajax({
       url: url,
       type: 'GET',
       success: function(data) {
       		displayOrderItems(data);   
       },
       error: (response)=>{
		handleError(response);
	   }
    });	
}

function displayOrderItems(data){
    var $tbody = $('#dtBasicExample2').find('tbody');
	$('#dtBasicExample2').DataTable().destroy();
    $tbody.empty();
    let j=1;
    for(var i in data){
        var e = data[i];
        var row = '<tr>'
        + '<td>' + j + '</td>'
        + '<td>' + e.itemName + '</td>'
        + '<td>'  + e.barcode + '</td>'
        + '<td>' + e.orderId + '</td>'
		+ '<td>' + e.quantity + '</td>'
        + '<td>' + e.sellingPrice + '</td>'
        + '</tr>';
        $tbody.append(row);
        j++;	
    }
    paginate("#dtBasicExample2");
    $('#view-order-modal').modal('toggle');
}

function displayEditOrder(id){
	var url = getOrderUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrder(data);   
	   },
	   error: (response)=>{
		handleError(response);
	   }
	});	
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#orderFile');
	$file.val('');
	$('#orderFileName').html("Choose File");
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
	var $file = $('#orderFile');
	var fileName = $file.val();
	$('#orderFileName').html(fileName);
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-order-modal').modal('toggle');
}

function displayOrder(data){
	$("#order-edit-form input[name=name]").val(data.name);	
	$("#order-edit-form input[name=age]").val(data.age);	
	$("#order-edit-form input[name=id]").val(data.id);	
	$('#edit-order-modal').modal('toggle');
}

function displayCreateOrder(){	
	$('#create-order-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-order').click(displayCreateOrder);
	$('#create-order').click(addOrder);
	$('#update-order').click(updateOrder);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#orderFile').on('change', updateFileName);
	$('#add-row').click(addRow);
}

let i=1;

let orderArr = []

let deleteArr = []

let barcodeMap = {}

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

function addRow(){
	$form = $('#order-create-form');
	var barcode = $form.find('input[name=barcode]').val();
	var mrp = $form.find('input[name=mrp]').val();
	var q = $form.find('input[name=quantity]').val();
	if(barcode=='' || mrp=='' || q==''){
		Swal.fire({
			title: "Error",
			text: "Please fill all the fields!",
			icon: "error",
		  });
		return;
	}
	let isExist = $('.'+barcode).length
	$form.find('input[name=barcode]').val('');
	$form.find('input[name=mrp]').val('');
	$form.find('input[name=quantity]').val('');
	if(isExist>0){
		if(orderArr[barcodeMap[barcode]-1]['mrp']!=mrp){
			Swal.fire({
				title: "Error",
				text: "Selling price of already existing item can't be different!",
				icon: "error",
			  });
			return;
		}
		let quant = parseInt(q) + parseInt($('.'+barcode).find('input[name=quantity]').val())
		$('.'+barcode).find('input[name=quantity]').val(quant)
		orderArr[barcodeMap[barcode]-1]['quantity'] = quant
	}
	else{
	barcodeMap[barcode] = i;
	orderArr.push({barcode:barcode,mrp:mrp,quantity:q});
	console.log(orderArr);
	// $('#dtBasicExample-order-create').DataTable().destroy();
	var $tbody = $('#dtBasicExample-order-create').find('tbody');
	var row = '<tr id='+i+' class='+barcode+'>'
	+ '<td>' + '<input type="text" disabled name=barcode value='+barcode+'  /></td>'
	+ '<td>'  + '<input type="number" pattern="^\\d*(\\.\\d{0,2})?$" disabled name=mrp value='+mrp+' /></td>'
	+ '<td>' + '<input type="number" disabled name=quantity value='+q+' /></td>'
	+ '<td>'  + '<button id='+i+' onclick="editRow('+i+')">edit</button>'+ '&nbsp<button onclick="deleteRow('+i+')">delete</button>' + '</td>'
	+ '</tr>';
	$tbody.append(row);
	i++;
	paginate("#dtBasicExample-order-create");
	}
}

function editRow(i){
	var $tr = $('#'+i);
	var $editRow = $tr.find('button[id='+i+']');
	$editRow.html('save');
	$tr.find('input').removeAttr('disabled');
	//input field barcode should remained disabled
	$tr.find('input[name=barcode]').attr('disabled', 'disabled');
	$editRow.attr('onclick', 'saveRow('+i+')');
	console.log(orderArr);
}

function saveRow(i){
	var $tr = $('#'+i);
	console.log('.'+$tr.attr('class'));
	delete barcodeMap['.'+$tr.attr('class')];
	$tr.removeAttr('class');
	$tr.addClass($tr.find('input[name=barcode]').val());
	var $editRow = $tr.find('button[id='+i+']');
	$editRow.html('edit');
	$tr.find('input').attr('disabled', 'disabled');
	orderArr[i-1].barcode = $tr.find('input[name=barcode]').val();
	orderArr[i-1].mrp = $tr.find('input[name=mrp]').val();
	orderArr[i-1].quantity = $tr.find('input[name=quantity]').val();
	barcodeMap[$tr.find('input[name=barcode]').val()] = i;
	//add new barcode as class to the tr element
	$editRow.attr('onclick', 'editRow('+i+')');
	console.log(orderArr);
}

function deleteRow(i){
	deleteArr.push(i-1);
	$('#'+i).remove();
	delete barcodeMap[orderArr[i-1].barcode];
	console.log(deleteArr);
}

function paginate(id) {
	$(id).DataTable();
	$('.dataTables_length').addClass('bs-select');
}

$(document).ready(init);
$(document).ready(getOrderList);

