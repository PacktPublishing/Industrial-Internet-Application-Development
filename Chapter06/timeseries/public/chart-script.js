$(document).ready(function () {
  var stats = [{}];
  var alertFrom = 90;
  var interval = 1000;

  $.getJSON('http://localhost:8080/last', function (response) {
    var data = [];
    for (var i = 0; i < response.length; i++) {
      data.push({
        x: new Date(response[i].time).getTime() * 1000,
        y: response[i].variable1
      });
    }

    Highcharts.chart('chart-container', {
      chart: {
        type: 'spline',
        animation: Highcharts.svg, // don't animate in old IE
        marginRight: 10,
        events: {
          load: function () {
            // set up the updating of the chart each second
            var series = this.series[0];
            setInterval(function () {
              $.getJSON('http://localhost:8080/stats', function (response) {
                stats = response;
                var x = (new Date()).getTime() * 1000;
                var y = response[0].last_variable1;
                series.addPoint([x, y], true, data.length > 300);

                if(y >= alertFrom) toastr['warning']('variable1 exceeded ' + y + ' rpm');
              });
            }, interval);
          }
        }
      },
      credits: {
        enabled: false
      },
      title: {
        text: 'sensor1'
      },
      xAxis: {
        type: 'datetime',
        tickPixelInterval: 150
      },
      yAxis: {
        title: {
          text: 'variable1'
        },
        plotLines: [{
          value: 0,
          width: 1,
          color: '#808080'
        }]
      },
      tooltip: {
        formatter: function () {
          return '<b>' + this.series.name + '</b><br/>' +
            Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
            Highcharts.numberFormat(this.y, 2);
        }
      },
      legend: {
        enabled: false
      },
      series: [{
        name: 'variable1',
        data: data,
        zones: [{
          value: alertFrom
        }, {
          color: 'red'
        }]
      }]
    });
  });
});
