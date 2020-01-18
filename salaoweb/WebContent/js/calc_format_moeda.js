function calcular() {
	var strDinheiro = document.getElementById("formPagamento:inDinheiro_input").value;
	var strDebito = document.getElementById("formPagamento:inCartaoDebito_input").value;
	var strCredito = document.getElementById("formPagamento:inCartaoCredito_input").value;
	var strCheque = document.getElementById("formPagamento:inCheque_input").value;
	var strAPagar = document.getElementById("formPagamento:outTotalApagar").innerHTML;
	strAPagar = strAPagar.split(" ")[1];
	var dinheiro = strReplaceAll(strDinheiro, "\.", "");
	dinheiro = strReplaceAll(dinheiro, ",", ".");
	var debito = strReplaceAll(strDebito, "\.", "");
	debito = strReplaceAll(debito, ",", ".");
	var credito = strReplaceAll(strCredito, "\.", "");
	credito = strReplaceAll(credito, ",", ".");
	var cheque = strReplaceAll(strCheque, "\.", "");
	cheque = strReplaceAll(cheque, ",", ".");
	var total = ((Number(dinheiro) + Number(debito) + Number(credito) + Number(cheque)));
	if (total == 0) {
		document.getElementById("formPagamento:outTotalEntrada").innerHTML = "R$ 00,00";
		document.getElementById("formPagamento:outTroco").innerHTML = "R$ 00,00";
		return;
	}
	strAPagar = strReplaceAll(strAPagar, "\.", "");
	strAPagar = strAPagar.replace(",", "\.");
	var valApagar = strAPagar;
	var troco = "" + (total - valApagar);
	pos = total.toString().indexOf("\.");
	if (pos > -1) {
		if(total.toString().split("\.")[1].length == 1){
			total = total.toString().replace("\.", "")+"0";
		}else{
			total = total.toString().replace("\.", "");
		}	
	} else {
		total += "00";
	}
	document.getElementById("formPagamento:outTotalEntrada").innerHTML = formatReal(total);
	pos = troco.indexOf("\.");
	if (pos > -1) {
		if(troco.toString().split("\.")[1].length == 1){
			troco = troco.toString().replace("\.", "")+"0";
		}else{
			troco = troco.toString().replace("\.", "");
		}		
	} else {
		troco += "00";
	}
	document.getElementById("formPagamento:outTroco").innerHTML = formatReal(troco);
}
function strReplaceAll(str, de, para) {
	pos = str.indexOf(de);
	while (pos > -1) {
		str = str.replace(de, para);
		pos = str.indexOf(de);
	}
	return str;
}
function formatReal(strValor) {
	var tmp = strValor;
	tmp = tmp.replace(/([0-9]{2})$/g, ",$1");
	if (tmp.length > 6) {
		tmp = tmp.replace(/([0-9]{3}),([0-9]{2}$)/g, ".$1,$2");
	}
	return "R$ " + tmp;
}