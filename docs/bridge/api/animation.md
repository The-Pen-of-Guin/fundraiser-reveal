# Animation API

An API is defined for high level control of the animation.
This includes playing the animation, saving it as a video, etc.

## Play Animation

Plays the animation in a generated window.

> The animation played is based on the nodes that have
been added.

`GET /api/v1/animation/play`

## Save Animation

Saves the animation to a video file.

Only MP4 will be supported for now.

`POST /api/v1/animation/save`

Request Body:
```java
{
    "filename": "string",
    "filetype": "string"
}
```
