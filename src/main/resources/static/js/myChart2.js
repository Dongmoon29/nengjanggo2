var chartDataStr = decodeHtml(brandChartData);
var chartJsonArray = JSON.parse(chartDataStr);

var arrayLength = chartJsonArray.length;

var numericData = [];
var labelData = [];

for(var i=0; i < arrayLength; i++){
    numericData[i] = chartJsonArray[i].brandCount;
    labelData[i] = chartJsonArray[i].brandName;
}

// For a pie chart
new Chart(document.getElementById("myPieChart2"), {
    type: 'pie',
    // The data for our dataset
    data: {
        labels: labelData,
        datasets: [{
            label: 'My Second dataset',
            backgroundColor: ["#3e95cd","#8e5ea2","#3cba9f","#800e2a","#451a4f","#ccc923","#ccc923","#807f65"],
            data: numericData
        }]
    },

    // Configuration options go here
    options: {
        title: {
            display: true,
            text: '내 냉장고 음식들 브랜드 현황'
        }

    }
});



function decodeHtml(html){
    var txt = document.createElement("textarea");
    txt.innerHTML = html;
    return txt.value;
}