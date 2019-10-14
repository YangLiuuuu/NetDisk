$(function () {
    getCapacity();
    function getCapacity() {
        var capacity = $("#capacity-hidden").val();
        capacity = parseFloat(capacity);
        if (capacity>1024){
            capacity = capacity/1024;
            $("#capacity").text(capacity.toFixed(2) + "GB");
        }else {
            $("#capacity").text(capacity.toFixed(2)+"MB");
        }
    }
})