//#type vertex
#version 330 core
layout (location=0) in vec2 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexIndex;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoords;
out float texIndex;

void main()
{
    fColor = aColor;
    fTexCoords = aTexCoords;
    texIndex = aTexIndex;

    gl_Position = uProjection * uView * vec4(aPos, 0.0, 1.0);
}

//#type fragment
#version 330 core

uniform sampler2D TEX_SAMPLER[16];

in vec4 fColor;
in vec2 fTexCoords;
in float texIndex;

out vec4 color;

void main()
{
    int id = int(texIndex);
    color = texture(TEX_SAMPLER[id], fTexCoords);
}