import json


class aggregator:
    def __init__(self):
        print "Create aggregator"

    def sum(self, data):
        sum = 0
        timeseries = json.loads(data).get("timeseries")
        for item in timeseries:
            sum += item[1]
        return {"result": sum}
