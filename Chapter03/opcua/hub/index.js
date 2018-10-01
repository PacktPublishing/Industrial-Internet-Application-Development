var opcua = require("node-opcua");
var async = require("async");
var request = require("request");

var session, subscription;
var client = new opcua.OPCUAClient();
var sensor = "opc.tcp://REMOTE-SENSOR-ADDRESS:4334/UA/resourcePath";
var receiver = "http://REMOTE-SERVER-ADDRESS.com:8080";

async.series(
  [
    // establishing connection
    function (cb) {
      client.connect(sensor, function (err) {
        if (err) {
          console.log("Connection to " + sensor + "failed");
        } else {
          console.log("Connection successful");
        }
        cb(err);
      });
    },

    // start session
    function (cb) {
      client.createSession(function (err, res) {
        if (!err) session = res;
        cb(err);
      });
    },

    // read value
    function (cb) {
      session.readVariableValue("ns=1;s=Variable1", function (err, dataValue) {
        if (!err) console.log("Variable1 = ", dataValue.value.value);
        cb(err);
      });
    },

    // write value
    function (cb) {
      session.writeSingleNode("ns=1;s=Variable1", new opcua.Variant({
        dataType: opcua.DataType.Double,
        value: 100
      }), function (err) {
        cb(err);
      });
    },

    // subscribe to changes
    function (cb) {
      subscription = new opcua.ClientSubscription(session, {
        maxNotificationsPerPublish: 5,
        priority: 5,
        publishingEnabled: true,
        requestedLifetimeCount: 5,
        requestedMaxKeepAliveCount: 3,
        requestedPublishingInterval: 500,
      });

      subscription.on("started", function () {
        console.log("subscription id: ", subscription.subscriptionId);
      }).on("terminated", function () {
        cb();
      });

      setTimeout(function () {
        subscription.terminate();
      }, 5000);

      // install monitored item
      var monitor = subscription.monitor({
          attributeId: opcua.AttributeIds.Value,
          nodeId: opcua.resolveNodeId("ns=1;s=Variable1"),
        },
        {
          discardOldest: true,
          samplingInterval: 50,
          queueSize: 5,
        },
        opcua.read_service.TimestampsToReturn.Both
      );

      monitor.on("changed", function (dataValue) {
        console.log("Variable1 = ", dataValue.value.value);

        // send to receiver
        var data = {
          device: "sensor1",
          timestamp: Date.now(),
          Variable1: dataValue.value.value
        };
        request.post({url: receiver, form: data}, function (err) {
          if (err) console.log("Failed to send " + JSON.stringify(data) + " to " + receiver);
        });
      });
    },

    // close session
    function (cb) {
      session.close(function (err) {
        if (err) console.log("Failed to close session");
        cb();
      });
    }
  ],

  function (err) {
    if (err) {
      console.log("Failed with error:", err);
    } else {
      console.log("Successfully finished");
    }
    client.disconnect(function () {
    });
  }
);
