# site-visits
This is a Google data flow project and is created to implement the pipeline for handling the site visit events coming from different sources whether it's batching or streaming. And the event comes in the form of JSON string. 
Pipeline takes the JSON event data and parses it. A composite will take the parsed json data and sums up the timeSpent value by category type.

## Sample JSON Event data
```json
{"site": "Youtube","visits": [{"timeSpent": 7200,"contentType": 1,"contentCategory": 2,"url": "youtube.com/godfather"},{"timeSpent": 1200,"contentType": 2,"contentCategory": 1,"url": "youtube.com/python"},
							  {"timeSpent": 2200,"contentType": 1,"contentCategory": 2,"url": "youtube.com/latenightshow"},
							  {"timeSpent": 3200,"contentType": 2,"contentCategory": 1,"url": "youtube.com/agile-methodology"}]}
```