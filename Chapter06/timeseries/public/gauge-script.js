$(document).ready(function () {
  var stats = [{}];
  var gauges = {};
  var min = 0;
  var max = 100;
  var interval = 1000;

  var gaugeOptions = {
    chart: {
      type: 'solidgauge'
    },

    credits: {
      enabled: false
    },

    title: null,

    pane: {
      center: ['50%', '85%'],
      size: '140%',
      startAngle: -90,
      endAngle: 90,
      background: {
        backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
        innerRadius: '60%',
        outerRadius: '100%',
        shape: 'arc'
      }
    },

    tooltip: {
      enabled: false
    },

    // the value axis
    yAxis: {
      stops: [
        [0.1, '#55BF3B'], // green
        [0.5, '#DDDF0D'], // yellow
        [0.9, '#DF5353'] // red
      ],
      lineWidth: 0,
      minorTickInterval: null,
      tickAmount: 2,
      title: {
        y: -70
      },
      labels: {
        y: 16
      }
    },

    plotOptions: {
      solidgauge: {
        dataLabels: {
          y: 5,
          borderWidth: 0,
          useHTML: true
        }
      }
    }
  };

  var gaugeTypes = [
    {name: 'count', suffix: ' changes'},
    {name: 'mean', suffix: ' rpm'},
    {name: 'median', suffix: ' rpm'},
    {name: 'mode', suffix: ' rpm'},
    {name: 'spread', suffix: ' rpm'},
    {name: 'stddev', suffix: ' rpm'},
    {name: 'sum', suffix: ' rpm'},
    {name: 'first', suffix: ' rpm'},
    {name: 'last', suffix: ' rpm'},
    {name: 'max', suffix: ' rpm'},
    {name: 'min', suffix: ' rpm'},
    {name: 'percentile', suffix: ' rpm'}
  ];

  for (var i = 0; i < gaugeTypes.length; i++) {
    var name = gaugeTypes[i].name;
    var suffix = gaugeTypes[i].suffix;
    gauges[name] = Highcharts.chart('gauge-container-' + name, Highcharts.merge(gaugeOptions, {
      yAxis: {
        min: min,
        max: max,
        title: {
          text: name
        }
      },

      series: [{
        name: name,
        data: [0],
        dataLabels: {
          format: '<div style="text-align:center"><span style="font-size:25px;color:' +
          ((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y}</span><br/>' +
          '<span style="font-size:12px;color:silver">' + suffix + '</span></div>'
        },
        tooltip: {
          valueSuffix: suffix
        }
      }]
    }));
  }

  // Bring life to the dials
  setInterval(function () {
    $.getJSON('http://localhost:8080/stats', function (response) {
      stats = response;
      for (var i = 0; i < gaugeTypes.length; i++) {
        var name = gaugeTypes[i].name;
        if (!gauges[name]) return;
        var newVal = stats[0][name + '_variable1'];
        if (!newVal) return;
        newVal = parseFloat(newVal.toFixed(2));
        var point = gauges[name].series[0].points[0];
        point.update(newVal);
      }
    });
  }, interval);
});
