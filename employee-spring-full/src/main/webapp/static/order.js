
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

Number.prototype.padLeft = function(base,chr){
    var  len = (String(base || 10).length - String(this).length)+1;
    return len > 0? new Array(len).join(chr || '0')+this : this;
}

function downloadPdf(id){
	var url = getOrderUrl() + "/invoice/" + id;
	window.open(url, '_blank');
}

function displayOrderList(data){
	var $tbody = $('#dtBasicExample').find('tbody');
	$('#dtBasicExample').DataTable().destroy();
	$tbody.empty();
	let j=1;
	for(var i in data){
		var e = data[i];
		var d = new Date(e.time);
		var time = [(d.getMonth()+1).padLeft(),
			d.getDate().padLeft(),
			d.getFullYear()].join('/') +' ' +
		   [d.getHours().padLeft(),
			d.getMinutes().padLeft()].join(':');
        var buttonHtml = ' <button class="btn btn-link btn-sm btn-rounded" onclick="displayWholeOrder('+e.id+')">view</button> &nbsp <button class="btn btn-link btn-sm btn-rounded" onclick="downloadPdf('+e.id+')">Download</button>';
		var row = '<tr>'
		+ '<td>' + j + '</td>'
		+ '<td>' + e.id + '</td>'
		+ '<td>'  + time + '</td>'
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
        + '<td>' + e.mrp + '</td>'
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

function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

function populateBarcode(){
	var url = getProductUrl() + 's';
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
			//find #inputBarcode and append the rows as option in it
			var $select = $('#inputBarcode');
			for(var i in data){
				var e = data[i];
				var option = '<option value="'+e.barcode+'">'+e.barcode+'</option>';
				$select.append(option);
			}
		},
		error: (response)=>{
			handleError(response);
		} 
	}) 
}

let mrpAndQuantity = {mrp:0, quantity:0};


function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

function getMrpAndQuantity(barcode){
	var url = getProductUrl() + '/barcode/' + barcode;
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
			mrpAndQuantity.mrp = data.mrp;
			$.ajax({
				url: getInventoryUrl() + '/' + data.id,
				type: 'GET',
				success: function(data) {
					mrpAndQuantity.quantity = data.quantity;
				},
				error: (response)=>{
					handleError(response);
				}
			})
		},
		error: (response)=>{
			handleError(response);
		}
	})
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
	$('#add-order').click(populateBarcode);
	$('#inputBarcode').change((e)=>{
		getMrpAndQuantity($('#inputBarcode').val());
	});
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
	var barcode = $form.find('select[name=barcode]').val();
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
	else if(q<0){
		Swal.fire({
			title: "Error",
			text: "Quantity cannot be negative!",
			icon: "error",
		});
		return;
	}
	//check if quantity is an integer
	else if(!Number.isInteger(Number(q))){
		Swal.fire({
			title: "Error",
			text: "Quantity must be an integer!",
			icon: "error",
		});
		return;
	}
	else if(mrp>=mrpAndQuantity.mrp){
		Swal.fire({
			title: "Error",
			text: "The MRP of the product is "+mrpAndQuantity.mrp+"!",
			icon: "error",
		});
		return;
	}
	else if(q>mrpAndQuantity.quantity){
		Swal.fire({
			title: "Error",
			text: "The Available quantity of the product is "+mrpAndQuantity.quantity+"!",
			icon: "error",
		});
		return;
	}
	let isExist = $('.'+barcode).length
	$form.find('select[name=barcode]').val('');
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
		getMrpAndQuantity(barcode);
		let quant = parseInt(q) + parseInt($('.'+barcode).find('input[name=quantity]').val())
		if(quant>mrpAndQuantity.quantity){
			Swal.fire({title: "Error",text: "The Available quantity of the product is "+mrpAndQuantity.quantity+"!",icon: "error" });
			return;
		}
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
	+ '<td>'  + '<button class="btn btn-link btn-sm btn-rounded" id='+i+' onclick="editRow('+i+')">edit</button>'+ '&nbsp<button class="btn btn-link btn-sm btn-rounded" onclick="deleteRow('+i+')">delete</button>' + '</td>'
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
	getMrpAndQuantity($tr.find('input[name=barcode]').val());

	if($tr.find('input[name=quantity]').val()<0){
		Swal.fire({icon: 'error',title: 'Oops...',text: 'Quantity cannot be negative!'});
		return;
	}
	else if(!Number.isInteger(Number($tr.find('input[name=quantity]').val()))){
		Swal.fire({icon: 'error',title: 'Oops...',text: 'Quantity must be an integer!'});
		return;
	}
	else if($tr.find('input[name=mrp]').val()>=mrpAndQuantity.mrp){
		Swal.fire({icon: 'error',title: 'Oops...',text: 'The MRP of the product is '+mrpAndQuantity.mrp+'!'})
		return;
	}
	else if($tr.find('input[name=quantity]').val()>mrpAndQuantity.quantity){
		Swal.fire({icon: 'error',title: 'Oops...',text: 'The Available quantity of the product is '+mrpAndQuantity.quantity+'!'});
		return;
	}
	delete barcodeMap['.'+$tr.attr('class')];
	$tr.removeAttr('class');
	$tr.addClass($tr.find('input[name=barcode]').val());
	var $editRow = $tr.find('button[id='+i+']');
	$editRow.html('edit');
	$tr.find('input').attr('disabled', 'disabled');
	orderArr[i-1].barcode = $tr.find('input[name=barcode]').val();
	orderArr[i-1].mrp = $tr.find('input[name=mrp]').val();
	orderArr[i-1].quantity = $tr.find('input[name=quantity]').val();
	barcodeMap[$tr.find('select[name=barcode]').val()] = i;
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


