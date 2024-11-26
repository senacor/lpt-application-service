# Load Test

## Running Load Test

```bash
docker run --rm -i grafana/k6 run - <{PATH_TO_SCENARIO}/load-script.js
```
When using Github Action to run the load scenario, a timeout could happen when starting the pod ... This will end up in a failing Github Action run