from analytic import aggregator

if __name__ == "__main__":
    a = aggregator()
    assert a.sum('{"timeseries": [[1501664960967,12,3],[1501664961973,48,3]]}') == {"result": 60}
