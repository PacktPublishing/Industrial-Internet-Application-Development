var opcua = require("node-opcua");
var min = 1;
var max = 100;

var host = new opcua.OPCUAServer({
  buildInfo: {
    buildDate: new Date(2018, 8, 8),
    buildNumber: "1234",
    productName: "productName",
  },
  port: 4334,
  resourcePath: "UA/resourcePath",
});

host.initialize(function () {
  var space = host.engine.addressSpace;

  var componentOf = space.addObject({
    browseName: "browseName",
    organizedBy: space.rootFolder.objects,
  });

  var variable1 = 0;

  // generate new value
  setInterval(function () {
    variable1 = Math.floor(max - Math.random() * (max - min));
  }, 500);

  space.addVariable({
    browseName: "browseName",
    componentOf: componentOf,
    dataType: "Double",
    nodeId: "ns=1;s=Variable1", // a string nodeID
    value: {
      get: function () {
        return new opcua.Variant({dataType: opcua.DataType.Double, value: variable1});
      },
      set: function (variant) {
        variable1 = parseFloat(variant.value);
        return opcua.StatusCodes.Good;
      }
    }
  });

  host.start(function () {
    var endpoint = host.endpoints[0].endpointDescriptions()[0].endpointUrl;
    console.log("Endpoint: ", endpoint);
  });
});
