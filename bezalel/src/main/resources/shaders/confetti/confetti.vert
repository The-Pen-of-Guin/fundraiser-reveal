#version 330 core
layout(location=0) in vec2 aPos;
layout(location=1) in vec3 aInstance; // x, y, rot
layout(location=2) in vec4 aColor;

uniform mat4 projection;
out vec4 vColor;

void main() {
    float c = cos(aInstance.z), s = sin(aInstance.z);
    vec2 rotated = vec2(aPos.x*c - aPos.y*s, aPos.x*s + aPos.y*c) * 10.0; // scale to size
    vec2 worldPos = rotated + aInstance.xy;
    gl_Position = projection * vec4(worldPos, 0.0, 1.0);
    vColor = aColor;
}
