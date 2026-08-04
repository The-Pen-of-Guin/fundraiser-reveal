# Node API

An API is defined for interacting with AnimationNodes. The API
is defined below.

## Append Node

Appends a node to the end of the animation.

`POST /api/v1/node/append`

### Request Body
```json
{
    "targetAmountCents": int,
    "animation": {
        "animationType": "string",
        "startDelayMs": int,
        "durationMs": int,
        "additionalProperties": []
    }
}
```

- targetAmountCents - Amount to end at in cents.
- animation - Object defining the animation to perform.
  - animationType - Type of animation.
  - startDelayMs - Milliseconds to delay the animation.
  - durationMs - Milliseconds to play the animation for.
  - additionalProperties - Additional fields specific to the animation.

## Add Node

Adds a node at the specified position in the animation.

`POST /api/v1/node/add/{position}`

### Params

- position - Position in animation to add node. Nodes at that position
and beyond will be pushed later.

### Request Body
```json
{
    "targetAmountCents": int,
    "animation": {
        "animationType": "string",
        "startDelayMs": int,
        "durationMs": int,
        "additionalProperties": []
    }
}
```

- targetAmountCents - Amount to end at in cents.
- animation - Object defining the animation to perform.
  - animationType - Type of animation.
  - startDelayMs - Milliseconds to delay the animation.
  - durationMs - Milliseconds to play the animation for.
  - additionalProperties - Additional fields specific to the animation.

## Delete Node

Delete a node at a specified position from the animation.

`DEL /api/v1/node/{position}`
