import boto3
import json

# EventBridge client
EVENT_BUS_NAME = "LiwanagEventBus"

session = boto3.Session(profile_name="jared-liwanag")
eventbridge = session.client("events")

source = "scoring.service"
detail_type = "AnswerEvaluated"
payload = {
    "userId": "e35e4184-f05c-4736-a902-8e0f1479563f",
    "questionId": "67890",
    "activityId": "abcde",
    "result": "correct",
    "xpGained": 300
}
# Sample event
# This is correct and required casing or this API to work; boto3 will
# internally format the event to the EventBridge format.
event_entry = {
    "Source": source,
    "DetailType": detail_type,
    "Detail": json.dumps(payload),
    "EventBusName": EVENT_BUS_NAME
}

test_event_pattern = {
    "version": "0",
    "id": "11111111-1111-1111-1111-111111111111",
    "source": source,
    "account": "123456789012",  # This field is required, but the value can be anything for testing
    "time": "2025-06-08T00:00:00Z",
    "region": "us-west-1",
    "resources": [],
    "detail-type": detail_type,
    "detail": json.dumps(payload)
}

test_response = eventbridge.test_event_pattern(
    EventPattern=json.dumps({
        "source": ["scoring.service"],
        "detail-type": ["AnswerEvaluated"]
    }),
    Event=json.dumps(test_event_pattern)
)
if not test_response["Result"]:
    raise ValueError("Event pattern does not match the event entry.")

# Send the event to EventBridge
response = eventbridge.put_events(Entries=[event_entry])

print("Event sent:")
print(response)