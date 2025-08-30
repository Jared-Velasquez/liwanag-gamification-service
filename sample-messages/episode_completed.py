import boto3
import json

# EventBridge client
EVENT_BUS_NAME = "LiwanagEventBus"
session = boto3.Session(profile_name="jared-liwanag")
eventbridge = session.client("events")

source = "progress.tracker"
detail_type = "EpisodeCompleted"

payload = {
    "userId": "e35e4184-f05c-4736-a902-8e0f1479563f",
    "episodeId": "b",
    "unitId": "c",
    "isFirstCompletion": True,
    "timestamp": "2025-08-23T12:34:56.789Z"
}

event_entry = {
    "Source": source,
    "DetailType": detail_type,
    "Detail": json.dumps(payload),
    "EventBusName": EVENT_BUS_NAME
}

# Send the event to EventBridge
response = eventbridge.put_events(Entries=[event_entry])

print("Event sent:")
print(response)