# Animations

Animations are built using several animation types. By chaining
these types, a larger animation can be created. The purpose of
chaining animations is creating fundraiser reveals with an amount
that grows over the course of the animation, which builds excitement
for the final number.

This documentation goes into the components of animations.

## Animation Nodes

Animations are built with nodes. Nodes store the following data:
- `targetAmount`
- `animation`

`targetAmount` is the amount that should be reached when the
animation completes.

`animation` defines the animation to use in this node.
It contains adjustable settings for the animation.

## Animation

Animations define an animation to play, and they include
adjustable settings. All animations have some base settings:
- `startDelay`
- `duration`

`startDelay` is the time in milliseconds to pause before
beginning the animation.

`duration` is the time in milliseconds to play the animation.
This can affect animations differently. For instance, a countup
would count faster or slower while a scramble would be unaffected.

### Animation Types

There are several different animation types that can be found
below. These animation types may include additional settings
based on the animation.

#### Countup

Countup takes the starting amount and increments to the target
amount. The speed that it counts depends on the `duration` of
the animation.

#### Set

Set just sets the amount. It's immediate, so there' not much to
the animation. Setting the `duration` will have no effect.

#### Scramble

Scramble rapidly changes the number for the course of the animation.
Currently, its speed is fixed and cannot be changed.
